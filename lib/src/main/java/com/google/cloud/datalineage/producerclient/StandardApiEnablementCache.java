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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

/**
 * Cache used to indicate whether API is disabled for given project.
 *
 * <p>Class lets specify default cache size, default duration of service disability and Clock.
 * Structure is thread-safe. There is no eviction guaranteed in case of cache overload.
 */
@Slf4j
public class StandardApiEnablementCache implements ApiEnablementCache {
  private final Cache<String, LocalDateTime> projectToLockEndTime;
  private final Duration defaultCacheDisabledStatusTime;
  private final Clock clock;

  StandardApiEnablementCache(ApiEnablementCacheOptions options) {
    log.debug(
        "Initializing StandardApiEnablementCache with cache size: {}, "
            + "default disabled duration: {}",
        options.getCacheSize(),
        options.getDefaultCacheDisabledStatusTime());
    defaultCacheDisabledStatusTime = options.getDefaultCacheDisabledStatusTime();
    clock = options.getClock();

    projectToLockEndTime = CacheBuilder.newBuilder().maximumSize(options.getCacheSize()).build();
  }

  /**
   * Defaults Duration to value specified by constructor.
   *
   * @see StandardApiEnablementCache#markServiceAsDisabled(String, Duration)
   */
  public synchronized void markServiceAsDisabled(String project) {
    markServiceAsDisabled(project, defaultCacheDisabledStatusTime);
  }

  /**
   * Sets service state for given project as disabled from current timestamp to current timestamp
   * increased by the given duration.
   *
   * <p>It is not guarantied that cache will indicate service state as disabled up to calculated
   * value. Specified entry may be deleted if cache is overloaded.
   */
  public synchronized void markServiceAsDisabled(String projectName, Duration duration) {
    log.warn(
        "Marking service as disabled for project '{}' for duration: {}", projectName, duration);
    projectToLockEndTime.put(projectName, LocalDateTime.now(clock).plus(duration));
  }

  /**
   * Indicates if service is disabled for given project.
   *
   * @return A boolean that indicates if service is disabled for given project
   */
  public synchronized boolean isServiceMarkedAsDisabled(String projectName) {
    LocalDateTime maybeTime = projectToLockEndTime.getIfPresent(projectName);
    if (maybeTime == null) {
      log.debug("No cache entry found for project: {}", projectName);
      return false;
    }
    boolean isDisabled = !maybeTime.isBefore(LocalDateTime.now(clock));
    if (isDisabled) {
      log.debug("Service is marked as disabled for project: {} until {}", projectName, maybeTime);
    } else {
      log.debug("Service disability has expired for project: {}", projectName);
    }
    return isDisabled;
  }
}
