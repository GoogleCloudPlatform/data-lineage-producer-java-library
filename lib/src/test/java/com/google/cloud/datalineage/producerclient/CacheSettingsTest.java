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

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Test suite for CacheSettings. */
@RunWith(JUnit4.class)
public class CacheSettingsTest {

  @Test
  public void getDisabledInstance_returnsDisabledSettings() {
    CacheSettings settings = CacheSettings.getDisabledInstance();

    assertThat(settings.getEnabled()).isFalse();
    assertThat(settings.getUseCommonInstance()).isFalse();
    assertThat(settings.getOptions()).isEqualTo(CacheOptions.getDefaultInstance());
  }

  @Test
  public void getCommonInstance_returnsEnabledAndCommon() {
    CacheSettings settings = CacheSettings.getCommonInstance();

    assertThat(settings.getEnabled()).isTrue();
    assertThat(settings.getUseCommonInstance()).isTrue();
    assertThat(settings.getOptions()).isEqualTo(CacheOptions.getDefaultInstance());
  }

  @Test
  public void getCommonInstanceWithFallback_returnsCorrectSettings() {
    CacheOptions fallbackOptions = CacheOptions.newBuilder().setCacheSize(50).build();
    CacheSettings settings = CacheSettings.getCommonInstance(fallbackOptions);

    assertThat(settings.getEnabled()).isTrue();
    assertThat(settings.getUseCommonInstance()).isTrue();
    assertThat(settings.getOptions()).isEqualTo(fallbackOptions);
  }

  @Test
  public void getCommonInstanceWithFallback_withNullFallback_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> CacheSettings.getCommonInstance(null));
  }

  @Test
  public void getStandAloneInstance_returnsEnabledAndNotCommon() {
    CacheSettings settings = CacheSettings.getStandAloneInstance();
    assertThat(settings.getEnabled()).isTrue();
    assertThat(settings.getUseCommonInstance()).isFalse();
    assertThat(settings.getOptions()).isEqualTo(CacheOptions.getDefaultInstance());
  }

  @Test
  public void getStandAloneInstanceWithFallback_returnsEnabledAndNotCommon() {
    CacheOptions fallbackOptions = CacheOptions.newBuilder().setCacheSize(50).build();
    CacheSettings settings = CacheSettings.getStandAloneInstance(fallbackOptions);
    assertThat(settings.getEnabled()).isTrue();
    assertThat(settings.getUseCommonInstance()).isFalse();
    assertThat(settings.getOptions()).isEqualTo(fallbackOptions);
  }

  @Test
  public void getStandAloneInstanceWithFallback_withNullFallback_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> CacheSettings.getStandAloneInstance(null));
  }
}
