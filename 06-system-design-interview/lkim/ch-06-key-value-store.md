key-value store, also referred to as a key-value database, is a non-relational database
- Amazon dynamo, Memcached, Redis

Given conditions
- The size of a key-value pair is small: less than 10 KB.
- Ability to store big data.
- High availability: The system responds quickly, even during failures.
- High scalability: The system can be scaled to support large data set.
- Automatic scaling: The addition/deletion of servers should be automatic based on traffic.
- Tunable consistency.
- Low latency.

## Single server key-value store
To fit more data in a single server:
- Data compression
- Store only frequently used data in memory and the rest on disk
  - memory access is fast, but fitting everything in memory may be impossible due to the space constraint.
  - even with the optimizations, a single server would the capacity quick
--> A distributed key-value store

## Distributed key-value store
= distributed hash table, which distributes key-value pairs across many servers.

### CAP theorem
A distributed system cannot provide more than two of these three guarantees: consistency, availability, and partition tolerance. 
**- Consistency**: all clients see the same data at the same time no matter which node they connect to.
**- Availability**: any client which requests data gets a response even if some of the nodes are down.
**- Partition Tolerance**: the system continues to operate despite network partitions (communication break).

### Real-world distributed system
- Since network failure is unavoidable, partition tolerance can't be sacrificed
- So need to choose between consistency and availability

#### CP system
Block all write operations to the server down. 
- Bank system chooses consistency over availability - frequent error messages

#### AP system
the system keeps accepting reads to the server down, even though it might return stale data.

## System components to discuss
• Data partition
• Data replication
• Consistency
• Inconsistency resolution
• Handling failures
• System architecture diagram
• Write path

### Data partition
- For large applications, it should split the data into smaller partitions. 
**Two challenges**
- Distribute data across multiple servers evenly.
- Minimize data movement when nodes are added or removed.

--> Consistent hashing is the solution! 
  - automatic scaling, Heterogeneity
    - the number of virtual nodes for a server is proportional to the server capacity.

### Data replication
- To achieve high availability and reliability, data must be replicated asynchronously over N servers, where N is a configurable parameter.
- Nodes in the same data center often fail at the same time due to power outages, network issues, natural disasters, etc.
- For better reliability, replicas are placed in distinct data centers, and data centers are connected through high-speed networks.
- Replication gives high availability but causes inconsistencies among replicas. 

### Consistency
**Quorum consensus**: guarantee consistency for both read and write operations. 
- If** W + R > N, **strong consistency is guaranteed because there must be at least one
  - N = The number of replicas
  - W = A write quorum of size W. For a write operation to be considered as successful, write operation must be acknowledged from W replicas.
  - R = A read quorum of size R. For a read operation to be considered as successful, read operation must wait for responses from at least R replicas.

- If R = 1 and W = N, the system is optimized for a fast read.
- If W = 1 and R = N, the system is optimized for fast write.
- If W + R <= N, strong consistency is not guaranteed.

#### Consistency models
- Strong consistency: any read operation returns a value corresponding to the result of the most updated write data item. A client never sees out-of-date data.
  - achieved by forcing a replica not to accept new reads/writes until every replica has agreed on current write.
  - Not ideal since it blocks availability

- Weak consistency: subsequent read operations may not see the most updated value.
- **Eventual consistency:** Given enough time, all updates are propagated, and all replicas are consistent.
  - Dynamo and Cassandra

### Inconsistency resolution
- Versioning and vector locks are used to solve inconsistency problems.
    - Versioning: treating each data modification as a new immutable version of data.

**A vector clock**: a [server, version] pair associated with a data item
- Downsides
    - First, vector clocks add complexity to the client because it needs to implement conflict resolution logic.
    - Second, the [server: version] pairs in the vector clock could grow rapidly. So if it exceeds the limit, the oldest pairs are removed. No problem found in the real applications like DynamoDB

### Handling failures
#### How to detect failures
- it requires at least two independent sources of information to mark a server down.
- use decentralized failure detection methods like gossip protocol.

#### Handling temporary failures
**sloppy quorum**: to improve availability, rather than the strict quorum
- the system chooses the first W healthy servers for writes and first R healthy servers for reads on the hash ring. Offline servers are ignored.
  - If a server is unavailable due to network or server failures, another server will process requests temporarily.
**- hinted handoff:** When the down server is up, changes will be pushed back to achieve data consistency.

#### Handling permanent failures
**anti-entropy protocol** to keep replicas in sync.
- involves comparing each piece of data on replicas and updating each replica to the newest version.
- A Merkle tree is used for inconsistency detection and minimizing the amount of data transferred.
  - Using Merkle trees, the amount of data needed to be synchronized is proportional to the differences between the two replicas, and not the amount of data they contain.
  

### System architecture diagram
![Screenshot 2024-11-11 at 7 30 24 AM](https://github.com/user-attachments/assets/3af1d922-6e83-4c70-8166-329ef488dee2)
- Clients communicate with the key-value store through simple APIs: get(key) and put(key,
value).
- A coordinator is a node that acts as a proxy between the client and the key-value store. • Nodes are distributed on a ring using consistent hashing.
- The system is completely decentralized so adding and moving nodes can be automatic. • Data is replicated at multiple nodes.
- There is no single point of failure as every node has the same set of responsibilities.
- Each node performs tasks as follows with decentralization
![Screenshot 2024-11-11 at 7 31 49 AM](https://github.com/user-attachments/assets/d3740ade-ceb7-45ef-ac92-979034dfce19)

### Write path - Cassandra
![Screenshot 2024-11-11 at 7 34 57 AM](https://github.com/user-attachments/assets/1ea2ad87-8175-4e1e-8b9a-9f31789ad050)
1. The write request is persisted on a commit log file.
2. Data is saved in the memory cache.
3. When the memory cache is full or reaches a predefined threshold, data is flushed to SSTable on disk.
   - A sorted-string table (SSTable) is a sorted list of <key, value> pairs.

### Read path
- it first checks if data is in the memory cache, otherwise fetches from the disk in the SSTables


## Summary
| Goal/Problems                        | Technique                                                    |
|--------------------------------------|--------------------------------------------------------------|
| Ability to store big data            | Use consistent hashing to spread the load across the servers |
| High availability reads              | Data replication, Multi-data center setup                    |
| Highly available writes              | Versioning and conflict resolution with vector clocks        |
| Dataset partition                    | Consistent hashing                                           |
| Incremental scalability              | Consistent hashing                                           |
| Heterogeneity                        | Consistent hashing                                           |
| Tunable consistency                  | Quorum consensus                                             |
| Handling temporary failures          | Sloppy quorum and hinted handoff                             |
| Handling permanent failures          | Merkle tree                                                  |
| Handling data center outage          | Cross-data center replication                                |
