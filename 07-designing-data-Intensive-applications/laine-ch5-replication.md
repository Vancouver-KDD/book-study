**Why replicate data?**
- keep data close to users
- keep working in case of system failure
- scale out the # of machines for read queries

**Why is replication hard?** 
- Data changes over time. Handling changes to replicated data is the hard part
- Will discuss _eventual consistency, read-your-writes, monotonic reads_

# Leaders and Followers
**Replica**
- node storing a copy of the database
- With multiple replicas, how do we ensure all data on all replicas stays the same

**leader-based replication (active/passive or master-slave replication)**
- leader replica: all writes request is sent to the leader
- followers (read replica, slaves, secondaries, hot standbys): read-only. leader sends the data change to all followers, as part of a replication log or change stream
- Client can read from either the leader or a follower
- applicable to not only DB but also distributed message brokers (Kafka, RabbitMQ)

## Synchronous Versus Asynchronous Replication
**Synchronous replication**
- Being asynchronous means no guarantee of how fast a replication is - followers might fall behind the leader due to a failure or network problems
- Pros: followers is guaranteed to have up-to-date copy consistent with the leader
- Cons: if a follower doesn't respond, the write can't be processed. One outage block the whole system
- **_semi-synchronous_**: often synchronous replication means only one follower is synchronous and the others are asynchronous.


**Asynchronous replication**
- Widely used with many followers or geographically distributed, leader-based replication is often completely asynchronous.
  - **Weakened durability**: if the leader fails, any writes are lost.
- Pros: leader can continue processing writes, even if all followers fall behind

## Setting Up New Followers
- To increase the # of replicas or to replicate the failed nodes, ensuring the new follower has an accurate copy of the leader
- Simply copying wouldn't cover constant writes from clients
  - locking the DB is not good for availability

**Steps to set a new follower** without downtime
1. Take a consistent snapshot of the leader at some point
2. Copy the snapshot to the new follower node
3. Catching up - New follower connects to the leader and requests data since the last snapshot

## Handling Node Outages
How to achieve high availability?

**Follower failure: catch-up recovery**
- each follower keeps a log on the local disk, thus recovery is easy from the log

**Leader failure: Failover process**
- One of the followers is promoted to be the new leader and the reconfiguration to send write to the new leader
- Automatic process
   1. Determining the leader has failed: timeout is the most common way, as there's no foolproof way
   2. Choosing a new leader: election process for the most up to date replica
   3. Reconfigurating the system to use the new leader

**Things affecting the failover**
No easy solution, so some prefers manual failover
- In asynchronous replication, the new leader might not have all the writes from the old reader - the lost data is discarded
- split brain: two nodes believe both are the leader, no process to resolve conflict some data is lost
- longer timeout means longer recovery time 

## Implementation of Replication Logs
- Statement-based replication
  - Leader logs every write request (statement) and sends the log to followers
  - Still used, but mostly converted to row based replication
- Write-ahead log (WAL) shipping
  - log is appended to all writes to the database, and the log is used to build a replica on another node (leader sends it to followers)
- Logical (row-based) log replication
  - For relational database, a sequence of records describing writes to database tables at the granularity of a row - insert/delete/update a row
  - decoupled from the storage engine, allowing easily kept backward compatible, and also easier for external applications to parse
- Trigger-based replication
  - Done in the application layer, not in the database system, to replicate a subset of the data only or conflict resolution logic
  - Trigger lets register custom application code with data change
  - greater overheads and bugs

# Problems with Replication Lag
Scalability and latency are also reasons for replication.
**Replication Lag**
- Delay between a write on leader and being reflected on a follower
- Read from asynchronous followers can show outdated info, leading to inconsistency
- Even though the inconsistency is temporary (eventual consistency) but it's vague and could vary from a few seconds to minutes

### Reading Your Own Writes
**read-after-write consistency, also known as read-your-writes consistency is needed**
- Read what the user modified from leader, and from follower if it's not modifed by the user
  - Such as my own profile VS seeing other users profile in Twitter
- 1 min from the last update, read from leader, otherwise from followers
- checking the timestamp of the recent write, and read from followers updated with it

**cross-device read-after-write consistency**
- metadata will be used across the devices
- Route request from all devices to the same datacenter

### Monotonic Reads
- If one user makes several reads in sequence, they won't see older data after reading new data
- each user always read from the same replica, based on the user ID, not randomly

### Consistent Prefix Reads
- if a sequence of writes happens in a certain order, then anyone reading those writes will see them appear in the same order.
- Particular problem with partitioned (sharded) DB
- Ensure writes related to each other are written to the same partition, but not always easy

### Solutions for Replication Lag
Pretending that replication is synchronous when in fact it is asynchronous is a recipe for problems down the line

_**transaction**_
- A way for a database to provide stronger guarantees so that the application can be simple
- In the distributed DB system, transactions are too expensive for performance/availability


# Multi-Leader Replication
- Single leader, every write must go through it.
- multiple leaders process write forward the change to all the other nodes. Each leader is also a follower to the other leaders

## Use Cases for Multi-Leader Replication
### Multi-datacenter operation
Having a leader in each datacenter. 

### Clients with offline operation
Every device has a local DB acting as a leader, and asynchronous replication process will be done once online. 

### Collaborative editing
- Like Google doc
Not exactly DB replication problem, but resembles a lot.
- One user edit is instantly applied to the local replica, and asynchronously replicated to the server
- For no editing conflicts, can obtain a lock
- To avoid lock, replicate with very small units of change (single keystroke)

## Handling Write Conflicts
Wiki page edited by two users simultaneously

#### Synchronous versus asynchronous conflict detection
- Single leader DB, the second writer will wait for the first write completion or abort the second write
- Multi leader, both writes are processed and the conflict is detected asynchronously
  - If conflict detection is synchronous, it loses the advantage of multi leaders

#### Conflict avoidance
Recommended approach
- Each user has the home datacenter (geo proximity), requests are always routed to the same datacenter
- But conflicts can't be avoided if you change the leader of a datacenter due to failure, or user moved to different location

#### Converging toward a consistent state
Multi-leader setup, there's no defined ordering of writes, and DB must resolve the conflict in convergent way
- All replicas must arrive at the same final value

#### Custom conflict resolution logic
Using application code for conflict resolution
- execute at write or read

#### What is a conflict?
Some conflicts are subtle to detect
- ex: Booking room, from two different groups booked at the same time

### Multi-Leader Replication Topologies
_**Replication topology**_: comm path which writes are propagated from one node to another

**all-to-all topology**
- (most general) - every leader sends writes to every other leader
- Better fault tolerance, avoiding a single point of failure
- some network links are faster than others, so some replica may overtake others
  - version vectors could be a solution

**Problem with circular & star**
- star: one designated root node forwards writes to all other nodes
- a write need to pass through several nodes before reaching all replicas
- if one node fails it interupt the flow until fix

# Leaderless Replication
Any replicas accept writes from clients directly or a coordinator node does it on behalf of the client
- Amazon in-house Dynamo system

## Writing to the Database When a Node Is Down
No failover in the leaderless setup
- If some replica is unavailable, it simply misses the write and data gets stale
- **Read requests are sent to several nodes in parallel,** the newer data with version number is read

**Read repair**
- A read checks several nodes and if a stale value is detected, writes newer value to the replica
**Anti-entropy process**
- background process constantly looking for differences of data between replicas and copies missing data
- may be a significant delay

**Quorums for reading and writing**
- ex) 3 replicas, write is guaranteed successful in at least two nodes, then read is guaranteed successful at least two 
- as long as **w + r > n**, expect up-to-date value when reading
- otherwise writes or reads return an error
- Remember limitations with edge cases returning stale values
  - network interruption lets a client off from DB nodes, while others can access

## Detecting Concurrent Writes
_**Concurrency**_: if they are both unware of each other, regardless of the physical time
- For eventual consistency, the replicas should converge toward the same value

#### Last write wins (discarding concurrent writes)
Declare that each replica need only store the most “recent” value and allow “older” values to be overwritten and discarded.
- The ordering is arbitrary forced by attaching a timestamp to each write (Supported by Cassandra, Riak)
- Durability is not kept: if several concurrent writes to the same key, only one will survive

#### The “happens-before” relationship and concurrency
- Writes on the same key, no casual dependency
- Writes based on **`happened before`** write value, then casual dependency - No concurrency

**Capturing the happens-before relationship**
- Example in a single replica - based on if the later operation knew about or depended on the earlier one
- _Version number_ is used to determine whether two operations are concurrent
  - version number is maintained for every key, incrementing for each write
  - client must read before writing
  - writing includes the version number from previous read
  - Can overwrite all values with =< version number, but keep all values with > version number
- _Version vector_ for multiple replicas
  - collection of version numbers from all the replicas
  - version number per replica and per key, per replica, it keeps track of the the version number seen from other replicas
  -allows DB to distinguish between overwrites and concurrent writes

#### Merging concurrently written values
siblings: concurrently written values
- clients have to clean up by merging value siblings - same problem as conflict resolution in multi-leader replication

**approaches**
- Take union: But if removing is allowed, union might not be right
- System must leave a marker with appropriate version number to indicate removed item
  - deletion marker: tombstone
