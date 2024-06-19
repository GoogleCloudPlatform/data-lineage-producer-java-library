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

/** NoOpConnectionCache always indicates that a project is enabled. */
public class NoOpApiEnablementCache implements ApiEnablementCache {

  public void markServiceAsDisabled(String project) {}

  public void markServiceAsDisabled(String project, Duration offset) {}

  public boolean isServiceMarkedAsDisabled(String project) {
    return false;
  }
}
