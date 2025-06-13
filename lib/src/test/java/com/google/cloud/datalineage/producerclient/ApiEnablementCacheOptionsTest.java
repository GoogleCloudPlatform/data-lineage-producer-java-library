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

/** * Test suite for ApiEnablementCacheOptions */
@RunWith(JUnit4.class)
public class ApiEnablementCacheOptionsTest {

  @Test
  public void builder_overrideValues() {
    Duration disabledTime = Duration.ofSeconds(30);
    int size = 500;
    Clock clock = Clock.fixed(Clock.systemDefaultZone().instant(), ZoneId.of("UTC"));

    ApiEnablementCacheOptions options =
        ApiEnablementCacheOptions.newBuilder()
            .setDefaultCacheDisabledStatusTime(disabledTime)
            .setCacheSize(size)
            .setClock(clock)
            .build();

    assertThat(options.getDefaultCacheDisabledStatusTime()).isEqualTo(disabledTime);
    assertThat(options.getCacheSize()).isEqualTo(size);
    assertThat(options.getClock()).isEqualTo(clock);
  }

  @Test
  public void builder_invalidDisabledTime_throwsException() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            ApiEnablementCacheOptions.newBuilder()
                .setDefaultCacheDisabledStatusTime(Duration.ofSeconds(-1)));
  }

  @Test
  public void builder_invalidCacheSize_throwsException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> ApiEnablementCacheOptions.newBuilder().setCacheSize(-1));
  }

  @Test
  public void getDefaultInstance_setsDefaultOptions() {
    ApiEnablementCacheOptions options = ApiEnablementCacheOptions.getDefaultInstance();
    assertThat(options.getDefaultCacheDisabledStatusTime())
        .isEqualTo(ApiEnablementCacheOptions.DEFAULT_DISABLED_TIME);
    assertThat(options.getCacheSize()).isEqualTo(ApiEnablementCacheOptions.DEFAULT_SIZE);
    assertThat(options.getClock()).isEqualTo(ApiEnablementCacheOptions.DEFAULT_CLOCK);
  }

  @Test
  public void toBuilder_preservesOptions() {
    Duration disabledTime = Duration.ofSeconds(30);
    int size = 500;
    Clock clock = Clock.fixed(Clock.systemDefaultZone().instant(), ZoneId.of("UTC"));

    ApiEnablementCacheOptions options =
        ApiEnablementCacheOptions.newBuilder()
            .setDefaultCacheDisabledStatusTime(disabledTime)
            .setCacheSize(size)
            .setClock(clock)
            .build();

    ApiEnablementCacheOptions newOptions = options.toBuilder().build();

    assertThat(newOptions.getDefaultCacheDisabledStatusTime()).isEqualTo(disabledTime);
    assertThat(newOptions.getCacheSize()).isEqualTo(size);
    assertThat(newOptions.getClock()).isEqualTo(clock);
  }
}
