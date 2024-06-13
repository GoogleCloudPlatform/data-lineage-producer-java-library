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

package com.google.cloud.datacatalog.lineage.producerclient.v1;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.UnaryCallable;
import java.util.function.Function;

/**
 * * UnaryCallable Fake.
 *
 * <p>Takes 'map' function as a constructor parameter. When called, immediately returns value
 * returned by 'map' function.
 */
public class UnaryCallableFake<RequestT, ResponseT> extends UnaryCallable<RequestT, ResponseT> {
  private final Function<RequestT, ResponseT> requestToResponseMapper;

  UnaryCallableFake(Function<RequestT, ResponseT> map) {
    requestToResponseMapper = map;
  }

  @Override
  public ApiFuture<ResponseT> futureCall(RequestT request, ApiCallContext context) {
    return ApiFutures.immediateFuture(requestToResponseMapper.apply(request));
  }
}
