# Chapter 2: Data Models and Query Languages 1

- Most applications are built by layering one data model on top of another.
- Each layer hides the complexity of the layers below it by providing a clean data model.
- There are many different kinds of data models, and every data model embodies assumptions about how it is going to be used.

## Relational Model Versus Document Model
- The goal of the relational model was to hide that implementation detail behind a cleaner interface.

### The Birth of NoSQL
- A need for greater **scalability** than relational databases can easily achieve, including very large datasets or very high write throughput
- A **widespread preference** for free and open source software over commercial database products
- **Specialized query operations** that are not well supported by the relational model 
- Frustration with **the restrictiveness of relational schemas**, and a desire for a more dynamic and expressive data model

### The Object-Relational Mismatch
- Object-relational mapping (ORM) frameworks like ActiveRecord and Hibernate reduce the amount of boilerplate code required for this translation layer, but they can’t completely hide the differences between the two models.
- The JSON representation has better locality than the multi-table schema

### Many-to-One and Many-to-Many Relationships
- Removing such duplication is the key idea behind normalization in databases.
- Unfortunately, normalizing this data requires many-to-one relationships
- In relational databases, it’s normal to refer to rows in other tables by ID, because joins are easy. 
- In document databases, joins are not needed for one-to-many tree structures, and support for joins is often weak.

### Relational Versus Document Databases Today
- Document data model: 
  1. schema flexibility
  2. better performance due to locality, 
  3. and that for some applications it is closer to the data structures used by the application. 
- Relational model: 
  1. providing better support for joins, 
  2. and many-to-one and many-to-many relationships.

#### Schema flexibility in the document model 
- Schema-on-read is similar to dynamic (runtime) type checking in programming languages, 
- whereas schema-on-write is similar to static (compile-time) type checking.

#### Data locality for queries 
- The locality advantage only applies if you need large parts of the document at the same time.
- It’s worth pointing out that the idea of grouping related data together for locality is not limited to the document model.

#### Convergence of document and relational databases
- It seems that relational and document databases are becoming more similar over time