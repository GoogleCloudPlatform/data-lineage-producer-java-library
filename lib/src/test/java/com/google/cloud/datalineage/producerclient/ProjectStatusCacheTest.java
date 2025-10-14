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

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProjectStatusCacheTest {
  private static final String PROJECT_ID = "test-project";
  private static final String CACHE_NAME = "Test Cache";

  @Test
  public void isProjectDisabled_withoutMarking_returnsFalse() {
    ProjectStatusCache cache = new ProjectStatusCache(CacheOptions.newBuilder().build(), CACHE_NAME);
    assertThat(cache.isProjectDisabled(PROJECT_ID)).isFalse();
  }

  @Test
  public void isProjectDisabled_afterMarking_returnsTrue() {
    ProjectStatusCache cache = new ProjectStatusCache(CacheOptions.newBuilder().build(), CACHE_NAME);
    cache.markProjectAsDisabled(PROJECT_ID);
    assertThat(cache.isProjectDisabled(PROJECT_ID)).isTrue();
  }

  @Test
  public void isProjectDisabled_afterMarkingWithDuration_returnsTrue() {
    ProjectStatusCache cache = new ProjectStatusCache(CacheOptions.newBuilder().build(), CACHE_NAME);
    cache.markProjectAsDisabled(PROJECT_ID, Duration.ofMinutes(5));
    assertThat(cache.isProjectDisabled(PROJECT_ID)).isTrue();
  }

  @Test
  public void isProjectDisabled_afterDurationPasses_returnsFalse() {
    // Use a fixed clock to control time
    Clock fixedClock = Clock.fixed(Instant.EPOCH, ZoneId.systemDefault());
    Duration duration = Duration.ofMinutes(5);

    CacheOptions options =
        CacheOptions.newBuilder()
            .setDefaultCacheDisabledStatusTime(duration)
            .setClock(fixedClock)
            .build();

    ProjectStatusCache cache = new ProjectStatusCache(options, CACHE_NAME);

    // Mark the project as disabled. The expiry will be EPOCH + 5 minutes.
    cache.markProjectAsDisabled(PROJECT_ID);
    assertThat(cache.isProjectDisabled(PROJECT_ID)).isTrue();

    // To test expiry, we must simulate time passing. Since the clock is immutable,
    // we create a *new* cache instance with a clock that is advanced past the expiry time.
    // This is a valid test because the cache state is not shared between instances.
    Clock advancedClock = Clock.offset(fixedClock, duration.plus(Duration.ofSeconds(1)));
    CacheOptions advancedOptions = options.toBuilder().setClock(advancedClock).build();
    ProjectStatusCache cacheAfterExpiry = new ProjectStatusCache(advancedOptions, CACHE_NAME);

    // The new cache instance should not have the project marked as disabled
    assertThat(cacheAfterExpiry.isProjectDisabled(PROJECT_ID)).isFalse();
  }
}
