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

import com.google.testing.junit.testparameterinjector.TestParameter;
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/** * Test suite for NamesHelper */
@RunWith(TestParameterInjector.class)
public class NamesHelperTest {

  @Test
  public void getProjectNameWithLocationFromResourceName_baseCase(
      @TestParameter({
         "a",
         "bcd123",
         "efg-hij",
         "klm-0456789-nopqrs",
         "0",
         "112355",
         "9223372036854775807",
          })
          String projectNamesOrId) {
    String projectName = "projects/" + projectNamesOrId + "/locations/location";
    String processName = projectName + "/processes/process";
    String runNames = processName + "/runs/run";
    String eventNames = runNames + "/lineageEvents/lineage_event";

    String[] results =
        Stream.of(projectName, processName, runNames, eventNames)
            .map(NamesHelper::getProjectNameWithLocationFromResourceName)
            .toArray(String[]::new);

    for (String result : results) {
      Assert.assertEquals(result, projectName);
    }
  }

  @Test
  public void
      getProjectNameWithLocationFromResourceName_throwsErrorWhenProjectNameDoesNotMatchTheFormat(
          @TestParameter({"a", "1", "project/asd/region/us", "projects//a/locations/us"})
              String incorrectName) {
    Assert.assertThrows(
        IllegalArgumentException.class,
        () -> NamesHelper.getProjectNameWithLocationFromResourceName(incorrectName));
  }
}
