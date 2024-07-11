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
- Single leader, eery write must go through it.
- multiple leaders process write forward the change to all the other nodes. Each leader is also a follower to the other leaders

## Use Cases for Multi-Leader Replication
### Multi-datacenter operation
Having a leader in each datacenter. 



