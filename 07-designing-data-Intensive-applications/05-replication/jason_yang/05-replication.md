## Definition and Need for Replication

1. **Definition of Replication**:
    - **Definition**: Replication means copying the same data to multiple machines connected by a network.
    - **Need**:
        - Reducing latency by keeping data close to users' geographical locations.
        - Increasing availability by ensuring the system continues to operate even if part of it fails.
        - Increasing read throughput by allowing more machines to handle read queries.

2. **Dataset Size**:
    - This chapter assumes a dataset small enough for each machine to hold a copy of the entire dataset.
    - In Chapter 6, we relax this assumption and discuss **sharding** a dataset that is too large for a single machine.

3. **Challenges of Replication**:
    - If data never changes, replication is easy. You copy the data to all nodes once.
    - The difficulty of replication lies in handling data changes.

4. **Replication Algorithms**:
    - Three popular algorithms:
        - Single-leader replication
        - Multi-leader replication
        - Leaderless replication
    - Most distributed databases use one of these approaches. We will examine the pros and cons of each algorithm in detail.

5. **Hot, Warm, Cold Standby Servers**:
    - Example with PostgreSQL:
        - **Hot Standby**: A replica that allows clients to read from it.
        - **Warm Standby**: Processes changes from the leader but does not handle client queries.
    - This distinction is not important in this book.

6. **Trade-offs**:
    - Synchronous vs. asynchronous replication.
    - Handling failed replicas.
    - These choices are often dependent on database configuration options, but general principles are similar across implementations.

7. **Historical Context**:
    - The principles of database replication have not changed much since the 1970s.
    - Basic constraints of the network have remained the same.
    - Many developers have long assumed databases to be single-node systems.
    - [[Mainstream use of distributed databases is a recent development]].
    - Many application developers [[misunderstand issues like eventual consistency]].
    - We will discuss guarantees like **read-your-writes** and **monotonic reads** in the context of "Replication Lag".

## Leaders and Followers

### Replication and Leader-Based Replication

1. **Replica**:
    - Refers to each node storing a copy of the database.
    - When there are multiple replicas, it is essential to ensure that all data is stored on all replicas.

2. **Handling Write Operations**:
    - All write operations to the database must be processed by all replicas. Otherwise, data consistency among replicas will break.

3. **Leader-Based Replication**:
    - **Leader**:
        - One replica is designated as the leader (also known as the master or primary).
        - When a client requests a write operation to the database, the request is sent to the leader.
        - The leader first writes the new data to its local storage.
    - **Followers**:
        - Other replicas are designated as followers (read replicas, slaves, secondaries, or hot standbys).
        - Once the leader writes the new data to its local storage, it sends the data changes to all followers as a replication log or change stream.
        - Each follower uses the log from the leader to update its local database in the same order.
    - **Read Operations**:
        - When a client wants to read from the database, it can query either the leader or the followers.
        - However, write operations are only accepted by the leader, and from the client's perspective, followers are read-only.

4. **Applications**:
    - **Relational Databases**: PostgreSQL, MySQL, Oracle Data Guard, SQL Server’s AlwaysOn Availability Groups.
    - **Non-Relational Databases**: MongoDB, RethinkDB, Espresso.
    - **Other Systems**: Distributed message brokers (Kafka, RabbitMQ), network file systems, replicated block devices (DRBD).

Leader-based replication is a crucial technology for maintaining data consistency in databases and providing high availability and scalability.

### Synchronous Versus Asynchronous Replication

- **Synchronous Replication**: The leader waits for the follower to acknowledge the write before reporting success and marking the write as committed to other clients.
    - **Pros**: Ensures that followers have an up-to-date copy of the data consistent with the leader. Guarantees that data exists on the followers even if the leader suddenly fails.
    - **Cons**: If a synchronous follower does not respond (due to failure, network issues, etc.), the write operation cannot be processed, and the leader must block all writes until the synchronous replica is available again.

- **Asynchronous Replication**: The leader sends the message and does not wait for the follower's response.
    - **Pros**: Allows the leader to continue processing write operations even if followers lag behind, keeping the system running.
    - **Cons**: If the leader fails and cannot recover, all writes not yet replicated to the followers may be lost. Even writes acknowledged to the clients may not be durable.

**Mixed Approach (Semi-Synchronous)**:
- Most systems use one synchronous follower and multiple asynchronous followers.
- If the synchronous follower is unavailable or slow, one of the asynchronous followers can take over as the synchronous replica.
- Ensures that the latest data exists on at least two nodes (the leader and one synchronous follower).

**Typical Configurations**:
- With many followers or geographically distributed replicas, most use asynchronous replication.
- Fully asynchronous replication allows the leader to continue processing write operations even if all followers are lagging.

**Lag Issues**:
- Replication is usually fast, but in cases of network issues, system capacity problems, or failure recovery situations, followers may lag behind the leader by several minutes.

This summary explains the workings, pros and cons, typical configurations, and lag issues of synchronous and asynchronous replication.

### Setting Up New Followers

Occasionally, there is a need to set up new followers. This may be required to increase the number of replicas or to replace a failed node. Ensuring that new followers accurately replicate the leader's data involves the following steps:

1. **Creating a Consistent Snapshot**:
    - Create a snapshot of the leader's database without locking the entire database.
    - Most databases provide this feature for backup purposes, and in MySQL, tools like `innobackupex` may be needed.
2. **Copying the Snapshot**:
    - Copy the created snapshot to the new follower node.
3. **Applying Data Changes**:
    - The follower connects to the leader to request all data changes since the snapshot was created.
    - This process relies on the snapshot being associated with an exact position in the leader's replication log, known as the log sequence number in PostgreSQL or the binlog coordinates in MySQL.
4. **Synchronizing the Follower**:
    - Once the follower processes all data changes since the snapshot, it is synchronized with the leader.
    - The follower can then continue processing real-time data changes from the leader.

**Differences by Database**:
- The actual steps to set up new followers vary significantly by database.
- Some systems automate this process entirely, while others may require administrators to follow complex manual workflows.

### Handling Node Outages

**Handling Node Outages**

Any node within the system can fail unexpectedly. Nodes can also be brought down for planned maintenance, such as rebooting a machine to install kernel security patches. The ability to reboot individual nodes without downtime is a significant advantage for operations and maintenance. Therefore, the goal is to keep the system running despite individual node failures and to minimize the impact of node outages.

**Achieving High Availability with Leader-Based Replication**

#### Follower Failure: Catch-up Recovery

- Each follower logs the data changes received from the leader to its local disk.
- When a follower crashes and restarts or temporarily loses connection to the leader, it can determine the last transaction processed before the crash from the log.
- The follower reconnects to the leader, requests all data changes that occurred during the downtime, and applies them to catch up with the leader.

#### Leader Failure: Failover

- Promote one of the followers to be the new leader, reconfigure clients to send write requests to the new leader, and ensure other followers receive data changes from the new leader.
- Failover can be manual (administrator detects leader failure and sets up the new leader) or automatic.

##### Steps in Automatic Failover:

1. Detect Leader Failure: Nodes exchange messages, and if a node does not respond for a specified time (e.g., 30 seconds), it is considered dead.
2. Select a New Leader: Through an election process or pre-selected controller node appoints the new leader. The replica with the most recent data is usually chosen as the new leader.
3. Reconfigure the System: Ensure clients send write requests to the new leader, and when the old leader returns, it is demoted to a follower and recognizes the new leader.

#### Issues with Failover:

- With asynchronous replication, the new leader may not have received all writes from the old leader, leading to conflicts when the old leader returns.
- Write loss can cause coordination issues with other external storage systems.
- Split-brain scenarios (where two nodes believe they are the leader) can cause data loss or corruption.
- Proper timeout settings before declaring a leader dead are crucial; too long increases recovery time, too short causes unnecessary failovers.

Given these challenges, some operations teams prefer manual failover even with automatic failover software. Node failures, unstable networks, trade-offs between consistency, durability, availability, and latency are fundamental issues in distributed systems, covered in more detail in Chapters 8 and 9.


## Implementation of Replication Logs

This section explains how leader-based replication works through various replication methods.

### Statement-based replication

- Logs all write requests (statements) executed by the leader and sends them to followers.
- Issues:
    - Non-deterministic functions (e.g., NOW(), RAND()) may generate different values.
    - Statements dependent on existing data or with auto-increment columns must be executed in the same order.
    - Statements with side effects (triggers, stored procedures, user-defined functions) may yield different results on each replica.
- Used in MySQL before version 5.1; now switches to row-based replication when non-deterministic cases are detected.

### Write-ahead log (WAL) shipping

- The leader writes a log of changes to disk and sends it to followers over the network.
- Followers process this log to replicate the same data structure as the leader.
- Used in PostgreSQL, Oracle.
- Drawbacks:
    - Logs include very low-level data changes.
    - Different versions of database software cannot run on the leader and followers if storage format changes.

### Logical (row-based) log replication

- Separates replication log from the storage engine.
- Logical logs are a sequence of records describing writes at the row level:
    - Inserted rows: contain new values for all columns.
    - Deleted rows: contain unique identifiers such as primary keys.
    - Updated rows: contain unique identifiers and new values for changed columns.
- MySQL's binlog uses this approach.
- Logical logs are storage engine independent and easier for external applications to interpret.

### Trigger-based replication

- Implement replication at the application code level, not the database system.
- Use triggers to automatically execute custom code on data changes.
- Useful for flexibility but can have significant overhead, bugs, or limitations.
- Examples: Oracle GoldenGate, Databus for Oracle, Bucardo for Postgres.

## Problems with Replication Lag

### The Need for Replication to Tolerate Node Failures

Replication is desired not just for tolerating node failures but also for scalability (handling requests beyond a single machine's capacity) and latency (placing replicas close to users geographically).

### Leader-based Replication

- All writes must go through a single node, but read-only queries can be performed on all replicas.
- In workloads with mostly reads, generating many followers and distributing read requests among them reduces load on the leader and allows handling read requests from nearby replicas.
- This read scaling architecture can handle more read-only requests by adding more followers. However, this is realistically feasible only with asynchronous replication. Synchronously replicating to all followers makes the system write-unavailable during single node failures or network interruptions.

### Issues with Asynchronous Replication

- Performing reads on asynchronous followers can result in seeing outdated information if followers lag behind. This can lead to consistency issues in the database.
- Running the same query at the same time on both the leader and a follower can yield different results. This temporary state, known as "eventual consistency," means the follower will eventually sync with the leader if writes are paused momentarily.
- The term "eventually" is intentionally vague, with no guarantee on how far replicas may lag. Under normal conditions, replication lag is usually under a second. However, during capacity nearing or network issues, lag can extend to several seconds or minutes.
- Large replication lag can become a practical issue for applications, not just a theoretical concern.

### Reading Your Own Writes

**Problem Definition**:

- When users submit data and immediately query it, asynchronous replication can result in followers not yet reflecting the data, making it appear lost to the user. To prevent this, 'read-after-write consistency' is needed.

**Solutions**:

1. **Reading from the Leader**:
    - Read data from the leader for queries involving user modifications, while other reads can be performed on followers. For example, always read user profile information from the leader.
2. **Time-based Approach**:
    - Perform all reads from the leader for a certain time after user data modifications. For example, read from the leader for one minute after the last update.
    - Monitor follower replication lag and avoid reading from followers lagging more than a minute behind the leader.
3. **Using Timestamps**:
    - The client remembers the timestamp of the most recent write operation and only reads from replicas that reflect updates after that timestamp.
    - If no replica is sufficiently up-to-date, read from another replica or wait until a replica is sufficiently up-to-date.
4. **Multiple Datacenters**:
    - Route requests to the datacenter where the leader resides and read from the leader.

**Additional Considerations**:

- **Cross-Device Access**: Provide cross-device read-after-write consistency for users accessing services from multiple devices.
    - Manage metadata centrally to keep track of updates from each device.
    - Route requests from all devices to the same datacenter to read from the leader, accounting for different network paths.

### Monotonic Reads

**Problem Definition**:

- The second anomaly when reading from asynchronous followers is seeing time go backward.
- This can happen when users read sequentially from different replicas. For instance, if a user first reads from a follower with low lag and then from one with high lag, the first query returns recent data, while the second query might return older data, causing confusion.

**Solutions**:

- **Ensuring Monotonic Reads**:
    - To avoid this anomaly, ensure [[Monotonic Reads]], guaranteeing that users do not see time go backward when performing multiple reads. This means users never see older data after seeing newer data.
    - This is a weaker guarantee than strong consistency but stronger than eventual consistency.

**Implementation**:

- **Reading from Consistent Replicas**:
    - Ensure each user always reads from the same replica. For example, select replicas based on a hash of the user ID, ensuring consistent reads.
    - If the selected replica fails, reroute the user's queries to another replica.

### Consistent Prefix Reads

**Problem Definition**:

- The third anomaly due to replication lag is the violation of causality. This occurs when a series of writes happening in a specific order are seen in the same order by readers.

**Example**:

- Conversation between Mr. Poons and Mrs. Cake:
    - Mr. Poons: "How far into the future can you see, Mrs. Cake?"
    - Mrs. Cake: "About ten seconds usually, Mr. Poons."
- If a third person hears this conversation through a follower receiving the messages in a different order, it might sound like this:
    - Mrs. Cake: "About ten seconds usually, Mr. Poons."
    - Mr. Poons: "How far into the future can you see, Mrs. Cake?"

In this case, it appears as though Mrs. Cake is answering Mr. Poons before he even asks the question, causing confusion.

**Solutions**:

- **Consistent Prefix Reads**: Guarantee that if a series of writes occur in a specific order, readers see them in the same order.
- **Problem Context**: This issue is particularly problematic in partitioned (sharded) databases where different partitions operate independently, lacking a global write order.

**Approaches**:

1. **Co-locate Writes to the Same Partition**: Ensure causally related writes are directed to the same partition. However, this can be challenging to perform efficiently for some applications.
2. **Use Causality Tracking Algorithms**: Algorithms that explicitly track causality can be used. This topic is covered in more detail in “The “happens-before” relationship and concurrency”.

### Solutions for Replication Lag

**Considering Increased Replication Lag**:
- Consider how your application will behave if the system experiences replication lag of several minutes or hours.
- If this is not a significant issue, it is acceptable. However, if it negatively impacts user experience, design the system to provide strong guarantees like read-after-write consistency.
- Assuming asynchronous replication behaves like synchronous replication can lead to issues in the long term.

**Providing Strong Guarantees at the Application Level**:
- Providing stronger guarantees than the underlying database by performing specific reads from the leader.
- However, dealing with these issues in application code is complex and error-prone.

**Importance of Database Transactions**:
- Ideally, application developers should rely on the database to "do the right thing" without worrying about subtle replication issues.
- Transactions exist to make databases provide strong guarantees, simplifying application development.

**Single-Node Transactions and Distributed Databases**:
- Single-node transactions have existed for a long time.
- Moving to distributed (replicated and partitioned) databases has led many systems to abandon transactions, arguing that they are too expensive in terms of performance and availability. They claim that eventual consistency is inevitable in scalable systems.
- While there is some truth to this, it is an oversimplification. The remainder of this book will develop a more nuanced perspective.

**Future Topics**:

- We will return to the topic of transactions in Chapters 7 and 9 and discuss alternative mechanisms in Part III.

## Multi-Leader Replication

**Disadvantages of Leader-based Replication**:

- Only a single leader exists, and all write operations must go through that leader.

**Multi-Leader Configuration**:

- Allows multiple nodes to accept write operations.
- Each node processes its write operations and then propagates the data changes to all other nodes.
- In this configuration, each leader simultaneously acts as a follower of other leaders.

**Key Features**:

- **Write Scalability**: Improves scalability by processing write operations on multiple nodes.
- **High Availability**: Continues to handle write operations even if one leader fails.

**Other Names**:

- Multi-Leader Replication is also known as master-master or active-active replication.

### Use Cases for Multi-Leader Replication

#### Multi-Datacenter Operation

- **Purpose**: Reduce latency and enhance data processing performance across data centers while providing fault tolerance.
- **Configuration**: Each data center has a leader, with each leader asynchronously replicating data to leaders in other data centers.
- **Advantages**:
    - **Performance**: Processes write operations locally within the data center, reducing latency.
    - **Fault Tolerance**: Each data center can operate independently and replicate data upon recovery.
    - **Network Issue Tolerance**: Handles temporary network interruptions due to asynchronous replication.
- **Drawbacks**: Requires conflict resolution for simultaneous data modifications.

#### Clients with Offline Operation

- **Purpose**: Ensure applications continue to function without an internet connection.
- **Configuration**: Each device uses a local database as a leader, asynchronously synchronizing data with other leaders.
- **Advantages**: Allows data input and querying while offline.
- **Drawbacks**: Can result in significant asynchronous replication delays, requiring complex synchronization and conflict resolution.

#### Collaborative Editing

- **Purpose**: Enable multiple users to edit documents simultaneously.
- **Configuration**: Each user makes changes on a local replica, asynchronously replicating them to the server and other users.
- **Advantages**: Supports quick collaboration.
- **Drawbacks**: Requires conflict resolution and careful selection of document locking or modification units.

### Handling Write Conflicts

**Problem Definition**:

- The most significant issue in multi-leader replication is write conflicts, occurring when multiple leaders simultaneously modify the same data, requiring conflict resolution.

**Conflict Detection Methods**:

- **Synchronous Detection**: Detect conflicts before confirming writes to all replicas, negating the main advantages of multi-leader replication.
- **Asynchronous Detection**: Detect conflicts after confirming writes, which may be challenging for user intervention.

**Conflict Avoidance**:

- Route all writes to a specific leader to avoid conflicts, such as directing requests to a single data center for user data modifications.
- This approach can break down during data center failures or changes in user locations.

**Converging to a Consistent State**:

- Resolve conflicts to ensure all replicas have the same final value.
- **Conflict Resolution Methods**:
    - **Using Unique IDs**: Assign unique IDs to each write and prioritize the highest ID (e.g., LWW - Last Write Wins).
    - **Using Replica IDs**: Prioritize writes from replicas with higher IDs.
    - **Value Merging**: Merge values to converge to a consistent state (e.g., alphabetically sorted).
    - **Explicit Conflict Recording**: Record conflicts explicitly and resolve them later in application code.

**Custom Conflict Resolution Logic**:

- Application code can implement custom conflict resolution logic.
- **Resolve on Write**: Detect and resolve conflicts immediately (e.g., Bucardo).
- **Resolve on Read**: Save all versions on conflict detection and resolve them upon the next read (e.g., CouchDB).

**Automatic Conflict Resolution**:

- **CRDTs (Conflict-free Replicated Data Types)**: Data structures that automatically resolve conflicts (e.g., Riak 2.0).
- **Mergeable Persistent Data Structures**: Use 3-way merges similar to Git version control.
- **Operational Transformation**: Used in real-time collaborative editing applications (e.g., Etherpad, Google Docs).

**Defining Conflicts**:

- **Obvious Conflicts**: Simultaneously modifying the same field of the same record to different values.
- **Subtle Conflicts**: For example, in a meeting room reservation system, booking the same room for the same time slot by two groups.

### Multi-Leader Replication Topologies

**Replication Topology**:

- Describes the communication paths through which write operations propagate between nodes.
- With two leaders, each leader must send its write operations to the other.

**Examples of Different Topologies**:

1. **All-to-All**:
    - All leaders send their write operations to all other leaders.
    - The most common topology, but network speed differences can cause message order discrepancies.
2. **Circular**:
    - Each node receives write operations from one node and forwards them to another.
    - Supported natively in MySQL.
3. **Star**:
    - A central root node sends write operations to all other nodes.
    - The star topology can be generalized to a tree structure.

**Pros and Cons of Topologies**:

- **Circular and Star**:
    - A single node failure can disrupt replication message flow between other nodes.
    - Manual reconfiguration is needed to handle failed nodes.
- **All-to-All**:
    - Avoids single points of failure by sending messages through various paths.
    - Network speed differences can cause replication messages to arrive in different orders.

**Issues and Solutions**:

- **Causality Problem**:
    - If client A inserts a row in leader 1 and client B updates that row in leader 3, leader 2 may receive messages in a different order.
    - Techniques like version vectors can address this issue.
- **Importance of Conflict Detection**:
    - Many multi-leader replication systems do not adequately implement conflict detection.
    - PostgreSQL BDR and MySQL's Tungsten Replicator do not provide causal ordering.

**Considerations**:

- When using systems with multi-leader replication, be aware of these issues, read documentation carefully, and thoroughly test the database's guarantees.

## Leaderless Replication

**Concept**:

- **Without a Leader**, all replicas directly accept writes from clients.
- Some early data storage systems used leaderless structures, but they were almost forgotten during the relational database dominance period.
- Amazon's Dynamo system revived this structure, adopted by open-source datastores like Riak, Cassandra, and Voldemort.

**Operation**:

- Clients send writes directly to multiple replicas or through a coordinator node.
- Unlike leader-based databases, the coordinator does not enforce a specific write order.

**Outcome**:

- This design difference significantly impacts how databases are used.

### Writing to the Database When a Node Is Down

**Handling Write Operations When Nodes Are Down in Leaderless Replication**:

- Clients send writes to multiple replicas in parallel, and the available replicas accept them. Missed writes are later recovered.

**Read Repair and Anti-Entropy**:

- **Read Repair**: Detects missing data by reading from multiple nodes and writes the latest data to replicas.
- **Anti-Entropy**: A background process continuously finds and copies differences between replicas.

**Quorum Reads and Writes**:

- Of n replicas, w nodes must confirm a write, and r nodes must be read to secure the latest data.
- Common configuration: n=3, w=2, r=2.
- Ensures data consistency if w + r > n.

**Node Availability**:

- If w < n, writes are possible even if some nodes are unavailable.
- If r < n, reads are possible even if some nodes are unavailable.

### Limitations of Quorum Consistency

**Quorum Reads and Writes**:

- **Condition**: Ensures the latest value is read if w + r > n. This guarantees at least one node overlaps between writes and reads.
- **Common Setup**: n is odd, w and r are greater than n/2.
- **Flexibility**: Smaller r and w lower consistency but increase latency and availability.

**Exceptions**:

- Sloppy quorum usage may not guarantee conditions.
- Concurrent writes and simultaneous read/write operations may cause issues.
- Data consistency issues arise if some replicas fail to write.

**Operational Perspective**:

- Monitoring replica freshness is necessary.
- Difficult to monitor staleness in leaderless replication systems.
- Researching ways to measure replica staleness.

Stronger consistency may require transactions or consensus.

### Sloppy Quorums and Hinted Handoff

**Quorum Benefits**:

- **High Availability**: Writes and reads are possible even if some nodes fail.
- **Low Latency**: Only need responses from w or r nodes, not all nodes.

**Issues**:

- Network interruptions can make some nodes unreachable, preventing quorum formation, leading to read/write failures.

**Sloppy Quorum**:

- Allows writes/reads even if w and r nodes are not home nodes.
- Temporary storage during network issues is sent to home nodes after resolution (hinted handoff).

**Multi-Datacenter Operations**:

- Send writes to all replicas, waiting for a quorum in the local datacenter.
- Riak performs asynchronous cross-datacenter replication similar to multi-leader replication.

### Detecting Concurrent Writes

Dynamo-style databases allow multiple clients to write to the same key concurrently, causing conflicts due to network delays and partial failures. This situation is similar to conflicts in multi-leader replication, which may arise during read repair or hinted handoff. The challenge is ensuring replicas converge to the same value despite these conflicts.

**Last Write Wins (LWW)**

- **Approach**: Only the most recent value is stored, with previous values discarded.
- **Method**: Assign timestamps to each write and retain the value with the latest timestamp.
- **Drawback**: Concurrent writes overwrite and delete data, leading to data loss. Safe only if keys are written once and never changed.

**"Happens-before" Relationship**

- **Definition**: Operation A happens before operation B if B knows about or depends on A.
- **Concurrency**: Two operations are considered concurrent if they do not know about each other.

**Capturing the Happens-before Relationship**

- **Algorithm**:
    - Servers maintain version numbers for each key and increment them on each write.
    - Clients read keys, receiving current values and version numbers.
    - Clients merge values and write back, including version numbers from the previous read.
    - Servers overwrite lower versioned values but retain concurrent values.

**Merging Concurrent Values**

- **Approach**: Clients must merge concurrent values, known as "siblings."
- **Example**: For a shopping cart, merge items to create a union. If items can be

 deleted, a "tombstone" marker is necessary.
- **Issue**: Merging in application code is complex and error-prone.

**Version Vectors**

- **Multiple Replicas**: Each replica increases its version number and tracks others' version numbers.
- **Version Vector**: A collection of version numbers for all replicas, distinguishing overwrites from concurrent writes.
- **Safe Operation**: Reading from one replica and writing to another without data loss, creating siblings to merge.

In conclusion, handling concurrent writes in Dynamo-style databases involves determining operation concurrency, managing multiple replicas with version vectors, and merging concurrent values to ensure eventual consistency. The last write wins method is simple but can result in data loss, while version vectors and appropriate merging techniques maintain data integrity.

## Summary

This chapter dealt with the issues of replication. Replication can serve multiple purposes:

- **High Availability**: Keep the system running even if one machine (or several machines or an entire datacenter) goes down.
- **Disconnected Operation**: Ensure applications continue to work even if the network is interrupted.
- **Latency Reduction**: Place data geographically closer to users to allow faster data access.
- **Scalability**: Handle read volumes beyond the capacity of a single machine by performing reads on multiple replicas.

Despite the simple goal of replicating the same data across multiple machines, replication has proven to be a challenging problem. It requires careful consideration of concurrency issues and handling potential problems that may arise, as well as dealing with the consequences of these errors. At a minimum, replication must handle unavailable nodes and network interruptions (not to mention more subtle errors like silent data corruption due to software bugs).

We discussed three main approaches to replication:

- **Single Leader Replication**: Clients send all writes to a single node (the leader), which streams data change events to other replicas (followers). Reads can be performed on any replica, but reads from followers might not be up-to-date.
- **Multi-Leader Replication**: Clients send writes to any of several leader nodes, which propagate data change events to each other and all follower nodes.
- **Leaderless Replication**: Clients send writes to multiple nodes directly and read in parallel from multiple nodes to detect and fix outdated nodes.

Each approach has its pros and cons. Single leader replication is popular due to its simplicity and lack of conflict resolution concerns. Multi-leader and leaderless replication can be more robust to node failures, network interruptions, and latency spikes, but they are harder to understand and provide weaker consistency guarantees.

Replication can be synchronous or asynchronous, significantly impacting system behavior during failures. Asynchronous replication allows the system to continue smoothly under normal conditions but necessitates understanding what happens when replication lag increases and servers fail. Promoting a follower to a new leader in asynchronous replication can lead to data loss if the original leader fails.

We explored the anomalies that can occur due to replication lag and discussed useful consistency models to determine how applications should behave under these conditions:

- **Read-After-Write Consistency**: Users should always see their submitted data.
- **Monotonic Reads**: Users should not see time going backward when performing multiple reads.
- **Consistent Prefix Reads**: Users should see data that respects causality, such as questions and their answers in the correct order.

Finally, we discussed concurrency issues inherent in multi-leader and leaderless replication approaches. These methods can lead to conflicts when multiple writes occur simultaneously. We explored algorithms to determine whether one operation happened before another or if they occurred concurrently. We also discussed merging concurrent updates to resolve conflicts.

This translation retains the detailed explanations and technical nuances of the original text, making it suitable for someone with expertise in database replication and distributed systems.