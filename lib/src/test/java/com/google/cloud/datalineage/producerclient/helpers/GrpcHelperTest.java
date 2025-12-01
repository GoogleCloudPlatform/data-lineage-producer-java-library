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

package com.google.cloud.datalineage.producerclient.helpers;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.protobuf.Any;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Test suite for GrpcHelper */
@RunWith(JUnit4.class)
public class GrpcHelperTest {

  @Test
  public void getReasons_withNonGrpcException_throws() {
    Throwable nonGrpcException = new IllegalArgumentException("This is not a gRPC exception");

    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class, () -> GrpcHelper.getErrorReasons(nonGrpcException));
    assertThat(thrown).hasMessageThat().contains("Provided throwable is not a gRPC exception");
  }

  @Test
  public void getReasons_withGrpcExceptionWithoutErrorInfo_returnsEmptySet() {
    Throwable grpcException = StatusProto.toStatusRuntimeException(Status.newBuilder().build());

    assertThat(GrpcHelper.getErrorReasons(grpcException)).isEmpty();
  }

  @Test
  public void getReasons_withGrpcExceptionWithErrorInfo_returnsReason() {
    String expectedReason = "test-reason";
    ErrorInfo errorInfo = ErrorInfo.newBuilder().setReason(expectedReason).build();
    Throwable grpcException =
        StatusProto.toStatusRuntimeException(
            Status.newBuilder().addDetails(Any.pack(errorInfo)).build());

    assertThat(GrpcHelper.getErrorReasons(grpcException)).containsExactly(expectedReason);
  }

  @Test
  public void getReasons_withGrpcExceptionWithMultipleErrorInfo_returnsReasons() {
    ErrorInfo errorInfoOne = ErrorInfo.newBuilder().setReason("reason1").build();
    ErrorInfo errorInfoTwo = ErrorInfo.newBuilder().setReason("reason2").build();
    Throwable grpcException =
        StatusProto.toStatusRuntimeException(
            Status.newBuilder()
                .addDetails(Any.pack(errorInfoOne))
                .addDetails(Any.pack(errorInfoTwo))
                .build());

    assertThat(GrpcHelper.getErrorReasons(grpcException)).containsExactly("reason1", "reason2");
  }
}
