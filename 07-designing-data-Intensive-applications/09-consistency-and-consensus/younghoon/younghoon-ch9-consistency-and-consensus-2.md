# Applications that Use Linearizability

Linearizability is a strong consistency guarantee used in distributed systems to ensure that operations appear to occur instantaneously and in a consistent order. Here are some applications and systems that use linearizability:

## 1. Databases and Distributed Storage Systems
- **Google Spanner**: Google Spanner provides globally distributed, strongly consistent transactions, ensuring linearizability across data centers using the TrueTime API.
- **Amazon DynamoDB (Strongly Consistent Reads)**: DynamoDB offers an option for strongly consistent reads, which guarantees linearizability, ensuring that a read operation always returns the most recent write.
- **CockroachDB**: CockroachDB is a distributed SQL database that provides linearizable transactions, ensuring that all operations appear in a single, global order.

## 2. Distributed Coordination Services
- **Apache ZooKeeper**: ZooKeeper is widely used for distributed coordination tasks like leader election and configuration management, offering linearizable reads and writes to maintain consistency across distributed systems.
- **etcd**: Used in Kubernetes and other distributed systems, etcd ensures linearizability for its read and write operations, providing consistent state across distributed components.

## 3. Consensus Algorithms
- **Paxos**: Paxos is a consensus algorithm that ensures linearizability in distributed systems, used in systems like Google Chubby and Microsoft Azure's Cosmos DB.
- **Raft**: Raft is a consensus algorithm that provides linearizability and is used in distributed systems like etcd, Consul, and others requiring reliable consensus and coordination.

## 4. Replicated State Machines
- **Chubby**: Google's distributed lock service, Chubby, relies on linearizability to ensure consistent lock operations across distributed nodes.
- **Multi-Paxos**: In systems using Multi-Paxos, such as Google Spanner, linearizability is used to maintain consistent state across replicas in distributed systems.

## 5. Messaging Systems
- **Apache Kafka (with strong consistency settings)**: Kafka, when configured with settings like "min.insync.replicas" and "acks=all," can provide linearizability for producing messages, ensuring consistent message delivery order across consumers.
  
## 6. Locking and Synchronization Services
- **Amazon DynamoDB with DAX (DynamoDB Accelerator)**: DynamoDB with DAX can be configured to provide linearizable reads, useful for synchronized access to distributed data.
- **Hazelcast**: Hazelcast, an in-memory data grid, offers linearizability for distributed data structures like maps, queues, and locks, ensuring atomic and consistent operations.

## 7. Distributed File Systems
- **Ceph (RADOS Gateway)**: Ceph ensures linearizability for object operations in its RADOS Gateway, providing strong consistency for object storage across distributed nodes.

## 8. Blockchain and Distributed Ledger Technologies
- **Hyperledger Fabric**: Hyperledger Fabric, a permissioned blockchain framework, uses linearizable transaction processing to ensure consistent and correct order of operations across the network.

## 9. Kubernetes
- **Kubernetes API Server**: The Kubernetes API server uses etcd as its backing store, which provides linearizability for operations like pod scheduling, deployments, and service discovery, ensuring consistent state across the cluster.

## Summary
Linearizability is essential in applications where consistency, correctness, and immediate visibility of operations are critical. These systems typically involve distributed components that must maintain a coherent and up-to-date state across different nodes or data centers, ensuring a seamless and reliable user experience. 



# General Estimates of Time Difference When Using Linearizability

Here’s a general estimate of the time difference when using linearizability compared to not using it, based on typical scenarios:

## 1. Single Data Center (Local Operations)
- **Linearizable Operations**:
  - **Time**: 1-5 milliseconds per operation.
  - **Reason**: Ensuring linearizability within a single data center typically involves a few network hops and possibly some disk I/O to synchronize state across nodes.
- **Non-Linearizable Operations**:
  - **Time**: 0.1-1 millisecond per operation.
  - **Reason**: Operations can be completed more quickly as they might only require local reads/writes without waiting for global synchronization.

**Difference**: **1-4 milliseconds** additional latency for linearizable operations.

## 2. Cross Data Center (Geographically Distributed)
- **Linearizable Operations**:
  - **Time**: 50-200 milliseconds per operation.
  - **Reason**: Synchronizing state across geographically distant data centers requires multiple network round-trips and coordination, especially in systems like Google Spanner.
- **Non-Linearizable Operations**:
  - **Time**: 5-20 milliseconds per operation.
  - **Reason**: Operations might be completed locally or with minimal synchronization, avoiding the need for global consensus.

**Difference**: **45-180 milliseconds** additional latency for linearizable operations.

## 3. Cloud Services (e.g., AWS, GCP)
- **Linearizable Operations (e.g., DynamoDB with Strongly Consistent Reads)**:
  - **Time**: 5-20 milliseconds per operation.
  - **Reason**: Ensuring a linearizable read or write across replicas adds some latency due to synchronization.
- **Non-Linearizable Operations (e.g., DynamoDB with Eventually Consistent Reads)**:
  - **Time**: 1-5 milliseconds per operation.
  - **Reason**: Operations can be faster as they might return data from the nearest replica without waiting for global synchronization.

**Difference**: **4-15 milliseconds** additional latency for linearizable operations.

## 4. Consensus-Based Systems (e.g., Paxos, Raft)
- **Linearizable Operations**:
  - **Time**: 10-50 milliseconds per operation.
  - **Reason**: Achieving consensus in distributed systems requires multiple nodes to agree on the operation order, which adds latency.
- **Non-Linearizable Operations**:
  - **Time**: 1-5 milliseconds per operation.
  - **Reason**: Without the need for global consensus, operations can be processed quickly, often from local data.

**Difference**: **9-45 milliseconds** additional latency for linearizable operations.

## Summary
- **Single Data Center**: Expect an additional **1-4 milliseconds** of latency for linearizable operations.
- **Cross Data Center**: Expect an additional **45-180 milliseconds** of latency for linearizable operations.
- **Cloud Services**: Expect an additional **4-15 milliseconds** of latency for linearizable operations.
- **Consensus-Based Systems**: Expect an additional **9-45 milliseconds** of latency for linearizable operations.

These estimates provide a general sense of the overhead associated with linearizability. The actual numbers can vary depending on the specific implementation and network conditions.


# End-User Services that Require Linearizability and Use Distributed Systems

End-user services that require linearizability and use distributed systems are typically those where high availability, fault tolerance, and consistent state across geographically distributed components are crucial. Here are some examples:

## 1. Global Payment and Banking Services
- **Cross-Border Payment Systems**: Services like Visa, Mastercard, and PayPal require linearizability to ensure that global transactions are processed consistently and accurately across all regions. Distributed systems provide the necessary availability and low latency.
- **Distributed Banking Platforms**: Modern banks like Goldman Sachs' Marcus, Revolut, and N26 use distributed systems to maintain consistent account states, transactions, and balances across global data centers, ensuring linearizable operations.

## 2. Global E-Commerce Platforms
- **Amazon**: Amazon uses distributed systems to manage inventory, orders, and shopping carts across the globe. Linearizability ensures that inventory updates and order statuses are consistent across all user interfaces.
- **Alibaba**: Alibaba's global e-commerce operations require linearizability for consistent order processing, payment confirmation, and inventory management across distributed data centers.

## 3. Social Media and Collaboration Tools with Global Reach
- **Facebook**: Facebook ensures that actions like posting, commenting, or liking are linearizable across its distributed system, maintaining a consistent view for users worldwide.
- **Slack**: Slack uses distributed systems to maintain linearizable message delivery and file updates across all team members, regardless of their location.

## 4. Online Gaming with Global Infrastructure
- **MMORPGs**: Games like World of Warcraft and Fortnite rely on distributed systems to maintain a consistent game state across all players worldwide. Linearizability ensures that actions and updates are reflected accurately.
- **Global Gaming Platforms**: Platforms like Steam and Xbox Live manage user accounts, purchases, and multiplayer sessions across distributed systems, ensuring consistent and linearizable operations.

## 5. Global Booking and Reservation Systems
- **Airbnb**: Airbnb uses distributed systems to manage property listings, bookings, and user data across the globe. Linearizability ensures that bookings are reflected immediately and consistently, preventing double bookings.
- **Uber**: Uber relies on distributed systems to manage ride requests, driver availability, and fare calculations, ensuring that state changes are consistent across all regions.

## 6. Distributed Content Delivery Networks (CDNs)
- **Netflix**: Netflix uses distributed systems to manage content delivery and user interactions. Linearizability ensures that user actions, like starting a show, are consistently reflected across all devices and servers.
- **Spotify**: Spotify uses distributed systems to manage streaming, playlist updates, and user preferences, ensuring that changes are reflected consistently and immediately.

## 7. Global Stock Trading Platforms
- **Robinhood**: Robinhood uses distributed systems to manage user portfolios, trades, and market data. Linearizability ensures that trades are processed correctly and portfolio states are updated consistently.
- **E*TRADE**: E*TRADE uses distributed systems to ensure that stock trades, account updates, and order executions are linearizable, providing consistent and immediate feedback across different regions.

## 8. Global Healthcare Platforms
- **Telemedicine Services**: Platforms like Teladoc or Amwell use distributed systems to manage patient records, appointments, and consultations. Linearizability ensures consistent and accurate patient data across global systems.
- **Distributed EHR Systems**: Electronic Health Record systems need linearizability to maintain consistent patient data across hospitals and clinics worldwide, ensuring accurate and timely information for healthcare providers.

---

These end-user services require linearizability to ensure that operations are consistent, correct, and reflect the most recent state across all distributed components. The use of distributed systems allows these services to maintain high availability and low latency on a global scale while ensuring that users experience a seamless and consistent service regardless of their location.
