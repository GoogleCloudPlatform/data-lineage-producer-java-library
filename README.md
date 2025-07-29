# Data Lineage Producer Java Library

Java producer library for [Data Lineage][product-docs]. 

[product-docs]: https://cloud.google.com/data-catalog/docs/data-lineage/
The library provides Synchronous and Asynchronous clients for interacting with Data Lineage API.

## Building
To build the library, run
```
./gradlew build
```
Or, for a shadow jar:
```
./gradlew shadowJar
```

The output will be located at `lib/build/libs`.

## Logging

This library uses [SLF4J](https://www.slf4j.org/) for logging. To see log output, you need to include an SLF4J binding in your application dependencies.

### Adding a logging implementation

For Logback (recommended):
```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.14</version>
</dependency>
```

For Log4j2:
```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j2-impl</artifactId>
    <version>2.20.0</version>
</dependency>
```

### Configuring log levels

The library logs at different levels:
- **INFO**: Client creation, cache initialization, shutdown events
- **WARN**: Service disruptions, graceful shutdown timeouts, API errors
- **DEBUG**: Detailed API call information, cache operations

Example Logback configuration (`logback.xml`):
```xml
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Set debug level for data lineage producer client -->
    <logger name="com.google.cloud.datalineage.producerclient" level="DEBUG"/>
    
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

## Performing calls
To perform a call, you need to:
1. Create a client
2. Create a request object
3. Call a corresponding method

Sample code:
```java
SyncLineageProducerClient client = SyncLineageProducerClient.create();
ProcessOpenLineageRunEventRequest request =
        ProcessOpenLineageRunEventRequest.newBuilder()
            .setParent(parent)
            .setOpenLineage(openLineageMessage)
            .build();
client.processOpenLineageRunEvent(request);
```

Sample code to compose an OpenLineage message:
```java
Struct job = Struct.newBuilder()
    .putFields("namespace", Value.newBuilder().setStringValue("namespace-1").build())
    .putFields("name", Value.newBuilder().setStringValue("job-1").build()).build();
Struct run = Struct.newBuilder()
    .putFields("runId", Value.newBuilder().setStringValue("1234-5678").build()).build();
Struct openLineageMessage = Struct.newBuilder()
    .putFields("eventType", Value.newBuilder().setStringValue("COMPLETE").build())
    .putFields("eventTime",
        Value.newBuilder().setStringValue("2023-04-04T13:21:16.098Z").build())
    .putFields("producer", Value.newBuilder().setStringValue("producer-1").build())
    .putFields("schemaURL", Value.newBuilder()
        .setStringValue("https://openlineage.io/spec/1-0-5/OpenLineage.json#/$defs/RunEvent")
        .build())
    .putFields("job", Value.newBuilder().setStructValue(job).build())
    .putFields("run", Value.newBuilder().setStructValue(run).build())
    .putFields("inputs",
        Value.newBuilder().setListValue(ListValue.newBuilder().addValues(Value.newBuilder()
            .setStructValue(Struct.newBuilder()
                .putFields("namespace", Value.newBuilder().setStringValue("bigquery").build())
                .putFields("name",
                    Value.newBuilder().setStringValue("input_project.input_dataset.input_table")
                        .build()).build()).build()).build()).build())
    .putFields("outputs",
        Value.newBuilder().setListValue(ListValue.newBuilder().addValues(Value.newBuilder()
            .setStructValue(Struct.newBuilder()
                .putFields("namespace", Value.newBuilder().setStringValue("bigquery").build())
                .putFields("name", Value.newBuilder()
                    .setStringValue("output_project.output_dataset.output_table").build())
                .build()).build()).build()).build()).build();
```

or, to convert it from an existing string:

```java
Struct openLineageStruct = OpenLineageHelper.jsonToStruct(jsonString);
```