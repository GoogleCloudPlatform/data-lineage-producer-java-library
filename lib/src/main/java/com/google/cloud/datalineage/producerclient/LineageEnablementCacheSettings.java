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
public final class LineageEnablementCacheSettings {

  /**
   * Disables connection cache feature.
   *
   * @return The requested cache settings.
   */
  public static LineageEnablementCacheSettings getDisabledInstance() {
    return new LineageEnablementCacheSettings(
        false, false, LineageEnablementCacheOptions.newBuilder().build());
  }

  /**
   * Uses common instance. If there is no such instance, creates one with default cache options.
   *
   * @return The requested cache settings.
   */
  public static LineageEnablementCacheSettings getCommonInstance() {
    return new LineageEnablementCacheSettings(
        true, true, LineageEnablementCacheOptions.newBuilder().build());
  }

  /**
   * Uses common instance. If there is no such instance, creates one using the provided cache
   * options.
   *
   * @param fallbackOptions The fallback cache options.
   * @return The requested cache settings.
   */
  public static LineageEnablementCacheSettings getCommonInstance(
      LineageEnablementCacheOptions fallbackOptions) {
    if (fallbackOptions == null) {
      throw new IllegalArgumentException("defaultSettings cannot be null");
    }
    return new LineageEnablementCacheSettings(true, true, fallbackOptions);
  }

  /**
   * Uses stand-alone instance with default cache options.
   *
   * @return The requested cache settings.
   */
  public static LineageEnablementCacheSettings getStandAloneInstance() {
    return new LineageEnablementCacheSettings(
        true, false, LineageEnablementCacheOptions.newBuilder().build());
  }

  /**
   * Uses stand-alone instance with provided settings.
   *
   * @param options The cache options
   * @return The requested cache settings.
   */
  public static LineageEnablementCacheSettings getStandAloneInstance(
      LineageEnablementCacheOptions options) {
    if (options == null) {
      throw new IllegalArgumentException("settings cannot be null");
    }
    return new LineageEnablementCacheSettings(true, false, options);
  }

  private final boolean enabled;
  private final boolean useCommonInstance;
  private final LineageEnablementCacheOptions options;

  private LineageEnablementCacheSettings(
      boolean enabled, boolean useCommonInstance, LineageEnablementCacheOptions options) {
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

  public LineageEnablementCacheOptions getOptions() {
    return options;
  }
}
