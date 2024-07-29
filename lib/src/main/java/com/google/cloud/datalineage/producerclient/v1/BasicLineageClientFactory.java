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

import com.google.cloud.datalineage.producerclient.helpers.FunctionWithException;
import java.io.IOException;

/** BasicLineageClient that can be used to replace underlying client (for example for testing). */
final class BasicLineageClientFactory {
  private static FunctionWithException<LineageBaseSettings, BasicLineageClient, IOException>
      constructor = StandardLineageClient::create;

  public static void register(
      FunctionWithException<LineageBaseSettings, BasicLineageClient, IOException> constructor) {
    BasicLineageClientFactory.constructor = constructor;
  }

  public static BasicLineageClient get(LineageBaseSettings settings) throws IOException {
    return constructor.apply(settings);
  }
}
