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
  - No point of retrying for permanent error (constraint violation)
- transaction has side effects outside of DB, it may happen even if the transaction is aborded
- If the client process fails while retrying, any data to be written is lost.

## Weak Isolation Levels
Concurrency issues (race conditions)
- when one transaction reads data being modified by another transaction, or when two transactions try to modify the same data simultaneously
- hard to test 
- DB tried to hide concurrency issues from application developer by transaction isolation in theory, serializable isolation
   - DB guarantees as if transactions run serially  
   - but in practice, the performance cost is high - thus **weaker levels of isolation**
- Weaker levels of isolation doesn't prevent all concurrency issues
   - leading to subtle bugs

_Rather than blindly relying on tools, we need to develop a good understanding of the kinds of concurrency problems that exist, and how to prevent them. Then we can build applications that are reliable and correct, using the tools at our disposal._

### Read Committed
- Most basic level of transaction isolation
- two guarantees: No dirty reads, No dirty writes

#### No dirty reads
Any writes by a transaction only become visible to others when that transaction commits (and then all of its writes become visible at once).

**dirty read**
- a transaction has written some data to the database, but the transaction has not yet committed or aborted. Then another transaction sees that uncommitted data
- Why problem?
  - If a transaction needs to update several objects, a dirty read means that another transaction may see some of the updates but not others. Seeing the database in a partially updated state is confusing to users and may cause other transactions to take incorrect decisions.
  - A transaction may see data that is later rolled back, which is never actually committed to the database.

#### No dirty writes
Transactions running at the read committed isolation level must prevent dirty writes, usually by delaying the second write until the first write’s transaction has committed or aborted.

**dirty write**
- if the earlier write is part of a transaction that has not yet committed, so the later write overwrites an uncommitted value
- Why problem?
  - If transactions update multiple objects, dirty writes can lead to a bad outcome.
- Read committed does not always prevent the race condition. If the second write happens after the first transaction has committed(so not a dirty write), but still incorrect for a different reason (discussed in Preventing Lost Update).


#### Implementing read committed
- Very popular isolation level, default setting in many DB

**DB prevent dirty writes by row-level locks**
- when a transaction wants to modify a particular object (row or document), it must first acquire a lock on that object.
- It must then hold that lock until the transaction is committed or aborted.
- Only one transaction can hold the lock for any given object

**How to prevent dirty read?**
- For every object being written, DB remembers the old and new value
- while the transaction is ongoing, other read transaction read from the old value
- long-running (as dirty write prevention) is not an option
   - one long-running write transaction can block many read only transactions, harming the response time and operability


### Snapshot Isolation and Repeatable Read
With read committed isolation, concurrency bugs can still occur.

**Read skew = Nonrepeatable read**
- While a read commit going on, update happens and the user sees the incorrect value, but eventually after the read commit, the read will be correct
- some situations cannot tolerate such temporary inconsistency, usually acceptable under read committed isolation though
   - Backups: During back up, if some part contains older version while other containing newer version, the inconsistency is permanent.
   - Analytic queries and integrity checks: query that scans over large part of the DB, likely to return nonsensical results if parts of DB at different points of time

**Snapshot isolation** 
- the most common solution to the problems above
- each transaction reads from a consistent snapshot of the database
  - the transaction sees all the data that was committed in the database at the _start of the transaction._
  - as if frozen at a particular point in time

#### Implementing snapshot isolation
- typically use write locks to prevent dirty writes
**- readers never block writers, and writers never block readers.**

**multi-version concurrency control (MVCC)**
- The DB maintains several versions of an object side by side
- The database must potentially keep several different committed versions of an object, because various in-progress transactions may need to see the state of the database at different points in time. 
- storage engines supporting snapshot isolation typically use MVCC for read committed isolation level

#### Visibility rules for observing a consistent snapshot
When a transaction reads from the database, transaction IDs are used to decide which objects it can see and which are invisible.

_Visibility rules_ (apply to creation and deletion both)
1. At the start of each transaction, the database makes a list of all the other **transactions that are in progress** (not yet committed or aborted) at that time. Any writes that those transactions have made are ignored, even if the transactions subsequently commit.
2. Any writes made by **aborted transactions** are ignored.
3. Any writes made by transactions with a **later transaction ID** (started after the current transaction started) are ignored, regardless of whether those transactions have committed.
4. All other writes are visible to the application’s queries.
5. Thus, an object is visible if
  - At the time when the reader’s transaction started, the transaction that created the object had already committed.
  - The object is not marked for deletion, or if it is, the transaction that requested deletion had not yet committed at the time when the reader’s transaction started.

#### Indexes and snapshot isolation
One option is to have the index simply point to all versions of an object and require an index query to filter out any object versions that are not visible to the current transaction.
- PostgreSQL: optimizations for avoiding index updates if different versions of the same object can fit on the same page
- CouchDB, Datomic: append-only/copy-on-write variant that does not overwrite pages of the tree when they are updated, but instead creates a new copy of each modified page.

#### Repeatable read and naming confusion
Snapshot isolation is called in the different DB systems
- Oracle: serializable
- PostgreSQL and MySQL: repeatable read - their definition is flawed though

### Preventing Lost Updates
_lost update problem_
- Issue regarding concurrent writes
- If an application reads some value from DB, modifies it, and writes back the modified value (read-modify-write cycle). If two transactions do this concurrently, one of the modifications can be lost, because the second write does not include the first modification.
- examples:
  - Incrementing a counter or updating an account balance (requires reading the current value, calculating the new value, and writing back the updated value)
  - Making a local change to a complex value, e.g., adding an element to a list within a JSON document (requires parsing the document, making the change, and writing back the modified document)
  - Two users editing a wiki page at the same time, where each user saves their changes by sending the entire page contents to the server, overwriting whatever is currently in the database

#### Atomic write operations (cursor stability)
UPDATE counters SET value = value + 1 WHERE key = 'foo';
- removes the need to implement read-modify-write cycles in application code
- Taking an exclusive lock on the object when it is read so that no other transaction can read it until the update has been applied
- Not all writes can easily be expressed in terms of atomic operations: for example, updates to a wiki page involve arbitrary text editing

#### Explicit locking
The application locks the object to be updated
- Read request wait until the write is done
- ex) multi players game, moving the same figure concurrently - preventing two players from moving the same piece

#### Automatically detecting lost update
Done by Database feature - if the transaction manager detects a lost update, abort the transaction and force it to retry its read-modify-write cycle.

#### Compare-and-set
Whenever updating, check if the value has not changed since last read, if then, read again then write

#### Conflict resolution and replication
A common approach is to allow concurrent writes to create several conflicting versions of a value (also known as siblings)
- then to use application code or special data structures to resolve and merge these versions after the fact.
- Techniques based on locks or compare-and-set (for the single up-to-date copy) do not apply in this context


### Write Skew and Phantoms
- subtler examples of conflicts than dirty writes or lost updates
- Example: two on-call doctors requesting leave at the same time, updating the different objects - then no one is on call

#### Characterizing write skew
_**Write skew**_
- The two transactions are updating two different objects (Alice’s and Bob’s on- call records, respectively), ending up undesired condition
- Limited options to resolve
  - atomic single-object doesn't work for multiple objects write
  - automatic detection doesn't help
  - DB object constraint to prevent write skew involve mutiple objects, and most DB doesn't have such support
- Explicit lock would be the only way

#### More examples of write skew
- Meeting room booking system - multiple requests reads the no current booking, then end up conflicting meetings reservation: Only serializable isolation is the answer
- Multiplayer game - players moving two different figures to the same position violating the rules

_**phantom pattern**_
where a write in one transaction changes the result of a search query in another transaction.
1. SELECT query checks if some requirement is satisfied
2. If satisfied, go ahead and make a write then commit - this write changes the condition for the requirement

#### Materializing conflicts
EX) meeting room problem: Creating a table with time slots and rooms for a duration in time like six months
- Each transaction can then lock the rows during the operation, forcing other transactions to wait to execute their first data fetch.
- hard to implement and error-prone, so as last resort

## Serializability
The strongest isolation level
- guarantees that even though transactions may execute in parallel, the end result is the same as if they had executed one at a time, serially, without any concurrency.
- the database prevents all possible race conditions.

Then why isn't everyone using it? To answer that, explore the techniques for the serializability

### Actual Serial Execution
Execute only one transaction at a time, in serial order, on a single thread. 
- Was not considered feasible due to performance but recently was rethought as feasible
   - with the development of cheap RAM
   - shorter and smaller number of OLTP (online transaction processing)
- To utilize the single thread throughput, transactions need to be structured differently

#### Encapsulating transactions in stored procedures
Transactions have continued to be executed in an interactive client/server style, one statement at a time.
- A transaction does not span multiple requests. A new HTTP request starts a new transaction.
- ex) booking an airline ticket, DB not waiting until it's completed to the last stage

_**Stored procedure**_
- the application must submit the entire transaction code to the database ahead of time
- as systems with single-threaded serial transaction processing don’t allow interactive multi-statement transactions

#### Pros and cons of stored procedures
**Cons and how modern implementations can overcome**
- Each database vendor has its own language for stored procedures, lacking the ecosystem of libraries
   - Now store procedures use general-purpose programming languages
- Code running in a database is difficult to manage: compared to an application server, it’s harder to debug
- A badly written stored procedure (e.g., using a lot of memory or CPU time) in a database can cause much more trouble than in application server

**Pros**
- stored procedures and in-memory data, executing all transactions on a single thread 
- VoltDB uses it for replication - executes the same stored procedure on each replica, instead of copying transactions writes

#### Partitioning
Cross-partition transactions are possible but have additional coordination overhead
- vastly slower than single-partition transactions

### Two-Phase Locking (2PL)
Readers block writers, writers block readers 
- opposed to _readers never block writers, and writers never block_ from snapshot isolation
- protects against all the race conditions discussed earlier, including lost updates and write skew

#### Implementation of two-phase locking
Each object has a lock, either in a shared mode or exclusive mode
- several transactions are allowed to hold the shard mode lock simultaneously, but should wait if the object is locked in exclusive mode by other transaction
- A trasaction continues to hold the lock until the end of the transaction
- Reads acquires a shared mode lock, writes acquires exclusive mode lock
- A transaction including a read then write, first gets the shared lock then upgrades to an exclusive lock
- used by the serializable isolation level in MySQL (InnoDB) and SQL Server, and the repeatable read isolation level in DB2

_**Deadlock**_
- transaction A is stuck waiting for transaction B to release its lock, and vice versa
- DB automatically detects deadlocks and aborts one of them, aborted transaction should be retried by the application, not DB

#### Performance of two-phase locking
Bad performance with unstable latencies
- overhead of acquiring and releasing all those locks, but more importantly due to reduced concurrency
   - If two concurrent transactions try to do anything that may in any way result in a race condition, one has to wait for the other to complete.
   - And no limit on how long it needs to wait
- If deadlocks occur frequently, meaning the work needs to be retried, wasting significant efforts
- Until 1970s, only one widely used algorithm for serializability in databases for 30 years but not after due to performance

#### Predicate locks
Similar to the shared/exclusive lock described earlier, but it belongs to all objects that match some search condition, not belonging to a particular object
- To resolve phantoms leading write skews
- Predicate lock applies even to objects that do not yet exist in the database, but which might be added in the future
- do not perform well: if there are many locks by active transactions, checking for matching locks becomes time-consuming

#### Index-range locks (next-key locking)
- simplified approximation of predicate locking, by making it match a greater set of objects.
- less precise but lower overheads, so most common implementation for DBs with 2PL 
- ex) predicate lock for booking of room 123 between 12 - 1pm
- index-range lock: booking of room 123 at any time or locking all rooms between 12 - 1pm

### Serializable Snapshot Isolation (SSI)
Are serializable isolation and good performance fundamentally at odds with each other?
- SSI answers promisingly, with full serializability but with only small performance penalty, compared to snapshot isolation


#### Pessimistic versus optimistic concurrency control
- 2PL and serial executions are pessimistic - based on if anything could go wrong, better to wait until safe

_**SSI is optimistic concurrency control**_
- instead of blocking if something potentially dangerous happens, transactions continue with the hope everything will be alright
- If DB found out something bad happened, then action is aborted and recommited
- Performs better if there's enough spare capacity with less contention, than pessimistic controls
*Contention: when multiple processes are trying to access the same data at the same time. 

#### Decisions based on an outdated premise
- Snapshot isolation - transaction is taking an action based on a premise (which was true at the beginning of the transaction, but no longer be true later)
- How does DB know if a query result might have changed?
  - detecting reads of a stale MVCC (multi version concurrency control)
  - detecting the writes affecting prior reads 

#### Detecting stale MVCC reads
Example of on call doctors
- Abort as the transaction wants to commit, the DB checks whether any of the ignored writes have now been commited, then abort.
- Why wait until commit? DB can't tell if a transaction starting with a read might include write or not, or if the read will turn out to be stale or not

#### Detecting writes that affect prior reads
When a transaction writes to the database, it must look in the indexes for any other transactions that have recently read the affected data
- similar to acquiring a write lock on the affected key range, but not blocking until the readers have committed.
- it simply notifies the transactions that the data they read may no longer be up to date

#### Performance of serializable snapshot isolation
- Compared to two-phase locking
  - one transaction doesn’t need to block waiting for locks held by another transaction.
  - Like under snapshot isolation, writers don’t block readers, and vice versa.
  - query latency much more predictable and less variable. In particular, read-only queries can run on a consistent snapshot without requiring any locks

- Compared to serial execution
   - not limited to the throughput of a single CPU core: allowing it to scale to very high throughput. 
