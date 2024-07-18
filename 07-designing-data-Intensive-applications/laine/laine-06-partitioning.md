- Each partition is a small DB of its own, while some operations can touch multiple partitions at the same time
- Scalability is the main reason so that a large dataset can be distributed to many disks, thus query load is distributed to many processors

### Partitioning and Replication
Copies of each partition are stored on multiple nodes.
- even though each record belongs to exactly one partition, it may still be stored on several different nodes for fault toler‐
ance.

A node may store more than one partition

## Partitioning of Key-Value Data
- How do you decide which records to store on which nodes?
- The goal of partitioning - to spread the data and the query load evenly across nodes. 

_**Skewed & hot spot**_
- If the partitioning is unfair, meaning some partitions have more data or queries than others
- less effective, and extremely if all the load end up on one partition, your bottleneck is the single busy node - hot spot

**How to avoid hot spots**
- Assign records to nodes randomly.
- disadvantage: when trying to read a particular item, no way of knowing which node it is on, so you have to query all nodes in parallel.
- Thus key-value data model, looking up

### Partitioning by Key range
- where keys are sorted, and a partition owns all the keys from some minimum up to some maximum.
- Sorting has the advantage that efficient range queries are possible
- Risk of hot spots if the application often accesses keys that are close together in the sorted order.
- Partitions are typically rebalanced dynamically by splitting the range into two subranges when a partition gets too big.

### Partitioning by Hash of Key
- Avoiding this risk of skew and hot spots, many distributed datastores use a hash function to determine the partition for a given key.
- a partition owns a range of hashes
- A good hash function takes skewed data and makes it uniformly distributed.
- disadvantage: destroys the ordering of keys, making range queries inefficient.

### Skewed Workloads and Relieving Hot Spots
- Even with hash, hot spots can't be avoided entirely: in the extreme case where all reads and writes are for the same key, you still end up with all requests being routed to the same partition.
- most data systems are not able to automatically compensate for such a highly
skewed workload
- so it’s the **responsibility of the application to reduce the skew.**
  - ex) add a random number to the beginning or end of the very hot key.
     - It splits the writes to the key evenly across 100 different keys, allowing those keys
to be distributed to different partitions.
     - Additional bookkeeping required: any reads now have to do additional work, as they have to read the data from all 100 keys and combine it.
     - So only apply to small number of hot keys

### Partitioning and Secondary Indexes
- More complicated if secondary indexes are involved
- A secondary index usually doesn’t identify a record uniquely but rather is a way of searching for occurrences of a particular
value
  - ex) find all actions by user 123, find all articles containing the word hogwash

#### Document-partitioned indexes (local indexes)
- the secondary indexes are stored in the same partition as the primary key and value.
- only a single partition needs to be updated on write, but a read of the secondary index requires a scatter/gather across all partitions.

#### Term-partitioned indexes (global indexes)
- the secondary indexes are partitioned separately, using the indexed values.
- An entry in the secondary index may include records from all partitions of the primary key.
- When a document is written, several partitions of the secondary index need to be updated
- a read can be served from a single partition.

## Rebalancing Partitions
**The process of moving load from one node in the cluster to another**

#### Why?
- Over time, things change in a database
- The query throughput increases, so you want to add more CPUs to handle the load.
- The dataset size increases, so you want to add more disks and RAM to store it.
- A machine fails, and other machines need to take over the failed machine’s
responsibilities.

#### Regardless of partitioning scheme, some minimum requirements
- After rebalancing, the load (data storage, read and write requests) should be shared fairly between the nodes in the cluster.
- While rebalancing is happening, the database should continue accepting reads and writes.
- No more data than necessary should be moved between nodes, to make rebalancing fast and to minimize the network and disk I/O load.

(Omit the different strategies)

#### Operations: Automatic or Manual Rebalancing
**Does the rebalancing happen automatically or manually? or between?**
- Rebalancing is an expensive operation, because it requires rerouting requests and moving a large amount of data from one node to another

**Fully automated rebalancing** 
- convenient, less operational work to do for normal maintenance.
- But unpredictable
- If not done carefully, this process can overload the network or the nodes and harm the performance of other requests while the rebalancing is in progress.
- Automation can be dangerous in combination with automatic failure detection.
  - For example, one node is overloaded and is temporarily slow to respond to requests.
  - The other nodes conclude that the overloaded node is dead, and automatically rebalance the cluster to move load away from it.
  - This puts additional load on the overloaded node, other nodes, and the network—making the situation worse and potentially causing a cascading failure.

**So have a human in the loop for rebalancing**
- It’s slower than a fully automatic process, but it can help prevent operational surprises. 


## Request Routing
DB is partitioned across multiple nodes running on multiple machines. 

#### Service discovery problem
**When a client wants to make a request, how does it know which node to connect to? **
- As partitions are rebalanced, the assignment of partitions to nodes changes.
- Somebody needs to stay on top of those changes
  - if I want to read or write the key “foo”, which IP address and port number do I need to connect to?
- Any piece of software that is accessible over a network has this problem, especially if it is aiming for high availability (running in a redundant
configuration on multiple machines) - not limited to DB

**How to resolve the problem**
1. Allow clients to contact any node (e.g., via a round-robin load balancer). If that
node coincidentally owns the partition to which the request applies, it can handle
the request directly; otherwise, it forwards the request to the appropriate node,
receives the reply, and passes the reply along to the client.
2. Send all requests from clients to a routing tier first, which determines the node
that should handle each request and forwards it accordingly. This routing tier
does not itself handle any requests; it only acts as a partition-aware load balancer.
3. Require that clients be aware of the partitioning and the assignment of partitions
to nodes. In this case, a client can connect directly to the appropriate node,
without any intermediary.
In all cases, the key problem is: how does the component making the routing decision
(which may be one of the nodes, or the routing tier, or the client) learn about changes
in the assignment of partitions to nodes?



#### Parallel Query Execution - analytics 
massively parallel processing (MPP) relational database products, often used for analytics, are much more sophisticated in the types of queries they support.
A typical data warehouse query contains several join, filtering, grouping, and aggre‐
gation operations. The MPP query optimizer breaks this complex query into a num‐
ber of execution stages and partitions, many of which can be executed in parallel on
different nodes of the database cluster. Queries that involve scanning over large parts
of the dataset particularly benefit from such parallel execution.
Fast parallel execution of data warehouse queries is a specialized topic, and given the
business importance of analytics, it receives a lot of commercial interest. We will dis‐
cuss some techniques for parallel query execution in Chapter 10. For a more detailed
overview of techniques used in parallel databases, please see the references [1, 33]. 

