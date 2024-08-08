# The Trouble with Distributed Systems
This chapter is a thoroughly pessimistic and depressing overview of things that may go wrong in a distributed system.

## Faults and Partial Failures
In distributed systems, we have no choice but to confront the messy reality of the physical world - a remarkably wide
range of things can go wrong.

_**partial failure**_
- there may well be some parts of the system that are broken in some unpredictable way, even though other parts of the system are working fine.
- partial failures are nondeterministic: if you try to do anything involving multiple nodes and the network, it may sometimes work and sometimes unpredictably fail.

## Cloud Computing and Supercomputing
- Distributed system: Build a reliable system from unreliable components.

- _It would be unwise to assume that faults are rare and simply hope for the best. It is important to consider a wide range of possible faults—even fairly unlikely ones—and
to artificially create such situations in your testing environment to see what happens. In distributed systems, suspicion, pessimism, and paranoia pay off._

**systems for implementing internet services**
- Making the online service unavailable—for example, stopping the cluster for repair—is not acceptable. I
- Supercomputers are typically built from specialized hardware, where each node is quite reliable, and nodes communicate through shared memory and remote
direct memory access (RDMA).
- On the other hand, nodes in cloud services are built from commodity machines, which can provide equivalent performance at lower cost due to economies of scale, but also have higher failure rates.
- In a geographically distributed deployment (keeping data geographically close to your users to reduce access latency), communication most likely goes over the
internet, which is slow and unreliable compared to local networks.
- Supercomputers generally assume that all of their nodes are close together.

## Unreliable Networks
**shared-nothing systems**
- a bunch of machines connected by a network. The network is the only way those machines can communicate
- the dominant approach for building internet services, for several reasons
   - it’s comparatively cheap because it requires no special hardware
   - it can make use of commoditized cloud computing services,
   - it can achieve high reliability through redundancy across multiple geographically distributed datacenters.

**asynchronous packet networks** - many things can go wrong
1. Your request may have been lost (perhaps someone unplugged a network cable).
2. Your request may be waiting in a queue and will be delivered later (perhaps the network or the recipient is overloaded).
3. The remote node may have failed (perhaps it crashed or it was powered down).
4. The remote node may have temporarily stopped responding but it will start responding again later.
5. The remote node may have processed your request, but the response has been lost on the network (perhaps a network switch has been misconfigured).
6. The remote node may have processed your request, but the response has been delayed and will be delivered later (perhaps the network or your own machine is
overloaded).

_**timeout:**_
- The usual way of handling this issue
- after some time you give up waiting and assume that the response is not going to arrive.
- However, you still don’t know whether the remote node got your request or not

### Network Faults in Practice
Nobody is immune from network problems:
- network problems can be surprisingly common, even in controlled environments like a datacenter operated by one company
- Public cloud services such as EC2 are notorious for having frequent transient network glitches and well-managed private datacenter networks can be stabler environments.
- your software needs to be able to handle them.

### Detecting Faults
Many systems need to automatically detect faulty nodes
- A load balancer needs to stop sending requests to a node that is dead (i.e., take it out of rotation).
- In a distributed database with single-leader replication, if the leader fails, one of the followers needs to be promoted to be the new leader

**Positive/error response**
Rapid feedback about a remote node being down is useful, but you can’t count on it.
- you need a positive response from the application itself [24].
- Conversely, if something has gone wrong, you may get an error response at some level of the stack, but in general you have to assume that you will get no response at
all.
- You can retry a few times, wait for a timeout to elapse, and eventually declare the node dead if you don’t hear back within the timeout. 

### Timeouts and Unbounded Delays
No simple answer to how long timeouts should be
- asynchronous networks have unbounded delays
  - they try to deliver packets as quickly as possible, but there is no upper limit on the time it may take for a packet to arrive
  - most server implementations cannot guarantee some maximum time

#### Network congestion and queueing
variability of packet delays on computer networks is most often due to queueing
- If several different nodes simultaneously try to send packets to the same destination, the network switch must queue them up and feed them into the destination network link one by one
- When a packet reaches the destination machine, if all CPU cores are currently busy, the incoming request from the network is queued by the operating system
- In virtualized environments, a running operating system is often paused for tens of milliseconds while another virtual machine uses a CPU core. During this time, the VM cannot consume any data from the network, so the incoming data is queued
- TCP performs flow control (also known as congestion avoidance or backpressure), in which a node limits its own rate of sending in order to avoid overloading a network link or the receiving node

**public clouds and multi-tenant datacenters, you can only choose timeouts experimentally**
- measure the distribution of network round-trip times over an extended period
- over many machines, to determine the expected variability of delays.
- Then, taking into account your application’s characteristics, you can determine an appropriate trade-off between failure detection delay and risk of premature timeouts.

### Synchronous Versus Asynchronous Networks
Comparison with traditional fixed-line telephone network
- (non-cellular, non-VoIP) extremely reliable and _synchronous_
- it establishes a circuit: a fixed, guaranteed amount of bandwidth is allocated for the call, along the entire route between the two callers.

#### Can we not simply make network delays predictable?
**circuit in a telephone network VS TCP connection**
- a circuit is a fixed amount of reserved bandwidth which nobody else can use while the circuit is established
- a TCP connection opportunistically use whatever network bandwidth is available.

**optimized for _bursty traffic_**
- datacenter networks and the internet use packet switching for that
- requesting a web page, sending an email, or transferring a file doesn’t have any particular bandwidth requirement— just as quickly as possible.
- circuit, good for an audio or video call, which needs to transfer a fairly constant number of bits per second for the duration of the call
- using circuits for bursty data transfers wastes network capacity and makes transfers unnecessarily slow.
- By contrast, TCP dynamically adapts the rate of data transfer to the available network capacity.

## Unreliable Clocks
Clocks and time are important. Applications depend on clocks in various ways to answer questions like the following:
1. Has this request timed out yet?
2. What’s the 99th percentile response time of this service?
3. How many queries per second did this service handle on average in the last five
minutes?
4. How long did the user spend on our site?
5. When was this article published?
6. At what date and time should the reminder email be sent?
7. When does this cache entry expire?
8. What is the timestamp on this error message in the log file?

**Time-of-day clocks**
- what you intuitively expect of a clock: it returns the current date and time according to some calendar
- clock_gettime(CLOCK_REALTIME) on Linuxv and System.currentTimeMillis()

**Monotonic clocks**
- suitable for measuring a duration (time interval), such as a timeout or a service’s response time

### Clock Synchronization and Accuracy
Monotonic clocks don’t need synchronization, but time-of-day clocks need to be set according to an NTP (Network time protocol) server or other external time source in order to be useful.

### Relying on Synchronized Clocks
The problem with clocks is that while they seem simple and easy to use, they have a surprising number of pitfalls
- incorrect clocks easily go unnoticed
- If some piece of software is relying on an accurately synchronized clock, the result is more likely to be silent and subtle data loss than a dramatic crash
- it is essential that you also carefully monitor the clock offsets between all the machines
- If a node’s clock is significantly out of sync with other nodes, it may suddenly jump forward or back in time, and relying on it is dangerous 

**The unreliability of networks, clocks, and processes is an inevitable law of nature?**
- No. it is possible to give hard real-time response guarantees and bounded delays in networks
- but doing so is very expensive and results in lower utilization of hardware resources.
- Most non-safety-critical systems choose cheap and unreliable over expensive and reliable.
  - real time != high performance
 
## Knowledge, Truth, and Lies
A node in the network cannot know anything for sure
- it can only make guesses based on the messages it receives (or doesn’t receive) via the network.
- A node can only find out what state another node is in (what data it has stored, whether it is correctly functioning, etc.) by exchanging messages with it
- If a remote node doesn’t respond, there is no way of knowing what state it is in, because problems in the network cannot reliably be distinguished from problems at a node.

### The Truth Is Defined by the Majority
a node cannot necessarily trust its own judgment of a situation
- A distributed system cannot exclusively rely on a single node, because a node may fail at any time, potentially leaving the system stuck and unable to recover.
- Instead, many distributed algorithms rely on a quorum, that is, voting among the nodes (page 179)
  - decisions require some minimum number of votes from several nodes in order to reduce the dependence on any one particular node.

#### The leader and the lock
Frequently, a system requires there to be only one of some thing
- the leader of the partition, the holder of the lock (transaction), the request handler of the user who successfully grabbed the username

**even if a node believes that it is “the chosen one”**
- that doesn’t necessarily mean a quorum of nodes agrees!
- A node may have formerly been the leader, but if the other nodes declared it dead in the meantime (e.g., due to a network interruption or GC pause)
  - it may have been demoted and another leader may have already been elected.

### Byzantine Faults
there is a risk that nodes may “lie” (send arbitrary faulty or corrupted responses)
- if a node may claim to have received a particular message when in fact it didn’t.
- the problem of reaching consensus in this untrusting environment is known as the Byzantine Generals Problem

**Byzantine fault-tolerant system**
- if it continues to operate correctly even if some of the nodes are malfunctioning and not obeying the protocol or if malicious attackers are interfering with the network.
- This concern is relevant in certain specific circumstances
  - In aerospace environments - (e.g., an aircraft crashing and killing everyone on board, or a rocket colliding with the International Space Station)
  - peer-to-peer networks like Bitcoin and other block chains - multiple participating organizations, some participants may attempt to cheat or defraud others.

### System Model and Reality
To solve distributed system problems, algorithms need to be written not depending too heavily on the details of hardware/software config.
- It requires that we somehow formalize the kinds of faults that we expect to happen in a system
- We do this by defining a system model, which is an abstraction that describes what things an algorithm may assume.

#### System models
_Synchronous model_
- assumes bounded network delay, bounded process pauses, and bounded clock error.
- This does not imply exactly synchronized clocks or zero network delay; it just means you know that network delay, pauses, and clock drift will never exceed some fixed upper bound
- not a realistic model of most practical systems, because (as discussed in this chapter) unbounded delays and pauses do occur.

**_Partially synchronous model_**
- a system behaves like a synchronous system most of the time, but it sometimes exceeds the bounds for network delay, process pauses, and clock drift.
- realistic model of many systems: most of the time, networks and processes are quite well behaved
- but we have to reckon with the fact that any timing assumptions may be shattered occasionally. When this happens, network delay, pauses, and clock error may become arbitrarily large.

_Asynchronous model_
- an algorithm is not allowed to make any timing assumptions
- it does not even have a clock (so it cannot use timeouts)
- Some algorithms can be designed for the asynchronous model, but it is very restrictive.

#### system models for nodes - for node failures
_Crash-stop faults_
- an algorithm may assume that a node can fail in only one way, namely by crashing
- the node may suddenly stop responding at any moment, and thereafter that node never comes back.

_Crash-recovery faults_
- assume that nodes may crash at any moment, and perhaps start responding again after some unknown time.
- nodes are assumed to have stable storage (i.e., nonvolatile disk storage) that is preserved across crashes, while the in-memory state is assumed to be lost.

_Byzantine (arbitrary) faults_
- Nodes may do absolutely anything, including trying to trick and deceive other nodes

#### Correctness of an algorithm
What properties of a correct distributed algorithm
_Uniqueness_: safety property
- No two requests for a fencing token return the same value.
_Monotonic sequence_: safety property
- If request x returned token tx, and request y returned token ty, and x completed before y began, then tx < ty.
_Availability_: liveness property
- A node that requests a fencing token and does not crash eventually receives a response.

Safety is often informally defined as nothing bad happens, and liveness as something good eventually happens.
