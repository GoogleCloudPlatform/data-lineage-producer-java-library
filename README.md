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
## Performing calls
To perform a call, you need to:
1. Create a client
2. Create a request object
3. Call a corresponding method

Sample code:
```
SyncLineageProducerClient client = SyncLineageProducerClient.create();
ProcessOpenLineageRunEventRequest request =
        ProcessOpenLineageRunEventRequest.newBuilder()
            .setParent(parent)
            .setOpenLineage(openLineageMessage)
            .build();
client.processOpenLineageRunEvent(request);
```