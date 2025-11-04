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

package com.google.cloud.datalineage.producerclient.v1;

import static org.junit.Assert.assertEquals;

import com.google.cloud.datalineage.producerclient.CacheSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** * Test suite for LineageBaseSettings */
@RunWith(JUnit4.class)
public class LineageBaseSettingsTest {

  @Test
  public void toBuilder_returnsBuilderWithProvidedSettings() throws Exception {
    LineageBaseSettings baseSettings = LineageBaseSettings.defaultInstance();

    LineageBaseSettings.Builder builder = baseSettings.toBuilder();

    assertSettingsAreEqual(baseSettings, builder.build());
  }

  @Test
  public void builder_initializesDefaultSettings() throws Exception {
    LineageBaseSettings.Builder builder = LineageBaseSettings.newBuilder();

    assertSettingsAreEqual(LineageBaseSettings.defaultInstance(), builder.build());
  }

  @Test
  public void setApiEnablementCacheSettings_updatesSettings() throws Exception {
    LineageBaseSettings.Builder builder = LineageBaseSettings.newBuilder();
    CacheSettings newCacheSettings = CacheSettings.getDisabledInstance();

    LineageBaseSettings.Builder returnedBuilder =
        builder.setApiEnablementCacheSettings(newCacheSettings);
    LineageBaseSettings settings = returnedBuilder.build();

    assertEquals(newCacheSettings, settings.getApiEnablementCacheSettings());
  }

  @Test
  public void setLineageEnablementCacheSettings_updatesSettings() throws Exception {
    LineageBaseSettings.Builder builder = LineageBaseSettings.newBuilder();
    CacheSettings newCacheSettings = CacheSettings.getDisabledInstance();

    LineageBaseSettings.Builder returnedBuilder =
        builder.setLineageEnablementCacheSettings(newCacheSettings);
    LineageBaseSettings settings = returnedBuilder.build();

    assertEquals(newCacheSettings, settings.getLineageEnablementCacheSettings());
  }

  private void assertSettingsAreEqual(LineageBaseSettings expected, LineageBaseSettings actual) {
    assertEquals(
        expected.getApiEnablementCacheSettings().getEnabled(),
        actual.getApiEnablementCacheSettings().getEnabled());
    assertEquals(
        expected.getApiEnablementCacheSettings().getUseCommonInstance(),
        actual.getApiEnablementCacheSettings().getUseCommonInstance());
    assertEquals(
        expected.getApiEnablementCacheSettings().getOptions().getDefaultCacheDisabledStatusTime(),
        actual.getApiEnablementCacheSettings().getOptions().getDefaultCacheDisabledStatusTime());
    assertEquals(
        expected.getApiEnablementCacheSettings().getOptions().getClock(),
        actual.getApiEnablementCacheSettings().getOptions().getClock());
    assertEquals(
        expected.getApiEnablementCacheSettings().getOptions().getCacheSize(),
        actual.getApiEnablementCacheSettings().getOptions().getCacheSize());
  }
}
