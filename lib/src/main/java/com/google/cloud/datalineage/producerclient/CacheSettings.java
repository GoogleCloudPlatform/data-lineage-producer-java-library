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

/** Provides an immutable object for storing cache settings. */
public final class CacheSettings {

  /**
   * Disables cache feature.
   *
   * @return The requested cache settings.
   */
  public static CacheSettings getDisabledInstance() {
    return new CacheSettings(false, false, CacheOptions.getDefaultInstance());
  }

  /**
   * Uses common instance. If there is no such instance, creates one with default cache options.
   *
   * @return The requested cache settings.
   */
  public static CacheSettings getCommonInstance() {
    return new CacheSettings(true, true, CacheOptions.getDefaultInstance());
  }

  /**
   * Uses common instance. If there is no such instance, creates one using the provided cache
   * options.
   *
   * @param fallbackOptions The fallback cache options.
   * @return The requested cache settings.
   */
  public static CacheSettings getCommonInstance(CacheOptions fallbackOptions) {
    if (fallbackOptions == null) {
      throw new IllegalArgumentException("defaultSettings cannot be null");
    }
    return new CacheSettings(true, true, fallbackOptions);
  }

  /**
   * Uses stand-alone instance with default cache options.
   *
   * @return The requested cache settings.
   */
  public static CacheSettings getStandAloneInstance() {
    return new CacheSettings(true, false, CacheOptions.getDefaultInstance());
  }

  /**
   * Uses stand-alone instance with provided settings.
   *
   * @param options The cache options
   * @return The requested cache settings.
   */
  public static CacheSettings getStandAloneInstance(CacheOptions options) {
    if (options == null) {
      throw new IllegalArgumentException("settings cannot be null");
    }
    return new CacheSettings(true, false, options);
  }

  private final boolean enabled;
  private final boolean useCommonInstance;
  private final CacheOptions options;

  private CacheSettings(boolean enabled, boolean useCommonInstance, CacheOptions options) {
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

  public CacheOptions getOptions() {
    return options;
  }
}
