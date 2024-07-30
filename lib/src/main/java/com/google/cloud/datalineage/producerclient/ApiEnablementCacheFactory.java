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

/** A factory that returns ConnectionCache based on ApiEnablementCacheSettings. */
public class ApiEnablementCacheFactory {
  private static volatile ApiEnablementCache commonInstance;

  public static ApiEnablementCache get(ApiEnablementCacheSettings settings) {
    if (!settings.getEnabled()) {
      return new NoOpApiEnablementCache();
    }

    if (!settings.getUseCommonInstance()) {
      return new StandardApiEnablementCache(settings.getOptions());
    }

    if (commonInstance != null) {
      return commonInstance;
    }

    synchronized (ApiEnablementCacheFactory.class) {
      if (commonInstance == null) {
        commonInstance = new StandardApiEnablementCache(settings.getOptions());
      }
    }

    return commonInstance;
  }
}
