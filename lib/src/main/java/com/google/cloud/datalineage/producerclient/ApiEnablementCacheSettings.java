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

/** Provides an immutable object for storing connection cache settings. */
public final class ApiEnablementCacheSettings {

  /** Disables connection cache feature. */
  public static ApiEnablementCacheSettings getDisabledInstance() {
    return new ApiEnablementCacheSettings(
        false, false, ApiEnablementCacheOptions.getDefaultInstance());
  }

  /** Uses common instance. If there is no such instance, creates one with default settings. */
  public static ApiEnablementCacheSettings getCommonInstance() {
    return new ApiEnablementCacheSettings(
        true, true, ApiEnablementCacheOptions.getDefaultInstance());
  }

  /** Uses common instance. If there is no such instance, creates one using provided settings. */
  public static ApiEnablementCacheSettings getCommonInstance(
      ApiEnablementCacheOptions fallbackSettings) {
    if (fallbackSettings == null) {
      throw new IllegalArgumentException("defaultSettings cannot be null");
    }
    return new ApiEnablementCacheSettings(true, true, fallbackSettings);
  }

  /** Uses stand-alone instance with default settings. */
  public static ApiEnablementCacheSettings getStandAloneInstance() {
    return new ApiEnablementCacheSettings(
        true, false, ApiEnablementCacheOptions.getDefaultInstance());
  }

  /** Uses stand-alone instance with provided settings. */
  public static ApiEnablementCacheSettings getStandAloneInstance(
      ApiEnablementCacheOptions settings) {
    if (settings == null) {
      throw new IllegalArgumentException("settings cannot be null");
    }
    return new ApiEnablementCacheSettings(true, false, settings);
  }

  private final boolean enabled;
  private final boolean useCommonInstance;
  private final ApiEnablementCacheOptions options;

  private ApiEnablementCacheSettings(
      boolean enabled, boolean useCommonInstance, ApiEnablementCacheOptions options) {
    this.enabled = enabled;
    this.useCommonInstance = useCommonInstance;
    this.options = options;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public boolean getUseCommonInstance() {
    return useCommonInstance;
  }

  public ApiEnablementCacheOptions getOptions() {
    return options;
  }
}
