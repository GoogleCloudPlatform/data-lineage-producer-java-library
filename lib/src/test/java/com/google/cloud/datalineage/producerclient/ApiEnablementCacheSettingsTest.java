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
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** * Test suite for ApiEnablementCacheSettings */
@RunWith(JUnit4.class)
public class ApiEnablementCacheSettingsTest {

  @Test
  public void getDisabledInstance_returnsCorrectSettings() {
    ApiEnablementCacheSettings settings = ApiEnablementCacheSettings.getDisabledInstance();

    assertThat(settings.getEnabled()).isFalse();
    assertThat(settings.getUseCommonInstance()).isFalse();

    // Not comparing clocks because Clock.systemDefaultZone() returns the current timestamp.
    assertThat(settings.getOptions().getCacheSize())
        .isEqualTo(ApiEnablementCacheOptions.getDefaultInstance().getCacheSize());
    assertThat(settings.getOptions().getDefaultCacheDisabledStatusTime())
        .isEqualTo(
            ApiEnablementCacheOptions.getDefaultInstance().getDefaultCacheDisabledStatusTime());
  }

  @Test
  public void getCommonInstance_returnsCorrectSettings() {
    ApiEnablementCacheSettings settings = ApiEnablementCacheSettings.getCommonInstance();

    assertThat(settings.getEnabled()).isTrue();
    assertThat(settings.getUseCommonInstance()).isTrue();

    // Not comparing clocks because Clock.systemDefaultZone() returns the current timestamp.
    assertThat(settings.getOptions().getCacheSize())
        .isEqualTo(ApiEnablementCacheOptions.getDefaultInstance().getCacheSize());
    assertThat(settings.getOptions().getDefaultCacheDisabledStatusTime())
        .isEqualTo(
            ApiEnablementCacheOptions.getDefaultInstance().getDefaultCacheDisabledStatusTime());
  }

  @Test
  public void getCommonInstanceWithFallback_returnsCorrectSettings() {
    ApiEnablementCacheOptions fallbackOptions =
        ApiEnablementCacheOptions.newBuilder().setCacheSize(50).build();
    ApiEnablementCacheSettings settings =
        ApiEnablementCacheSettings.getCommonInstance(fallbackOptions);

    assertThat(settings.getEnabled()).isTrue();
    assertThat(settings.getUseCommonInstance()).isTrue();
    assertThat(settings.getOptions()).isEqualTo(fallbackOptions);
  }

  @Test
  public void getCommonInstanceWithFallback_withNullFallback_throwsException() {
    assertThrows(
        IllegalArgumentException.class, () -> ApiEnablementCacheSettings.getCommonInstance(null));
  }

  @Test
  public void getStandAloneInstance_returnsCorrectSettings() {
    ApiEnablementCacheSettings settings = ApiEnablementCacheSettings.getStandAloneInstance();

    assertThat(settings.getEnabled()).isTrue();
    assertThat(settings.getUseCommonInstance()).isFalse();

    // Not comparing clocks because Clock.systemDefaultZone() returns the current timestamp.
    assertThat(settings.getOptions().getCacheSize())
        .isEqualTo(ApiEnablementCacheOptions.getDefaultInstance().getCacheSize());
    assertThat(settings.getOptions().getDefaultCacheDisabledStatusTime())
        .isEqualTo(
            ApiEnablementCacheOptions.getDefaultInstance().getDefaultCacheDisabledStatusTime());
  }

  @Test
  public void getStandAloneInstanceWithSettings_returnsCorrectSettings() {
    ApiEnablementCacheOptions options =
        ApiEnablementCacheOptions.newBuilder().setCacheSize(50).build();
    ApiEnablementCacheSettings settings = ApiEnablementCacheSettings.getStandAloneInstance(options);

    assertThat(settings.getEnabled()).isTrue();
    assertThat(settings.getUseCommonInstance()).isFalse();

    // Not comparing clocks because Clock.systemDefaultZone() returns the current timestamp.
    assertThat(settings.getOptions().getCacheSize()).isEqualTo(options.getCacheSize());
    assertThat(settings.getOptions().getDefaultCacheDisabledStatusTime())
        .isEqualTo(options.getDefaultCacheDisabledStatusTime());
  }

  @Test
  public void getStandAloneInstanceWithSettings_withNullSettings_throwsException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> ApiEnablementCacheSettings.getStandAloneInstance(null));
  }
}
