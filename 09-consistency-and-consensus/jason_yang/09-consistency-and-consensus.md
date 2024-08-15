### Distributed System Challenges

**Distributed System Challenges**: Distributed systems face a variety of challenges like network delays, packet loss, message duplication, message reordering, and node failures. The simplest way to handle these issues might be to crash the system and show an error message to the user, but to avoid this, we need to build fault-tolerant systems.

**Building Fault Tolerance**: The best way to build fault tolerance is by providing general abstractions that handle these failures. Just like transactions allow applications to ignore crashes or disk errors, we can create an abstraction layer that helps applications manage these issues without needing to deal with them directly.

**Importance of Consensus**: One of the most critical abstractions in distributed systems is **consensus**. Consensus ensures that all nodes (servers) agree on the same outcome. For example, if a database leader (the main server) dies, the remaining nodes use consensus to choose a new leader. A proper consensus implementation prevents issues like **split brain**, where different parts of the system might have conflicting information.

**Limits of Distributed Systems**: The limits of how systems can tolerate failures and continue to operate are well studied, both in theory and practice. This chapter explores these limits and offers references for further reading.

### Consistency Guarantees

**Replication Lag Issue**: In distributed databases, different database nodes might see different data at the same time. This happens because write requests reach different nodes at different times, and it can occur no matter what replication method is used (single-leader, multi-leader, or leaderless).

**Eventual Consistency**: Most replicated databases offer at least **eventual consistency**. This means that after some time, all read requests will return the same value. However, this is a weak guarantee because it doesn't specify when all nodes will have the same data, and until then, read requests might return outdated information. For example, if you write a value and then immediately read it, you might get the old value from another replica.

**Challenges for Developers**: **Eventual consistency** can be confusing for developers. In a typical single-threaded program, when you assign a value to a variable and then immediately read it, you expect to see the assigned value. With eventual consistency, this isn't always the case, leading to subtle bugs that are hard to catch, especially in network failure scenarios or high concurrency.

**Stronger Consistency Models**: This chapter will explore consistency models that are stronger than eventual consistency. While these models might trade off performance or fault tolerance, they can be easier to use correctly. Transaction isolation and distributed consistency are related but address different problems.

**Linearizability**: One of the strongest consistency models is **linearizability**. It ensures that all operations happen in a sequential order, and all clients see the same data. We will examine the pros and cons of linearizability and explore how to guarantee the order of events in a distributed system, as well as how to atomically commit distributed transactions.

### Linearizability

**Concept of Linearizability**: **Linearizability** is a strong consistency model that makes a database appear as if it has only one copy of the data. In this model, when one client writes data, all clients must immediately be able to read that new data. This provides a **recency guarantee**, meaning that data is always up-to-date.

**Example of Linearizability**: In a system that does not guarantee linearizability, different clients might see different versions of the data at the same time. For example, if Alice and Bob check the score of a soccer game, Alice might see the latest score while Bob sees an older score. This violates linearizability.

**Guaranteeing Linearizability**: In a linearizable system, once a write operation is complete, all subsequent read operations must return that value. This means that data changes occur atomically at a specific point in time, and all reads after that point will see the new value.

**Linearizability vs. Serializability**:
- **Serializability** ensures that transactions appear to be executed in some sequential order.
- **Linearizability** guarantees that a single object (like a key, row, or document) is always up-to-date, but it does not bundle multiple transactions together.

### Relying on Linearizability

**Usefulness of Linearizability**: **Linearizability** is crucial in systems where all clients must see up-to-date data. While a few seconds of delay might not matter in some situations, in certain cases, it is essential.

**Locking and Leader Election**: In single-leader replication systems, a **linearizable** lock is used to ensure that only one leader exists at a time. If the lock isn't linearizable, multiple nodes might mistakenly think they are the leader. Services like **Apache ZooKeeper** and **etcd** support these linearizable locks and leader elections.

**Constraints and Uniqueness Guarantees**: To ensure that a username or file name is unique in a database, **linearizability** is necessary. All nodes must check the same up-to-date value to avoid conflicts and ensure uniqueness.

**Cross-Channel Timing Dependencies**: In systems with more than one communication channel, **linearizability** is essential to avoid race conditions. For example, timing issues between a file storage service and a message queue could cause data inconsistencies, which **linearizability** can help prevent.

### Implementing Linearizable Systems

**Implementing Linearizability**: To implement **linearizability**, the system must appear as though it has only one copy of the data. However, using just a single copy is prone to failures, so replication is used to build a fault-tolerant system.

**Single-Leader Replication**: If the leader handles all writes and followers stay synchronized, the system can be **linearizable**. But, asynchronous replication or the leader’s failure to correctly synchronize can violate linearizability.

**Consensus Algorithms**: **Consensus algorithms** are similar to single-leader replication but have additional safeguards to prevent **split brain** and stale replicas, making them safer for implementing linearizability. Examples include **ZooKeeper** and **etcd**.

**Multi-Leader Replication**: **Multi-leader replication** is generally not **linearizable** because multiple nodes handle writes simultaneously.

**Leaderless Replication**: **Leaderless replication** (like **Dynamo-style** systems) uses quorums to claim strong consistency, but due to network delays and clock mismatches, it might not always be **linearizable**. Especially, conflict resolution methods like “last write wins” do not guarantee linearizability.

**Linearizability and Quorums**: Even strict quorums can fail to maintain **linearizability** due to race conditions caused by network delays. Therefore, it is safer to assume that **Dynamo-style** replication does not provide **linearizability**.

### The Cost of Linearizability

**Trade-Offs of Linearizability**: **Linearizability** ensures consistency but can come at the cost of performance and availability. During network partitions, maintaining **linearizability** may prevent some replicas from processing requests, reducing availability. Conversely, maintaining availability might require sacrificing consistency.

**CAP Theorem**: According to the **CAP theorem**, during a network partition, a system must choose between **consistency** (linearizability) and **availability**. This trade-off is a key consideration in distributed system design. However, **CAP theorem** applies to a narrow context, and other trade-offs should also be considered in system design.

**Performance Impact of Linearizability**: Systems that maintain **linearizability** often experience performance degradation. Ensuring linearizability takes more time, especially in networks with significant delays. Even modern multi-core CPUs sometimes sacrifice **linearizability** for better performance.

### Ordering Guarantees

**Importance of Ordering**: Ordering is a crucial concept in databases and distributed systems. It defines the sequence in which operations are executed and is essential for maintaining causality (the relationship between cause and effect). Ordering is discussed in multiple chapters, as it relates to database consistency, transaction serialization, and time synchronization in distributed systems.

**Causality and Ordering**: Causality imposes an order between events. For example, a question comes before an answer, and a message must be sent before it can be received. A system that maintains causal consistency ensures that operations happen in the order that respects their causal relationships. In other words, an operation will only proceed after the operations that causally precede it are completed.

**Total Ordering and Partial Ordering**: **Linearizability** guarantees a total order of operations, meaning that the system processes all operations in a single timeline, and the order between operations is clear. On the other hand, **causal consistency** defines a partial order, where the order is not defined for concurrent operations.

**Linearizability and Causal Consistency**: While **linearizability** provides causal consistency, it can impact performance and availability. **Causal consistency**, on the other hand, offers better performance and availability by not requiring a strict order for concurrent operations. In many cases, **causal consistency** can be a more efficient alternative to **linearizability**.

**Implementing Causal Consistency**: To maintain **causal consistency**, a system needs to track dependencies between operations. This involves tracking which data was read and written during each operation and ensuring that operations that depend on each other are executed in the correct order.

In conclusion, **ordering** and **causality** are central to maintaining consistency in distributed systems and databases. While **linearizability** ensures a total order of operations, **causal consistency** offers a valuable alternative that balances performance and availability.

### Sequence Number Ordering

**The Need for Sequence Numbers**: Tracking causality can be complex and inefficient, so using sequence numbers or timestamps to order operations is preferred.

 These sequence numbers are unique for each operation, allowing the system to establish a total order of operations.

**Total Ordering and Causality**: Sequence numbers or timestamps provide a total order, but they don't always align with causal relationships. A total order that doesn't match causality can be meaningless, and sometimes a causal order is necessary.

**Lamport Timestamps**: One method to generate sequence numbers that align with causality is **Lamport Timestamps**. Each node increases its counter with each operation and incorporates the counters of other nodes when they communicate, ensuring that the order respects causality.

**Limitations and Solutions**: **Lamport Timestamps** provide a causality-respecting order but aren't enough to resolve issues like ensuring uniqueness. For example, when multiple nodes try to create the same username at the same time, **Lamport Timestamps** alone can't prevent conflicts. To resolve this, a guarantee that the total order is fixed at the time of message delivery is needed, which ties into **Total Order Broadcast**.

### Total Order Broadcast

**Concept Explanation**: **Total Order Broadcast** is a protocol in distributed systems that ensures all nodes process operations in the same order. It guarantees that messages are not lost, are delivered to all nodes, and are processed in the same order on each node. This protocol is used in database replication, implementing serializable transactions, writing logs, and locking services.

**Use Cases**: **Total Order Broadcast** extends single-leader replication to improve throughput and handle failures. Since operations are applied to the database in the order they were logged, consistency between replicas is maintained.

**Relationship with Linearizable Storage**: **Total Order Broadcast** can be used to build linearizable storage, and conversely, linearizable storage can be used to implement **Total Order Broadcast**. This bidirectional relationship shows how these problems are closely tied to consensus problems.

**Importance**: **Total Order Broadcast** provides stronger guarantees than timestamp ordering because it fixes the order at the time of message delivery, ensuring causal consistency. This is essential for understanding and implementing linearizable systems.

**Connection with Consensus**: Both **Total Order Broadcast** and linearizable operations are equivalent to solving consensus problems. This offers significant insights into maintaining consistency and improving performance in distributed systems.

### Distributed Transactions and Consensus

**Consensus** is the problem of getting multiple nodes in a distributed system to agree on a specific value or state. This is a complex and critical issue because, without consensus, it's hard to maintain system consistency. Consensus is essential for leader election, atomic commits, and preventing split brain scenarios.

#### Importance of Consensus
- **Leader Election**: In distributed databases using single-leader replication, all nodes must agree on who the leader is. If they don't, a split brain scenario can occur, leading to data loss.
- **Atomic Commit**: When transactions span multiple nodes, all nodes must agree on the same outcome to maintain the atomicity of the transaction.

#### Impossibility of Consensus
The **FLP Impossibility Theorem** states that no algorithm can guarantee consensus in an asynchronous system where nodes can fail. However, in practice, systems overcome this limitation using timeouts or randomness.

#### Two-Phase Commit (2PC)
**2PC** is an algorithm used to solve the atomic commit problem. It's widely used in many systems but has limitations, such as the potential to halt the entire system if a node fails.

Later on, better consensus algorithms used in systems like **ZooKeeper** and **etcd** are discussed, which offer more stability and efficiency than **2PC**.

### Atomic Commit and Two-Phase Commit (2PC)

**Atomicity** ensures that a transaction is either fully completed or not completed at all. This is particularly important when a transaction involves multiple database nodes or partitions. In distributed transactions, all nodes must agree on the same result, and this is where the **Two-Phase Commit (2PC)** algorithm comes into play.

#### Two-Phase Commit (2PC)

**2PC** ensures that a transaction is committed or aborted atomically across multiple nodes. It works in two phases:

1. **Prepare Phase**: The **Coordinator** asks all participating nodes (participants) if they are ready to commit the transaction. Each node replies with "Yes" or "No."
2. **Commit/Abort Phase**: If all nodes reply "Yes," the **Coordinator** instructs all nodes to commit the transaction. If any node replies "No," the **Coordinator** instructs all nodes to abort the transaction.

This protocol ensures the **Atomicity** of the transaction, preventing scenarios where some nodes commit and others abort.

#### Coordinator Failure

One of the weaknesses of **2PC** is that if the **Coordinator** fails, the transaction may enter an **In-Doubt** state, where the participants don't know whether to commit or abort. They must wait for the **Coordinator** to recover.

#### Three-Phase Commit (3PC)

**3PC** is designed to prevent the transaction from stalling if the **Coordinator** fails. However, **3PC** assumes that network delays and node response times are bounded, which isn't always the case in real-world systems. Therefore, **2PC** is still more widely used, with **Atomicity** being ensured through the recovery of the **Coordinator**.

### Distributed Transactions in Practice

**Distributed Transactions** are implemented using protocols like **Two-Phase Commit (2PC)** and play a crucial role in maintaining consistency across distributed systems. However, they also come with operational challenges and performance overheads, which is why many cloud services choose not to use them. **2PC**, in particular, can significantly slow down performance, and **Distributed Transactions** in systems like **MySQL** can be more than 10 times slower than single-node transactions.

**Two Types of Distributed Transactions**:
1. **Database-internal distributed transactions**: Transactions between nodes within a single database system.
2. **Heterogeneous distributed transactions**: Transactions that span across different systems.

**Exactly-once Message Processing** is an example where **Distributed Transactions** provide strong consistency between a message broker and a database. However, this requires all systems involved to support the same **Atomic Commit** protocol.

**XA Transactions** are a standard for implementing **Two-Phase Commit** across different systems. But **XA** implementations have several issues. Specifically, if the **Coordinator** crashes, the transaction might be left **In-Doubt**, leading to locks being held for an extended period, which can block other transactions. If the **Coordinator** does not recover, an administrator must manually resolve the transaction.

**Limitations of Distributed Transactions**:
- The **Coordinator** can be a single point of failure, and if not sufficiently replicated, **In-Doubt** transactions can block the entire system.
- **XA** is designed as a least common denominator, making it incompatible with deadlock detection across systems or **Serializable Snapshot Isolation (SSI)**.
- **Distributed Transactions** are prone to system failures and can contradict the goal of building fault-tolerant systems.

### Fault-Tolerant Consensus

**Consensus** solves the problem of multiple nodes agreeing on a specific value or decision. For example, when multiple users try to book the last seat on a flight or a theater at the same time, a **Consensus algorithm** decides which request succeeds.

A **Consensus problem** must meet the following properties:
1. **Uniform Agreement**: All nodes must choose the same value.
2. **Integrity**: Each node can decide only once.
3. **Validity**: The chosen value must have been proposed by one of the nodes.
4. **Termination**: In the absence of failures, nodes must eventually decide on a value.

**Termination** is about fault tolerance. Even if a majority of nodes fail, the remaining nodes should be able to decide. Meanwhile, **Uniform Agreement**, **Integrity**, and **Validity** are safety properties that must never be violated.

Many **Consensus algorithms** like Paxos, Raft, and Zab implement **Total Order Broadcast** to ensure that messages are delivered to all nodes in the same order, guaranteeing consensus across nodes.

**Fault-Tolerant Consensus** is critical in ensuring that a system doesn't make incorrect decisions even in the presence of failures. A majority of nodes must function correctly to maintain progress, and the system must maintain correct decisions even during failures.

### Single-Leader Replication and Consensus

**Single-leader replication** involves sending all write requests to a leader, who then replicates them to followers in the same order, ensuring data consistency. This process is similar to **Total Order Broadcast**, where the leader determines the order of all write operations.

The **Consensus** problem arises when selecting a leader. If the leader is chosen manually, human intervention is needed, and if the system automatically selects a leader, it must handle failures and choose a new leader. During this process, all nodes must agree on who the leader is to avoid **split brain** scenarios. To achieve this agreement, a **Consensus** algorithm is necessary.

**Consensus** is solved using **epoch numbering** and **quorums**. When the leader proposes a value, a majority of nodes must agree to accept the proposal. If the proposal is accepted, the leader is confirmed as valid. This ensures that the leader is unique and that the system remains consistent even after a new leader is chosen.

**Consensus algorithms** are fault-tolerant but come with performance costs and complexity. If more than half the nodes fail, the system cannot progress, and network delays can lead to performance degradation due to incorrect leader detection.

**ZooKeeper** and similar systems manage node coordination, ensuring that even during failures, the system can automatically recover. These systems primarily handle leader election, partition allocation, and other critical tasks, typically using a fixed number of nodes to perform majority voting. **ZooKeeper** allows some of the coordination work to be outsourced to an external service, helping applications automatically recover from failures.

### Service Discovery

**ZooKeeper**, **etcd**, and **Consul** are often used for service discovery, the process of finding the IP address of a service that needs to be accessed. In cloud environments, virtual machines are frequently created and removed, so it's not always possible to know a service's IP address in advance. Instead, when a service starts, it registers its network endpoint with a **service registry**, and other services can find it through this registry.

**Service Discovery** doesn’t always require **Consensus**, but leader election and other specific functions do. Traditional DNS is the standard method for looking up an IP address for a service name, using multiple layers of caching to improve performance and availability. However, DNS query results are not necessarily up-to-date, and linearizability is not guaranteed.

**Membership Services**: Systems like ZooKeeper are part of the membership service research that dates back to the 1980s. Membership services play a crucial role in building highly reliable systems, like air traffic control systems. They determine which nodes are active members of a cluster. As discussed in chapter 8, it’s impossible to reliably detect when another node has failed due to unbounded network delays. However, by combining failure detection with consensus, nodes can agree on which nodes should be considered alive.

Although it’s possible for a node to be mistakenly declared dead by consensus even if it’s still alive, having a consistent view of membership is still highly useful. For example, a leader can be elected by selecting the node with the lowest number among the current members. But if nodes disagree on who the current members are, this approach won’t work.
