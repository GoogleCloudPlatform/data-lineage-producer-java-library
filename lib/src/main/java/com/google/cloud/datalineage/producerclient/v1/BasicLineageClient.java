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

import com.google.api.gax.core.BackgroundResource;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.api.gax.rpc.UnaryCallable;
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

/**
 * Represents a set of basic methods from LineageClient. This set of methods is sufficient for
 * communication with Lineage API.
 *
 * <p>Interface is introduced to provide a way to swap real client with a fake one.
 */
public interface BasicLineageClient extends BackgroundResource {
  UnaryCallable<DeleteLineageEventRequest, Empty> deleteLineageEventCallable();

  OperationFuture<Empty, OperationMetadata> deleteProcessAsync(DeleteProcessRequest request);

  OperationFuture<Empty, OperationMetadata> deleteRunAsync(DeleteRunRequest request);

  UnaryCallable<GetLineageEventRequest, LineageEvent> getEventCallable();

  UnaryCallable<GetProcessRequest, Process> getProcessCallable();

  UnaryCallable<GetRunRequest, Run> getRunCallable();

  UnaryCallable<ListLineageEventsRequest, ListLineageEventsPagedResponse>
      listLineageEventsPagedCallable();

  UnaryCallable<ListProcessesRequest, ListProcessesPagedResponse> listProcessesPagedCallable();

  UnaryCallable<ListRunsRequest, ListRunsPagedResponse> listRunsPagedCallable();

  UnaryCallable<ProcessOpenLineageRunEventRequest, ProcessOpenLineageRunEventResponse>
      processOpenLineageRunEventCallable();
}
