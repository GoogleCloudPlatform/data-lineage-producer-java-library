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
 * Cache used to store information about whether Lineage is disabled for a given project in
 * LineageConfigurations.
 */
public interface LineageEnablementCache {

  /**
   * Mark lineage ingestion state as disabled for a given project name.
   *
   * @param projectName The project for which to disable the lineage ingestion
   * @see LineageEnablementCache#markLineageAsDisabled(String, Duration)
   */
  void markLineageAsDisabled(String projectName);

  /**
   * Mark lineage ingestion state as disabled for a given project name and duration.
   *
   * @param projectName The project for which to disable the lineage ingestion
   * @param duration - suggests how long the project should be marked as disabled. It is not
   *     guarantied that cache will indicate lineage ingestion enablement state as disabled for
   *     given time. Behaviour depends on implementation.
   */
  void markLineageAsDisabled(String projectName, Duration duration);

  /**
   * Indicates if lineage ingestion with provided projectName is marked as disabled.
   *
   * @param projectName The project for which to disable the lineage ingestion
   * @return `true` if the lineage ingestion is marked as disabled
   */
  boolean isLineageMarkedAsDisabled(String projectName);
}
