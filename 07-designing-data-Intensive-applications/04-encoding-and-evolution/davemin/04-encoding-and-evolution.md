# CHAPTER 4 Encoding and Evolution

Backward compatibility
- Newer code can read data that was written by older code.
- Backward compatibility is normally not hard to achieve: as author of the newer code, you know the format of data written by older code, and so you can explicitly handle it (if necessary by simply keeping the old code to read the old data).

Forward compatibility
- Older code can read data that was written by newer code.
- Forward compatibility can be trickier, because it requires older code to ignore additions made by a newer version of the code.

## Formats for Encoding Data
Two different representations of Programs with data
1. In memory, data is kept in objects, structs, lists, arrays, hash tables, trees, and so on. These data structures are optimized for efficient access and manipulation by the CPU (typically using pointers).
2. When you want to write data to a file or send it over the network, you have to encode it as some kind of self-contained sequence of bytes (for example, a JSON document).
   Since a pointer wouldn’t make sense to any other process, this sequence-of-bytes representation looks quite different from the data structures that are normally used in memory.

- Thus, we need some kind of translation between the two representations. The translation from the in-memory representation to a byte sequence is called encoding (also known as serialization or marshalling), and the reverse is called decoding (parsing, deserialization, unmarshalling)

### Language-Specific Formats
Many programming languages come with built-in support for encoding in-memory objects into byte sequences.
- Java: java.io.Serializable
- Ruby: Marshal
- Python: pickle
- Third-party: Kryo for Java

- Convenient: because they allow in-memory objects to be saved and restored with minimal additional code.
- Problem:
1. The encoding is often tied to a particular programming language, and reading the data in another language is very difficult.
   If you store or transmit data in such an encoding, you are committing yourself to your current programming language for potentially a very long time,
   and precluding integrating your systems with those of other organizations (which may use different languages).
2. In order to restore data in the same object types, the decoding process needs to be able to instantiate arbitrary classes. This is frequently a source of security problems: if an attacker can get your application to     decode an arbitrary byte sequence, they can instantiate arbitrary classes, which in turn often allows them to do terrible things such as remotely executing arbitrary code.
3. Versioning data is often an afterthought in these libraries: as they are intended for quick and easy encoding of data, they often neglect the inconvenient problems of forward and backward compatibility.
4. Efficiency (CPU time taken to encode or decode, and the size of the encoded structure) is also often an afterthought.
   (For example, Java’s built-in serialization is notorious for its bad performance and bloated encoding).

### JSON, XML, and Binary Variants
- Moving to standardized encodings that can be written and read by many programming languages, JSON and XML are the obvious contenders
- They are widely known, widely supported, and almost as widely disliked.
- XML is often criticized for being too verbose and unnecessarily complicated.
- JSON’s popularity is mainly due to its built-in support in web browsers (by virtue of being a subset of JavaScript) and simplicity relative to XML.
- CSV is another popular language-independent format, albeit less powerful.

subtle problems:
- There is a lot of ambiguity around the encoding of numbers. In XML and CSV, you cannot distinguish between a number and a string that happens to consist of digits (except by referring to an external schema). 
JSON distinguishes strings and numbers, but it doesn’t distinguish integers and floating-point numbers, and it doesn’t specify precision. This is a problem when dealing with large numbers; for example, integers greater than 253 cannot be exactly represented in an IEEE 754 double-precision floating-point number, so such numbers become inaccurate when parsed in a language that uses floating-point numbers (such as JavaScript). An example of numbers larger than 253 occurs on Twitter, which uses a 64-bit number to identify each tweet. The JSON returned by Twitter’s API includes tweet IDs twice, once as a JSON number and once as a decimal string, to work around the fact that the numbers are not correctly parsed by JavaScript applications [10].
- JSON and XML have good support for Unicode character strings (i.e., human- readable text), but they don’t support binary strings (sequences of bytes without a character encoding). Binary strings are a useful feature, so people get around this limitation by encoding the binary data as text using Base64. The schema is then used to indicate that the value should be interpreted as Base64-encoded. 
This works, but it’s somewhat hacky and increases the data size by 33%.
- There is optional schema support for both XML [11] and JSON [12]. These schema languages are quite powerful, and thus quite complicated to learn and implement. Use of XML schemas is fairly widespread, but many JSON-based tools don’t bother using schemas. Since the correct interpretation of data (such as numbers and binary strings) depends on information in the schema, applica‐ tions that don’t use XML/JSON schemas need to potentially hardcode the appro‐ priate encoding/decoding logic instead.
- CSV does not have any schema, so it is up to the application to define the mean‐ ing of each row and column. If an application change adds a new row or column, you have to handle that change manually. CSV is also a quite vague format (what happens if a value contains a comma or a newline character?). Although its escaping rules have been formally specified [13], not all parsers implement them correctly.

#### Binary encoding
- JSON is less verbose than XML, but both still use a lot of space compared to binary formats. 
- This observation led to the development of a profusion of binary encodings for JSON (MessagePack, BSON, BJSON, UBJSON, BISON, and Smile, to name a few) and for XML (WBXML and Fast Infoset, for example).
- These formats have been adopted in various niches, but none of them are as widely adopted as the textual versions of JSON and XML.
- Some of these formats extend the set of datatypes (e.g., distinguishing integers and floating-point numbers, or adding support for binary strings), but otherwise they keep the JSON/XML data model unchanged.

```json
//Example 4-1. Example record which we will encode in several binary formats in this chapter
{
  "userName": "Martin", 
  "favoriteNumber": 1337,
  "interests": ["daydreaming", "hacking"] 
}
```
![f4-1.jpg](image/f4-1.jpg "f4-1.jpg")
- Figure 4-1 shows the byte sequence that you get if you encode the JSON document in Example 4-1 with MessagePack [14]. The first few bytes are as follows:

1. The first byte, 0x83, indicates that what follows is an object (top four bits = 0x80) with three fields (bottom four bits = 0x03).
   (In case you’re wondering what happens if an object has more than 15 fields, so that the number of fields doesn’t fit in four bits, it then gets a different type indicator, and the number of fields is encoded in two or four bytes.)
2.  The second byte, 0xa8, indicates that what follows is a string (top four bits = 0xa0) that is eight bytes long (bottom four bits = 0x08).
3. The next eight bytes are the field name userName in ASCII. Since the length was indicated previously, there’s no need for any marker to tell us where the string ends (or any escaping).
4.  The next seven bytes encode the six-letter string value Martin with a prefix 0xa6, and so on.
- The binary encoding is 66 bytes long, which is only a little less than the 81 bytes taken by the textual JSON encoding (with whitespace removed).
- All the binary encodings of JSON are similar in this regard. 

### Thrift and Protocol Buffers
- how we can do much better, and encode the same record in just 32 bytes.
- Apache Thrift (Facebook) and Protocol Buffers (protobuf/ Google) [16] are binary encoding libraries that are based on the same principle.

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
- What does data encoded with this schema look like? Confusingly, Thrift has two different binary encoding formats,iii called BinaryProtocol and CompactProtocol, respectively. Let’s look at BinaryProtocol first. 

![f4-2.jpg](image/f4-2.jpg "f4-2.jpg")

- Similarly to Figure 4-1, each field has a type annotation (to indicate whether it is a string, integer, list, etc.) and, where required, a length indication (length of a string, number of items in a list). The strings that appear in the data (“Martin”, “daydreaming”, “hacking”) are also encoded as ASCII (or rather, UTF-8), similar to before.
- The big difference compared to Figure 4-1 is that there are no field names (userName, favoriteNumber, interests). Instead, the encoded data contains field tags, which are numbers (1, 2, and 3). Those are the numbers that appear in the schema definition. 
- Field tags are like aliases for fields—they are a compact way of saying what field we’re talking about, without having to spell out the field name.
#### Field tags and schema evolution
