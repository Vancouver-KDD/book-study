# PART II - Distributed Data
- Various reasons why we want to distribute a database across multiple machines:
  - Scalability: 
    - Data volume, read load, or write load grows bigger than a single machine can handle, you can spread the load across multiple machines.
  - Fault tolerance/high availability:
    - If an application needs to continue working even if one machine (or several machines, or the network, or an entire datacenter) goes down, you can use multiple machines to give you redundancy.
    - When one fails, another one can take over.
  - Latency:
    - Each user around the world can be served from a data center that is geo-graphically close to them.
      - That avoids the users having to wait for network packets to travel halfway around the world.

## Scaling to Higher Load
### Vertical scaling(Scaling up)
- Shared-*memory* architecture: Many CPUs, many RAM chips, and many disks can be joined together under one operating system.
  - Pros
     - Fast Interconnect: with the memory or disk as a single machine
     - No shutting down: Hot-swappable components: you can replace disks, memory modules, and even CPUs without shutting down the machines
  - Cons:
     - High cost
     - geographical limit
     - Bottlenecks: a machine twice the size cannot necessarily handle twice the load
- Shared-*disk* architecture: With fast network, Several machines with independent CPUs and RAM, but stores data on an array of disks that is shared between the machines
  - Good for some data warehousing workloads
  - But, Contention and the overhead of locking limit the scalability

### Horizontal scaling(Scaling out)
- Shared-Nothing Architectures
- Each machine or virtual machine running the database software(called a *Node*).
- Each node uses its CPUs, RAM, and disks *independently*.
- Any coordination between nodes is done at the software level, using a conventional network.
- Pros
  - No special hardware is required by a shared-nothing system
    - So, use whatever machines have the best price/performance ratio
  - Distribute data across multiple geographic regions
    - (1) to reduce latency and
    - (2) for user to survive the loss of an entire data center.
  - With cloud deployments of virtual machines, even for small companies, a multi-region distributed architecture is now feasible.
    - You don’t need to be operating at Google scale: 
  - As an application developer, plz be aware of the *constraints* and *trade-offs* that occur in such a distributed system across multiple nodes.
  - Very powerful
- Cons
  - Additional complexity for applications
  - In some cases, a simple single-threaded program can perform significantly better than a clustered 100 CPU cores.

### Replication Versus Partitioning (for Scaling)
- Two common ways data across multiple nodes:
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
The difficult trade-offs of distributed systems in [Chapter 7. Transactions].
 - When things go wrong, what you(developer) can do
The fundamental limitations of distributed systems in [Chapters 8. The Trouble with Distributed Systems]/ [9. Consistency and Consensus]
```

# CHAPTER 5 Replication
- Replication: keeping a copy of the same data on multiple machines and connecting them via a network.
- Reasons why replicate data?:
  - To reduce latency geographically by keeping data close to users
  - To increase availability by getting systems working continuosly even if there is a failure
  - To increase read throughput by scaling out the number of machines for read queries
- Let's assume that your dataset is so small that each machine can hold a copy of the entire dataset.
- Three popular algorithms:
  - Single-leader
  - Multi-leader
  - Leaderless replication
    - We will discuss them for replicating changes between nodes: 
    - Almost all distributed databases use one of these three approaches.


## Leaders and Followers
-  a replica: a copy of the database stored by a node.
-  Master-slave replication
  - a.k.a leader-based replication, active/passive replica 
  - The most common solution

![](image/fg5-1.jpg "")

- (1) Leader - One of the replicas(a.k.a master or primary)
- (2) Followers - other replicas (a.k.a read replicas, slaves, secondaries, or hot standbys)
  - When clients want to write to the database,
    - clients send the requests to the leader, which first writes the new data to its local storage.
  - Whenever the leader writes new data to its local storage,
    - it also sends the data change to all of its followers as part of a replication log or change stream. 
  - Each follower takes the log from the leader and updates its local copy of the database accordingly, by applying all writes *in the same order* as the leader did.
  - When a client wants to read from the database, it can query either the leader or any of the followers.
- Rules
  - Writes are only accepted on the leader
  - Read-only is on the followers from the client’s point of view
- Usage of leader-based replication
  - RDBS with this mode
    - PostgreSQL/ MySQL/ Oracle Data Guard, and SQL Server’s AlwaysOn Availability Groups
  - non-RDBS with this mode:
    - MongoDB/ RethinkDB/ Espresso
  - Other usage: distributed message brokers(Kafka and RabbitMQ)

 ### Synchronous vs Asynchronous Replication
 ![](image/fg5-2.jpg "")
 
 - Sync: For the follower 1, the leader *waits* the follower's reply to report success to the user
 - Async: For the follower 2, the leader *don't wait*
  - But, there is a *substantial* delay before the follower 2 processes the message.
- Normally, Applying changes to followers is fast (in less than a second)
  - But, No guarantee; might way fall behind  (by several minutes or more)
    - ex) the follower is recovering from a failure,
    - if the system is operating near maximum capacity,
    - or if there are network problems between the nodes.
- Advantage of synchronous replication
  - guaranteed that the follower has a up-to-date copy of the data.
    - If the leader suddenly fails, the data is still available on the follower. 
- Disadvantage of synchronous replication
  - if no respond (because it has crashed, or there is a network fault, or for any other reason), the write cannot be processed.
  - The leader must block all writes and wait until the synchronous replica is back on.
- Semi-synchronous configuraton
  - Impractical for all followers to be synchronous;(any one node outage would cause the whole system to grind to a halt)
  - Instead, Semi-synchronous: one sync follower & all other async followers.    
    - If the synchronous follower becomes unavailable or slow, one of the asynchronous followers is made synchronous.
    - This guarantees that you have an up-to-date copy of the data on at least two nodes: the leader and one synchronous follower.
- Asynchronous replication
  - Advantage
    - the leader can continue processing writes, even if all of its followers have fallen behind.
  - Disadvantage
    - if the leader fails and is not recoverable, any writes are lost without replications to followers.
  - Weakening durability may sound like a bad trade-off, but asynchronous replication is nevertheless widely used,
    - especially if there are many followers
    - or if they are geographically distributed.

> Research on Replication
- A serious problem for asynchronously replicated systems: if the leader fails, it loses data
- Replication methods with performance and availability.
  - For example, Chain replication at Microsoft Azure Storage - a variant of synchronous replication

### Setting Up New Followers
- How do you ensure that the *New follower* has an accurate copy of the leader’s data?
- Simply copying data files from one node to another? Not sufficient - due to the Time difference (The data is always in flux).
- Locking the database? Not high availability
- then how? Setting up a follower: No downtime
  - 1. Take a consistent *SNAPSHOT* of the leader’s database at some point in time — if possible, without taking a lock on the entire database.
    -  Most databases have this feature, as it is also required for backups.
    -  In some cases, Third-party tools are needed, such as innobackupex for MySQL.
  - 2. Copy the snapshot to the new follower node.
  - 3. The follower connects to the leader and requests all the data changes that have happened since the snapshot was taken.
       This requires that the snapshot is associated with an exact position in the leader’s replication log. That position has various names:
       -  for example, PostgreSQL calls it the log sequence number, and MySQL calls it the binlog coordinates.
  - 4. When the follower has processed the backlog of data changes since the snapshot, we say it has caught up.
  - Finally, it can now continue to process data changes from the leader as they happen.
    
### Handling Node Outages
> Our goal is
##### - to keep the system as a whole running despite individual node failures, and
##### - to keep the impact of a node outage as small as possible

#### (1) Follower failure: Catch-up recovery
- On its local disk, each follower keeps a log of the data changes it has received from the leader.
- The follower can recover quite easily from its log

#### (2) Leader failure: Failover 
- Failover (Switching to alternative systems)
  - A follower becomes the new leader
  - Clients need to be reconfigured to send the 'Write's to the new leader
  - Followers need to start consuming data changes from the new leader
 
- Automatic failover process
  - 1. Determining that the leader has failed. (by crashes, power outages, network issues, and more).
    - Timeout: Nodes frequently bounce messages back and forth between each other ==> No respond for some period of time ==> it is assumed to be dead.
  - 2. Choosing a new leader.
    - Election process
      - chosen by a majority of the remaining replicas), or
      - chosen by a previously elected controller node.
    - The best candidate: the most up-to-date data changes
    - In Chapter 9 - Agreement on a new leader
  - 3. Reconfiguring to use the new leader (in Chapter 6)
    - Clients now need to send their write requests to the new leader 
    - The system needs to ensure that the old leader becomes a follower and recognizes the new leader.

##### Impact by a failure
  - In teh asynchronous replication, the new leader may *NOT* have received all the writes from the old leader before it failed.
    - The new leader may have received conflicting writes in the meantime (if the former leader rejoins the cluster after a new leader has been chosen)
    - The most common solution: Discarding writes  ==> No durability.
  
  - Discarding writes is dangerous
    - especially if other storage systems(Redis) outside of the database(MySQL) need to be coordinated with the database contents.
    - For example, at GitHub
      - an out-of-date MySQL follower was promoted to leader.
      - The database used autoincrementing primary keys for new rows
      - because of the out-of-date data, it reused some primary keys ==> inconsistency between MySQL and Redis
  - Another fault scenario - '*Split Brain*' (Chapter 8)
    - Two nodes both believe that they are the leader 
    - Dangerous: if both leaders accept writes, No resolustion for conflicts - Data is likely to be lost or corrupted.
      - As a safety catch, some systems have a mechanism to shut down one node if two leaders are detected.
      - However, if this mechanism is not carefully designed, you can end up with both nodes being shut down.
  - Right timeout before declareing dead?
    - if the timeout is too short, there could be unnecessary failovers.
      - For example, a temporary load spike could cause a node’s response time to increase above the timeout,
      - or a network glitch could cause delayed packets.
    - High load or network problems: declareing failover ==>  make it worse

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
- Obvious conflict:
  - (Figure 5-7) Two writes concurrently modified the same field in the same record.
- Little doubt conflict.
  - Other kinds of conflict can be more subtle to detect.
  - For example, Meeting room booking system
    - it tracks which room is booked by which group of people at which time.
    - Each room must be only booked by one group of people at any one time. (i.e., there must not be any overlapping bookings for the same room).
    - But, A conflict may arise if two different bookings are created for the same room at the same time.
    - Even if the application checks availability before allowing a user to make a booking, there can be a conflict if the two bookings are made on two different leaders.
  - We will see some more examples of conflicts in Chapter 7, and Chapter 12 with scalable approaches for detecting and resolving conflicts in a replicated system.
  
### Multi-Leader Replication Topologies
- A replication topology:  the communication paths along which writes are propagated from one node to another.
- Two leaders(Figure 5-7): Leader 1 must send all of its writes to leader 2, and vice versa.
- More than two leaders(Figure 5-8), various different topologies are possible. 

![](image/fg5-8.jpg "")

- Circular and star topologies:
  - Circular topology(Figure 5-8[a]: More restricted topologies. Default supports in MySQL. Each node receives writes from one node and forwards those writes to one other node.
  - Star topology(Figure 5-8[b]: Another popular topology. One designated root node forwards writes to all of the other nodes which can be generalized to a tree.
    - A write may need to pass through several nodes before it reaches all replicas. Therefore, nodes need to forward data changes they receive from other nodes.
    - To prevent infinite replication loops, each node is given a unique identifier, and in the replication log, each write is tagged with the identifiers of all the nodes it has passed through [43]. 
    - but, a problem: if just one node fails, it can interrupt the flow of replication messages between other nodes ==> No communication until the node is fixed mostly manually. 
- All-to-All(Figure 5-8[c]):
  - The most general topology. Every leader sends its writes to every other leader.
    - The fault tolerance of a more densely connected topology (such as all-to-all) is better because it allows messages to travel along different paths, avoiding a single point of failure.
    - But, issues: In particular, some network links may be faster than others (e.g., due to network congestion) ==> some replication messages may “overtake” others, as illustrated in Figure 5-9.

![](image/fg5-9.jpg "")

```
(Figure 5-9)
  - Client A inserts a row into a table on leader 1,
  - Client B updates the row on leader 3.
  - 🥵 Leader 2 may first receive the update but no data in the database
```
- Resolution?
  - Timestamp?: not sufficient, because clocks cannot be trusted in sync(Chapter 8) 
  - Version vectors: OK (see “Detecting Concurrent Writes” on page 184)
- Poor Conflict detection techniques
  - (Write)PostgreSQL BDR: no causal ordering of writes
  - (Write)Tungsten Replicator-MySQL: no conflict detection.
  - So, for the multi-leader replication, plz be aware of these issues(read documentation/ test database)

## Leaderless Replication
- By far, Replication approaches: Single-leader / multi-leader replication => client sends a Write request to one node (the leader), and the database system takes care of copying that Write to the other replicas.
- A leader determines the order of the 'Write' processes/ Followers apply the leader’s writes in the same order.

> Different approaches - Leaderless replication models

- allowing any replica to directly accept writes from clients
  - Some of the earliest replicated data systems were leaderless [1, 44],
  - but the idea was mostly forgotten during the era of dominance of relational databases.
  - It once again became a fashionable architecture for databases after Amazon used it for its in-house Dynamo system [37].
- Amazon's Dynamo
- Dynamo-style: Riak, Cassandra, and Voldemort - open source datastores(leaderless replication models inspired by Dynamo)
- Two different leaderless implementations
  - (1) Client === writes ==> several replicas
  - (2) Coordinator node === writes ==> several replicas 
    - The coordinator does not enforce a particular ordering of writes.
- This difference in design has profound consequences for the way the database is used as we shall see.

### Writing to the Database When a Node Is Down
- Imagine you have a database with three replicas and one of the replicas is currently unavailable—perhaps it is being rebooted to install a system update.
- In a leader-based configuration,
  - need to perform a failover (see “*Handling Node Outages*” on page 156).
- In a leaderless configuration, on the other hand,
  - No failover exists
    - Example(Figure 5-10), The client (user 1234) sends the write to all three replicas in parallel. The two available replicas accept the write, but the unavailable replica misses it.
    - Let’s say that it’s sufficient for two out of three replicas to acknowledge the 'write':
    - After user 1234 has received two ok responses, we consider the write to be successful. The client simply ignores the fact that one of the replicas missed the write.
    - The unavailable node comes back online, and clients start reading from it. (Any writes that happened while the node was down are missing from that node.)
    - Thus, if you read from that node, you may get stale (outdated) values as responses.
    - To solve that problem, a client sends read requests to *several nodes* in parallel.
    - The client may get different responses from different nodes; i.e., the up-to-date value from one node and a stale value from another.
    - *Version numbers* are used to determine which value is newer (see “Detecting Concurrent Writes” on page 184).

![](image/fg5-10.jpg "")

#### Read repair and anti-entropy
- How does it catch up on the writes that it missed? (After an unavailable node comes back online). 
- The replication scheme should ensure that eventually all the data is copied to every replica. 
- Two mechanisms are often used in Dynamo-style datastores:
  - Read repair:
    - When a client makes a read from several nodes in parallel, it can detect any stale responses.
    - For example(Figure 5-10), user 2345 gets a version 6 value from replica 3 and a version 7 value from replicas 1 and 2.
    - The client writes the newer value back to that replica 3. This approach works well for values that are frequently read.
  - Anti-entropy process:
    - In addition, some datastores have a *background process* that *constantly looks for* differences in the data between replicas and copies any missing data from one replica to another.
    - Unlike the replication log in leader-based replication, this anti-entropy process does not copy writes in any particular order, and there may be a significant delay before data is copied.
- Not all systems implement both of these;
  - For example, Voldemort currently does not have an anti-entropy process.
  - Note that without an anti-entropy process, values that are rarely read may be missing from some replicas and thus have reduced durability, because read repair is only performed when a value is read by the application.

#### Quorums for reading and writing
- we considered the write(Figure 5-10) to be successful even though it was only processed on *two* out of three replicas.
  - What if *only one* out of three replicas accepted the write? How far can we push this?
  - If we know at least two out of three replicas succeed, that means at most one replica can be stale.
  - Thus, if we read from at least two replicas, we can be sure that at least one of the two is up to date.
  - If the third replica is down or slow to respond, reads can nevertheless continue returning an up-to-date value.
- **More generally, if there are n replicas, every write must be confirmed by w nodes to be considered successful, and we must query at least r nodes for each read.***
- (In our example, n = 3, w = 2, r = 2.) As long as w + r > n, we expect to get an up-to-date value when reading, because at least one of the r nodes we’re reading from must be up to date. Reads and writes that obey these r and w values are called quorum reads and writes [44]).
- You can think of r and w as the minimum number of votes required for the read or write to be valid.
- In Dynamo-style databases, the parameters n, w, and r are typically configurable. A common choice is to make n an odd number (typically 3 or 5) and to set w = r = (n + 1) / 2 (rounded up).
- However, you can vary the numbers as you see fit. For example, a workload with few writes and many reads may benefit from setting w = n and r = 1. This makes reads faster,
- but has the disadvantage that just one failed node causes all database writes to fail.

```
  There may be more than n nodes in the cluster, but any given value is stored only on n nodes.
  This allows the dataset to be partitioned, supporting datasets that are larger than you can fit on one node. 
  We will return to partitioning in Chapter 6.
```
- The quorum condition, w + r > n, allows the system to tolerate unavailable nodes as follows:
  -  If w < n, we can still process writes if a node is unavailable.
  -  If r < n, we can still process reads if a node is unavailable.
  -  With n = 3, w = 2, r = 2 we can tolerate one unavailable node.
  -  With n = 5, w = 3, r = 3 we can tolerate two unavailable nodes. This case is illustrated in Figure 5-11.
  - Normally, reads and writes are always sent to all n replicas in parallel. The parameters w and r determine how many nodes we wait for—i.e., how many of the n nodes need to report success before we consider the read or write to be successful.

![](image/fg5-11.jpg "")

- If fewer than the required w or r nodes are available, writes or reads return an error.
- A node could be unavailable for many reasons:
  - because the node is down (crashed, powered down),
  - due to an error executing the operation (can’t write because the disk is full),
  - due to a network interruption between the client and the node, or for any number of other reasons.
- We only care whether the node returned a successful response and don’t need to distinguish between different kinds of fault.

### Limitations of Quorum Consistency
- If you have n replicas, and you choose w and r such that w + r > n, you can generally expect every read to return the most recent value written for a key.
  - This is the case because the set of nodes to which you’ve written and the set of nodes from which you’ve read must overlap.
  - That is, among the nodes you read there must be at least one node with the latest value (illustrated in Figure 5-11).
  - Often, r and w are chosen to be a majority (more than n/2) of nodes, because that ensures w + r > n while still tolerating up to n/2 node failures.
  - But quorums are not necessarily majorities—it only matters that the sets of nodes used by the read and write operations overlap in at least one node.
- Other quorum assignments are possible, which allows some flexibility in the design of distributed algorithms [45].
  - You may also set w and r to smaller numbers, so that w + r ≤ n (i.e., the quorum condition is not satisfied).
  - In this case, reads and writes will still be sent to n nodes, but a smaller number of successful responses is required for the operation to succeed.
  - With a smaller w and r you are more likely to read stale values, because it’s more likely that your read didn’t include the node with the latest value.
- On the upside, this configuration allows lower latency and higher availability:
  - if there is a network interruption and many replicas become unreachable, there’s a higher chance that you can continue processing reads and writes.
  - Only after the number of reachable replicas falls below w or r does the database become unavailable for writing or reading, respectively.
- However, even with w + r > n, there are likely to be edge cases where stale values are returned.
- These depend on the implementation, but possible scenarios include:
  -  If a sloppy quorum is used (see *“Sloppy Quorums and Hinted Handoff”* on page 183), the w writes may end up on different nodes than the r reads, so there is no longer a guaranteed overlap between the r nodes and the w nodes [46].
  - If two writes occur concurrently, it is not clear which one happened first. In this case, the only safe solution is to merge the concurrent writes (see *“Handling Write Conflicts”* on page 171). If a winner is picked based on a timestamp (last write wins), writes can be lost due to clock skew [35]. We will return to this topic in “Detecting Concurrent Writes” on page 184.
  - If a write happens concurrently with a read, the write may be reflected on only some of the replicas. In this case, it’s undetermined whether the read returns the old or the new value.
  - If a write succeeded on some replicas but failed on others (for example because the disks on some nodes are full), and overall succeeded on fewer than w replicas, it is not rolled back on the replicas where it succeeded. This means that if a write was reported as failed, subsequent reads may or may not return the value from that write [47].
  - If a node carrying a new value fails, and its data is restored from a replica carrying an old value, the number of replicas storing the new value may fall below w, breaking the quorum condition.
  - Even if everything is working correctly, there are edge cases in which you can get unlucky with the timing, as we shall see in “Linearizability and quorums” on page 334.

- Thus, although quorums appear to guarantee that a read returns the latest written value, in practice it is not so simple.

- Dynamo-style databases are generally optimized for use cases that can tolerate eventual consistency.
- The parameters w and r allow you to adjust the probability of stale values being read, but it’s wise to not take them as absolute guarantees.
- In particular, you usually do not get the guarantees discussed in “Problems with Replication Lag” on page 161 (reading your writes, monotonic reads, or consistent prefix reads), so the previously mentioned anomalies can occur in applications.
- Stronger guarantees generally require transactions or consensus. We will return to these topics in Chapter 7 and Chapter 9.

#### Monitoring staleness
- From an operational perspective, it’s important to monitor whether your databases are returning up-to-date results.
- Even if your application can tolerate stale reads, you need to be aware of the health of your replication. If it falls behind significantly, it should alert you so that you can investigate the cause (for example, a problem in the network or an overloaded node).
- For leader-based replication, the database typically exposes metrics for the replication lag, which you can feed into a monitoring system. This is possible because writes are applied to the leader and to followers in the same order, and each node has a position in the replication log (the number of writes it has applied locally). By subtracting a follower’s current position from the leader’s current position, you can measure the 
amount of replication lag.
- However, in systems with leaderless replication, there is no fixed order in which writes are applied, which makes monitoring more difficult. Moreover, if the database only uses read repair (no anti-entropy), there is no limit to how old a value might be —if a value is only infrequently read, the value returned by a stale replica may be ancient.
- There has been some research on measuring replica staleness in databases with leaderless replication and predicting the expected percentage of stale reads depending on the parameters n, w, and r.
- This is unfortunately not yet common practice, but it would be good to include staleness measurements in the standard set of metrics for databases.
- Eventual consistency is a deliberately vague guarantee, but for operability it’s important to be able to quantify “eventual.”

### Sloppy Quorums and Hinted Handoff
- Databases with appropriately configured quorums can tolerate the failure of individual nodes without the need for failover. They can also tolerate individual nodes going slow, because requests don’t have to wait for all n nodes to respond—they can return when w or r nodes have responded. These characteristics make databases with leaderless replication appealing for use cases that require high availability and low latency, and that can tolerate occasional stale reads.  
- However, quorums (as described so far) are not as fault-tolerant as they could be. A network interruption can easily cut off a client from a large number of database nodes. Although those nodes are alive, and other clients may be able to connect to them, to a client that is cut off from the database nodes, they might as well be dead. In this situation, it’s likely that fewer than w or r reachable nodes remain, so the client 
can no longer reach a quorum.
- In a large cluster (with significantly more than n nodes) it’s likely that the client can connect to some database nodes during the network interruption, just not to the nodes that it needs to assemble a quorum for a particular value. In that case, database designers face a trade-off:
  - Is it better to return errors to all requests for which we cannot reach a quorum of w or r nodes?
  - Or should we accept writes anyway, and write them to some nodes that are reachable but aren’t among the n nodes on which the value usually lives?
- The latter is known as a sloppy quorum [37]: writes and reads still require w and r successful responses, but those may include nodes that are not among the designated n “home” nodes for a value. By analogy, if you lock yourself out of your house, you may knock on the neighbor’s door and ask whether you may stay on their couch temporarily.
- Once the network interruption is fixed, any writes that one node temporarily accepted on behalf of another node are sent to the appropriate “home” nodes. This is called hinted handoff. (Once you find the keys to your house again, your neighbor politely asks you to get off their couch and go home.)
- Sloppy quorums are particularly useful for increasing write availability: as long as any w nodes are available, the database can accept writes. However, this means that even when w + r > n, you cannot be sure to read the latest value for a key, because the latest value may have been temporarily written to some nodes outside of n [47].
- Thus, a sloppy quorum actually isn’t a quorum at all in the traditional sense. It’s only an assurance of durability, namely that the data is stored on w nodes somewhere. There is no guarantee that a read of r nodes will see it until the hinted handoff has completed.
- Sloppy quorums are optional in all common Dynamo implementations. In Riak they are enabled by default, and in Cassandra and Voldemort they are disabled by default [46, 49, 50].

#### Multi-datacenter operation
- We previously discussed cross-datacenter replication as a use case for multi-leader replication (see “Multi-Leader Replication” on page 168). Leaderless replication is also suitable for multi-datacenter operation, since it is designed to tolerate conflicting concurrent writes, network interruptions, and latency spikes.
- Cassandra and Voldemort implement their multi-datacenter support within the normal leaderless model: the number of replicas n includes nodes in all datacenters, and in the configuration you can specify how many of the n replicas you want to have in each datacenter. Each write from a client is sent to all replicas, regardless of datacenter, but the client usually only waits for acknowledgment from a quorum of nodes within its local datacenter so that it is unaffected by delays and interruptions on the cross-datacenter link. The higher-latency writes to other datacenters are often configured to happen asynchronously, although there is some flexibility in the configuration [50, 51].
- Riak keeps all communication between clients and database nodes local to one datacenter, so n describes the number of replicas within one datacenter. Cross-datacenter replication between database clusters happens asynchronously in the background, in a style that is similar to multi-leader replication [52].

### Detecting Concurrent Writes
- Dynamo-style databases allow several clients to concurrently write to the same key, which means that conflicts will occur even if strict quorums are used. The situation is similar to multi-leader replication (see “Handling Write Conflicts” on page 171), although in Dynamo-style databases conflicts can also arise during read repair or hinted handoff.
- The problem is that events may arrive in a different order at different nodes, due to variable network delays and partial failures. For example, Figure 5-12 shows two clients, A and B, simultaneously writing to a key X in a three-node datastore:
  - Node 1 receives the write from A, but never receives the write from B due to a transient outage.
  - Node 2 first receives the write from A, then the write from B.
  - Node 3 first receives the write from B, then the write from A.

![](image/fg5-12.jpg "")

- If each node simply overwrote the value for a key whenever it received a write request from a client, the nodes would become permanently inconsistent, as shown by the final get request in Figure 5-12: node 2 thinks that the final value of X is B, whereas the other nodes think that the value is A.
- In order to become eventually consistent, the replicas should converge toward the same value. How do they do that? One might hope that replicated databases would handle this automatically, but unfortunately most implementations are quite poor: if you want to avoid losing data, you—the application developer—need to know a lot about the internals of your database’s conflict handling.
- We briefly touched on some techniques for conflict resolution in “Handling Write Conflicts” on page 171. Before we wrap up this chapter, let’s explore the issue in a bit more detail.

#### Last write wins (discarding concurrent writes)
- One approach for achieving eventual convergence is to declare that each replica need only store the most “recent” value and allow “older” values to be overwritten and discarded. Then, as long as we have some way of unambiguously determining which write is more “recent,” and every write is eventually copied to every replica, the replicas will eventually converge to the same value.
- As indicated by the quotes around “recent,” this idea is actually quite misleading. In the example of Figure 5-12, neither client knew about the other one when it sent its write requests to the database nodes, so it’s not clear which one happened first. In fact, it doesn’t really make sense to say that either happened “first”: we say the writes are concurrent, so their order is undefined.
- Even though the writes don’t have a natural ordering, we can force an arbitrary order on them. For example, we can attach a timestamp to each write, pick the biggest timestamp as the most “recent,” and discard any writes with an earlier timestamp. This conflict resolution algorithm, called last write wins (LWW), is the only supported conflict resolution method in Cassandra [53], and an optional feature in Riak [35].
- LWW achieves the goal of eventual convergence, but at the cost of durability: if there are several concurrent writes to the same key, even if they were all reported as successful to the client (because they were written to w replicas), only one of the writes will survive and the others will be silently discarded. Moreover, LWW may even drop writes that are not concurrent, as we shall discuss in “Timestamps for ordering 
events” on page 291.
- There are some situations, such as caching, in which lost writes are perhaps acceptable. If losing data is not acceptable, LWW is a poor choice for conflict resolution.
- The only safe way of using a database with LWW is to ensure that a key is only written once and thereafter treated as immutable, thus avoiding any concurrent updates to the same key. For example, a recommended way of using Cassandra is to use a UUID as the key, thus giving each write operation a unique key [53].

#### The “happens-before” relationship and concurrency
- How do we decide whether two operations are concurrent or not? To develop an intuition, let’s look at some examples:
  - In Figure 5-9, the two writes are not concurrent: A’s insert happens before B’s increment, because the value incremented by B is the value inserted by A. In other words, B’s operation builds upon A’s operation, so B’s operation must have happened later. We also say that B is causally dependent on A.
  - On the other hand, the two writes in Figure 5-12 are concurrent: when each client starts the operation, it does not know that another client is also performing an operation on the same key. Thus, there is no causal dependency between the operations.
- An operation A happens before another operation B if B knows about A, or depends on A, or builds upon A in some way. Whether one operation happens before another operation is the key to defining what concurrency means. In fact, we can simply say that two operations are concurrent if neither happens before the other (i.e., neither knows about the other) [54].
- Thus, whenever you have two operations A and B, there are three possibilities: either A happened before B, or B happened before A, or A and B are concurrent. What we need is an algorithm to tell us whether two operations are concurrent or not. If one operation happened before another, the later operation should overwrite the earlier operation, but if the operations are concurrent, we have a conflict that needs to be resolved.

> Concurrency, Time, and Relativity
```
It may seem that two operations should be called concurrent if they occur “at the same time”—but in fact, it is not important whether they literally overlap in time. 
Because of problems with clocks in distributed systems, it is actually quite difficult to tell whether two things happened at exactly the same time—an issue we will discuss in more detail in Chapter 8.

For defining concurrency, exact time doesn’t matter: we simply call two operations concurrent if they are both unaware of each other, regardless of the physical time at which they occurred. People sometimes make a connection between this principle and the special theory of relativity in physics [54], which introduced the idea that information cannot travel faster than the speed of light.
Consequently, two events that occur some distance apart cannot possibly affect each other if the time between the events is shorter than the time it takes light to travel the distance between them.

In computer systems, two operations might be concurrent even though the speed of light would in principle have allowed one operation to affect the other. For example, if the network was slow or interrupted at the time, two operations can occur some time apart and still be concurrent, because the network problems prevented one operation from being able to know about the other.
```
#### Capturing the happens-before relationship
- Let’s look at an algorithm that determines whether two operations are concurrent, or whether one happened before another. To keep things simple, let’s start with a data‐base that has only one replica. Once we have worked out how to do this on a single replica, we can generalize the approach to a leaderless database with multiple replicas. 
- Figure 5-13 shows two clients concurrently adding items to the same shopping cart. (If that example strikes you as too inane, imagine instead two air traffic controllers concurrently adding aircraft to the sector they are tracking.)
- Initially, the cart isempty. Between them, the clients make five writes to the database:
  - 1. Client 1 adds milk to the cart. This is the first write to that key, so the server successfully stores it and assigns it version 1. The server also echoes the value back to the client, along with the version number.
  - 2. Client 2 adds eggs to the cart, not knowing that client 1 concurrently added milk (client 2 thought that its eggs were the only item in the cart). The server assigns version 2 to this write, and stores eggs and milk as two separate values. It then returns both values to the client, along with the version number of 2.
  - 3. Client 1, oblivious to client 2’s write, wants to add flour to the cart, so it thinks the current cart contents should be [milk, flour]. It sends this value to the server, along with the version number 1 that the server gave client 1 previously. The server can tell from the version number that the write of [milk, flour] supersedes the prior value of [milk] but that it is concurrent with [eggs]. Thus, the server assigns version 3 to [milk, flour], overwrites the version 1 value [milk], but keeps the version 2 value [eggs] and returns both remaining values to the client.
  - 4.  Meanwhile, client 2 wants to add ham to the cart, unaware that client 1 just added flour. Client 2 received the two values [milk] and [eggs] from the server in the last response, so the client now merges those values and adds ham to form a new value, [eggs, milk, ham]. It sends that value to the server, along with the previous version number 2. The server detects that version 2 overwrites [eggs] but is concurrent with [milk, flour], so the two remaining values are [milk, flour] with version 3, and [eggs, milk, ham] with version 4.
  - 5. Finally, client 1 wants to add bacon. It previously received [milk, flour] and [eggs] from the server at version 3, so it merges those, adds bacon, and sends the final value [milk, flour, eggs, bacon] to the server, along with the version number 3. This overwrites [milk, flour] (note that [eggs] was already overwritten in the last step) but is concurrent with [eggs, milk, ham], so the server keeps those two concurrent values.

![](image/fg5-13.jpg "")

- The dataflow between the operations in Figure 5-13 is illustrated graphically in Figure 5-14. The arrows indicate which operation happened before which other operation, in the sense that the later operation knew about or depended on the earlier one. In this example, the clients are never fully up to date with the data on the server, since there is always another operation going on concurrently. But old versions of the value 
do get overwritten eventually, and no writes are lost.

![](image/fg5-14.jpg "")

- Note that the server can determine whether two operations are concurrent by looking at the version numbers—it does not need to interpret the value itself (so the value could be any data structure). The algorithm works as follows:

  - • The server maintains a version number for every key, increments the version number every time that key is written, and stores the new version number along with the value written.
  - • When a client reads a key, the server returns all values that have not been over‐written, as well as the latest version number. A client must read a key before writing.
  - • When a client writes a key, it must include the version number from the prior read, and it must merge together all values that it received in the prior read. (The response from a write request can be like a read, returning all current values, which allows us to chain several writes like in the shopping cart example.)
  - • When the server receives a write with a particular version number, it can over‐write all values with that version number or below (since it knows that they have been merged into the new value), but it must keep all values with a higher version number (because those values are concurrent with the incoming write).
 
- When a write includes the version number from a prior read, that tells us which previous state the write is based on. If you make a write without including a version number, it is concurrent with all other writes, so it will not overwrite anything—it will just be returned as one of the values on subsequent reads.

#### Merging concurrently written values
- This algorithm ensures that no data is silently dropped, but it unfortunately requires that the clients do some extra work: if several operations happen concurrently, clients have to clean up afterward by merging the concurrently written values. Riak calls these concurrent values siblings.
- Merging sibling values is essentially the same problem as conflict resolution in multi-leader replication, which we discussed previously (see “Handling Write Conflicts” on page 171). A simple approach is to just pick one of the values based on a version number or timestamp (last write wins), but that implies losing data. So, you may need to do something more intelligent in application code.
- With the example of a shopping cart, a reasonable approach to merging siblings is to just take the union. In Figure 5-14, the two final siblings are [milk, flour, eggs, bacon] and [eggs, milk, ham]; note that milk and eggs appear in both, even though they were each only written once. The merged value might be something like [milk, flour, eggs, bacon, ham], without duplicates.
- However, if you want to allow people to also remove things from their carts, and not just add things, then taking the union of siblings may not yield the right result: if you merge two sibling carts and an item has been removed in only one of them, then the removed item will reappear in the union of the siblings [37]. To prevent this problem, an item cannot simply be deleted from the database when it is removed; instead, the system must leave a marker with an appropriate version number to indicate that the item has been removed when merging siblings. Such a deletion marker is known as a tombstone. (We previously saw tombstones in the context of log compaction in “Hash Indexes” on page 72.)
- As merging siblings in application code is complex and error-prone, there are some efforts to design data structures that can perform this merging automatically, as discussed in “Automatic Conflict Resolution” on page 174. For example, Riak’s datatype support uses a family of data structures called CRDTs [38, 39, 55] that can automatically merge siblings in sensible ways, including preserving deletions.

#### Version vectors
- The example in Figure 5-13 used only a single replica. How does the algorithm change when there are multiple replicas, but no leader?
- Figure 5-13 uses a single version number to capture dependencies between operations, but that is not sufficient when there are multiple replicas accepting writes concurrently. Instead, we need to use a version number per replica as well as per key. Each replica increments its own version number when processing a write, and also keeps track of the version numbers it has seen from each of the other replicas. This information indicates which values to overwrite and which values to keep as siblings. 
- The collection of version numbers from all the replicas is called a version vector [56]. A few variants of this idea are in use, but the most interesting is probably the dotted version vector [57], which is used in Riak 2.0 [58, 59]. We won’t go into the details, but the way it works is quite similar to what we saw in our cart example.
- Like the version numbers in Figure 5-13, version vectors are sent from the database replicas to clients when values are read, and need to be sent back to the database when a value is subsequently written. (Riak encodes the version vector as a string that it calls causal context.) The version vector allows the database to distinguish between overwrites and concurrent writes.
- Also, like in the single-replica example, the application may need to merge siblings. The version vector structure ensures that it is safe to read from one replica and subsequently write back to another replica. Doing so may result in siblings being created, but no data is lost as long as siblings are merged correctly.

```
[Version vectors and vector clocks]
A version vector is sometimes also called a vector clock, even though they are not quite the same. The difference is subtle—please see the 
references for details [57, 60, 61]. In brief, when comparing the state of replicas, version vectors are the right data structure to use.
```

### Summary
- In this chapter we looked at the issue of replication. Replication can serve several purposes:
  - High availability
    - Keeping the system running, even when one machine (or several machines, or an entire datacenter) goes down
  - Disconnected operation
    - Allowing an application to continue working when there is a network interruption
  - Latency
    - Placing data geographically close to users, so that users can interact with it faster
  - Scalability
    - Being able to handle a higher volume of reads than a single machine could handle, by performing reads on replicas
- Despite being a simple goal—keeping a copy of the same data on several machines—replication turns out to be a remarkably tricky problem.
- It requires carefully thinking about concurrency and about all the things that can go wrong, and dealing with the consequences of those faults.
- At a minimum, we need to deal with unavailable nodes and network interruptions (and that’s not even considering the more insidious kinds of fault, such as silent data corruption due to software bugs).
- We discussed three main approaches to replication: 
  - Single-leader replication
    - Clients send all writes to a single node (the leader), which sends a stream of data change events to the other replicas (followers). Reads can be performed on any replica, but reads from followers might be stale.
  - Multi-leader replication
    - Clients send each write to one of several leader nodes, any of which can accept writes. The leaders send streams of data change events to each other and to any follower nodes.
  - Leaderless replication
    - Clients send each write to several nodes, and read from several nodes in parallel in order to detect and correct nodes with stale data.
- Each approach has advantages and disadvantages. Single-leader replication is popular because it is fairly easy to understand and there is no conflict resolution to worry about. Multi-leader and leaderless replication can be more robust in the presence of
