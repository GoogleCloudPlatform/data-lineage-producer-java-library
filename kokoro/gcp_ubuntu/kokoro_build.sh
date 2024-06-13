#!/bin/bash

# Copyright 2024 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Fail on any error
set -e

sudo update-java-alternatives --set java-1.11.0-openjdk-amd64
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

cd "${KOKORO_ARTIFACTS_DIR}/git/data-lineage-producer-java-library"
./tests.sh
./build.sh
