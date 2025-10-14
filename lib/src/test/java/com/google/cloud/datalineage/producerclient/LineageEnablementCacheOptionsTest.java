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
import org.junit.Test;

public class LineageEnablementCacheOptionsTest {
  @Test
  public void newBuilder_setsDefaultValues() {
    LineageEnablementCacheOptions options = LineageEnablementCacheOptions.newBuilder().build();
    assertThat(options.getCacheSize()).isEqualTo(1000L);
    assertThat(options.getDefaultCacheDisabledStatusTime()).isEqualTo(Duration.ofMinutes(10));
    assertThat(options.getClock()).isEqualTo(Clock.systemUTC());
  }

  @Test
  public void setters_changeValues() {
    Clock clock = Clock.systemDefaultZone();
    LineageEnablementCacheOptions options =
        LineageEnablementCacheOptions.newBuilder()
            .setCacheSize(500L)
            .setDefaultCacheDisabledStatusTime(Duration.ofMinutes(5))
            .setClock(clock)
            .build();

    assertThat(options.getCacheSize()).isEqualTo(500L);
    assertThat(options.getDefaultCacheDisabledStatusTime()).isEqualTo(Duration.ofMinutes(5));
    assertThat(options.getClock()).isEqualTo(clock);
  }
}