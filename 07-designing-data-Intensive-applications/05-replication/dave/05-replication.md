# PART II - Distributed Data
- Now, in Part II, we move up a level and ask: what happens if multiple machines are involved in the storage and retrieval of data?
- various reasons why you might want to distribute a database across multiple machines:
  - Scalability: 
    - If your data volume, read load, or write load grows bigger than a single machine can handle, you can potentially spread the load across multiple machines.
  - Fault tolerance/high availability:
    - If your application needs to continue working even if one machine (or several machines, or the network, or an entire datacenter) goes down, you can use multiple machines to give you redundancy.
    - When one fails, another one can take over.
  - Latency:
    - Each user around the world can be served from a data center that is geo-graphically close to them.
    - That avoids the users having to wait for network packets to travel halfway around the world.

## Scaling to Higher Load
### Vertical scaling(Scaling up)
- Shared-memory architecture: Many CPUs, many RAM chips, and many disks can be joined together under one operating system.
  - Pros
     - Fast Interconnect with the memory or disk as a single machine
     - Hot-swappable components: you can replace disks, memory modules, and even CPUs without shutting down the machines
  - Cons:
     - High cost
     - geographical limit
     - Bottlenecks: a machine twice the size cannot necessarily handle twice the load
- Shared-memory architecture: Several machines with independent CPUs and RAM, but stores data on an array of disks that is shared between the machines with fast network
  - Good for some data warehousing workloads
  - Contention and the overhead of locking limit the scalability of the shared-disk approach

### Horizontal scaling(Scaling out)
- Shared-Nothing Architectures
- Each machine or virtual machine running the database software(called a *Node*). Each node uses its CPUs, RAM, and disks independently.
- Any coordination between nodes is done at the software level, using a conventional network.
- Pros
  - No special hardware is required by a shared-nothing system, so you can use whatever machines have the best price/performance ratio.
  - You can potentially distribute data across multiple geographic regions, and thus reduce latency for users and potentially be able to survive the loss of an entire data center.
  - With cloud deployments of virtual machines, you don’t need to be operating at Google scale: even for small companies, a multi-region distributed architecture is now feasible.
  - As an application developer, you need to be aware of the constraints and trade-offs that occur in such a distributed system across multiple node.
  - Very powerful
- Cons
  - Additional complexity for applications
  - In some cases,  In some cases, a simple single-threaded program can perform significantly better than a cluster with over 100 CPU cores.

### Replication Versus Partitioning
- Two common ways data is distributed across multiple nodes:
- Replication
  - Keeping a copy of the same data on several different nodes, potentially in different locations.
  - Replication provides redundancy: if some nodes are unavailable, the data can still be served from the remaining nodes.
  - Replication can also help improve performance.
  - in Chapter 5.
- Partitioning
  - Splitting a big database into smaller subsets called partitions so that different partitions can be assigned to different nodes (also known as sharding).
  - in Chapter 6.
```
[Follow-up]
The difficult trade-offs of distributed systems in Chapter 7. Transactions.
 - When things go wrong, what you(developer) can do
The fundamental limitations of distributed systems in Chapters 8 The Trouble with Distributed Systems/ 9 Consistency and Consensus
```

# CHAPTER 5 Replication
- Replication: keeping a copy of the same data on multiple machines that are connected via a network.
- Reasons why you might want to replicate data:
  - To keep data geographically close to your users (and thus reduce latency)
  - To allow the system to continue working even if some of its parts have failed (and thus increase availability)
  - To scale out the number of machines that can serve read queries (and thus increase read throughput)
- Let's assume that your dataset is so small that each machine can hold a copy of the entire dataset.
-  We will discuss three popular algorithms for replicating changes between nodes: single-leader, multi-leader, and leaderless replication.
-  Almost all distributed databases use one of these three approaches.


## Leaders and Followers
-  a replica: a copy of the database stored by a node.
-  Master-slave replication
  - a.k.a leader-based replication, active/passive replica 
  - The most common solution

![](image/fg5-1.jpg "")

- (1) Leader - One of the replicas(also known as master or primary)
  - clients want to write to the database,
  - clients send their requests to the leader, which first writes the new data to its local storage.
- (2) Followers - other replicas (read replicas, slaves, secondaries, or hot standbys)
  - Whenever the leader writes new data to its local storage, it also sends the data change to all of its followers as part of a replication log or change stream. 
  - Each follower takes the log from the leader and updates its local copy of the database accordingly, by applying all writes in the same order as they were processed on the leader.
- (3) When a client wants to read from the database, it can query either the leader or any of the followers.
  - writes are only accepted on the leader
  - read-only is on the followers from the client’s point of view
- RDBS with this mode: PostgreSQL/ MySQL/ Oracle Data Guard, and SQL Server’s AlwaysOn Availability Groups
- non-RDBS with this mode: MongoDB/ RethinkDB/ Espresso
- Other usage of leader-based replication:  distributed message brokers(Kafka and RabbitMQ)

 ### Synchronous Versus Asynchronous Replication
 ![](image/fg5-2.jpg "")
 
 - Because of follower 1(Sync), the leader waits the follower's reply to report success to the user
 - Because of follower 2(Async), the leader don't wait but there is a substantial delay before follower 2 processes the message. 
 - Cases when followers might fall behind the leader by several minutes or more
   -  if a follower is recovering from a failure, if the system is operating near maximum capacity, or if there are network problems between the nodes.
- The advantage of synchronous replication
  - is that the follower is guaranteed to have an up-to-date copy of the data that is consistent with the leader. If the leader suddenly fails, we can be sure that the data is still available on the follower. 
- The disadvantage is that
  - if the synchronous follower doesn’t respond (because it has crashed, or there is a network fault, or for any other reason), the write cannot be processed.
  - The leader must block all writes and wait until the synchronous replica is available again.
- Semi-synchronous
  - It is impractical for all followers to be synchronous: any one node outage would cause the whole system to grind to a halt.
  - if you enable synchronous replication on a database, it usually means that one of the followers is synchronous, and the others are asynchronous.
  - If the synchronous follower becomes unavailable or slow, one of the asynchronous followers is made synchronous.
  - This guarantees that you have an up-to-date copy of the data on at least two nodes: the leader and one synchronous follower.
-  leader-based replication is configured to be completely asynchronous. In this case, if the leader fails and is not recoverable, any writes that have not yet been replicated to followers are lost.
-  fully asynchronous configuration has the advantage that the leader can continue processing writes, even if all of its followers have fallen behind.

- Weakening durability may sound like a bad trade-off, but asynchronous replication is nevertheless widely used, especially if there are many followers or if they are geographically distributed.

> Research on Replication
- It can be a serious problem for asynchronously replicated systems to lose data if the leader fails, so researchers have continued investigating replication methods that do not lose data but still provide good performance and availability.
- For example, chain replication [8, 9] is a variant of synchronous replication that has been successfully implemented in a few systems such as Microsoft Azure Storage [10, 11].
- There is a strong connection between consistency of replication and consensus (getting several nodes to agree on a value).

### Setting Up New Followers
- How do you ensure that the new follower has an accurate copy of the leader’s data?
- Simply copying data files from one node to another is typically not sufficient: Time difference - The data is always in flux.
- Locking the database? Not high availability
- Answer) Setting up a follower: No downtime
  - 1. Take a consistent *SNAPSHOT* of the leader’s database at some point in time—if possible, without taking a lock on the entire database.
    -  Most databases have this feature, as it is also required for backups. In some cases, Third-party tools are needed, such as innobackupex for MySQL.
  - 2. Copy the snapshot to the new follower node.
  - 3. The follower connects to the leader and requests all the data changes that have happened since the snapshot was taken.
       This requires that the snapshot is associated with an exact position in the leader’s replication log. That position has various names:
       -  for example, PostgreSQL calls it the log sequence number, and MySQL calls it the binlog coordinates.
  - 4. When the follower has processed the backlog of data changes since the snapshot, we say it has caught up. It can now continue to process data changes from the leader as they happen.
   
### Handling Node Outages
> Our goal is to keep the system as a whole running despite individual node failures, and to keep the impact of a node outage as small as possible.

#### A. Follower failure: Catch-up recovery
- On its local disk, each follower keeps a log of the data changes it has received from the leader.
- the follower can recover quite easily from its log,

#### B. Leader failure: Failover
- Failover
  - one of the followers needs to be promoted to be the new leader
  - clients need to be reconfigured to send their writes to the new leader
  - the other followers need to start consuming data changes from the new leader
 
- An automatic failover process
  - 1. Determining that the leader has failed. There are many things that could potentially go wrong: crashes, power outages, network issues, and more.
    - There is no foolproof way of detecting what has gone wrong, so most systems simply use a timeout: nodes frequently bounce messages back and forth between each other, and if a node doesn’t respond for some period of time—say, 30 seconds—it is assumed to be dead.
    - (If the leader is deliberately taken down for planned maintenance, this doesn’t apply.)
  - 2. Choosing a new leader. This could be done through an election process (where the leader is chosen by a majority of the remaining replicas), or a new leader could be appointed by a previously elected controller node.
    - The best candidate for leadership is usually the replica with the most up-to-date data changes from the old leader (to minimize any data loss).
    - Getting all the nodes to agree on a new leader is a consensus problem, discussed in detail in Chapter 9.
  - 3. Reconfiguring the system to use the new leader.
    - Clients now need to send their write requests to the new leader (we discuss this in “Request Routing” on page 214).
    - If the old leader comes back, it might still believe that it is the leader, not realizing that the other replicas have forced it to step down.
    - The system needs to ensure that the old leader becomes a follower and recognizes the new leader.

- things that can go wrong by Failover
  - • If asynchronous replication is used, the new leader may not have received all the writes from the old leader before it failed. If the former leader rejoins the cluster after a new leader has been chosen, what should happen to those writes? The new leader may have received conflicting writes in the meantime. The most common solution is for the old leader’s unreplicated writes to simply be discarded, which may violate clients’ durability expectations.
  - • Discarding writes is especially dangerous if other storage systems outside of the database need to be coordinated with the database contents. For example, in one incident at GitHub [13], an out-of-date MySQL follower was promoted to leader. The database used an autoincrementing counter to assign primary keys to new rows, but because the new leader’s counter lagged behind the old leader’s, it reused some primary keys that were previously assigned by the old leader. These primary keys were also used in a Redis store, so the reuse of primary keys resulted in inconsistency between MySQL and Redis, which caused some private data to be disclosed to the wrong users.
  - • In certain fault scenarios (see Chapter 8), it could happen that two nodes both believe that they are the leader. This situation is called split brain, and it is dangerous: if both leaders accept writes, and there is no process for resolving conflicts (see “Multi-Leader Replication” on page 168), data is likely to be lost or corrupted. As a safety catch, some systems have a mechanism to shut down one node if two leaders are detected.ii However, if this mechanism is not carefully designed, you can end up with both nodes being shut down.
  - • What is the right timeout before the leader is declared dead? A longer timeout means a longer time to recovery in the case where the leader fails. However, if the timeout is too short, there could be unnecessary failovers. For example, a temporary load spike could cause a node’s response time to increase above the timeout, or a network glitch could cause delayed packets. If the system is already struggling with high load or network problems, an unnecessary failover is likely to make the situation worse, not better.

> Fundamental problems in distributed systems

-> Node failures; Unreliable networks; and trade-offs around replica consistency, durability, availability, and latency (In Chapter 8 and Chapter 9)

### Implementation of Replication Logs
> Several different replication methods
#### Statement-based replication
  - In the simplest case, the leader logs every write request (statement) that it executes and sends that statement log to its followers.
  - For a relational database, this means that every INSERT, UPDATE, or DELETE statement is forwarded to followers, and each follower parses and executes that SQL statement as if it had been received from a client.
  - Disorder cases
     - Any statement that calls a nondeterministic function, such as NOW() to get the current date and time or RAND() to get a random number, is likely to generate a different value on each replica.
     - If statements use an autoincrementing column, or if they depend on the existing data in the database (e.g., UPDATE … WHERE <some condition>), they must be executed in exactly the same order on each replica, or else they may have a different effect. This can be limiting when there are multiple concurrently executing transactions.
     - Statements that have side effects (e.g., triggers, stored procedures, user-defined functions) may result in different side effects occurring on each replica, unless the side effects are absolutely deterministic.
#### Write-ahead log (WAL) shipping
#### Logical (row-based) log replication
#### Trigger-based replication

## Problems with Replication Lag
