# Chapter 06: Partitioning

- Concept of data partitioning: each piece of data belongs to exactly one partition. 
- Partitioning enhances scalability by distributing data across different nodes in a shared-nothing cluster
    - better use of storage and processing resources. 
    - enables independent execution of queries on separate partitions, improving query throughput as more nodes are added. 
    
- The partitioned database approach as been adopted by modern NoSQL databases and Hadoop-based data warehouses. 

## Partitioning and Replication
- Partitioning is often combined with replication to enhance fault tolerance
    - This allows copies of each partition to be stored on multiple nodes. 
    - Although each record belongs to one partition, it can still exist on several nodes. 
        - A node may store multiple partitions, and in a leader-follower replication model, each partition's leader is on one node while followers are on others. 
        - A node can be the leader for some partitions and a follower for others. 
        
- The principles of database replication also apply to partition replication. 
- The choice of partitioning scheme is mostly independent of the replication scheme, and the chapter will focus on partitioning without delving into replication details.

## Partitioning of Key-Value Data
- Strategies for partitioning a large dataset across multiple nodes to distribute data and query load evenly. 
    - Ideally, each node should handle an equal share of data and queries, allowing 10 nodes to manage 10 times the data and throughput of a single node. 
    - If the distribution is uneven, resulting in some partitions having more data or queries than others, it is considered skewed. 
        - Skewed partitioning reduces effectiveness and can create "hot spots," where one node becomes a bottleneck due to a high load.
        - A simple way to avoid hot spots is to assign records randomly to nodes, which balances the data but makes it difficult to locate specific records without querying all nodes. 
    - A more effective method assumes a key-value data model, where records are accessed by a primary key, similar to looking up entries alphabetically in a paper encyclopedia. 
        - This helps efficiently locate records on specific nodes.

### Partitioning by Key Range
- One partitioning method is to assign a continuous range of keys to each partition
    - similar to how encyclopedia volumes cover ranges of letters.
- Knowing the key range boundaries helps determine which partition contains a given key
    -  This allows requests to be directed to the appropriate node.
- Key ranges may be unevenly spaced to adapt to data distribution
    - Ensures even data distribution across partitions.
- Partition boundaries can be set manually or automatically 
    - used in dbs like Bigtable, HBase, RethinkDB, and MongoDB (before version 2.4).
- Within each partition, keys can be kept in sorted order
    - facilitates range scans and fetching related records in one query.
- Key range partitioning can lead to hot spots if access patterns are uneven,
    - i.e. all writes targeting a partition for the current day.
- To avoid hot spots, alternative key structures (i.e. prefixing timestamps with sensor names) can distribute write loads more evenly across partitions.
- With this method, fetching data from multiple sensors within a time range requires separate range queries for each sensor name.

## Partitioning by Hash of Key
- To avoid skew and hot spots, many distributed datastores use hash functions to determine the partition for a given key.
- A good hash function transforms skewed data into a uniform distribution, providing a seemingly random number for each input string, evenly distributed across a range.
- Cryptographic strength is not necessary for partitioning
    - i.e. MD5 (used by Cassandra & MongoDB), Fowler–Noll–Vo function (used by Voldemort).
- Built-in hash functions in programming languages may not be suitable for partitioning due to inconsistencies across processes.
- Keys are assigned to partitions based on their hash values, which can be evenly spaced or chosen pseudorandomly
    - aka consistent hashing.
- This method effectively distributes keys among partitions but loses the ability to perform efficient range queries, as adjacent keys are scattered across partitions.
- In hash-based sharding (i.e. MongoDB), range queries must be sent to all partitions, and some databases like Riak, Couchbase, and Voldemort do not support range queries on primary keys.
- Cassandra offers a compromise by using a compound primary key, where only the first part is hashed for partitioning, while other columns are used as a concatenated index for sorting.
    - This approach supports efficient range scans over non-hashed columns, allowing for an elegant data model for one-to-many relationships.
    - For example, in a social media context, using a primary key of (user_id, update_timestamp) allows efficient retrieval of a user's updates within a time interval, with updates ordered by timestamp within each partition.

### Skewed Workloads and Relieving Hot Spots
- Hashing a key can help reduce hot spots 
    - However can't completely eliminate them, especially when all reads and writes are for the same key.
    - i.e. a celebrity on a social media platform causing a surge of activity, leading to many writes to the same key.
- Most data systems currently do not automatically handle highly skewed workloads
    - it falls to the application to address this issue.
- A common solution is to add a random number to the key, distributing writes evenly across multiple keys and partitions.
    - This complicates reads, which must combine data from all split keys
    - requires tracking which keys are split.
- Future data systems may detect and manage skewed workloads automatically, but for now, developers must weigh the trade-offs for their applications.


## Partitioning and Secondary Indexes
- Above partitioning schemes rely on a key-value data model
    - uses primary key to determine the partition and route requests accordingly.
- More complex with secondary indexes (used for searching records by particular values rather than uniquely identifying records)
    - Secondary indexes are essential in relational databases and common in document databases
    - However, some key-value stores have avoided them due to complexity.
    - Despite the complexity, some key-value stores like Riak have added secondary indexes because of their usefulness in data modeling.
    - Secondary indexes are fundamental to search servers like Solr and Elasticsearch.
    - The challenge: they don't map neatly to partitions.

- There are two main approaches to partitioning databases with secondary indexes: document-based partitioning and term-based partitioning.

### Partitioning Secondary Indexes by Document
- In a website for selling used cars, each listing has a unique document ID, and the database is partitioned by these IDs 
    - i.e. IDs 0-499 in partition 0, IDs 500-999 in partition 1, and so on.
- To allow users to search by attributes like color and make, a secondary index is needed on these fields.
- Each partition maintains its own secondary indexes, covering only its documents, making it a local index.
- Writing operations only involve the partition containing the relevant document ID.
- Reading from a document-partitioned index requires querying all partitions, as cars of the same color or make may be spread across different partitions.
    - aka scatter/gather
    - can make read queries on secondary indexes expensive and prone to tail latency amplification.
    - Expensive. However, this approach is used by systems like MongoDB, Riak, Cassandra, Elasticsearch, SolrCloud, and VoltDB.
- Database vendors often recommend structuring partitioning so that secondary index queries are served from a single partition, but this can be challenging with multiple secondary indexes.

### Partitioning Secondary Indexes by Term
- A global index covers data from all partitions
    - whereas a local index is specific to each partition.

- A global index cannot be stored on a single node (it becomes a bottleneck)
    - must be partitioned differently from the primary key index.

- In a term-partitioned global index, terms like `color:red` determine the partition
    - i.e. colors `a to r` might be in partition 0, and `s to z` in partition 1.
- `term` comes from full-text indexes
    - terms are words in a document.
- Indexes can be partitioned by the term itself or a hash of the term; partitioning by term aids range scans, while hashing evenly distributes the load.

- Global indexes make reads more efficient 
    - client only requests the partition containing the desired term
    - this avoids scatter/gather across all partitions.
    - downside: writes are slower and more complex because updating a document may affect multiple index partitions.
- Keeping a global index up to date requires distributed transactions across affected partitions, not supported in all databases.
- Updates to global secondary indexes are often asynchronous
    - recent changes may not immediately appear in the index.
    - i.e. Amazon DynamoDB's global secondary indexes are updated quickly under normal conditions but may delay during infrastructure faults.
- Other systems using global term-partitioned indexes include Riak's search feature and Oracle's data warehouse, which allows choosing between local and global indexing.

## Rebalancing Partitions
- Changes in a database over time can include:
    - increased query throughput
    - larger dataset size
    - machine failures (needs rebalancing). 
        - Rebalancing involves moving data and requests from one node to another in a cluster.
        - Rebalancing should ensure that load is shared fairly between nodes after the process.
- Db should continue to accept reads and writes during rebalancing.
- Only the necessary amount of data should be moved to minimize network and disk I/O load and make rebalancing fast.

### Strategies for Rebalancing
Different ways of assigning partitions to nodes:

#### How not to do it: hash mod N
- When partitioning by the hash of a key, it's effective to divide hash ranges and assign each range to a partition (i.e. range 0 ≤ hash(key) < b0 to partition 0, range b0 ≤ hash(key) < b1 to partition 1, etc.).
- Using the mod operator (i.e. hash(key) % N) to assign keys to partitions seems straightforward but has drawbacks.
- If N (number of nodes) changes, many keys will need to be moved between nodes
    - this makes rebalancing costly.
- i.e. a key that maps to node 6 with 10 nodes would move to node 3 with 11 nodes and node 0 with 12 nodes, resulting in frequent and expensive data moves.
- An approach that minimizes data movement during rebalancing is needed to reduce the cost of adjustments.

#### Fixed number of partitions
- To simplify rebalancing, create more partitions than there are nodes and assign multiple partitions to each node 
    - i.e. 1,000 partitions for 10 nodes.
- When adding a node, it can take a few partitions from each existing node, redistributing partitions evenly across the cluster.
- When removing a node, the process reverses, with partitions being reassigned to the remaining nodes.
- Only entire partitions are moved between nodes, not individual records. The assignment of keys to partitions remains unchanged.
- The rebalancing process takes time
    - thus old partition assignments are used for reads and writes during the transfer.
    - This approach allows adjusting for mismatched hardware by assigning more partitions to more powerful nodes.
    - Used by systems like Riak, Elasticsearch, Couchbase, and Voldemort.

- The number of partitions is usually fixed when the database is set up and is not changed afterward. This simplifies management but requires choosing a number high enough to accommodate future growth.

- Selecting the right number of partitions is challenging with variable dataset sizes
    - too many partitions can increase overhead, while too few can make rebalancing and failure recovery expensive.
- The optimal partition size is one that balances performance and overhead, which can be difficult to achieve with fixed partition numbers.

#### Dynamic partitioning
- Key Range Partitioning Challenges:
  - Fixed partitions with set boundaries can lead to data being unevenly distributed 
    - i.e. all data in one partition.
  - Manually reconfiguring boundaries is tedious and impractical.

- Dynamic Partitioning Approach:
  - Databases like HBase and RethinkDB create partitions dynamically based on data size.
  - When a partition exceeds a configured size (i.e. 10 GB in HBase), it is split into two.
  - Partitions can also be merged if data is deleted and partition size falls below a threshold.

- Partition Management:
  - Partitions are assigned to nodes, and nodes can handle multiple partitions.
  - After splitting, partitions can be redistributed across nodes to balance load.
  - For HBase, partition transfers use the underlying distributed filesystem (HDFS).

- Advantages:
  - Number of partitions adjusts with total data volume: fewer partitions for small datasets and more for large ones.
  - Limits individual partition size to a configurable maximum.

- Caveats:
  - An empty database starts with a single partition, causing potential load imbalance until the first split.
  - HBase and MongoDB offer pre-splitting to mitigate initial load imbalance by setting up an initial set of partitions.

- Flexibility:
  - Dynamic partitioning works with both key-range and hash-partitioned data.
  - MongoDB supports dynamic splitting for both partitioning strategies since version 2.4.

#### Partitioning proportionally to nodes
- Dynamic Partitioning:
  - Number of partitions scales with dataset size, maintaining partition sizes between a fixed minimum and maximum.
  - Number of partitions is independent of the number of nodes.

- Fixed Number of Partitions:
  - Partition size is proportional to dataset size.
  - Number of partitions remains constant, regardless of node count.

- Proportional to Nodes:
  - Cassandra and Ketama use a fixed number of partitions per node.
  - As dataset size grows, partitions are distributed across nodes, keeping partition size stable.
  - Adding nodes decreases partition size, maintaining balance as data volume increases.

- Rebalancing with New Nodes:
  - New nodes split a fixed number of existing partitions and take ownership of one half of each split partition.
  - Random splits can be uneven but average out with a large number of partitions (i.e. 256 per node in Cassandra).
  - Cassandra 3.0 introduced a rebalancing algorithm to avoid unfair splits.

- Hash-Based Partitioning:
  - Partition boundaries are picked from the range of numbers produced by hash functions.
  - This approach aligns with consistent hashing principles and reduces metadata overhead with newer hash functions.

## Operations: Automatic or Manual Rebalancing
- Rebalancing Types:
  - Fully Automatic 
    - The system autonomously decides when to move partitions between nodes, with no administrator input.
  - Fully Manual 
    - Administrators explicitly configure and update partition assignments, requiring manual intervention for changes.

- Hybrid Approach: 
  - Systems like Couchbase, Riak, and Voldemort suggest automatic rebalancing but require administrator approval to implement changes.

- Advantages of Automated Rebalancing:
  - Reduces operational work during normal maintenance.
  - Convenient as it minimizes manual intervention.

- Disadvantages of Automated Rebalancing:
  - Can be unpredictable and expensive, involving significant data movement and network load.
  - May negatively impact performance if not managed carefully.

- Risks with Automation:
  - Combined with automatic failure detection, it may worsen situations where nodes are slow or overloaded.
  - Can lead to cascading failures due to increased load on overloaded nodes and the network.

- Human Involvement:
  - Having a human in the loop can help prevent operational issues and surprises, despite being slower than fully automatic processes.

## Request Routing
Service discovery and routing approaches for databases:

- Service Discovery Problem: 
  - Determines how clients find the correct node for a request, given that partition assignments to nodes can change due to rebalancing.

- High-Level Approaches:
  1. Direct Node Contact: 
     - Clients connect to any node.
     - The node handles the request if it owns the partition; otherwise, it forwards the request to the correct node.
  2. Routing Tier:
     - All requests are sent to a dedicated routing tier.
     - The tier determines the correct node for each request and forwards it accordingly.
  3. Client Awareness:
     - Clients are aware of the partitioning and directly connect to the appropriate node.

- Handling Routing Changes:
  - Essential to ensure all components are aware of partition-to-node assignments to avoid misrouted requests.
  - Protocols for achieving consensus in distributed systems are complex but necessary.

- Coordination Services:
  - ZooKeeper: 
    - Used by systems like LinkedIn’s Espresso, HBase, SolrCloud, and Kafka.
    - Maintains authoritative partition-to-node mappings and notifies other components of changes.
  - Helix: 
    - Used by LinkedIn’s Espresso, relying on ZooKeeper.
  - MongoDB: 
    - Uses its own config servers and mongos daemons for routing.

- Gossip Protocols:
  - Cassandra and Riak:
    - Use gossip protocols to disseminate cluster state changes.
    - Clients send requests to any node, which forwards them as needed.

- DNS for Node IPs:
  - Used for locating nodes, which are less frequently updated compared to partition assignments.

- Couchbase:
  - Does not use automatic rebalancing, and its routing tier (moxi) learns about changes from the cluster nodes.

## Parallel Query Execution
- Simple Queries:
  - Most NoSQL distributed datastores handle basic queries, such as reading or writing a single key, and scatter/gather queries for document-partitioned secondary indexes.

- Massively Parallel Processing (MPP) Databases:
  - Used for complex analytics tasks.
  - Support advanced queries involving joins, filtering, grouping, and aggregation.
  - MPP databases break complex queries into multiple execution stages.
  - These stages are executed in parallel across different nodes in the database cluster.
  - Parallel execution benefits queries that scan large portions of the dataset.

- Commercial Interest:
  - Fast parallel query execution is crucial due to the business importance of analytics.

## Summary

- Purpose of Partitioning: 
  - Spread data and query load across multiple machines to avoid hot spots.
  - Necessary when a single machine cannot handle the data volume.

- Partitioning Approaches:
  - Key Range Partitioning:
    - Keys are sorted, and each partition holds a range of keys.
    - Allows efficient range queries but may lead to hot spots with skewed key access.
    - Partitions are dynamically rebalanced by splitting ranges when they grow too large.
  - Hash Partitioning:
    - A hash function is applied to keys, and partitions own ranges of hash values.
    - Does not support efficient range queries but generally distributes load more evenly.
    - Often uses a fixed number of partitions assigned to nodes, with partitions moved as nodes are added or removed.
    - Dynamic partitioning is also possible.

- Secondary Index Partitioning:
  - Document-Partitioned Indexes (Local Indexes):
    - Secondary indexes are stored with the primary key in the same partition.
    - Updates are localized to a single partition, but reads require scatter/gather across all partitions.
  - Term-Partitioned Indexes (Global Indexes):
    - Secondary indexes are partitioned separately based on indexed values.
    - Updates may affect multiple partitions, but reads can be served from a single partition.

- Query Routing:
  - Techniques range from simple load balancing to advanced parallel query execution.
  - Effective routing is crucial for handling partition changes and ensuring accurate data access.

- Challenges with Partitioned Databases:
  - Partitions operate mostly independently, which simplifies scaling but complicates operations requiring multi-partition writes.
  - Issues can arise when writes succeed in some partitions but fail in others, which will be addressed in future chapters.