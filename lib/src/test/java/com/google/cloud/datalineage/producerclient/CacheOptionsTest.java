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
import static org.junit.Assert.assertThrows;

import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CacheOptionsTest {

  @Test
  public void newBuilder_setsDefaultValues() {
    CacheOptions options = CacheOptions.newBuilder().build();
    assertThat(options.getCacheSize()).isEqualTo(1000);
    assertThat(options.getDefaultCacheDisabledStatusTime()).isEqualTo(Duration.ofMinutes(5));
    assertThat(options.getClock()).isEqualTo(Clock.systemDefaultZone());
  }

  @Test
  public void setters_changeValues() {
    Clock clock = Clock.systemDefaultZone();
    CacheOptions options =
        CacheOptions.newBuilder()
            .setCacheSize(500)
            .setDefaultCacheDisabledStatusTime(Duration.ofMinutes(5))
            .setClock(clock)
            .build();

    assertThat(options.getCacheSize()).isEqualTo(500);
    assertThat(options.getDefaultCacheDisabledStatusTime()).isEqualTo(Duration.ofMinutes(5));
    assertThat(options.getClock()).isEqualTo(clock);
  }

  @Test
  public void setCacheSize_negative_throwsIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> CacheOptions.newBuilder().setCacheSize(-1));
    assertThat(exception).hasMessageThat().contains("Limit cannot be negative");
  }

  @Test
  public void setDefaultCacheDisabledStatusTime_negative_throwsIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                CacheOptions.newBuilder()
                    .setDefaultCacheDisabledStatusTime(Duration.ofMinutes(-1)));
    assertThat(exception).hasMessageThat().contains("Duration cannot be negative");
  }

  @Test
  public void toBuilder_preserveOptions() {
    Duration disabledTime = Duration.ofSeconds(30);
    int size = 500;
    Clock clock = Clock.fixed(Clock.systemDefaultZone().instant(), ZoneId.of("UTC"));

    CacheOptions options =
        CacheOptions.newBuilder()
            .setDefaultCacheDisabledStatusTime(disabledTime)
            .setCacheSize(size)
            .setClock(clock)
            .build();

    CacheOptions newOptions = options.toBuilder().setCacheSize(1000).build();

    assertThat(newOptions.getCacheSize()).isEqualTo(1000);
    assertThat(newOptions.getClock()).isEqualTo(clock);
    assertThat(newOptions.getDefaultCacheDisabledStatusTime()).isEqualTo(disabledTime);
  }
}
