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

import java.time.Clock;
import java.time.Duration;

/**
 * Provides an immutable object for Cache initialization. CacheOptions object can be created via
 * Builder.
 */
public class CacheOptions {

  protected static final Duration DEFAULT_DISABLED_TIME = Duration.ofMinutes(5);
  protected static final int DEFAULT_SIZE = 1000;
  protected static final Clock DEFAULT_CLOCK = Clock.systemDefaultZone();

  private final Duration defaultCacheDisabledStatusTime;
  private final int cacheSize;
  private final Clock clock;

  protected CacheOptions(CacheOptions.Builder settingsBuilder) {
    defaultCacheDisabledStatusTime = settingsBuilder.defaultCacheDisabledStatusTime;
    cacheSize = settingsBuilder.cacheSize;
    clock = settingsBuilder.clock;
  }

  public static CacheOptions.Builder newBuilder() {
    return CacheOptions.Builder.createDefault();
  }

  public static CacheOptions getDefaultInstance() {
    return CacheOptions.Builder.createDefault().build();
  }

  public Duration getDefaultCacheDisabledStatusTime() {
    return defaultCacheDisabledStatusTime;
  }

  public int getCacheSize() {
    return cacheSize;
  }

  public Clock getClock() {
    return clock;
  }

  public CacheOptions.Builder toBuilder() {
    return new CacheOptions.Builder(this);
  }

  /**
   * Returns true if the other object is also a CacheOptions and has the same values for all
   * fields.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CacheOptions)) {
      return false;
    }
    CacheOptions that = (CacheOptions) obj;
    return this.cacheSize == that.cacheSize
        && this.defaultCacheDisabledStatusTime.equals(that.defaultCacheDisabledStatusTime)
        && this.clock.equals(that.clock);
  }

  /**
   * * Builder for CacheSettings.
   *
   * <p>Lets setting `markServiceAsDisabledTime`, `cacheSize`, and `clock`. Can be created by
   * CacheOptions.newBuilder method. To create settings object, use build method.
   */
  public static class Builder {

    protected Duration defaultCacheDisabledStatusTime;
    protected int cacheSize;
    protected Clock clock;

    protected Builder(CacheOptions settings) {
      defaultCacheDisabledStatusTime = settings.defaultCacheDisabledStatusTime;
      cacheSize = settings.cacheSize;
      clock = settings.clock;
    }

    protected Builder(Duration defaultCacheDisabledStatusTime, int cacheSize, Clock clock) {
      this.defaultCacheDisabledStatusTime = defaultCacheDisabledStatusTime;
      this.cacheSize = cacheSize;
      this.clock = clock;
    }

    private static CacheOptions.Builder createDefault() {
      return new CacheOptions.Builder(DEFAULT_DISABLED_TIME, DEFAULT_SIZE, DEFAULT_CLOCK);
    }

    public CacheOptions.Builder setDefaultCacheDisabledStatusTime(
        Duration defaultCacheDisabledStatusTime) {
      if (defaultCacheDisabledStatusTime.isNegative()) {
        throw new IllegalArgumentException("Duration cannot be negative");
      }
      this.defaultCacheDisabledStatusTime = defaultCacheDisabledStatusTime;
      return this;
    }

    public CacheOptions.Builder setCacheSize(int cacheSize) {
      if (cacheSize < 0) {
        throw new IllegalArgumentException("Limit cannot be negative");
      }
      this.cacheSize = cacheSize;
      return this;
    }

    public CacheOptions.Builder setClock(Clock clock) {
      this.clock = clock;
      return this;
    }

    public CacheOptions build() {
      return new CacheOptions(this);
    }
  }
}
