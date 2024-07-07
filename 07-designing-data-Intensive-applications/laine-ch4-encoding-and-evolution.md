Change to an application’s features also requires a change to data that it stores: a new field or record type needs to be captured, or perhaps existing
data needs to be presented in a new way.

Different data models have different ways of coping with such change.
Change doesn't happen instantaneously, thus the old and new versions of the code and data formats may coexist in the system.
Backward compatibility: Newer code can read data that was written by older code.
Forward compatibility: Older code can read data that was written by newer code.

## Formats for Encoding Data
Data kept in memory as objects, structs, lists, arrays, hash tables, trees, etc is different from the format used, self-contained sequence of bytes (JSON document) as the user writes data to a file.

--> thus, translation between two representations needed.
- in-memory --> byte sequence: encoding, serialization
- the reverse: decoding

Using language specific formats is bad due to the strong tie to the language, creating arbitrary classes for decoding and exposed to the security threat, inefficiency.

### JSON, XML, and Binary Variants
JSON's built-in support in web browser gained its popularity - and data interchange formats between different organizations.

Subtle problems
- ambiguity around encoding the numbers and large numbers
- no support for binary strings
- optional schema support or no support

#### Binary encoding such formats
- These formats use a lot of space compared to binary formats. 
- These formats don't prescribe a schema, they include all the object field names within the encoded data.
- Binary encoded data reduces a little space, might not be worth the loss of human readability


### Thrift and Protocol Buffers
Binary encoding libraries
- No filed names, but field tags as numbers, appearing in the schema definition. Compact way
- Forward compatibility: Adding a new field, then old code can ignore the field
- Backward compatibility: Don't make a new field required, it would fail new code reading data written by old code.
- Never use the same tag number again
- Datatype changes: if the new data is larger, old code will truncate reading the new data


### Avro - binary encoding format

With Avro, the writer’s schema and the reader’s schema don’t have to be the same—they only need to be compatible. 
- When data is decoded (read), the Avro library resolves the differences by looking at the writer’s schema and the reader’s schema side by side and translating the data from the writer’s schema into the reader’s schema.

Compatibilities
- forward compatibility means that you can have a new version of the schema as writer and an old version of the schema as reader. - backward compatibility means that you can have a new version of the schema as reader and an old version as writer.
- To maintain compatibility, you may only add or remove a field that has a default value

### The Merits of Schemas
Binary encodings based on schemas are also a viable option with nice properties, although textual data formats such as JSON, XML, and CSV are widespread.

- Much more compact than the various “binary JSON” variants, since they can omit field names from the encoded data.
- The schema is a valuable form of documentation, and because the schema is required for decoding, you can be sure that it is up to date.
- Keeping a database of schemas allows you to check forward and backward compatibility of schema changes, before anything is deployed.
- For users of statically typed programming languages, the ability to generate code from the schema is useful, since it enables type checking at compile time.

## Modes of Dataflows
Via database, service calls, and asynchronous message passing

### Dataflow Through Databases
Write - encoding / Read - decoding

- Without backward compatibility, your future self won’t be able to decode what you previously wrote.
- Forward compatibility: Some processes accessing the database will be running newer code and some will be running older code - rolling upgrade
  - meaning that a value in the database may be written by a newer version of the code, and subsequently read by an older version of the code that is still running.

### Dataflow Through Services: REST and RPC
Servers expose an API over the network, and the clients connect to the servers to make request to that API. Web browsers, native app on a mobile device/desktop can be clients
- Service: The API exposed by the server.

**SOA - microservices or service-oriented architecture**
- A server can be a client to another service too
- A large application is decomposed into smaller services by functionality
- Goal: make the application easier to change and maintain by making services independently deployable and evolvable

#### Web services
When HTTP is used as the underlying protocol for talking to the service. Also used in other context, not just web

**REST**
- rather a design philosophy based on HTTP
- simple data formats, using URLs
- API designed to the principles of REST is RESTful
- Less code generation and automated tooling than SOAP

**SOAP**
- XML-based protocol for making network API requests
- Used over HTTP but aims to be independent from it and avoids most HTTP features

#### The problems with remote procedure calls (RPCs)
This has been around since 1970s, but flawed since the local function calls are different from network requests.

- local function call is predictable and controllable under my control. A network call is unpredictable with network problem and remote machine's problem
- local function call returns a result or exception, a network call might return without result due to timeout
- Retry could have issue with idepotency in a network call, while local function call doesn't have the issue
- local function call latency is the same, a network call is slower and latency is variable
- RPC framework must translate datatypes between different languages if client/service are using different one

Current directions for RPC

