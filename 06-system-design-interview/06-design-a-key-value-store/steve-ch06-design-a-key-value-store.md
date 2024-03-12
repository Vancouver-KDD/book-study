# Chapter 6: Design Design a Key-Value Store

- put(key, value) // insert “value” associated with “key”
- get(key) // get “value” associated with “key”

## Understand the problem and establish design scope

- The size of a key-value pair is small: less than 10 KB.
- Ability to store big data.
- High availability: The system responds quickly, even during failures.
- High scalability: The system can be scaled to support large data set.
- Automatic scaling: The addition/deletion of servers should be automatic based on traffic.
- Tunable consistency.
- Low latency.

## Single server key-value store
- Data compression
- Store only frequently used data in memory and the rest on disk

## Distributed key-value store
### CAP (Consistency, Availability, Partition Tolerance) theorem

 - CAP theorem states it is impossible for a distributed system to simultaneously provide more than two of these three guarantees: consistency, availability, and partition tolerance. Let us establish a few definitions.

- Consistency: consistency means all clients see the same data at the same time no matter
which node they connect to.
- Availability: availability means any client which requests data gets a response even if some
of the nodes are down.
- Partition Tolerance: a partition indicates a communication break between two nodes.
Partition tolerance means the system continues to operate despite network partitions.

### Ideal situation
In the ideal world, network partition never occurs. Data written to n1 is automatically
replicated to n2 and n3. Both consistency and availability are achieved.

### Real-world distributed systems
In a distributed system, partitions cannot be avoided, and when a partition occurs, we must
choose between consistency and availability.

## System components

- Data partition
- Data replication
- Consistency
- Inconsistency resolution
- Handling failures
- System architecture diagram
- Write path
- Read path

### Data partition
- Distribute data across multiple servers evenly.
- Minimize data movement when nodes are added or removed.

### Data replication
To achieve high availability and reliability, data must be replicated asynchronously over N
servers, where N is a configurable parameter.

### Consistency
- N = The number of replicas
- W = A write quorum of size W. For a write operation to be considered as successful, write operation must be acknowledged from W replicas.
- R = A read quorum of size R. For a read operation to be considered as successful, read operation must wait for responses from at least R replicas.

### Consistency models
- Strong consistency: any read operation returns a value corresponding to the result of the
most updated write data item. A client never sees out-of-date data.
- Weak consistency: subsequent read operations may not see the most updated value.
- Eventual consistency: this is a specific form of weak consistency. Given enough time, all
updates are propagated, and all replicas are consistent.

### Inconsistency resolution: versioning
Replication gives high availability but causes inconsistencies among replicas. Versioning and
vector locks are used to solve inconsistency problems. Versioning means treating each data
modification as a new immutable version of data.

### Handling failures
#### Failure detection
In a distributed system, it is insufficient to believe that a server is down because another
server says so. Usually, it requires at least two independent sources of information to mark a
server down.

#### Handling temporary failures
After failures have been detected through the gossip protocol, the system needs to deploy
certain mechanisms to ensure availability. In the strict quorum approach, read and write
operations could be blocked as illustrated in the quorum consensus section.

#### Handling data center outage
Data center outage could happen due to power outage, network outage, natural disaster, etc.
To build a system capable of handling data center outage, it is important to replicate data
across multiple data centers. Even if a data center is completely offline, users can still access
data through the other data centers.

#### System architecture diagram
- Clients communicate with the key-value store through simple APIs: get(key) and put(key,
value).
- A coordinator is a node that acts as a proxy between the client and the key-value store.
- Nodes are distributed on a ring using consistent hashing.
- The system is completely decentralized so adding and moving nodes can be automatic.
- Data is replicated at multiple nodes.
- There is no single point of failure as every node has the same set of responsibilities.

#### Write path
1. The write request is persisted on a commit log file.
2. Data is saved in the memory cache.
3. When the memory cache is full or reaches a predefined threshold, data is flushed to
SSTable on disk. Note: A sorted-string table (SSTable) is a sorted list of <key, value>
pairs. For readers interested in learning more about SStable, refer to the reference material.

#### Read path
1. The system first checks if data is in memory. If not, go to step 2.
2. If data is not in memory, the system checks the bloom filter.
3. The bloom filter is used to figure out which SSTables might contain the key.
4. SSTables return the result of the data set.
5. The result of the data set is returned to the client.