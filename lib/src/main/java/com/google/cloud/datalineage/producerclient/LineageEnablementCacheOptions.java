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

import com.google.auto.value.AutoValue;
import com.google.common.annotations.VisibleForTesting;
import java.time.Clock;
import java.time.Duration;

/** Options for configuring the API enablement cache. */
@AutoValue
public abstract class LineageEnablementCacheOptions {
  public static Builder newBuilder() {
    return new AutoValue_LineageEnablementCacheOptions.Builder()
        .setCacheSize(1000L)
        .setDefaultCacheDisabledStatusTime(Duration.ofMinutes(10))
        .setClock(Clock.systemUTC());
  }

  public abstract Builder toBuilder();

  public abstract Long getCacheSize();

  public abstract Duration getDefaultCacheDisabledStatusTime();

  @VisibleForTesting
  public abstract Clock getClock();

  /** Builder for ApiEnablementCacheOptions. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setCacheSize(Long cacheSize);

    public abstract Builder setDefaultCacheDisabledStatusTime(Duration duration);

    @VisibleForTesting
    public abstract Builder setClock(Clock clock);

    public abstract LineageEnablementCacheOptions build();
  }
}