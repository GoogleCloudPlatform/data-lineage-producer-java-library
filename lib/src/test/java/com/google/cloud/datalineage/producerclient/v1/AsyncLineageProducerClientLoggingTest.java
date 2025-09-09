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
import com.google.cloud.datacatalog.lineage.v1.ListProcessesRequest;
import com.google.cloud.datacatalog.lineage.v1.ProcessOpenLineageRunEventRequest;
import com.google.cloud.datalineage.producerclient.test.TestLogAppender;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Duration;

/** Tests logging functionality in AsyncLineageProducerClient. */
@RunWith(JUnit4.class)
public class AsyncLineageProducerClientLoggingTest {

  private TestLogAppender testAppender;
  private Logger logger;
  private final BasicLineageClient basicLineageClient = Mockito.mock(BasicLineageClient.class);
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
    client = AsyncLineageProducerClient.create(basicLineageClient);
    testAppender.clear(); // Clear logs from setup
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
  public void testDeleteLineageEventLogging() {
    DeleteLineageEventRequest request =
        DeleteLineageEventRequest.newBuilder()
            .setName(
                "projects/test-project/locations/us-central1/"
                    + "processes/test-process/runs/test-run/lineageEvents/test-event")
            .build();
    try {
      client.deleteLineageEvent(request);
    } catch (Exception e) {
      // Expected to fail in test environment, we're just testing logging
    }
    // Verify the debug log was created
    assertThat(testAppender.getMessagesAtLevel(Level.DEBUG))
        .contains(
            "Deleting lineage event: projects/test-project/locations/us-central1/processes/test-process/runs/test-run/lineageEvents/test-event");
  }

  @Test
  public void testGetLineageEventLogging() {
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
    assertThat(testAppender.getMessagesAtLevel(Level.DEBUG))
        .contains(
            "Getting lineage event: projects/test-project/locations/us-central1/processes/test-process/runs/test-run/lineageEvents/test-event");
  }

  @Test
  public void testProcessOpenLineageRunEventLogging() {
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
    assertThat(testAppender.getMessagesAtLevel(Level.DEBUG))
        .contains("Processing OpenLineage run event: ");
  }

  @Test
  public void testListProcessesBasicLogging() {
    ListProcessesRequest request =
        ListProcessesRequest.newBuilder()
            .setParent("projects/test-project/locations/us-central1")
            .build();
    try {
      client.listProcesses(request);
    } catch (Exception e) {
      // Expected to fail in test environment, we're just testing logging
    }
    // Verify the debug log was created
    assertThat(testAppender.getMessagesAtLevel(Level.DEBUG))
        .contains("Listing processes for parent: " + "projects/test-project/locations/us-central1");
  }

  @Test
  public void testListProcessesWithParametersLogging() {
    ListProcessesRequest request =
        ListProcessesRequest.newBuilder()
            .setParent("projects/test-project/locations/us-central1")
            .setPageSize(25)
            .setPageToken("advanced-page-token")
            .build();
    try {
      client.listProcesses(request);
    } catch (Exception e) {
      // Expected to fail in test environment, we're just testing logging
    }
    // Verify the debug log includes parent (parameters are not logged individually)
    assertThat(testAppender.getMessagesAtLevel(Level.DEBUG))
        .contains("Listing processes for parent: projects/test-project/locations/us-central1");
  }

  @Test
  public void testDefaultGracefulShutdownLogging() throws Exception {
    // Test graceful shutdown logging
    client.close();
    // Verify shutdown logging
    assertThat(testAppender.getMessagesAtLevel(Level.DEBUG))
        .contains("Starting graceful shutdown with duration: PT30S");
  }

  @Test
  public void testHardShutdownLogging() throws Exception {
    // Create a client with zero timeout to trigger the warning
    AsyncLineageProducerClientSettings zeroTimeoutSettings =
        AsyncLineageProducerClientSettings.newBuilder()
            .setGracefulShutdownDuration(Duration.ZERO)
            .build();
    AsyncLineageProducerClient hardShutdownClient =
        AsyncLineageProducerClient.create(basicLineageClient, zeroTimeoutSettings);

    hardShutdownClient.close();

    // Verify hard-shutdown message is printed
    assertThat(testAppender.getMessagesAtLevel(Level.WARN))
        .contains(
            "AsyncLineageProducerClient graceful shutdown duration was set to zero. This effectively means hard shutdown");
  }

  @Test
  public void testShutdownTimeoutLogging() throws Exception {
    // Create a client with very short timeout to trigger timeout warning
    AsyncLineageProducerClientSettings shortTimeoutSettings =
        AsyncLineageProducerClientSettings.newBuilder()
            .setGracefulShutdownDuration(Duration.ofMillis(1)) // Very short timeout
            .build();
    AsyncLineageProducerClient shortTimeoutClient =
        AsyncLineageProducerClient.create(basicLineageClient, shortTimeoutSettings);

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

    try {
      shortTimeoutClient.close();
    } catch (Exception e) {
      // Ignore any exceptions, we're testing logging
    }

    // Verify timeout warning was logged
    assertThat(testAppender.getMessagesAtLevel(Level.WARN))
        .contains(
            "AsyncLineageProducerClient did not terminate within the"
                + " graceful shutdown duration");
    shortTimeoutClient.shutdownNow();
  }
}
