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

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ListValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Test suite for OpenLineageHelper */
@RunWith(JUnit4.class)
public class OpenLineageHelperTest {

  @Test
  public void jsonToStruct_convertsValidJson() throws Exception {
    String validJson =
        "{ \"key\": \"value\", \"keyList\": [ 1, 2, 3], \"keyStruct\": { \"key\": \"value\"}}";
    Struct actualStruct = OpenLineageHelper.jsonToStruct(validJson);
    assertThat(actualStruct)
        .isEqualTo(
            Struct.newBuilder()
                .putFields("key", Value.newBuilder().setStringValue("value").build())
                .putFields(
                    "keyList",
                    Value.newBuilder()
                        .setListValue(
                            ListValue.newBuilder()
                                .addValues(Value.newBuilder().setNumberValue(1))
                                .addValues(Value.newBuilder().setNumberValue(2))
                                .addValues(Value.newBuilder().setNumberValue(3)))
                        .build())
                .putFields(
                    "keyStruct",
                    Value.newBuilder()
                        .setStructValue(
                            Struct.newBuilder()
                                .putFields(
                                    "key", Value.newBuilder().setStringValue("value").build())
                                .build())
                        .build())
                .build());
  }

  @Test
  public void jsonToStruct_whenInvalidJson_throwsInvalidProtobufException() {
    String invalidJson = "invalid json";
    assertThrows(
        InvalidProtocolBufferException.class, () -> OpenLineageHelper.jsonToStruct(invalidJson));
  }
}
