## Chapter 6 Partitioning

- Break the data up into _partitions_ aka _sharding_
- Confusing terms
  - _shard_ in MongoDB, Elasticsearch, and SolrCloud
  - _region_ in HBase, _tablet_ in Bigtable, _vnode_ in Cassandra and Riak, and _vBucket_ in Counchbase.
- _scalability_
- Applies to either transactional processing or analytics.

### Partitioning and Replication

- A node may store more than one partition.
- The choice of partitioning scheme is mostly independent of the choice of replication scheme.

### Partitioning of Key-Value Data

- Goal: To spread the data and the query load evenly across nodes.
- _Skewed_: Some partitions have more data or queries than others.
- _Hot spot_: A partition with disproportionately high load.
- To avoid hot spots, assign records to nodes randomly? Or use primary key that's alphabetically sorted? Check below.

#### Partitioning by Key Range

- Assign a continuous range of keys to each partition.
- Partition boundaries need to adapt to the data.
- Downside: Certain access patterns can lead to hot spots. - Need to choose the key wisely?

#### Partitioning by Hash of Key

- Hash function to determine the partition for a given key.
- For partitioning purposes, the hash function need not be cryptographically strong.
- Regarding the term, it's better to use _hash partitioning_ instead of _consistent hashing_.
- Range queries on the primary key are not supported by Riak, Couchbase, or Voldemort.
- Cassandra uses _compound primary key_ consisting of several columns.
- Concatenated index approach

#### Skewed Workloads and Relieving Hot Spots

- Hashing keys for partitioning can help reduce hot spots, but extreme skew (e.g., all requests for one key) can still overload a single partition. This often occurs with highly active users or events on social media platforms.
- To mitigate skew, applications can add random numbers to hot keys, spreading writes across multiple keys. However, this requires additional read complexity and bookkeeping. Future systems may automatically handle skew, but currently, developers must manage these trade-offs

### Partitioning and Secondary Indexes

#### Partitioning Secondary Indexes by Document

- Document-partitioned secondary indexes are maintained separately for each partition, simplifying writes but potentially complicating reads that require querying multiple partitions.
- This approach, also known as scatter/gather, can lead to performance issues due to the need to query all partitions and combine results, especially for queries using multiple secondary indexes.

#### Partitioning Secondary Indexes by Term

- Global term-partitioned indexes cover data across all partitions and are themselves partitioned, improving read efficiency by allowing targeted queries to specific index partitions.
- While offering faster reads, term-partitioned indexes can slow down writes and make them more complex, often requiring asynchronous updates to maintain consistency across partitions.

### Rebalancing Partitions

- The process of moving load from one node in the cluster to another.
  - After rebalancing, the load(data storage, read and write requests) should be shared fairly between the nodes in the cluster.
  - While rebalancing is happening, the database should continue accepting reads and writes.
  - No more data than necessary should be moved between nodes, to make rebalancing fast and to minimize the network and disk I/O load.

#### Strategies for Rebalancing

##### How not to do it: hash mod N

- N(number of nodes) can grow, and it's not stable to use as a key

##### Fixed number of partitions

- Can not add or remove a node from a cluster.

##### Dynamic partitioning

- Dynamic partitioning adapts the number and boundaries of partitions based on data volume. As data grows, partitions are automatically split to maintain a balanced distribution across nodes. This avoids manual reconfiguration and allows the system to scale smoothly.
- The approach can be used with both key range and hash-based partitioning. It helps balance load as the dataset size changes, but may require strategies like pre-splitting to avoid initial hotspots. The number of partitions can be made proportional to dataset size or number of nodes, each with different trade-offs.

##### Partitioning proportionally to nodes

- In this approach, the number of partitions is proportional to the number of nodes, ensuring that as nodes are added, partitions are split and distributed to maintain balance. This keeps partition sizes stable relative to the dataset size, even as the cluster grows.
- When a new node joins, it randomly splits a fixed number of existing partitions and takes ownership of half, distributing the load more evenly. This method, often using hash-based partitioning, aligns with consistent hashing principles and helps manage load distribution efficiently.

### Request Routing

- When a client wants to make a request, how does it know which node to connect to? -> _service discovery_
  - Allow clients to contact any node(e.g., via a round-robin load balancer)
  - Send all requests from clients to a routing tier first, which determines the node that should handle each request and forwards it accordingly.
  - Require that clients be aware of the partitioning and the assignment of partitions to nodes.
- How does the component making the routing decision learn about changes?
  - ZooKeeper
  - LinkedIn's Espresso uses Helix

#### Parallel Query Execution

- _Massively Parallel Processing (MPP)_ relational databases, often used for analytics, support complex queries with joins, filtering, grouping, and aggregation operations. These systems break down complex queries into multiple execution stages that can run in parallel across different nodes in the database cluster.
- Parallel execution is particularly beneficial for queries that scan large portions of a dataset, significantly improving performance for data warehouse and analytics workloads. This approach contrasts with simpler NoSQL systems that typically support only single-key operations or basic scatter/gather queries.
