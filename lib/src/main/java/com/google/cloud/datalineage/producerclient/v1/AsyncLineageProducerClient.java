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
import com.google.protobuf.Empty;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;

/**
 * * Async producer client.
 *
 * <p>Implements AsyncLineageClient using client library. This implementation also provides support
 * for connection cache.
 */
public final class AsyncLineageProducerClient implements BackgroundResource, AsyncLineageClient {

  private static final Logger logger = LoggerFactory.getLogger(AsyncLineageProducerClient.class);

  public static AsyncLineageProducerClient create() throws IOException {
    return create(AsyncLineageProducerClientSettings.newBuilder().build());
  }

  public static AsyncLineageProducerClient create(AsyncLineageProducerClientSettings settings)
      throws IOException {
    logger.debug(
        "Creating AsyncLineageProducerClient with graceful shutdown duration: {}",
        settings.getGracefulShutdownDuration());
    return new AsyncLineageProducerClient(settings);
  }

  static AsyncLineageProducerClient create(BasicLineageClient basicClient) throws IOException {
    return new AsyncLineageProducerClient(basicClient, Duration.ZERO);
  }

  static AsyncLineageProducerClient create(
      BasicLineageClient basicClient, Duration gracefulShutdownDuration) throws IOException {
    return new AsyncLineageProducerClient(basicClient, gracefulShutdownDuration);
  }

  private final InternalClient client;
  private final Duration gracefulShutdownDuration;

  private AsyncLineageProducerClient(AsyncLineageProducerClientSettings settings)
      throws IOException {
    client = InternalClient.create(settings);
    this.gracefulShutdownDuration = settings.getGracefulShutdownDuration();
  }

  private AsyncLineageProducerClient(
      BasicLineageClient basicClient, Duration gracefulShutdownDuration) throws IOException {
    client = InternalClient.create(basicClient);
    this.gracefulShutdownDuration = gracefulShutdownDuration;
  }

  @Override
  public ApiFuture<Empty> deleteLineageEvent(DeleteLineageEventRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug("Deleting lineage event: {}", request.getName());
    }
    return client.deleteLineageEvent(request);
  }

  @Override
  public OperationFuture<Empty, OperationMetadata> deleteProcess(DeleteProcessRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug("Deleting process: {}", request.getName());
    }
    return client.deleteProcess(request);
  }

  @Override
  public OperationFuture<Empty, OperationMetadata> deleteRun(DeleteRunRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug("Deleting run: {}", request.getName());
    }
    return client.deleteRun(request);
  }

  @Override
  public ApiFuture<LineageEvent> getLineageEvent(GetLineageEventRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug("Getting lineage event: {}", request.getName());
    }
    return client.getLineageEvent(request);
  }

  @Override
  public ApiFuture<Process> getProcess(GetProcessRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug("Getting process: {}", request.getName());
    }
    return client.getProcess(request);
  }

  @Override
  public ApiFuture<Run> getRun(GetRunRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug("Getting run: {}", request.getName());
    }
    return client.getRun(request);
  }

  @Override
  public ApiFuture<ListLineageEventsPagedResponse> listLineageEvents(
      ListLineageEventsRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug("Listing lineage events for request: {}", request);
    }
    return client.listLineageEvents(request);
  }

  @Override
  public ApiFuture<ListProcessesPagedResponse> listProcesses(ListProcessesRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug("Listing processes for request: {}", request);
    }
    return client.listProcesses(request);
  }

  @Override
  public ApiFuture<ListRunsPagedResponse> listRuns(ListRunsRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug("Listing runs for request: {}", request);
    }
    return client.listRuns(request);
  }

  @Override
  public ApiFuture<ProcessOpenLineageRunEventResponse> processOpenLineageRunEvent(
      ProcessOpenLineageRunEventRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug("Processing OpenLineage run event: {}", request.getOpenLineage());
    }
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
      logger.warn("Interrupted during shutdown", e);
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

  private void gracefulShutdown(Instant start) throws InterruptedException {
    if (!gracefulShutdownDuration.isZero()) {
      if (logger.isDebugEnabled()) {
        logger.debug("Starting graceful shutdown with duration: {}", gracefulShutdownDuration);
      }
      boolean terminated =
          awaitTermination(
              gracefulShutdownDuration.minus(Duration.between(start, Instant.now())).toNanos(),
              TimeUnit.NANOSECONDS);
      if (!terminated) {
        logger.warn(
            "AsyncLineageProducerClient did not terminate within the graceful shutdown duration: {}",
            gracefulShutdownDuration);
      }
    }
  }
}
