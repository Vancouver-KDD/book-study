# Transactions

_We believe it is better to have application programmers deal with performance problems due to overuse of transactions as bottlenecks arise, rather than always coding around the lack of transactions._

**Transaction:**
- a way for an application to group several reads and writes together into a logical unit. 
- Conceptually, all the reads and writes in a transaction are executed as one operation: either the entire transaction succeeds (commit) or it fails (abort, rollback). If it fails, the application can safely retry.
- With transactions, error handling becomes much simpler for an application.
- **simplify the programming model** for applications accessing a database. 
- safety guarantees
  - the application is free to ignore certain potential error scenarios and concurrency issues, because the database takes care of
them instead 
- Sometimes there are advantages to weakening transactional guarantees or abandoning them entirely (for example, to
achieve higher performance or higher availability). 
- Some safety properties can be achieved without transactions.

**How do you know whether you need transactions?**
- First understand what safety guarantees transactions can provide and costs

## The Slippery Concept of a Transaction
**Abandoned transaction**
- In the late 2000s, with NoSQL becoming popular, new data models, including replication and partitioning by default, Transactions were sacrificed.
- Distributed DB, Transactions were considered antithesis of scalability, and should be abandoned for good performance and high availability.

But Transactions have its own advantages and limitations.

### The Meaning of ACID
The safety guarantees provided by transactions are often described by the acronym ACID. 
- system not meeting ACID = BASE (Basically Available, Soft state, and Eventual consistency) 

#### Atomicity (Abortability)
something that cannot be broken down into smaller parts.
- describes what happens if a client wants to make several writes, but a fault occurs after some of the writes have been processed.
- If the writes are grouped together into an atomic transaction, and the transaction cannot be completed (committed) due to a fault, then the transaction is aborted and the database must discard or undo any writes it has made so far in that transaction.
- Without it, if an error occurs with multiple changes, it's difficult to know which changes have taken effect and which havent.
- With it, if a transaction was aborted, the application can be sure that it didn’t change anything, so it can safely be retried.

#### Consistency
ACID consistency: you have certain statements about your data (invariants) that must always be true
- Ex) accounting system, credits and debits must always be balanced
- consistency depends on the application’s notion of invariants, and it’s the application’s responsibility to define its transactions correctly so that they preserve consistency.
- This is not something that the database can guarantee: if you write bad data that violates your invariants, the database can’t stop you.
- consistency (in the ACID sense) is a property of the application, not of the database.

#### Isolation
Race conditions: If several clients are accessing the same database records, you can run into concurrency problems
**ACID isolation** 
- Concurrently executing transactions are isolated from each other: they cannot step on each other’s toes
  - if one transaction makes several writes, then another transaction should see either all or none of those writes, but not some subset.
- = serializability: each transaction can pretend that it is the only transaction running on the entire database.
- in practice, serializable isolation is rarely used, because it carries a performance penalty.

#### Durability
the promise that once a transaction has committed successfully, any data it has written will not be forgotten, even if there is a
hardware fault or the database crashes. 
- perfect durability doesn't exist
- In the replicated DB, meaning the data has been successfully copied to some number of nodes.
   - In order to provide a durability guarantee, a database must wait until these writes or replications are complete before reporting a transaction as successfully committed.
  
### Single-Object and Multi-Object Operations
**_Multi-object transactions_**
- ACID atomicity and isolation definitions assume that you want to modify several objects (rows, documents, records) at once. 
- It's often needed if several pieces of data need to be kept in sync.
- It requires some way of determining which read and write operations belong to the same transaction.
   - In relational databases, that is typically done based on the client’s TCP connection to the database server
   - Non relational DB don't have such way of grouping operations together. 

#### Single-object writes
Imagine you are writing a 20 KB JSON document to a database:
- If the network connection is interrupted after the first 10 KB have been sent, does the database store that unparseable 10 KB fragment of JSON?
- If the power fails while the database is in the middle of overwriting the previous value on disk, do you end up with the old and new values spliced together?
- If another client reads that document while the write is in progress, will it see a partially updated value?

Storage engines almost universally aim to provide atomicity and isolation on the level of a single object on one node. 
- Atomicity: using a log for crash recovery (see “Making B-trees reliable” on page 82)
- Isolation: using a lock on each object (allowing only one thread to access an object at any one time)

Single object operations are useful but **not transactions.**
- A transaction is a mechanism for **grouping multiple operations on multiple objects** into one unit of execution.

#### The need for multi-object transactions
Nothing that fundamentally prevents transactions in a distributed database
- even though many distributed datastores have abandoned multi-object transactions due to
   - difficult to implement across partitions
   - requiring very high availability and performance

**Why do we need multi-object transactions**
- A row of a table often has a foreign key reference to another table. The reference should stay valid
- Document data model. fields to update are often in the same document
  - but when document data model lacks join functionality, denormalization is encouraged then several documents should be updated in one go
- DB with secondary indexes (almost everything), indexes also need to be updated every write.
- above cases can be handled without transactions, but error handling becomes much more complicated without atomicity/isolation.


#### Handling errors and aborts
**ACID philosophy,** it would rather abandon the transaction entirely than allow it to remain half-finished.
- not all systems follow the philosophy
  - leaderless replication work as `best effort` basis, summarized as the Db will do as much as it can.

**The whole point of aborts is to enable safe retries**
- but not all object-relational mapping (ORM) frameworks (Rails ActiveRecord/Django) don't retry aborted transactions

**Retrying mechanism isn't perfect**
- transaction succeeded but the network failed, it performs twice
- error due to overload, retrying makes it worse
- retrying is only worth after transient errors (due to deadlock, isolation violation, temporary network interruptions, failover)
  - no point of retrying for permanent error (constraint violation)
- transaction has side effects outside of DB, it may happen even if the transaction is aborded
- If the client process fails while retrying, any data to be written is lost.

## Weak Isolation Levels


