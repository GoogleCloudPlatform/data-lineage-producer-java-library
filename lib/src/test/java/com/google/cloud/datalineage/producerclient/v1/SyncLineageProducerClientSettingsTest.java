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
import static org.junit.Assert.assertNotNull;

import com.google.api.core.ApiClock;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.rpc.HeaderProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.api.gax.rpc.WatchdogProvider;
import org.junit.Test;
import org.mockito.Mockito;
import org.threeten.bp.Duration;

/** * Test suite for SyncLineageProducerClientSettingsTest */
public class SyncLineageProducerClientSettingsTest {

  @Test
  public void newBuilder_notNull() {
    SyncLineageProducerClientSettings.Builder builder =
        SyncLineageProducerClientSettings.newBuilder();
    assertNotNull(builder);
  }

  @Test
  public void defaultInstance_notNull() throws Exception {
    SyncLineageProducerClientSettings settings =
        SyncLineageProducerClientSettings.defaultInstance();
    assertNotNull(settings);
  }

  @Test
  public void builderWithSettings_initializesCorrectSettings() throws Exception {
    SyncLineageProducerClientSettings initialSettings =
        SyncLineageProducerClientSettings.defaultInstance();

    SyncLineageProducerClientSettings.Builder builder =
        new SyncLineageProducerClientSettings.Builder(initialSettings);
    SyncLineageProducerClientSettings newSettings = builder.build();

    assertNotNull(builder);
    assertEquals(initialSettings.getEndpoint(), newSettings.getEndpoint());
    assertEquals(initialSettings.getClock(), newSettings.getClock());
    assertEquals(
        initialSettings.getBackgroundExecutorProvider(),
        newSettings.getBackgroundExecutorProvider());
    assertEquals(initialSettings.getCredentialsProvider(), newSettings.getCredentialsProvider());
    assertEquals(initialSettings.getHeaderProvider(), newSettings.getHeaderProvider());
    assertEquals(initialSettings.getQuotaProjectId(), newSettings.getQuotaProjectId());
    assertEquals(
        initialSettings.getTransportChannelProvider(), newSettings.getTransportChannelProvider());
    assertEquals(initialSettings.getWatchdogProvider(), newSettings.getWatchdogProvider());
    assertEquals(
        initialSettings.getWatchdogCheckInterval(), newSettings.getWatchdogCheckInterval());
  }

  @Test
  public void builder_setEndpoint() {
    SyncLineageProducerClientSettings.Builder builder =
        SyncLineageProducerClientSettings.newBuilder();
    String testEndpoint = "test-endpoint";

    SyncLineageProducerClientSettings.Builder resultBuilder = builder.setEndpoint(testEndpoint);

    assertNotNull(resultBuilder);
    assertEquals(testEndpoint, resultBuilder.getEndpoint());
  }

  @Test
  public void builder_setClock() throws Exception {
    SyncLineageProducerClientSettings.Builder builder =
        SyncLineageProducerClientSettings.newBuilder();
    ApiClock mockClock = Mockito.mock(ApiClock.class);

    SyncLineageProducerClientSettings.Builder resultBuilder = builder.setClock(mockClock);

    assertNotNull(resultBuilder);
    assertEquals(mockClock, resultBuilder.build().getClock());
  }

  @Test
  @Deprecated
  public void builder_setExecutorProvider() {
    SyncLineageProducerClientSettings.Builder builder =
        SyncLineageProducerClientSettings.newBuilder();
    ExecutorProvider mockExecutorProvider = Mockito.mock(ExecutorProvider.class);

    SyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setExecutorProvider(mockExecutorProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockExecutorProvider, resultBuilder.getExecutorProvider());
  }

  @Test
  public void builder_setWatchdogCheckInterval() {
    SyncLineageProducerClientSettings.Builder builder =
        SyncLineageProducerClientSettings.newBuilder();
    Duration checkInterval = Duration.ofSeconds(30);

    SyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setWatchdogCheckInterval(checkInterval);

    assertNotNull(resultBuilder);
    assertEquals(checkInterval, resultBuilder.getWatchdogCheckInterval());
  }

  @Test
  public void builder_setWatchdogProvider() {
    SyncLineageProducerClientSettings.Builder builder =
        SyncLineageProducerClientSettings.newBuilder();
    WatchdogProvider mockWatchdogProvider = Mockito.mock(WatchdogProvider.class);

    SyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setWatchdogProvider(mockWatchdogProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockWatchdogProvider, resultBuilder.getWatchdogProvider());
  }

  @Test
  public void builder_setTransportChannelProvider() {
    SyncLineageProducerClientSettings.Builder builder =
        SyncLineageProducerClientSettings.newBuilder();
    TransportChannelProvider mockTransportChannelProvider =
        Mockito.mock(TransportChannelProvider.class);

    SyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setTransportChannelProvider(mockTransportChannelProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockTransportChannelProvider, resultBuilder.getTransportChannelProvider());
  }

  @Test
  public void builder_setQuotaProjectId() {
    SyncLineageProducerClientSettings.Builder builder =
        SyncLineageProducerClientSettings.newBuilder();
    String quotaProjectId = "test-quota-project-id";

    SyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setQuotaProjectId(quotaProjectId);

    assertNotNull(resultBuilder);
    assertEquals(quotaProjectId, resultBuilder.getQuotaProjectId());
  }

  @Test
  public void builder_setHeaderProvider() {
    SyncLineageProducerClientSettings.Builder builder =
        SyncLineageProducerClientSettings.newBuilder();
    HeaderProvider mockHeaderProvider = Mockito.mock(HeaderProvider.class);

    SyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setHeaderProvider(mockHeaderProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockHeaderProvider, resultBuilder.getHeaderProvider());
  }

  @Test
  public void builder_setCredentialsProvider() {
    SyncLineageProducerClientSettings.Builder builder =
        SyncLineageProducerClientSettings.newBuilder();
    CredentialsProvider mockCredentialsProvider = Mockito.mock(CredentialsProvider.class);

    SyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setCredentialsProvider(mockCredentialsProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockCredentialsProvider, resultBuilder.getCredentialsProvider());
  }

  @Test
  public void builder_setBackgroundExecutorProvider() {
    SyncLineageProducerClientSettings.Builder builder =
        SyncLineageProducerClientSettings.newBuilder();
    ExecutorProvider mockExecutorProvider = Mockito.mock(ExecutorProvider.class);

    SyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setBackgroundExecutorProvider(mockExecutorProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockExecutorProvider, resultBuilder.getBackgroundExecutorProvider());
  }
}
