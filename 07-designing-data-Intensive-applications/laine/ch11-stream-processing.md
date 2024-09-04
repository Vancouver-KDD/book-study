### State, Streams, and Immutability
- The principle of immutability makes event sourcing and change data capture so powerful.
- mutable state and an append-only log of immutable events do not contradict each other
  - Whenever a state changes, it's the result of the events that mutated it over time.
  - the changelog: The log of all changes - represents the evolution of state over time.
  - application state: integration of an event stream over time
  - a change stream: when you differentiate the state by time
 
**changelog again**
- log of events to be your system of record
- any mutable state as being derived from it, easier to reason about the flow of data through a system.
- ELK log example
- The truth is the log. The database is a cache of a subset of the log.


#### Advantages of immutable events
Immutability in databases is an old idea
  - For example, accountants - append only ledger, not changing or erasing the incorrect transaction but add another one to compensate the mistake

**benefit of immutability in the database**
- accidentally deployed buggy code that writes bad data to a database, recovery is much harder if the code is able to destructively overwrite data.
  - With an append-only log of immutable events, much easier to diagnose what happened and recover from the problem.
- Immutable events also capture more information than just the current state.
  - Shopping card history for analytic purpose

#### Deriving several views from the same event log
- by separating mutable state from the immutable event log, you can derive several different read-oriented representations from the same log of events.
  - having multiple consumers of a stream

_**command query responsibliity segregation (CQRS)**_
- Flexibility by separating the form how data is written and read, allowing several different views
- You can translate data from a write-optimized event log to read-optimized application state
  - then normalization in DB is not too relevant: data to be written as it will be queried
- Note: Storing data is normally quite straightforward if you don’t have to worry about how it is going to be queried and accessed; since many of the complexities of schema design, indexing, and storage engines are the result of wanting to support certain query and access patterns

#### Concurrency control
- The biggest downside of event sourcing and change data capture
  - the consumers of the event log are usually asynchronous: mismatch of read and a write can happen

**Solution**
1. perform the updates of the read view synchronously with appending the event to the log. This requires a transaction to combine the writes into an atomic unit
2. event sourcing: deriving the current state from an event log also simplifies some aspects of concurrency control
  - an event is a self-contained description of a user action, requiring only a single write in one place, appending the events to the log
3. Partitioning the event log and the application state in the same place
  -  a straightforward single-threaded log consumer needs no concurrency control for writes — it only processes a single event at a time

#### Limitations of immutability
Many systems that are not an event-sourced model also rely on immutability
  - version control systems: Git, Mercurial etc, preserving version history of files

**How much can an immutable history of all changes be kept forever?**
- depending on the amount of churn in the dataset

**When you need data to be deleted for administrative reasons**
- user privacy
- Not just prepending the delete log, but you actually want to rewrite history and pretend that the data was never written in the first place.
- truly deleting data is hard, since copies live in many places - storage engines, filesystems, and SSD

## Processing Streams
Now what can we process the stream once you have it?
- So far, where streams come from (user activity events, sensors, and writes to databases), and how streams are transported (through direct messaging, via message brokers, and in event logs).

1. write it to a database, cache, search index, or similar storage system, from where it can then be queried by other clients
2. push the events to humer users in some way
   - for example by sending email alerts or push notifications, or by streaming the events to a real-time dashboard where they are visualized.
3. process one or more **input** streams to produce one or more **output** streams.
   - Streams may go through a pipeline consisting of several such processing stages before they eventually end up at an output
   - **(operator or a job)** : a piece of code that process this
   - closely related to the Unix processes and MapReduce jobs and the pattern of dataflow is similar:
       - a stream processor consumes input streams in a read-only fashion and writes its output to a different location in an append-only fashion.

**Crucial difference to batch job - Stream never ends.**
- With the unbounded dataset, sorting doesn't make sense - sort-merge joins can't be used
- fault-tolerance: stream job running for years, restarting from the beginning after a crash is not an option
- similarity: basic mapping operation - transforming and filtering records, patterns for partitioning and parallelization in stream processor

### Uses of Stream Processing
monitoring purposes 
- fraud detection, trading systems to examine price changes, Manufacturing systems to monitor malfunction, Military and intelligence systems to track signs of an attack

#### Complex event processing
- an approach for analyzing event streams for certain event pattern - similar to regular expression search
- use a high-level declarative query language like SQL, or GUI
- queries are stored long-term, and events from the input streams continuously flow past them in search of a query that matches an event pattern
  - Looker events dashboard

#### Stream analytics
analytics tends to be less interested in finding specific event sequences than CEP
- is more oriented toward aggregations and statistical metrics over a large number of events
- examples: measuring the occurrence rate of some events, calculating the rolling average of a value over some time period, comparing the current stats to previous time interval
- _**window**_: The time interval over which you aggregate
- uses probabilistic algorithms: approximate the results, with way less memory in the stream processor - for optimization
- All designed with analytics in mind: Apache Storm, Spark Streaming, Flink, Concord, Samza, and Kafka Streams [74]. Hosted services include Google Cloud Dataflow and Azure Stream Analytics.

#### Maintaining materialized views
- materialized views: a stream of changes to a database can be used to keep derived data systems, such as caches, search indexes, and data warehouses, up to date with a source database.
- In principle, any stream processor could be used for materialized view maintenance
  - although the need to maintain events forever runs counter to the assumptions of some analytics-oriented frameworks that mostly operate on windows of a limited duration.

#### Search on streams
- i.e. media monitoring services subscribe to feeds and search for any news mentioning companies, real estate website notified for a new property 
- Searching a stream turns the processing on its head: the queries are stored, and the documents run past the queries, like in CEP
  - In contrast, conventional search engines first index the documents and then run queries over the index.

#### Message passing and RPC
- a mechanism for services to communicate
- there is some crossover area between RPC-like systems and stream processing
   - Apache Storm has a feature called distributed RPC, allowing user queries to be farmed out to a set of nodes that also process event streams
   - these queries are then interleaved with events from the input streams, and results can be aggregated and sent back to the user.
- possible to process streams using actor frameworks - but not fault-tolerant

### Reasoning About Time
Many stream processing frameworks use the local system clock on the processing machine to determine windowing
  - What if there is any significant processing lag? processing time != event time
  - Batch processing doesn't care about the system clock because always processing time != event time

**Event time versus processing time**
- Star Wars movie example: The episode number is the event timestamp, and when you watched the movie is the processing time.
- message delay from many reasons, lead to unpredictable ordering of messages

#### Knowing when you’re ready
- you can never be sure when you have received all of the events for a particular window, or whether there are some events still to come.
_**how to handle straggler events**_
  - ignore and track the number of dropped events and alert when needed
  - publish a correction - retracting the previous output
- adding a special message (i.e. log) - From now on there will be no more messages with a timestamp earlier than t

#### Whose clock are you using, anyway?
- For example, a mobile app that reports events for usage metrics to a server.
- clock on a user-controlled device often cannot be trusted, as it may be accidentally or deliberately set to the wrong time
- The time at which the event was received by the server is more accurate, but not describing user interaction

**To adjust for incorrect device clocks, one approach is to log three timestamps**
1. The time at which the event occurred, according to the device clock
2. The time at which the event was sent to the server, according to the device clock
3. The time at which the event was received by the server, according to the server clock
- 3 - 2 = the offset of the device clock and the server clock, use it to get the correct 1

#### Types of windows
window can be used for aggregations, for example to count events, or to calculate the average of values within the window. 
- Tumbling window: fixed length, every event belong to only one
- Hopping window: fixed length, allows windows oeverlap with each other. by aggregating tumbling windows
- Sliding window:
- Session window: no fixed duration, defined by grouping all events from the same user closely together in time, and ends if inactive for some time

### Stream Joins
- stream processing generalizes data pipelines to incremental processing of unbounded datasets - exactly the same need for joins on streams.
- new events can appear anytime on a stream makes joins on streams more challenging than in batch jobs

#### Stream-stream join (window join)
Advertising systems, to detect click-through rate for each URL - whenever someone types a search query and clicks a search result, record the events connected
- the time between two actions can vary
- keep the same session ID, but different state is maintained for each

#### Stream-table join (stream enrichment)
- For example, user activity events (stream) and user profile database
  - Input: activity events with a user ID
  - Output: a stream of activity events that user ID has been augmented with profile information - **enriching**
- How?
  - load a copy of the database into the stream processor so that it can be queried locally without a network round-trip.
  - DB content changes over time so the copy should be up to date, whereas batch job use a point-in-time snapshot of DB
- similar to the one above, but this doesn't use window

#### Table-table join (materialized view maintenance)
Twitter timeline example
- timeline cache maintenance in a stream processor, streams of events for tweets and for follow relationships
  - it needs to maintain a db containing this information
  - maintains a materialized view for a query that joins two tables
- The timelines are effectively a cache of the result of the query, corresponding directly to the join of the table, updated every time the underlying tables change

#### Time-dependence of joins
- If events on different streams happen around a similar time, in which order are they processed? - user updating profile example
  - if state changes over time, and you join with some state, what point in time do you use for the join

_**slowly changing dimension (SCD) issue**_
  - i.e. if you sell things, you should apply the right tax rate at the time of the same, which can be different from the current tax rate
  - addressed by using a unique identifier for a particular version of join record
  - downside: log compaction is not possible to retain all versions of the records in the table

### Fault Tolerance
_**exactly-once semantics, or effectively-once principle**_
- The batch approach to fault tolerance
- ensures that the output of the batch job is the same as if nothing had gone wrong.
- It appears as though every input record was processed exactly once

How is it in the stream processing that is infinite and never can be finished?

#### Microbatching and checkpointing
Microbatching (at Spark)
- break the stream into small blocks, and treat each block like a miniature batch process
- typically around one sec batch size
- any jobs that require larger windows need to explicitly carry over state from one microbatch to the next.

checkpointing (at Apache Flink)
- periodically generate rolling checkpoints of state and write them to durable storage
- if crashed so it can restart from its most recent checkpoint and discard any ouput after then

- In these approaches, as soon as output leaves the stream processor (for example, by writing to a database, sending messages to an external message broker, or sending emails)
   - the framework is no longer able to discard the output of a failed batch.
   - restarting can't be handled only by these

Atomic commit revisited
  - For such exactly-once processing, need to ensure that all outputs and side effects of processing an event take effect **if and only if the processing is successful.**
  - including any messages sent to downstream operators or external messaging systems (including email or push notifications), any database writes, any changes to operator state, and any acknowledgment of input messages
  - All should happen or none - atomically in a single transaction

#### Idempotence
An idempotent operation is one that you can perform multiple times, and it has the same effect as if you performed it only once.
- Easy to make an operation be idempotent with a bit of metadata
  - i.e. Kafka every message has an increasing offset, Stripe IdempotencyKey with request

#### Rebuilding state after a failure
Any stream process that requires state and tables/indexes used for joins must ensure the state can be recovered after a failure
- How?
  - keep the state in a remote datastore and replicate it, could be slow
    - or keep the state local to the stream processor and replicate periodically
    - Flink uses HDFS, Samza/Kafka/VoltDB all similar approach
  - some cases, it can be rebuilt from the input streams without need to replicate the state
    - if the state consistes of aggregations over a short window
- The decision depends on trade-offs regarding the performance characteristics of the underlying infrastructure
  - network delay, disk access latency, etc. 
