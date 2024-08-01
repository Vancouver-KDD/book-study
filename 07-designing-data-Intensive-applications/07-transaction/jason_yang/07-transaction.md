
- **Challenges in Data Systems**: Ensuring reliability in data systems is difficult due to issues like software/hardware failures, application crashes, network interruptions, concurrent data writes, and partial updates.
- **Role of Transactions**: Transactions group multiple read and write operations into a single logical unit. If all operations succeed, the transaction commits; if any fail, it rolls back. Transactions help applications handle partial failures reliably.
- **Importance of Transactions**: Transactions simplify the application programming model by handling error scenarios and concurrency issues, so developers can ignore these complexities.
- **Need for Transactions**: Not all applications need transactions. In some cases, weakening or giving up transaction guarantees can improve performance or availability. Some safety can be achieved without transactions.
- **Deciding on Transactions**: Understanding the guarantees provided by transactions and their costs is crucial. Transactions appear simple but involve many details.
- **Chapter Summary**: This chapter discusses various problems in databases and algorithms to prevent them. It covers race conditions related to concurrency control and isolation levels (e.g., read committed, snapshot isolation, serializability). The next chapter addresses issues in distributed systems.

## The Slippery Concept of a Transaction

- **Transaction History**: Most relational and some non-relational databases support transactions, following the style first introduced by IBM System R in 1975. Systems like MySQL, PostgreSQL, Oracle, and SQL Server still use similar transaction support.
- **Rise of NoSQL Databases**: Emerging in the late 2000s, NoSQL databases aimed to overcome limitations of relational databases by offering new data models, replication, and partitioning. This often meant sacrificing transactions or offering weaker guarantees.
- **Transactions and Scalability**: With the trend of distributed databases, there was a belief that transactions hinder scalability. It was argued that large systems must forgo transactions for performance and availability. However, database vendors highlighted transactions as essential for critical applications.
- **Reality of Transactions**: The benefits and limitations of transactions are complex. Like other technical choices, transactions have trade-offs. Understanding the guarantees they provide and their role in normal and extreme scenarios is important.

### The Meaning of ACID

1. **ACID Definition**:
   - **Atomicity**: All operations in a transaction must be completed successfully or none at all.
   - **Consistency**: Transactions must leave the database in a consistent state, maintaining data integrity as defined by the application.
   - **Isolation**: Concurrent transactions should not interfere with each other, ideally appearing to run sequentially.
   - **Durability**: Once a transaction commits, the data must persist even in case of system failures.

2. **Atomicity**:
   - If an error occurs during multiple writes, the transaction is aborted, and all writes are canceled.
   - Ensures no duplicate or incorrect data is created if the same operation is retried by the application.

3. **Consistency**:
   - Data integrity is defined by the application, and the database cannot guarantee it.
   - For example, in an account system, the total credits and debits must always balance.

4. **Isolation**:
   - Prevents concurrency issues when multiple clients access the same data simultaneously.
   - Ideally, transactions should appear to run sequentially (serializability).
   - For performance reasons, weaker isolation levels like snapshot isolation are sometimes used.

5. **Durability**:
   - Once a transaction commits, data must be safely stored to prevent loss even in system failures.
   - Single-node databases use non-volatile storage (e.g., hard drives, SSDs) for this.
   - Replicated databases ensure durability by copying data across multiple nodes.

6. **Replication and Durability**:
   - No single technique provides perfect guarantees; a combination of disk writes, remote replication, and backups reduces risk.
   - Using multiple techniques together is crucial to protect data in various failure scenarios.

### Single-Object and Multi-Object Operations

1. **Atomicity**:
   - If an error occurs during multiple writes, the transaction is aborted, and all writes are canceled. This prevents partial failures and ensures all or nothing.

2. **Isolation**:
   - Ensures concurrent transactions do not interfere with each other. One transaction’s writes must be fully visible or not at all to other transactions.

3. **Multi-Object Transactions**:
   - Needed when multiple objects must be updated together. For example, displaying unread messages count in an email app requires maintaining consistency across multiple objects.

4. **Single-Object Writes**:
   - Must ensure atomicity and isolation even in network or power failures. Most storage engines provide atomicity and isolation at the single-object level.

5. **Need for Multi-Object Transactions**:
   - Many distributed data stores have given up multi-object transactions, but they are still needed in scenarios requiring coordinated writes across multiple objects, such as maintaining consistent foreign key references in relational models or updating multiple documents in document models.

6. **Handling Errors and Interruptions**:
   - The ability to safely abort and retry transactions is important. Retrying is a simple and effective error handling mechanism, but if a client thinks a transaction failed due to network issues, it might perform the same transaction twice. Retry mechanisms should include limits and exponential backoff to avoid exacerbating overload issues.

## Weak Isolation Levels

1. **Concurrency Issues**:
   - Concurrency issues occur when two transactions access the same data simultaneously.
   - Concurrency bugs are rare, hard to detect in testing, and difficult to reproduce.
   - To handle these, databases provide transaction isolation.

2. **Transaction Isolation**:
   - Isolation theoretically allows developers to work as if there is no concurrency. Serializability ensures transactions appear to execute one at a time.
   - However, serializability is costly, so many databases use weaker isolation levels for performance.

3. **Weak Isolation Levels**:
   - Weak isolation levels prevent some concurrency issues but are complex and can cause subtle bugs.
   - Concurrency bugs due to weak isolation levels have led to financial losses, audits, and customer data corruption in real scenarios.

4. **Approach to Solutions**:
   - Understanding the types of concurrency issues and how to prevent them is more important than relying on tools.
   - This understanding helps build reliable and accurate applications.

5. **Discussion Content**:
   - This section covers various weak isolation levels in practice, explaining each race condition.
   - Serializability is also discussed in detail, with informal explanations and examples of isolation levels.

### Read Committed

**Read Committed Isolation Level**:
- **Two Guarantees**:
  1. Read operations only see committed data (no dirty reads).
  2. Write operations only overwrite committed data (no dirty writes).

**Preventing Dirty Reads**:
- Prevents transactions from reading uncommitted data from other transactions.
- This ensures the database doesn’t show partially updated states, avoiding user confusion and accessing data that might be rolled back.

**Preventing Dirty Writes**:
- Prevents two transactions from concurrently updating the same object, avoiding overwriting uncommitted data.
- For example, if two people try to buy the same car simultaneously, the listing and invoice will not get updated inconsistently.

**Implementing Read Committed**:
- Most databases use row-level locks to prevent dirty writes. A transaction must acquire a lock before modifying an object and hold the lock until the transaction commits or aborts.
- To prevent dirty reads, databases ensure that ongoing write transactions only expose previously committed values.

**Significance**:
- Read Committed is the default setting in Oracle 11g, PostgreSQL, SQL Server 2012, MemSQL, and many others.

### Snapshot Isolation and Repeatable Read

**Problems with Read Committed**:
- Even with read committed, concurrency bugs can occur. For instance, a bank account balance may appear inconsistent during a transaction.

**Snapshot Isolation**:
- **Concept**: Each transaction reads from a consistent snapshot of the database, showing data as of the transaction’s start time.
- **Benefits**: Provides consistent data for backups and analytical queries, making it easier to understand. Avoids read-write lock contention.
- **Implementation**: Uses multi-version concurrency control (MVCC) to keep multiple versions of objects. Transactions have unique IDs, and objects are tagged with creation and deletion transaction IDs.

**Repeatable Read**:
- **Concept**: Defined in the SQL standard and similar to snapshot isolation, but the standard’s definition is ambiguous and incomplete. PostgreSQL and MySQL implement snapshot isolation as repeatable read.
- **Issues**: The SQL standard’s definition is vague, and guarantees vary by database. IBM DB2 uses repeatable read to mean serializability.

**Implementation**:
- **MVCC**: Maintains multiple versions of objects to provide a consistent snapshot.
- **B-Tree**: Systems like CouchDB, Datomic, and LMDB use modified pages to create new tree roots, ensuring a consistent snapshot.

### Preventing Lost Updates

**Problem Description**:
- **Lost Update Problem**: Occurs when two transactions read and modify the same value, causing one modification to be lost. For example, two users editing the same wiki page simultaneously.

**Solutions**:

1. **Atomic Write Operations**:
   - Use database-provided atomic update operations to remove the read-modify-write cycle.
   - Example: `UPDATE counters SET value = value + 1 WHERE key = 'foo';`
   - Available in most relational databases and document databases like MongoDB.

2. **Explicit Locking**:
   - Application explicitly locks the objects to be updated.
   - Example: Multiplayer game where players move the same figure simultaneously.
   - SQL Example:
     ```sql
     BEGIN TRANSACTION;
     SELECT * FROM figures WHERE name = 'robot' AND game_id = 222 FOR UPDATE;
     UPDATE figures SET position = 'c4' WHERE

 id = 1234;
     COMMIT;
     ```

3. **Automatically Detecting Lost Updates**:
   - Database automatically detects lost updates and aborts the transaction.
   - Supported in PostgreSQL’s repeatable read, Oracle’s serializable, SQL Server’s snapshot isolation.
   - Reduces the need for special handling in application code.

4. **Compare-and-Set**:
   - Perform the update only if the value hasn’t changed since it was last read.
   - Example: Used in wiki page updates.
   - SQL Example:
     ```sql
     UPDATE wiki_pages SET content = 'new content' WHERE id = 1234 AND content = 'old content';
     ```

5. **Conflict Resolution and Replication**:
   - In multi-leader or leaderless replication, concurrent modifications can happen on different nodes.
   - Create multiple versions of a value and merge them to resolve conflicts.
   - Riak 2.0’s data types automatically merge concurrent updates.

**Importance**:
- Different solutions are needed to prevent lost updates, each suited for specific scenarios.
- Choosing the right method effectively manages concurrency issues.

### Write Skew and Phantoms

**Write Skew Problem**:
- **Example**: Two doctors at a hospital simultaneously request leave, leading to no doctors on call.
- **Nature**: Write skew is different from dirty writes or lost updates and occurs when multiple objects are involved.

**Solutions**:
1. **Atomic Single-Object Operations**: Not effective when multiple objects are involved.
2. **Automatic Detection**: Databases like PostgreSQL, MySQL, and Oracle don’t automatically detect write skew.
3. **Explicit Locking**: Explicitly lock rows the transaction depends on.
   ```sql
   BEGIN TRANSACTION;
   SELECT * FROM doctors WHERE on_call = true AND shift_id = 1234 FOR UPDATE;
   UPDATE doctors SET on_call = false WHERE name = 'Alice' AND shift_id = 1234;
   COMMIT;
   ```

**Other Examples**:
- **Meeting Room Booking System**: Prevent double booking.
- **Multiplayer Game**: Prevent two pieces from being placed on the same position.
- **Username Registration**: Prevent duplicate usernames.
- **Double-Spending Prevention**: Prevent spending more than the account balance.

**Phantom Problem**:
- **Definition**: A write in one transaction changes the result of a search query in another transaction.
- **Solution**: Use the Materializing Conflicts approach by artificially introducing lock objects.

**Materializing Conflicts**:
- **Method**: Create a table of time slots and rooms for booking systems and use it as lock objects.
- **Limitations**: Difficult to implement, and concurrency control mechanisms may leak into the application data model.

**Recommendation**:
- Use serializable isolation levels whenever possible.

### Serializability

**Problems**:
- Isolation levels are hard to understand and vary across databases.
- It’s difficult to ensure application code runs safely at a particular isolation level.
- There are no good tools for detecting race conditions, and concurrency issues are hard to test due to their nondeterministic nature.

**Solution**:
- **Serializable Isolation**: The strongest isolation level, ensuring that the result of parallel transactions is the same as if they were executed one by one. Prevents all possible race conditions.

**Implementing Serializable Isolation**:
1. **Actual Serial Execution**: Execute transactions sequentially.
2. **Two-Phase Locking (2PL)**: Used for several decades as the only viable option.
3. **Optimistic Concurrency Control**: Techniques like Serializable Snapshot Isolation (SSI).

**Focus on Single-Node Databases**:
- The next chapter will discuss transactions involving multiple nodes in a distributed system.

### Actual Serial Execution

**Concept**:
- **Single-Threaded Execution**: Execute transactions one by one, removing concurrency issues and ensuring serializability.
- **Changes**:
  - RAM became cheaper, allowing entire active datasets to be kept in memory.
  - OLTP transactions are short with few read and write operations.

**Implementation**:
- **Using Stored Procedures**: Applications must submit the entire transaction code to the database in advance to execute transactions quickly in a single thread.
- **Advantages and Disadvantages of Stored Procedures**:
  - **Advantages**: Execute quickly without waiting for I/O.
  - **Disadvantages**: Difficult to debug, manage in version control, test, and monitor. Different languages used by each database vendor (Oracle PL/SQL, SQL Server T-SQL, etc.).

**Modern Implementations**:
- Systems like VoltDB, Datomic, and Redis use general-purpose programming languages (Java, Clojure, Lua) for stored procedures.
- Execute all transactions in a single thread to avoid concurrency control overhead and achieve good throughput.

**Partitioning**:
- **Need**: Limited by the speed of a single CPU core. Partition the dataset to allow each partition to handle transactions independently.
- **Limitations**: Cross-partition transactions require additional coordination overhead and can be very slow.

**Summary**:
- **Advantages**: Handles small transactions quickly, keeps the dataset in memory, processes on a single CPU core.
- **Limitations**: Slow transactions can stall the entire process, large datasets in memory cause performance degradation, limited by single CPU core throughput, and cross-partition transactions slow down significantly.

### Two-Phase Locking (2PL)

**Concept**:
- 2PL ensures serializability in databases using locks for read and write operations.

**How It Works**:
- Uses shared locks (for reads) and exclusive locks (for writes).
- Transactions must acquire a shared lock to read and an exclusive lock to write. Shared locks can be held by multiple transactions, but exclusive locks are held by only one transaction.
- Transactions can upgrade a shared lock to an exclusive lock if needed.
- Locks are held until the transaction completes, either by committing or aborting.

**Advantages**:
- Prevents all forms of race conditions and ensures serializability.

**Disadvantages**:
- Performance degradation due to the overhead of acquiring and releasing locks, and reduced concurrency.
- Increased waiting time as transactions form queues waiting for locks.
- Unstable latency, particularly at high percentiles.
- Deadlocks, where transactions wait indefinitely for each other, require automatic detection and resolution by aborting one of the transactions.

**Predicate Locks**:
- Locks that apply to all objects matching a search condition to prevent phantom issues.
- Not commonly used due to performance issues.

**Index-Range Locks**:
- Use index-range locks to approximate predicate locks and prevent phantom issues.
- More efficient with lower overhead, providing effective protection.

### Serializable Snapshot Isolation (SSI)

**Concept**:
- SSI guarantees serializability with minimal performance impact, using optimistic concurrency control.
- First introduced in 2008 and used in PostgreSQL 9.1 and distributed databases like FoundationDB.

**Optimistic vs. Pessimistic Concurrency Control**:
- **Pessimistic Concurrency Control**: Wait if there is a potential conflict (e.g., 2PL).
- **Optimistic Concurrency Control**: Continue and check for conflicts at commit time, aborting if necessary (e.g., SSI).

**Key Principles**:
- Transactions read from a consistent snapshot.
- At commit time, the database checks for conflicts between reads and writes.

**Conflict Detection**:
1. **Stale MVCC Reads**:
   - Detect when a transaction reads an outdated value from a snapshot. Abort if the value has been modified by another committed transaction.
2. **Writes Affecting Prior Reads**:
   - Track transactions’ read operations in indexes. If another transaction modifies the read data, the original transaction is notified, and the conflicting transaction is aborted if it tries to commit.

**Performance**:
- **Advantages**: Transactions proceed without waiting for locks, read-only queries run without locks.
- **Disadvantages**: High conflict rates impact performance, requiring short read-write transactions to minimize conflicts.
- **Scalability**: Distributed databases like FoundationDB distribute conflict detection, allowing high throughput across multiple machines.

These summaries translate the key points into simpler English for better understanding.