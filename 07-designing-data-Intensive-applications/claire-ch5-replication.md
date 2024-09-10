## Replication

Keeping a copy of the same data on multiple machines.

- To keep data geographically close to your users (and thus reduce latency)
- To allow the system to continue working even if some of its parts have failed(and thus increase availability)
- To scale out the number of machines that can serve read queries(and thus increase read throughput)

Three popular algorithms for replicating changes between nodes:

Single-leader
Multi-leader
Leaderless

Consideration (configuration options in databases)
synchronous vs asynchronous
how to halde failed replicas

A database consisted of one node vs Distributed databases

Eventual consistency
_read-your-writes_
_monotonic reads_

### Leaders and Followers

- How to ensure all the data ends up on all the replicas?
  - _leader-based replication_(aka active/passive or master-slave replication)
  - Writes are only accepted on the leader(the followers are read-only from the client's poitn of view)
  - PostgreSQL, MySQL, Oracle Data Guard, SQL Server's AlwaysOn Availability Groups, MongoDB, RethinkDB, Espresso, Kafka, RabbitMQ, DRBD

#### Synchronous Versus Asynchronous Replication

- Circumstances when follower might fall behind the leader by several minutes or more

  - If a follower is recovering from a failure
  - If the system is operating near maximum capacity
  - If there are network problems between the nodes

- Advantage of synchronous replication:
  - up-to-date copy
- Disadvantage of synchronous replication:
  - If the synchronous follower doesn't respond, the write cannot be processed.
    - Therefore, it is impractical for all followers to be synchronous
    - Any one node outage would cause the whole system to stop.
    - _semi-synchronous_ : One of the followers is synchronous, and the others are asynchronous.

#### Setting Up New Followers

- How to ensure the new follower has an accurate copy of the leader's data?
  - Locking the database(making it unavailable for writes)? -> low availability
  - Use Snapshot(Requires the snapshot is associated with an exact position in the leader's replication log. _sequence number_(PostgreSQL), _binlog coordinates_(MySQL))

#### Handling Node Outages

- How do you achieve high availability with leader-based replication?

##### Follower failure: Catch-up recovery

- Request the leader all the data changes occurred during the time when the follower was disconnected.

##### Leader failure: Failover

- Failover: New leader promotion, clients to send write to the new leader, the other followers to follow the new leader(consume data change from the new leader)
  - _Determining that the leader has failed_
  - _Choosing a new leader_
  - _Reconfiguring the system to use the new leader_
- Github incident

#### Implementation of Replication Logs

##### Statement-based replication

- The leader logs and sends every write request (SQL statement) to followers, who execute them as if received from a client. This method can break down due to nondeterministic functions, order-dependent statements, and side effects.

##### Write-ahead log(WAL) shippping

- The leader sends its append-only log of all database writes to followers, who process it to build identical data structures. While efficient, this method tightly couples replication to the storage engine, making version mismatches between leader and followers problematic.

##### Logical(row-based) log replication

- A sequence of records describing writes to database tables at the row level is used for replication. This method decouples replication from storage engine internals, allowing for better compatibility and easier parsing by external applications.

##### Trigger-based replication

- Custom application code is executed when data changes occur, logging these changes for external processes to replicate. While flexible, this method typically has higher overheads and is more prone to bugs than built-in replication methods.

### Problems with Replication Lag

- _Read-scaling_ architecture: If
- Asynchronous followers and leader-based replication: _eventual consistency_

  - LinkedIn search result

#### Reading Your Own Writes

- _read-after-write consistency_(aka _read-your-writes consistency_)

#### Monotonic Reads

- Strong consistency > _Monotonic reads_ > Eventual consistency
- Make sure each user always makes their reads from the same replica

#### Consistent Prefix Reads

- If a sequence of writes happens in a certain order, then anyone reading those wrties will see them appear in the same order.
- A problem in partitioned(sharded) databases.

#### Solutions for Replication Lag

- Replication lag in eventually consistent systems can lead to user experience issues, necessitating stronger guarantees like read-after-write consistency.
- While applications can provide stronger guarantees than the underlying database, this approach is complex and prone to errors.
- Transactions offer a way for databases to provide stronger guarantees, simplifying application development, though some distributed systems have moved away from them citing performance and availability concerns.

### Multi-Leader Replication

#### Use Cases for Multi-Leader Replication

##### Multi-datacenter operation

- Performance / Tolerance of datacenter outages / Tolerance of network problems

- Tungsten Replicator for MySQL, BDR for PostgreSQL, GoldenGate for Oracle

- Autoincrementing keys, triggers, and integrity constraints

##### Clients with offline operation

- Allows applications to work without internet connection, using local databases as leaders.
- Requires multi-leader replication to sync changes across devices when online.

##### Collaborative editing

- Enables multiple users to edit a document simultaneously in real-time.
- Can use either single-leader replication with locking or multi-leader replication with conflict resolution, depending on the desired user experience.

#### Handling Write Conflicts

##### Synchronous versus asynchronous conflict detection

- Single-leader databases handle conflicts synchronously, blocking or aborting conflicting writes.
- Multi-leader setups detect conflicts asynchronously, potentially after both writes have been accepted.

##### Conflict avoidance

- Routing all writes for a particular record through the same leader can prevent conflicts.
- This approach may break down when changing the designated leader for a record.

##### Converging toward a consistent state

- Multi-leader systems must ensure all replicas eventually reach the same final state.
- Various methods exist for convergent conflict resolution, including last write wins, replica ID prioritization, and custom merging strategies.

##### Custom conflict resolution logic

- Many multi-leader replication tools allow custom conflict resolution code.
- Resolution can occur on write (immediately when conflict is detected) or on read (when data is accessed).

#### Multi-Leader Replication Topologies

- Multi-leader replication topologies describe how writes are propagated between nodes, with options including all-to-all, circular, and star configurations.
- Different topologies have trade-offs in terms of fault tolerance and complexity, with more densely connected topologies generally offering better fault tolerance.
- Causality issues can arise in multi-leader setups, where writes may arrive at different replicas in the wrong order, potentially leading to inconsistencies.
- Many multi-leader replication systems have poor implementations of conflict detection and resolution, making it crucial to thoroughly test and understand the guarantees provided by your chosen system.

### Leaderless Replication

- Dynamo, Riak, Cassandra, and Voldmort: Dynamo-style
- Coordinator exists on behalf of the db client but it does not enforce ordering of writes

#### Writing to the Database When a Node Is Down

##### Read repair and anti-entropy

- Read repair detects and updates stale data during client reads.
- Anti-entropy processes continuously check for and copy missing data between replicas.

##### Quorums for reading and writing

- Quorum reads and writes ensure data consistency by requiring a minimum number of nodes to participate in each operation.
- The parameters n, w, and r can be configured to balance between consistency and availability in different workload scenarios.

#### Detecting Concurrent Writes

##### Last write wins (discarding concurrent writes)

- This conflict resolution method discards older values in favor of the most recent write, determined by timestamps.
- While it ensures eventual convergence, it can result in data loss, making it suitable only for scenarios where losing data is acceptable.

##### The “happens-before” relationship and concurrency

- Operations are concurrent if neither operation knows about the other, and one operation happens before another if it builds upon or depends on the first.
- This relationship helps determine the order of operations and resolve conflicts by overwriting earlier operations with later ones.

##### Captureing the happens-before relationship

- The algorithm uses version numbers to track the order of operations and determine concurrency, with the server incrementing version numbers for each write and clients including version numbers from prior reads in their writes.
- When writing, the server overwrites values with the same or lower version numbers but keeps values with higher version numbers, allowing it to maintain concurrent values and prevent data loss.

##### Merging concurrently written values

- Clients must merge concurrent values (siblings) to prevent data loss.
- Merging strategies vary, from simple union to more complex approaches like using tombstones for deletions.

##### Version vectors

- Version vectors track version numbers per replica and key to handle concurrent writes across multiple replicas.
- They allow databases to distinguish between overwrites and concurrent writes, ensuring safe reads and writes across different replicas.
