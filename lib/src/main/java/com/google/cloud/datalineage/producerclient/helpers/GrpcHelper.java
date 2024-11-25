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
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;

/** Set of helpers for Grpc handling. */
public class GrpcHelper {

  /** Make this helper class non-instantiable */
  private GrpcHelper() {}

  /**
   * Lets getting reason field from grpc response.
   *
   * @param grpcException - error returned form grpc call
   * @return string with value from reason field
   */
  public static String getReason(Throwable grpcException) {
    Status statusProto = StatusProto.fromThrowable(grpcException);
    if (statusProto == null) {
      throw new IllegalArgumentException("Provided throwable is not a Grpc exception");
    }
    /* Status is a standard way to represent API error.
     * This model consists of code, message and details.
     * ErrorInfo is a type of details that contains reason field.
     * Status stores details as a list, because:
     * - there is no set of required details,
     * - user can introduce new ones.
     * That's why in order to get ErrorInfo, we need to iterate over details and check all of them.
     */
    for (Any any : statusProto.getDetailsList()) {
      if (any.is(ErrorInfo.class)) {
        try {
          return any.unpack(ErrorInfo.class).getReason();
        } catch (InvalidProtocolBufferException exception) {
          throw new IllegalArgumentException("Invalid protocol buffer message", exception);
        }
      }
    }
    throw new IllegalArgumentException("Message does not contain ErrorInfo", grpcException);
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
