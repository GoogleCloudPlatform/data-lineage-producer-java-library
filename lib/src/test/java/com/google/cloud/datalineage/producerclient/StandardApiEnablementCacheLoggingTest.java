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

package com.google.cloud.datalineage.producerclient;

import static com.google.common.truth.Truth.assertThat;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.google.cloud.datalineage.producerclient.test.TestLogAppender;
import java.time.Clock;
import java.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.LoggerFactory;

/** Tests logging functionality in StandardApiEnablementCache. */
@RunWith(JUnit4.class)
public class StandardApiEnablementCacheLoggingTest {

  private TestLogAppender testAppender;
  private Logger logger;
  private StandardApiEnablementCache cache;

  @Before
  public void setUp() {
    // Set up logging capture
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    logger = loggerContext.getLogger(StandardApiEnablementCache.class);

    testAppender = new TestLogAppender();
    testAppender.setContext(loggerContext);
    testAppender.start();

    logger.addAppender(testAppender);
    logger.setLevel(Level.DEBUG); // Enable debug logging for tests

    ApiEnablementCacheOptions options =
        ApiEnablementCacheOptions.newBuilder()
            .setCacheSize(100)
            .setDefaultCacheDisabledStatusTime(Duration.ofMinutes(5))
            .setClock(Clock.systemDefaultZone())
            .build();

    cache = new StandardApiEnablementCache(options);
  }

  @After
  public void tearDown() {
    if (logger != null && testAppender != null) {
      logger.detachAppender(testAppender);
    }
    if (testAppender != null) {
      testAppender.stop();
    }
  }

  @Test
  public void testCacheInitializationLogging() {
    // Verify that cache initialization is logged
    assertThat(testAppender.getMessagesAtLevel(Level.DEBUG)).contains(
        "Initializing StandardApiEnablementCache with cache size: 100, "
            + "default disabled duration: PT5M");
  }

  @Test
  public void testMarkServiceAsDisabledLogging() {
    testAppender.clear(); // Clear logs from setup

    String projectName = "test-project";
    Duration duration = Duration.ofMinutes(10);

    cache.markServiceAsDisabled(projectName, duration);

    // Verify that marking service as disabled is logged
    assertThat(testAppender.getMessagesAtLevel(Level.WARN)).contains(
        "Marking service as disabled for project 'test-project'"
            + " for duration: PT10M");
  }

  @Test
  public void testIsServiceMarkedAsDisabledLogging_NotFound() {
    testAppender.clear(); // Clear logs from setup

    String projectName = "non-existent-project";

    boolean result = cache.isServiceMarkedAsDisabled(projectName);

    assertThat(result).isFalse();
    assertThat(testAppender.getMessagesAtLevel(Level.DEBUG)).contains(
        "No cache entry found for project: non-existent-project");
  }

  @Test
  public void testIsServiceMarkedAsDisabledLogging_Found() {
    // First mark the service as disabled
    cache.markServiceAsDisabled("test-project", Duration.ofMinutes(5));

    // Clear logs to focus on the check operation
    testAppender.clear();

    boolean result = cache.isServiceMarkedAsDisabled("test-project");

    assertThat(result).isTrue();
    boolean found =
        testAppender.getMessagesAtLevel(Level.DEBUG).stream()
            .anyMatch(
                log ->
                    log.contains("Service is marked as disabled for project: test-project until"));
    assertThat(found).isTrue();
  }

  @Test
  public void testIsServiceMarkedAsDisabledLogging_Expired() {
    // Mark service as disabled for a very short duration
    cache.markServiceAsDisabled("test-project", Duration.ofNanos(1));

    // Wait a bit to ensure expiration
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    // Clear logs to focus on the check operation
    testAppender.clear();

    boolean result = cache.isServiceMarkedAsDisabled("test-project");

    assertThat(result).isFalse();
    assertThat(testAppender.getMessagesAtLevel(Level.DEBUG)).contains(
        "Service disability has expired for project: test-project");
  }
}
