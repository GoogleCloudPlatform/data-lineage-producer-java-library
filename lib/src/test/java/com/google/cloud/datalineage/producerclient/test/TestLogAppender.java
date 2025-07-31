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

package com.google.cloud.datalineage.producerclient.test;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Test appender for capturing log messages during tests. */
public class TestLogAppender extends AppenderBase<ILoggingEvent> {
  private final List<ILoggingEvent> events = Collections.synchronizedList(new ArrayList<>());

  @Override
  protected void append(ILoggingEvent event) {
    events.add(event);
  }

  public List<ILoggingEvent> getEvents() {
    return new ArrayList<>(events);
  }

  public List<String> getMessages() {
    return events.stream()
        .map(ILoggingEvent::getFormattedMessage)
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  public List<String> getMessagesAtLevel(ch.qos.logback.classic.Level level) {
    return events.stream()
        .filter(event -> event.getLevel().equals(level))
        .map(ILoggingEvent::getFormattedMessage)
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  public void clear() {
    events.clear();
  }

  public int size() {
    return events.size();
  }
}
