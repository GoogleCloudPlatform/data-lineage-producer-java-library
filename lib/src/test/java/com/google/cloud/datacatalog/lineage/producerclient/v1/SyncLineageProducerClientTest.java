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

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.datacatalog.lineage.v1.ProcessOpenLineageRunEventRequest;
import com.google.cloud.datacatalog.lineage.v1.ProcessOpenLineageRunEventResponse;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.protobuf.Any;
import com.google.protobuf.Struct;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** Tests for SyncLineageProducerClient. */
public class SyncLineageProducerClientTest {

  private static final String PROJECT_NAME_AND_LOCATION = "projects/test/locations/test";
  private BasicLineageClient basicLineageClient;
  private SyncLineageProducerClient client;

  @Before
  public void setUp() throws IOException {
    basicLineageClient = mock(BasicLineageClient.class);
    client = SyncLineageProducerClient.create(basicLineageClient);
  }

  @After
  public void close() throws Exception {
    client.close();
  }

  @Test
  public void processOpenLineageRunEvent_supported() {
    ProcessOpenLineageRunEventRequest request = createProcessOpenLineageRunEventRequest();
    ProcessOpenLineageRunEventResponse response =
        ProcessOpenLineageRunEventResponse.newBuilder().build();
    when(basicLineageClient.processOpenLineageRunEventCallable())
        .thenReturn(
            new UnaryCallableFake<
                ProcessOpenLineageRunEventRequest, ProcessOpenLineageRunEventResponse>(
                r -> response));

    ProcessOpenLineageRunEventResponse gotResponse = client.processOpenLineageRunEvent(request);

    assertThat(gotResponse).isEqualTo(response);
  }

  @Test
  public void propagatesException() {
    ProcessOpenLineageRunEventRequest request = createProcessOpenLineageRunEventRequest();

    when(basicLineageClient.processOpenLineageRunEventCallable())
        .thenThrow(InvalidArgumentException.class);

    assertThrows(InvalidArgumentException.class, () -> client.processOpenLineageRunEvent(request));
  }

  @Test
  public void apiDisabled_doesNotRetry() {
    returnServiceDisabledFromMocker();
    ProcessOpenLineageRunEventRequest request =
        createProcessOpenLineageRunEventRequest("projects/test-api-disabled-sync/locations/test");

    // For the first call, throw a SERVICE_DISABLED exception
    assertThrows(
        UncheckedExecutionException.class, () -> client.processOpenLineageRunEvent(request));
    // Not attempt the second call
    assertThat(
        assertThrows(ApiException.class, () -> client.processOpenLineageRunEvent(request))
            .getMessage()
            .contains("Data Lineage API is disabled in project"));
  }

  private static Struct someOpenLineage() {
    return Struct.newBuilder().build();
  }

  private static ProcessOpenLineageRunEventRequest createProcessOpenLineageRunEventRequest() {
    return createProcessOpenLineageRunEventRequest(PROJECT_NAME_AND_LOCATION);
  }

  private static ProcessOpenLineageRunEventRequest createProcessOpenLineageRunEventRequest(
      String parent) {
    return ProcessOpenLineageRunEventRequest.newBuilder()
        .setParent(parent)
        .setOpenLineage(someOpenLineage())
        .build();
  }

  /**
   * Configure the BasicLineageClient mocker to return an exception indicating Lineage API
   * disablement.
   */
  private void returnServiceDisabledFromMocker() {
    when(basicLineageClient.processOpenLineageRunEventCallable())
        .thenReturn(
            new UnaryCallable<
                ProcessOpenLineageRunEventRequest, ProcessOpenLineageRunEventResponse>() {
              @Override
              public ApiFuture<ProcessOpenLineageRunEventResponse> futureCall(
                  ProcessOpenLineageRunEventRequest request, ApiCallContext context) {
                Status.Builder statusBuilder = com.google.rpc.Status.newBuilder();
                statusBuilder.setCode(Code.PERMISSION_DENIED.getNumber());
                ErrorInfo.Builder errorInfoBuilder =
                    ErrorInfo.newBuilder().setReason("SERVICE_DISABLED");

                statusBuilder.addDetails(Any.pack(errorInfoBuilder.build()));

                return ApiFutures.immediateFailedFuture(
                    StatusProto.toStatusException(statusBuilder.build()));
              }
            });
  }
}
