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

import com.google.api.gax.longrunning.OperationFuture;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.datacatalog.lineage.v1.DeleteLineageEventRequest;
import com.google.cloud.datacatalog.lineage.v1.DeleteProcessRequest;
import com.google.cloud.datacatalog.lineage.v1.DeleteRunRequest;
import com.google.cloud.datacatalog.lineage.v1.GetLineageEventRequest;
import com.google.cloud.datacatalog.lineage.v1.GetProcessRequest;
import com.google.cloud.datacatalog.lineage.v1.GetRunRequest;
import com.google.cloud.datacatalog.lineage.v1.LineageClient;
import com.google.cloud.datacatalog.lineage.v1.LineageClient.ListLineageEventsPagedResponse;
import com.google.cloud.datacatalog.lineage.v1.LineageClient.ListProcessesPagedResponse;
import com.google.cloud.datacatalog.lineage.v1.LineageClient.ListRunsPagedResponse;
import com.google.cloud.datacatalog.lineage.v1.LineageEvent;
import com.google.cloud.datacatalog.lineage.v1.LineageSettings;
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
 * A wrapper for LineageClient from standard library. The main reason for introducing this wrapper
 * is to create a fake for testing purposes.
 */
class StandardLineageClient implements BasicLineageClient {

  public static StandardLineageClient create() throws IOException {
    return create(LineageSettings.newBuilder().build());
  }

  public static StandardLineageClient create(LineageSettings settings) throws IOException {
    return new StandardLineageClient(settings);
  }

  private final LineageClient client;

  private StandardLineageClient(LineageSettings settings) throws IOException {
    client = LineageClient.create(settings);
  }

  @Override
  public UnaryCallable<DeleteLineageEventRequest, Empty> deleteLineageEventCallable() {
    return client.deleteLineageEventCallable();
  }

  @Override
  public OperationFuture<Empty, OperationMetadata> deleteProcessAsync(
      DeleteProcessRequest request) {
    return client.deleteProcessAsync(request);
  }

  @Override
  public OperationFuture<Empty, OperationMetadata> deleteRunAsync(DeleteRunRequest request) {
    return client.deleteRunAsync(request);
  }

  @Override
  public UnaryCallable<GetLineageEventRequest, LineageEvent> getEventCallable() {
    return client.getLineageEventCallable();
  }

  @Override
  public UnaryCallable<GetProcessRequest, Process> getProcessCallable() {
    return client.getProcessCallable();
  }

  @Override
  public UnaryCallable<GetRunRequest, Run> getRunCallable() {
    return client.getRunCallable();
  }

  @Override
  public UnaryCallable<ListLineageEventsRequest, ListLineageEventsPagedResponse>
      listLineageEventsPagedCallable() {
    return client.listLineageEventsPagedCallable();
  }

  @Override
  public UnaryCallable<ListProcessesRequest, ListProcessesPagedResponse>
      listProcessesPagedCallable() {
    return client.listProcessesPagedCallable();
  }

  @Override
  public UnaryCallable<ListRunsRequest, ListRunsPagedResponse> listRunsPagedCallable() {
    return client.listRunsPagedCallable();
  }

  @Override
  public UnaryCallable<ProcessOpenLineageRunEventRequest, ProcessOpenLineageRunEventResponse>
      processOpenLineageRunEventCallable() {
    return client.processOpenLineageRunEventCallable();
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

  @Override
  public void close() {
    client.close();
  }
}
