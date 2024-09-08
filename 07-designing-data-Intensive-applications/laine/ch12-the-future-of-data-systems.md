## Data Integration
_the most appropriate choice of software tool also depends on the circumstances. Every piece of software, even a so-called “general-purpose” database, is designed for a particular usage pattern._
- The first challenge: figure out the mapping between the software products and the circumstances in which they are a good fit. Vendors are understandably reluctant to tell you about the kinds of workloads for which their software is poorly suited
- The second challenge: data is often used in several different ways. There is unlikely to be one piece of software that is suitable for all the different circumstances in which the data is used, so you inevitably end up having to cobble together several different pieces of software in order to provide your application’s functionality.

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
