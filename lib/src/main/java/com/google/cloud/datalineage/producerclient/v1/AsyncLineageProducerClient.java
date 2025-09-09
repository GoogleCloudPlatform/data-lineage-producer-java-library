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

import com.google.api.core.ApiFuture;
import com.google.api.gax.core.BackgroundResource;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.datacatalog.lineage.v1.DeleteLineageEventRequest;
import com.google.cloud.datacatalog.lineage.v1.DeleteProcessRequest;
import com.google.cloud.datacatalog.lineage.v1.DeleteRunRequest;
import com.google.cloud.datacatalog.lineage.v1.GetLineageEventRequest;
import com.google.cloud.datacatalog.lineage.v1.GetProcessRequest;
import com.google.cloud.datacatalog.lineage.v1.GetRunRequest;
import com.google.cloud.datacatalog.lineage.v1.LineageClient.ListLineageEventsPagedResponse;
import com.google.cloud.datacatalog.lineage.v1.LineageClient.ListProcessesPagedResponse;
import com.google.cloud.datacatalog.lineage.v1.LineageClient.ListRunsPagedResponse;
import com.google.cloud.datacatalog.lineage.v1.LineageEvent;
import com.google.cloud.datacatalog.lineage.v1.ListLineageEventsRequest;
import com.google.cloud.datacatalog.lineage.v1.ListProcessesRequest;
import com.google.cloud.datacatalog.lineage.v1.ListRunsRequest;
import com.google.cloud.datacatalog.lineage.v1.OperationMetadata;
import com.google.cloud.datacatalog.lineage.v1.Process;
import com.google.cloud.datacatalog.lineage.v1.ProcessOpenLineageRunEventRequest;
import com.google.cloud.datacatalog.lineage.v1.ProcessOpenLineageRunEventResponse;
import com.google.cloud.datacatalog.lineage.v1.Run;
import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Empty;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;

/**
 * * Async producer client.
 *
 * <p>Implements AsyncLineageClient using client library. This implementation also provides support
 * for connection cache.
 */
@Slf4j
public final class AsyncLineageProducerClient implements BackgroundResource, AsyncLineageClient {

  public static AsyncLineageProducerClient create() throws IOException {
    return create(AsyncLineageProducerClientSettings.defaultInstance());
  }

  public static AsyncLineageProducerClient create(AsyncLineageProducerClientSettings settings)
      throws IOException {
    log.debug(
        "Creating AsyncLineageProducerClient with graceful shutdown duration: {}",
        settings.getGracefulShutdownDuration());
    return new AsyncLineageProducerClient(settings);
  }

  @VisibleForTesting
  static AsyncLineageProducerClient create(BasicLineageClient basicClient) throws IOException {
    return create(basicClient, AsyncLineageProducerClientSettings.defaultInstance());
  }

  @VisibleForTesting
  static AsyncLineageProducerClient create(
      BasicLineageClient basicClient, AsyncLineageProducerClientSettings settings) throws IOException {
    return new AsyncLineageProducerClient(basicClient, settings);
  }

  private final InternalClient client;
  private final Duration gracefulShutdownDuration;

  private AsyncLineageProducerClient(AsyncLineageProducerClientSettings settings)
      throws IOException {
    client = InternalClient.create(settings);
    this.gracefulShutdownDuration = settings.getGracefulShutdownDuration();
  }

  private AsyncLineageProducerClient(BasicLineageClient basicClient, AsyncLineageProducerClientSettings settings) throws IOException {
    client = InternalClient.create(basicClient);
    this.gracefulShutdownDuration = settings.getGracefulShutdownDuration();
  }

  @Override
  public ApiFuture<Empty> deleteLineageEvent(DeleteLineageEventRequest request) {
    log.debug("Deleting lineage event: {}", request.getName());
    return client.deleteLineageEvent(request);
  }

  @Override
  public OperationFuture<Empty, OperationMetadata> deleteProcess(DeleteProcessRequest request) {
    log.debug("Deleting process: {}", request.getName());
    return client.deleteProcess(request);
  }

  @Override
  public OperationFuture<Empty, OperationMetadata> deleteRun(DeleteRunRequest request) {
    log.debug("Deleting run: {}", request.getName());
    return client.deleteRun(request);
  }

  @Override
  public ApiFuture<LineageEvent> getLineageEvent(GetLineageEventRequest request) {
    log.debug("Getting lineage event: {}", request.getName());
    return client.getLineageEvent(request);
  }

  @Override
  public ApiFuture<Process> getProcess(GetProcessRequest request) {
    log.debug("Getting process: {}", request.getName());
    return client.getProcess(request);
  }

  @Override
  public ApiFuture<Run> getRun(GetRunRequest request) {
    log.debug("Getting run: {}", request.getName());
    return client.getRun(request);
  }

  @Override
  public ApiFuture<ListLineageEventsPagedResponse> listLineageEvents(
      ListLineageEventsRequest request) {
    log.debug("Listing lineage events for parent: {}", request.getParent());
    return client.listLineageEvents(request);
  }

  @Override
  public ApiFuture<ListProcessesPagedResponse> listProcesses(ListProcessesRequest request) {
    log.debug("Listing processes for parent: {}", request.getParent());
    return client.listProcesses(request);
  }

  @Override
  public ApiFuture<ListRunsPagedResponse> listRuns(ListRunsRequest request) {
    log.debug("Listing runs for parent: {}", request.getParent());
    return client.listRuns(request);
  }

  @Override
  public ApiFuture<ProcessOpenLineageRunEventResponse> processOpenLineageRunEvent(
      ProcessOpenLineageRunEventRequest request) {
    log.debug("Processing OpenLineage run event: {}", request.getOpenLineage());
    return client.processOpenLineageRunEvent(request);
  }

  @Override
  public void close() throws Exception {
    Instant start = Instant.now();
    client.close();
    gracefulShutdown(start);
  }

  @Override
  public void shutdown() {
    Instant start = Instant.now();
    client.shutdown();
    try {
      gracefulShutdown(start);
    } catch (InterruptedException e) {
      log.warn("Interrupted during shutdown", e);
    }
  }

  @Override
  public boolean isShutdown() {
    return client.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return client.isTerminated();
  }

  @Override
  public void shutdownNow() {
    client.shutdownNow();
  }

  @Override
  public boolean awaitTermination(long duration, TimeUnit unit) throws InterruptedException {
    return client.awaitTermination(duration, unit);
  }

  private void gracefulShutdown(Instant shutdownStartedAt) throws InterruptedException {
    if (gracefulShutdownDuration.isZero()) {
      log.warn(
          "AsyncLineageProducerClient graceful shutdown duration was set to zero. This effectively means hard shutdown");
      return;
    }
    log.debug("Starting graceful shutdown with duration: {}", gracefulShutdownDuration);
    boolean terminated =
        awaitTermination(
            gracefulShutdownDuration.minus(Duration.between(shutdownStartedAt, Instant.now()))
                .toNanos(),
            TimeUnit.NANOSECONDS);
    if (!terminated) {
      log.warn(
          "AsyncLineageProducerClient did not terminate within the "
              + "graceful shutdown duration: {}",
          gracefulShutdownDuration);
    }
  }
}
