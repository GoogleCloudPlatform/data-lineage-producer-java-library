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

package com.google.cloud.datacatalog.lineage.producerclient.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Helper class created for handling resources names */
public class NamesHelper {
  private static final String PROJECT_NAME_AND_LOCATION_GROUP_NAME = "projectNameAndLocation";
  private static final Pattern RESOURCE_PATTERN =
      Pattern.compile(
          "^(?<" + PROJECT_NAME_AND_LOCATION_GROUP_NAME + ">projects/[^/]+/locations/[^/]+).*$");

  public static String getProjectNameWithLocationFromResourceName(String resourceName) {
    Matcher matcher = RESOURCE_PATTERN.matcher(resourceName);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Name of the resource is not valid");
    }
    return matcher.group(PROJECT_NAME_AND_LOCATION_GROUP_NAME);
  }
}
