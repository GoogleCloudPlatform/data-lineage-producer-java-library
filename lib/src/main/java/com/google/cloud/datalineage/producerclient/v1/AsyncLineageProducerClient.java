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

/**
 * * Async producer client.
 *
 * <p>Implements AsyncLineageClient using client library. This implementation also provides support
 * for connection cache.
 */
public final class AsyncLineageProducerClient implements BackgroundResource, AsyncLineageClient {

  public static AsyncLineageProducerClient create() throws IOException {
    return create(AsyncLineageProducerClientSettings.newBuilder().build());
  }

  public static AsyncLineageProducerClient create(AsyncLineageProducerClientSettings settings)
      throws IOException {
    return new AsyncLineageProducerClient(settings);
  }

  static AsyncLineageProducerClient create(BasicLineageClient basicClient) throws IOException {
    return new AsyncLineageProducerClient(basicClient);
  }

  private final InternalClient client;

  private AsyncLineageProducerClient(AsyncLineageProducerClientSettings settings)
      throws IOException {
    client = InternalClient.create(settings);
  }

  private AsyncLineageProducerClient(BasicLineageClient basicClient) throws IOException {
    client = InternalClient.create(basicClient);
  }

  @Override
  public ApiFuture<Empty> deleteLineageEvent(DeleteLineageEventRequest request) {
    return client.deleteLineageEvent(request);
  }

  @Override
  public OperationFuture<Empty, OperationMetadata> deleteProcess(DeleteProcessRequest request) {
    return client.deleteProcess(request);
  }

  @Override
  public OperationFuture<Empty, OperationMetadata> deleteRun(DeleteRunRequest request) {
    return client.deleteRun(request);
  }

  @Override
  public ApiFuture<LineageEvent> getLineageEvent(GetLineageEventRequest request) {
    return client.getLineageEvent(request);
  }

  @Override
  public ApiFuture<Process> getProcess(GetProcessRequest request) {
    return client.getProcess(request);
  }

  @Override
  public ApiFuture<Run> getRun(GetRunRequest request) {
    return client.getRun(request);
  }

  @Override
  public ApiFuture<ListLineageEventsPagedResponse> listLineageEvents(
      ListLineageEventsRequest request) {
    return client.listLineageEvents(request);
  }

  @Override
  public ApiFuture<ListProcessesPagedResponse> listProcesses(ListProcessesRequest request) {
    return client.listProcesses(request);
  }

  @Override
  public ApiFuture<ListRunsPagedResponse> listRuns(ListRunsRequest request) {
    return client.listRuns(request);
  }

  @Override
  public ApiFuture<ProcessOpenLineageRunEventResponse> processOpenLineageRunEvent(
      ProcessOpenLineageRunEventRequest request) {
    return client.processOpenLineageRunEvent(request);
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
