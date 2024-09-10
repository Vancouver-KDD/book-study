## Data Integration

The book emphasizes various solutions to problems, highlighting the pros and cons of each approach and their trade-offs.

- Chapter 3 discusses different storage engines, such as log-structured storage, B-trees, and column-oriented storage.
- Chapter 5 covers replication methods, including single-leader, multi-leader, and leaderless replication.

There is no one-size-fits-all answer to the problem of "storing some data and later retrieving it," as there are various solutions depending on the situation. Trying to solve every scenario with a single software might lead to suboptimal implementations. Therefore, software tool selection should be context-specific, and even "general-purpose" databases are designed with specific usage patterns in mind.

The first challenge is mapping software products to the situations they are suited for. Vendors may not mention cases where their software is unsuitable, but the questions discussed in this book help understand this.

In complex applications, data is often used in multiple ways, so it is common to combine multiple software tools.

### Combining Specialized Tools by Deriving Data

Integrating OLTP databases with full-text search indexes is a common task.

- Databases like PostgreSQL offer full-text indexing capabilities.
- However, for more complex search features, specialized tools are required.
- Search indexes are not suitable as durable record systems.
- Therefore, many applications combine both tools to meet their requirements.

"Keeping Systems in Sync" explains the difficulties of integrating data systems.

- As the variety of data representations increases, integration becomes more complex.
- In addition to databases and search indexes, data might need to be processed through analytics systems, caches, denormalized data, and machine learning.
- Notification systems for data changes may also be required.

Software engineers often make statements like "99% of people only need X," but this often reflects the speaker’s experience more than the utility of the technology. The need for data integration becomes clear only when considering the data flow across the entire organization.



#### Reasoning about dataflows

When maintaining the same data across multiple storage systems, a clear plan for data input and output is necessary.

- Determine where data is first recorded.
- Identify which representations are derived from which sources.
- Plan how to transfer data to the appropriate places in the right format.

For example:

- Data might first be recorded in a **system of record** database.
- Changes in this database can be captured (Change Data Capture, CDC).
- These changes can then be applied to the search index in the same order.

If **Change Data Capture** is the only method for updating the index:

- The index is entirely derived from the **system of record**.
- This ensures consistency as long as there are no software bugs.

However, if an application allows writes to both the search index and the database simultaneously, it can lead to issues like those shown in Figure 11-4.

- Two clients might send conflicting writes, and the storage systems could process them in different orders.
- This can result in a permanent inconsistency if the orders do not match.

To solve this, it is important to route all user inputs through a single system to maintain consistent write order. This makes it easier to derive data into different representations. This approach is similar to **Total Order Broadcast** used in **state machine replication**.

Finally, updating systems based on an event log can be made deterministic and **idempotent**, which facilitates error recovery.


#### Derived data (파생 데이터) versus distributed transactions

Traditional methods for maintaining consistency across different data systems involve **distributed transactions**. However, what differentiates derived data systems from distributed transactions?

Both methods use different mechanisms to achieve similar goals.

- Distributed transactions use **locks** to determine the order of write operations.
- **CDC** (Change Data Capture) and **event sourcing** use **logs** to maintain order.
- Distributed transactions ensure that changes are applied exactly once through **atomic commits**.
- Log-based systems utilize **deterministic retries** and **idempotence**.

Key differences include:

- Distributed transaction systems provide **linearizability**, ensuring strong consistency guarantees such as "reading your writes."
- Derived data systems use **asynchronous updates**, which do not offer the same timing guarantees.

Distributed transactions have been successful in limited environments, but protocols like **XA** have limitations in fault tolerance and performance, leading to their limited adoption. Even with better transaction protocols, wide adoption and integration remain challenging.

In this context, **log-based derived data systems** seem to be the most promising method for integrating different data systems. However, guarantees like "reading your writes" remain important, so advice to simply accept **eventual consistency** may not be sufficient.

Lastly, **Aiming for Correctness** discusses how to implement stronger guarantees in asynchronous derived systems and explores balancing **distributed transactions** and **asynchronous log-based systems**.

#### The Limits of Total Ordering

While determining the order of events in small systems is feasible, several limitations arise as systems scale:

- A **single leader node** determines the order of all events. When the volume exceeds what a single machine can handle, the log must be **partitioned**, leading to ambiguity in the order of events across partitions.
- In **geographically distributed data centers**, each data center might have its own **leader**, and network latency between data centers makes synchronized coordination difficult, resulting in unclear event order.
- In **microservices architectures**, each service is independently deployed, and event order between services is not defined.
- Applications with **client-side state** often update user inputs immediately without server confirmation, leading to different event orders observed by clients and servers.

Determining total order of events is equivalent to **total order broadcast**, which is akin to **consensus**.

- Most consensus algorithms depend on the throughput of a single node and do not provide mechanisms for multiple nodes to handle event order.
- Consensus algorithms for systems exceeding the throughput of geographically distributed environments are still under research.


#### Ordering events to capture causality

When there is no causal connection between events, having a total order is not critical, and concurrent events can be ordered arbitrarily. However, when there is causal dependence, it becomes more complex.

For instance, consider a social network where a user removes someone from their friend list and then sends an unpleasant message. If the friend removal and message sending are stored in different systems, the causal order might not be guaranteed, leading to incorrect notifications.

This issue is similar to the problem of joining message sending and friend list updates. While there is no simple solution, several approaches can be considered:

- **Logical Timestamps**: Provide a total order but require additional metadata to handle incorrect orderings.
- **Event Identifiers**: Assign unique identifiers to events and reference these identifiers to record causal dependencies.
- **Conflict Resolution Algorithms**: Automatically resolve ordering conflicts, but may have limited effectiveness in the presence of external side effects.

Over time, development patterns that efficiently capture causal dependencies and handle all events in the correct order without bottlenecks will emerge.


### Batch and Stream Processing

The goal of data integration is to ensure that data exists in the appropriate form in all relevant locations.

- This requires consuming, transforming, joining, filtering, aggregating input data, training and evaluating machine learning models, and then recording the results in suitable outputs.
- Batch and stream processors are tools for performing these tasks.

The results of batch and stream processing are derived datasets such as **search indexes**, **materialized views**, recommendation system outputs, and aggregated metrics.

As discussed in Chapters 10 and 11, while batch and stream processing share many common principles, the main differences are:

- **Stream processing** deals with infinite datasets, while
- **Batch processing** handles finite datasets of fixed size.
- Though there are differences in the implementation of processing engines, this distinction is becoming increasingly blurred.

For example:

- **Spark** processes streams as **micro-batches**, operating stream processing on top of batch processing engines,
- **Apache Flink** performs batch processing on top of a stream processing engine.

One type of processing can emulate the other, but performance characteristics differ. For instance, **micro-batching** can suffer performance issues in tasks such as hopping or sliding windows.



#### Maintaining derived state

**Batch processing** reflects many features of functional programming:

- It encourages **deterministic** and **pure functions** that are dependent on inputs and have no side effects beyond explicit outputs.
- Input data is treated as **immutable**, and outputs are processed as **append-only**.
- **Stream processing** is similar but extends with operators that allow **managed and fault-tolerant state**.

The principle of **deterministic functions** requires clearly defined inputs and outputs, which improves **fault tolerance** and makes logical reasoning about data flow in the organization easier. This can be applied in systems synchronizing derived data such as **search indexes**, **statistical models**, or **caches**.

In theory, **derived data systems** can be maintained **synchronously**:

- This is similar to how relational databases handle write operations to tables while synchronously updating **secondary indexes**.
- However, **asynchrony** is a key factor in making systems more robust and scalable. Asynchronous processing ensures that a failure in one part of the system does not affect the rest, whereas in distributed transactions, a single failure can impact the entire system.

For secondary indexes, maintaining them in a partitioned system requires **cross-partition communication**. When partitioning by term, writes must be sent to multiple partitions, and when partitioning by document, reads must be sent to all partitions. Such communication is most reliable and scalable when handled **asynchronously**.

#### Reprocessing data for application evolution

Both **batch processing** and **stream processing** are useful for maintaining derived data:

- **Stream processing** can quickly reflect changes in input data in **derived views**,
- **Batch processing** is effective for reprocessing large amounts of **historical data** to create new views on existing datasets.

Reprocessing **existing data** is particularly useful for system maintenance and supporting new features or changed requirements:

- Without reprocessing, schema evolution is limited to simple changes like adding new **optional fields** or new types of records.
- This applies to both **schema-on-write** and **schema-on-read** approaches.
- However, reprocessing allows complete restructuring of existing data into a new model, better supporting new requirements.

#### Schema Migrations on Railways

Large-scale **schema migrations** also occur outside of computer systems. In the early 19th century, various competing standards for **rail gauge** in the UK meant trains built for one gauge could not run on tracks of another gauge, limiting the interconnection of the railway network. After a standard gauge was set in 1846, tracks of other gauges had to be converted. This was done by converting to **dual gauge** so that the old and new systems could coexist, allowing for gradual change.

Similarly, **derived views** enable gradual evolution:

- When restructuring datasets, migration can be done progressively by maintaining two derived views based on the old and new schemas side by side.
- Transition a small number of users to the new view for testing, then gradually move more users, ultimately removing the old view.

The advantage of gradual migration is that it is **reversible**:

- If problems occur, you can always revert to the stable system, reducing risk and enabling safer and faster system improvements.

#### The lambda architecture

**Lambda architecture** has gained attention for combining batch and stream processing to handle data.

The main idea of the lambda architecture is to:

- Record incoming data as immutable events, similar to **event sourcing**,
- Continuously add to the growing dataset,
- Derive **read-optimized views** from these events.

The lambda architecture proposes running **batch processing systems** (e.g., Hadoop MapReduce) and separate **stream processing systems** (e.g., Storm) in parallel.

In the lambda architecture:

- **Stream processors** quickly consume events and generate **approximate updates** to views.
- Later, **batch processors** use the same events to generate **accurate derived views**.

This approach is based on the assumption that batch processing is simpler and less error-prone, while stream processing is faster but less reliable. It also uses **approximate algorithms** in stream processing and **exact algorithms** in batch processing.

Despite its limitations, the lambda architecture has had a positive impact on data system design by providing a structure for deriving views from immutable event streams and reprocessing data when needed. However, it also presents practical challenges:

1. **Duplicate Work**: There is a burden of maintaining the same logic in both batch and stream processing frameworks. Libraries like Summingbird provide abstractions to address this, but debugging, tuning, and maintaining both systems remains complex.
2. **Difficulty Merging Outputs**: Combining outputs from stream pipelines and batch pipelines is necessary, and while simple **tumbling window** aggregations are straightforward, complex operations like **joins** or **sessionization** are challenging to merge.
3. **Processing Large Datasets**: Frequently reprocessing entire historical datasets is **costly**, often leading to the use of **incremental batch processing**, which adds complexity and conflicts with the goal of keeping the batch layer simple.

Despite these issues, the lambda architecture remains a significant concept in data system design.

#### Unifying batch and stream processing

Recent research aims to overcome the limitations of the **lambda architecture** by integrating **batch processing** (historical data reprocessing) and **stream processing** (real-time event processing) into a single system, thus leveraging the strengths of both.

To implement such integration, the following features are required:

- The ability to **replay past events** using the same engine that processes recent event streams.
    - Log-based message brokers offer replay capabilities, and some **stream processors** can read input data from distributed file systems like **HDFS**.
- Stream processors that support **exactly-once semantics**.
    - This ensures that like batch processing, in the event of a failure, partially output data is discarded, and correct output is guaranteed as if no failure occurred.
- Support for **event-time** based **windowing**.
    - This is important because processing time is meaningless when reprocessing past data. **Apache Beam** provides an API to express such computations, which can be executed in **Apache Flink** or **Google Cloud Dataflow**.

This unified approach combines the benefits of batch and stream processing into a more efficient and reliable data processing system.



## Unbundling (분리) Databases

At a fundamental level, **databases**, **Hadoop**, and **operating systems** all provide mechanisms for storing data, and for processing or querying it.

- Databases store data as records such as **rows** in tables, **documents**, or **graph vertices**.
- Operating systems store data through files, but both are fundamentally similar in that they are **information management systems**.

There are practical differences as well.

- For example, file systems are vulnerable to handling numerous small files, whereas databases are suited for managing numerous small records.
- Understanding the philosophical differences between operating systems and databases is also important.

**Unix** and **relational databases** have applied different philosophies to **information management problems**.

- Unix provides **low-level hardware abstraction**, treating files and pipes as simple **sequences of bytes**.
- On the other hand, relational databases abstract data structures, concurrency, and recovery through **SQL** and **transactions**, allowing programmers to avoid dealing with complex implementation details.

Which approach is better depends on the **goals**.

- Unix aims for "simplicity" by wrapping hardware resources in a straightforward manner.
- Databases offer a different kind of "simplicity" by enabling programmers to leverage complex infrastructure through concise SQL queries.

The tension between these two philosophies persists, and the **NoSQL** movement can be seen as an attempt to apply Unix's **low-level abstraction** philosophy to the domain of distributed **OLTP data stores**.

### Composing Data Storage Technologies (데이터 저장 기술의 구성)

This book has discussed various features provided by databases and how they operate. These include:

- **Secondary Indexes**: Help efficiently search for records based on specific field values.
- **Materialized Views**: Cache precomputed query results to enable quick retrieval.
- **Replication Logs**: Keep data up-to-date by replicating it to other nodes.
- **Full-Text Search Indexes**: Enable keyword searches within text, some of which are built into relational databases.

Related topics were also covered in Chapters 10 and 11. For example:

- How to build **full-text search indexes**,
- How to maintain **materialized views**,
- How to replicate changes from databases to derived data systems.

These features exhibit similarities between database built-in functionalities and **derived data systems** constructed using batch and stream processors. This creates opportunities to increasingly blend the boundaries between database features and data processing systems, allowing for more efficient and integrated data management solutions.

#### Creating an index

In a relational database, executing the **CREATE INDEX** command involves the following steps:

- First, the database scans a **consistent snapshot** of the table.
- It selects and **sorts** all the field values to be indexed.
- A new **index** is then created.
- The database maintains the index up-to-date by reflecting any **write operations** that occurred after the snapshot was taken (if the table was not locked, write operations continue).
- Finally, all subsequent **transactions** automatically keep the index updated.

This process is similar to setting up a new **follower replica**. It also resembles capturing an initial snapshot through **change data capture** in streaming systems.

In essence, the **CREATE INDEX** command reprocesses the **existing dataset** to create a new view, the index. While the existing data might be a **snapshot of state** rather than a change log, these two elements operate in close relation.


#### The meta-database of everything

From this perspective, the **data flow** across an organization can be seen as one large database. Batch processes, streams, and ETL processes transfer data from one place to another, or from one form to another, similar to how a database system maintains **indexes** and **materialized views**.

Thus, **batch and stream processors** perform roles akin to:

- Complex **triggers**,
- **Stored procedures**,
- **Materialized view maintenance** routines.

The **derived data systems** they maintain operate similarly to various **index** types.

- Relational databases support **B-tree indexes**, **hash indexes**, **spatial indexes**, and more.
- However, in emerging **derived data system architectures**, these indexing functionalities are handled by multiple pieces of software running on different machines and managed by different teams, rather than a single database system.

Considering future developments, integrating storage and processing tools into a unified system can follow two paths due to the lack of a **single data model** or **storage format** that fits all access patterns:

1. **Federated Database: Read Integration**
    
    - This approach integrates various storage engines and processing methods into a single **query interface**, known as a **federated database** or **polystore**.
    - For example, PostgreSQL’s **foreign data wrapper** fits this pattern. Specific applications can still access the underlying storage engines but can combine data from multiple sources through a federated query interface.
    - This approach provides a high-level query language and **semantics** but can be complex to implement.
2. **Separated Databases: Write Integration**
    
    - **Write synchronization** remains an unresolved issue in federated databases.
    - To synchronize data across multiple systems, **Change Data Capture** and **event logs** are used to ensure data changes reach the appropriate places.
    - This approach is similar to Unix’s philosophy of using small tools and separates database indexing functions to enable **write synchronization** across multiple systems.


#### Making unbundling work

**Federation** and **unbundling** are two sides of the same coin when it comes to designing systems that are reliable, scalable, and maintainable.

- **Federated read-only querying** requires mapping one data model to another, which is manageable.
- However, **write synchronization** across multiple storage systems is a more challenging engineering problem, which we will focus on.

Traditionally, **distributed transactions** across heterogeneous storage systems were used to address write synchronization.

- However, this approach can be inefficient, and I believe **asynchronous event logs** and **idempotent writes** offer a more robust and practical solution.

For example, some stream processors use distributed transactions for **exactly-once semantics**, but integrating different systems (e.g., **distributed key-value stores** or **search indexes**) is difficult. Instead, **event log** sequential recording and **idempotent consumers** provide a simpler and more effective method.

**Log-based integration** offers the crucial advantage of **loose coupling**:

1. **System Aspect**:
    
    - **Asynchronous event streams** provide robustness against individual system failures or performance issues.
    - Slow or failed consumers can catch up later, and **event logs** buffer data without affecting other systems.
    - In contrast, **distributed transactions** involve synchronous interactions, leading to wide-reaching impacts if failures occur.
2. **Human Aspect**:
    
    - Separating data systems allows different teams to develop and maintain software components independently.
    - This enables each team to focus on specific tasks and interact through **well-defined interfaces**.
    - **Event logs** provide a robust interface that ensures durability and consistency while handling various data types, offering a versatile solution.

This approach maximizes flexibility in operations and development while ensuring system robustness and scalability.


#### Unbundled versus integrated systems

While **unbundled systems** may become more prevalent, **databases** will remain essential. Databases play crucial roles such as:

- **Maintaining state** in stream processors,
- Handling **output queries** from batch and stream processors.

Specialized **query engines** optimized for specific tasks remain important.

- For instance, **MPP data warehouses** have query engines optimized for **exploratory analysis queries**, effectively handling these workloads.

The **complexity** of operating various infrastructures can be a problem.

- Each software component comes with its learning curve and operational issues, so minimizing the number of components is preferable.
- Additionally, a **single integrated software** often offers **better performance** and **predictable results** compared to systems composed of multiple tools.
- Designing systems for unnecessary expansion can lead to **premature optimization**.

The goal of **unbundled systems** is not to compete with a single database in terms of performance but to achieve efficient performance across a broad range of workloads that a single software cannot handle.

Therefore, if a **single technology** can meet all requirements, it is likely the best choice.

- It is more efficient than trying to implement lower-level components directly.
- **Unbundling** and **composing** make sense when a single software cannot address all needs.



#### What’s missing?

Even though data systems and tools are advancing rapidly, there is still one **important aspect** that seems to be missing.

- We don't yet have a **high-level language** that allows us to **declaratively configure** storage and processing systems in a **simple and declarative** way, similar to how Unix shells work.
    
- For example, it would be beneficial to be able to declare something like **`mysql | elasticsearch`** easily.
    
    - This would automatically index all documents from a MySQL database into Elasticsearch.
    - It would then capture all changes in the MySQL database and automatically reflect them in the search index.
    - This **integration approach** should be possible across various storage or indexing systems.
- Additionally, it would be great if it were easier to **precompute and update caches**.
    
    - **Materialized views** essentially serve as precomputed **caches**, so we need functionality to declaratively specify materialized views and create caches for complex queries.
    - This could include **recursive queries** for graphs or **application logic**.
    - Promising research in this area includes early-stage ideas like **differential dataflow**, and we hope these concepts will be applied in real systems.

Ultimately, such high-level languages could reduce the complexity of data systems and make interactions between separated components simpler and more efficient.


### Designing Applications Around Dataflow

The approach of **decoupling databases** and using application code to configure **specialized storage and processing systems** is often referred to as "database inside-out." This approach is more of a **design pattern** rather than a new architecture and is based on the concept of building applications around **data flow**.

These ideas are not my own invention but a fusion of **other people's ideas**. There is much to learn from **dataflow languages** like **Oz** and **Juttle**, **functional reactive programming (FRP)** languages like **Elm**, and **logic programming languages** like **Bloom**. The concept of **unbundling** proposed by **Jay Kreps** also makes a significant contribution.

Interestingly, **spreadsheets** have had **data flow programming** capabilities that are ahead of many mainstream programming languages. Just as you enter formulas in spreadsheet cells and get automatic recalculations when input values change, we expect related **indexes**, **cached views**, and **aggregations** to be automatically updated when database records change. Ideally, this should work reliably without worrying about the underlying process.

Modern **data systems** still have much to learn from the features implemented in **VisiCalc** back in 1979. However, today’s data systems need to ensure **fault tolerance**, **scalability**, and **durable data storage** while integrating diverse technologies written by different groups and reusing existing **libraries** and **services**.

This section explores how to extend these ideas and build applications around the concepts of **decoupled databases** and **data flow**.

#### Application code as a derivation function

When a dataset is derived from another dataset, it goes through a **transformation function**.

- **Auxiliary indexes** are created through simple transformations like selecting and sorting specific columns or field values from the base table.
- **Full-text search indexes** generate data structures like inverted indexes after applying natural language processing features.
- **Machine learning models** are derived by transforming training data, applying feature extraction and statistical analysis functions.
- **Caches** are aggregated results tailored to the user interface (UI), which can be rebuilt when the UI changes.

**Auxiliary indexes** are frequently used and can be easily created with the **CREATE INDEX** command, but **full-text indexing** and **machine learning** may require domain-specific custom functionality.

Custom code needed for creating derived datasets can be handled in databases through **triggers**, **stored procedures**, and **user-defined functions**, although these functionalities are sometimes treated as lower priorities in database design.

#### Separation of application code and state

In theory, databases could serve as an environment where **application code can be deployed**, but in practice, they don't align well with modern application requirements such as **dependency management**, **version control**, **scalability**, and **network integration**. On the other hand, **deployment tools** like **Mesos** and **Kubernetes** are specialized for running application code.

**Stateless services** allow servers to forget state after handling a request, making it possible to freely add or remove servers. **State management** is typically handled by **databases**, which separates application logic from **persistent state**.

However, most programming languages lack the ability to **subscribe to changes** and rely on periodic reads. Similarly, databases also require **polling** to check for state changes, though recent developments have introduced **change subscription** features.


#### Dataflow: Interplay between state changes and application code

Thinking of applications in terms of **data flow** redefines the relationship between **application code** and **state management**. Instead of treating the database as a **passive variable**, the focus shifts to the **interaction** between **state changes** and the code that processes them. Application code responds to state changes by triggering other state changes.

**Database change logs** can be handled as **subscribeable event streams**, with systems like **message passing systems** or the **tuple space model** used to respond to them.

Maintaining **derived data** requires attention to the **order of state changes**, and events must be processed in a consistent order. **Fault tolerance** is also crucial, as both message delivery and state updates require reliability. These requirements are less costly and more robust compared to **distributed transactions**, and **modern stream processors** offer scalable solutions for this.

Application code runs as **stream operators**, building large-scale systems by taking state change streams as input and producing other state change streams as output.


#### Stream processors and services

The modern application development style involves breaking down **functionality into a set of services** that communicate through **synchronous network requests** like **REST APIs**. The main advantage of this approach is **loose coupling**, which enables **organizational scalability**, allowing teams to develop and deploy services independently.

**Configuring stream operators** in a data flow system is similar to **microservices**, but differs in that it uses **one-way asynchronous message streams** for communication. This enhances **fault tolerance** and **performance** compared to **synchronous request/response** mechanisms.

For example, when handling **currency conversion**:

1. With the **microservices** approach, you query the **currency service** at the time of purchase.
2. With the **data flow** approach, you subscribe to the **currency update stream** to record currency rates in the local database and perform only local queries at the time of purchase.

The **data flow approach** is faster, less sensitive to service failures, and minimizes **network requests**. **Stream joins** between **purchase events** and **currency update events** are **time-dependent**, meaning that past currency rates might be needed at the time of purchase.

Subscribing to **change streams** resembles a **spreadsheet**-like **computation model**, where all dependent derived data is automatically updated when the data changes. Although there are still unresolved issues, building applications around **data flow** is a very promising approach.


### Observing Derived State

**Data flow systems** create and maintain **derived datasets** (e.g., search indexes, physical views, prediction models) through a process called the **write path**, where derived datasets are updated based on data writes. **Figure 12-1** illustrates an example of how a **search index** is updated.

The reason for creating derived datasets is to allow querying later via the **read path**. The read path involves querying the derived datasets to provide results to user requests.

The **write path** is similar to **eager evaluation**, while the **read path** resembles **lazy evaluation**. **Derived datasets** represent the intersection of these two paths, reflecting a trade-off between write and read operations.

#### Materialized views and caching

**Full-text search indexes** exemplify the balance between **write path** and **read path**:

- The write path involves **index updates**.
- The read path requires **keyword searches** and **Boolean logic** application.
- Without indexes, the write path is less intensive, but the read path requires **scanning all documents**, increasing workload.

Conversely, **precomputing all query results** simplifies the read path but is inefficient due to the need for **infinite time and storage space** on the write path. A practical approach involves **precomputing only common queries** for caching and using indexes for others. This approach is known as **caching** or **materialized views**.

In summary, **caches**, **indexes**, and **materialized views** all adjust the boundary between **write path and read path** to reduce **read workload by performing more work on the write path**. This aligns with the concepts explained earlier in the book using the **Twitter example**.

#### Stateful, offline-capable clients

The concept of the boundary between the **write path** and **read path** can be discussed in various contexts. Specifically, in the **client/server model**, traditional assumptions where **clients are stateless** and servers hold data authority need reconsideration.

Traditionally, **web browsers** were **stateless clients**, useful only with an internet connection. However, modern **single-page web apps** and **mobile apps** **maintain state on the client side**, allowing most tasks to be performed without server round trips using **local storage**.

This shift has brought attention to **offline-first applications**. These applications use a **local database** to handle as many tasks as possible on the device and synchronize with the server when a network connection is available. This is particularly advantageous for **mobile devices** with slow and unreliable **internet connections**.

Moving away from **stateless clients** to **stateful devices** opens up new opportunities. **Device-local state** can be seen as a **cache of server state**, and **screen pixels** act as a **physical view** in the client app, serving as a **local replica** of remote server state.

#### Pushing state changes to clients

A typical web page does not notice server data changes unless the browser **refreshes**, as the browser assumes a **static** reading time for the data. To recognize state changes, **polling** is needed, which is inefficient.

Recently, protocols such as **EventSource API** and **WebSocket** have evolved to allow servers to **push real-time state changes** to clients. This reduces the **staleness of client state** and operates by extending **state change streams** from the server to the client.

Clients initially retrieve the state using the **read path** and then receive **state change streams** from the server. While in **offline mode**, the client cannot receive notifications from the server but can use **reconnect technology** in **log-based message brokers** to catch up on missed messages.
#### End-to-end event streams

Client state management tools like **Elm**, **React**, **Flux**, and **Redux** use **event streams** to manage **client-side state**, resembling the structure of **event sourcing**. Extending this approach, where the server pushes **state change events** to clients, is natural and allows state changes to flow through an **end-to-end write path**.

Applications using **real-time architecture**, such as **instant messengers** or **online games**, also rely on this approach. However, most applications still depend on **stateless clients** and **request/response interactions** due to a lack of proper support for **change subscription**.

To extend the write path to end-users, switching from **request/response** to **publish/subscribe data flows** is necessary. This transition offers benefits such as **more responsive UI** and **enhanced offline support**. Considering **change subscriptions** when designing **data systems** is crucial.

#### Reads are events too

We discussed how **stream processors** write derived data to a **store**, with user requests querying that store. The store acts as a **boundary** between **write path** and **read path**. However, **stream processors** also maintain **state** and perform **joins**, with some frameworks allowing **querying** of this state.

**Read requests** can also be treated as **event streams**, and it is possible to handle **write events** and **read events** within the same stream processor. In this case, **read events** are processed similarly to **stream-table joins** with the database.

Recording **read event logs** has the advantage of tracking **causal dependencies** and **data sources**, but this may incur additional **storage and I/O costs**. This issue remains an area for **further research and optimization**.


#### Multi-partition data processing

For simple queries processed on a single **partition**, using **streams** for query and response collection may be excessive. However, for **complex queries**, combining data from multiple partitions allows for **distributed execution**. This leverages **message routing**, **partitioning**, and **join infrastructure** in stream processors.

For instance, **Storm's distributed RPC** was used by **Twitter** to count users viewing a specific URL and by **fraud detection** systems to combine partitioned datasets for trust score evaluation.

When **multi-partition joins** are needed, using **databases** might be simpler, but leveraging **stream processors** enables implementing **large-scale applications** that surpass existing solution limits.


## Aiming for Correctness

**Stateless services** involve merely reading data, so if issues arise, they can be easily resolved by fixing bugs and restarting. However, **stateful systems** (e.g., **databases**) may have **permanent impacts** if problems occur, necessitating more careful design.

To build a **reliable application**, it must provide **correct semantics** even in the face of failures. Traditionally, **transaction properties** (atomicity, isolation, durability) have ensured this reliability, but in practice, limitations such as **weak isolation levels** have emerged. Some applications forgo **transactions** in favor of **complex semantics** for performance reasons, leading to **consistency** issues.

Experiments like **Jepsen** show that **safety guarantees** in products may actually be weak. Additionally, it is important that **application code** correctly utilizes the features of the infrastructure. Therefore, if **strong correctness guarantees** that do not allow for **data corruption** are needed, **serializability** and **atomic commits** are useful but come with **costs** and **scalability** limitations.

This section discusses how to pursue **correctness** within the context of a **data flow architecture**.

### The End-to-End Argument for Databases

Using a **data system** that provides strong **safety properties** like **serializable transactions** does not entirely prevent **data loss** or **corruption**. For example, **application bugs** can lead to **incorrect data being written** or **data being deleted**, and **serializable transactions** cannot prevent this.

These issues may seem minor, but bugs or **errors** in applications can occur, making concepts like **immutability** and **additional dedicated data** useful for preventing such issues. While these concepts can make **recovery** easier, **immutability** alone cannot resolve all problems.
#### Exactly-once execution of an operation

**Exactly-once** (or **effectively-once**) execution is the concept of preventing a message from being **processed twice** due to errors such as **message dropping** or **retries**. Double processing can lead to **data corruption**, such as **double billing** a customer or **increasing a counter twice**.

**Exactly-once** means that even if an error occurs, the **final result** should be as if the operation was **processed without errors**. To achieve this, **idempotence** is used to ensure that an operation has the **same effect** whether performed once or multiple times. Implementing this may require **additional metadata** management and **failover for fault tolerance**.
#### Duplicate suppression

**Duplicate suppression** occurs in various scenarios, not just in **stream processing**. For example, **TCP** uses **sequence numbers** to suppress **packet duplication**, but this only works within a **single connection**. However, in database transactions, **network interruptions** and **retries** pose risks of **duplicate executions**. For instance, **non-idempotent transactions** can result in **duplicated amounts** being transferred.

**Two-Phase Commit (2PC)** addresses some **transaction duplication** issues, but problems with **duplicate requests between end-users and servers** still remain. For example, during an **HTTP POST request**, a network issue may cause the user to **retry** the request, potentially leading to **duplicate processing**. Existing **duplicate suppression mechanisms** may not be sufficient in these cases.


#### Operation identifiers

To make operations **idempotent**, it is necessary to consider the entire **flow of network communication**, not just the **database transaction** mechanisms. For example, using **UUIDs** as **unique identifiers** for requests and passing these to the database can help **suppress duplicate requests**.

**Example 12-2** demonstrates how to prevent duplicate requests using a **request_id**. By enforcing **uniqueness constraints**, already processed requests are not executed again, thus suppressing duplicates while serving as an **event log**. Additionally, similarly to **event sourcing**, **account balance updates** can be derived from subsequent **events**, and **request IDs** can ensure that processing occurs **exactly once**.


#### The end-to-end argument

The **End-to-End Argument** is a crucial principle for issues such as **duplicate transaction suppression**. Described in 1984 by **Saltzer**, **Reed**, and **Clark**, this principle states that solutions to problems are only **fully correct** when they involve the knowledge and assistance of **application endpoints**. This implies that the communication system alone is not sufficient.

**Duplicate suppression** cannot be resolved solely at the TCP connection level or by **stream processors**; it is necessary to **pass transaction identifiers** from the **end-user client** to the **database**.

This principle also applies to issues like **data integrity** and **encryption**. Network checksums or encryption alone cannot prevent **software bugs**, **disk corruption**, or **server security breaches** that occur at the endpoints. Therefore, solving all **End-to-End** issues requires **unique solutions**.
#### Applying end-to-end thinking in data systems

Even when an application uses a **data system** providing strong **safety properties** like **serializable transactions**, **data loss** or **corruption** can still occur. **End-to-End** measures, such as resolving issues like **duplicate suppression** within the **application itself**, are necessary. While low-level **reliability mechanisms** like **TCP** work well, high-level fault tolerance mechanisms often lack the required **abstraction**, requiring direct handling by the application code.

**Transactions** offer a good **abstraction** for simplifying concurrency issues and faults, but in **distributed environments**, they can be costly, and directly handling fault tolerance in application code can be very challenging. Therefore, there is a need to explore **fault-tolerant abstractions** suitable for **large-scale distributed systems** while providing **End-to-End correctness**..


### Enforcing Constraints

Considering the concept of **unbundling** in databases and revisiting **correctness**, we observed that **End-to-End duplicate suppression** is achieved by passing **request IDs** from the client to the **database**. Now, let's examine other constraints, such as **uniqueness constraints**.

**Uniqueness constraints** are used in various situations. For example, **usernames** or **email addresses** must be unique, **file storage services** cannot store files with duplicate names, and **seat reservations** must also be unique. Constraints that prevent **negative account balances** or **inventory shortages** are equally important.

Techniques that ensure **uniqueness** are useful for applying these various constraints and play a critical role in maintaining **data integrity**.


#### Uniqueness constraints require consensus

In a **distributed environment**, applying **uniqueness constraints** requires **consensus**. When concurrent requests collide, the system must allow one request and reject the others. This may involve having a **leader node** with decision-making authority, but if the leader fails, **consensus issues** can arise again.

**Uniqueness checks** can be scaled by **partitioning** values, such as **request IDs** or **usernames**. However, **asynchronous multi-master replication** cannot guarantee uniqueness, so **synchronized coordination** is necessary.

#### Uniqueness in log-based messaging

In **log-based messaging systems**, all **consumers** process **messages** in the same order, ensuring **total order broadcast**, which is conceptually equivalent to **consensus**. To enforce **uniqueness constraints** in such systems, a **stream processor** can be used to process messages sequentially within **log partitions**.

1. Add **request messages** to logs partitioned by values requiring uniqueness.
2. **Stream processors** sequentially process messages from the logs, checking uniqueness constraints against the **local database**, and output **success** or **rejection messages**.
3. **Clients** receive responses about their requests from the output stream.

This approach can apply **uniqueness constraints** and handle various constraints with **potential conflicts** through **sequential processing**.

#### Multi-partition request processing

Processing operations involving multiple partitions while satisfying constraints and ensuring atomic execution is more complex. For example, an account transfer operation may involve independent partitions for the sender's account, recipient's account, and request ID. Traditional database approaches require atomic commits across partitions, enforcing a global order, but using partitioned logs can achieve the same correctness without this requirement.

1. The client assigns a unique request ID and adds the transfer request to the log partitions.
2. The stream processor reads requests from the log and sends debit and credit commands to the respective account partitions through output streams.
3. Other processors handle the commands, using request IDs to remove duplicates and update account balances.

This approach maintains the same correctness while handling multi-partition operations through logs, without requiring atomic commits.


### Timeliness and Integrity

One of the convenient properties of transactions is **linearizability**, which ensures that once a transaction is committed, it is immediately visible. However, due to the asynchronous nature of stream processors, immediate visibility might not be achievable when operations are broken down into multiple stages.

**Consistency** can be divided into two aspects:

1. **Timeliness**: This refers to the property that the system reflects the most up-to-date state, and weaker forms like **read-after-write consistency** can also be useful.
2. **Integrity**: This refers to the property of data being preserved correctly and without corruption. Violations of integrity can lead to persistent discrepancies.

**Timeliness violations** can be resolved with "eventual consistency," but **integrity violations** can be critical and lead to "perpetual inconsistency."

#### Correctness of dataflow systems  (데이터플로우 시스템의 정확성)

ACID transactions guarantee both timeliness (e.g., linearizability) and integrity (e.g., atomic commits), ensuring application correctness. In contrast, event-based dataflow systems handle timeliness and integrity separately. While timeliness might not be guaranteed when processing asynchronous event streams, integrity remains crucial.

**Exactly-once** execution is key for maintaining integrity in streaming systems, where **fault tolerance** in message delivery and **idempotent** operations are critical to prevent event loss or duplication. Such systems can enhance performance and operational stability while maintaining integrity, even without distributed transactions.

Mechanisms used for this include:

- **Event sourcing** that atomically handles write operations with a single message.
- Deriving state updates through **deterministic derived functions**.
- **End-to-end deduplication and idempotency** using request IDs.
- Keeping messages immutable for easier recovery in case of bugs.

These methods are promising for building fault-tolerant applications.

#### Loosely interpreted constraints

While traditional uniqueness constraints require consensus, many applications can operate effectively with a weaker notion of uniqueness. For example, issues like duplicate username registrations or stock shortages can be resolved with **compensating transactions**. This means that corrective measures are part of the business process, including apologies and compensation.

Some airlines and hotels allow overbooking, and banks charge fees for overdrafts, often violating constraints but compensating afterwards. If the cost of compensation is low, it may be more efficient to write data first and verify constraints later.

Thus, in these applications, while integrity is important, there is no need to enforce constraints immediately, and they can be addressed retrospectively.

#### Coordination-avoiding data systems

Through two key observations, dataflow systems can maintain integrity without the need for atomic commits or linearizability, and many applications can function well with loose constraints. Such **coordination-avoiding data systems** offer better performance and fault tolerance.

For example, these systems operate in a **multi-leader configuration**, with asynchronous replication across regions, allowing independent operation without synchronous coordination. Coordination can be introduced only when necessary for specific tasks, minimizing costs.

Coordination reduces inconsistencies but can degrade system performance and availability, so a **proper balance** must be found.


### Trust, but Verify

The correctness, integrity, and fault tolerance we discussed are based on certain **system models**. For example, it assumes that data written to disk is not lost and that memory is not corrupted. While these assumptions are generally reasonable, they are probabilistic issues in reality—some errors occur frequently, while others are rare.

For instance, data corruption on disks or networks, and hardware errors such as **random bit flips** can occur, though they are infrequent. These phenomena are rare, but with enough devices, they can actually occur. **Rowhammer** attacks illustrate hardware vulnerabilities.

#### Maintaining integrity in the face of software bugs

Software bugs can threaten data integrity beyond hardware issues, and these may not be caught by checksums in networks, memory, or filesystems. Even reliable databases like MySQL and PostgreSQL can experience bugs like uniqueness constraint failures or **write skew**. In application code, these bugs are more common, and even the integrity guarantees provided by databases (e.g., foreign keys, uniqueness constraints) might not be fully utilized.

**ACID** consistency is guaranteed only under the assumption of no bugs, and there is a risk of data integrity being compromised due to improper application use.

#### Don’t just blindly trust what they promise

Since data corruption is inevitably possible, there must be methods to verify and correct it. This requires **auditing**, which is crucial not only for financial systems but for all systems. Mature systems, such as **HDFS** or **Amazon S3**, periodically read data and compare it with other replicas to prevent corruption.

Verifying that data exists correctly is essential, and backups should be periodically restored and checked. Systems should not be **blindly trusted** to work correctly; instead, risk management should involve procedures for data verification.

#### A culture of verification

Systems like HDFS and S3 rely on disk reliability, but many systems do not adopt a "trust but verify" approach. Most systems ignore the possibility of data corruption and blindly trust the accuracy of technology. In the future, **self-verifying** or **self-auditing** systems are expected to become more common.

Previously, the reliability of ACID databases led to a lack of need for auditing mechanisms. However, with the standardization of weaker consistency guarantees in **NoSQL** databases, blind trust has become a riskier approach. Systems should now be designed with **auditability** in mind.

#### Designing for auditability

In databases where transactions modify multiple objects, understanding the meaning of a transaction post-facto can be challenging. Even if transaction logs are captured, it may not be clear why changes occurred across multiple tables. In contrast, **event-based systems** offer better auditability. In **event sourcing**, user inputs are recorded as immutable events, and state updates are derived deterministically and repeatably from these events.

These event logs can use **hashing** to verify integrity, and results can be validated by re-running processors or handling duplicates. Additionally, deterministic dataflow makes it easier to **debug** and **trace** why specific actions were taken. **Time-travel debugging** features allow unexpected situations to be reproduced, which is useful for diagnosis.

#### The end-to-end argument again

If we cannot fully trust individual system components to operate without error, we need to periodically check **data integrity**. Otherwise, corrupted data may be detected too late, leading to high costs for resolving issues downstream.

The most effective way to check data integrity is through an **end-to-end approach** that inspects the entire system. This helps implicitly verify problems that may arise in each step of the disk, network, service, and algorithm. Continuous end-to-end integrity checks enhance **system reliability** and facilitate swift and safe improvements.

#### Tools for auditable data systems

Many data systems do not prioritize auditability and even when self-audit mechanisms are implemented, ensuring the integrity of audit logs and database states remains challenging. Transaction logs can be signed by hardware security modules to prevent tampering, but it does not guarantee that transactions were recorded correctly from the start.

**Cryptographic tools** for ensuring system integrity against hardware, software, and even **malicious acts** are an intriguing area of research. **Cryptocurrencies, blockchain, and distributed ledger technologies** explore this area, with non-trusting organizations hosting their own replicas and using consensus protocols to maintain integrity.

Although there is skepticism about the **wastefulness of proof of work** and **Byzantine fault tolerance**, these technologies present interesting possibilities for integrity checks. For example, **Merkle trees** used in **cryptographic auditing** efficiently handle integrity verification.

These technologies are already in use for **certificate transparency** and are likely to see broader application in data systems in the future.

## Doing the Right Thing

In this book, we explored various data system architectures and their technical advantages and disadvantages, but one important discussion was missing—**ethical responsibility**. When designing systems, we need to carefully consider not only their intended purposes but also their unintended consequences. Beyond merely making money, we must consider the broader impact of the systems we create on the world.

Data often relates to people’s behaviors, interests, and identities, and we should handle this data with **humanity and respect**. Users are **humans**, and **human dignity** should always be a priority.

Software development involves significant **ethical choices**, and while there are guidelines such as the **ACM Code of Ethics for Software Engineering**, these are often overlooked in practice. Failing to consider the impact of technology and its outcomes on people can lead to negative results. Technology itself is neither inherently good nor bad; it is the use and outcomes of the technology that are important. We must not neglect **ethical responsibility**.
### Predictive Analytics

**Predictive analytics** is a crucial part of big data, addressing various issues such as forecasting weather or disease spread, and predicting the likelihood of recidivism, loan default, and insurance claims. These predictions can have direct impacts on individuals' lives.

For example, payment networks aim to prevent fraudulent transactions, banks want to avoid bad loans, airlines seek to prevent hijackings, and companies strive not to hire unreliable candidates. Organizations act carefully to reduce costs, and it may be more advantageous to decline questionable cases.

However, as **algorithm-based decision-making** becomes widespread, individuals classified as **risky** by algorithms may be systematically excluded from major parts of society. Such algorithmic prisons can restrict individual freedoms and, unlike the presumption of innocence in the criminal justice system, can prevent societal participation without evidence.
#### Bias and discrimination

Algorithmic decisions cannot be conclusively better or worse than human decisions, and biases and discriminatory practices can be institutionalized. While data-driven decisions might offer fair opportunities to those overlooked by traditional systems, the patterns learned by algorithms can be opaque and systematic biases can be learned.

Anti-discrimination laws prohibit discrimination based on protected characteristics, but issues may arise when personal data correlates with these characteristics. For example, if a zip code or IP address predicts race, it is unrealistic to believe that an algorithm can produce fair outcomes despite biased input data. Predictive analytics systems can perpetuate past discrimination, and ethical imagination and human involvement are necessary. Data and models are tools, not masters.

#### Responsibility and accountability

Automated decision-making raises issues of responsibility and accountability. While mistakes made by humans can be attributed to them, who is responsible for errors made by algorithms? Accidents involving autonomous vehicles or discriminatory credit scoring algorithms can result in unclear responsibilities and remedies. The explainability of decision-making processes in machine learning systems is also a concern when judicial review is involved.

Traditional credit scoring systems assign scores based on loan records, which can be corrected if errors are found, but machine learning-based algorithms are opaque and complex, making it difficult to understand decision processes. Predictive analytics are based on past data to make probabilistic forecasts but can be wrong in individual cases. Blind faith in data superiority is risky; responsible use of algorithms, ensuring transparency, and providing remedies for incorrect decisions are essential.

It is crucial to ensure that data does not harm individuals and to explore ways to realize its positive potential. While providing support through analysis to those in need, we must also prevent exploitative businesses from selling harmful products to vulnerable people.
#### Feedback loops

Predictive applications, especially recommendation systems, can create echo chambers that only show opinions that users agree with. This can lead to stereotypes, misinformation, and extreme polarization, as seen in the impact of social media on election campaigns.

When predictive analytics influence people's lives, harmful self-reinforcing feedback loops can occur. For instance, a lower credit score can make it harder to find a job, leading to unemployment, which in turn worsens the credit score in a downward spiral. Such feedback loops are difficult to predict but can be anticipated by considering the system as a whole. It is important to understand whether data analysis systems are reinforcing existing disparities or working to address inequities and to be mindful of unintended consequences.

### Privacy and Tracking

In addition to automated decision-making in predictive analytics, data collection itself can raise ethical issues. Data explicitly entered by users is stored as part of service provision, and users are considered customers. However, when users' activities are passively tracked and recorded, the purpose of data collection may conflict with users' understanding.

Tracking user behavior has become increasingly important in many online services, including click tracking on search results, recommendation features, and A/B testing, which provide benefits to users. However, in services funded by advertising, advertisers become the actual customers, and users' interests become secondary. In this case, data is collected in detail and stored for extended periods, used for marketing purposes. This can lead to data collection and tracking turning into surveillance.
#### Surveillance

Viewing data collection as surveillance can erase the positive connotations of typical phrases. For example, a statement like "Our surveillance-based organization collects real-time surveillance streams and stores them in a surveillance data warehouse. Our surveillance scientists use advanced analytics and surveillance processing to derive new insights" emphasizes the nature of surveillance.

We are currently diving into an all-encompassing surveillance world voluntarily, and sometimes enthusiastically, with IoT devices spread across our daily lives. These devices often have security vulnerabilities, and the data collected by companies can frequently turn into surveillance.

Not all data collection is considered surveillance, but it is important to understand the relationship between data collectors from the perspective of surveillance. The reason people willingly accept corporate surveillance might be because they feel they have nothing to hide, but this may not be true for everyone. If surveillance impacts significant life decisions like insurance rates or employment, it could be more than benign marketing and become intrusive. Data analytics technologies are advancing, and these technologies can lead to increasingly sophisticated and invasive outcomes.

#### Consent and freedom of choice

Users might argue that they consented to data collection by using services that track activities, but there are several issues in practice. Users often do not know how their data is used or processed, and privacy policies are often unclear. Some of the data includes information about individuals other than the service users, which they did not consent to.

Data is extracted unilaterally, and users have no opportunity to negotiate the terms of their relationship with the service. The relationship between services and users is asymmetric, with terms set by the service provider. Users who do not consent to surveillance have the choice not to use the service, but some services are essential for social participation, making this choice practically difficult. Especially in services with network effects, not participating can result in social costs.

Ultimately, only a privileged few with the time and knowledge to understand privacy policies can make meaningful choices, while for most people, surveillance becomes inevitable.

#### Privacy and use of data

The claim that "privacy is dead" is a misunderstanding. Privacy does not mean keeping everything secret but rather having the freedom to choose what to disclose and to whom. Data collection does not necessarily infringe on privacy rights, but the rights to data shift from individuals to companies. Companies keep surveillance results confidential, and intimate information about users is only indirectly revealed through targeted advertising.

Companies attempt to avoid the invasive nature of data collection and manage user perceptions, but this management is often poorly handled. Data can be incorrect or inappropriate, and algorithms do not recognize this unless explicitly programmed. Privacy settings provide a starting point for users to control data access, but services still have unrestricted access to and use of data.

Such massive transfer of privacy rights is historically unprecedented, and internet services are accumulating and using sensitive information on a large scale without user consent.

#### Data as assets and power

Behavioral data is generated from interactions with services and is often referred to as **data waste**. However, it is actually considered a **core asset**. Services collect and analyze personal data to support revenue models like **targeted advertising**. Data brokers buy, analyze, and resell personal data for **marketing** purposes.

Data is **valuable** to both corporations and governments, and it can be acquired through secret transactions, coercion, or legal compulsion. Data frequently poses **security issues** with high risks of leakage, leading to data being regarded as **toxic assets** or **hazardous materials**.

When collecting data, today’s political environment and future governments must be considered. The potential for technology to foster a **police state** should be kept in mind, as the accumulation of data and knowledge grants significant **power**. This can contribute to surveillance and population control by totalitarian governments, and modern tech companies also wield substantial **power**.
#### Remembering the Industrial Revolution

**Data** is a core characteristic of the information age, and internet and data processing technologies have a profound impact on the global economy and society. Compared to the Industrial Revolution, which achieved economic growth through technological advancements but caused issues like air pollution, water pollution, and poor working conditions, the information age faces similar challenges. The Industrial Revolution saw improvements in living standards through safety regulations and social reforms.

The information age is also facing significant challenges related to **data collection and usage**. Bruce Schneier has pointed out that data is the **pollution problem** of the information age and emphasizes that **privacy protection** is a critical environmental challenge. How we manage and protect data today will determine how it is evaluated in the future.
#### Legislation and self-regulation

**Data protection laws** help preserve individual rights but face challenges in today’s internet environment. For instance, the 1995 European Data Protection Directive specifies that data should be collected for legitimate purposes and not processed in ways incompatible with those purposes. However, the philosophy of big data aims to maximize data collection, combine it with other data, and use it for unforeseen purposes, conflicting with this directive.

Companies argue that regulation burdens innovation, but excessive regulation can stifle opportunities. For example, analyzing medical data could provide better diagnoses and treatments, but regulations might obstruct this process.

The tech industry needs a **cultural shift** regarding personal data, and users should not be viewed merely as data collection targets. To build and maintain **trust**, data collection and processing practices should be self-regulated, and users should be educated about data usage.

We must ensure **data control rights** and prevent surveillance from stripping these rights away. Data should be deleted immediately when no longer needed, and promising approaches like access control through encryption protocols are suggested. A cultural and attitudinal shift towards data protection is necessary.

