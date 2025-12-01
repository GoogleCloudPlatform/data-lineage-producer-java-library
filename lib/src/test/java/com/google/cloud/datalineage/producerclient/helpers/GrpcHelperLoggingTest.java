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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.google.cloud.datalineage.producerclient.test.TestLogAppender;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.Any;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.LoggerFactory;

/** Tests logging functionality in GrpcHelper. */
@RunWith(JUnit4.class)
public class GrpcHelperLoggingTest {

  private TestLogAppender testAppender;
  private Logger logger;

  @Before
  public void setUp() {
    // Set up logging capture
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    logger = loggerContext.getLogger(GrpcHelper.class);

    testAppender = new TestLogAppender();
    testAppender.setContext(loggerContext);
    testAppender.start();

    logger.addAppender(testAppender);
    logger.setLevel(Level.DEBUG); // Enable debug logging for tests
  }

  @After
  public void tearDown() {
    if (logger != null && testAppender != null) {
      logger.detachAppender(testAppender);
    }
    if (testAppender != null) {
      testAppender.stop();
    }
  }

  @Test
  public void testGetReasons_validErrorInfo_logsSuccess() {
    testAppender.clear(); // Clear any existing logs

    // Create an ErrorInfo with a reason
    ErrorInfo errorInfo =
        ErrorInfo.newBuilder().setReason("API_DISABLED").setDomain("googleapis.com").build();

    // Create a Status with the ErrorInfo
    Status status =
        Status.newBuilder()
            .setCode(com.google.rpc.Code.FAILED_PRECONDITION_VALUE)
            .setMessage("API is disabled")
            .addDetails(Any.pack(errorInfo))
            .build();

    // Create a gRPC exception from the status
    StatusRuntimeException exception = StatusProto.toStatusRuntimeException(status);

    // Call the method
    ImmutableSet<String> reason = GrpcHelper.getErrorReasons(exception);

    // Verify the result
    assertThat(reason).containsExactly("API_DISABLED");

    // Verify debug logging
    assertThat(testAppender.getMessagesAtLevel(Level.DEBUG))
        .contains("Successfully extracted reason from ErrorInfo: API_DISABLED");
  }

  @Test
  public void testGetReasons_invalidProtocolBuffer_logsWarnAndReturnsNull() {
    testAppender.clear(); // Clear any existing logs

    // Create a malformed Any that can't be unpacked
    Any invalidAny =
        Any.newBuilder()
            .setTypeUrl("type.googleapis.com/google.rpc.ErrorInfo")
            .setValue(com.google.protobuf.ByteString.copyFromUtf8("invalid-data"))
            .build();

    Status status =
        Status.newBuilder()
            .setCode(com.google.rpc.Code.FAILED_PRECONDITION_VALUE)
            .setMessage("API is disabled")
            .addDetails(invalidAny)
            .build();

    StatusRuntimeException exception = StatusProto.toStatusRuntimeException(status);

    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> GrpcHelper.getErrorReasons(exception));

    assertThat(thrown).hasMessageThat().contains("Invalid protocol buffer message");
    assertThat(testAppender.getMessagesAtLevel(Level.ERROR))
        .contains("Invalid protocol buffer message while extracting ErrorInfo");
  }

  @Test
  public void testGetReasons_nonGrpcException_returnsNull() {
    testAppender.clear(); // Clear any existing logs

    // Create a non-gRPC exception
    RuntimeException nonGrpcException = new RuntimeException("Not a gRPC exception");

    // Call the method
    assertThrows(
        IllegalArgumentException.class, () -> GrpcHelper.getErrorReasons(nonGrpcException));

    // Verify debug logging
    assertThat(testAppender.getMessagesAtLevel(Level.ERROR))
        .contains("Provided throwable is not a gRPC exception: java.lang.RuntimeException");
  }
}
