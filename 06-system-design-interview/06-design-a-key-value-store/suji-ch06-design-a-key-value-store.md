# CH6. Design a Key-Value Store

### **Design a Key-Value Store**

Key-value store is a type of non-relational databases.

- Unique identifier(Key)
    - Keys must be unique and can be plain text or hashes.
    - Performance-wise, short keys better.

Example of key-value store:

| Key | value |
| --- | --- |
| 145 | john |
| 147 | bob |
| 160 | julia |

We're now about to design a key-value store which supports:

- `put(key, value)` - insert `value` associated to `key`
- `get(key)` - get `value` associated to `key`

### **Understand the problem and establish design scope**

Trade-offs:

- read, write, and memory usage.
- consistency and availability.

In this chapter, we design a key-value store that comprises of the following
characteristics:

- Key-value pair size is small - 10kb
- We need to be able to store a lot of data.
- High availability - system responds quickly even during failures.
- High scalability - system can be scaled to support large data sets.
- Automatic scaling - addition/deletion of servers should happen automatically based on traffic.
- Tunable consistency.
- Low latency.

### **Single server key-value store**

Developing a key-value store that resides in a single server is easy. A single server can reach its capacity very quickly even with optimizations, but we could try: 

- Data compression

<aside>
💡 what is data compression? 
Data compression in a single server reduces data size for storage efficiency. It uses techniques like lossless (e.g., RLE, Huffman) or lossy (e.g., JPEG, MP3) compression, file system-level compression, database-level compression, index compression, and memory compression.

</aside>

- Store only frequently used data in-memory. The rest store on disk.

# **Distributed key-value store**

Distributes key value pairs across many servers. 

When designing a distributed system, it is important to understand CAP (Consistency, Availability, Partition Tolerance) theorem

### **CAP Theorem**

This theorem states that **a data store can't provide more than two of the following guarantees - consistency, availability, partition tolerance;**

- Consistency - all clients see the same data at the same time, no matter which node they're connected to.
- Availability - all clients get a response, regardless of which node they connect to.
- Partition tolerance - A network partition means that not all nodes within the cluster can communicate. Partition tolerance means that the system is operational even in such circumstances.

<aside>
💡 Partition tolerance

Partition tolerance in the CAP theorem refers to the system's ability to continue operating despite **network partitions** or communication failures between nodes in a distributed system.

Network partitions
Network partitions occur when communication between nodes in a distributed system is disrupted, leading to the isolation of some nodes from others. 

</aside>

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/cap-theorem.png

<aside>
💡 what is partition tolerance?

</aside>

- CP (consistency and partition tolerance) systems: a CP key-value store supports
consistency and partition tolerance while sacrificing availability.
- AP (availability and partition tolerance) systems: an AP key-value store supports
availability and partition tolerance while sacrificing consistency.
- CA (consistency and availability) systems: a CA key-value store supports consistency and availability while sacrificing partition tolerance. ; Since network failure is unavoidable, a distributed system must tolerate network partition. Thus, a **CA system cannot exist in real world applications**

**Distributed system Ideal situation**

- Network partition never occurs. Data written to n1 is automatically replicated to n2 and n3. Both **consistency and availability are achieved.**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/example-distributed-system.png

**Distributed system in real world**

- In a distributed system, partitions cannot be avoided, and when a partition occurs, we must choose between consistency and availability
    - CP OR AP

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/network-partition-example.png

**CP System Scenario**

- If we choose **consistency** over availability (CP system), we must block all write operations to n1 and n2 to avoid data inconsistency among these three servers, which makes the system unavailable.
- Example usages:
    - bank systems: usually have extremely high consistent requirements.

**AP System Scenario**

- If we choose **availability** over consistency (AP system), the system keeps accepting reads, even though it might return stale data. For writes, n1 and n2 will keep accepting writes, and data will be synced to n3 when the network partition is resolved.

<aside>
💡 stale data? 
"Stale data" refers to information or data that has become **outdated or no longer relevant** due to the passage of time or changes in circumstances.

</aside>

### **System components**

In this section, we will discuss the following core components and techniques used to build a key-value store:

- Data partition
- Data replication
- Consistency
- Inconsistency resolution
- Handling failures
- System architecture diagram
- Write path
- Read path

### **Data partition**

For large applications, it is infeasible to fit the complete data set in a single server. The simplest way to accomplish this is to split the data into smaller partitions and store them in multiple servers. There are two challenges while partitioning the data:

- Distribute data across multiple servers evenly.
- Minimize data movement when nodes are added or removed.

**Consistent hashing** discussed in Chapter 5 is a great technique to solve these problems. 

**Consistent hashing**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/consistent-hashing.png

This has the following advantages:

- Automatic scaling: servers could be added and removed automatically depending on the load.
- Heterogeneity: the number of virtual nodes for a server is proportional to the server capacity. For example, servers with higher capacity are assigned with more virtual nodes

### **Data replication**

To achieve high availability and reliability, data must be replicated asynchronously over N servers, where N is a configurable parameter.

**Consistent hash ring with virtual nodes** 

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/data-replication.png

With virtual nodes, the first N nodes on the ring may be owned by fewer than N physical servers. To avoid this issue, we only choose unique servers while performing the clockwise walk logic.
Nodes in the same data center often fail at the same time due to power outages, network issues, natural disasters, etc. For better reliability, replicas are placed in distinct data centers, and data centers are connected through high-speed networks

### **Consistency**

Since data is replicated, it must be synchronized.

Quorum consensus can guarantee consistency for both reads and writes:

- N = The number of replicas
- W = A write quorum of size W. For a write operation to be considered as successful, write operation must be acknowledged from W replicas.

<aside>
💡 write quorum?
A write quorum in distributed systems is the **minimum number of replicas or nodes that must acknowledge a write operation** before it's considered successful.

</aside>

- R = A read quorum of size R. For a read operation to be considered as successful, read operation must wait for responses from at least R replicas

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/write-quorum-example.png

- Data is replicated at *s0*, *s1,* and *s2*. *W = 1* means that the coordinator must receive at least one acknowledgment before the write operation is considered as successful.
- The configuration of ***W, R* and *N* is a typical tradeoff between latency and consistency.**
- How to configure *N, W*, and *R* to fit our use cases? Here are some of the possible setups:
    - If *R = 1* and *W = N*, the system is optimized for a **fast read.**
    - If *W = 1 and R = N*, the system is optimized for **fast write.**
    - If *W + R > N*, strong consistency is guaranteed (Usually *N = 3, W = R = 2*).
    - If *W + R <= N*, strong consistency is not guaranteed.

Depending on the requirement, we can tune the values of *W, R, N* to achieve the desired level of consistency.

### **Consistency models**

- Strong consistency: any read operation returns a value corresponding to the result of the most updated write data item. **A client never sees out-of-date data.**
- Weak consistency: subsequent read operations may not see the most updated value.
- Eventual consistency: this is a specific form of weak consistency. Given enough time, all updates are propagated, and all replicas are consistent. ; **Dynamo and Cassandra adopt eventual consistency, which is our recommended consistency model for our key-value store.**

### **Inconsistency resolution:** versioning

Replication provides high availability, but it leads to data inconsistencies across replicas.

Example inconsistency:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/inconsistency-example.png

**Example inconsistency resolution: vector clocks**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/inconsistency-resolution.png

- 

### **Handling failures**

- Usually, it requires at least two independent sources of information to mark a server down.

**Methods**

- all-to-all multicasting is a straightforward solution. However, this is inefficient when many servers are in the system.
- A better solution is to use decentralized failure detection methods like **gossip protocol.**

### **Failure detection w/ gossip protocol**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/gossip-protocol.png

Heartbeat counter increments periodically to signal activity. If a node's counter doesn't increase for a set time, it's considered offline. This helps detect failures in decentralized systems like the gossip protocol.

> - Node *s0* maintains a node membership list shown on the left side.
- Node *s0* notices that node s2’s (member ID = 2) heartbeat counter has not increased for a long time.
- Node *s0* sends heartbeats that include *s2*’s info to a set of random nodes. **Once other nodes confirm that *s2*’s heartbeat counter has not been updated for a long time, node *s2* is marked down, and this information is propagated to other nodes.**
> 

### **Handling temporary failures**

A technique called **“sloppy quorum”** [4] is used to improve availability. Instead of enforcing the quorum requirement, the system **chooses the first *W* healthy servers for writes and first *R* healthy servers for reads on the hash ring.** Offline servers are ignored.

<aside>
💡 "Sloppy" typically means careless, untidy, or lacking in precision.

</aside>

### **Handling permanent failures**

- what if permanently unavailable?
    - A Merkle tree is used for inconsistency detection and minimizing the amount of data transferred.
        - We use Merkle trees for inconsistency detection, reducing data transfer. To compare Merkle trees, start from the roots. If they match, data is the same; if not, compare child hashes to find differences. This way, we synchronize only the differing parts

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/merkle-tree.png

### **Handling data center outage**

To ensure continuous access, replicate data across multiple data centers. If one center is offline due to power, network, or natural disasters, users can still access data from others.

### **System architecture diagram**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/architecture-diagram.png

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/node-responsibilities.png

1. The write request is persisted on a commit log file. 2. Data is saved in the memory cache.
2. When the memory cache is full or reaches a predefined threshold, data is flushed to SSTable [9] on disk. Note: A sorted-string table (SSTable) is a sorted list of <key, value> pairs. For readers interested in learning more about SStable, refer to the reference material [9].

**Write path**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/write-pth.png

### **Read path**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/read-path-in-memory.png

Checks if data is in the memory cache.

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/read-path-not-in-memory.png

If the data is not in memory, it will be retrieved from the disk instead.
