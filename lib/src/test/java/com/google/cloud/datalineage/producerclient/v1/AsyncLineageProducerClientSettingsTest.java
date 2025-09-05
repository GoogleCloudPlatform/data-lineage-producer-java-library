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

/** * Test suite for AsyncLineageProducerClientSettingsTest */
public class AsyncLineageProducerClientSettingsTest {

  @Test
  public void newBuilder_notNull() {
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    assertNotNull(builder);
  }

  @Test
  public void defaultInstance_notNull() throws Exception {
    AsyncLineageProducerClientSettings settings =
        AsyncLineageProducerClientSettings.defaultInstance();
    assertNotNull(settings);
  }

  @Test
  public void builderWithSettings_initializesCorrectSettings() throws Exception {
    AsyncLineageProducerClientSettings initialSettings =
        AsyncLineageProducerClientSettings.defaultInstance();

    AsyncLineageProducerClientSettings.Builder builder =
        new AsyncLineageProducerClientSettings.Builder(initialSettings);
    AsyncLineageProducerClientSettings newSettings = builder.build();

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
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    String testEndpoint = "test-endpoint";

    AsyncLineageProducerClientSettings.Builder resultBuilder = builder.setEndpoint(testEndpoint);

    assertNotNull(resultBuilder);
    assertEquals(testEndpoint, resultBuilder.getEndpoint());
  }

  @Test
  public void builder_setClock() throws Exception {
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    ApiClock mockClock = Mockito.mock(ApiClock.class);

    AsyncLineageProducerClientSettings.Builder resultBuilder = builder.setClock(mockClock);

    assertNotNull(resultBuilder);
    assertEquals(mockClock, resultBuilder.build().getClock());
  }

  @Test
  @Deprecated
  public void builder_setExecutorProvider() {
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    ExecutorProvider mockExecutorProvider = Mockito.mock(ExecutorProvider.class);

    AsyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setExecutorProvider(mockExecutorProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockExecutorProvider, resultBuilder.getExecutorProvider());
  }

  @Test
  public void builder_setWatchdogCheckInterval() {
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    Duration checkInterval = Duration.ofSeconds(30);

    AsyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setWatchdogCheckInterval(checkInterval);

    assertNotNull(resultBuilder);
    assertEquals(checkInterval, resultBuilder.getWatchdogCheckInterval());
  }

  @Test
  public void builder_setWatchdogProvider() {
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    WatchdogProvider mockWatchdogProvider = Mockito.mock(WatchdogProvider.class);

    AsyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setWatchdogProvider(mockWatchdogProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockWatchdogProvider, resultBuilder.getWatchdogProvider());
  }

  @Test
  public void builder_setTransportChannelProvider() {
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    TransportChannelProvider mockTransportChannelProvider =
        Mockito.mock(TransportChannelProvider.class);

    AsyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setTransportChannelProvider(mockTransportChannelProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockTransportChannelProvider, resultBuilder.getTransportChannelProvider());
  }

  @Test
  public void builder_setQuotaProjectId() {
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    String quotaProjectId = "test-quota-project-id";

    AsyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setQuotaProjectId(quotaProjectId);

    assertNotNull(resultBuilder);
    assertEquals(quotaProjectId, resultBuilder.getQuotaProjectId());
  }

  @Test
  public void builder_setHeaderProvider() {
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    HeaderProvider mockHeaderProvider = Mockito.mock(HeaderProvider.class);

    AsyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setHeaderProvider(mockHeaderProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockHeaderProvider, resultBuilder.getHeaderProvider());
  }

  @Test
  public void builder_setCredentialsProvider() {
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    CredentialsProvider mockCredentialsProvider = Mockito.mock(CredentialsProvider.class);

    AsyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setCredentialsProvider(mockCredentialsProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockCredentialsProvider, resultBuilder.getCredentialsProvider());
  }

  @Test
  public void builder_setBackgroundExecutorProvider() {
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    ExecutorProvider mockExecutorProvider = Mockito.mock(ExecutorProvider.class);

    AsyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setBackgroundExecutorProvider(mockExecutorProvider);

    assertNotNull(resultBuilder);
    assertEquals(mockExecutorProvider, resultBuilder.getBackgroundExecutorProvider());
  }

  @Test
  public void builder_setGracefulShutdownDuration() {
    AsyncLineageProducerClientSettings.Builder builder =
        AsyncLineageProducerClientSettings.newBuilder();
    Duration gracefulShutdownDuration = Duration.ofSeconds(2);

    AsyncLineageProducerClientSettings.Builder resultBuilder =
        builder.setGracefulShutdownDuration(gracefulShutdownDuration);

    assertNotNull(resultBuilder);
    assertEquals(gracefulShutdownDuration, resultBuilder.getGracefulShutdownDuration());
  }
}
