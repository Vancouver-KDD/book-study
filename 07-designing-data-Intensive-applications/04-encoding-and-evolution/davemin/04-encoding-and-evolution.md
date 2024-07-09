# CHAPTER 4 Encoding and Evolution

- In this chapter we will look at several formats for encoding data, including JSON, XML, Protocol Buffers, Thrift, and Avro. 
- In particular, we will look at
   - how they handle schema changes and
   - how they support systems where old and new data and code need to coexist


#### In order for the system to continue running smoothly, we need to maintain compatibility in both directions:

- [Backward compatibility]
   - Newer code can read data that was written by older code.
   - Backward compatibility is normally not hard to achieve: as author of the newer code, you know the format of data written by older code, and so you can explicitly handle it (if necessary by simply keeping the old code to read the old data).

- [Forward compatibility]
    - Older code can read data that was written by newer code.
    - Forward compatibility can be trickier, because it requires older code to ignore additions made by a newer version of the code.

## Formats for Encoding Data
Two different data representations that Programs work with 
1. Data in memory which is kept in objects, structs, lists, arrays, hash tables, trees, and so on.
2. Encoded data to be written in a file or sent it over the network which has self-contained sequence of bytes(i.e. JSON)

- Encoding : the Translation from the in-memory representation to a byte sequence (also known as serialization or marshalling)
- Decoding : the Reverse of the encoding(parsing, deserialization, unmarshalling)

### Language-Specific Formats
> Encoding libraries in many programming languages for encoding in-memory objects into byte sequences.
```
- Java: java.io.Serializable
- Ruby: Marshal
- Python: pickle
- Third-party: Kryo for Java
```

- Convenient: because Encoding libraries allow in-memory objects to be saved and restored with minimal additional code.
- Problem:
1. Compatible issue b/w differnet languages:
   - A particular program language might not be able to be read the encoded data by another language.
2. Security issue :
   - In order to restore data in the same object types, the decoding process needs to instantiate arbitrary classes.
   ```
   That is,
   -> Attackers can get your application to decode an arbitrary byte sequence
   -> they can instantiate arbitrary classes -> they can do terrible things(such as remotely executing arbitrary code).
   ```
3. Compatible issue b/w differnet versions:
   - Versioning data is often an afterthought in these libraries: as they are intended for quick and easy encoding of data, they often neglect the inconvenient problems of forward and backward compatibility.
4. Efficiency (CPU time taken to encode or decode, and the size of the encoded structure) is also often an afterthought.
   - (For example, Java’s built-in serialization is notorious for its bad performance and bloated encoding).

> ==> For these reasons it’s generally a bad idea to use your language’s built-in encoding for anything other than very transient purposes.

&nbsp;

### JSON, XML, and Binary Variants
- JSON & XML : Standardized encodings (widely known, widely supported, and almost as widely disliked)
   - XML : often criticized for being too verbose and unnecessarily complicated.
   - JSON : popular due to its built-in support in web browsers and simplicity relative to XML.
   - CSV : another popular language-independent format
   - Textual formats / human-readable
   - But, Subtle problems:
      1. There is a lot of ambiguity around the encoding of numbers.
         - XML & CSV: cannot distinguish b/w numbers and string+digits
         - JSON: cannot distinguish integers and floating-point numbers
         - This is a problem when dealing with large numbers(i.e.  > 2^53); 
      2. JSON and XML have good support for Unicode character strings (i.e., human- readable text), but they don’t support binary strings (sequences of bytes without a character encoding).
      3. If applications don’t use XML/JSON schemas, then they need to potentially hardcode the appropriate encoding/decoding logic instead.(Since the interpretation depends on information in the schema)
      4. CSV does not have any schema, so it is up to the application to define the meaning of each row and column. If an application change adds a new row or column, you have to handle that change manually.

> ==> Despite these flaws, JSON, XML, and CSV are good enough for many purposes(data interchange formats b/w different organizations)
Because as long as people agree on what the format is, it often doesn’t matter how pretty or efficient the format is. 

#### Binary encoding
- Binary encodings for JSON and XML was developed because the textual versions of JSON and XML use a lot of space.
   - Binary encodings for JSON : *MessagePack*, BSON, BJSON, UBJSON, BISON, and Smile
   - Binary encodings for XML : WBXML and Fast Infoset
- No prescribed schema -> include all the object field names

```json
//Example 4-1. Example record which we will encode in several binary formats in this chapter
{
  "userName": "Martin", 
  "favoriteNumber": 1337,
  "interests": ["daydreaming", "hacking"] 
}
```
![f4-1.jpg](image/f4-1.jpg "f4-1.jpg")

**Encoded JSON document with MessagePack**
- [Indicator / Meaning]
   - 1st byte, 0x83 : Ox -> Hexadecimal / Top 4 bits 0x80 -> a map with key-value pairs / Bottom 4 bits = 0x03 -> three key-value pairs.
   - 2nd byte, 0xa8 : 0xa0(Top 4 bits) -> string / 0x08(bottom 4 bits) - > 8 bytes long.
   - 3rd 8 bytes:  Field name 'userName' in ASCII.
   - 4th byte, 0xa6: six-letter string inficator
   - 5th 6 byte: string value 'Martin' and so on.   
- The binary encoding 66 bytes long ( a little less than textual JSON encoding(81 bytes) (with whitespace removed)).
- But, such a small space reduction

&nbsp;

### Thrift and Protocol Buffers
- how we can do much better, and encode the same record in just 32 bytes.
- Apache Thrift (Facebook) and Protocol Buffers (protobuf/ Google) binary encoding libraries
- They come with a code generation tool that takes a schema definition like the ones shown here, and produces classes that implement the schema in various programming languages 
```
// Thrift interface definition language (IDL)
struct Person {
  1: required string       userName,
  2: optional i64          favoriteNumber, 
  3: optional list<string> interests
}
```

```
//Protocol Buffers
message Person {
  required string user_name = 1; 
  optional int64 favorite_number = 2; 
  repeated string interests = 3;
}
```
- **Thrift** : two different binary encoding formats 'BinaryProtocol' & 'CompactProtocol'

> **(1) Thrift - BinaryProtocol** 

![f4-2.jpg](image/f4-2.jpg "f4-2.jpg")

- [Similarity]
   - a type annotation (to indicate whether it is a string, integer, list, etc.) 
   - a length indication (length of a string, number of items in a list).
   - Strings encoded as ASCII (or rather, UTF-8)
- [Difference]
   - No field names (userName, favoriteNumber, interests).
   - Instead, field tags(schema definition)- numbers (1, 2, and 3)

- ==> 59 byte

> **(2) Thrift - CompactProtocol**

![](image/f4-3.jpg "f4-3.jpg")

- ==> only 34 bytes
- by packing the *'field type'* and *'tag number'* into a single byte
- by using variable-length integers
- Number 1337 => encoded in two bytes (the top bit indicator -> still more bytes to come? )
- That is, '–64 ~ 63' --encoded--> 1 byte and '–8192 ~ 8191' --encoded--> 2 byte

>**Protocol Buffers (which has only one binary encoding format)**

![](image/f4-4.jpg)

- ==> only 33 bytes
- bit packing slightly differently, but is otherwise very similar to Thrift’s CompactProtocol.

#### Field tags and schema evolution
- Schema Evolution : Schemas inevitably need to change over time
- How do Thrift and Protocol Buffers handle schema changes while keeping backward and forward compatibility?

> First, Field’s tags are important!!
- You can change a field's name in the schema, since the encoded data never refers to field names,
- But, you can**not** change a field’s tag in the schema, since that would make all existing encoded data invalid.

> Forward compatibility: (old code can read records that were written by new code)
- You can add new fields to the schema with new tag numbers
   - Old code can skip it using the datatype annotation   

> Backward compatibility: (new code can read records that were written by old code)
- New code can always read old data
- If you add a new field
   - New code can not read data written by old code
   - So, the added fields must be optional or have a default value.
 
#### ==> This way, we can maintain the compatibility. (Removing a field is just like adding a field)

#### Datatypes and schema evolution

### Apache Avro

- Another binary encoding format
-  It was started in 2009 as a subproject of Hadoop, as a result of Thrift not being a good fit for Hadoop’s use cases.
-  Hadoop: a framework that allows for the distributed processing of large data sets across clusters of computers using simple programming models.
-  Avro also uses a schema to specify the structure of the data being encoded. It has two schema languages:
-  one (Avro IDL) intended for human editing,

```
Our example schema, written in Avro IDL, might look like this:
record Person { 
   string    userName;
   union { null, long } favoriteNumber = null; 
   array<string>        interests;
}
```

-  and one (based on JSON) that is more easily machine-readable.
```
{
The equivalent JSON representation of that schema is as follows:
   "type": "record", 
   "name": "Person", 
   "fields": [
      {"name": "userName",       "type": "string"},
      {"name": "favoriteNumber", "type": ["null", "long"], "default": null}, 
      {"name": "interests",      "type": {"type": "array", "items": "string"}}
   ]
}
```

[Characteristics - Avro binary encoding]
- No tag numbers in the schema
- For the figure 4-1 case, it uses just 32 bytes long which is compact.

![](image/f4-5.jpg)

- No identify fields or their datatypes.
- concatenated values together
- The type is encoded using a variable-length encoding (the same as Thrift’s CompactProtocol).

#### The writer’s schema and the reader’s schema
- writer’s schema:
   - The schema that is used for application to encode some data     
- reader’s schema:
   -  The schema that is used for application to decode some data     
- Key Idea in Avro.
   - The writer’s schema and the reader’s schema don’t have to be the same—they only need to be compatible by **Field Name*
- If the code reads data expecting some field,
   - But, no field info in the writer’s schema
   - Then, a **Default Value* declared in the reader’s schema.

#### Schema evolution rules 
- To maintain forward/backward compatibility, you may only add or remove a field that has a default value.
> For example,
```
case 1)
you add a field with default value && new field in the new schema, but not the old one
When reader with new schema ---read--- old schema record ==>  Then, Default value (Read OK)

case 2-1)
you 'Add' a field with 'NO' default value && new field in the new schema, but not the old one
When reader with new schema ---read--- old schema record ==>  Then, No Default value (Read Error -> backward compatibility Error)

case 2-2)
you 'Remove' a field with 'NO' default value && new field in the new schema, but not the old one
When reader with old schema ---read--- new schema record ==>  Then, No Default value (Read Error -> forward compatibility Error)
```

-  Consequently,
-  Avro : no optional and required markers /  instead, default values  
-  the same as Protocol Buffers and Thrift 

![](image/f4-6.jpg)

#### But what is the writer’s schema?
- How does the reader know the writer’s schema with which a particular piece of data was encoded?
- can’t just include the entire schema with every record, because the schema would be too much bigger than the encoded data
- Answer? It depends in context in Avro being used  
   - Large file with lots of records :
      - Writer’s schema included once at the beginning of the file
      -  all encoded with the same schem
   - Database with individually written records
      - In a database, different records / different points in time / different writer’s schemas
      - How?
      - include a version number at the beginning of every encoded record, and to keep a list of schema versions in your database.
   - Sending records over a network connection
      - When two processes are communicating over a bidirectional network connection
      - They can negotiate the schema version which is used for the lifetime of the connection.
      - It's how Avro RPC protocol works.
    
#### Dynamically generated schemas
- No Tag Number in Avro unlike Protocol Buffers and Thrift( why is it important?)
- The difference is that Avro is friendlier to **dynamically generated schemas**.
> Example
```
- say you have a relational database
- you want to dump contents in DB to a file
- also you want to use binary format, not the textual format

- In the case of Avro
   - you can fairly easily generate an Avro schema from the relational schema and encode the database contents
   - Because,
      - each DB column  ==> a field in Avro.
      - DB column names ==> field names in Avro.
      - So, schema changes is not that hard
      - And the updated writer’s schema can still be matched up with the old reader’s schema because the field name is identical.

- By contrast, in the case of 'Thrift' or 'Protocol Buffers'
   - the field tags have to be assigned by hand
```

#### Code generation and dynamically typed languages
> 'Thrift and Protocol Buffers' rely on code generation
- By using a programming language, you can implements this schema.
   -  Code generation is useful in statically typed languages(Java, C++, or C#)
      -  1. Because of efficient in-memory structures for decoded data
         2. IDE - type checking and autocompletion
   -  Code generation is pointless in dynamically typed languages(JavaScript, Ruby, or Python)
      -  Because of no compile-time for type checker

By contrast

> 'Avro' provides optional code generation(with/without any code generation)
- by using an object container file (which embeds the writer’s schema), you can simply open it using the Avro library and look at the data for analysis
- The file is self-describing since it includes all the necessary metadata.

### The Merits of Schemas
