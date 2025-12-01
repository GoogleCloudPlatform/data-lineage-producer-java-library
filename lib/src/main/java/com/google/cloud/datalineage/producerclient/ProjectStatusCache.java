// Copyright 2025 Google LLC
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
 * Generic cache to indicate whether a feature is disabled for a given project.
 *
 * <p>This class is thread-safe. There is no eviction guaranteed in case of cache overload.
 */
@Slf4j
public class ProjectStatusCache {

  private final Cache<String, LocalDateTime> projectToLockEndTime;
  private final Duration defaultCacheDisabledStatusTime;
  private final Clock clock;
  private final String cacheName;

  public ProjectStatusCache(CacheOptions options, String cacheName) {
    log.debug(
        "Initializing ProjectStatusCache '{}' with cache size: {}, default disabled duration: {}",
        cacheName,
        options.getCacheSize(),
        options.getDefaultCacheDisabledStatusTime());
    this.defaultCacheDisabledStatusTime = options.getDefaultCacheDisabledStatusTime();
    this.clock = options.getClock();
    this.projectToLockEndTime =
        CacheBuilder.newBuilder().maximumSize(options.getCacheSize()).build();
    this.cacheName = cacheName;
  }

  public synchronized void markProjectAsDisabled(String project) {
    markProjectAsDisabled(project, defaultCacheDisabledStatusTime);
  }

  public synchronized void markProjectAsDisabled(String projectName, Duration duration) {
    log.warn(
        "Marking project '{}' as disabled in cache '{}' for duration: {}",
        projectName,
        cacheName,
        duration);
    projectToLockEndTime.put(projectName, LocalDateTime.now(clock).plus(duration));
  }

  public synchronized boolean isProjectDisabled(String projectName) {
    LocalDateTime maybeTime = projectToLockEndTime.getIfPresent(projectName);
    if (maybeTime == null) {
      log.debug("No cache entry found for project '{}' in cache '{}'", projectName, cacheName);
      return false;
    }
    boolean isDisabled = !maybeTime.isBefore(LocalDateTime.now(clock));
    if (isDisabled) {
      log.debug(
          "Project '{}' is marked as disabled in cache '{}' until {}",
          projectName,
          cacheName,
          maybeTime);
    } else {
      log.debug(
          "Project disability has expired for project '{}' in cache '{}'", projectName, cacheName);
    }
    return isDisabled;
  }
}
