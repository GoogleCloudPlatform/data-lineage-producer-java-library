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
import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class ProjectStatusCacheTest {

  private static final String CACHE_NAME = "Test Cache";

  private static final LocalDateTime BASE_DATE = LocalDateTime.of(1989, 1, 13, 0, 0);

  private ProjectStatusCache cache;
  private Clock clock;

  @Before
  public void init() {
    clock = Mockito.mock(Clock.class);
    cache = new ProjectStatusCache(CacheOptions.newBuilder().setClock(clock).build(), CACHE_NAME);
    setupTime(BASE_DATE);
  }

  @Test
  public void isProjectDisabled_withoutMarking_returnsFalse() {
    assertThat(cache.isProjectDisabled("random-project")).isFalse();
  }

  @Test
  public void isProjectDisabled_markDisabled_withoutMarking_returnsFalse() {
    cache.markProjectAsDisabled("random-project", Duration.ZERO);
    setupTime(BASE_DATE.plus(Duration.ofMillis(1)));
    assertThat(cache.isProjectDisabled("random-project")).isFalse();
  }

  @Test
  public void isProjectDisabled_respectsDuration() {
    String projectId = "test-project";
    Duration expireDuration = Duration.ofMillis(10);

    cache.markProjectAsDisabled(projectId, expireDuration);

    assertNoStateChangeAtTime(projectId, BASE_DATE);
    assertStateChangedAtTime(projectId, BASE_DATE.plus(expireDuration));
  }

  @Test
  public void isProjectDisabled_respectsDefaultDuration() {

    String projectId = "test-project";

    cache.markProjectAsDisabled(projectId);

    assertNoStateChangeAtTime(projectId, BASE_DATE);
    assertStateChangedAtTime(projectId, BASE_DATE.plus(Duration.ofMinutes(5)));
  }

  @Test
  public void isProjectDisabled_onlyLastEntryInCacheIsRespected() {
    String projectId = "test-project";
    Duration oldDuration = Duration.ofMillis(10);

    cache.markProjectAsDisabled(projectId, oldDuration);
    Duration newDuration = Duration.ofMillis(5);
    cache.markProjectAsDisabled(projectId, newDuration);

    assertNoStateChangeAtTime(projectId, BASE_DATE);
    assertStateChangedAtTime(projectId, BASE_DATE.plus(newDuration));
    assertNoStateChangeAtTime(projectId, BASE_DATE.plus(oldDuration));
  }

  @Test
  public void markProjectAsDisabled_respectsSize() {
    ImmutableList<String> projects = ImmutableList.of("project1", "project2", "project3");
    int cacheSize = projects.size() - 1;
    cache = new ProjectStatusCache(
        CacheOptions.newBuilder().setCacheSize(cacheSize).build(),
        CACHE_NAME);

    projects.forEach(p -> cache.markProjectAsDisabled(p));

    long projectsInCache = projects.stream().filter(p -> cache.isProjectDisabled(p)).count();
    assertThat(projectsInCache).isEqualTo(cacheSize);
  }

  /**
   * Asserts that there was no change in state for a project before and after a given point in
   * time.
   */
  private void assertNoStateChangeAtTime(String projectId, LocalDateTime time) {
    setupTime(time);
    boolean before = cache.isProjectDisabled(projectId);

    setupTime(time.plus(Duration.ofMillis(1)));
    boolean after = cache.isProjectDisabled(projectId);
    assertEquals(before, after);
  }

  /**
   * Asserts that the state was flipped for a project at a given point in time.
   */
  private void assertStateChangedAtTime(String projectId, LocalDateTime time) {
    setupTime(time);
    boolean before = cache.isProjectDisabled(projectId);

    setupTime(time.plus(Duration.ofMillis(1)));
    boolean after = cache.isProjectDisabled(projectId);
    assertEquals(before, !after);
  }

  private void setupTime(LocalDateTime time) {
    Clock fixedClock = Clock.fixed(time.toInstant(OffsetDateTime.now().getOffset()),
        ZoneId.systemDefault());
    Mockito.doReturn(fixedClock.instant()).when(clock).instant();
    Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
  }
}
