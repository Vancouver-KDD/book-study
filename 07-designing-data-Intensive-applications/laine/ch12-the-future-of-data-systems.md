## Data Integration
_the most appropriate choice of software tool also depends on the circumstances. Every piece of software, even a so-called “general-purpose” database, is designed for a particular usage pattern._
- The first challenge: **figure out the mapping between the software products and the circumstances in which they are a good fit**. Vendors are understandably reluctant to tell you about the kinds of workloads for which their software is poorly suited
- The second challenge: **data is often used in several different ways.** There is unlikely to be one piece of software that is suitable for all the different circumstances in which the data is used, so you inevitably end up having to cobble together several different pieces of software in order to provide your application’s functionality.

### Combining Specialized Tools by Deriving Data
many applications need to combine two different tools in order to satisfy all of the requirements.
  - i.e. it is common to need to integrate an OLTP database with a full-text search index to handle queries for arbitrary keywords.

As the number of different representations of the data increases, the integration problem becomes harder
  - the database and the search index
  - need to keep copies of the data in analytics systems (data warehouses, or batch and stream processing systems)
  - maintain caches or denormalized versions of objects that were derived from the original data
  - pass the data through machine learning, classification, ranking, or recommendation systems
  - or send notifications based on changes to the data

#### Reasoning about dataflows
Clear about inputs and outputs: To maintain copies of the same data in several storage systems for different access patterns
  - where is data written first, and which representations are derived from which sources?
  - How do you get data into all the right places, in the right formats?

Solution: if you can funnel all user input through a single system that decides on an ordering for all writes
  = Total Order Broadcast (p348) Application of the state machine replication approach
  - much easier to derive other representations of the data by processing the writes in the same order.
  - Updating a derived data system based on an event log can often be made deterministic and idempotent, easily

#### Derived data versus distributed transactions
How does the approach of using derived data systems fare in comparison to distributed transactions?

Deciding an ordering of writes
  - Distributed transactions: locks for mutual exclusion
  - derived data: log for ordering
Ensuring changes take effect exactly once
  - Distributed: atomic commit
  - derived: deterministic retry and idempotence

Biggest difference
  - transaction system: provide linearizability - guarantees of reading your own write
  - derived: updated asynchronously, so don't need to offer it

Author: 
  - XA has poor fault tolerance and performance characteristics - might be possible to create a better protocol for distributed transactions, but not soon
  - believes that log-based derived data is the most promising approach for integrating different data systems
    - However, guarantees such as reading your own writes? - will discuss stronger guarantees in the Aiming for Correctness section

#### The limits of total ordering (=consensus)
Constructing totally ordered event logs is challenging in bigger and complex systems, due to
  - totally ordered log requires all events to pass through a single leader node that decides on the ordering.
    - throughput of events > a single machine can handle ? then need to paritition but the order becomes ambiguous
  - geographically distributed datacenters with a separate leader in each datacenter - undefined ordering of events
  - microservice - When two events originate in different services, there is no defined order for those events.
  - client side state updated by user input and continued to work offline, then the clients/servers are likely to see events in different orders

#### Ordering events to capture causality
Social media example: rude messages regarding ex-partner after unfriend him
  - if the friendship status and messages are stored in a different place, ordering dependency between the unfriend event and message event can be lost.

Potential solutions starting point
  - Logical timestamps where total order broadcast is not feasible - still require recipients to handle events that are delivered out of order with additional metadata to be passed around.
  - Unique identifier to an event that user saw before request, any later events can reference it to record causal dependency - Reads are events too (p 513)
  - Conflict resolution algorithms after events are delivered in an unexpected order - but do not help if actions have external side effects (such as sending a notification to a user).

### Batch and Stream Processing
- the goal of data integration
    - to make sure that data ends up in the right form in all the right places.
    - Doing so requires consuming inputs, transforming, joining, filtering, aggregating, training models, evaluating, and eventually writing to the appropriate outputs, **with batch & stream processors**

The outputs of batch and stream processes
  - derived datasets such as search indexes, materialized views, recommendations to show to users, aggregate metrics, etc

#### Maintaining derived state
Batch processing - functional flavour
  - encourages deterministic, pure functions whose output depends only on the input and which have no side effects other than the explicit outputs, treating inputs as immutable and outputs as append-only.
  - this is good for fault tolerance and also simplifies reasoning about the dataflow in an organization
    - regardless of the derived data type, data pipelines deriving one thing from another, pushing state changes in a system via functional application code

Asynchronous derived data system
  - asynchrony makes systems based on event logs robust
  - allows a fault in one part of the system to be contained locally, whereas distributed transactions abort if one participant fails and amplify failures
  - cross-partition communication is also most reliable and scalable if the index is maintained asynchronously
    - A partitioned system with secondary indexes either needs to send writes to multiple partitions (if the index is term-partitioned) or send reads to all partitions (if the index is document-partitioned)

#### Reprocessing data for application evolution
- with reprocessing it is possible to restructure a dataset into a completely different model in order to better serve new requirements.

**gradual evolution**
- maintain the old schema and the new schema side by side as two independently derived views onto the same underlying data.
- then start shifting a small number of users to the new view, and gradually increase the proportion of the new view, and eventually drop the old view
- every stage of the process is easily reversible if something goes wrong

#### The lambda architecture
- combining batch processing used to reprocess historical data, and stream processing used to process recent updates
- proposes running two different systems in parallel: a batch processing system such as Hadoop MapReduce, and a separate stream- processing system such as Storm.

1. stream processor: fast approximate algorithms
   - consumes the events and quickly produces an approximate update to the view with 
2. batch processor: slow exact algorithm
   - consumes the same set of events and produces a corrected version of the derived view, 

Practical problems were resolved by recent features
- replay historical events through the same processing engine as the stream of recent events 
- exactly-once semantics for stream processors


## Aiming for Correctness
- correctness in the context of dataflow architectures

### The End-to-End Argument for Databases
Data system doesn't guarantee the application code being free from data loss or corruption with its own bug
 - that's why append-only data is favoured

#### Exactly-once execution of an operation
- exactly-once means arranging the computation such that the final effect is the same as if no faults had occurred, even if the operation actually was retried due to some fault.
  - idempotent operation, one of the most effective approach

#### Operation identifiers
- idempotent operation can't be created relying on just DB transaction, it should consider end-to-end flow of the request
- Thus generate a unique identifier for an operation - UUID
  - suppress duplicate requests and acts as an event log

#### The end-to-end argument
_"The function in question can completely and correctly be implemented only with the knowledge and help of the application standing at the endpoints of the communica‐ tion system. Therefore, providing that questioned function as a feature of the communication system itself is not possible."_

- encryption example: in every layer implemented, encryption and authentication can protect against all of these things.

#### Applying end-to-end thinking in data systems
The author thinks we have not yet found the right abstraction.
- Transactions have long been seen as a good abstraction and useful, but not enough.
- They are expensive in the distributed systems especially, so other fault-tolerance mechanism should be implemented in the application code
_- "worth exploring fault-tolerance abstractions that make it easy to provide application-specific end-to-end correctness properties, but also maintain good performance and good operational characteristics in a large-scale distributed environment."_

### Enforcing Constraints
- focus on uniqueness constraints

#### Uniqueness constraints require consensus
- getting consensus in the distributed system is hard, only simple when a single node is the leader and it funnels all requests
- uniqueness checking can be partitioned but if different masters accept conflicting writes?
  - synchronous coordination is unavoidable for uniqueness constraints

#### Uniqueness in log-based messaging
_total order broadcast = consensus_
- In the unbundled database approach with log-based messaging, we can use a very similar approach to enforce uniqueness constraints.
- if the log is partitioned based on the value that needs to be unique, a stream processor can unambiguously and deterministically decide which one of several conflicting operations came first.
  - client watches the output stream and waits for a success or rejection
- Not just for uniqueness, but for any constraints

#### Multi-partition request processing
In the traditional approach to databases, executing multiple related transaction would require an atomic commit across all three partitions, with total order on all partitions.
- throughput suffering

Equivalent correctness is achieved with partitioned logs, without an atomic commit
- by breaking down the multi-partition transaction into two differently partitioned stages and using the end-to-end request ID
- deriving information from the message, that was logged from the earlier request

### Timeliness and Integrity
_"violations of timeliness are “eventual consistency,” whereas violations of integrity are “perpetual inconsistency.”"_
- Timeliness means ensuring that users observe the system in an up-to-date state.
- Integrity means absence of corruption; i.e., no data loss, and no contradictory or false data.
- integrity is much more important than timeliness. Violations of timeliness can be annoying and confusing, but violations of integrity can be catastrophic.

#### Correctness of dataflow systems
The event-based dataflow systems discussed in this chapter decouple timeliness and integrity.
- processing event streams asynchronously, there is no guarantee of timeliness
- integrity is in fact central to streaming systems: Exactly-once or effectively-once semantics

_"reliable stream processing systems can preserve integrity without requiring distributed transactions and an atomic commit protocol, which means they can potentially achieve comparable correctness with much better performance and operational robustness"_
  - Representing the content of the write operation as a single message, which can easily be written atomically—an approach that fits very well with event sourcing (see “Event Sourcing” on page 457)
  - Deriving all other state updates from that single message using deterministic der‐ ivation functions, similarly to stored procedures (see “Actual Serial Execution” on page 252 and “Application code as a derivation function” on page 505)
  - Passing a client-generated request ID through all these levels of processing, ena‐ bling end-to-end duplicate suppression and idempotence
  - Making messages immutable and allowing derived data to be reprocessed from time to time, which makes it easier to recover from bugs (see “Advantages of immutable events” on page 460)

- ACID transactions usually provide both timeliness (e.g., linearizability) and integrity (e.g., atomic commit) guarantees.

#### Loosely interpreted constraints
many real applications can actually get away with much weaker notions of uniqueness by apologizing and fixing later
- In many business contexts, it is actually acceptable to temporarily violate a constraint and fix it up later by apologizing, with often low cost
- **These applications do require integrity, but they don’t require timeliness on the enforcement of the constraint**

#### Coordination-avoiding data systems
_coordination-avoiding data systems_
1. Dataflow systems can maintain integrity guarantees on derived data without atomic commit, linearizability, or synchronous cross-partition coordination.
2. Although strict uniqueness constraints require timeliness and coordination, many applications are actually fine with loose constraints that may be temporar‐ ily violated and fixed up later, as long as integrity is preserved throughout.
-> dataflow systems can provide the data management services for many applications without requiring coordination, while still giving strong integrity guarantees.

_coordination and constraints_
- they reduce the number of apologies you have to make due to inconsistencies,
- but potentially also reduce the performance and availability of your system, and thus **potentially increase the number of apologies** you have to make due to outages.
- find the best trade-off for your need

### Trust, but Verify
#### Maintaining integrity in the face of software bugs
- There always exist hardware issues that can be caught by lower-level network, memory, or filesystem checksums & software bugs
- If the application uses the database incorrectly in some way, for example using a weak isolation level unsafely, the integrity of the database cannot be guaranteed.

#### Don’t just blindly trust what they promise - auditing
- data corruption is inevitable sooner or later from hardware or software issues, so should check the integrity of data
- large-scale storage systems such as HDFS and Amazon S3 do not fully trust disks
  - they run background processes that continually read back files, compare them to other replicas, and move files from one disk to another, in order to mitigate the risk of silent corruption

#### A culture of verification
Many assume that correctness guarantees are absolute and make no provision for the possibility of rare data corruption
  - more self-validating or self-auditing systems that continually check their own integrity in the future

Author: "I fear that the culture of ACID databases has led us toward developing applications on the basis of blindly trusting technology (such as a transaction mechanism), and neglecting any sort of auditability in the process."
- with NoSQL and weaker consistency guarantees, audit mechanism hasn't been developed.

#### Designing for auditability
event-based systems can provide better auditability than transaction based data system
  - In the event sourcing approach, user input to the system is represented as a single immutable event, and any resulting state updates are derived from that event.
  - If a transaction mutates several objects in a database, it is difficult to tell after the fact what that transaction means

A deterministic and well-defined dataflow makes it easier
 - to make integrity checking much more feasible - using hashes to check the event storage is not corrupted
 - to debug and trace the execution of a system in order to determine why it did something

#### The end-to-end argument again
- we must at least periodically check the integrity of our data. If we don’t check, we won’t find out about corruption until it is too late and expensive
- Having continuous end-to-end integrity checks gives you increased confidence about the correctness of your systems

#### Tools for auditable data systems
At present, not many data systems make auditability a top-level concern.
- cryptographic tools to prove the integrity of a system in a way that is robust to a wide range of hardware and software issues, and even potentially malicious actions. Cryptocurrencies, blockchains, and distributed ledger technologies such as Bitcoin, Ethereum, Ripple, Stellar, and various others have sprung up to explore this area.
