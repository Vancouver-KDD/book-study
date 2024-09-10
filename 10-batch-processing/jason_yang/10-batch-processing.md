**Services (Online Systems)**
- These systems receive requests from clients and respond as quickly as possible. Response time and availability are key performance indicators. If there is a delay or the system is down, users may experience errors.

**Batch Processing Systems (Offline Systems)**
- These systems process large data sets to generate output data. Batch jobs can take anywhere from a few minutes to several days to complete and are run periodically. A key performance indicator is throughput, which is the amount of time it takes to process a certain amount of data.

**Stream Processing Systems (Near-Real-Time Systems)**
- Stream processing sits between online systems and batch processing systems. It processes data as it arrives, with lower latency than batch systems. It’s used to handle real-time or near-real-time data.

## Batch Processing with Unix Tools

Let's start with a simple example. Imagine a web server adds a line to a log file every time it handles a request. For example, in the default nginx access log format, one line might look like this:

```
216.58.210.78 - - [27/Feb/2015:17:55:11 +0000] "GET /css/typography.css HTTP/1.1" 200 3377 "http://martin.kleppmann.com/" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214. Safari/537.36"
```

(This is actually one line; I’ve broken it up here for readability.)

This line contains a lot of information. To interpret it, we need to look at the log format definition, which looks like this:

```
$remote_addr - $remote_user [$time_local] "$request" $status $body_bytes_sent "$http_referer" "$http_user_agent"
```

So, this line of the log indicates that on February 27, 2015, at 17:55:11 UTC, the server received a request from the client IP address 216.58.210.78 for the file `/css/typography.css`. Since the user was not authenticated, the `$remote_user` is set to a hyphen (-). The response status was 200 (meaning the request was successful), and the response size was 3,377 bytes. The web browser was Chrome 40, and the file was loaded as a reference from the page http://martin.kleppmann.com/.

> **Note:** Some people may point out that using `cat` here is unnecessary because you could pass the input file directly to `awk`. However, this setup makes the linear pipeline clearer.

### Simple Log Analysis

Here is an example of finding the top 5 most popular pages on a website using Unix shell commands to analyze the log file:

```
cat /var/log/nginx/access.log |  (1)
awk '{print $7}' |              (2)
sort |                          (3)
uniq -c |                       (4)
sort -r -n |                    (5)
head -n 5                       (6)
```

1. Read the log file,
2. Extract the URL information from each line,
3. Sort the list of URLs,
4. Count the unique URLs and their occurrences,
5. Sort the result in descending order by the count,
6. Output the top 5 URLs.

This command chain is very efficient and can handle large log files quickly. Additionally, with a few simple modifications, it can be adapted to meet various analysis needs. Unix tools make it possible to combine small tasks into powerful and fast data analysis.


#### Chain of Commands vs. Custom Program

Instead of using a Unix command chain, you can also write a simple program in a language like Ruby. For example, with Ruby code, you could store URLs in a hash table, count their occurrences, and then print out the top 5 most requested URLs. While this method may be a bit more complex than using a Unix command chain, it results in code that is often clearer and easier to read. Which approach you choose depends on your personal preference.

#### Sorting vs. In-Memory Aggregation

A Ruby script would use a hash table to count the occurrences of URLs in memory, whereas a Unix pipeline uses a sorted list of URLs to calculate the same counts. 

Which approach is better depends on the size of the data you are dealing with. Most small to medium-sized websites can store all unique URLs and their counts in memory. However, if memory is limited, the Unix sorting approach might be better as it efficiently uses disk space.

This sorting method is scalable and can handle large datasets effectively, even those that are too big to fit into memory.

### The Unix Philosophy

The Unix philosophy is the principle of designing each program to do one thing well and enabling data to be passed between programs flexibly. This concept was first introduced by Doug McIlroy in 1964 and is often described as connecting programs like garden hoses, allowing data to flow between them like water. This design principle is still relevant today, emphasizing breaking down complex tasks into smaller, manageable units.

The key principles are:
1. Design each program to do one thing well.
2. Make sure the output of a program can serve as the input to another.
3. Software should be flexible, allowing quick prototyping and easy rebuilding if needed.
4. Tools should simplify tasks and be disposable when no longer needed.

`sort` is a good example of this philosophy. On its own, `sort` is not particularly powerful, but when combined with other tools, it becomes a strong data processing utility. Bash, the Unix shell, allows you to combine small programs to perform complex tasks, and tools created by different developers can interact with each other flexibly. The strength of Unix lies in this ability to combine programs seamlessly.

#### A Uniform Interface

A key part of Unix systems is that different programs use the same input/output interface to exchange data. This interface is typically a file or file descriptor, which is just a sequence of bytes. As a result, various programs can interact with each other.

Because of this interface, files, communication channels, device drivers, and TCP connections can all be handled in the same way. Many Unix programs process this byte sequence as ASCII text, allowing different tools to interact with each other using the same format.

Although this ASCII text-based interface works well in most cases, it’s not always ideal. For example, in log analysis, a format like `{print $7}` may not be intuitive. However, this simple interface has allowed Unix tools to maintain their impressive interactivity even after decades.

This same unified interface concept can be seen on the web. Through URLs and HTTP, users can seamlessly navigate between different websites, and data can be shared between servers. This is one of the principles that made the web so successful.

#### Separation of Logic and Wiring

One of the important features of Unix tools is that they use standard input (stdin) and standard output (stdout). By default, a program receives input from the keyboard and outputs to the screen, but it can also read from files or connect to other processes. By using stdin and stdout, programs can connect their input and output flexibly. This allows programs to focus on logic without worrying about where the input comes from or where the output goes.

This approach separates program logic from input/output paths, making it easy to combine small tools into larger systems. Even if you write your own program, you can use it alongside Unix tools. Programs that participate in pipelines simply need to take input from stdin and send output to stdout.

However, there are limitations to using stdin and stdout. Programs that need to handle multiple inputs or outputs can find it challenging. When programs read files directly or open network connections, their flexibility is reduced.

#### Transparency and Experimentation

One of the factors contributing to the success of Unix tools is that users can easily see the progress of their work and experiment with it:

- Unix commands do not modify input files, so you can try different commands multiple times without damaging the original data.
- You can use tools like `less` to inspect intermediate results in a pipeline and verify that the output matches your expectations, making debugging easier.
- You can break a task into multiple steps, output each step to a file, and use that file as input for the next step, allowing you to rerun only the parts you need without having to rerun the entire pipeline.

In this way, Unix tools act as simple yet powerful tools for experimentation. However, they are limited to running on a single machine, which is why distributed system tools like Hadoop are used to overcome this limitation.

## MapReduce and Distributed Filesystems

MapReduce is similar to Unix tools in that it processes large amounts of data, but it differs in that it can operate across thousands of machines. MapReduce takes input data, processes it, and produces output data without modifying the input files during the process, recording the results sequentially.

While Unix tools handle input and output through stdin and stdout, MapReduce uses distributed filesystems like HDFS (Hadoop Distributed File System) to manage data. HDFS is an open-source version of the Google File System (GFS) and is designed to store and process large-scale data across multiple machines. Other distributed filesystems include GlusterFS, QFS, and Amazon S3, all of which operate on similar principles.

HDFS leverages commodity computers connected through a typical data center network, allowing it to distribute large datasets without relying on centralized storage, unlike shared disk systems. A central server called the NameNode manages where each file block is stored, and data redundancy is achieved by replicating files across multiple machines or using erasure coding to protect against failures.

HDFS is highly scalable, capable of handling hundreds of petabytes of data across tens of thousands of machines. By using commercial hardware and open-source software, it provides a cost-effective way to manage large datasets compared to dedicated storage devices.

### MapReduce Job Execution

MapReduce is a programming framework designed to process large amounts of data on distributed filesystems like HDFS. The data processing steps in MapReduce are similar to log file analysis:

1. The input file is divided into records. For example, in a web server log, each line could be a record.
2. The `Mapper` function extracts key-value pairs from each record. For instance, it might extract the URL as the key.
3. The extracted key-value pairs are then sorted by key.
4. The `Reducer` function processes the sorted key-value pairs and combines the results. If a key appears multiple times, it aggregates these values and outputs the final result.

This process makes up a MapReduce job, where each step of data processing is automatically executed. Users define the `Mapper` and `Reducer`, while MapReduce handles splitting the input file, sorting data, and combining results.

- **Mapper**: Prepares data in a format suitable for sorting.
- **Reducer**: Processes and combines sorted data.

### Distributed Execution of MapReduce

MapReduce is similar in concept to a Unix command pipeline but can execute tasks in parallel across multiple machines. The parallelism is handled automatically by the framework, so developers don't need to manage it explicitly. The `Mapper` and `Reducer` handle individual records without needing to know where the data comes from or where it’s going. The framework takes care of these complexities.

The workflow of a MapReduce job is as follows:
1. The input file is split into multiple blocks, each processed by a separate map task.
2. The MapReduce scheduler tries to execute map tasks on the machines where the data is located, reducing network load and optimizing performance.
3. The `Mapper` processes each input record and produces key-value pairs.
4. These key-value pairs are sent to the appropriate `Reducer` based on their hash values. Each `Reducer` collects and processes all values associated with the same key.
5. The process of moving and sorting data is known as the "shuffle," ensuring that data is sorted before it reaches the `Reducer`.
6. Finally, the `Reducer` processes the data and writes the results back to the distributed filesystem.

Throughout this process, parallel processing and data movement are managed automatically, so developers don’t have to worry about these details.

#### MapReduce Workflows

A single MapReduce job can only solve a limited range of problems. For example, while you can calculate page views for each URL with one MapReduce job, finding the most popular URL would require additional sorting. Therefore, it’s common to chain multiple MapReduce jobs together to create a workflow, where the output of one job serves as the input to the next.

Hadoop MapReduce doesn’t inherently support these workflow chains; instead, it’s managed through directory names. The first job writes output data to a specific directory, and the second job reads data from that directory. Unlike a Unix command pipeline, where data flows directly from one command to another, each step in a MapReduce workflow writes intermediate results to temporary files, which the next step reads.

Tools like Oozie, Azkaban, Luigi, Airflow, and Pinball have been developed to manage these large batch job workflows and handle dependencies between jobs. Hadoop tools like Pig, Hive, and Cascading also automate the chaining of multiple MapReduce steps into a coherent workflow.

### Reduce-Side Joins and Grouping

When one record in a dataset is related to another, you need a join to process that relationship. A join handles relationships between records, such as foreign keys, document references, or edges in a graph. While you can reduce the need for joins by denormalizing the data model, you can’t eliminate them entirely.

In databases, an index is used to efficiently find a few records, but in MapReduce, there are no indexes, so you need to scan the entire file. This is similar to a full table scan, which can be inefficient for small data queries. However, when analyzing large amounts of data, scanning the entire dataset may be more efficient, especially since it can be processed in parallel across many machines.

When we talk about joins in MapReduce, we mean processing the relationships between all records at once. For smaller queries, using an index would be more efficient, so we won’t cover that here.

#### Example: Analysis of User Activity Events

A typical example of a join in batch processing is shown in Figure 10-2. On the left, there is an event log that describes user activities on a website (also known as activity events or clickstream data), and on the right, there is a user database. This is similar to part of a star schema: the event log is the fact table, and the user database is one of the dimensions.

![User Activity and Profile Data Join](https://i.imgur.com/w6IjswA.png)
**Figure 10-2.** Joining user activity event logs with a user profile database.

An analysis might need to correlate user activity with user profile information. For example, if the profile includes the user's age or birth date, the system could determine the most popular pages for a particular age group. However, the activity events only include the user ID and not the full profile information. Including the full profile in every activity event would be wasteful, so the activity events need to be joined with the user profile database.

A simple implementation of this join would be to process each activity event and send a query to the user database (on a remote server) for every user ID encountered.

- While possible, this approach is likely to perform poorly:
  - The processing speed will be limited by the round-trip time to the database server.
  - The effectiveness of a local cache will heavily depend on the distribution of data.
  - Executing many queries in parallel could easily overload the database.

To achieve good throughput in batch processing, as much computation as possible should happen locally on one machine.
- Making random access requests over the network for every record is too slow.
- Additionally, querying a remote database makes the batch job non-deterministic because the remote database's data can change.

A better approach is to take a snapshot of the user database (e.g., using an ETL process to extract data from a database backup) and place it in the same distributed filesystem as the user activity event log. The user database would then reside in one set of files in HDFS, and the user activity records in another set, allowing MapReduce to efficiently bring related records together and process them.



#### Sort-Merge Joins

In a sort-merge join, the Mapper extracts the key and value from each input record, partitions and sorts the data by the key, which in this case is set to the user ID. The activity events and user data are sorted by this key, and all records with the same user ID are sent to the same Reducer.

A technique called **secondary sort** is used here. This ensures that the Reducer first sees the user profile information (e.g., birthdate) followed by the user's activity events in timestamp order. The Reducer can then combine the user profile and activity events to perform tasks such as calculating the age of a user who visited a specific URL.

Because the Reducer processes all the data for a specific user ID at once, it uses memory efficiently and can perform all tasks locally without needing to make network requests. This method is known as a **sort-merge join**, where the Mapper sorts the data by key so that the Reducer can merge it easily.

#### Bringing Related Data Together in One Place

In a sort-merge join, the Mapper and the sorting process work to gather the data needed for a join into a single Reducer call. Since the data is pre-sorted, the Reducer can process the records efficiently using a single thread, with high throughput and minimal memory overhead.

Think of this process as the Mapper "sending a message" to the Reducer. The key in the key-value pair produced by the Mapper acts like a destination address that determines where the value should go. All key-value pairs with the same key are sent to the same Reducer.

MapReduce separates the handling of network communication from the application logic. This is different from applications where database requests are handled directly in the code. MapReduce automatically handles network communication, so if a node fails, the framework transparently retries the task without the application code needing to worry about it.

#### GROUP BY

Another common use case for bringing related data together, though not a join, is **GROUP BY**. In this pattern, all records with the same key are grouped together, and aggregation tasks are performed within each group.

Examples of such tasks include counting the number of records in the group (`COUNT(*)`), summing the values of a specific field (`SUM(fieldname)`), or performing ranking tasks such as selecting the top k records.

To perform such grouping tasks in MapReduce, the Mapper sets the key-value pairs for each record. Through partitioning and sorting, all records with the same key are gathered by the same Reducer, where the aggregation is done.

Another use case is **sessionization**, which involves grouping a user's activity events into a single session to analyze the sequence of their actions. This can be used to analyze the effectiveness of A/B tests or marketing activities.

To analyze user activity logs generated by multiple servers, a session cookie or user ID can be used as the grouping key. This ensures that all events for a particular user are sent to the same Reducer, while events for other users are distributed to other partitions.

This method enables efficient analysis of log data distributed across multiple servers.

#### Handling Skew

In some datasets, specific keys may have an excessive amount of data, which can cause **skew** (data imbalance), leading to a single Reducer being overwhelmed with too much data. For example, in a social network, a celebrity might generate a large volume of data. If all of this data is processed by one Reducer, it could significantly slow down the overall process.

To address this, techniques such as **skewed join** are used. For example, systems like Pig first sample the data to identify keys with too much data and then distribute the data for those keys across multiple Reducers. The other data is replicated and also processed by multiple Reducers.

This approach increases parallelism and improves performance, though it comes at the cost of replicating data. Similar techniques include **sharded join** or Hive's **skewed join**.

Another approach is to perform the aggregation in two stages. In the first stage, data is randomly distributed so that each Reducer handles smaller aggregations. Then, in the second stage, the results from these Reducers are combined to produce the final result.

This method is useful for effectively distributing large amounts of data and solving the **hot key** problem.

### Map-Side Joins

Earlier, we discussed **reduce-side joins**, where the actual join logic is handled by the Reducer, while the Mapper only prepares the data. This method works without assumptions about the data but comes with significant costs due to sorting and network communication. It may also involve writing data to disk multiple times during processing.

**Map-side joins** assume that the data is already sorted in a certain way, allowing the join to be performed directly in the Mapper. This method does not require Reducers or sorting. The Mapper reads each input block, performs the join, and writes the results directly to the output file, resulting in a simplified process.

In summary, **map-side joins** are an efficient way to join data when the input data meets certain conditions, greatly speeding up the process.

#### Broadcast Hash Joins

A simple type of **map-side join** is used when you are joining a large dataset with a small dataset. This works well when the small dataset can fit entirely into memory.

For example, if the user database is small enough to load into the memory of each Mapper, the Mapper can read this small dataset from the distributed filesystem at the start of the job and load it into memory as a hash table. As the Mapper processes user activity events, it looks up the user ID in the hash table to perform the join.

This method is called a **broadcast hash join** because the small dataset is "broadcast" to each Mapper, which then uses a hash table to perform the join. This technique is supported in Pig, Hive, Cascading, Crunch, and many other data warehouse query engines.

Alternatively, instead of loading the small dataset into memory, you could use a read-only index stored on the local disk, which allows fast lookups while using less memory.

#### Partitioned Hash Joins

**Partitioned hash join** is a method used when the input data for a map-side join is partitioned in the same way. For example, you could partition both the activity events and the user database by the last digit of the user ID. This would give each dataset 10 partitions. The Mapper would then load only the users whose IDs end with a certain number into its hash table and process only the activity events with matching IDs.

When partitioning is done correctly, the records to be joined end up in the same partition, so each Mapper only needs to process the partition assigned to it. This reduces the amount of data each Mapper needs to load into memory.

This approach only works when both inputs have the same number of partitions and are partitioned using the same hash function. If a previous MapReduce job has already grouped the data in this way, this method can be very effective.

In Hive, this method is known as **bucketed map joins**.


#### Map-Side Merge Joins

A map-side merge join is possible when the input datasets are partitioned in the same way and are also sorted by the same key. In this case, the input datasets do not need to be fully loaded into memory because the Mapper can perform the merging work that the Reducer would typically handle. Essentially, the Mapper reads both input files in ascending key order and matches records with the same key.

Map-side merge joins are useful when the input datasets have already been partitioned and sorted in a previous MapReduce job. While this join could technically be handled during the Reducer phase of that earlier job, if the partitioned and sorted datasets will be used for other purposes, it might be more efficient to handle the merge join in a separate **map-only** job.

This approach is particularly effective when working with sorted datasets, and it is helpful if you can reuse the output from previous jobs.

#### MapReduce Workflows with Map-Side Joins

When the output of a MapReduce join is used in a subsequent job, choosing between a **map-side join** and a **reduce-side join** affects the structure of the output. The output of a reduce-side join is partitioned and sorted by the join key, while the output of a map-side join is partitioned and sorted in the same way as the large input file, because each **map task** processes a block of the large input file.

Map-side joins require more assumptions about the size, sorting, and partitioning of the input datasets. Therefore, understanding how the datasets are physically laid out in the distributed file system is key to optimizing the join strategy. It is not enough to simply know the directory names or file formats; you need to know how many partitions there are and what keys the datasets are partitioned and sorted by.

In the Hadoop ecosystem, metadata about these partitioned datasets is managed by tools like **HCatalog** and the **Hive metastore**.

### The Output of Batch Workflows

We've discussed various MapReduce workflows, but one important question remains: What is the result of all this processing? Why are we running these jobs?

In databases, we distinguish between transactional processing (OLTP) and analytics. OLTP uses indexes to retrieve a small number of records, while analytics involves scanning large numbers of records to produce reports that guide business decisions.

Batch processing is closer to analytics because it involves scanning large datasets, but the output of MapReduce jobs is often not a report—it’s something different.

#### Building Search Indexes

Google originally used MapReduce to build search engine indexes. This involved a workflow of 5–10 MapReduce jobs. Building a search index is also a key task in tools like Lucene/Solr.

A **full-text search index** is a file that allows keywords to be efficiently looked up, providing a list of document IDs that contain those keywords. Batch processing is very effective for building such indexes. Mappers divide the document set, and Reducers build an index for each partition and write it to the distributed file system.

Once the index is built, it is used for read-only operations and doesn’t change. When updates are needed, the entire indexing process can be rerun or updated incrementally. Lucene handles this by writing new segment files and merging them in the background.

#### Key-Value Stores as Batch Process Output

Beyond search indexes, batch processing output is often used to build machine learning systems, such as spam filters, anomaly detection, image recognition, and recommendation systems. The output of these batch jobs is often stored in a special database that the web application can query.

While the most intuitive approach might be to write data directly to the database during the batch job, this can lead to performance issues and overload the database. There is also a risk of partial data corruption if the job fails.

A better approach is to generate a new database file within the batch job, treat it as immutable, and then load it into a read-only server for querying. Key-value stores like Voldemort, Terrapin, ElephantDB, and HBase are designed to handle this type of batch-generated data.

This method ensures that existing data remains unchanged during processing, and the new data file can be atomically swapped in, improving reliability and stability.

#### Philosophy of Batch Process Outputs

The "Unix Philosophy," discussed earlier, emphasizes experimentation and repetition when processing data. Programs read inputs, generate outputs, and do not modify the input data. This allows jobs to be rerun multiple times without damaging the system if something goes wrong.

MapReduce follows the same philosophy. The input data is treated as immutable, and direct writes to external databases are avoided. This approach offers several advantages:

- If a bug generates incorrect output, the job can simply be rerun with the previous version of the code.
- MapReduce automatically retries failed jobs, ensuring system stability.
- By separating code logic from data flow configuration, teams can focus on their respective roles.

In conclusion, both Unix and Hadoop share a philosophy that emphasizes the immutability of input data and the reversibility of jobs, contributing to the overall stability and maintainability of the system.

### Comparing Hadoop to Distributed Databases

Hadoop is like a distributed version of Unix. HDFS acts as the file system, and MapReduce serves as an unusual implementation of Unix processes, with sorting automatically performed between stages. This allows for the implementation of various joins and grouping operations.

However, when MapReduce was first introduced, the concept was not entirely new. Distributed massively parallel processing (MPP) databases had already been providing parallel join algorithms and other features for over a decade. Examples include the Gamma database machine, Teradata, and Tandem NonStop SQL.

The key difference is that MPP databases focus on the parallel execution of SQL queries, while the combination of MapReduce and distributed file systems offers the flexibility to run arbitrary programs, much like a general-purpose operating system.

#### Diversity of Storage

Databases require data to be structured according to a specific model (e.g., relational or document-based), but files in a distributed file system are simply **sequences of bytes** and can be written using any data model or encoding. These files might be collections of database records, but they could also be text, images, videos, sensor readings, sparse matrices, feature vectors, genomic sequences, or other types of data.

In simple terms, Hadoop opens up the possibility of dumping raw data into HDFS first and deciding later how to process it [53]. In contrast, MPP databases require **prior modeling** of the data and query patterns before importing the data into the database's proprietary storage format.

From a purist's perspective, this careful modeling and data import process might seem ideal because it ensures higher quality data for database users. However, in reality, simply making **messy and hard-to-handle raw data** quickly accessible is often more valuable than trying to define the ideal data model upfront [54].

This concept is similar to **data warehousing** (see the "Data Warehousing" section on page 91): the mere act of bringing together data from various parts of a large organization is valuable because it allows for joins between datasets that were previously isolated. The careful **schema design** required by MPP databases slows down centralized data collection, but collecting data in its raw form and considering schema design later speeds up the process (this concept is often referred to as a "datalake" or "enterprise data hub" [55]).

Dumping raw data shifts the burden of data interpretation. Instead of forcing the data producer to standardize the data, it becomes the responsibility of the **consumer** to interpret it (known as the **schema-on-read** approach [56], as discussed on page 39, "Schema Flexibility in the Document Model"). This can be advantageous when the producer and consumer are different teams with different priorities. There may not be one ideal data model—instead, there could be **multiple views of the data** that serve different purposes. Dumping the data in raw form allows for these various transformations. This approach is sometimes called the **sushi principle**: "raw data is better" [57].

Hadoop is often used to implement **ETL processes** (see "Data Warehousing" on page 91): data from **transaction processing systems** is dumped in raw form into the distributed file system, and then MapReduce jobs are used to clean and transform the data into relational form, which is then imported into an **MPP data warehouse** for analysis. Data modeling still happens, but it is a separate step from data collection. This separation is possible because the distributed file system can support data encoded in any format.

#### Diversity of Storage

Databases need to structure data according to a specific model (e.g., relational or document-based), but files in a distributed file system (like HDFS) are simply sequences of bytes, and they can be written in any data model or format. These files can contain database records, text, images, videos, or any other data type.

Hadoop opens up the possibility of storing raw data in HDFS and deciding how to process it later. In contrast, MPP databases require careful modeling of data before it can be imported. While data modeling might seem ideal, in practice, it’s often more valuable to quickly collect and use complex raw data.

This approach is similar to a data warehouse, where data is collected quickly and processed as needed. The burden of interpreting the data shifts from the producer to the consumer, allowing for multiple views of the data to serve different purposes. This approach is called the "sushi principle," meaning "raw data is better."

Thus, Hadoop is often used in ETL processes, where raw data is collected from transaction processing systems, cleaned, transformed, and then loaded into an analytical data warehouse.

#### Designing for Frequent Faults

**MapReduce** and **MPP databases** differ in how they handle faults and use memory and disk. **MPP databases** restart the entire query if a fault occurs, while **MapReduce** handles faults at the individual task level, retrying only the failed tasks.

This fault tolerance approach is better suited for **MapReduce**, which often processes large data over long periods. However, **MapReduce** was designed to handle faults not due to hardware instability but because tasks can be preempted by higher-priority jobs. For example, in Google's **mixed-use datacenters**, lower-priority jobs may be stopped when higher-priority jobs need resources. **MapReduce** is designed to handle such unexpected shutdowns.

While **MapReduce** was built to handle these interruptions, in environments where preemption is rare, this design decision becomes less important.

## Beyond MapReduce

MapReduce became popular in the late 2000s, but it is just one of many models for distributed systems. Depending on the data's size, structure, and processing needs, other tools may be more suitable.

We’ve focused on MapReduce in this book because it's a useful learning tool. MapReduce provides a simple abstraction on top of a distributed file system, making it easy to understand but challenging to use. For example, you often need to implement join algorithms manually [37].

To address these difficulties, higher-level programming models like Pig, Hive, Cascading, and Crunch were built on top of MapReduce. Understanding how MapReduce works makes learning these tools easier. However, the performance issues with MapReduce aren’t solved by higher-level abstractions, and in certain tasks, performance may suffer.

MapReduce is robust but can be slow, and other tools can offer better performance for specific tasks. The rest of this chapter will explore alternatives for batch processing, and Chapter 11 will cover stream processing.

### Materialization of Intermediate State

All MapReduce jobs are independent of each other, and the main connection point is the input and output directories in the distributed file system. To use the output of one job as the input for another, the input directory for the second job must match the output directory of the first job, and a workflow scheduler must ensure that the second job starts only after the first job finishes.

This setup works well when the output of the first job is widely used or reused data. The jobs are loosely coupled without needing to know each other’s inputs and outputs. However, in most cases, the output of one job is used only as the input for the next job, meaning it’s just intermediate data. In complex workflows, there can be many such intermediate states.

Recording these intermediate states to files is called **materialization**, which means precomputing and storing the data.

By contrast, Unix pipes do not fully **materialize** the intermediate state but instead use small **in-memory buffers** to connect the output directly to the input.

**Materializing** intermediate states in MapReduce has some disadvantages:

- MapReduce jobs can only start after the previous one finishes, while Unix pipes run simultaneously. This can slow down the overall workflow.
- The Mapper often repeats unnecessary work by reading files written by the previous Reducer.
- Storing intermediate data in the distributed file system involves replicating the data across multiple nodes, which can be excessive for temporary data.

#### Dataflow Engines

New distributed batch computation engines like **Spark**, **Tez**, and **Flink** were developed to address the shortcomings of MapReduce. These engines treat the entire workflow as a single job rather than splitting it into independent sub-tasks. These systems are called **dataflow engines** because they model the flow of data through multiple processing stages.

**Dataflow engines** have several advantages:

- Expensive operations like sorting are only performed where necessary.
- Unnecessary **map tasks** are eliminated, simplifying the job.
- They explicitly declare data dependencies, allowing the scheduler to process data more efficiently.
- Intermediate state can be stored in memory or on local disk, reducing I/O costs compared to storing it in HDFS.
- **Operators** run as soon as their input is ready, so there’s no need to wait for the entire previous stage to finish.
- The reuse of **JVM** processes reduces job startup overhead.

These systems can perform the same computations as **MapReduce workflows** but faster, and their generalized **operators** make it easy to switch between different engines without rewriting the code.

**Tez** is a thin library that uses the YARN shuffle service, while **Spark** and **Flink** are larger frameworks with more features.

#### Fault Tolerance

One of the benefits of **materializing** intermediate states in a distributed file system is guaranteed durability. This makes **MapReduce** fault tolerance easy. If a task fails, it can simply be restarted on another machine, and the same input can be re-read from the file system.

**Spark**, **Flink**, and **Tez** implement fault tolerance differently because they don’t write intermediate states to **HDFS**. If a machine fails and the intermediate state is lost, they recover it by recalculating from the previous stage’s data or from the original input data stored in **HDFS**.

To enable this recalculation, the framework tracks the lineage of data computation. **Spark** uses the concept of a **resilient distributed dataset (RDD)** to accomplish this [61], and **Flink** stores **operator state** as checkpoints to allow for recovery in case of failure [66].

When recalculating data, it’s important to ensure that the computation is **deterministic**—meaning the same input will always produce the same output. If the computation is **nondeterministic**, lower-level **operators** may struggle to maintain consistency. In such cases, the system must re-execute dependent tasks to ensure correctness.

To avoid these issues, **operators** should be made **deterministic**. For example, **pseudorandom numbers** can be generated using a fixed seed to eliminate **nondeterminism**.

However, recalculating data is not always the best approach. When intermediate data is much smaller than the original input, or when the computation is very **CPU-intensive**, it might be cheaper to materialize the intermediate data to a file.

#### Discussion of Materialization

Returning to the **Unix analogy**, **MapReduce** is similar to writing the output of each command to temporary files, while **dataflow engines** are more like **Unix pipes**, which operate more directly. **Flink**, in particular, is designed around the concept of **pipelined execution**, where **operator** output is progressively passed on to the next **operators**, allowing processing to begin before the entire input is received.

In sorting operations, since the input record with the smallest **key** might arrive last, the system must receive the entire input before generating the output. Therefore, **operators** that require sorting may need to temporarily store data. However, other parts of the workflow can run in a **pipelined** manner.

Once the job is complete, the output must be moved to a durable location for users to access—this typically involves writing the output back to the distributed file system. Even when using a **dataflow engine**, **materialized datasets** are still stored in **HDFS** and used as input and final output for jobs. Like **MapReduce**, the input remains **immutable**, and the output is fully replaced. The improvement over **MapReduce** is that you don’t need to store intermediate states in the file system.


### Graphs and Iterative Processing

On page 49, we discussed modeling data using graphs and navigating **edges** and **vertices** with graph query languages. This approach is often used in OLTP-style applications to quickly find specific vertices.

However, graph analysis in batch processing is a bit different. Here, the focus is on analyzing the entire graph, often for **machine learning** applications like recommendation or ranking systems. For example, the **PageRank** algorithm estimates the popularity of a web page based on links from other pages, and this is used to determine the order of results in search engines.

Additionally, **dataflow engines** like Spark, Flink, and Tez arrange jobs in a **directed acyclic graph (DAG)**, but this represents the flow of tasks, not actual graph processing. In contrast, in graph processing, the data itself is structured as a graph. This can lead to some confusion in terminology.

Many graph algorithms work by following an **edge** to connect **vertices**, passing information along until certain conditions are met. For example, the **transitive closure** algorithm repeatedly follows relationships between locations to create a list of all places within North America.

While it’s possible to store graphs in a distributed file system, expressing the concept of "run until the iteration is complete" is difficult in **pure MapReduce**. Since **MapReduce** processes data in a single pass, iterative tasks can be inefficient. An external scheduler could rerun the batch process until the conditions are met, but this approach would still be inefficient.

#### The Pregel Processing Model

To optimize batch processing of graphs, the **Bulk Synchronous Parallel (BSP)** model has gained popularity. This model is implemented in systems like **Apache Giraph**, **Spark's GraphX API**, and **Flink's Gelly API**, and is also known as the **Pregel** model, named after the Google Pregel paper that popularized this approach.

Similar to how **MapReduce** Mappers send messages to Reducers, in the **Pregel** model, a **vertex** sends messages to other **vertices**. These messages typically travel along the graph’s **edges**.

In each iteration, a function is called for each **vertex**, processing all messages sent to that vertex. This process is similar to Reducer calls in **MapReduce**, but in the **Pregel** model, each **vertex** remembers its state from the previous iteration. As a result, the function only needs to process new messages, and if a **vertex** receives no messages, it doesn't need to do anything.

This model is similar to the **actor model**. Each **vertex** can be thought of as an **actor**, with its state and messages stored safely. Because all messages are processed within fixed rounds, messages are guaranteed to be delivered at the correct time in each iteration.

#### Fault Tolerance

In the **Pregel** model, **vertices** communicate only through messages, which improves performance. Messages are batched, reducing communication latency. Delays only occur between iterations, during which all messages are sent across the network.

The **Pregel** model ensures that each message is processed exactly once in the next iteration, even if messages are lost, duplicated, or delayed. Like **MapReduce**, the **Pregel** framework handles **fault tolerance** automatically, so developers don’t need to write complex error-handling code.

**Fault tolerance** is implemented through periodic checkpoints. After each iteration, the state of all **vertices** is saved to storage. If a node fails and its in-memory state is lost, the system can revert to the last checkpoint and restart the computation. If the algorithm is **deterministic**, only the lost part can be selectively recovered.

#### Parallel Execution

A **vertex** doesn’t need to know which physical machine it’s running on. When it sends a message to another **vertex**, it uses the **vertex ID**, and the framework partitions the graph, routing messages across the network to the correct **vertex**.

Since the programming model processes one **vertex** at a time, the framework is free to partition the graph as needed. Ideally, **vertices** that need to communicate frequently are placed on the same machine to reduce communication overhead, but this is difficult to achieve perfectly. As a result, graphs are often partitioned randomly by **vertex ID**, without grouping related **vertices**.

This leads to higher network overhead in graph algorithms, and the size of the messages exchanged between machines can grow larger than the original graph. This network overhead can significantly slow down distributed graph algorithms.

Therefore, if a graph fits into the memory of a single computer, single-machine or single-thread algorithms are often more efficient than distributed processes. Even if the graph doesn’t fit into memory but can be stored on disk, single-machine frameworks like **GraphChi** can be a good option. However, if the graph is too large for a single machine, distributed processing models like **Pregel** become necessary. Efficient parallelization of graph algorithms is still an active area of research.

### High-Level APIs and Languages

Since **MapReduce** first gained popularity, **execution engines** for large-scale distributed batch processing have evolved significantly. Today, infrastructures are robust enough to process petabytes of data across clusters of thousands of machines. With many physical operational challenges addressed, there is now greater focus on improving programming models, increasing efficiency, and expanding the range of problems that can be solved.

High-level languages and APIs like **Hive**, **Pig**, **Cascading**, and **Crunch** have become popular because writing **MapReduce** jobs directly is tedious. The introduction of **Tez** allowed these high-level languages to switch to a new **dataflow execution engine** without rewriting code. **Spark** and **Flink** have also introduced their own high-level **dataflow APIs**, inspired by **FlumeJava**.

These **dataflow APIs** are typically based on relational database-style operations, allowing for **joins**, **grouping**, **filtering**, and **aggregation** of datasets. Internally, they handle operations using **join** and **grouping algorithms**.

High-level interfaces not only make writing code easier but also allow for **interactive use**, where analysts can gradually build and test code in a shell to explore data. This style is particularly useful for exploring datasets and conducting experiments, reflecting part of the **Unix philosophy**.

Ultimately, these high-level interfaces improve both developer productivity and the efficiency of system-level job execution.

#### The Move Toward Declarative Query Languages

Instead of manually coding join operations, declaratively specifying joins using relational operators has the advantage of allowing the framework to analyze the join inputs and automatically select the best join algorithm. Systems like **Hive**, **Spark**, and **Flink** include cost-based query optimizers that can also reorder joins to minimize intermediate data sizes.

Choosing the right join algorithm has a significant impact on the performance of batch jobs, so automating this process is highly beneficial. Applications need only declare the required joins, and the query optimizer determines the best execution plan. This concept was first introduced on page 42.

However, **MapReduce** and the subsequent **dataflow** models are not fully declarative like **SQL**. **MapReduce** is based on **function callbacks**, where user-defined functions (**mappers** or **reducers**) are called for each record or group of records, and these functions can execute arbitrary code to determine the output. The benefit of this approach is that it leverages existing library ecosystems to perform a wide range of tasks.

While **MapReduce** systems offer the freedom to execute arbitrary code, **dataflow engines** improve performance by introducing declarative features. For example, when filtering or selecting fields is expressed declaratively, the query optimizer can leverage **column-oriented storage layouts** to read only the required columns from disk. Systems like **Hive**, **Spark DataFrames**, and **Impala** use **vectorized execution**, processing data in a **CPU cache-friendly** manner while minimizing function calls. **Spark** generates **JVM bytecode**, while **Impala** uses **LLVM** to generate native code.

By incorporating declarative features into high-level **APIs**, batch processing frameworks can achieve performance similar to **MPP databases**, while retaining the flexibility to execute arbitrary code.

#### Specialization for Different Domains

While the flexibility to execute arbitrary code is useful, having reusable implementations of common building blocks is advantageous when certain processing patterns are frequently repeated. Traditionally, **MPP databases** have served the needs of **business intelligence analytics** and **business reporting**, but this is only one of the many domains served by batch processing.

In recent years, statistical and numerical algorithms needed for **machine learning** applications have become an important domain. Applications like **classification systems** and **recommendation systems** rely on these algorithms. To support them, **Mahout** implements a variety of **machine learning** algorithms on top of **MapReduce**, **Spark**, and **Flink**, while **MADlib** provides similar functionality within **MPP databases** like **Apache HAWQ**.

Spatial algorithms, such as **k-nearest neighbors**, are also valuable, especially for finding nearby items in multidimensional space. These algorithms are also crucial for **genomic analysis algorithms**, which need to find strings that are similar but not identical.

Batch processing engines are increasingly being used to run distributed algorithms in many domains. As batch processing systems offer more built-in functionality and high-level declarative operators, and as **MPP databases** become more flexible, these systems are becoming more alike. Ultimately, they are all systems for storing and processing data.

