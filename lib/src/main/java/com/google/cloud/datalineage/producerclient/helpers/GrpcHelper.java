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

import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.StatusCode.Code;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import lombok.extern.slf4j.Slf4j;

/** Set of helpers for Grpc handling. */
@Slf4j
public class GrpcHelper {

  /** Make this helper class non-instantiable */
  private GrpcHelper() {}

  /**
   * Returns a set of error reasons from <code>com.google.rpc.Status</code> of a gRPC Exception.
   *
   * @param grpcException - error returned form grpc call
   * @return a set of strings with reasons extracted from Status if any
   */
  public static ImmutableSet<String> getErrorReasons(Throwable grpcException) {
    log.debug("Extracting reasons from gRPC exception: {}", grpcException.getMessage());
    Status statusProto = StatusProto.fromThrowable(grpcException);
    if (statusProto == null) {
      log.error(
          "Provided throwable is not a gRPC exception: {}", grpcException.getClass().getName());
      throw new IllegalArgumentException("Provided throwable is not a gRPC exception");
    }
    /* Status is a standard way to represent API error.
     * This model consists of code, message and details.
     * ErrorInfo is a type of details that contains reason field.
     * Status stores details as a list, because:
     * - there is no set of required details,
     * - user can introduce new ones.
     * That's why in order to get ErrorInfo, we need to iterate over details and check all of them.
     */
    return statusProto.getDetailsList().stream()
        .filter((detail) -> detail.is(ErrorInfo.class))
        .map(
            (errorInfo) -> {
              try {
                String reason = errorInfo.unpack(ErrorInfo.class).getReason();
                log.debug("Successfully extracted reason from ErrorInfo: {}", reason);
                return reason;
              } catch (InvalidProtocolBufferException e) {
                log.error("Invalid protocol buffer message while extracting ErrorInfo", e);
                throw new IllegalArgumentException("Invalid protocol buffer message", e);
              }
            })
        .collect(ImmutableSet.toImmutableSet());
  }

  /**
   * Creates StatusCode based on StatusCode.Code enum.
   *
   * @param code - error code
   * @return StatusCode representing passed error code
   */
  public static StatusCode getStatusCodeFromCode(Code code) {
    return new StatusCode() {
      @Override
      public Code getCode() {
        return code;
      }

      @Override
      public Object getTransportCode() {
        return getCode();
      }
    };
  }
}
