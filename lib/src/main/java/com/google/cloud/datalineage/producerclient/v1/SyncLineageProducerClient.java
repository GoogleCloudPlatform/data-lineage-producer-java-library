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

import com.google.api.gax.rpc.ApiExceptions;
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
import com.google.cloud.datacatalog.lineage.v1.Process;
import com.google.cloud.datacatalog.lineage.v1.ProcessOpenLineageRunEventRequest;
import com.google.cloud.datacatalog.lineage.v1.ProcessOpenLineageRunEventResponse;
import com.google.cloud.datacatalog.lineage.v1.Run;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * * Sync lineage producer client.
 *
 * <p>Implements SyncLineageClient using client library. This implementation also provides support
 * for connection cache.
 */
@Slf4j
public final class SyncLineageProducerClient implements SyncLineageClient {

  public static SyncLineageProducerClient create() throws IOException {
    return create(SyncLineageProducerClientSettings.newBuilder().build());
  }

  public static SyncLineageProducerClient create(SyncLineageProducerClientSettings settings)
      throws IOException {
    return new SyncLineageProducerClient(settings);
  }

  static SyncLineageProducerClient create(BasicLineageClient basicClient) throws IOException {
    return new SyncLineageProducerClient(basicClient);
  }

  private final InternalClient client;

  private SyncLineageProducerClient(SyncLineageProducerClientSettings settings) throws IOException {
    client = InternalClient.create(settings);
  }

  private SyncLineageProducerClient(BasicLineageClient basicClient) throws IOException {
    client = InternalClient.create(basicClient);
  }

  @Override
  public void deleteLineageEvent(DeleteLineageEventRequest request) {
    log.debug("Deleting lineage event: {}", request.getName());
    ApiExceptions.callAndTranslateApiException(client.deleteLineageEvent(request));
  }

  @Override
  public void deleteProcess(DeleteProcessRequest request)
      throws ExecutionException, InterruptedException {
    log.debug("Deleting process: {}", request.getName());
    client.deleteProcess(request).get();
  }

  @Override
  public void deleteRun(DeleteRunRequest request) throws ExecutionException, InterruptedException {
    log.debug("Deleting run: {}", request.getName());
    client.deleteRun(request).get();
  }

  @Override
  public LineageEvent getLineageEvent(GetLineageEventRequest request) {
    log.debug("Getting lineage event: {}", request.getName());
    return ApiExceptions.callAndTranslateApiException(client.getLineageEvent(request));
  }

  @Override
  public Process getProcess(GetProcessRequest request) {
    log.debug("Getting process: {}", request.getName());
    return ApiExceptions.callAndTranslateApiException(client.getProcess(request));
  }

  @Override
  public Run getRun(GetRunRequest request) {
    log.debug("Getting run: {}", request.getName());
    return ApiExceptions.callAndTranslateApiException(client.getRun(request));
  }

  @Override
  public ListLineageEventsPagedResponse listLineageEvents(ListLineageEventsRequest request) {
    log.debug("Listing lineage events for parent: {}", request.getParent());
    return ApiExceptions.callAndTranslateApiException(client.listLineageEvents(request));
  }

  @Override
  public ListProcessesPagedResponse listProcesses(ListProcessesRequest request) {
    log.debug("Listing processes for parent: {}", request.getParent());
    return ApiExceptions.callAndTranslateApiException(client.listProcesses(request));
  }

  @Override
  public ListRunsPagedResponse listRuns(ListRunsRequest request) {
    log.debug("Listing runs for parent: {}", request.getParent());
    return ApiExceptions.callAndTranslateApiException(client.listRuns(request));
  }

  @Override
  public ProcessOpenLineageRunEventResponse processOpenLineageRunEvent(
      ProcessOpenLineageRunEventRequest request) {
    log.debug("Processing OpenLineage run event: {}", request.getOpenLineage());
    return ApiExceptions.callAndTranslateApiException(client.processOpenLineageRunEvent(request));
  }

  @Override
  public void close() throws Exception {
    client.close();
  }

  @Override
  public void shutdown() {
    client.shutdown();
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
}
