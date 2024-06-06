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

package com.google.cloud.datacatalog.lineage.producerclient;

/** Provides an immutable object for storing connection cache settings. */
public final class ConnectionCacheSettings {

  /** Disables connection cache feature. */
  public static ConnectionCacheSettings getDisabledInstance() {
    return new ConnectionCacheSettings(false, false, ConnectionCacheOptions.getDefaultInstance());
  }

  /** Uses common instance. If there is no such instance, creates one with default settings. */
  public static ConnectionCacheSettings getCommonInstance() {
    return new ConnectionCacheSettings(true, true, ConnectionCacheOptions.getDefaultInstance());
  }

  /** Uses common instance. If there is no such instance, creates one using provided settings. */
  public static ConnectionCacheSettings getCommonInstance(ConnectionCacheOptions fallbackSettings) {
    if (fallbackSettings == null) {
      throw new IllegalArgumentException("defaultSettings cannot be null");
    }
    return new ConnectionCacheSettings(true, true, fallbackSettings);
  }

  /** Uses stand-alone instance with default settings. */
  public static ConnectionCacheSettings getStandAloneInstance() {
    return new ConnectionCacheSettings(true, false, ConnectionCacheOptions.getDefaultInstance());
  }

  /** Uses stand-alone instance with provided settings. */
  public static ConnectionCacheSettings getStandAloneInstance(ConnectionCacheOptions settings) {
    if (settings == null) {
      throw new IllegalArgumentException("settings cannot be null");
    }
    return new ConnectionCacheSettings(true, false, settings);
  }

  private final boolean enabled;
  private final boolean useCommonInstance;
  private final ConnectionCacheOptions options;

  private ConnectionCacheSettings(
      boolean enabled, boolean useCommonInstance, ConnectionCacheOptions options) {
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

  public ConnectionCacheOptions getOptions() {
    return options;
  }
}
