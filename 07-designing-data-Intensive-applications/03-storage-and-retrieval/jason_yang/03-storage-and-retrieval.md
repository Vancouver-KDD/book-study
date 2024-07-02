#### Basic Concepts

- **Core Functions**: A database should store data when provided and return the data upon request later.
- **Perspective Shift**: While the previous chapter discussed data models and query languages, this chapter focuses on how databases store and retrieve data internally.

#### Importance

- **Application Developers**: Understanding how a database internally handles storage and retrieval is crucial.
- **Storage Engine Selection**: Choosing the appropriate storage engine from the many available options is important for application needs.
- **Performance Tuning**: To optimize a storage engine, a rough understanding of its internal workings is necessary.

#### Differences in Storage Engines

- **Transaction Processing vs. Analytics**: There is a significant difference between storage engines optimized for transactional workloads and those for analytics.
    - **Transaction Processing**: Frequent data modifications and maintaining consistency are key.
    - **Analytics**: Efficient querying and aggregating large volumes of data is essential.
- **Analytics-Optimized Storage Engines**: Discussed in "Transaction Processing or Analytics?" and "Column-Oriented Storage".

#### Main Types of Storage Engines

- **Log-Structured Storage Engines**: Data is appended sequentially to log files, with periodic merging and compaction.
- **Page-Oriented Storage Engines (e.g., B-Trees)**: Data is stored in fixed-size blocks and read/written one page at a time.

### Data Structures That Power Your Database

#### Need for Indexes

- **Indexes**: Necessary for efficiently finding the value of a specific key.
    - Indexes are additional structures derived from primary data to facilitate quick data retrieval.
    - Multiple indexes may be required for different ways of searching the data.

#### Indexes and Performance

- **Advantages**: Well-chosen indexes speed up read queries.
- **Disadvantages**: Indexes slow down write operations as they need updating with each data write.
- **Trade-offs**: Choosing indexes that optimize read performance while considering the overhead on writes.
    - Databases do not index everything by default; developers or database administrators need to select indexes manually based on query patterns.

### Hash Indexes

#### Basic Concept

- **Hash Indexes**: A method for indexing key-value data by mapping each key to a byte offset in the data file using an in-memory hash map.
- **Working Principle**: Update the hash map with the offset of the new key-value pair added to the file. Use the hash map to find the data file's location for value retrieval.

#### Implementation and Examples

- **Bitcask**: Riak's default storage engine, storing all keys in memory for high-performance reads and writes.
- **Limitations**: All keys must fit in memory, but values can be stored on disk.

#### Managing Disk Space

- **Segmented Logs**: Logs are divided into segments of a fixed size, with new writes directed to new segment files.
- **Compaction**: Removing duplicate keys and keeping only the latest update for each key.
- **Merging**: Combining multiple segments to reduce the number of hash maps checked during retrieval.

#### Practical Considerations

- **File Format**: Use binary format instead of CSV for encoding string length and data.
- **Record Deletion**: Add deletion records (tombstones) to indicate deleted keys.
- **Crash Recovery**: Store hash map snapshots on disk for faster recovery.
- **Partial Records**: Include checksums to detect and ignore corrupted log parts.
- **Concurrency Control**: Use a single writer thread and multiple reader threads for concurrent access to data file segments.

#### Advantages

- **Sequential Writes**: Log additions and segment merging are sequential, making them faster than random writes.
- **Simplified Concurrency and Recovery**: Immutable segment files simplify concurrency control and crash recovery.

#### Disadvantages

- **Memory Limitations**: Hash maps must fit in memory, limiting scalability with large key sets.
- **Inefficient Range Queries**: Difficult to perform range queries, requiring individual key lookups in hash maps.

#### Conclusion

- Hash indexes are suitable for high-performance reads and writes but have limitations with memory constraints and range query inefficiency. The next section will explore indexing structures without these limitations.

### SSTables and LSM-Trees

#### SSTables (Sorted String Tables)

- **Basic Concept**: Store log-structured storage segments sorted by key.
- **Advantages**:
    1. **Efficient Merging**: Similar to merge sort, compare the first keys of input files and copy the smallest key to the output file.
    2. **Reduced In-Memory Index**: Use a sparse index of key offsets to quickly scan key ranges.
    3. **Compression**: Group and compress key-value pairs into blocks to save disk space and reduce I/O bandwidth usage.

#### Data Sorting and Maintenance in SSTables

- **Data Sorting**: Add writes to an in-memory balanced tree structure (e.g., red-black tree).
    - **Memtable**: Use the in-memory tree structure to sort data.
    - **Writing to Disk**: Write the memtable to disk as an SSTable file when it reaches a certain size.
- **Read Request Handling**: First check the memtable for the key, then the most recent disk segment.
- **Merging and Compaction**: Background processes combine segment files and discard overwritten or deleted values.

#### LSM-Trees (Log-Structured Merge-Trees)

- **Basic Concept**: An indexing structure based on merging and compacting SSTables.
- **Examples**: Used in LevelDB, RocksDB, Cassandra, HBase, etc.
- **Operation**: Inspired by log-structured file systems, combining memtables and SSTables.
- **Advantages**:
    - **High Write Throughput**: Supports high write throughput due to sequential disk writes.
    - **Efficient Range Queries**: Stores data in sorted order, enabling efficient range queries.
- **Performance Optimizations**:
    - **Bloom Filters**: Used to reduce unnecessary disk reads for nonexistent keys.
    - **Compression Strategies**: Uses size-tiered and leveled compaction strategies.

### Making B-trees Reliable

- **Write Operations**: Overwrite pages on disk without changing their location, maintaining page references.
- **Crash Recovery**: Use a write-ahead log (WAL) to record modifications before applying them to B-tree pages.
- **Concurrency Control**: Protect B-tree data structures with latches (lightweight locks) for multi-thread access.

### B-tree Optimizations

1. **Copy-on-Write**:
    
    - Instead of overwriting pages, modified pages are written to new locations, updating parent pages to point to the new location.
    - Useful for concurrency control and crash recovery.
2. **Key Abbreviation**:
    
    - Store only enough key information to act as boundaries between key ranges, saving space in pages.
    - Allows more keys per page, increasing branching factor and reducing tree levels.
3. **Sequential Layout**:
    
    - Position leaf pages in sequential order on disk for efficient range scans.
    - LSM-trees handle this more easily due to segment rewrites during merging.
4. **Additional Pointers**:
    
    - Add pointers to sibling pages in leaf pages for efficient key scanning.
5. **Fractal Trees**:
    
    - Incorporate log-structured ideas to reduce disk seeks, despite the name having nothing to do with fractals.


### Comparing B-Trees and LSM-Trees

#### Advantages of LSM-Trees:

1. **Write Performance**:
    - LSM-trees have excellent write performance because they use a log-structured index that only appends to files and never overwrites them.
    - Lower write amplification extends the lifespan of SSDs.
2. **Write Throughput**:
    - LSM-trees can often sustain higher write throughput. This is because compaction is more efficient, and SSTable files are written sequentially.
    - On magnetic hard drives, sequential writes are much faster than random writes.
3. **Compression and Storage Space**:
    - LSM-trees offer better compression rates as they periodically rewrite SSTables to remove fragmentation.
    - This reduces storage overheads, especially with leveled compaction.
4. **Advantages on SSDs**:
    - SSDs internally use log-structured algorithms to turn random writes into sequential writes, so LSM-trees’ write patterns do not significantly impact performance.
    - Lower write amplification and reduced fragmentation are still advantageous on SSDs.

#### Disadvantages of LSM-Trees:

1. **Performance Degradation Due to Compaction**:
    - The compaction process can affect the performance of ongoing reads and writes.
    - At higher percentiles, response times can be long, and B-trees offer more predictable performance.
2. **Issues at High Write Throughput**:
    - When write throughput is high, disk bandwidth needs to be shared between initial writes and background compaction.
    - If compaction cannot keep up with the rate of incoming writes, disk space may run out, and read performance may degrade.
3. **Key Duplication**:
    - In LSM-trees, the same key can exist in multiple segments.
    - In B-trees, each key exists in only one place, making it easier to implement transactional isolation.

#### Advantages of B-Trees:

1. **Predictable Performance**:
    - B-trees provide consistent performance as each key exists in only one place in the index.
    - Range locks can be directly attached to the tree, simplifying transactional isolation.
2. **Integration with Database Architecture**:
    - B-trees are deeply integrated into many database architectures, providing reliable performance for various workloads.

#### Conclusion:

- Both B-trees and LSM-trees have their advantages and disadvantages, and their suitability depends on the specific workload.
- Log-structured indexes are becoming increasingly popular in new data stores.
- Testing with actual workloads is essential to determine which storage engine is better for a particular use case.

### Other Indexing Structures

#### 1. **Primary and Secondary Indexes**

- **Primary Index**: Similar to a primary key index, uniquely identifying a row, document, or graph vertex.
- **Secondary Index**: Keys are not unique, so indexes may store a list of matching row identifiers or make each key unique by appending a row identifier.

#### 2. **Storing Values Within the Index**

- **Heap File Approach**: Actual data stored in a separate heap file, with the index referencing its location. Useful for avoiding data duplication with multiple secondary indexes.
- **Clustered Index**: Stores indexed row data directly within the index for improved read performance (e.g., MySQL's InnoDB and SQL Server).
- **Covering Index**: Stores some columns within the index to handle specific queries without accessing the main table.

#### 3. **Multi-Column Indexes**

- **Concatenated Index**: Combines multiple columns into one key, like a phone book indexed by (lastname, firstname).
- **Multi-Dimensional Index**: Used for querying multiple columns simultaneously, important for geospatial data. R-trees are commonly used for such queries.

#### 4. **Full-Text Search and Fuzzy Indexes**

- **Full-Text Search**: Allows searching for similar words and handling typos. Lucene supports searching within a certain edit distance.
- **Fuzzy Search**: Uses document classification and machine learning techniques for searching similar keys.

#### 5. **In-Memory Data Storage**

- **In-Memory Databases**: RAM is becoming cheaper, making it feasible to keep all data in memory, providing high performance and durability through logs or snapshots.
- **Performance Benefits**: Removing the overhead of disk-based index structures can significantly improve performance.
- **Anti-Caching Approach**: Evicts least recently used data to disk when memory is insufficient and reloads it on access, managing memory more efficiently than the OS.

### Transaction Processing or Analytics?

#### Transaction Processing (OLTP):

- **Definition**: Interacting with the database based on user inputs, typically looking up a small number of records by key and inserting or updating records.
- **Key Characteristics**:
    - **Read Pattern**: Fetch a small number of records by key.
    - **Write Pattern**: Random-access, low-latency writes based on user input.
    - **Users**: Mainly end-users/customers via web applications.
    - **Data**: Represents the current state of data.
    - **Dataset Size**: Gigabytes to terabytes.

#### Analytic Processing (OLAP):

- **Definition**: Scanning a large number of records, reading only a few columns, and calculating aggregate statistics for business intelligence.
- **Key Characteristics**:
    - **Read Pattern**: Aggregate over a large number of records.
    - **Write Pattern**: Bulk import (ETL) or event streams.
    - **Users**: Internal analysts for decision support.
    - **Data**: Represents a history of events over time.
    - **Dataset Size**: Terabytes to petabytes.

#### Comparing OLTP and OLAP (Table 3-1):


| Property             | Transaction processing systems (OLTP)             | Analytic systems (OLAP)                   |
| -------------------- | ------------------------------------------------- | ----------------------------------------- |
| Main read pattern    | Small number of records per query, fetched by key | Aggregate over large number of records    |
| Main write pattern   | Random-access, low-latency writes from user input | Bulk import (ETL) or event stream         |
| Primarily used by    | End user/customer, via web application            | Internal analyst, for decision support    |
| What data represents | Latest state of data (current point in time)      | History of events that happened over time |
| Dataset size         | Gigabytes to terabytes                            | Terabytes to petabytes                    |

### Data Warehousing

#### Role of Data Warehouses

- **Multiple OLTP Systems**: Companies operate various OLTP systems for customer-facing websites, point-of-sale (POS) systems, inventory management, vehicle route planning, supplier management, employee management, etc. These systems are complex and operate mostly independently.
- **Importance of OLTP Systems**: OLTP systems require high availability and low-latency transaction processing, making database administrators reluctant to allow business analysts to run ad hoc analytical queries on them, as such queries can be expensive and impact performance.

#### Advantages of Data Warehouses

- **Separate Database**: A data warehouse is a separate database that analysts can query without affecting OLTP operations.
- **ETL Process**: The data warehouse contains a read-only copy of the data from all the OLTP systems. Data is extracted, transformed into an analysis-friendly schema, cleaned, and then loaded into the data warehouse through an ETL (Extract-Transform-Load) process.

#### Differences Between Small and Large Companies

- **Small Companies**: They have fewer OLTP systems and smaller data volumes, making it feasible to analyze data in a conventional SQL database or even in a spreadsheet.
- **Large Companies**: They require data warehouses to handle various OLTP systems and large volumes of data.

#### Optimization of Data Warehouses

- **Optimized for Analytical Queries**: Data warehouses are optimized for analytical access patterns, making them more efficient than querying OLTP systems directly.
- **Data Model**: The data model of a data warehouse is typically relational, and SQL is well-suited for analytical queries.

#### Divergence of OLTP Databases and Data Warehouses

- **Different Optimizations**: OLTP and data warehouses are optimized for very different query patterns, resulting in different internal structures.
- **Mixed Support Products**: Some databases (e.g., Microsoft SQL Server, SAP HANA) support both transaction processing and data warehousing, but they are increasingly becoming two separate storage and query engines accessible through a common SQL interface.

#### Data Warehouse Solutions

- **Commercial Solutions**: Teradata, Vertica, SAP HANA, ParAccel, and others are sold under commercial licenses. Amazon RedShift is a hosted version of ParAccel.
- **Open Source Projects**: SQL-on-Hadoop projects such as Apache Hive, Spark SQL, Cloudera Impala, Facebook Presto, Apache Tajo, and Apache Drill aim to compete with commercial data warehouse systems.

### Stars and Snowflakes: Schemas for Analytics

#### Star Schema Concept

- **Star Schema**: A common structure in data warehouses with a central fact table surrounded by dimension tables.
- **Fact Table**: Each row represents an event at a particular time (e.g., a customer's purchase of a product).
- **Dimension Tables**: Represent the "who, what, where, when, how, and why" of the event, containing details like product, time, customer, etc.

#### Key Features

- **Attributes of the Fact Table**: Includes attributes related to the event, such as price and cost.
- **Foreign Keys in Dimension Tables**: Fact table rows reference details of each event using foreign keys to dimension tables.
- **Event Capture**: Capturing individual events allows maximum analysis flexibility. Large enterprises may have tens of petabytes of transaction history in their data warehouse.
- **Date and Time**: Represented using dimension tables to encode additional information like public holidays, allowing differentiation between holiday and non-holiday sales.

#### Schema Variations

- **Snowflake Schema**: Breaks dimensions down into subdimensions, making it more normalized. For example, separate tables for brands and product categories.
- **Normalization Level**: Snowflake schemas are more normalized, but star schemas are often preferred because they are easier for analysts to work with.

#### Table Characteristics in Data Warehouses

- **Wide Tables**: Fact tables often have over 100 columns, and dimension tables can also be wide, including all metadata relevant for analysis.
- **Example**: The `dim_store` table might include details of services offered at each store, store area, opening date, remodeling date, distance from the nearest highway, etc.

### Column-Oriented Storage

#### Problem Definition

- **Size of Fact Tables**: Fact tables often include trillions of rows and petabytes of data, requiring efficient storage and query processing.
- **Query Characteristics**: Typical data warehouse queries access only a few columns at a time (e.g., 4-5 columns), rather than the entire table.

#### Example Query (Example 3-1)

- **Purpose**: Analyze whether people are more inclined to buy fresh fruit or candy depending on the day of the week in 2013.
- **Required Columns**: `date_key`, `product_sk`, and `quantity`.
- **Query Characteristics**: Accesses many rows but only a few columns.

#### Row-Oriented Storage Problems

- **Data Layout**: In row-oriented storage, all values from one row are stored next to each other.
- **Processing Method**: Queries need to load entire rows into memory, parse them, and filter out unneeded columns, which can be slow.

#### Column-Oriented Storage Concept

- **Data Layout**: Store all values from each column together instead of all values from one row together.
- **Advantage**: Queries only need to read and parse the columns required, significantly reducing workload.
- **Data Reassembly**: Columns must be stored in the same order so that corresponding values from different columns can be reassembled into rows.

#### Example Application

- **Parquet**: A columnar storage format based on Google’s Dremel, supporting a document data model.

### Column Compression

#### Need for Compression

- **Improving Query Efficiency**: Loading only necessary columns from disk reduces disk throughput requirements.
- **Data Compression**: Column-oriented storage often lends itself well to compression due to repetitive patterns.

#### Bitmap Encoding

- **Basic Concept**: For columns with a small number of distinct values, create a separate bitmap for each value. Each bitmap has one bit per row, indicating the presence of the value.
- **Example**: A retailer may have billions of transactions but only 100,000 distinct products.
- **Efficiency**: Sparse bitmaps can be further compressed using run-length encoding.
- **Bitmap Query Examples**:
    - **IN Condition**: `WHERE product_sk IN (30, 68, 69)` -> Load three bitmaps and perform a bitwise OR operation.
    - **AND Condition**: `WHERE product_sk = 31 AND store_sk = 3` -> Load two bitmaps and perform a bitwise AND operation.

#### Column-Oriented Storage and Column Families

- **Misconception**: Column families in Cassandra and HBase are not truly column-oriented as they store all columns from a row together and do not use column compression.

### Memory Bandwidth and Vectorized Processing

- **Memory Bandwidth**: A major bottleneck for data warehouse queries is the bandwidth from disk to memory.
- **CPU Efficiency**: Efficiently using the bandwidth from main memory to the CPU cache, avoiding branch mispredictions, and using SIMD instructions.
- **Vectorized Processing**:
    - **Principle**: Process chunks of compressed column data that fit in the CPU’s L1 cache in a tight loop without function calls.
    - **Efficiency**: Such loops execute faster than those requiring many function calls and conditions.
    - **Operations on Compressed Data**: Perform operations like bitwise AND and OR directly on compressed column data chunks.

### Sort Order in Column Storage

#### Necessity of Row Sorting

- **Row Sorting**: Sorting data by columns independently would lose the correspondence between values in the same row. Data must be sorted as entire rows.
- **Query Optimization**: Sorting by `date_key` first, for example, allows faster queries for recent data.
- **Multiple Sort Keys**: Sorting by `product_sk` within `date_key` groups sales for the same product on the same day, aiding queries that filter by product within a date range.

#### Compression Effect

- **Primary Sort Key Compression**: Long sequences of repeated values in the primary sort key can be highly compressed using run-length encoding.
- **Other Sort Keys**: The second and third sort keys will not compress as well, but overall compression is still beneficial.

#### Multiple Sort Orders

- **Extended Idea**: Introduced by C-Store and adopted by Vertica, the same data is stored sorted in different ways to fit various query patterns.
- **Data Replication**: Data needs to be replicated to multiple machines anyway, so different sort orders can be used to enhance query performance.
- **Comparison with Row-Oriented Storage**: Unlike row-oriented storage that uses secondary indexes with pointers, column-oriented storage typically contains only column values.

### Writing to Column-Oriented Storage

#### Write Complexity

- **In-Place Updates**: Not feasible with compressed columns as inserting a row in the middle of a sorted table would require rewriting all column files.
- **Consistency Maintenance**: Rows are identified by their position within a column, so insertions must update all columns consistently.

#### Solution: LSM-Trees

- **LSM-Trees**: All writes first go to an in-memory store, then are merged with column files on disk and written in bulk.
- **Query Processing**: Queries examine both column data on disk and recent writes in memory, combining the two seamlessly. The query optimizer hides this distinction from the user.

### Aggregation: Data Cubes and Materialized Views

#### Materialized Views

- **Definition**: A materialized view is a copy of query results written to disk, unlike virtual views that are just query shortcuts.
- **Advantages**: Useful when the same aggregates are used by many queries, potentially improving read performance.
- **Disadvantages**: Needs to be updated when underlying data changes, making writes more expensive.

#### Data Cubes (OLAP Cubes)

- **Definition**: A grid of aggregates grouped by different dimensions. Each cell contains aggregates of a fact attribute for a particular combination of dimensions.
- **Example**: A two-dimensional table with dates along one axis and products along the other, each cell containing the aggregate (e.g., SUM) of all facts for that date-product combination.
- **Multi-Dimensional**: Often has more than two dimensions, with cells containing aggregates for combinations of multiple dimensions.

#### Advantages and Disadvantages

- **Advantages**: Certain queries become very fast as they have been precomputed.
- **Disadvantages**: Not as flexible as querying raw data, as it cannot calculate aggregates for conditions not included in the dimensions.


