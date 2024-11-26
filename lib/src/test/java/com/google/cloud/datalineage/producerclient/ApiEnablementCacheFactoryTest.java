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

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** * Test suite for ApiEnablementCacheFactory */
@RunWith(JUnit4.class)
public class ApiEnablementCacheFactoryTest {

  @Test
  public void get_disabledSettings_returnsNoOpCache() {
    ApiEnablementCacheSettings settings = ApiEnablementCacheSettings.getDisabledInstance();

    ApiEnablementCache cache = ApiEnablementCacheFactory.get(settings);

    assertThat(cache).isInstanceOf(NoOpApiEnablementCache.class);
  }

  @Test
  public void get_withStandAloneSettings_returnsStandardCache() {
    ApiEnablementCacheSettings settings = ApiEnablementCacheSettings.getStandAloneInstance();

    ApiEnablementCache cache = ApiEnablementCacheFactory.get(settings);

    assertThat(cache).isInstanceOf(StandardApiEnablementCache.class);
  }

  @Test
  public void get_withCommonInstanceSettings_returnsSameCacheInstance() {
    ApiEnablementCacheSettings settings = ApiEnablementCacheSettings.getCommonInstance();

    ApiEnablementCache cache1 = ApiEnablementCacheFactory.get(settings);
    ApiEnablementCache cache2 = ApiEnablementCacheFactory.get(settings);

    assertThat(cache1).isSameInstanceAs(cache2);
  }
}
