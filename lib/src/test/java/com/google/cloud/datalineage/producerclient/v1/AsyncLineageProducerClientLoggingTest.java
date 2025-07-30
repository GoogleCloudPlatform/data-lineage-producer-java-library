// Copyright 2024 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.cloud.datalineage.producerclient.v1;

import static com.google.common.truth.Truth.assertThat;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.google.cloud.datacatalog.lineage.v1.DeleteLineageEventRequest;
import com.google.cloud.datacatalog.lineage.v1.GetLineageEventRequest;
import com.google.cloud.datacatalog.lineage.v1.ProcessOpenLineageRunEventRequest;
import com.google.cloud.datalineage.producerclient.test.TestLogAppender;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Duration;

/** Tests logging functionality in AsyncLineageProducerClient. */
@RunWith(JUnit4.class)
public class AsyncLineageProducerClientLoggingTest {
  private TestLogAppender testAppender;
  private Logger logger;
  private AsyncLineageProducerClient client;

  @Before
  public void setUp() throws IOException {
    // Set up logging capture
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    logger = loggerContext.getLogger(AsyncLineageProducerClient.class);

    testAppender = new TestLogAppender();
    testAppender.setContext(loggerContext);
    testAppender.start();

    logger.addAppender(testAppender);
    logger.setLevel(Level.DEBUG); // Enable debug logging for tests
    // Create client with settings for testing
    AsyncLineageProducerClientSettings settings =
        AsyncLineageProducerClientSettings.newBuilder()
            .setGracefulShutdownDuration(Duration.ofSeconds(1))
            .build();
    client = AsyncLineageProducerClient.create(settings);
  }

  @After
  public void tearDown() {
    if (client != null) {
      client.shutdownNow();
    }
    if (logger != null && testAppender != null) {
      logger.detachAppender(testAppender);
    }
    if (testAppender != null) {
      testAppender.stop();
    }
  }

  @Test
  public void testClientCreationLogging() {
    // Verify that client creation is logged
    assertThat(testAppender.getMessagesAtLevel(Level.DEBUG))
        .contains("Creating AsyncLineageProducerClient with graceful shutdown duration: PT1S");
  }

  @Test
  public void testDeleteLineageEventLogging() {
    testAppender.clear(); // Clear logs from setup

    DeleteLineageEventRequest request =
        DeleteLineageEventRequest.newBuilder()
            .setName(
                "projects/test-project/locations/us-central1/processes/test-process/runs/test-run/lineageEvents/test-event")
            .build();
    try {
      client.deleteLineageEvent(request);
    } catch (Exception e) {
      // Expected to fail in test environment, we're just testing logging
    }
    // Verify the debug log was created
    boolean found =
        testAppender.getMessagesAtLevel(Level.DEBUG).stream()
            .anyMatch(
                log ->
                    log.contains(
                        "Deleting lineage event: projects/test-project/locations/us-central1/processes/test-process/runs/test-run/lineageEvents/test-event"));
    assertThat(found).isTrue();
  }

  @Test
  public void testGetLineageEventLogging() {
    testAppender.clear(); // Clear logs from setup

    GetLineageEventRequest request =
        GetLineageEventRequest.newBuilder()
            .setName(
                "projects/test-project/locations/us-central1/processes/test-process/runs/test-run/lineageEvents/test-event")
            .build();
    try {
      client.getLineageEvent(request);
    } catch (Exception e) {
      // Expected to fail in test environment, we're just testing logging
    }
    // Verify the debug log was created
    boolean found =
        testAppender.getMessagesAtLevel(Level.DEBUG).stream()
            .anyMatch(
                log ->
                    log.contains(
                        "Getting lineage event: projects/test-project/locations/us-central1/processes/test-process/runs/test-run/lineageEvents/test-event"));
    assertThat(found).isTrue();
  }

  @Test
  public void testProcessOpenLineageRunEventLogging() {
    testAppender.clear(); // Clear logs from setup

    ProcessOpenLineageRunEventRequest request =
        ProcessOpenLineageRunEventRequest.newBuilder()
            .setParent("projects/test-project/locations/us-central1")
            .build();
    try {
      client.processOpenLineageRunEvent(request);
    } catch (Exception e) {
      // Expected to fail in test environment, we're just testing logging
    }
    // Verify the debug log was created
    boolean found =
        testAppender.getMessagesAtLevel(Level.DEBUG).stream()
            .anyMatch(log -> log.contains("Processing OpenLineage run event:"));
    assertThat(found).isTrue();
  }

  @Test
  public void testGracefulShutdownLogging() throws Exception {
    testAppender.clear(); // Clear logs from setup

    // Test graceful shutdown logging
    client.close();
    // Verify shutdown logging
    boolean found =
        testAppender.getMessagesAtLevel(Level.DEBUG).stream()
            .anyMatch(log -> log.contains("Starting graceful shutdown with duration: PT1S"));
    assertThat(found).isTrue();
  }

  @Test
  public void testShutdownTimeoutLogging() throws Exception {
    // Create a client with very short timeout to trigger timeout warning
    AsyncLineageProducerClientSettings shortTimeoutSettings =
        AsyncLineageProducerClientSettings.newBuilder()
            .setGracefulShutdownDuration(Duration.ofMillis(1)) // Very short timeout
            .build();
    AsyncLineageProducerClient shortTimeoutClient =
        AsyncLineageProducerClient.create(shortTimeoutSettings);

    // Start some background operations to create work that needs shutdown
    // This creates background threads and operations that need time to shut down
    for (int i = 0; i < 10; i++) {
      try {
        GetLineageEventRequest request =
            GetLineageEventRequest.newBuilder()
                .setName(
                    "projects/test-project/locations/us-central1/processes/test-process/runs/test-run/lineageEvents/test-event-"
                        + i)
                .build();
        shortTimeoutClient.getLineageEvent(request);
      } catch (Exception e) {
        // Ignore exceptions, we just want to create background work
      }
    }

    testAppender.clear(); // Clear logs from setup

    try {
      shortTimeoutClient.close();
    } catch (Exception e) {
      // Ignore any exceptions, we're testing logging
    }

    // Verify timeout warning was logged
    boolean found =
        testAppender.getMessagesAtLevel(Level.WARN).stream()
            .anyMatch(
                log ->
                    log.contains(
                        "AsyncLineageProducerClient did not terminate within the"
                            + " graceful shutdown duration"));
    assertThat(found).isTrue();

    shortTimeoutClient.shutdownNow();
  }
}
