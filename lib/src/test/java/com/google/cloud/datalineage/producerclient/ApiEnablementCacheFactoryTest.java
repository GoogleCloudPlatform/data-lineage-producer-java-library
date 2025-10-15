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

@RunWith(JUnit4.class)
public class ApiEnablementCacheFactoryTest {

  @Test
  public void get_returnsSameInstance() {
    CacheSettings settings = CacheSettings.getCommonInstance();
    ApiEnablementCache firstInstance = ApiEnablementCacheFactory.get(settings);
    ApiEnablementCache secondInstance = ApiEnablementCacheFactory.get(settings);
    assertThat(firstInstance).isSameInstanceAs(secondInstance);
  }

  @Test
  public void getDisabled_returnsNoOpInstance() {
    CacheSettings settings = CacheSettings.getDisabledInstance();
    ApiEnablementCache cache = ApiEnablementCacheFactory.get(settings);
    assertThat(cache).isInstanceOf(NoOpApiEnablementCache.class);
  }

  @Test
  public void getStandAlone_returnsNewInstance() {
    CacheSettings settings = CacheSettings.getStandAloneInstance();
    ApiEnablementCache firstInstance = ApiEnablementCacheFactory.get(settings);
    ApiEnablementCache secondInstance = ApiEnablementCacheFactory.get(settings);
    assertThat(firstInstance).isNotSameInstanceAs(secondInstance);
  }
}
