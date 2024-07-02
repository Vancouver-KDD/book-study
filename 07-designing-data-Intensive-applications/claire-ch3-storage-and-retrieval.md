# Storage and Retrieval

## Data Structures that power our database

- We can create a simple key-value database implemented using Bash functions (db_set and db_get), which store and retrieve data by appending to a file.
- While this simple approach is efficient for writing data, it becomes inefficient for reading when the database grows large, as it requires scanning the entire file for each lookup.
- To improve read efficiency, databases use indexes - additional data structures that act as signposts to help locate data quickly. However, indexes add overhead to write operations as they need to be updated with each write.
- There's a trade-off in storage systems: well-chosen indexes speed up read queries but slow down writes. Database administrators must carefully select indexes based on typical query patterns to optimize performance.

### Hash indexes

- Hash indexes are a simple and effective indexing strategy for key-value stores, where an in-memory hash map maps keys to byte offsets in the data file.
- This approach, used by systems like Bitcask, offers high-performance reads and writes but requires all keys to fit in memory. It's well-suited for situations with frequent updates to a limited number of keys.
- To manage disk space, the log is broken into segments. Compaction and merging processes are used to discard duplicate keys and retain only the most recent values, while allowing continued read and write operations.
- While append-only logs have advantages like faster sequential writes and simpler concurrency control, hash table indexes have limitations such as memory constraints and inefficient range queries.

### SSTables and LSM-Trees

- SSTable: `Sorted String Table`
  - 1. Merging segrments is simple and efficient.
    - Works like `mergesort` algorithm
    - When multiple segments contain the same key, keep the value from the most recent segment and discard the values in older segments.
  - 2. To find a particular key, no need to keep an index of all the keys in memory. Keys are sorted in a certain way so it's easy to find where a particular key is located.
  - 3. Compression before writing it to disk.

#### Constructing and maintaining SSTables

- To maintain sorted data, the system uses an in-memory balanced tree structure (like a red-black tree) called a memtable, which allows insertion of keys in any order and reading them back in sorted order.
- When the memtable reaches a certain size, it's written to disk as an SSTable (Sorted String Table) file, becoming the most recent segment of the database. A new memtable is then created for incoming writes.
- Read requests check the memtable first, then the most recent on-disk segment, and so on through older segments. A background process periodically merges and compacts segment files.
- To prevent data loss in case of crashes, a separate log is maintained on disk for immediate write appending. This log is discarded once the memtable is written to an SSTable.

#### Making an LSM-Tree out of SSTables

- LSTM-Tree: `Log-Structured Merge-Tree`
- The algorithm described is essentially what's used in LevelDB and RocksDB, which are key-value storage engine libraries designed to be embedded in other applications.
- Similar storage engines are used in Cassandra and HBase, inspired by Google's Bigtable paper which introduced terms like SSTable and `memtable`.
- This indexing structure was originally described as Log-Structured Merge-Tree (LSM-Tree) by Patrick O'Neil et al., building on earlier work on log-structured filesystems.
- Storage engines based on merging and compacting sorted files are often called LSM storage engines.
- Lucene, used by Elasticsearch and Solr for full-text search, uses a similar method for storing its term dictionary, though a full-text index is more complex than a key-value index.

#### Performance optimizations

- Performance optimization: LSM-trees use Bloom filters to quickly determine if a key doesn't exist, avoiding unnecessary disk reads. This addresses the potential slowdown when looking up non-existent keys, which would otherwise require checking the memtable and all disk segments.
- Compaction strategies: Two main approaches are used - size-tiered (merging smaller SSTables into larger ones) and leveled compaction (splitting key ranges into smaller SSTables across different levels). LevelDB and RocksDB use leveled compaction, HBase uses size-tiered, and Cassandra supports both. These strategies help manage disk space and improve read/write performance.

### B-Trees

- B-trees are the most widely used indexing structure in databases, breaking data into fixed-size pages (typically 4 KB) and maintaining a balanced tree structure for efficient key-value lookups and range queries.
- B-trees update data in-place by overwriting pages on disk, using a write-ahead log for crash recovery, and require careful concurrency control; this contrasts with log-structured indexes which append data and perform background merging.

#### Making B-trees reliable

- B-tree write operation:
  - Overwrites pages on disk with new data
  - Assumes page location remains unchanged after overwrite
  - Contrasts with log-structured indexes that only append and delete files
- Hardware considerations:
  - On HDDs: involves moving disk head and overwriting specific sectors
  - On SSDs: more complex due to large block erasure and rewrite requirements
- Complexity and risks:
  - Some operations (like page splits) require overwriting multiple pages
  - Risk of corruption if crash occurs mid-operation
- Write-ahead log (WAL):
  - Used to make B-trees crash-resistant
  - Append-only file recording all modifications before applying to tree
  - Helps restore B-tree to consistent state after crashes
- Concurrency challenges:
  - B-trees require careful concurrency control (using latches) for multi-threaded access
  - Log-structured approaches are simpler in this regard, doing merges in the background
- Comparison with log-structured indexes:
  - Log-structured indexes avoid in-place modifications
  - Simpler concurrency model
  - Background merging doesn't interfere with incoming queries

#### B-tree optimizations

- Space and performance optimizations: These include using copy-on-write schemes instead of WAL for crash recovery, abbreviating keys to save space and increase branching factor, and adding pointers between leaf pages for efficient sequential scans.
- Layout optimizations: Efforts are made to position pages with nearby key ranges close to each other on disk to reduce seek times during range queries, though this is challenging to maintain as the tree grows. Some B-tree variants, like fractal trees, incorporate log-structured ideas to further reduce disk seeks.

### Comparing B-Trees and LSM-Trees

#### Advantages of LSM-trees

- Write amplification:
  - B-trees typically write data at least twice (to WAL(Write-Ahead Log) and tree page)
  - LSM-trees also rewrite data multiple times due to compaction
  - This is a concern for SSDs which have limited write cycles
- Write performance:
  - LSM-trees often sustain higher write throughput than B-trees
  - LSM-trees use sequential writes which are faster, especially on HDDs
  - B-trees require overwriting existing pages which is slower
- Space efficiency:
  - LSM-trees can be compressed better and produce smaller files
  - B-trees have fragmentation due to page splits and unused space
  - LSM-trees periodically rewrite data to remove fragmentation
- SSD considerations:
  - SSD firmware often uses log-structured algorithms internally
  - LSM-trees still have advantages in write amplification and fragmentation
  - More compact data representation allows better I/O utilization on SSDs

#### Downsides of LSM-trees

- Performance interference:
  - Compaction in log-structured storage can interfere with ongoing reads and writes
  - Can lead to higher latency at higher percentiles
  - B-trees generally offer more predictable performance
- Compaction challenges:
  - At high write throughput, compaction may not keep up with incoming writes
  - Can lead to growing number of unmerged segments, slowing down reads
  - Requires careful configuration and monitoring
- Key uniqueness:
  - In B-trees, each key exists in exactly one place
  - Log-structured storage may have multiple copies of the same key in different segments
  - B-trees are advantageous for strong transactional semantics and lock implementation
- Established vs. emerging:
  - B-trees are well-established and provide consistently good performance for many workloads
  - Log-structured indexes are becoming increasingly popular in new datastores
- No clear winner:
  - There's no universal rule for determining which is better
  - Empirical testing is recommended for specific use cases

### Other indexing structures

#### Storing values within the index

- Heap file approach:
  - The index stores references to rows in a separate heap file
  - Actual data is stored in one place, avoiding duplication across multiple indexes
  - Efficient for updates that don't change the key or increase value size
  - Challenges arise when new values are larger, requiring relocation or forwarding pointers
- Clustered index:
  - Stores the indexed row directly within the index
  - Eliminates the extra hop from index to heap file, improving read performance
  - Examples: MySQL's InnoDB (primary key), SQL Server (one per table)
- Covering index (index with included columns):
  - A compromise between clustered and non-clustered indexes
  - Stores some of a table's columns within the index
  - Allows some queries to be answered using only the index
- Trade-offs:
  - Clustered and covering indexes can speed up reads
  - They require additional storage and can add overhead on writes
  - Databases need extra effort to maintain transactional guarantees due to data duplication
- Considerations:
  - The choice depends on the specific use case and performance requirements
  - There's a balance between read performance, write overhead, and storage efficiency

#### Multi-column indexes

- Concatenated indexes: The most common type of multi-column index, combining several fields into one key. They are useful for queries that match the index's column order but less effective for other query patterns.
- Multi-dimensional indexes: More versatile for querying multiple columns, especially for geospatial data. Examples include space-filling curves, R-trees (used in PostGIS), and can be applied to non-geographic data like product colors or weather observations. These allow efficient querying across multiple dimensions simultaneously.

#### Full-text search and fuzzy indexes

- Full-text search capabilities: These include searching for synonyms, ignoring grammatical variations, finding words near each other in a document, and handling typos. Lucene, for example, can search for words within a certain edit distance to account for misspellings.
- Specialized data structures: Lucene uses an SSTable-like structure with a finite state automaton for its in-memory index, which can be transformed into a Levenshtein automaton for efficient fuzzy searching. Other techniques involve document classification and machine learning approaches for more advanced information retrieval.

#### Keeping everthing in memory

- Rationale and advantages:
  - RAM becoming cheaper allows for keeping entire datasets in memory
  - In-memory databases can offer significant performance improvements by avoiding disk-related overheads
  - They can provide data models that are difficult to implement with disk-based indexes
- Durability approaches:
  - Some in-memory databases (like Memcached) are for caching only and accept data loss
  - Others aim for durability through special hardware, disk logging, periodic snapshots, or replication
  - Disk is often still used for append-only logs and operational advantages (e.g., backups)
- Examples and innovations:
  - VoltDB, MemSQL, Oracle TimesTen (relational model)
  - RAMCloud, Redis, Couchbase (key-value stores with varying durability)
  - Recent research explores "anti-caching" to support datasets larger than available memory
- Future considerations:
  - Potential changes in storage engine design with the adoption of non-volatile memory (NVM) technologies

## Transaction processing or analytics?

- OLTP vs OLAP: Online Transaction Processing (OLTP) involves low-latency reads and writes of small amounts of data, typically used for interactive user sessions. Online Analytical Processing (OLAP) involves scanning large volumes of data to compute aggregates, usually for business intelligence and decision support.
- Data Warehousing: Due to the different nature of OLTP and OLAP workloads, companies began separating their analytical processing from their transaction processing systems in the late 1980s and early 1990s, leading to the creation of separate data warehouses for analytics.

### Data warehousing

- Purpose and separation: Data warehouses are separate databases used for analytics, allowing business analysts to run complex queries without affecting the performance of OLTP systems used for day-to-day operations.
- ETL process: Data is extracted from various OLTP systems, transformed into an analysis-friendly schema, cleaned up, and loaded into the data warehouse through a process known as Extract-Transform-Load (ETL).
- Optimization for analytics: Data warehouses are specifically optimized for analytic access patterns, which differ from OLTP systems. This allows for more efficient processing of complex queries that scan large parts of the dataset.
- Prevalence and scale: Data warehouses are common in large enterprises with multiple OLTP systems and large amounts of data, but less common in smaller companies where data can often be analyzed using conventional SQL databases or spreadsheets.

#### The divergence between OLTP databases and data warehouses

- Different optimizations: While both OLTP databases and data warehouses often use SQL interfaces, their internal architectures are optimized for different query patterns. OLTP systems are designed for low-latency reads and writes of small amounts of data, while data warehouses are optimized for complex analytical queries over large datasets.
- Specialized systems: Due to these different requirements, many database vendors now focus on supporting either transaction processing or analytics workloads, but not both. Some products offer both capabilities, but often through separate storage and query engines within the same interface. There's also a growing market of specialized data warehouse systems, including both commercial offerings and open-source SQL-on-Hadoop projects.

### Stars and snowflakes: Schemas for analytics

- Star schema structure: In a star schema, a central fact table represents events or transactions, with foreign key references to dimension tables that provide context (who, what, where, when, how, why). Fact tables can be extremely large, while dimension tables contain metadata about the events.
- Variations and characteristics: Snowflake schemas are a variation where dimensions are further broken down into subdimensions. Data warehouse tables are often very wide, with fact tables having over 100 columns and dimension tables including extensive metadata. Star schemas are generally preferred for their simplicity in analysis.

## Column-oriented Storage

- Efficiency for analytical queries: Column-oriented storage stores all values from each column together, rather than storing all values from one row together. This approach is more efficient for analytical queries that typically access only a few columns but scan many rows, as it allows the database to read only the relevant columns from disk.
- Implementation and benefits: Each column is stored in a separate file, with rows in the same order across all column files. This layout significantly reduces the amount of data that needs to be loaded and processed for queries that only access a subset of columns, leading to improved query performance for analytical workloads.

### Column compression

- Compression techniques: Column-oriented storage often allows for effective compression, particularly in data warehouses. One notable technique is bitmap encoding, which is especially useful for columns with a limited number of distinct values. This approach can significantly reduce storage requirements and improve query performance.
- Efficiency for analytical queries: Compressed column storage, such as bitmap indexes, is well-suited for common data warehouse queries. It allows for efficient operations like bitwise OR and AND on the compressed data, enabling quick filtering and aggregation of large datasets without needing to decompress the entire column.

#### Memory bandwidth and vectorized processings

- Bottlenecks and optimizations: For data warehouse queries scanning millions of rows, major bottlenecks include disk-to-memory bandwidth, memory-to-CPU cache bandwidth, and CPU instruction processing efficiency. Column-oriented storage helps address these by reducing data volume and allowing for more efficient use of CPU cycles.
- Vectorized processing: This technique involves processing chunks of compressed column data that fit in CPU cache, allowing for tight loops with fewer function calls and conditions. It enables efficient use of modern CPU features like SIMD instructions and helps avoid branch mispredictions, leading to faster query execution.

### Sort order in column storage

- Importance of row-wise sorting: In column stores, data must be sorted by entire rows, not individual columns, to maintain row integrity. The database administrator can choose the sort order based on common query patterns, which can significantly improve query performance by allowing the query optimizer to scan only relevant rows.
- Benefits of sorted order: Sorting can improve compression, especially for the primary sort key, which can have long sequences of repeated values. This effect diminishes for secondary and tertiary sort keys. Sorted order also enables efficient range queries and can group related data together, further enhancing query performance for certain types of queries.

#### Several different sort orders

- Multiple sort orders: Column stores can store the same data sorted in several different ways to benefit different types of queries. This is similar to having multiple secondary indexes in row-oriented stores, but with some key differences.
- Advantages:
  - Redundant data from replication can be used to store different sort orders
  - Allows choosing the best-suited sort order for each query
  - No need for pointers to data elsewhere, as each sort order contains all column values

### Writing to column-oriented storage

- Challenges with updates: Column-oriented storage, compression, and sorting optimize read performance but make writes more difficult. Update-in-place is not possible with compressed columns, and inserting a row in the middle of a sorted table would require rewriting all column files.
- LSM-tree solution: To handle writes efficiently, column stores often use an LSM-tree-like approach. Writes go to an in-memory store first, then are periodically merged with the column files on disk and written in bulk. This approach allows for efficient writes while maintaining the benefits of column-oriented storage for reads. Queries combine data from both disk and memory seamlessly.

### Aggregation: Data cubes and materialized views

- Materialized views and aggregates:
  - Materialized views are cached results of queries, stored on disk
  - They can improve performance for frequently used aggregations
  - Updates to underlying data require updating materialized views, making them less suitable for OLTP databases
- Data cubes:
  - A special case of materialized view
  - Precompute aggregates along multiple dimensions
  - Allow for very fast queries on precomputed aggregations
  - Limited flexibility compared to querying raw data
- Trade-offs:
  - Data cubes speed up certain queries but reduce flexibility
  - Most data warehouses keep raw data and use aggregates as a performance boost
  - The choice depends on specific query patterns and performance requirements
