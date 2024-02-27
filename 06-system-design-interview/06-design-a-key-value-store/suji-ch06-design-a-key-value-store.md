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

</aside>

- Store only frequently used data in-memory. The rest store on disk.

### **Distributed key-value store**

Distributes keyvalue pairs across many servers. 

When designing a distributed system, it is important to understand CAP (Consistency, Availability, Partition Tolerance) theorem

### **CAP Theorem**

This theorem states that **a data store can't provide more than two of the following guarantees - consistency, availability, partition tolerance;**

- Consistency - all clients see the same data at the same time, no matter which node they're connected to.
- Availability - all clients get a response, regardless of which node they connect to.
- Partition tolerance - A network partition means that not all nodes within the cluster can communicate. Partition tolerance means that the system is operational even in such circumstances.

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

- In a distributed system, partitions cannot be avoided, and when a partition occurs, we must
choose between consistency and availability
    - CP OR AP

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/network-partition-example.png

**CP System Scenario**

- If we choose **consistency** over availability (CP system), we must block all write operations to
n1 and n2 to avoid data inconsistency among these three servers, which makes the system
unavailable.
- Example usages:
    - bank systems: usually have extremely high consistent requirements.

**AP System Scenario**

- If we choose **availability** over consistency (AP system), the system keeps accepting
reads, even though it might return stale data. For writes, n1 and n2 will keep accepting writes,
and data will be synced to n3 when the network partition is resolved.

<aside>
💡 stale data? 
"Stale data" refers to information or data that has become outdated or no longer relevant due to the passage of time or changes in circumstances.

</aside>

### **System components**

In this section, we will discuss the following core components and techniques used to build a
key-value store:

- Data partition
- Data replication
- Consistency
- Inconsistency resolution
- Handling failures
- System architecture diagram
- Write path
- Read path

### **Data partition**

For large applications, it is infeasible to fit the complete data set in a single server. The
simplest way to accomplish this is to split the data into smaller partitions and store them in
multiple servers. There are two challenges while partitioning the data:

- Distribute data across multiple servers evenly.
- Minimize data movement when nodes are added or removed.

Consistent hashing discussed in Chapter 5 is a great technique to solve these problems. 

**Consistent hashing**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/consistent-hashing.png

This has the following advantages:

- Automatic scaling: servers could be added and removed automatically depending on the load.
- Heterogeneity: the number of virtual nodes for a server is proportional to the server capacity.
For example, servers with higher capacity are assigned with more virtual nodes

### **Data replication**

To achieve high availability and reliability, data must be replicated asynchronously over N
servers, where N is a configurable parameter.

**Consistent hash ring with virtual nodes** 

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/data-replication.png

With virtual nodes, the first N nodes on the ring may be owned by fewer than N physical
servers. To avoid this issue, we only choose unique servers while performing the clockwise
walk logic.
Nodes in the same data center often fail at the same time due to power outages, network
issues, natural disasters, etc. For better reliability, replicas are placed in distinct data centers,
and data centers are connected through high-speed networks

### **Consistency**

Since data is replicated, it must be synchronized.

Quorum consensus can guarantee consistency for both reads and writes:

- N = The number of replicas
- W = A write quorum of size W. For a write operation to be considered as successful, write
operation must be acknowledged from W replicas.
- R = A read quorum of size R. For a read operation to be considered as successful, read
operation must wait for responses from at least R replicas

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/write-quorum-example.png

### **Consistency models**

### **Inconsistency resolution: versioning**

Replication provides high availability, but it leads to data inconsistencies across replicas.

Example inconsistency:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/inconsistency-example.png

Example inconsistency resolution:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/inconsistency-resolution.png

- 

### **Handling failures**

### **Failure detection**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/multicasting.png

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/gossip-protocol.png

### **Handling temporary failures**

### **Handling permanent failures**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/merkle-tree.png

### **Handling data center outage**

### **System architecture diagram**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/architecture-diagram.png

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/node-responsibilities.png

**Write path**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/write-pth.png

### **Read path**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter07/images/read-path-in-memory.png

Read path when data is not in memory: