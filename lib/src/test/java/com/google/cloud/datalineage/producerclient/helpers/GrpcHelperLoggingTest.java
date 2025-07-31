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
  public void testGetReason_ValidErrorInfo_LogsSuccess() {
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
    String reason = GrpcHelper.getReason(exception);

    // Verify the result
    assertThat(reason).isEqualTo("API_DISABLED");

    // Verify debug logging
    boolean found =
        testAppender.getMessagesAtLevel(Level.DEBUG).stream()
            .anyMatch(
                log -> log.contains("Successfully extracted reason from ErrorInfo: API_DISABLED"));
    assertThat(found).isTrue();
  }

  @Test
  public void testGetReason_InvalidProtocolBuffer_LogsError() {
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

    // Call the method and expect an exception
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> GrpcHelper.getReason(exception));

    assertThat(thrown.getMessage()).contains("Invalid protocol buffer message");

    // Verify error logging
    boolean found =
        testAppender.getMessagesAtLevel(Level.ERROR).stream()
            .anyMatch(
                log -> log.contains("Invalid protocol buffer message while extracting ErrorInfo"));
    assertThat(found).isTrue();
  }

  @Test
  public void testGetReason_NoErrorInfo_LogsWarning() {
    testAppender.clear(); // Clear any existing logs

    // Create a Status without ErrorInfo
    Status status =
        Status.newBuilder()
            .setCode(com.google.rpc.Code.FAILED_PRECONDITION_VALUE)
            .setMessage("API is disabled")
            .build();

    StatusRuntimeException exception = StatusProto.toStatusRuntimeException(status);

    // Call the method and expect an exception
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> GrpcHelper.getReason(exception));

    assertThat(thrown.getMessage()).contains("Message does not contain ErrorInfo");

    // Verify warning logging
    boolean found =
        testAppender.getMessagesAtLevel(Level.WARN).stream()
            .anyMatch(
                log ->
                    log.contains(
                        "Message does not contain ErrorInfo for exception:"
                            + " FAILED_PRECONDITION: API is disabled"));
    assertThat(found).isTrue();
  }

  @Test
  public void testGetReason_NonGrpcException_ThrowsException() {
    testAppender.clear(); // Clear any existing logs

    // Create a non-gRPC exception
    RuntimeException nonGrpcException = new RuntimeException("Not a gRPC exception");

    // Call the method and expect an exception
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> GrpcHelper.getReason(nonGrpcException));

    assertThat(thrown.getMessage()).contains("Provided throwable is not a Grpc exception");

    // Note: The current implementation doesn't log this case, but if we wanted to test it,
    // we would need to modify the GrpcHelper to add logging for this scenario.
  }
}
