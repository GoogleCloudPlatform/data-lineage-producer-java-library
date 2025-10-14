// Copyright 2024 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not not use this file except in compliance with the License.
// You may- may not use this file except in compliance with the License.
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

public class LineageEnablementCacheSettingsTest {
  @Test
  public void getDisabledInstance_returnsDisabledSettings() {
    LineageEnablementCacheSettings settings = LineageEnablementCacheSettings.getDisabledInstance();
    assertThat(settings.getEnabled()).isFalse();
  }

  @Test
  public void getCommonInstance_returnsEnabledAndCommon() {
    LineageEnablementCacheSettings settings = LineageEnablementCacheSettings.getCommonInstance();
    assertThat(settings.getEnabled()).isTrue();
    assertThat(settings.getUseCommonInstance()).isTrue();
  }

  @Test
  public void getStandAloneInstance_returnsEnabledAndNotCommon() {
    LineageEnablementCacheSettings settings =
        LineageEnablementCacheSettings.getStandAloneInstance();
    assertThat(settings.getEnabled()).isTrue();
    assertThat(settings.getUseCommonInstance()).isFalse();
  }
}
