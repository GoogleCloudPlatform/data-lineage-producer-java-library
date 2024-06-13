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

package com.google.cloud.datacatalog.lineage.producerclient;

import java.time.Clock;
import java.time.Duration;

/**
 * Provides an immutable object for ConnectionCache initialization. ApiEnablementCacheOptions object
 * can be created via Builder.
 */
public class ApiEnablementCacheOptions {
  protected static final Duration DEFAULT_DISABLED_TIME = Duration.ofMinutes(5);
  protected static final int DEFAULT_SIZE = 1000;
  protected static final Clock DEFAULT_CLOCK = Clock.systemDefaultZone();

  private final Duration markServiceAsDisabledTime;
  private final int cacheSize;
  private final Clock clock;

  protected ApiEnablementCacheOptions(ApiEnablementCacheOptions.Builder settingsBuilder) {
    markServiceAsDisabledTime = settingsBuilder.markServiceAsDisabledTime;
    cacheSize = settingsBuilder.cacheSize;
    clock = settingsBuilder.clock;
  }

  public static ApiEnablementCacheOptions.Builder newBuilder() {
    return ApiEnablementCacheOptions.Builder.createDefault();
  }

  public static ApiEnablementCacheOptions getDefaultInstance() {
    return ApiEnablementCacheOptions.Builder.createDefault().build();
  }

  public Duration getMarkServiceAsDisabledTime() {
    return markServiceAsDisabledTime;
  }

  public int getCacheSize() {
    return cacheSize;
  }

  public Clock getClock() {
    return clock;
  }

  public ApiEnablementCacheOptions.Builder toBuilder() {
    return new ApiEnablementCacheOptions.Builder(this);
  }

  /**
   * * Builder for ApiEnablementCacheSettings.
   *
   * <p>Lets setting `markServiceAsDisabledTime`, `cacheSize`, and `clock`. Can be created by
   * ApiEnablementCacheOptions.newBuilder method. To create settings object, use build method.
   */
  public static class Builder {
    protected Duration markServiceAsDisabledTime;
    protected int cacheSize;
    protected Clock clock;

    protected Builder(ApiEnablementCacheOptions settings) {
      markServiceAsDisabledTime = settings.markServiceAsDisabledTime;
      cacheSize = settings.cacheSize;
      clock = settings.clock;
    }

    protected Builder(Duration markServiceAsDisabledTime, int cacheSize, Clock clock) {
      this.markServiceAsDisabledTime = markServiceAsDisabledTime;
      this.cacheSize = cacheSize;
      this.clock = clock;
    }

    private static ApiEnablementCacheOptions.Builder createDefault() {
      return new ApiEnablementCacheOptions.Builder(DEFAULT_DISABLED_TIME, DEFAULT_SIZE, DEFAULT_CLOCK);
    }

    public ApiEnablementCacheOptions.Builder setMarkServiceAsDisabledTime(
        Duration markServiceAsDisabledTime) {
      if (markServiceAsDisabledTime.isNegative()) {
        throw new IllegalArgumentException("Duration cannot be negative");
      }
      this.markServiceAsDisabledTime = markServiceAsDisabledTime;
      return this;
    }

    public ApiEnablementCacheOptions.Builder setCacheSize(int cacheSize) {
      if (cacheSize < 0) {
        throw new IllegalArgumentException("Limit cannot be negative");
      }
      this.cacheSize = cacheSize;
      return this;
    }

    public ApiEnablementCacheOptions.Builder setClock(Clock clock) {
      this.clock = clock;
      return this;
    }

    public ApiEnablementCacheOptions build() {
      return new ApiEnablementCacheOptions(this);
    }
  }
}
