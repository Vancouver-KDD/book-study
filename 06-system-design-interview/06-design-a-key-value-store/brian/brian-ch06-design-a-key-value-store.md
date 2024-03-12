# CHAPTER 6: DESIGN A KEY-VALUE STORE
> Examples: Amazon dynamo, Memcached, Redis, etc.

## Understand the problem and establish design scope
- The size of a key-value pair is small: less than 10 KB.
- Ability to store big data.
- High availability: The system responds quickly, even during failures.
- High scalability: The system can be scaled to support large data set.
- Automatic scaling: The addition/deletion of servers should be automatic based on traffic.
- Tunable consistency.
- Low latency.

### Single server key-value store (in memory)
- Data compression
- Store only frequently used data in memory and the rest on disk 

### Distributed key-value store 
#### CAP theorem
- Consistency: consistency means all clients see the same data at the same time no matter which node they connect to. 
- Availability: availability means any client which requests data gets a response even if some of the nodes are down. 
- Partition Tolerance: a partition indicates a communication break between two nodes. Partition tolerance means the system continues to operate despite network partitions. 

#### CP VS AP VS CA
- CP (consistency and partition tolerance) systems: a CP key-value store supports consistency and partition tolerance while sacrificing availability. 
- AP (availability and partition tolerance) systems: an AP key-value store supports availability and partition tolerance while sacrificing consistency.
- CA (consistency and availability) systems: a CA key-value store supports consistency and availability while sacrificing partition tolerance. 
  - Since network failure is unavoidable, a distributed system must tolerate network partition. 
  - Thus, a CA system **cannot exist in real- world applications**.

#### Ideal situation VS Real-world distributed systems 
- We must choose between consistency and availability.
- CP system(Consistency > Availability) like Bank systems
  - A server goes down -> block all write operations
- AP system(Consistency < Availability) like SNS systems
  - A server goes down -> it might return stale data

### System components
- Data partition -> Consistent Hashing -> Automatic Scaling + Heterogeneity
  - Distribute data across multiple servers evenly.
  - Minimize data movement when nodes are added or removed.
- Data replication
- Consistency
  - Quorum consensus
    - N = The number of replicas 
    - W = A write quorum of size W. 
      - For a write operation to be considered as successful, write operation must be acknowledged from W replicas. 
    - R = A read quorum of size R. 
      - For a read operation to be considered as successful, read operation must wait for responses from at least R replicas.
  - Strength of Consistency = Case1 < Case2 < Case3
    - Case1: If W = 1 or R = 1
    - Case2: If W or R > 1
    - Case3: If W + R > N
  - Consistency models
    - Strong consistency: any read operation returns a value corresponding to the result of the most updated write data item. A client never sees out-of-date data.
    - Weak consistency: subsequent read operations may not see the most updated value.
    - Eventual consistency: this is a specific form of weak consistency. Given enough time, all updates are propagated, and all replicas are consistent.
- Inconsistency resolution: versioning
  - To resolve this issue, we need a versioning system that can detect conflicts and reconcile conflicts.
  - Ex)
    - D([s0, 1], [s1, 1])] -> D([s0, 1], [s1, 2])] = No Conflict
    - D([s0, 1], [s1, 2])] -> D([s0, 2], [s1, 1])] = Conflict
  - Downside
    1. Complexity Up -> Conflict Resolution Logic
    2. Vector clock data could grow rapidly 

- Handling failures
  1. Failure Detection -> Gossip protocol
     - A node notice a node is down.
     - Let random nodes know and verify the down node in their side.
     - If it is correct, this information propagated to other nodes.
  2. Handling temporary failures
     - Sloppy quorum: Offline servers are ignored. -> improve availability
     - Hinted hadnoff: action to push back not updated data when down server is up 
  3. Handling permanent failures
    - Merkle tree: detect unsynchronized data
  4. Handling data center outage
    - Replicate data across multiple data centers 
- System architecture diagram
  - Clients communicate with the key-value store through simple APIs: get(key) and put(key, value).
  - A coordinator is a node that acts as a proxy between the client and the key-value store.
  - Nodes are distributed on a ring using consistent hashing.
  - The system is completely decentralized so adding and moving nodes can be automatic.
  - Data is replicated at multiple nodes.
  - There is no single point of failure as every node has the same set of responsibilities.
- Write path
  - Client -> DISK: Commit log -> Memory cache -> DISK: SSTables
- Read path
  1. Client -> Memory cache -> Client
  2. Client -> Memory cache -> Bloom filter -> SSTables -> Client