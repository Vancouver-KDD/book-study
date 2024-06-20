
Thinking about data model - how we think about the problem that we are solving.

Most applications are built by layering one data model on top of another. Each layer hides the complexity of the layers below it by providing a clean data model. These abstractions allow different groups of people—for example, the engineers at the database vendor and the application developers using their database—to work together effectively.

## Relational Model Versus Document Model
The goal of the relational model was to hide that implementation detail behind a cleaner interface.

### NoSQL
- A need for greater scalability than relational databases can easily achieve, including very large datasets or very high write throughput
- A widespread preference for free and open source software over commercial database products
- Specialized query operations that are not well supported by the relational model
- Frustration with the restrictiveness of relational schemas, and a desire for a more dynamic and expressive data model

### The Object-Relational Mismatch
- The different representation of Relational database model and OOP pragramming language: OOP application code requires some wrapper (like ActiveRecord) for database model, but not enough
- For example, a person has multiple jobs/educations in a resume, one-to-many relationships
  - traditional SQL creates normalized SQL creates separate tables for position/education/etc then refers it
  - Document oriented model is better, for such self-contained object or SQL with support for JSON
- JSON representation contains all the relevant info in one place, instead of multiple queries for RDM

### Many-to-One and Many-to-Many Relationships
**Normalization**: instead of free-text, keep the preset of data
- No duplication with ID - key idea of normalization
- Consistent style and spelling across profiles
- Avoiding ambiguity (e.g., if there are several cities with the same name)
- Ease of updating
- Localization support
- Better search

Normalization requires many-to-one, and Document model can't easily support it. Data has a tendency of becoming more interconnected as features are added to applications.

### Relational VS Document Database
**Historical discussion about network model**: hierarchical model, every record has exactly one parent.
  - The links between records are like pointers in a programming language (while still being stored on disk). The only way of accessing a record was to follow a path from a root record along these chains of links.
  - For many-to-many relationships, several different paths can lead to the same record, and a programmer had to keep track of these different access paths in their head.

**Relational model**
- Query optimizer automatically decides which parts of the query to execute in which order, and which indexes to use, developers don't need to think about it.
- You only need to build a query optimizer once, and then all applications that use the database can benefit from it.

**Document model**
- Useful for self-contained documents and when relationships between documents are rare
- Flexibility: do not enforce any schema on the data in documents. XML support in relational databases usually comes with optional schema validation.
- Schema-on-read:
  - Good when items don't have the same structure, and can change by external systems
  - Application can assume some kind of structure on read, not enforced by DB

- Locality: The database typically needs to load the entire document, even if you access only a small portion of it - wasteful. On updates to a document, the entire document usually needs to be rewritten. Thus, **keep documents fairly small and avoid writes that increase the size of a document**


- Limitation
  - cannot refer directly to a nested item within a document, but instead “the second item in the list of positions for user 251” (much like an access path in the hierarchical model).
  - Proper for NOT many-to-many relationships and not too deeply nested document
  - Not for highly interconnected data

  

## Query Languages for Data
**Imperative language**
- Most commonly used programming language, it tells the computer to perform certain operations in a certain order.

**Declarative language**
- like SQL or relational algebra, you just specify the pattern of the data you want—what conditions the results must meet, and how you want the data to be transformed - but not how to achieve that goal.
- typically more concise and easier to work with than an imperative API. And hides implementation details of the database engine
- SQL is more limited in functionality gives the database much more room for automatic optimizations.
- declarative languages often lend themselves to parallel execution
- CSS/XSL in the web is a good example of advantage of declarative queries over imperative approach of JS

## Graph-Like Data Models
**What if many-to-many relationships are very common**: RDM can handle some cases, but it's complex.
Graph DB is useful for data where anything is potentially related to everything
- Facebook maintains a single graph with many different types of vertices and edges:
  - vertices represent people, locations, events, checkins, and comments made by users;
  - edges indicate which people are friends with each other, which checkin happened in which location, who commented on which post, who attended which event, and so on

![Screenshot 2024-06-19 at 10.00.40 PM.png](..%2F..%2F..%2F..%2FDesktop%2FScreenshot%202024-06-19%20at%2010.00.40%E2%80%AFPM.png)
### Property graph model
Each vertex consists of:
- A unique identifier
- A set of outgoing edges
- A set of incoming edges
- A collection of properties (key-value pairs)

Each edge consists of:
- A unique identifier
- The vertex at which the edge starts (the tail vertex)
- The vertex at which the edge ends (the head vertex)
- A label to describe the kind of relationship between the two vertices
- A collection of properties (key-value pairs)

**Some important aspects**
1. Any vertex can have an edge connecting it with any other vertex. There is no schema that restricts which kinds of things can or cannot be associated.
2. Given any vertex, you can efficiently find both its incoming and its outgoing edges, and thus traverse the graph—i.e., follow a path through a chain of vertices —both forward and backward. (That’s why Example 2-2 has indexes on both the tail_vertex and head_vertex columns.)
3. By using different labels for different kinds of relationships, you can store several different kinds of information in a single graph, while still maintaining a clean data model.
4. Vertices and edges are not ordered - can sort only when querying.
5. Most support high-level declarative query language such as Cypher or SPARQL

**Cypher query languages for property graphs**
 ![Screenshot 2024-06-19 at 10.11.57 PM.png](..%2F..%2F..%2F..%2FDesktop%2FScreenshot%202024-06-19%20at%2010.11.57%E2%80%AFPM.png)

**Triple-Stores** (p56)
- all information is stored in the form of very simple three-part statements: (subject, predicate, object).
  - ex: In the triple (Jim, likes, bananas), Jim is the subject, likes is the predicate (verb), and bananas is the object.
- subject = vertex in a graph
- predicate & object
  1. key and value of a property: (lucy, age, 33) -> {"age": 33}
  2. another vertex: (lucy, marriedTo, alain) -> lucy & alain are both vertices, and predicate marriedTo is the label of the edge

**SPARQL**
