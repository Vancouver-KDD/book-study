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
 
