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

import com.google.api.gax.core.BackgroundResource;
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
import java.util.concurrent.ExecutionException;

/** Represents client-facing synchronous API for producer library. */
public interface SyncLineageClient extends BackgroundResource {
  /**
   * Deletes the process with the specified name.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (SyncLineageClient lineageClient = SyncLineageProducerClient.create()) {
   *   ProcessName name = ProcessName.of("[PROJECT]", "[LOCATION]", "[PROCESS]");
   *   DeleteProcessRequest request = DeleteProcessRequest.newBuilder()
   *       .setName(name).build();
   *   lineageClient.deleteProcess(request);
   * }
   * }</pre>
   *
   * @param request Required. The request object that will be used to execute API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  void deleteProcess(DeleteProcessRequest request) throws ExecutionException, InterruptedException;

  /**
   * Deletes the run with the specified name.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (SyncLineageClient lineageClient = SyncLineageProducerClient.create()) {
   *   RunName name = RunName.of("[PROJECT]", "[LOCATION]", "[PROCESS]", "[RUN]");
   *   DeleteRunRequest request = DeleteRunRequest.newBuilder()
   *       .setName(name).build();
   *   lineageClient.deleteRun(request);
   * }
   * }</pre>
   *
   * @param request Required. The request object that will be used to execute API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  void deleteRun(DeleteRunRequest request) throws ExecutionException, InterruptedException;

  /**
   * Deletes the lineage event with the specified name.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (SyncLineageClient lineageClient = SyncLineageProducerClient.create()) {
   *   LineageEventName name =
   *       LineageEventName.of("[PROJECT]", "[LOCATION]", "[PROCESS]", "[RUN]", "[LINEAGE_EVENT]");
   *   DeleteLineageEventRequest request = DeleteLineageEventRequest.newBuilder()
   *       .setName(name).build();
   *   lineageClient.deleteLineageEvent(request);
   * }
   * }</pre>
   *
   * @param request Required. The request object that will be used to execute API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  void deleteLineageEvent(DeleteLineageEventRequest request);

  /**
   * Gets the details of the specified process.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (SyncLineageClient lineageClient = SyncLineageProducerClient.create()) {
   *   ProcessName name = ProcessName.of("[PROJECT]", "[LOCATION]", "[PROCESS]");
   *   GetProcessRequest request = GetProcessRequest.newBuilder().setName(name).build();
   *   Process response = lineageClient.getProcess(request);
   * }
   * }</pre>
   *
   * @param request Required. The request object that will be used to execute API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  Process getProcess(GetProcessRequest request);

  /**
   * Gets the details of the specified run.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (SyncLineageClient lineageClient = SyncLineageProducerClient.create()) {
   *   RunName name = RunName.of("[PROJECT]", "[LOCATION]", "[PROCESS]", "[RUN]");
   *   GetRunRequest request = GetRunRequest.newBuilder().setName(name).build();
   *   Run response = lineageClient.getRun(request);
   * }
   * }</pre>
   *
   * @param request Required. The request object that will be used to execute API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  Run getRun(GetRunRequest request);

  /**
   * Gets the details of the specified lineage event.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (SyncLineageClient lineageClient = SyncLineageProducerClient.create()) {
   *   LineageEventName name =
   *       LineageEventName.of("[PROJECT]", "[LOCATION]", "[PROCESS]", "[RUN]", "[LINEAGE_EVENT]");
   *   GetLineageEventRequest request = GetLineageEventRequest.newBuilder().setName(name).build();
   *   LineageEvent response = lineageClient.getLineageEvent(request);
   * }
   * }</pre>
   *
   * @param request Required. The request object that will be used to execute API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  LineageEvent getLineageEvent(GetLineageEventRequest request);

  /**
   * Lists processes in the given project and location. List order is descending by insertion time.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (SyncLineageClient lineageClient = SyncLineageProducerClient.create()) {
   *   LocationName parent = LocationName.of("[PROJECT]", "[LOCATION]");
   *   ListProcessesPagedResponse request = ListProcessesPagedResponse.newBuilder()
   *       .setParent(parent).build();
   *   for (Process element : lineageClient.listProcesses(request).iterateAll()) {
   *     // doThingsWith(element);
   *   }
   * }
   * }</pre>
   *
   * @param request Required. The request object that will be used to execute API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  ListProcessesPagedResponse listProcesses(ListProcessesRequest request);

  /**
   * Lists runs in the given project and location. List order is descending by `start_time`.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (SyncLineageClient lineageClient = SyncLineageProducerClient.create()) {
   *   ProcessName parent = ProcessName.of("[PROJECT]", "[LOCATION]", "[PROCESS]");
   *   ListRunsPagedResponse request = ListRunsPagedResponse.newBuilder()
   *    *       .setParent(parent).build();
   *   for (Run element : lineageClient.listRuns(request).iterateAll()) {
   *     // doThingsWith(element);
   *   }
   * }
   * }</pre>
   *
   * @param request Required. The request object that will be used to execute API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  ListRunsPagedResponse listRuns(ListRunsRequest request);

  /**
   * Lists lineage events in the given project and location. The list order is not defined.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (SyncLineageClient lineageClient = SyncLineageProducerClient.create()) {
   *   RunName parent = RunName.of("[PROJECT]", "[LOCATION]", "[PROCESS]", "[RUN]");
   *   ListLineageEventsRequest request = ListLineageEventsRequest.newBuilder()
   *       .setParent(parent).build();
   *   for (LineageEvent element : lineageClient.listLineageEvents(request).iterateAll()) {
   *     // doThingsWith(element);
   *   }
   * }
   * }</pre>
   *
   * @param request Required. The request object that will be used to execute API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  ListLineageEventsPagedResponse listLineageEvents(ListLineageEventsRequest request);

  /**
   * Processes OpenLineage message.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (SyncLineageClient lineageClient = SyncLineageProducerClient.create()) {
   *   ProcessOpenLineageRunEventRequest request = ProcessOpenLineageRunEventRequest.newBuilder()
   *       .setParent(parent).setOpenLineage(message).build();
   *   lineageClient.processOpenLineageRunEvent(request);
   * }
   * }</pre>
   *
   * @param request Required. The request object that will be used to execute API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  ProcessOpenLineageRunEventResponse processOpenLineageRunEvent(
      ProcessOpenLineageRunEventRequest request);
}
