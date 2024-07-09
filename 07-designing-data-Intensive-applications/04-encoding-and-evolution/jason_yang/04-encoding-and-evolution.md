**Inevitability of Change**:

- Applications change over time. New features are added, user requirements change, and business circumstances evolve, necessitating changes.

**Need for Data Changes**:

- When application features change, stored data also needs to change. This might involve adding new fields or record types or representing existing data differently.

**Approaches to Handling Data Model Changes**:

- Relational databases maintain a single schema, and schema changes are made using `ALTER` commands.
- Schema-less databases can store various types of data simultaneously.

**Coexistence of Code and Data Formats**:

- In large-scale applications, code changes do not happen instantly. New code and existing code, new data formats and existing data formats may coexist.
- To manage this, bidirectional compatibility must be maintained:
    - **Backward Compatibility**: New code must be able to read data written by the old code.
    - **Forward Compatibility**: Old code must be able to read data written by the new code.

**Data Encoding Formats**:

- Data encoding formats such as JSON, XML, Protocol Buffers, Thrift, and Avro handle schema changes and support the coexistence of old and new data and code.

**Data Storage and Communication**:

- These formats explain how they are used in data storage, web services (REST), remote procedure calls (RPC), and message queues.

## Formats for Encoding Data

Programs handle data in two ways:

**In-Memory Data**:

- Stored as objects, structs, lists, arrays, hash tables, trees, etc.
- Optimized for efficient access and manipulation by the CPU.

**Data for File Storage or Network Transmission**:

- Encoded as self-contained byte sequences, like JSON documents.
- Byte sequence representation differs from in-memory data structures due to the lack of pointers, which cannot be understood by other processes.

Transformation between these two representations is required:

- **Encoding (Serialization/Marshaling)**: Converts in-memory data into a byte sequence.
- **Decoding (Parsing/Deserialization/Unmarshaling)**: Converts a byte sequence back into in-memory data.

**Notes**

- Encoding is not related to encryption.
- To avoid confusion with the term 'serialization' used in transactions, 'encoding' is used here.

**Various Libraries and Encoding Formats**

- There are many libraries and formats related to data encoding.

### Language-Specific Formats

Many programming languages have built-in support for encoding in-memory objects into byte sequences. For example, Java uses `java.io.Serializable`, Ruby uses `Marshal`, and Python uses `pickle`. Additionally, third-party libraries like Kryo exist.

While these encoding libraries are convenient, they have some serious issues:

- **Language Dependence**: Encoding is specific to a programming language, making it hard to read data from other languages. This reliance can make long-term dependency on the current programming language and integration with other systems difficult.
- **Security Issues**: To restore data to the same object type, arbitrary classes must be instantiated during decoding. This can cause security problems, allowing attackers to execute arbitrary code remotely by decoding a crafted byte sequence.
- **Lack of Versioning**: These libraries often overlook forward and backward compatibility issues in favor of quick and easy data encoding.
- **Efficiency Problems**: They often overlook efficiency problems like the CPU time taken for encoding or decoding and the size of the encoded structure. For instance, Java's built-in serialization is notorious for its poor performance and large encoding size.

For these reasons, except for temporary purposes, it is generally recommended to avoid using the language's built-in encoding.

### JSON, XML, and Binary Variants

#### Standardized Encoding Formats

JSON and XML are standardized encoding formats that can be read and written by many programming languages.
- **JSON**: Popular due to built-in support in web browsers and simpler compared to XML.
- **XML**: Criticized for being verbose and complex.
- **CSV**: A less powerful but popular text format.

**Problems**
- **Ambiguity in Number Encoding**: XML and CSV cannot distinguish between numbers and strings. JSON distinguishes between numbers and strings but does not differentiate between integers and floating points or specify precision.
- **Lack of Binary String Support**: JSON and XML do not support binary data, which must be encoded in Base64, increasing data size by 33%.
- **Complex Schema Support**: XML and JSON optionally support schemas, but they are complex to learn and implement. Many JSON tools do not use schemas.
- **Lack of Schema in CSV**: CSV lacks a schema, so the application must define the data's meaning. Handling values with commas or newline characters can be challenging.

Despite these drawbacks, JSON, XML, and CSV are widely useful for various purposes, especially as data exchange formats.

#### Binary Encoding

- Data used internally can benefit from more compact formats or formats that can be parsed quickly.
- **MessagePack**: A binary encoding of JSON, which encodes example data into 66 bytes, less than the 81 bytes of text JSON encoding.

The next section will describe a method to encode the same example data in 32 bytes.

**Example Data (JSON)**:
```json
{
	"userName": "Martin",
	"favoriteNumber": 1337,
	"interests": ["daydreaming", "hacking"]
}
```

**MessagePack Encoding Process**:
1. First byte `0x83`: Indicates an object with three fields.
2. Second byte `0xa8`: Indicates a string of 8 bytes.
3. Next 8 bytes: Store the field name `userName` in ASCII.
4. Next 7 bytes: Encode the string `Martin`.

The total MessagePack binary encoding size is 66 bytes.

### Thrift and Protocol Buffers

**Basic Principles**:
- Both encoding formats require a schema. Thrift was developed by Facebook, and Protocol Buffers by Google.
    
**Schema Definition**:
- Thrift
```
struct Person {
    1: required string userName,
    2: optional i64 favoriteNumber,
    3: optional list<string> interests
}
```

- Protocol Buffers
```
message Person {
    required string user_name = 1;
    optional int64 favorite_number = 2;
    repeated string interests = 3;
}
```

**Code Generation Tools**:
- Thrift and Protocol Buffers generate classes for multiple programming languages based on the schema definition.
    
**Binary Encoding Formats**:
- **Thrift**:
	- **BinaryProtocol**: Uses 59 bytes
	- **CompactProtocol**: Uses 34 bytes
- **Protocol Buffers**: Uses 33 bytes

**Schema Evolution**
- **Field Tags and Compatibility**:
    - Field tags indicate the meaning of the fields and are crucial.
    - When adding new fields, assign new tag numbers.
    - Old code will ignore fields with unknown tag numbers.
    - Added fields must be optional or have default values.
    - Only optional fields can be removed, and tag numbers cannot be reused.
- **Changing Data Types**:
    - Changing data types is possible but may lead to precision loss or truncation.
    - For example, changing a 32-bit integer to a 64-bit integer may cause issues when old code reads new data.
- **List Data Types**:
    - **Protocol Buffers**: No array type but uses the repeated marker for multi-value fields.
    - **Thrift**: Supports list data types for nested lists.

Thrift and Protocol Buffers support schema evolution and maintain data compatibility.

### Apache Avro

Avro is a binary encoding format that began as a Hadoop subproject in 2009 because Thrift was not suitable for Hadoop's needs.
    
**Schema**:
Avro uses schemas to encode data. It supports two schema languages:

- **Avro IDL** (Human-readable)
```
record Person {
    string userName;
    union { null, long } favoriteNumber = null;
    array<string> interests;
}
```

- **JSON-based Schema** (Machine-readable)
```json
{
    "type": "record",
    "name": "Person",
    "fields": [
        {"name": "userName", "type": "string"},
        {"name": "favoriteNumber", "type": ["null", "long"], "default": null},
        {"name": "interests", "type": {"type": "array", "items": "string"}}
    ]
}
```

- **Binary Encoding**: Avro's binary encoding arranges values sequentially without identifying fields or data types, providing the most compact encoding of 32 bytes.

**Schema Evolution**

- **Writer Schema and Reader Schema**:
    - Writer Schema: Used when encoding data.
    - Reader Schema: Expected when decoding data.
    - They do not need to match but must be compatible. The Avro library resolves differences between the writer and reader schemas.
- **Compatibility Rules**:
    - **Forward Compatibility**: New schema writing data, read by an old schema.
    - **Backward Compatibility**: Old schema writing data, read by a new schema.
    - Only fields with default values can be added or removed.

**Schema Storage Methods**
- **Large Files**: Include the schema once at the beginning of the file.
- **Databases**: Include a version number with each record and fetch the schema from the database.
- **Network Communication**: Negotiate the schema version during connection setup and use it for the connection duration.

**Dynamically Generated Schemas**
- **Advantages**: Avro does not use tag numbers, making it more suitable for dynamically generated schemas.
- **Example**: Easily convert a relational database schema into an Avro schema and encode the database content.

**Code Generation and Dynamic Languages**
- **Thrift and Protocol Buffers**: Require code generation and are useful in statically-typed languages.
- **Avro**: Code generation is optional and can be easily used in dynamically-typed languages.

**Features**
- Avro files are self-descriptive, including the writer schema.
- This is especially useful for dynamic data processing languages like Apache Pig.


### The Merits of Schemas

Schema Definition:
- First, a schema that defines the data is required. This schema describes the structure of the data, specifying what fields exist and what data type each field has.

Binary Encoding:
- Before storing or transmitting data, it is encoded into a binary format according to the schema. This process uses the field and type information defined in the schema to convert the data into an optimized binary format.

#### **Advantages of Schemas**
- **Protocol Buffers, Thrift, Avro**:
    - All use schemas to describe the binary encoding format.
    - Simpler than XML schemas or JSON schemas, making them easier to implement and use.
    - Support various programming languages.
- **ASN.1**:
    - A standardized schema definition language from 1984, used to define several network protocols.
    - Supports binary encodings like DER, used for encoding SSL certificates (X.509).
    - Supports schema evolution through tag numbers but is complex and poorly documented, making it unsuitable for new applications.
- **Proprietary Binary Encodings**:
    - Most relational databases exchange queries and responses via network protocols.
    - These protocols are specific to the database, and drivers decode responses into in-memory data structures.

#### **Advantages of Binary Encoding**
- **Higher Compression**: Can be more compressed than "binary JSON" variants, omitting field names.
- **Schemas as Useful Documentation**: Ensures that schemas required for decoding are always up-to-date.
- **Compatibility Verification**: Maintaining a schema database allows verifying forward and backward compatibility of schema changes before deployment.
- **Code Generation**: Useful for users of statically typed programming languages, enabling type checks at compile time.

**Summary**: Schema evolution provides the flexibility of schema-less/schema-on-read JSON databases, while offering better guarantees and tooling for data.

## Modes of Dataflow

**Need for Data Transmission**

- **Data Encoding**: Data needs to be encoded into a byte sequence to be transmitted to another process not sharing memory, sent over a network, or written to a file.
- **Compatibility**: Forward and backward compatibility are essential for independently upgrading different parts of a system, enhancing the ease of changes.
    - **Forward Compatibility**: The ability of new code to read old data.
    - **Backward Compatibility**: The ability of old code to read new data.

**Different Modes of Dataflow**

1. **Dataflow Through Databases**:
    - Processes writing to a database encode the data, and processes reading from the database decode it.
    - For details, refer to "Dataflow Through Databases" (page 129).
2. **Dataflow Through Service Calls**:
    - The client encodes the request, the server decodes it to generate a response, and the client decodes the response.
    - For details, refer to "Dataflow Through Services: REST and RPC" (page 131).
3. **Dataflow Through Asynchronous Messaging**:
    - One process sends a message through a message broker, and another process receives it.
    - For details, refer to "Message-Passing Dataflow" (page 136).

**Conclusion**
- Data encoding and compatibility facilitate the evolution of a system, allowing data to flow between processes in various ways.
- Each mode of dataflow provides methods for encoding and decoding data suitable for specific scenarios.

### Dataflow Through Databases

- **Data Encoding and Decoding**: Processes writing to the database encode the data, and processes reading from it decode the data. This is similar to reading past data in the future.
    
- **Need for Compatibility**:
    - **Backward Compatibility**: Must be able to read old data stored in the database.
    - **Forward Compatibility**: New code must allow old code to read the data it writes.
- **Concurrent Access**:
    - Multiple processes or services often access the database concurrently.
    - During rolling upgrades, some instances may be updated with new code, while others still run the old code.
- **Preventing Data Loss**:
    - When adding new fields, they must be preserved even if old code does not recognize them.
    - When re-encoding decoded model objects at the application level, ensure unknown fields are not lost.

**Values Written at Different Times**

- **Database Updates**: The database can update values at any time, mixing old and new data.
- **Data Rewrites**:
    - Existing data remains encoded in its original format even after deploying new application versions.
    - Data rewriting to migrate to a new schema is possible but costly.

**Archival Storage**

An archive is a storage or system for long-term data preservation. Originally referring to a place for historical documents or records, today it includes various forms of information, including digital data, stored and managed long-term.

- **Data Snapshots**: Periodically take database snapshots for backups or loading into data warehouses, using the latest schema to encode data.
- **Immutable Data Dumps**: Data dumps are written in bulk and not changed afterward, making formats like Avro object container files suitable.
- **Analysis-Friendly Formats**: Encoding data in columnar formats like Parquet can also be a good opportunity.

This summary covers important points about dataflow through databases, the need for compatibility, preventing data loss, handling values written at different times, and archival storage.

### Dataflow Through Services: REST and RPC

**Inter-Process Communication Over a Network**

- **Client-Server Model**:
    - Servers expose APIs over the network, and clients send requests to those APIs.
    - On the web, clients (web browsers) send GET, POST requests to servers.
    - Native apps or client-side JavaScript applications can also send network requests to servers.
- **Service-Oriented Architecture (SOA) and Microservices Architecture**:
    - Decomposes large applications into small, functional services, allowing each to be independently deployed and evolved.
    - Each service is owned by a team and should release new versions frequently.
    - Servers and clients must maintain compatibility between service API versions.

**Web Services**

- **Web Services Using HTTP**:
    - Client applications send requests to services over HTTP.
    - Services within the same organization send requests to other services as part of service-oriented/microservices architecture.
    - Services exchange data between different organizations over the internet.

- **REST and SOAP**:
    - **REST**: A design philosophy based on HTTP principles, using simple data formats and URLs. Supports cache control, authentication, content type negotiation, etc.
    - **SOAP**: An XML-based protocol, independent of HTTP, with complex features, including the WS-* standards. Uses WSDL to describe APIs and supports code generation.

**Issues with Remote Procedure Calls (RPC)**

RPC attempts to make network service requests appear like local function calls, but network requests differ fundamentally from local function calls.

- Network requests are unpredictable and can fail due to network issues.
- Requests may be performed multiple times, requiring deduplication mechanisms in the protocol.
- Network requests take longer and have variable latency compared to local function calls.
- Parameters must be encoded as byte sequences for network transmission.
- Clients and services may be implemented in different programming languages, requiring data type conversions.

**Current Direction of RPC**

- New RPC frameworks clarify the differences between remote requests and local function calls.
    - Finagle, Rest.li use futures (promises) to encapsulate asynchronous tasks.
    - gRPC supports streams using Protocol Buffers.
    - Provides service discovery, allowing clients to find specific services.

- **Advantages of REST**:
    - Good for experimentation and debugging, supported by all major programming languages and platforms.
    - Extensive tool ecosystem available.

**RPC Data Encoding and Evolution**

- RPC clients and servers must be able to change and deploy independently.
    - Thrift, gRPC (Protocol Buffers), Avro RPC can evolve according to their respective encoding format compatibility rules.
    - SOAP uses XML schemas to specify requests and responses.
    - RESTful APIs primarily use JSON responses, with request parameters handled as URI-encoded/form-encoded.

- **API Versioning**:
    - Various approaches to API versioning exist. For RESTful APIs, it's common to include version numbers in the URL or HTTP Accept header.
    - Services using API keys store the requested API version on the server, allowing version selection updates through a separate management interface.

### Message-Passing Dataflow

**Asynchronous Message-Passing Systems**

**Message Broker**:
- Acts as an intermediary for message delivery, enhancing system reliability when the recipient is unavailable or overloaded.
- Automatically redelivers messages to crashed processes to prevent message loss.
- Eliminates the need for the sender to know the recipient's IP address and port number.
- Allows sending a single message to multiple recipients.
- Logically decouples the sender and receiver, enabling the sender to post messages without worrying about the consumer.
- Message delivery is typically one-way, with no expectation of a response. If a response is needed, a separate channel is used.

**Message Broker Implementations**:
- Shift from commercial software (TIBCO, IBM WebSphere, webMethods) to open-source implementations (RabbitMQ, ActiveMQ, HornetQ, NATS, Apache Kafka).
- Processes send messages to named queues or topics, and the broker delivers these messages to one or more consumers of the queue or topic.

**Distributed Actor Frameworks**:
- **Actor Model**: A concurrency programming model within a single process, where each actor represents a single client or entity and communicates asynchronously with other actors.
- **Distributed Actor Frameworks**: Used to scale applications across multiple nodes, with messages transparently encoded and sent over the network, then decoded on the other side.
- **Three Popular Distributed Actor Frameworks**:
	- **Akka**: Uses Java's default serialization, but can be replaced with Protocol Buffers.
	- **Orleans**: Uses a custom data encoding format by default and does not support rolling upgrades. Instead, set up a new cluster and move traffic to the new cluster before shutting down the old one.
	- **Erlang OTP**: Changing record schemas is difficult, and rolling upgrades must be carefully planned. The new JSON-like maps data type can simplify this process.

Asynchronous message-passing systems lie between RPC and databases, sending messages via a message broker instead of direct network connections, increasing system stability and flexibility.

## Summary

**Converting Data Structures to Bytes**

This chapter covered various methods of converting data to bytes for transmission over a network or disk. It examined how these encoding methods impact not only efficiency but also application architecture and deployment options.

**Importance of Supporting Rolling Upgrades**

- Services should be deployed incrementally, allowing new versions to be released without downtime and encouraging frequent, small releases.
- Rolling upgrades mean different versions of code may run on different nodes during deployment, requiring data encoding to provide backward compatibility (new code can read old data) and forward compatibility (old code can read new data).

**Data Encoding Formats and Compatibility Properties**

- **Language-Specific Encodings**: Limited to a single programming language and often do not provide backward and forward compatibility.
- **Text Formats (JSON, XML, CSV)**: Widely used with compatibility depending on usage. Optional schema languages can be helpful or a hindrance. These formats can be somewhat ambiguous regarding data types.
- **Binary Schema-Based Formats (Thrift, Protocol Buffers, Avro)**: Allow efficient encoding with clearly defined forward and backward compatibility semantics. Schemas are useful for documentation and code generation in statically-typed languages. The downside is that data must be decoded into a human-readable form.

**Modes of Dataflow**

- **Databases**: Processes writing to a database encode the data, and processes reading from the database decode the data.
- **RPC and REST APIs**: The client encodes the request, the server decodes it, then encodes the response, and finally, the client decodes the response.
- **Asynchronous Message Passing (Using Message Brokers or Actors)**: Nodes send messages to each other, with the sender encoding and the receiver decoding the messages.

By carefully considering backward/forward compatibility and supporting rolling upgrades, various data encoding formats and dataflow methods enable the evolution of applications and frequent deployments.