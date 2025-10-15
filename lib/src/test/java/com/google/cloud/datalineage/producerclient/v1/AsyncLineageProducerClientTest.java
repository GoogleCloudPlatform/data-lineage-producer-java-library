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

import static com.google.cloud.datalineage.producerclient.v1.AsyncLineageProducerClientSettings.DEFAULT_GRACEFUL_SHUTDOWN_DURATION;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.threeten.bp.Duration.ofSeconds;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.datacatalog.lineage.v1.ProcessOpenLineageRunEventRequest;
import com.google.cloud.datacatalog.lineage.v1.ProcessOpenLineageRunEventResponse;
import com.google.protobuf.Any;
import com.google.protobuf.Struct;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.StatusException;
import io.grpc.protobuf.StatusProto;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.Duration;

/** Tests for AsyncLineageProducerClient. */
public class AsyncLineageProducerClientTest {

  private static final String PROJECT_NAME_AND_LOCATION = "projects/test/locations/test";
  private BasicLineageClient basicLineageClient;
  private AsyncLineageProducerClient client;

  @Before
  public void setUp() throws IOException {
    basicLineageClient = mock(BasicLineageClient.class);
    client = AsyncLineageProducerClient.create(basicLineageClient);
  }

  @After
  public void close() throws Exception {
    client.close();
  }

  @Test
  public void processOpenLineageRunEvent_supported()
      throws ExecutionException, InterruptedException {
    ProcessOpenLineageRunEventRequest request = createProcessOpenLineageRunEventRequest();
    ProcessOpenLineageRunEventResponse response =
        ProcessOpenLineageRunEventResponse.newBuilder().build();
    when(basicLineageClient.processOpenLineageRunEventCallable())
        .thenReturn(new UnaryCallableFake<>(r -> response));

    ProcessOpenLineageRunEventResponse gotResponse =
        client.processOpenLineageRunEvent(request).get();

    assertThat(gotResponse).isEqualTo(response);
  }

  @Test
  public void propagatesException() {
    ProcessOpenLineageRunEventRequest request = createProcessOpenLineageRunEventRequest();

    when(basicLineageClient.processOpenLineageRunEventCallable())
        .thenThrow(InvalidArgumentException.class);

    assertThrows(
        InvalidArgumentException.class, () -> client.processOpenLineageRunEvent(request).get());
  }

  @Test
  public void apiDisabled_doesNotRetry() {
    returnServiceDisabledFromMocker();
    ProcessOpenLineageRunEventRequest request =
        createProcessOpenLineageRunEventRequest("projects/test-api-disabled-async/locations/test");

    // For the first call, throw a SERVICE_DISABLED exception
    assertThrows(ExecutionException.class, () -> client.processOpenLineageRunEvent(request).get());
    // Not attempt the second call
    assertThat(
        assertThrows(ApiException.class, () -> client.processOpenLineageRunEvent(request))
            .getMessage()
            .contains("Data Lineage API is disabled in project"));
  }

  @Test
  public void lineageDisabled_doesNotRetry() {
    returnLineageDisabledFromMocker();
    ProcessOpenLineageRunEventRequest request =
        createProcessOpenLineageRunEventRequest(
            "projects/test-lineage-disabled-async/locations/test");

    // For the first call, throw a PERMISSION_DENIED exception
    assertThrows(ExecutionException.class, () -> client.processOpenLineageRunEvent(request).get());
    // Not attempt the second call
    ApiException exception =
        assertThrows(ApiException.class, () -> client.processOpenLineageRunEvent(request));
    assertThat(exception.getMessage())
        .contains("Lineage is not enabled in Lineage Configurations for project");
  }

  @Test
  public void gracefulShutdown_awaitsTerminationByDefault() throws Exception {
    // objects passed to lambda must be final or effectively final, so we use arrays to store the
    // values
    long[] resultAwaitTerminationTime = new long[1];
    AsyncLineageProducerClient asyncLineageProducerClient =
        AsyncLineageProducerClient.create(basicLineageClient);
    doAnswer(
            invocation -> {
              resultAwaitTerminationTime[0] = invocation.getArgument(0);
              return true;
            })
        .when(basicLineageClient)
        .awaitTermination(anyLong(), any(TimeUnit.class));

    asyncLineageProducerClient.close();
    // Verify that the awaitTermination was called with the expected values
    // we cannot get the resultAwaitTerminationTime exactly, so we check reasonable scope
    assertThat(Duration.ofNanos(resultAwaitTerminationTime[0])).isAtLeast(Duration.ZERO);
    assertThat(Duration.ofNanos(resultAwaitTerminationTime[0]))
        .isAtMost(DEFAULT_GRACEFUL_SHUTDOWN_DURATION.plus(ofSeconds(1)));
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
            new UnaryCallable<>() {
              @Override
              public ApiFuture<ProcessOpenLineageRunEventResponse> futureCall(
                  ProcessOpenLineageRunEventRequest request, ApiCallContext context) {

                return ApiFutures.immediateFailedFuture(
                    createStatusExceptionWithReasons("SERVICE_DISABLED", "SOME_OTHER_REASON"));
              }
            });
  }

  /**
   * Configure the BasicLineageClient mocker to return an exception indicating Lineage is not
   * enabled.
   */
  private void returnLineageDisabledFromMocker() {
    when(basicLineageClient.processOpenLineageRunEventCallable())
        .thenReturn(
            new UnaryCallable<>() {
              @Override
              public ApiFuture<ProcessOpenLineageRunEventResponse> futureCall(
                  ProcessOpenLineageRunEventRequest request, ApiCallContext context) {

                return ApiFutures.immediateFailedFuture(
                    createStatusExceptionWithReasons(
                        "LINEAGE_INGESTION_DISABLED", "SOME_OTHER_REASON"));
              }
            });
  }

  private StatusException createStatusExceptionWithReasons(String... reasons) {
    Status.Builder statusBuilder = com.google.rpc.Status.newBuilder();
    statusBuilder.setCode(Code.PERMISSION_DENIED.getNumber());
    statusBuilder.addAllDetails(
        Arrays.stream(reasons)
            .map(r -> Any.pack(ErrorInfo.newBuilder().setReason(r).build()))
            .collect(Collectors.toList()));
    return StatusProto.toStatusException(statusBuilder.build());
  }
}
