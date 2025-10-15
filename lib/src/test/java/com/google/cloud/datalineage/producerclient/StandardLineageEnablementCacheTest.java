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

public class StandardLineageEnablementCacheTest {
  private static final String PROJECT_ID = "project-id";

  @Test
  public void isLineageMarkedAsDisabled_afterMarking_returnsTrue() {
    StandardLineageEnablementCache cache =
        new StandardLineageEnablementCache(CacheOptions.newBuilder().build());
    cache.markLineageAsDisabled(PROJECT_ID);
    assertThat(cache.isLineageMarkedAsDisabled(PROJECT_ID)).isTrue();
  }

  @Test
  public void isLineageMarkedAsDisabled_withoutMarking_returnsFalse() {
    StandardLineageEnablementCache cache =
        new StandardLineageEnablementCache(CacheOptions.newBuilder().build());
    assertThat(cache.isLineageMarkedAsDisabled(PROJECT_ID)).isFalse();
  }

  @Test
  public void isLineageMarkedAsDisabled_afterDurationPasses_returnsFalse() {
    Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    Duration duration = Duration.ofMinutes(5);
    CacheOptions options =
        CacheOptions.newBuilder().setDefaultCacheDisabledStatusTime(duration).setClock(clock).build();
    StandardLineageEnablementCache cache = new StandardLineageEnablementCache(options);

    cache.markLineageAsDisabled(PROJECT_ID);

    // Advance the clock
    options = options.toBuilder().setClock(Clock.offset(clock, duration)).build();
    cache = new StandardLineageEnablementCache(options);

    assertThat(cache.isLineageMarkedAsDisabled(PROJECT_ID)).isFalse();
  }
}
