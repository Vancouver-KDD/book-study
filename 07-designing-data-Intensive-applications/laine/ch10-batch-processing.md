**Batch processing systems (offline systems)**
- A batch processing system takes a large amount of input data, runs a job to process it, and produces some output data.
- often scheduled to run periodically (for example, once a day).
- The primary performance measure of a batch job is usually throughput (the time it takes to crunch through an input dataset of a certain size).
  
**Stream processing systems (near-real-time systems)**
- somewhere between online and offline/batch processing (so it is sometimes called near-real-time or nearline processing).
- Like a batch processing system, a stream processor consumes inputs and produces outputs (rather than responding to requests).
- However, a stream job operates on events shortly after they happen, whereas a batch job operates on a fixed set of input data.
- have lower latency than the equivalent batch systems.

**MapReduce**
- was called “the algorithm that makes Google so massively scalable”, and subsequently implemented in various open source data systems, including Hadoop, CouchDB, and MongoDB.

## Batch Processing with Unix Tools
### The Unix Philosophy
- design principles that became popular among the developers and users of Unix, described in 1978
- automation, rapid prototyping, incremental iteration, being friendly to experimentation, and breaking down large projects into manageable chunks
1. Make each program do one thing well. To do a new job, build afresh rather than complicate old programs by adding new “features”.
2. Expect the output of every program to become the input to another, as yet unknown, program. Don’t clutter output with extraneous information. Avoid stringently columnar or binary input formats.** Don’t insist on interactive input.**
3. Design and build software, even operating systems, to be tried early, ideally within weeks. Don’t hesitate to throw away the clumsy parts and rebuild them.
4. Use tools in preference to unskilled help to lighten a programming task, even if you have to detour to build the tools and expect to throw some of them out after you’ve finished using them.

- A Unix shell like bash lets us easily compose these small programs into surprisingly powerful data processing jobs.
- What does Unix do to enable this composability?

#### A uniform interface
- To connect any program’s output to any program’s input, that means that all programs must use the same input/output interface.
- In Unix, that interface is a file (or, more precisely, a file descriptor). - ASCII text.
   - A file is just an ordered sequence of bytes, very simple interface
   - many different things can be represented using the same interface:
      - an actual file on the filesystem, a communication channel to another process (Unix socket, stdin, stdout), a device driver (say /dev/audio or /dev/lp0), a socket representing a TCP connection. 

#### Separation of logic and wiring
- their use of standard input (stdin) and standard output (stdout). If you run a program and don’t specify anything else, stdin comes from the keyboard and stdout goes to the screen.
- Separating the input/output wiring from the program logic makes it easier to compose small tools into bigger systems.
- limit: You can’t pipe a program’s output into a network connection. If a program directly opens files for reading and writing, or starts another program as a subprocess, or opens a network connection, then that I/O is wired up by the program itself.
  
#### Transparency and experimentation
- they make it quite easy to see what is going on:
• The input files to Unix commands are normally treated as immutable.
  - This means you can run the commands as often as you want, trying various command-line options, without damaging the input files.
• You can end the pipeline at any point, pipe the output into less, and look at it to see if it has the expected form. This ability to inspect is great for debugging.
• You can write the output of one pipeline stage to a file and use that file as input to the next stage.
  - This allows you to restart the later stage without rerunning the entire pipeline.

## MapReduce and Distributed Filesystems
**- the biggest limitation of Unix tools is that they run only on a single machine**—and that’s where tools like Hadoop come in. 

**MapReduce** 
- a bit like Unix tools, but distributed across potentially thousands of machines.
- MapReduce jobs read and write files on a distributed filesystem.

**HDFS (Hadoop Distributed File System)**
- In Hadoop’s implementation of MapReduce, that filesystem is called HDFS, an open source reimplementation of the Google File System (GFS)
- shared-nothing principle, no need for special hardware
- HDFS consists of a daemon process running on each machine, exposing a network service that allows other nodes to access files stored on that machine (assuming that every general-purpose machine in a datacenter has some disks attached to it).
- HDFS has scaled well: at the time of writing, the biggest HDFS deployments run on tens of thousands of machines, with combined storage capacity of hundreds of petabytes
  - Such large scale has become viable because the cost of data storage and access on HDFS is much lower than that of the equivalent capacity on a dedicated storage appliance.

### MapReduce Job Execution
- MapReduce is a programming framework with which you can write code to process large datasets in a distributed filesystem like HDFS.

**pattern of data processing in MapReduce**
1. Read a set of input files, and break it up into records.
   - In the web server log example, each record is one line in the log (that is, \n is the record separator).
2. Call the mapper function to extract a key and value from each input record.
   - In the preceding example, the mapper function is awk '{print $7}': it extracts the URL ($7) as the key, and leaves the value empty.
3. Sort all of the key-value pairs by key.
   - In the log example, this is done by the first sort command.
4. Call the reducer function to iterate over the sorted key-value pairs. If there are multiple occurrences of the same key, the sorting has made them adjacent in the list, so it is easy to combine those values without having to keep a lot of state in memory.
   - In the preceding example, the reducer is implemented by the command uniq -c, which counts the number of adjacent records with the same key.

**To create a MapReduce job, you need to implement two callback functions**
- Mapper: prepare the data by putting it into a form that is suitable for sorting
   - is called once for every input record, and its job is to extract the key and value from the input record.
   - For each input, it may generate any number of key-value pairs (including none).
   - It does not keep any state from one input record to the next, so each record is handled independently.
- Reducer: to process the data that has been sorted.
   - After collecting all the values belonging to the same key by the mapper, and calls the reducer with an iterator over that collection of values.
   - The reducer can produce output records (such as the number of occurrences of the same URL).

#### Distributed execution of MapReduce
**MapReduce and paralleization**
- can parallelize a computation across many machines, different from Unix, without writing code to explicitly handle the parallelism. 
- The mapper and reducer only operate on one record at a time; they don’t need to know where their input is coming from or their output is going to, so the framework can handle the complexities of moving data between machines.

**Hadoop MapReduce job**
- Its parallelization is based on partitioning
- the input to a job is typically a directory in HDFS
- Map task:
  - each file or file block within the input directory is considered to be a separate partition that can be processed by a separate map task
  - MapReduce framework first copies the code (e.g., JAR files in the case of a Java program) to the appropriate machines.
  - starts the map task and begins reading the input file, passing one record at a time to the mapper callback.
  - The output of the mapper consists of key-value pairs.
- Reduce task:
  - The reducers connect to each of the mappers and download the files of sorted key-value pairs for their partition, preserving the sort order
  - shuffle: The process of partitioning by reducer, sorting, and copying data partitions from mappers to reducers

#### MapReduce workflows
**Chained MapReduce jobs**
- the output of one job becomes the input to the next job.
- this chaining is done implicitly by directory name:
  - the first job must be configured to write its output to a designated directory in HDFS, and the second job must be configured to read that same directory name as its input.
  - From the MapReduce framework’s point of view, they are two independent jobs.
- more like a sequence of commands where each command’s output is written to a temporary file, and the next command reads from the temporary file.

**How MapReduce handle a failed job**
- A batch job’s output is only considered valid when the job has completed successfully, and only when the prior job succeeded, the next job starts
- MapReduce discards the partial output of a failed job

### Reduce-Side Joins and Grouping
- A join is necessary whenever you have some code that needs to access records on both sides of that association (both the record that holds the reference and the record being referenced).
- MapReduce has no concept of indexes—at least not in the usual sense.
- joins in the context of batch processing, we mean resolving **all occurrences** of some association within a dataset.
  - For example, we assume that a job is processing the data for all users simultaneously, not merely looking up the data for one particular user (which would be done far more efficiently with an index).

#### Example: analysis of user activity events
Two databases for user activity events and user
- The simplest implementation: go over the activity events one by one and query the user database (on a remote server) for every user ID it encounters.
  - This is possible, but it would most likely suffer from very poor performance
**- Good performance implementation:**
  - to take a copy of the user database and to put it in the same distributed filesystem as the log of user activity events.
  - You would then have the user database in one set of files in HDFS and the user activity records in another set of files
  - then could use MapReduce to bring together all of the relevant records in the same place and process them efficiently.
  - because to achieve good throughput in a batch process, the computation must be (as much as possible) local to one machine

#### Sort-merge joins
- mapper output is sorted by key, and the reducers then merge together the sorted lists of records from both sides of the join.
- the reducer function is called once for every user ID, and thanks to the secondary sort, the first value is expected to be the date-of-birth record from the user database.
- The reducer stores the date of birth in a local variable and then iterates over the activity events with the same user ID, outputting pairs of viewed-url and viewer-age-in-years. 
- Since the reducer processes all of the records for a particular user ID in one go, it only needs to keep one user record in memory at any one time, and it never needs to make any requests over the network.

#### Bringing related data together in the same place
- Using the MapReduce programming model has separated the physical network communication aspects of the computation (getting the data to the right machine) from the application logic (processing the data once you have it).
  - contrasts with the typical use of databases
- MapReduce handles all network communication, it also shields the application code from having to worry about partial failures, such as the crash of another node:
  - retries failed tasks without affecting the application logic
 
#### GROUP BY
Usage 1 - grouping records by some key
- MapReduce sets up the mappers so that the key-value pairs they produce use the desired grouping key
- The partitioning and sorting process then brings together all the records with the same key in the same reducer.
- Thus, grouping and joining look quite similar when implemented on top of MapReduce.

Usage 2 - collating all the activity events for a particular user session
- in order to find out the sequence of actions that the user took a process called sessionization
  - i.e. such analysis could be used to work out whether users who were shown a new version of your website are more likely to make a purchase than those who were shown the old version (A/B testing),
  - or to calculate whether some marketing activity is worthwhile.

#### Handling skew
Hot keys
- The pattern of “bringing all records with the same key to the same place” breaks down if there is a very large amount of data related to a single key.
  - i.e. in a social network, most users might be connected to a few hundred people, but a small number of celebrities may have many millions of followers.
  - Collecting all activity related to a celebrity (e.g., replies to something they posted) in a single reducer can lead to **significant skew **(also known as hot spots)
  - meaning one reducer that must process significantly more records than the others

Algorithms to resolve the issue
- skewed join in Pig - first runs a sampling job to find hot keys and spread work oever several reducers for better parallelization
- Hive's skewed join - hot keys are specified in the DB table metatdata, and use map-side join for the hot keys

### Map-Side Joins
if you can make certain assumptions about your input data, it is possible to make joins faster
- uses a cut-down MapReduce job in which there are no reducers and no sorting.
- Instead, each mapper simply reads one input file block from the distributed filesystem and writes one output file to the filesystem

#### MapReduce workflows with map-side joins
- the choice of map-side or reduce-side join affects the structure of the output.
  - The output of a reduce-side join is partitioned and sorted by the join key
  - the output of a map-side join is partitioned and sorted in the same way as the large input
- Knowing about the physical layout of datasets in the distributed filesystem becomes important when optimizing join strategies:
  - the encoding format and the name of the directory in which the data is stored;
  - the number of partitions and the keys by which the data is partitioned and sorted.
  - In the Hadoop ecosystem, this kind of metadata about the partitioning of datasets is often maintained in HCatalog and the Hive metastore

#### Broadcast hash joins
where a large dataset is joined with a small dataset, small enough to be loaded entirely into memory in each of the mappers.
- i.e. first read the user database into an in-memory hash table, then mapper scan oer user activity events and look up user ID for each event in the hash table
- broadcast: each mapper for a partition of large input reads the entirety of the small input

#### Partitioned hash joins (bucketed map joins)
can be applied to each partition independently
- i.e. mapper 3 first loads all users with an ID ending in 3 into a hash table, then scans over all the activity events for each user whose ID ends in 3.
- only works if both of the join’s inputs have the same number of partitions, with records assigned to partitions based on the same key and the same hash function

#### Map-side merge joins
input datasets are not only partitioned in the same way, but also sorted based on the same key.
- it does not matter whether the inputs are small enough to fit in memory, because a mapper can perform the same merging operation that would normally be done by a reducer:
  - reading both input files incrementally, in order of ascending key, and matching records with the same key.
- probably means that prior MapReduce jobs brought the input datasets into this partitioned and sorted form in the first place

### The Output of Batch Workflows
what is the result of all of that processing, once it is done? 
Why are we running all these jobs in the first place?
- not transaction processing, nor is it analytics, closer to analytics
- The output of a batch process is some other kind of structure.

#### Building search indexes
perform a full-text search over a fixed set of documents
- a batch process is a very effective way of building the indexes:
  - the mappers partition the set of documents as needed, each reducer builds the index for its partition, and the index files are written to the distributed filesystem.

- if indexed set of documents changes, periodically rerun the entire indexing workflow for the entire set of documents
  - and replace the previous index files wholesale with the new index files when it is done.

#### Key-value stores as batch process output
Another common uses for batch processing
- build machine learning systems such as classifiers (e.g., spam filters, anomaly detection, image recognition)
- recommendation systems (e.g., people you may know, products you may be interested in, or related searches)
**- the output of those jobs is often some kind of database**

Good solution is to build a brand-new database inside the batch job
- and write it as files to the job’s output directory in the distributed filesystem, just like the search indexes.
- data files are then immutable once written, and can be loaded in bulk into servers that handle read-only queries.

#### Philosophy of batch process outputs
The handling of output from MapReduce jobs follows the same philosophy from Unix philosophy. 
- By treating inputs as immutable and avoiding side effects (such as writing to external databases),
- batch jobs not only achieve good performance but also become much easier to maintain:
• If you introduce a bug into the code and the output is wrong or corrupted, you can simply roll back to a previous version of the code and rerun the job. Or, even simpler, you can keep the old output in a different directory and simply switch back to it.
• with the ease of rolling back, feature development can proceed more quickly than in an environment where mistakes could mean irreversible
damage.
• If a map or reduce task fails, the MapReduce framework automatically reschedules it and runs it again on the same input.
• The same set of files can be used as input for various different jobs, including monitoring jobs that calculate metrics and evaluate whether a job’s output has the expected characteristics
• Like Unix tools, MapReduce jobs separate logic from wiring (configuring the input and output directories), which provides a separation of concerns and enables potential reuse of code

### Comparing Hadoop to Distributed Databases
**massively parallel processing (MPP) databases**
- MPP databases focus on parallel execution of analytic SQL queries on a cluster of machines
- the combination of MapReduce and a distributed filesystem provides something much more like a general-purpose operating system that can run arbitrary programs.

#### Diversity of storage
- MPP databases typically require careful up-front modeling of the data and query pat‐terns before importing the data into the database’s proprietary storage format.
- Hadoop indiscriminately dumping data into HDFS, and only later figuring out how to process it further
  - it shifts the burden of interpreting the data: instead of forcing the producer of a dataset to bring it into a standardized format, the interpretation of the data becomes the consumer’s problem (the schema-on-read approach)
  - good if the producer and consumers are different teams with different priorities.

**sushi principle**
- raw data is better
- Simply dumping data in its raw form allows for several such transformations.

#### Diversity of processing models
not all kinds of processing can be sensibly expressed as SQL queries, as MPP. 
  - general model of data processing is needed for:
      - machine learning and recommendation systems, or full-text search indexes with relevance ranking models, or performing image analysis

**With HDFS and MapReduce, you can build a SQL query execution engine**
- but it was not enough, and more different models were needed
- due to the openness of the Hadoop platform, it was feasible to implement a whole range of approaches, not possible for a monolithic MPP database

#### Designing for frequent faults
- Batch processes are less sensitive to faults because they do not immediately affect users if they fail and they can always be run again.
- node failure: most MPP databases abort the entire query, and either let the user resubmit the query or automatically run it again

- The MapReduce approach is more appropriate for larger jobs
    - jobs that process so much data and run for such a long time that they are likely to experience at least one task failure along the way
    - Is it really worth incurring significant overheads for the sake of fault tolerance?

**Why MapReduce's sparing use of memory and task-level recovery**
- because the freedom to arbitrarily terminate processes enables better resource utilization in a computing cluster.
- from looking at the environment for which MapReduce was originally designed in Google
  - Every task has a resource allocation (CPU cores, RAM, disk space, etc.), a priority that determines pricing of the computing resources
  - as MapReduce jobs run at low priority, they run the risk of being preempted at any time because a higher-priority process requires their resources.
- batch job: “pick up the scraps under the table,”

## Beyond MapReduce
MapReduce is very robust but slow
- you can use it to process almost arbitrarily large quantities of data on an unreliable multi-tenant system with frequent task terminations, and it will still get the job done (albeit slowly).
- On the other hand, other tools are sometimes orders of magnitude faster for some kinds of processing.

### Materialization of Intermediate State
- every MapReduce job is independent from every other job. The main contact points of a job with the rest of the world are its input and output directories on the distributed filesystem.

_**intermediate state**_
- But in many cases, the output of one job is only ever used as input to one other job, which is maintained by the same team
   - a means of passing data from one job to the next.
   - For a complex system with 50 or 100 MapReduce jobs, there is a lot of such intermediate state.
- The process of writing out this intermediate state to files is called materialization.

**Unix pipes (in contrast to materialization)**
- Unix pipes to connect the output of one command with the input of another.
- Pipes do not fully materialize the intermediate state, but instead stream the output to the input incre‐ mentally, using only a small in-memory buffer.

**MapReduce's downsides compared to this**
- A MapReduce job can only start when all tasks in the preceding jobs (that generate its inputs) have completed, whereas processes connected by a Unix pipe are started at the same time, with output being consumed as soon as it is produced.
- Mappers are often redundant: they just read back the same file that was just written by a reducer, and prepare it for the next stage of partitioning and sorting. In many cases, the mapper code could be part of the previous reducer
- Storing intermediate state in a distributed filesystem means those files are repli‐ cated across several nodes, which is often overkill for such temporary data.

#### Dataflow engines
**_Dataflow engines_ - Spark, Tez, and Flink**
- In order to fix these problems with MapReduce, several **new execution engines** for distributed batch computations were developed
- they handle an entire workflow as one job, rather than breaking it up into independent subjobs.
- execute significantly faster due to the optimizations described below
- Operators: Unlike in MapReduce, these functions need not take the strict roles of alternating map and reduce, but instead can be assembled in more flexible ways

**Advantages**
• Expensive work such as sorting need only be performed in places where it is actually required, rather than always happening by default between every map and reduce stage.
• There are no unnecessary map tasks, since the work done by a mapper can often be incorporated into the preceding reduce operator (because a mapper does not change the partitioning of a dataset).
• Because all joins and data dependencies in a workflow are explicitly declared, the scheduler has an overview of what data is required where, so it can make locality optimizations.
• It is sufficient for intermediate state between operators to be kept in memory or written to local disk, which requires less I/O than writing it to HDFS (where it must be replicated to several machines and written to disk on each replica).
• Operators can start executing as soon as their input is ready; no need to wait for the entire preceding stage to finish before the next one starts.
• Existing Java Virtual Machine (JVM) processes can be reused to run new operators, reducing startup overheads compared to MapReduce (which launches a new JVM for each task).

#### Fault tolerance
- fault tolerance is fairly easy in MapReduce: if a task fails, it can just be restarted on another machine and read the same input again from the filesystem.

The new systems
- if a machine fails and the intermediate state on that machine is lost, it is recomputed from other data that is still available
- To enable this recomputation, the framework must keep track of how a given piece of data was computed
- In order to avoid such cascading faults, it is better to make operators deterministic.
- Recovering from faults by recomputing data is not always the right answer
   - if the intermediate data is much smaller than the source data, or if the computation is very CPU-intensive, it is probably cheaper to materialize the intermediate data to files than to recompute it.
