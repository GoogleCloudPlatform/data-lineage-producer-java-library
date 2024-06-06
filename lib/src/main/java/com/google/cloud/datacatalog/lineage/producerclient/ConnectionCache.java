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

import java.time.Duration;

/** Cache used to store information about whether the API is disabled for a given project. */
public interface ConnectionCache {

  /** @see ConnectionCache#markServiceAsDisabled(String, Duration) */
  void markServiceAsDisabled(String project);

  /**
   * Mark service state as disabled for a given project name.
   *
   * @param offset - suggests how long given project should be marked as disabled. It is not
   *     guarantied that cache will indicate service state as disabled for given time. Behaviour
   *     depends on implementation.
   */
  void markServiceAsDisabled(String projectName, Duration offset);

  /** Indicates if service with provided projectName is marked as disabled. */
  boolean isServiceMarkedAsDisabled(String projectName);
}
