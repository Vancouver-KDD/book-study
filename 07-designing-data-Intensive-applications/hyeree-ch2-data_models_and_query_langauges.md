
Different ways of representing the data model
1. Application developer - model in terms of objects or data structures, and use APIs to manipulate those data structures
2. storing data structures - express them in general-purpose data model such as JSON, XML, tables in relational database, or a graph model
3. Represents the data model in bytes in memory, on disk, or a network which can be queried, searched, manipulted, and processed in many ways
4. lower level- hardware engineer represents bytes in electrical currents, pulses of light, magnetic fields, etc

The abstraction in each layer hides the complexity of the layer below it, providing a clean data model

Relational Model vs. Document Model
* SQL - relation is in an unordered collection of tuples
* used in business data processing, transaction processing, batch processing, etc
* hides the implementation deetail behind a cleaner interface
* network model and hierarchical model were alternatives, but relational model took over
* relational database generalize variety of use cases = online publishing, discussion, social networking, ecommerce, games, software-as-a-service productivity applications, etc

NoSQL (Not Only SQL)
* need for greater scalability with large datasets and high write throughput
* widespread preference for free open source software
* specialized query operations not supported in relational model
* frustration with the restrictiveneww of relatinoal schemas (wants more dynamic and expressive data model)

The Object-Relational Mismatch
* Object-oriented programming + SQL data model with relational tables = requires transaltion layer
* disconnection between models == "impedance mismatch"
* Object-relational mapping (ORM) frameworks like Active Record and Hibernate reduce the amoun to fboilerplate code required for the translational layer.

LinkedIn Resume example:
1. traditional SQL Model - normalized representation for positions, education, and contact information with foreign key reference to the users table.
2. Later version - support structured datatypes and XML data 
  - allowed multi-valued data in a single row
  - supports querying and indexing inside those documents
  - features supported by Oracle, IBM DB2, MS SQL Server, and PostgreSQL, etc
3. encode jobs, education, and contact info as JSON or XML, store it in the text column in the database, let application to interpret the structure and content.

JSON may reduce impedance mismatch - but data encoding format problem
- has better locality than multi-table schema
  
Many-to-One and Many-to-Many relationships
Why do we want IDs over plain-text string? - Consistent style and spelling across profiles, avoiding ambiguity, ease of updating, localization support, better search

if we use ID - information meaning to humans is sotres in only one place - never needs to change
text - you are duplicating the human-meaningful information in every record - may change in the future with duplicates
Normalization in databases (many-to-one relationship) - remove duplicates

Can we use document databases and NoSQL to better represent many-to-many relationships?
- Various solutions to solve the limiataions of the hierarchical model
    1. The network model
        - CODASYL model - hierarchical model
        - a record can have  multiple parents (many-to-one + many-to-many)
        - links between networks are not foreign keys but pointers
        - need to traverse from access path
        - querying and updating the database is complicated and inflexible
    2. Relational model
        - relation is simply a collection of tuples
        - no nested structures or complicated access paths
        - query optimizer automatically decides which parts of the query to execute in which order, and which indexes to use
        - the choices == access path


What data model leads to simpler application code?
- document-like structure -> document model
  - relation model with shredding splitting a document like structure into multiple tables
  - document model limitation - cannot refer directly to a nested item, you need to specify locatino of item
  - poor join support
- many-to-many: document model can lead to significantly more complex application code and worse performance (emulating joins, etc)
- Schema flexibility: document databases do not enforce schema - arbitrary keys and values can be added, and there is no guarantees as to what fields the documents may contain
- schema-on-read (the structure of data is implicit, and only interpreted when the data is read)
  - advantageous if the items in the collection don't all have teh same structure for some reason
- schema-on-write (traditional approach of relational databases, schema is explicit and database ensures all written data conforms to it)

Data locality for queries - applies to the large parts of the document at the same time. on updates to a document, the entire document usually needs to be rewritten - should keep the documents fairly small and avoid increase in size

Convergence of document and relational databases - relational database can support making local modifications to XML documents, and index and query inside XML docuements - it allows app to use data models very similar to what we do in document database. PostgreSQL, MySQL, IBM DB2 supports JSON documents for web APIs, RethinkDB supports relational-like joins in its query language, etc...
* A hybrid of relational and document models is goooood

Query Langauges for Data
- SQL is a declarative query lanaguage = specify the pattern of the data you want, what conditions, how data to be transaformed, how to achieve the goal, etc.
  - It's more concise and easy to work with imperative APIs
- Most others are imperative = tells computer to perform certain operations in a certain order
Declarative languages have a better chance of getting faster in parallel execution
Declarative Queries on the Web (Examples)

MapReduceQuerying - programming model for processing large amounts of data in bulk across may machines - inbetween declarative and imperative query. May and Reduce functions are somwhat restricted in what they are allowed to do. As pure functions, they only use the data that is passed to them as input, and cannot perform additional queries - allows to run functions anywhere in any order, and rerun them on failure! - but it's still powerful as they can parse strings, call library functions, perform calculations, and more.

Graph-like Data Model - many-to-many relationships. consists of vertices and edges. (Ex. Social graphs, web graph, road and rail networks) - shorted path between two points, etc

Property Graphs - vertex (unique identifier, a set of outgoing edges, a set of incoming edges, a collection properties in key-value pairs) + edges (a unique identifier, the vertex where the edge starts (tail vertex) and where the edge ends (head vertex), a label to describe relationshio, and a collection of properties (k-v pairs))
    Gives graphs flexibility for data modeling and can extend the graph to include more information

Cypher Query Language - Cypher is a declarative query language for property graphs. Don't need to specify such execution details when writing the query, the query optimzer automatically chooses the strategy that is predicted to be teh most efficient.

Graph Queries in SQL - If we can put graph data in a relational structure, can we also query it using SQL? - Yes, but with difficulty
    - RD - need to know in advance which joins you need in your query
    - GQ - can traverse a variable number of edges before you find the vertex you are looking for

Triple-Store and SPARQL == property graph model
    - there are various tools and langauges for triple stores that can be valuable.
        - all info is stored in three-part statements (subject, predicate, object) == (Jim, likes, bananas) == (vertex, vertex, vertex)

The semantic web
- (completely separate from three-store)
- simple and reasonable - websites already publish info for humans to read, why not do the same as machine-readable data?
RDF data model
- the Turtle language is a humanreadable format for RFD data. (RDF == a model for encoding semantic relationships between items of data so that these relationships can be interpreted computationally.)
subject, predicate, and object are often URI.
The SPARQL query language for triple-stores using the RFD data model - predecates cypher. 

The Foundation: Datalog
- much older tahn SPARQL ro Cypher
- similar to triple-store model, generalized a bit
- instead of triplet (subject, predicate, object), it writes predicate(subject, object)
- small steps at a time with new predicates - complex queries can be built up a small piece at a time (Rule 1, 2, 3..)
