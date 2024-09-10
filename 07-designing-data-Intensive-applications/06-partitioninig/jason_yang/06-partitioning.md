**Partitioning** is the method of splitting a database into smaller parts to manage large datasets and high query loads more effectively. 

**Key Terms:**
- **Partitioning:** Dividing a database into smaller parts.
- **Shard:** Term used in MongoDB, Elasticsearch, SolrCloud for a partition.
- **Region:** Partition in HBase.
- **Tablet:** Partition in Bigtable.
- **vnode:** Partition in Cassandra and Riak.
- **vBucket:** Partition in Couchbase.

### Main Goals of Partitioning

- **Scalability:** Distributing data across multiple disks and spreading query load over many processors to achieve scalability.
- **Independent Query Processing:** Each node can execute queries on its partition independently, allowing for increased query throughput by adding more nodes.
- **Parallel Processing:** Complex large queries can be processed across multiple nodes in parallel.

### Approaches to Partitioning

- **Key Range Partitioning:** Assigns a range of keys from the minimum to the maximum value. This allows efficient range queries but can cause hotspots if keys are accessed sequentially.
- **Hash Partitioning:** Applies a hash function to the key and assigns the range of hash values to partitions. This destroys key order, making range queries inefficient but evenly distributes load.

### Interaction of Partitioning and Indexing

- **Document-Based Index (Local Index):** The primary key and value are stored in the same partition, requiring updates to a single partition during writes. However, reading may require scatter/gather across all partitions.
- **Term-Based Index (Global Index):** The index is split by value, and records for a specific index term may be in multiple partitions. This can provide single-partition reads but requires updates to multiple partitions during writes.

### Rebalancing and Request Routing

- **Rebalancing:** Redistributes partitions when nodes are added or removed to keep load balanced.
- **Request Routing:** Determines how clients send requests to the correct partition, ranging from simple partition-aware load balancers to advanced parallel query execution engines.

Partitioned databases provide scalability by distributing data across multiple machines. However, additional considerations are needed to handle issues like partial writes across multiple partitions. The next chapter will address these challenges.

## Partitioning and Replication

### Combining Partitioning and Replication

- Partitioning is often combined with replication, where copies of each partition are stored on multiple nodes.
- Each record belongs to one partition but can be stored on several nodes for fault tolerance.
- A single node may store multiple partitions.

### Leader-Follower Replication Model

- Each partition's leader is assigned to a specific node, with followers on other nodes.
- A node can act as a leader for some partitions and a follower for others.

### Choosing Partitioning and Replication

- The choice of partitioning method is largely independent of the replication method.
- This chapter focuses on partitioning, not replication.

## Partitioning of Key-Value Data

### Goals of Partitioning

- Aim to distribute data evenly across nodes to balance data and query load.
- Ideally, 10 nodes can handle 10 times the data and load of a single node.

### Skew and Hotspots

- Uneven partitioning can cause skew, leading to hotspots where one partition handles all the load, leaving other nodes idle.

### Random Allocation Problems

- Avoiding hotspots can be done by randomly assigning data to nodes, but this makes it difficult to know which node to query for a specific item.

### Key-Value Data Model Approach

- With a simple key-value data model, you can always access records by their primary key, similar to looking up an entry in an encyclopedia by title.

### Key Range Partitioning

- Assigns continuous key ranges to each partition, making it easy to determine which partition contains a specific key.

### Handling Data Imbalance

- Key ranges should be evenly distributed to avoid skew, similar to distributing words across volumes in an encyclopedia.

### Partition Boundaries

- Boundaries can be manually set by administrators or automatically by the database.
- This method is used in systems like Bigtable, HBase, RethinkDB, and MongoDB (before version 2.4).

### Advantages of Key Ordering

- Within each partition, keys are sorted, making range scans efficient.

### Hotspot Issues

- Certain access patterns can cause hotspots, such as keys based on timestamps.

## Hash Partitioning

### Need for Hash Partitioning

- To avoid skew and hotspots, many distributed data stores use a hash function to determine the partition for a given key.

### Role of Hash Functions

- A 32-bit hash function takes a string and returns a number between 0 and 2^32-1.
- A good hash function ensures evenly distributed hash values.

### Hash Partitioning Methods

- Each partition has a range of hash values, and a key's hash value determines its partition.
- Partition boundaries can be set evenly or pseudo-randomly.

### Consistent Hashing

- Used in internet-wide cache systems like CDNs to evenly distribute load without central control or distributed consensus.

### Drawbacks of Hash Partitioning

- Destroys the order of keys, making range queries inefficient.
- For example, MongoDB's hash-based sharding mode requires range queries to be sent to all partitions.

### Hybrid Approach

- Systems like Cassandra use a hybrid approach, combining hash and sorted indexes for efficient range scans.

### Handling Skewed Workloads and Hotspots

- Certain workloads may still cause hotspots, requiring application-level solutions like appending random numbers to hot keys.
- Managing split keys requires additional work during reads.

### Future Prospects

- Future data systems may automatically detect and compensate for skewed workloads, but currently, the application must handle these trade-offs.

## Partitioning and Secondary Indexes

### Basic Concept

- The discussed partitioning methods apply to a key-value data model.
- Accessing records via primary key only.

### Complexity of Secondary Indexes

- Secondary indexes are used for searching by non-primary key attributes.

### Importance of Secondary Indexes

- Essential in relational and document databases for advanced querying capabilities.

### Challenges of Partitioning with Secondary Indexes

- Secondary indexes don't map neatly to partitions.
- Two main approaches: Document-based partitioning and term-based partitioning.

### Document-Based Secondary Index Partitioning

- Each partition maintains its secondary index.
- Writes update only the relevant partition.
- Reads may require scatter/gather across all partitions.

### Global Index (Term-Based Partitioning)

- A global index spans all partitions.
- Each partition may hold index entries for specific terms.
- Reads target specific partitions, but writes may need updates across multiple partitions.

### Rebalancing Partitions

### Database Evolution

- Adding more CPU or storage due to increased query load or data size.
- Redistributing data when nodes are added or removed.

### Rebalancing Requirements

- Ensure fair distribution of load post-rebalancing.
- Allow continuous database operation during rebalancing.
- Move only necessary data to minimize network and disk I/O load.

### Strategies for Rebalancing

- Avoid `mod N` as it causes most keys to move when the number of nodes changes.
- Use a fixed number of partitions and assign multiple partitions to each node.
- Dynamic partitioning splits partitions when they grow too large.

### Automatic vs Manual Rebalancing

- Automatic rebalancing reduces operational overhead but can be unpredictable.
- Manual rebalancing allows for controlled adjustments but requires human intervention.

### Request Routing

- Determines how clients send requests to the correct partition.
- Options include all nodes handling any request, a routing layer, or partition-aware clients.
- Cluster metadata can be managed with tools like ZooKeeper.

### Parallel Query Execution

- Simple queries in NoSQL databases focus on single-key operations.
- MPP databases support complex queries with multiple joins and aggregations executed in parallel across nodes.
- Discussing advanced parallel query execution techniques will be covered in future chapters.

This summary provides an overview of partitioning strategies and their implications for database scalability and performance.