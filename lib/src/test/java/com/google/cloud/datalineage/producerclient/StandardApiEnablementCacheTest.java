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

import com.google.cloud.datalineage.producerclient.ApiEnablementCacheOptions;
import com.google.cloud.datalineage.producerclient.StandardApiEnablementCache;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

/** * Test suite for StandardApiEnablementCache */
@RunWith(JUnit4.class)
public class StandardApiEnablementCacheTest {
  private static final LocalDateTime BASE_DATE = LocalDateTime.of(1989, 1, 13, 0, 0);

  private StandardApiEnablementCache standardApiEnablementCache;
  private Clock clock;

  @Before
  public void init() {
    clock = Mockito.mock(Clock.class);
    standardApiEnablementCache =
        new StandardApiEnablementCache(
            ApiEnablementCacheOptions.newBuilder().setClock(clock).build());
    setupTime(BASE_DATE);
  }

  @Test
  public void baseCase_allowsToMarkAndCheckServiceDisabilityForGivenProject() {
    String projectId1 = "[project1]";
    String projectId2 = "[project2]";
    String projectId3 = "[project3]";
    Duration project1DurationExpired = Duration.ofMillis(10);
    Duration project2DurationExpired = Duration.ofHours(12);
    Duration project3DurationExpired = Duration.ofDays(10);
    standardApiEnablementCache.markServiceAsDisabled(projectId1, project1DurationExpired);
    standardApiEnablementCache.markServiceAsDisabled(projectId2, project2DurationExpired);
    standardApiEnablementCache.markServiceAsDisabled(projectId3, project3DurationExpired);

    assertNoStateChange(projectId1, BASE_DATE, true);
    assertNoStateChange(projectId2, BASE_DATE, true);
    assertNoStateChange(projectId3, BASE_DATE, true);

    assertServiceEnablingTime(projectId1, BASE_DATE.plus(project1DurationExpired));
    assertNoStateChange(projectId2, BASE_DATE.plus(project1DurationExpired), true);
    assertNoStateChange(projectId3, BASE_DATE.plus(project1DurationExpired), true);

    assertNoStateChange(projectId1, BASE_DATE.plus(project2DurationExpired), false);
    assertServiceEnablingTime(projectId2, BASE_DATE.plus(project2DurationExpired));
    assertNoStateChange(projectId3, BASE_DATE.plus(project2DurationExpired), true);

    assertNoStateChange(projectId1, BASE_DATE.plus(project3DurationExpired), false);
    assertNoStateChange(projectId2, BASE_DATE.plus(project3DurationExpired), false);
    assertServiceEnablingTime(projectId3, BASE_DATE.plus(project3DurationExpired));
  }

  @Test
  public void defaultsTimeOfServerDisabilityTo5Minutes() {
    String projectId = "[project]";
    standardApiEnablementCache.markServiceAsDisabled(projectId);

    assertServiceEnablingTime(projectId, BASE_DATE.plus(Duration.ofMinutes(5)));
  }

  @Test
  public void storesOnlyLastSetLockTime() {
    String projectId = "[project]";
    Duration longerDurationExpired = Duration.ofMinutes(10);
    Duration shorterDurationExpired = Duration.ofMinutes(3);

    setupTime(BASE_DATE);
    standardApiEnablementCache.markServiceAsDisabled(projectId, longerDurationExpired);
    assertServiceEnablingTime(projectId, BASE_DATE.plus(longerDurationExpired));
    assertNoStateChange(projectId, BASE_DATE.plus(shorterDurationExpired), true);

    setupTime(BASE_DATE);
    standardApiEnablementCache.markServiceAsDisabled(projectId, shorterDurationExpired);
    assertNoStateChange(projectId, BASE_DATE.plus(longerDurationExpired), false);
    assertServiceEnablingTime(projectId, BASE_DATE.plus(shorterDurationExpired));
  }

  @Test
  public void cachesLimitedNumberOfProjectsAndAllowsToSetALimit() {
    int limit = 5;
    ApiEnablementCacheOptions options =
        ApiEnablementCacheOptions.newBuilder()
            .setDefaultCacheDisabledStatusTime(Duration.ofMinutes(5))
            .setClock(clock)
            .setCacheSize(limit)
            .build();
    standardApiEnablementCache = new StandardApiEnablementCache(options);

    for (int i = 0; i < limit + 1; i++) {
      standardApiEnablementCache.markServiceAsDisabled("[project" + i + "]");
    }

    int presentProjects = 0;
    for (int i = 0; i < limit + 1; i++) {
      presentProjects +=
          standardApiEnablementCache.isServiceMarkedAsDisabled("[project" + i + "]") ? 1 : 0;
    }
    Assert.assertTrue(presentProjects <= limit);
  }

  private void setupTime(LocalDateTime time) {
    Clock fixedClock =
        Clock.fixed(time.toInstant(OffsetDateTime.now().getOffset()), ZoneId.systemDefault());
    Mockito.doReturn(fixedClock.instant()).when(clock).instant();
    Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
  }

  private void assertServiceEnablingTime(String projectId, LocalDateTime changeTime) {
    setupTime(changeTime);
    boolean result = standardApiEnablementCache.isServiceMarkedAsDisabled(projectId);
    Assert.assertTrue(result);

    setupTime(changeTime.plus(Duration.ofMillis(1)));
    result = standardApiEnablementCache.isServiceMarkedAsDisabled(projectId);
    Assert.assertFalse(result);
  }

  private void assertNoStateChange(String projectId, LocalDateTime changeTime, boolean state) {
    setupTime(changeTime);
    boolean result = standardApiEnablementCache.isServiceMarkedAsDisabled(projectId);
    Assert.assertEquals(result, state);

    setupTime(changeTime.plus(Duration.ofMillis(1)));
    result = standardApiEnablementCache.isServiceMarkedAsDisabled(projectId);
    Assert.assertEquals(result, state);
  }
}
