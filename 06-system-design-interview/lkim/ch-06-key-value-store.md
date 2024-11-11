key-value store, also referred to as a key-value database, is a non-relational database
- Amazon dynamo, Memcached, Redis

Given conditions
• The size of a key-value pair is small: less than 10 KB.
• Ability to store big data.
• High availability: The system responds quickly, even during failures.
• High scalability: The system can be scaled to support large data set.
• Automatic scaling: The addition/deletion of servers should be automatic based on traffic.
• Tunable consistency.
• Low latency.

## Single server key-value store
To fit more data in a single server:
• Data compression
• Store only frequently used data in memory and the rest on disk
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
• Read path

