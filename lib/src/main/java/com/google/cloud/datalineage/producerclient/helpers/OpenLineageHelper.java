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

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;

/** Helper class for working with OpenLineage messages */
public class OpenLineageHelper {

  /** Converts a valid JSON String to a protobuf Struct */
  public static Struct jsonToStruct(String json) throws InvalidProtocolBufferException {
    Struct.Builder message = Struct.newBuilder();
    JsonFormat.parser().ignoringUnknownFields().merge(json, message);
    return message.build();
  }
}
