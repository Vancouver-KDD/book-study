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
