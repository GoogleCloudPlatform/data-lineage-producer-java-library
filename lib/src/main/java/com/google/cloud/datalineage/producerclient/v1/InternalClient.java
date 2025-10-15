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
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.api.gax.rpc.ApiExceptionFactory;
import com.google.api.gax.rpc.PermissionDeniedException;
import com.google.api.gax.rpc.StatusCode.Code;
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
import com.google.cloud.datalineage.producerclient.ApiEnablementCache;
import com.google.cloud.datalineage.producerclient.ApiEnablementCacheFactory;
import com.google.cloud.datalineage.producerclient.LineageEnablementCache;
import com.google.cloud.datalineage.producerclient.LineageEnablementCacheFactory;
import com.google.cloud.datalineage.producerclient.helpers.GrpcHelper;
import com.google.cloud.datalineage.producerclient.helpers.NamesHelper;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.Empty;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps standard lineage client and provides common functionalities.
 */
final class InternalClient implements AsyncLineageClient {

  private static final Logger logger = LoggerFactory.getLogger(AsyncLineageProducerClient.class);

  static InternalClient create() throws IOException {
    return create(LineageBaseSettings.defaultInstance());
  }

  static InternalClient create(LineageBaseSettings settings) throws IOException {
    return new InternalClient(settings, BasicLineageClientFactory.get(settings));
  }

  static InternalClient create(BasicLineageClient client) throws IOException {
    return new InternalClient(LineageBaseSettings.defaultInstance(), client);
  }

  private final BasicLineageClient client;
  private final ApiEnablementCache apiEnablementCache;
  private final LineageEnablementCache lineageEnablementCache;

  private InternalClient(LineageBaseSettings settings, BasicLineageClient basicLineageClient) {
    apiEnablementCache = ApiEnablementCacheFactory.get(settings.getApiEnablementCacheSettings());
    lineageEnablementCache = LineageEnablementCacheFactory.get(
        settings.getLineageEnablementCacheSettings());
    client = basicLineageClient;
  }

  @Override
  public OperationFuture<Empty, OperationMetadata> deleteProcess(DeleteProcessRequest request) {
    return handleCall(() -> client.deleteProcessAsync(request), request.getName());
  }

  @Override
  public OperationFuture<Empty, OperationMetadata> deleteRun(DeleteRunRequest request) {
    return handleCall(() -> client.deleteRunAsync(request), request.getName());
  }

  @Override
  public ApiFuture<Empty> deleteLineageEvent(DeleteLineageEventRequest request) {
    return handleCall(() -> client.deleteLineageEventCallable().futureCall(request),
        request.getName());
  }

  @Override
  public ApiFuture<Process> getProcess(GetProcessRequest request) {
    return handleCall(() -> client.getProcessCallable().futureCall(request), request.getName());
  }

  @Override
  public ApiFuture<Run> getRun(GetRunRequest request) {
    return handleCall(() -> client.getRunCallable().futureCall(request), request.getName());
  }

  @Override
  public ApiFuture<LineageEvent> getLineageEvent(GetLineageEventRequest request) {
    return handleCall(() -> client.getEventCallable().futureCall(request), request.getName());
  }

  @Override
  public ApiFuture<ListProcessesPagedResponse> listProcesses(ListProcessesRequest request) {
    return handleCall(() -> client.listProcessesPagedCallable().futureCall(request),
        request.getParent());
  }

  @Override
  public ApiFuture<ListRunsPagedResponse> listRuns(ListRunsRequest request) {
    return handleCall(() -> client.listRunsPagedCallable().futureCall(request),
        request.getParent());
  }

  @Override
  public ApiFuture<ListLineageEventsPagedResponse> listLineageEvents(
      ListLineageEventsRequest request) {
    return handleCall(() -> client.listLineageEventsPagedCallable().futureCall(request),
        request.getParent());
  }

  @Override
  public ApiFuture<ProcessOpenLineageRunEventResponse> processOpenLineageRunEvent(
      ProcessOpenLineageRunEventRequest request) {
    return handleCall(() -> client.processOpenLineageRunEventCallable().futureCall(request),
        request.getParent());
  }

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
  public void close() throws Exception {
    client.close();
  }

  /**
   * Wraps an API call with ConnectionCache logic to prevent unnecessary API calls.
   *
   * @param call - supplier that cause API call
   * @param resourceName - name of the resource
   * @throws PermissionDeniedException - exception that indicates that API call was suspended
   */
  private <F extends ApiFuture<T>, T> F handleCall(Supplier<F> call, String resourceName) {
    String projectName = NamesHelper.getProjectNameWithLocationFromResourceName(resourceName);
    if (apiEnablementCache.isServiceMarkedAsDisabled(projectName)) {
      throw ApiExceptionFactory.createException(
          "Data Lineage API is disabled in project " + projectName
              + ". Please enable the API and try again after a few minutes.", null,
          GrpcHelper.getStatusCodeFromCode(Code.PERMISSION_DENIED), false);
    }

    if (lineageEnablementCache.isLineageMarkedAsDisabled(projectName)) {
      throw ApiExceptionFactory.createException(
          "Lineage is not enabled in Lineage Configurations for project " + projectName
              + ". Please enable Lineage in Lineage Configurations and try again after a few minutes.",
          null, GrpcHelper.getStatusCodeFromCode(Code.PERMISSION_DENIED), false);
    }

    F result = call.get();
    ApiFutures.addCallback(result, new ApiFutureCallback<>() {
      @Override
      public void onFailure(Throwable exception) {
        logger.error("Failed to call API for resource {}: {}", resourceName, exception.getMessage(),
            exception);
        ImmutableSet<String> reasons = GrpcHelper.getErrorReasons(exception);
        if (reasons.contains("SERVICE_DISABLED")) {
          apiEnablementCache.markServiceAsDisabled(projectName);
        } else if (reasons.contains("LINEAGE_INGESTION_DISABLED")) {
          lineageEnablementCache.markLineageAsDisabled(projectName);
        }
      }

      @Override
      public void onSuccess(Object result) {
        if (logger.isDebugEnabled()) {
          logger.debug("Successfully called API for resource: {}", resourceName);
        }
      }
    }, MoreExecutors.directExecutor());

    return result;
  }
}
