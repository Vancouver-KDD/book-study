# CHAPTER 8 - The Trouble with Distributed Systems



## Faults and Partial Failures
> Why is it difficult to deal with the Distributed Systems?

- Single computer
  - fairly predictable: either it works or it doesn’t
  - mostly just a consequence of badly written software
  - If there is a hardware problem (e.g., memory corruption or a loose connector), the consequence is usually a total system failure.

- Distributed system
  - Partial Failures: some parts of the system that are broken in some unpredictable way, but others are fine.
  - In multiple nodes and the network -> sometimes work and sometimes unpredictably fail.
  - If we want to make distributed systems work, we must accept
    - (1) the possibility of partial failure and
    - (2) build fault-tolerance mechanisms into the software.    
  - In other words, we need to build a reliable system from unreliable components.

### Approaches to Handling Faults of the Distributed System
> Large-scale computing systems in internet services - Cloud Computing
- associated with multi-tenant datacenters using connectin with an IP network (often Ethernet), elastic/on-demand resource allocation.
  
> Approaches to handling faults
- Many internet-related applications are *On-line*, in the sense that they need to be able to serve users with low latency at any time.
- Nodes in cloud services are built from commodity machines
  - Equivalent performance at lower cost due to economies of scale
  - But also have higher failure rates.
- Large datacenter networks are often based on IP and Ethernet.
- It is reasonable to assume that something is always broken in a system with thousands of nodes
- If the system can tolerate failed nodes and still keep working as a whole, that is a very useful feature for operations and maintenance
- Communication most likely goes over the internet
  - => Slow and Unreliable (compared to local networks)

> Again
- We need to build a reliable system from unreliable components
- We must accept the possibility of partial failure and build fault-tolerance mechanisms into the software.

## Unreliable Networks

- The internet and most internal networks in datacenters (often Ethernet) ==> *asynchronous packet networks*.
- One node can send a message (a packet) to another node,
  - but No guarantees as to when it will arrive, or whether it will arrive at all.
  
- If you send a request and don’t get a response, the reason for that is the following 3 thing.

![](images/fg8-1.jpg "")

  - (a) Request was lost, 
  - (b) Remote node is down, or 
  - (c) Response was lost.
    
- It is impossible to tell why.
- **Timeout** : The usual way of handling the issue in the Asynchronous Packet Networks(Internet/Most Internal Networks)
  - After some time you give up waiting and assume that the response is not going to arrive.
 
### Network Faults in Practice
- Building a reliable network is still not an easy task.
- Adding redundant networking gear doesn’t reduce faults as much as you might hope
- Instead, the software needs to be able to handle them, Considering faults can occur.
  - Handling network faults doesn’t necessarily mean tolerating them
  - Show an error message to users while your network is in problems.
  - However, by having software react to network problems, you should ensure that the system can recover from them.

### Detecting Faults
- In some specific circumstances, you might get some feedback to explain something not working.
- Unfortunately, however, the uncertainty about the network makes it difficult to tell whether a node is working or not.

### Timeouts and Unbounded Delays
- If a timeout is the only sure way of detecting a fault, then how long should the timeout be?
  - A long timeout means a long wait until a node is declared dead
  - A short timeout means it's faster but, carries a higher risk of incorrectly declaring a node dead.
    - if the node is actually alive, the action may end up being performed twice.
    - Placing additional load on other nodes might cause an overload and a cascading failure.
  
- Asynchronous networks have unbounded delays
  - They deliver packets as quickly as possible
  - But there is no upper limit on the time to deliver packets
  - No guarantee to handle requests within some maximum time

#### Network congestion and queueing
- Variability of packet delays on computer networks is most often due to Queueing
  - i.e., (Figure 8-2) If several different nodes simultaneously try to send packets to the same destination
    - the network switch must queue them up and feed them into the destination network link one by one
  - i.e., When all CPU cores in the destination are currently busy
  
![](images/fg8-2.jpg "")


### Synchronous Versus Asynchronous Networks
What about the Reliable and Synchronous Network at the hardware level?

> Datacenter networks vs Traditional fixed-line telephone network(non-cellular, non-VoIP)

- Traditional fixed-line telephone network(non-cellular, non-VoIP)
  - *Synchronous* / Bounded delays / No Suffering from queueing
  - circuit-switched networks 
  - Extremely reliable
  - requires a constantly low end-to-end latency 
  - Bounded delay: maximum end-to-end latency of the network is fixed
  - How it works?
    - When you make a call over the telephone network, it establishes a *circuit*
    - This circuit remains in place until the call ends.
    - A circuit is a fixed amount of reserved bandwidth which nobody else can use while the circuit is established.
    
- Datacenter networks
  - *Asynchronous* / Unbounded delays / Suffering from queueing
  - Packet-switched protocols 
  - TCP connection
  - Optimized for bursty traffic
  - use whatever network bandwidth is available
  - A variable-sized block of data(e.g., an email or a web page)
  - transfer data in the shortest time possible.
  - While a TCP connection is idle, it doesn’t use any bandwidth.  
  - Requesting a web page / Sending an email / Transferring a file
  - No particular Bandwidth
  - As quickly As possible

#### Consequently,
- Currently deployed technology does not allow us to make any guarantees about delays or reliability of the network
- We have to assume that *network congestion*, *queueing*, and *unbounded delays* will happen.
- Consequently, there’s no “correct” value for timeouts—they need to be determined experimentally.

## Unreliable Clocks
- In a distributed system, time is a tricky business, because communication is not instantaneous.
- The time when a message is received is always later than the time when it is sent
  - but due to variable delays in the network, we don’t know how much later.
- Each machine on the network has its own clock with its own notion of time.
  - ==> It makes it difficult to determine the order in which things happened when multiple machines are involved, 

### Monotonic Versus Time-of-Day Clocks 
- Modern computers have at least two different kinds of clocks: a time-of-day clock / a monotonic clock.

#### Time-of-day clocks
- Returns the current date and time according to the reference point each system has (usually midnight of January 1, 1970 UTC)
- Time-of-day clocks are usually synchronized with NTP(Network Time Protocol)
- Not suitable for measuring elapsed time
  - cuz 1. it may *jump back* in time if the local clock is too far ahead of the NTP server.
  - cuz 2. ignore *leap seconds(23:59:60* <> 00:00:00)
    
#### Monotonic clocks
- A monotonic clock is suitable for measuring a duration (time interval: timeout or service’s response time)
  - for example, clock_gettime(CLOCK_MONOTONIC) on Linux / System.nanoTime() in Java.
  - But, comparing two monotonic clock values from two different computers is meaningless - not same
- By default, NTP allows the clock rate to be speeded up or slowed down by up to 0.05%,
  - but NTP cannot cause the monotonic clock to jump forward or backward.
- In a distributed system, using a monotonic clock for measuring elapsed time (e.g.,timeouts) is usually fine,
  - because it doesn’t assume any synchronization between different nodes’ clocks and is not sensitive to slight inaccuracies of measurement.

### Clock Synchronization and Accuracy
- Unfortunately, our methods for getting a clock to tell the correct time aren’t nearly as reliable or accurate as you might hope.
  - hardware clocks and NTP are fickle.
- Examples
  - The quartz clock in a computer is not very accurate: it drifts (runs faster or slower than it should).
  - If a computer’s clock differs too much from an NTP server, it may refuse to synchronize, or the local clock will be forcibly reset.
  - If a node is accidentally firewalled off from NTP servers, the misconfiguration may go unnoticed for some time.
  - NTP synchronization can only be as good as the network delay, so there is a limit to its accuracy when you’re on a congested network with variable packet delays
  - Some NTP servers are wrong or misconfigured, reporting time that is off by hours
  - Leap seconds result in a minute that is 59 seconds or 61 seconds long, which messes up timing assumptions in systems that are not designed with leap seconds in mind
  - In virtual machines, the hardware clock is virtualized, which raises additional challenges for applications that need accurate timekeeping

### Relying on Synchronized Clocks
- Thus, if you use software that requires synchronized clocks, it is essential that you also carefully monitor the clock offsets between all the machines.
- Any node whose clock drifts too far from the others should be declared dead and removed from the cluster.
- Such monitoring ensures that you notice the broken clocks before they can cause too much damage.

#### Timestamps for ordering events

![](images/fg8-3.jpg "")
- The write by client B is causally later than the write by client A, but B’s write has an earlier timestamp.
- This conflict resolution strategy is called last write wins (LWW), and it is widely used in both multi-leader replication and leaderless databases such as Cassandra and Riak
- but this doesn’t change the fundamental problems with LWW
- So-called logical clocks [56, 57], which are based on incrementing counters rather than an oscillating quartz crystal, are a safer alternative for ordering events.
- Logical clocks do not measure the time of day or the number of seconds elapsed, only the relative ordering of events


#### Clock readings have a confidence interval
- it doesn’t make sense to think of a clock reading as a point in time
- it is more like a range of times, within a confidence interval.
- because it is not accurate
  - quartz drift since your last sync with the server,
  - plus the NTP server’s uncertainty,
  - plus the network round-trip time to the server
  
#### Synchronized clocks for global snapshots
- snapshot isolation is a very useful feature in databases that need to support both small, fast read-write transactions and large, long-running read-only transactions
- but, when a database is distributed across many machines, potentially in multiple datacenters, a global, monotonically increasing transaction ID (across all partitions) is difficult to generate,
- The uncertainty might cause an issues. 

### Process Pauses
- Several programming languages have Garbage Collectors (GCs) that sometimes have to stop all running threads at runtime.
- It can sometimes last up to a few minutes and cannot run completely parallel to the application code.
- In a virtual environment, virtual equipment can be suspended (stop running all processes and save memory content to disk) and then resumed.
- If the operating system switches contextually to another thread or the hypervisor switches to another virtual device, the currently running thread can stop at any point in the code.

- In a single device, you can make application code thread-safe using mutex, semaphore, atomic counters, and the like.

- However, the above tools cannot be utilized because distributed systems do not have shared memory and only send and receive messages over unreliable networks.

- Therefore, it should be assumed that the node in the distributed system can stop running for a considerable amount of time at any point. While stopped, an externally stopped node may declare dead because it did not respond.
- Even if a stopped node is executed again later, the node does not know that it was stopped until the code checks the clock.
