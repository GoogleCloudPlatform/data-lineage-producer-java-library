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
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import io.grpc.protobuf.StatusProto;
import org.junit.Test;
import org.junit.runner.RunWith;

/** * Test suite for NamesHelper */
@RunWith(TestParameterInjector.class)
public class GrpcHelperTest {

  @Test
  public void getReason_withNonGrpcException_throwsIllegalArgumentException() {
    Throwable nonGrpcException = new IllegalArgumentException("This is not a gRPC exception");

    assertThrows(IllegalArgumentException.class, () -> GrpcHelper.getReason(nonGrpcException));
  }

  @Test
  public void getReason_withGrpcExceptionWithoutErrorInfo_throwsIllegalArgumentException() {
    Throwable grpcException = StatusProto.toStatusRuntimeException(Status.newBuilder().build());
    assertThrows(IllegalArgumentException.class, () -> GrpcHelper.getReason(grpcException));
  }

  @Test
  public void getReason_withGrpcExceptionWithErrorInfo_returnsReason() {
    String expectedReason = "test-reason";
    ErrorInfo errorInfo = ErrorInfo.newBuilder().setReason(expectedReason).build();
    Throwable grpcException =
        StatusProto.toStatusRuntimeException(
            Status.newBuilder().addDetails(Any.pack(errorInfo)).build());

    String reason = GrpcHelper.getReason(grpcException);

    assertThat(reason).isEqualTo(expectedReason);
  }
}
