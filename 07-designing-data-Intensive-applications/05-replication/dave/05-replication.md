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
- Log-structured storage engine(LSM-Tree/SSTable)
  - This log is the main place for storage. Log segments are compacted and garbage-collected in the background.
-  B-tree
  - Overwrites individual disk blocks, every modification is first written to a write-ahead log so that the index can be restored to a consistent state after a crash.
- In either case, the log is an append-only sequence of bytes containing all writes to the database.
- We can use the exact same log to build a replica on another node: besides writing the log to disk, the leader also sends it across the network to its followers.
- When the follower processes this log, it builds a copy of the exact same data structures as found on the leader.
- Disadvantage
 - the log describes the data on a very low level
 - a WAL contains details of which bytes were changed in which disk blocks.
 - This makes replication closely coupled to the storage engine. If the database changes its storage format from one version to another, it is typically not possible to run different versions of the database software on the leader and the followers.
  - If the replication protocol allows the follower to use a newer software version than the leader, you can perform a zero-downtime upgrade of the database software by first upgrading the followers and then performing a failover to make one of the upgraded nodes the new leader.
  - Other than that, Down time for Upgrade.

#### Logical (row-based) log replication
- An alternative is to use different log formats for replication and for the storage engine, which allows the replication log to be decoupled from the storage engine internals.
- Logical log, to distinguish it from the storage engine’s (physical) data representation.
- A logical log for a relational database is usually a sequence of records describing writes to database tables at the granularity of a row:
  - •  For an inserted row, the log contains the new values of all columns.
  - • For a deleted row, the log contains enough information to uniquely identify the row that was deleted. Typically this would be the primary key, but if there is no primary key on the table, the old values of all columns need to be logged.
  - • For an updated row, the log contains enough information to uniquely identify the updated row, and the new values of all columns (or at least the new values of all columns that changed).
- A transaction that modifies several rows generates several such log records, followed by a record indicating that the transaction was committed. MySQL’s binlog (when configured to use row-based replication) uses this approach.
- Since a logical log is decoupled from the storage engine internals, it can more easily be kept backward compatible, allowing the leader and the follower to run different versions of the database software, or even different storage engines.
- A logical log format is also easier for external applications to parse. This aspect is useful if you want to send the contents of a database to an external system, such as a data warehouse for offline analysis, or for building custom indexes and caches (change data capture in Ch.11)

#### Trigger-based replication
- More flexibility
- ex)  if you want to only replicate a subset of the data, or want to replicate from one kind of database to another, or if you need conflict resolution logic
- then you may need to move replication up to the application layer.

- Some tools, such as Oracle GoldenGate [19], can make data changes available to an application by reading the database log. An alternative is to use features that are available in many relational databases: triggers and stored procedures.
- A trigger lets you register custom application code that is automatically executed when a data change (write transaction) occurs in a database system.
  - The trigger has the opportunity to log this change into a separate table, from which it can be read by an external process. That external process can then apply any necessary application logic and replicate the data change to another system. Databus for Oracle [20] and Bucardo for Postgres [21] work like this, for example.
  - Trigger-based replication typically has greater overheads than other replication methods, and is more prone to bugs and limitations than the database’s built-in replication.
  - However, it can nevertheless be useful due to its flexibility.


## Problems with Replication Lag
Replication? why? 
1. cuz of being able to tolerate node failures
2. scalability: processing more requests than a single machine can handle
3. latency: placing replicas geo-graphically closer to users

- Read-scaling architecture
  - you can increase the capacity for serving read-only requests simply by adding more followers.
  - Detail:
    - In the Leader-based replication, basically all writes to go through a single node, but read-only queries can go to any replica.
    - create many followers, and distribute the read requests across those followers.
    - This removes load from the leader and allows read requests to be served by nearby replicas.
  - But, synchronously replicate....
    - Only realistically works with asynchronous replication
    - because if you tried to synchronously replicate to all followers, a single node failure or network outage would make the entire system unavailable for writing.
    - The more nodes you have, the likelier it is that one will be down, so a fully synchronous configuration would be very unreliable.
  - But, asynchronous replication....
    - Out‐dated information(inconsistency) issue : if the follower has fallen behind
    - different results on the leader and a follower from same query - because not all writes have been reflected in the follower
    - if you stop writing to the database and wait a while, the followers will eventually catch up and become consistent with the leader. For that reason, this effect is known as eventual consistency.
  - The term “eventually” is deliberately vague:
    - in general, there is no limit to how far a replica can fall behind.
    - In normal operation, the delay between a write happening on the leader and being reflected on a follower—the replication lag—may be only a fraction of a second, and not noticeable in practice.
    - However, if the system is operating near capacity or if there is a problem in the network, the lag can easily increase to several seconds or even minutes.
    - When the lag is so large, the inconsistencies it introduces are not just a theoretical issue but a real problem for applications. 

- 3 problems caused by replication lag
- 1 solution

### Reading Your Own Writes
- When new data is submitted, it must be sent to the leader, but when the user views the data, it can be read from a follower. 
- This is especially appropriate if data is frequently viewed but only occasionally written.

> A problem in asynchronous replication

![](image/fg5-3.jpg "")

- if the user views the data shortly after making a write, the new data may not yet have reached the replica.
- To the user, it looks as though the data they submitted was lost, so they will be understandably unhappy.
- In this situation, we need *read-after-write consistency*(a.k.a. *read-your-writes consistency*)
  - This is a guarantee that if the user reloads the page, they will always see any updates they submitted themselves.
  - It makes no promises about other users: other users’ updates may not be visible until some later time.
  - However, it reassures the user that their own input has been saved correctly.
- How can we implement read-after-write consistency in a system with leader-based replication?
  1. When reading something that the user may have modified, read it from the leader; otherwise, read it from a follower. This requires that you have some way of knowing whether something might have been modified, without actually querying it. For example, user profile information on a social network is normally only editable by the owner of the profile, not by anybody else. Thus, a simple rule is: always read the user’s own profile from the leader, and any other users’ profiles from a follower.
  2. If most things in the application are potentially editable by the user, that approach won’t be effective, as most things would have to be read from the leader (negating the benefit of read scaling). In that case, other criteria may be used to decide whether to read from the leader. For example, you could track the time of the last update and, for one minute after the last update, make all reads from the leader. You could also monitor the replication lag on followers and prevent queries on any follower that is more than one minute behind the leader.
  3. The client can remember the timestamp of its most recent write—then the system can ensure that the replica serving any reads for that user reflects updates at least until that timestamp. If a replica is not sufficiently up to date, either the read can be handled by another replica or the query can wait until the replica has caught up. The timestamp could be a logical timestamp (something that indicates ordering of writes, such as the log sequence number) or the actual system clock (in which case clock synchronization becomes critical.
  4. If your replicas are distributed across multiple datacenters (for geographical proximity to users or for availability), there is additional complexity. Any request that needs to be served by the leader must be routed to the datacenter that contains the leader.

> Same users & multiple devices
-  In this case you may want to provide *cross-device* read-after-write consistency: if the user enters some information on one device and then views it on another device, they should see the information they just entered.
  - Approaches that require remembering the timestamp of the user’s last update become more difficult, because the code running on one device doesn’t know what updates have happened on the other device. This metadata will need to be centralized.
  - If your replicas are distributed across different datacenters, there is no guarantee that connections from different devices will be routed to the same datacenter. (For example, if the user’s desktop computer uses the home broadband connection and their mobile device uses the cellular data network, the devices’ network routes may be completely different.) If your approach requires reading from the leader, you may first need to route requests from all of a user’s devices to the same datacenter.


### Monotonic Reads
- when reading from asynchronous followers is that it’s possible for a user to see things moving backward in time.
  - This can happen if a user makes several reads from different replicas.

![](image/fg5-4.jpg "")
  
  -  For example, Figure 5-4 shows user 2345 making the same query twice, first to a follower with little lag, then to a follower with greater lag.
  -  (This scenario is quite likely if the user refreshes a web page, and each request is routed to a random server.)
  -  The first query returns a comment that was recently added by user 1234, but the second query doesn’t return anything because the lagging follower has not yet picked up that write.
  -  In effect, the second query is observing the system at an earlier point in time than the first query.
  -  This wouldn’t be so bad if the first query hadn’t returned anything, because user 2345 probably wouldn’t know that user 1234 had recently added a comment.
  -  However, it’s very confusing for user 2345 if they first see user 1234’s comment
- Monotonic reads [23] is a guarantee that this kind of anomaly does not happen.
- It’s a lesser guarantee than strong consistency, but a stronger guarantee than eventual consistency.
  - When you read data, you may see an old value; monotonic reads only means that if one user makes several reads in sequence, they will not see time go backward
  - i.e., they will not read older data after having previously read newer data.
  - One way of achieving monotonic reads is to make sure that each user always makes their reads from the same replica (different users can read from different replicas).
  - For example, the replica can be chosen based on a hash of the user ID, rather than randomly.
- However, if that replica fails, the user’s queries will need to be rerouted to appear, and then see it disappear again.

### Consistent Prefix Reads
>  Short dialog between Mr. Poons and Mrs. Cake

```
Mr. Poons: How far into the future can you see, Mrs. Cake? 
Mrs. Cake: About ten seconds usually, Mr. Poons.
```
- Now, imagine a third person is listening to this conversation through followers.
- The things said by Mrs. Cake go through a follower with little lag, but the things said by Mr. Poons have a longer replication lag
```
Mrs. Cake: About ten seconds usually, Mr. Poons. 
Mr. Poons: How far into the future can you see, Mrs. Cake?
```
- Preventing this kind of anomaly requires another type of guarantee: consistent prefix reads [23].
- This guarantee says that if a sequence of writes happens in a certain order, then anyone reading those writes will see them appear in the same order.
- This is a particular problem in partitioned (sharded) databases, which we will discuss in Chapter 6.
- If the database always applies writes in the same order, reads always see a consistent prefix, so this anomaly cannot happen. However, in many distributed databases, different partitions operate independently, so there is no global ordering of writes: when a user reads from the database, they may see some parts of the database in an older state and some in a newer state.
- One solution is to make sure that any writes that are causally related to each other are written to the same partition—but in some applications that cannot be done efficiently.
- There are also algorithms that explicitly keep track of causal dependencies, a topic that we will return to in “The “happens-before” relationship and concurrency”


![](image/fg5-5.jpg "")


### Solutions for Replication Lag
- When working with an eventually consistent system, it is worth thinking about how the application behaves if the replication lag increases to several minutes or even hours.
- If the answer is “no problem,” that’s great. However, if the result is a bad experience for users, it’s important to design the system to provide a stronger guarantee, such as read-after-write.
- Pretending that replication is synchronous when in fact it is asynchronous is a recipe for problems down the line.
- As discussed earlier, there are ways in which an application can provide a stronger guarantee than the underlying database—for example, by performing certain kinds of reads on the leader.
- However, dealing with these issues in application code is complex and easy to get wrong.
- It would be better if application developers didn’t have to worry about subtle replication issues and could just trust their databases to “do the right thing.”
- This is why transactions exist: they are a way for a database to provide stronger guarantees so that the application can be simpler.
- Single-node transactions have existed for a long time. However, in the move to distributed (replicated and partitioned) databases, many systems have abandoned them, claiming that transactions are too expensive in terms of performance and availability, and asserting that eventual consistency is inevitable in a scalable system.
- There is some truth in that statement, but it is overly simplistic, and we will develop a more nuanced view over the course of the rest of this book. We will return to the topic of transactions in Chapters 7 and 9, and we will discuss some alternative mechanisms in Part III.


## Multi-Leader Replication
> One major downside of the Leader-based replication
- only one leader, and all writes must go through it
- If you can’t connect to the leader for any reason, for example due to a network interruption between you and the leader
- you can’t write to the database.

> Multi-leader configuration (a.k.a master–master or active/active replication). 
- In this setup, each leader simultaneously acts as a follower to the other leaders.
  
### Use Cases for Multi-Leader Replication
#### Multi-datacenter operation
- Normal leader-based replication setup, the leader has to be in one of the datacenters
  - All writes must go through that datacenter.
- Multi-leader configuration
  - you can have a leader in each datacenter. Figure 5-6 shows what this architecture might look like.
  - Within each datacenter, regular leader–follower replication is used; between datacenters, each datacenter’s leader replicates its changes to the leaders in other datacenters.

![](image/fg5-6.jpg "")

> Single-leader vs multi-leader configurations in a multi-datacenter deployment:
- Performance
  - single-leader configuration: every write must go over the internet to the datacenter with the leader. This can add significant latency to writes and might 
contravene the purpose of having multiple datacenters in the first place.
  - In a multi-leader configuration, every write can be processed in the local datacenter and is replicated asynchronously to the other datacenters. Thus, the inter-datacenter network delay is hidden from users, which means the perceived performance may be better.

- Tolerance of datacenter outages
  - single-leader configuration: if the datacenter with the leader fails, failover can promote a follower in another datacenter to be leader.
  - multi-leader configuration: each datacenter can continue operating independently of the others, and replication catches up when the failed datacenter comes back online.
- Tolerance of network problems
  - Traffic between datacenters usually goes over the public internet, which may be less reliable than the local network within a datacenter.
  - single-leader configuration: is very sensitive to problems in this inter-datacenter link, because writes are made synchronously over this link.
  - multi-leader configuration: with asyn‐chronous replication can usually tolerate network problems better: a temporary network interruption does not prevent writes being processed.
- external tools to implment the multi-leader configuration such as Tungsten Replicator for MySQL, BDR for PostgreSQL, and GoldenGate for Oracle 

>  But, a big downside of multi-leader configuration
- the same data may be concurrently modified in two different datacenters, and those write conflicts must be resolved
- subtle configuration pitfalls and surprising interactions: autoincrementing keys, triggers, and integrity constraints can be problematic.
- For this reason, multi-leader replication is often considered dangerous territory that should be avoided if possible

#### Clients with offline operation
- multi-leader replication is appropriate is if you have an application that needs to continue to work while it is disconnected from the internet.
  - For example, consider the calendar apps on your mobile phone, your laptop, and other devices.
  - You need to be able to see your meetings (make read requests) and enter new meetings (make write requests) at any time, regardless of whether your device currently has an internet connection.
  - If you make any changes while you are offline, they need to be synced with a server and your other devices when the device is next online.
- In this case, every device has a local database that acts as a leader (it accepts write requests), and there is an asynchronous multi-leader replication process (sync) between the replicas of your calendar on all of your devices. The replication lag may be hours or even days, depending on when you have internet access available.
- From an architectural point of view, this setup is essentially the same as multi-leader replication between datacenters, taken to the extreme: each device is a “datacenter,” and the network connection between them is extremely unreliable. As the rich history of broken calendar sync implementations demonstrates, multi-leader replication is a tricky thing to get right.
- There are tools that aim to make this kind of multi-leader configuration easier. Forexample, CouchDB is designed for this mode of operation 

#### Collaborative editing
- Real-time collaborative editing applications allow several people to edit a document simultaneously.
  - For example, Etherpad [30] and Google Docs [31] allow multiple people to concurrently edit a text document or spreadsheet (the algorithm is briefly discussed in “Automatic Conflict Resolution” on page 174).
- When one user edits a document, the changes are instantly applied to their local replica (the state of the document in their web browser or client application) and asynchronously replicated to the server and any other users who are editing the same document.
- If you want to guarantee that there will be no editing conflicts, the application must obtain a lock on the document before a user can edit it.
- If another user wants to edit the same document, they first have to wait until the first user has committed their changes and released the lock.
- This collaboration model is equivalent to single-leader replication with transactions on the leader.
- However, for faster collaboration, you may want to make the unit of change very small (e.g., a single keystroke) and avoid locking.
- This approach allows multiple users to edit simultaneously, but it also brings all the challenges of multi-leader replication, including requiring conflict resolution [32].

### Handling Write Conflicts
- The biggest problem with multi-leader replication is that write conflicts can occur, which means that conflict resolution is required.
  - For example, consider a wiki page that is simultaneously being edited by two users, as shown in Figure 5-7. User 1 changes the title of the page from A to B, and user 2 changes the title from A to C at the same time. Each user’s change is successfully applied to their local leader. However, when the changes are asynchronously replicated, a conflict is detected [33]. This problem does not occur in a single-leader database.

![](image/fg5-7.jpg "")

#### Synchronous versus asynchronous conflict detection
- In a single-leader database, the second writer will either block and wait for the first write to complete, or abort the second write transaction, forcing the user to retry the write.
- In a multi-leader setup, On the other hand, both writes are successful, and the conflict is only detected asynchronously at some later point in time. At that time, it may be too late to ask the user to resolve the conflict.
- In principle, you could make the conflict detection synchronous—i.e., wait for the write to be replicated to all replicas before telling the user that the write was successful.
- However, by doing so, you would lose the main advantage of multi-leader replication: allowing each replica to accept writes independently.
- If you want synchronous conflict detection, you might as well just use single-leader replication.
- 
#### Conflict avoidance
- The simplest strategy for dealing with conflicts is to *avoid them*: if the application can ensure that all writes for a particular record go through the same leader, then conflicts cannot occur.
- Since many implementations of multi-leader replication handle conflicts quite poorly, avoiding conflicts is a frequently recommended approach [34]. 
  - For example, in an application where a user can edit their own data, you can ensure that requests from a particular user are always routed to the same datacenter and use the leader in that datacenter for reading and writing.
  - Different users may have different “home” datacenters (perhaps picked based on geographic proximity to the user), but from any one user’s point of view the configuration is essentially single-leader.
- However, sometimes you might want to change the designated leader for a record perhaps because one datacenter has failed and you need to reroute traffic to another datacenter, or perhaps because a user has moved to a different location and is now closer to a different datacenter.
- In this situation, conflict avoidance breaks down, and you have to deal with the possibility of concurrent writes on different leaders.

#### Converging toward a consistent state
- A single-leader database applies writes in a sequential order: if there are several updates to the same field, the last write determines the final value of the field.
- In a multi-leader configuration, there is no defined ordering of writes, so it’s not clear what the final value should be.
- In Figure 5-7, at leader 1 the title is first updated to B and then to C; at leader 2 it is first updated to C and then to B. Neither order is “more correct” than the other.
- If each replica simply applied writes in the order that it saw the writes, the database would end up in an inconsistent state: the final value would be C at leader 1 and B at leader 2.
- That is not acceptable—every replication scheme must ensure that the data is eventually the same in all replicas.
- Thus, the database must resolve the conflict in a convergent way, which means that all replicas must arrive at the same final value when all changes have been replicated.

- There are various ways of achieving convergent conflict resolution:
  - Give each write a unique ID (e.g., a timestamp, a long random number, a UUID, or a hash of the key and value), pick the write with the highest ID as the winner, and throw away the other writes. If a timestamp is used, this technique is known as last write wins (LWW). Although this approach is popular, it is dangerously prone to data loss [35]. We will discuss LWW in more detail at the end of this chapter (“Detecting Concurrent Writes” on page 184).
  - Give each replica a unique ID, and let writes that originated at a higher-numbered replica always take precedence over writes that originated at a lower-numbered replica. This approach also implies data loss.
  - Somehow merge the values together—e.g., order them alphabetically and then concatenate them (in Figure 5-7, the merged title might be something like “B/C”).
  - Record the conflict in an explicit data structure that preserves all information, and write application code that resolves the conflict at some later time (perhaps by prompting the user).
  
#### Custom conflict resolution logic
- most multi-leader replication tools let you write conflict resolution logic using application code. That code may be executed on write or on read:

- On write
  - As soon as the database system detects a conflict in the log of replicated changes, it calls the conflict handler.
    - For example, Bucardo allows you to write a snippet of Perl for this purpose. This handler typically cannot prompt a user—it runs in a background process and it must execute quickly.
- On read
  - When a conflict is detected, all the conflicting writes are stored. The next time the data is read, these multiple versions of the data are returned to the application.
  - The application may prompt the user or automatically resolve the conflict, and write the result back to the database. CouchDB works this way, for example.
- Note that conflict resolution usually applies at the level of an individual row or document, not for an entire transaction [36].
- Thus, if you have a transaction that atomically makes several different writes (see Chapter 7), each write is still considered separately for the purposes of conflict resolution.

> Automatic Conflict Resolution
- Conflict resolution rules can quickly become complicated, and custom code can be error-prone.
- Amazon is a frequently cited example of surprising effects due to a conflict resolution handler:
- for some time, the conflict resolution logic on the shopping cart would preserve items added to the cart, but not items removed from the cart.
- Thus, customers would sometimes see items reappearing in their carts even though they had previously been removed [37].
- There has been some interesting research into automatically resolving conflicts caused by concurrent data modifications. A few lines of research are worth mentioning:

  - • Conflict-free replicated datatypes (CRDTs) [32, 38] are a family of data structures for sets, maps, ordered lists, counters, etc. that can be concurrently edited by multiple users, and which automatically resolve conflicts in sensible ways. Some CRDTs have been implemented in Riak 2.0 [39, 40].
  - • Mergeable persistent data structures [41] track history explicitly, similarly to the Git version control system, and use a three-way merge function (whereas CRDTs use two-way merges).
  - • Operational transformation [42] is the conflict resolution algorithm behind collaborative editing applications such as Etherpad [30] and Google Docs [31]. It was designed particularly for concurrent editing of an ordered list of items, such as the list of characters that constitute a text document.
- Implementations of these algorithms in databases are still young, but it’s likely that they will be integrated into more replicated data systems in the future. Automatic conflict resolution could make multi-leader data synchronization much simpler for applications to deal with.

#### What is a conflict?
- Some kinds of conflict are obvious. In the example in Figure 5-7, two writes concurrently modified the same field in the same record, setting it to two different values. 
- There is little doubt that this is a conflict.
  - Other kinds of conflict can be more subtle to detect.
  - For example, consider a meeting room booking system: it tracks which room is booked by which group of people at which time.
  - This application needs to ensure that each room is only booked by one group of people at any one time (i.e., there must not be any overlapping bookings for the same room).
  - In this case, a conflict may arise if two different bookings are created for the same room at the same time. Even if the application checks availability before allowing a user to make a booking, there can be a conflict if the two bookings are made on two different leaders.
  - There isn’t a quick ready-made answer, but in the following chapters we will trace a path toward a good understanding of this problem. We will see some more examples of conflicts in Chapter 7, and in Chapter 12 we will discuss scalable approaches for detecting and resolving conflicts in a replicated system.
  
### Multi-Leader Replication Topologies
