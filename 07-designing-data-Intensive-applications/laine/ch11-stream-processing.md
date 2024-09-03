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
   - Apache Storm has a feature, distributed RPC, allowing user queries to be farmed out to a set of nodes that also process event streams; these queries are then interleaved with events from the input streams, and results can be aggregated and sent back to the user [78].
- It is also possible to process streams using actor frameworks. However, many such frameworks do not guarantee message delivery in the case of crashes, so the processing is not fault-tolerant unless you implement additional retry logic.

### Reasoning About Time
