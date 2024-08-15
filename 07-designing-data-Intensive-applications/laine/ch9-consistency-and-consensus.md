## Linearizability 
- stronger consistency models that data systems may choose to provide. They don’t come for free: systems with stronger guarantees may have worse performance or be less fault-tolerant than systems with weaker guarantees. Nevertheless, stronger guarantees can be appealing because they are easier to use correctly. 

- distributed consistency is mostly about coordinating the state of replicas in the face of delays and faults.
- whereas transaction isolation is primarily about avoiding race conditions due to concurrently executing transactions


### What Makes a System Linearizable?
- to make a system appear as if there is only a single copy of the data. 
- In a linearizable system we imagine that there must be some point in time (between the start and end of the write operation) at which the value of x atomically flips from 0 to 1. Thus, if one client’s read returns the new value 1, all subsequent reads must also return the new value, even if the write operation has not yet completed.


**Linearizability Versus Serializability**
- recency guarantee on reads and writes of a register (an individual object). It doesn’t group operations together into transactions, so it does not prevent problems such as write skew.
- serializable snapshot isolation is not linearizable

### Relying on Linearizability
In what circumstances is linearizability useful? 

- Locking and leader election: A system that uses single-leader replication needs to ensure that there is indeed only one leader, not several
- Constraints and uniqueness guarantees
  - a username or email address must uniquely identify one user
  - If you want to enforce this constraint as the data is written (such that if two people try to concurrently create a user or a file with the same name, one of them will be returned an error), you need linearizability.
  - similar to a lock
- Constraints and uniqueness guarantees: This problem arises because there are two different communication channels between the web server and the resizer

### Implementing Linearizable Systems
- Single-leader replication (potentially linearizable)
- Consensus algorithms (linearizable): consensus protocols contain measures to prevent split brain and stale replicas.
- Multi-leader replication (not linearizable)
- Leaderless replication (probably not linearizable)

### The Cost of Linearizability
#### The CAP theorem
Applications that don’t require linearizability can be more tolerant of network problems. This insight is popularly known as the CAP theorem
- If your application requires linearizability, and some replicas are disconnected from the other replicas due to a network problem, then some replicas cannot process requests while they are disconnected: they must either wait until the network problem is fixed, or return an error (either way, they become unavailable).
- If your application does not require linearizability, then it can be written in a way that each replica can process requests independently, even if it is disconnected from other replicas (e.g., multi-leader). In this case, the application can remain available in the face of a network problem, but its behavior is not linearizable.

#### Linearizability and network delays
Only few systems are actually linearizable in practice. 
- For example, even RAM on a modern multi-core CPU is not linearizable.. The reason for dropping linearizability is performance, not fault tolerance.
- The same is true of many distributed databases that choose not to provide lineariza‐ ble guarantees: they do so primarily to increase performance, not so much for fault tolerance. Linearizability is slow—and this is true all the time, not only during a network fault.

## Ordering Guarantees
- deep connections between ordering, linearizability, and consensus.

### Ordering and Causality
ordering helps preserve causality. (340p examples)
- Causality imposes an ordering on events: cause comes before effect; a message is sent before that message is received; the question comes before the answer. And, like in real life, one thing leads to another: 

#### The causal order is not a total order
- total order: A total order allows any two elements to be compared. i.e. 5 < 13
- partial order: incomparable. {a, b} and {b, c}?

#### Linearizability is stronger than causal consistency
linearizability implies causality: any system that is linearizable will preserve causality correctly
- making a system linearizable can harm its performance and availability, especially if the system has significant network delays

**middle ground is possible.** 
- Linearizability is not the only way of preserving causality
- A system can be causally consistent without incurring the performance hit of making it linearizable (in particular, the CAP theorem does not apply).
- In fact, causal consistency is the strongest possible consistency model that does not slow down due to network delays, and remains available in the face of network failures

#### Capturing causal dependencies
- The techniques for determining which operation happened before which other operation are similar to what we discussed in “Detecting Concurrent Writes” on page 184. That section discussed causality in a leaderless datastore, where we need to detect concurrent writes to the same key in order to prevent lost updates. Causal consistency goes further: it needs to track causal dependencies across the entire database, not just for a single key. Version vectors can be generalized to do this.
