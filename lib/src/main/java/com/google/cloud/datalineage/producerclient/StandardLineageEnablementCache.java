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

import java.time.Duration;

/**
 * Cache used to indicate whether Lineage Ingestion is disabled for a given project.
 *
 * <p>This class is a wrapper around a generic ProjectStatusCache.
 */
public class StandardLineageEnablementCache implements LineageEnablementCache {

  private final ProjectStatusCache delegate;

  StandardLineageEnablementCache(CacheOptions options) {
    this.delegate = new ProjectStatusCache(options, "Lineage Enablement");
  }

  @Override
  public void markLineageAsDisabled(String project) {
    delegate.markProjectAsDisabled(project);
  }

  @Override
  public void markLineageAsDisabled(String projectName, Duration duration) {
    delegate.markProjectAsDisabled(projectName, duration);
  }

  @Override
  public boolean isLineageMarkedAsDisabled(String projectName) {
    return delegate.isProjectDisabled(projectName);
  }
}
