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

package com.google.cloud.datacatalog.lineage.producerclient.v1;

import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.StatusCode.Code;
import com.google.cloud.datacatalog.lineage.producerclient.ApiEnablementCacheSettings;
import com.google.cloud.datacatalog.lineage.v1.LineageSettings;
import com.google.cloud.datacatalog.lineage.v1.stub.LineageStubSettings;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import org.threeten.bp.Duration;

/**
 * Provides immutable object for storing base lineage settings. Client specific settings should
 * extend this class. Settings object can be created via Builder.
 */
public class LineageBaseSettings extends LineageSettings {

  public static Builder newBuilder() {
    return Builder.createDefault();
  }

  public static Builder newBuilder(ClientContext clientContext) {
    return new Builder(clientContext);
  }

  public static LineageBaseSettings defaultInstance() throws IOException {
    return newBuilder().build();
  }

  private final ApiEnablementCacheSettings apiEnablementCacheSettings;

  protected LineageBaseSettings(Builder settingsBuilder) throws IOException {
    super(settingsBuilder);
    this.apiEnablementCacheSettings = settingsBuilder.apiEnablementCacheSettings;
  }

  public ApiEnablementCacheSettings getConnectionCacheSettings() {
    return apiEnablementCacheSettings;
  }

  @Override
  public Builder toBuilder() {
    return new Builder(this);
  }

  /**
   * * Builder for LineageBaseSettings.
   *
   * <p>Lets setting all properties of LineageSettings and ApiEnablementCacheSettings. Can be created
   * by ApiEnablementCacheSettings.newBuilder method. To create settings object, use build method.
   */
  public static class Builder extends LineageSettings.Builder {

    private static final RetrySettings DEFAULT_RETRY_SETTINGS =
        RetrySettings.newBuilder()
            .setMaxAttempts(0)
            .setTotalTimeout(Duration.ofMinutes(50))
            .setInitialRetryDelay(Duration.ofSeconds(1))
            .setRetryDelayMultiplier(1.3)
            .setMaxRetryDelay(Duration.ofMinutes(3))
            .setInitialRpcTimeout(Duration.ofSeconds(60))
            .setRpcTimeoutMultiplier(1.3)
            .setMaxRpcTimeout(Duration.ofMinutes(3))
            .build();

    private static final ImmutableSet<Code> DEFAULT_RETRY_CODES =
        ImmutableSet.of(Code.UNAVAILABLE, Code.DEADLINE_EXCEEDED);

    private static Builder createDefault() {
      return new Builder(LineageStubSettings.newBuilder());
    }

    private ApiEnablementCacheSettings apiEnablementCacheSettings;

    protected Builder() throws IOException {
      super();
      apiEnablementCacheSettings = ApiEnablementCacheSettings.getCommonInstance();
      applyDefaultRetryPolicy();
    }

    protected Builder(ClientContext clientContext) {
      super(clientContext);
      apiEnablementCacheSettings = ApiEnablementCacheSettings.getCommonInstance();
      applyDefaultRetryPolicy();
    }

    protected Builder(LineageBaseSettings settings) {
      super(settings);
      this.apiEnablementCacheSettings = settings.apiEnablementCacheSettings;
    }

    protected Builder(LineageStubSettings.Builder stubSettings) {
      super(stubSettings);
      apiEnablementCacheSettings = ApiEnablementCacheSettings.getCommonInstance();
      applyDefaultRetryPolicy();
    }

    public Builder setConnectionCacheSettings(ApiEnablementCacheSettings settings) {
      apiEnablementCacheSettings = settings;
      return this;
    }

    @Override
    public LineageBaseSettings build() throws IOException {
      return new LineageBaseSettings(this);
    }

    private void applyDefaultRetryPolicy() {
      deleteProcessSettings()
          .setRetrySettings(DEFAULT_RETRY_SETTINGS)
          .setRetryableCodes(DEFAULT_RETRY_CODES);
      deleteRunSettings()
          .setRetrySettings(DEFAULT_RETRY_SETTINGS)
          .setRetryableCodes(DEFAULT_RETRY_CODES);
      deleteLineageEventSettings()
          .setRetrySettings(DEFAULT_RETRY_SETTINGS)
          .setRetryableCodes(DEFAULT_RETRY_CODES);
    }
  }
}
