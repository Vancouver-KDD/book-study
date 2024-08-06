
- **Challenges of Distributed Systems:**
	- Distributed systems are very different from writing software on a single computer, primarily because various problems can occur in many different ways.
	- It is crucial to address how to handle the system when problems arise.
	- Issues like failover for replicas, replication lag, and concurrency control in transactions are considered.

- **Realistic Approach:**
	- The previously covered content remains optimistic, but reality is more complex and can involve many unexpected problems.
	- Experienced system operators would say it is reasonable to predict and prepare for these issues.

- **Assumption Excluding Byzantine Faults:**
	- The text assumes and discusses failures excluding Byzantine faults.
	- This is because Byzantine faults are considered difficult and special problems to solve.

- **Network and Timing Issues:***
	- *Reliability of Networks:** Networks are inherently unreliable, and network issues can lead to data loss or delays.
	- **Clocks and Timing Issues:** Clocks in distributed systems cannot be entirely trusted, and timing issues can cause data consistency problems.

- **Possibility of Avoiding Problems:**
	- It explores the extent to which these problems can be avoided.
	- It also includes exploring methods to understand the system's state and infer what has happened.

- **Meeting User Guarantees:**
	- The engineer's mission is to build a system that performs its function even when everything goes wrong.
	- This means building a system that meets the guarantees users expect.

- **Future Discussion:**
	- Chapter 9 examines several algorithms that can provide these guarantees in distributed systems.
	- Understanding the challenges faced in distributed systems is important in this chapter.

## Faults and Partial Failures

- **Deterministic Behavior of a Single Computer:** A single computer generally operates in a predictable manner, meaning it is in one of two states: **working** or **not working**. Software instability is usually due to faulty code or hardware failure. In such cases, it is better to experience a **system crash** or **total failure** (e.g., **kernel panic**) rather than returning incorrect results because it is easier to manage and diagnose.

- **Challenges of Distributed Systems:** In distributed systems, we face the complexities of the physical world, leading to various issues. **Partial Failure** refers to situations where some parts of the system fail while other parts continue to operate normally. Due to this **non-determinism**, it is often difficult to determine whether a request has succeeded or failed.

- **Examples of Partial Failure:** **Network partition**, **Power Distribution Unit (PDU) failure**, **Switch failure**, and **Data Center outage** are examples of various partial failures that can occur unpredictably.

- **Statistical Analysis:** Due to the non-determinism of partial failures, distributed systems often use **statistical methods** to analyze and manage the system.

### Cloud Computing and Supercomputing

- **Different Philosophies:** There are various philosophies for building large-scale computing systems, such as **Supercomputing** and **Cloud Computing**. Supercomputing operates by restarting from a **checkpoint** when errors occur, while cloud systems emphasize **continuous availability** and **fault tolerance**.

- **Differences between Supercomputers and Cloud Systems:**
  - **Supercomputers:** Use specialized hardware and assume that all nodes are close together. They stop and restart operations in case of errors.
  - **Cloud Systems:** Use **commodity hardware**, accept higher failure rates, and emphasize high availability and fault tolerance. They must consider network uncertainty and geographic distribution.

- **Handling Errors in Large-Scale Systems:** Large-scale systems require **fault-tolerant** design because there is always a possibility that some component will fail.

- **Tolerance to Partial Failures:** Internet services must endure partial failures and remain maintainable. This means that, unlike supercomputers, they must continue to operate without **downtime**.

- **Proactive Error Handling:** Distributed systems should test various error scenarios and ensure that software functions correctly. During design, a suspicious, pessimistic, and paranoid approach can be beneficial.

### Building a Reliable System from Unreliable Components

- **Enhancing Reliability:** It is possible to build a more reliable system from unreliable components, which can be achieved through technologies like **error-correcting codes** or **TCP**. These technologies hide low-level errors and provide a more stable system experience.

- **Limits of Reliability:** A system can be more reliable than its components, but there are limits. Error-correcting codes can handle a small number of single-bit errors but have limitations when interference is severe. TCP can manage packet issues but cannot solve **network delays**.

- **End-to-End Argument:** A reliable upper-layer system is not perfect, but by handling difficult low-level errors, it can make the remaining errors easier to handle. This concept is explored more deeply in the "End-to-End Argument."




## Unreliable Networks

- **Shared-Nothing Systems:** The distributed systems discussed in this book are shared-nothing systems, which are composed of multiple machines connected through a network. Each machine has its own memory and disk and communicates with other machines only through the network. This architecture is widely used in building internet services because it is cost-effective, does not require special hardware, and can take advantage of commercial cloud computing services.

- **Asynchronous Packet Networks:** The internet and data center networks (primarily Ethernet) are asynchronous packet networks, where a node can send a message to another node without guarantees about its arrival or timing. Issues in network communication can include request loss, increased latency, remote node failures, and response loss and delays. In asynchronous networks, these issues cannot be distinguished.

- **Timeouts:** A common approach to address these problems is to set timeouts. After a certain period, the system gives up waiting for a response and assumes that it cannot determine whether the request was successful.

### Network Faults in Practice

- **Network Faults:** Despite decades of advancements in computer networking, network faults still occur frequently, even in data center environments. Causes of network faults include human error, misconfigured switches, and temporary network glitches.

- **Network Partitions:** When network faults sever parts of the network from the rest, this is called a network partition, which can cause ongoing issues for the system.

- **Handling Network Faults:** Since network faults are unavoidable, software must be able to handle them. All communications over the network are subject to failure, and showing users error messages can be a valid approach. It is crucial to know how software responds to network issues and to verify that the system can recover from problems. Testing system reactions by intentionally inducing network problems can also be useful.

### Detecting Faults

- **Fault Detection:** Many systems need to automatically detect failed nodes. For instance, load balancers should not send requests to dead nodes, and in distributed databases, if a leader node fails, one of the followers should be promoted to leader.

- **Uncertainty of Node Status:** Due to the uncertainty of networks, it is challenging to determine whether a node is operational. In some cases, quick feedback can be obtained, but it cannot always be relied upon.

  - **Feedback Mechanisms:** There are several ways to determine the status of a node through feedback. For example, if a node can be reached but no process is listening on the target port, the TCP connection may be closed with an RST or FIN packet. Additionally, if a node process crashes, a script can be used to notify other nodes of the crash.

- **Handling Absence of Responses:** If something goes wrong, it is possible to receive an error response at some level of the stack, but generally, the possibility of receiving no response at all must be assumed. After a few retries and no response within the timeout period, the node can be declared dead.

### Timeouts and Unbounded Delays

- **Challenges of Setting Timeouts:** Timeouts are a crucial method for detecting faults, but determining the appropriate duration is a complex issue. Long timeouts may delay fault detection and cause users to experience wait times. On the other hand, short timeouts risk mistakenly interpreting transient delays as faults, leading to the unnecessary declaration of a node as dead.

- **Problems with Prematurely Declaring Nodes Dead:** Declaring a node dead too quickly may result in redundant processing of tasks that are still being executed by a live node, increasing system load and potentially causing cascading failures.

- **Timeouts in Ideal Systems:** Theoretically, if the network guarantees a maximum delay \(d\) and a node processes requests within a maximum time \(r\), the timeout can be set to \(2d + r\). However, real systems have asynchronous networks with unbounded delays, and most servers do not guarantee an upper limit on processing time.

#### Network Congestion and Queueing

- **Causes of Delay:** Variability in packet delay in computer networks is primarily caused by queuing. When multiple nodes send packets to the same destination simultaneously, network congestion occurs, increasing wait times.

  - **Switch Queues:** When network switches become full, packets are dropped, necessitating retransmission, which increases network delay.

  - **CPU and VM Queues:** Even when packets reach their destination, they may be queued if the CPU is busy. In virtualized environments, VMs may be paused, causing additional queuing delays.

  - **TCP Flow Control:** TCP reduces network congestion by limiting transmission speed through congestion avoidance, but this can result in additional queuing on the sender's side.

![Network Congestion](https://i.imgur.com/XwDr20k.png)

*Figure 8-2. When several machines send network traffic to the same destination, switch queues may become full. Here, ports 1, 2, and 4 all attempt to send packets to port 3.*

#### TCP Versus UDP

- **Advantages of UDP:** Latency-sensitive applications, such as video conferencing and VoIP, use UDP instead of TCP. UDP does not perform flow control or retransmit lost packets, reducing the variability in delay. This is advantageous in situations where delayed data is meaningless. For example, in a VoIP call, there is no time to retransmit lost packets, so they are replaced with silence.

- **Variability of Network Delays:** Network delays are particularly variable when the system is near maximum capacity. In public clouds and multi-tenant data centers, resources are shared among multiple customers, and one user's resource usage can impact network delays for others.

- **Experimental Timeout Configuration:** It is necessary to measure the distribution of network round-trip times and consider application characteristics when setting appropriate timeouts. Instead of a fixed timeout, systems can measure the variability in response times and automatically adjust timeouts. This approach is used in the Phi Accrual failure detector of Akka and Cassandra. TCP retransmission timeouts also operate similarly.

### Synchronous Versus Asynchronous Networks

- **Concept of Synchronous Networks:** If we assume that a network in a distributed system delivers packets with a fixed maximum delay and does not drop packets, the system would be much simpler. Traditional fixed-line telephone networks offer this reliability and predictability by allocating a fixed amount of bandwidth across the entire call path, ensuring stable delay.

- **Telephone Networks vs. TCP Connections:** In telephone networks, once a circuit is established, a constant amount of bandwidth is allocated. In contrast, TCP connections utilize available network bandwidth and transmit data in variable-sized blocks. TCP connections do not use bandwidth when idle and have variable packet delays.

#### Can we not simply make network delays predictable?

- **Circuit Switching vs. Packet Switching:** Data center networks and the internet use packet switching. This is optimized for bursty traffic and is suitable for requests that do not require a constant bit rate, such as web page requests, email transmissions, and file transfers. Packet switching dynamically adjusts data transfer speeds to optimize network capacity.

- **Attempts at Hybrid Networks:** Hybrid networks like ATM attempted to combine circuit switching and packet switching but could not entirely eliminate congestion and delays. While latency can be statistically limited through QoS and admission control, these features are not currently activated in multi-tenant data centers and public clouds.

#### Latency and Resource Utilization

- **Consequences of Resource Partitioning:** Variable network delays result from dynamic resource partitioning. Instead of allocating fixed bandwidth, the internet dynamically shares network bandwidth, leading to queues. This dynamic resource partitioning maximizes network and CPU utilization but introduces variability in delays.

- **Hardware Utilization vs. Delay Guarantees:** Partitioning resources statically can achieve delay guarantees, but at the cost of reduced utilization. Conversely, multi-tenancy and dynamic resource partitioning offer high utilization and low cost but have the drawback of variable delays.

- **Delay and Reliability Guarantees:** Currently deployed technologies do not provide guarantees for network delays or reliability, so it is necessary to assume that network congestion, queues, and unbounded delays may occur. Therefore, timeout configurations must be determined experimentally.



## Unreliable Clocks

Clocks and time are critical elements in distributed systems. Clocks are used by applications to determine timeouts, measure response times, and record event timestamps. However, the concept of time in distributed systems is complex. Network delays are variable, making it difficult to determine the order of events when multiple machines are involved, and each machine's clock can differ slightly.

### Monotonic vs. Time-of-Day Clocks

Modern computers have two types of clocks: **Monotonic clocks** and **Time-of-day clocks**.

#### Time-of-day clocks

- **Definition:**
  - These clocks represent the actual time of day, i.e., the real-world time.
  - They track the system's current date and time, typically synchronized using the Network Time Protocol (NTP).

- **Characteristics:**
  - They operate similarly to a wall clock.
  - They can experience anomalies such as leap seconds or clock adjustments.
  - While synchronized with NTP, inaccuracies can occur due to network delays or clock resets.
  - Examples include Linux's `clock_gettime(CLOCK_REALTIME)` and Java's `System.currentTimeMillis()`.

#### Monotonic clocks

- **Definition:**
  - **Monotonic:** Refers to a property of always increasing or decreasing.
  - Monotonic clocks always have a non-decreasing property, meaning the measured time at any point is always less than or equal to any subsequent time.

- **Characteristics:**
  - Primarily used for measuring timeouts and service response times.
  - The absolute value of the clock is not important; rather, it is useful for measuring the difference between two points in time.
  - Comparing monotonic clock values from different nodes is meaningless.
  - Examples include Linux's `clock_gettime(CLOCK_MONOTONIC)` and Java's `System.nanoTime()`.

### Clock Synchronization and Accuracy

- **Limitations of NTP:**
  - NTP is used to synchronize computer clocks, but it does not guarantee perfect accuracy.
  - The quartz clock in a computer can drift due to environmental factors such as temperature, and synchronization issues with the NTP server can cause time to jump forward or backward.

- **Inaccuracy of Clocks:**
  - Quartz clocks can drift by several seconds per day.
  - NTP is affected by network delays and can experience errors.
  - Incorrect NTP servers can cause clocks to be significantly off.
  - Leap seconds disrupt time-related assumptions and can cause conflicts in some systems.

- **Clocks in Virtual Machines:**
  - In virtual machines, clocks are virtualized, making precise time measurement challenging.
  - When CPU cores are shared among multiple VMs, a VM can be paused, causing time jumps.

- **Reliability of Clocks in External Devices:**
  - In mobile or embedded devices, hardware clocks may not be reliable.
  - Users can intentionally misconfigure the clock.

- **Achieving High Time Accuracy:**
  - Financial institutions and similar organizations achieve high time accuracy using GPS receivers and the Precision Time Protocol (PTP). However, achieving this level of accuracy requires significant cost and expertise, and incorrect settings can lead to substantial errors quickly.

### Relying on Synchronized Clocks

While clocks may appear easy to use, they come with various pitfalls:
- A day may not be exactly 86,400 seconds.
- Time-of-day clocks can be set backward.
- The time on different nodes can vary significantly.

Thus, while clocks generally work well, software must be prepared to handle faulty clocks.

- **Issues with Clocks:**
  - Faulty clocks are not easily noticeable and can lead to data loss.
  - Therefore, software using synchronized clocks should monitor clock offsets across all machines and remove nodes with faulty clocks from the cluster.

#### Timestamps for Ordering Events

Using clocks to determine the order of events is tempting but risky. For example, in a distributed database, it is challenging to determine which of two clients' writes occurred first.

![Event Ordering with Clocks](https://i.imgur.com/xmVfpA8.png)

*Figure 8-3. Client B's write occurs causally later than Client A's write, but B's write has an earlier timestamp.*

- **Last Write Wins (LWW):** Even with good clock synchronization, timestamps can misorder events, leading to data loss and violations of causality.

- **Logical Clocks:** Logical clocks provide a safer alternative to LWW by ordering events based on incrementing counters rather than physical clocks.

#### Clock readings have a confidence interval

Clock readings can be inaccurate due to quartz clock drift and NTP limitations. Therefore, it is essential to view clock readings with a confidence interval.

- **Confidence Interval:** Clock readings should be interpreted within an uncertainty range. For example, if a clock provides the current time with an accuracy of ±100ms, the microsecond digits in the timestamp are meaningless.

- **TrueTime API:** Google's Spanner uses the TrueTime API to explicitly report the confidence interval of the clock. When requesting the current time, it returns the earliest and latest possible times, ensuring that the actual time falls within that interval.

#### Synchronized clocks for global snapshots

**Snapshot Isolation** is beneficial when a database supports small, fast read-write transactions alongside large, long-running read-only transactions. It allows the database to be viewed in a consistent state at a particular point in time, enabling read-only transactions without impacting read-write transactions.

- **Single-node Database:** A simple counter can be used to generate transaction IDs.
- **Distributed Database:** When distributed across multiple machines, generating globally monotonic transaction IDs across all partitions is challenging and requires coordination.

Can synchronized time-of-day clocks be used as transaction IDs for distributed transaction semantics? If synchronization is good enough, later transactions can have higher timestamps, but the uncertainty in clock accuracy poses a problem.

- **Spanner's Approach:**
  - **TrueTime API:** Spanner uses TrueTime API's confidence intervals for snapshot isolation across data centers.
  - **Using Confidence Intervals:** If two confidence intervals do not overlap, it can be determined that B occurred after A. If intervals overlap, the order cannot be known.

```
If A = [A(earliest), A(latest)]
and B = [B(earliest), B(latest)],
then if A(earliest) < A(latest) < B(earliest) < B(latest),
```

- **Intentional Waiting:** Spanner intentionally waits for the length of the confidence interval before committing read-write transactions to ensure the timestamps reflect causality, guaranteeing that confidence intervals do not overlap.

- **Clock Synchronization:** Google deploys GPS receivers or atomic clocks in each data center to synchronize clocks within about 7ms, minimizing uncertainty.

This approach is uniquely implemented in Google's Spanner and is not yet widely adopted in mainstream databases outside Google. Clock synchronization for distributed transaction semantics remains an active research area.

### Process Pauses

**Leader Confirmation Problem:** In a database, there is one leader per partition, and only the leader is allowed to perform writes. How can we confirm that the leader is still valid?

- **Using Leases:** A leader can obtain a **lease** from another node to guarantee its leadership for a certain period. A lease is similar to a lock with a timeout. To remain the leader, the lease must be periodically renewed. If a node fails, it stops renewing the lease, allowing another node to take over when it expires.

```java
while (true) {
    request = getIncomingRequest();

    // Ensure that the lease always has at least 10 seconds remaining
    if (lease.expiryTimeMillis - System.currentTimeMillis() < 10000) {
        lease = lease.renew();
    }

    if (lease.isValid()) {
        process(request);
    }
}
```

- **Problems with the Code:**
  - **Dependency on Synchronized Clocks:** Since the lease expiration time is set by another machine, strange behavior can occur if the local clock is not synchronized.
  - **Unexpected Pauses:** If a thread is paused, the lease may expire, and another node might already have assumed leadership. The code might continue executing without recognizing this fact.

- **Possibility of Pauses:**
  - **Garbage Collection (GC):** The Java Virtual Machine, among others, has a GC that requires all threads to be paused, which can last for minutes.
  - **Virtualization Environment:** A virtual machine can be paused and restarted later.
  - **Operating System:** Threads can be paused due to context switching or disk I/O.
  - **Other Reasons:** Execution can be arbitrarily suspended for reasons like closing a laptop lid.

In all these cases, an executing thread can be preempted at any point and restarted after a certain period. The thread may not even realize it was paused. This issue is similar to writing multithreaded code on a single computer: nothing can be assumed about timing. Arbitrary context switches and parallel processing can occur.

When writing multithreaded code on a single machine, there are good tools to ensure thread safety: mutexes, semaphores, atomic counters, lock-free data structures, blocking queues, etc. Unfortunately, these tools do not translate directly to distributed systems. Distributed systems lack shared memory and only have message passing over unreliable networks.

Nodes in distributed systems must assume that execution can be paused for a significant amount of time at any point. During a pause, the rest of the world continues moving, and a paused node may be declared dead due to non-responsiveness. Eventually, the paused node may continue execution without even knowing it was asleep until it checks the clock later.

#### Response Time Guarantees

In most programming languages and operating systems, threads and processes can be paused indefinitely. There are ways to mitigate

 these pauses.

- **Hard Real-Time Systems:** These systems operate in environments where failing to respond within a specific time can lead to catastrophic damage. Aircraft, rockets, robots, and cars require fast and predictable responses to sensor inputs.

```
Is real-time really real?

In embedded systems, real-time means the system is carefully designed and tested to meet specified timing guarantees in all situations. This meaning contrasts with the more ambiguous use of real-time on the web, where it refers to servers pushing data to clients and performing stream processing without strict response time constraints (see Chapter 11).
```

- **Elements Required for Real-Time Guarantees in Systems:**
  - **RTOS (Real-Time Operating System):** Must guarantee CPU time for processes within specified intervals.
  - **Library Functions:** Should document worst-case execution times.
  - **Dynamic Memory Allocation:** May be restricted or disallowed.
  - **Software Stack:** Must be designed and tested to meet specified time guarantees.

- **Costs and Limitations of Real-Time Systems:**
  - Developing real-time systems is costly and primarily used in safety-critical embedded devices.
  - "Real-time" is not synonymous with "high performance" and may have low throughput.

Most server-side data processing systems must tolerate pauses and clock instability that occur in non-real-time environments because real-time guarantees are not economical or appropriate.

#### Limiting the Impact of Garbage Collection

It is possible to mitigate the negative effects of process pauses without using expensive real-time scheduling guarantees.

- **Methods to Reduce GC Pauses:**
  - **Consider as Planned Downtime:** Treat GC pauses as short planned downtimes for the node, allowing other nodes to handle client requests.
  - **Runtime Warnings:** The runtime can warn the application that a node will soon require a GC pause. The application can avoid sending new requests to that node and perform GC when not busy. This approach hides GC pauses from clients and reduces high percentiles of response times.
  - **Use Only for Short-Lived Objects:** Use garbage collectors only for short-lived objects and periodically restart processes before long-lived objects require full GC.

- **Node Restart:** Restart one node at a time, and shift traffic away from the node before the planned restart. This is similar to rolling upgrades.

These measures cannot completely prevent garbage collection pauses, but they can usefully reduce the impact on applications.


## Knowledge, Truth, and Lies

In a single computer, the same task always returns the same result deterministically, but in a **distributed system**, you have to endure uncertain networks and partial failures, clocks are unreliable, and processes may pause.

These issues can make **distributed systems** confusing. A network node cannot be certain of anything and can only make guesses based on received messages. To know the status of a remote node, messages must be exchanged, and if there is no response, you cannot distinguish between network and node issues.

- **System Model:** A system model is used to explicitly state the assumptions about the behavior of a distributed system, and the real system can be designed to meet those assumptions. An algorithm can be proven to work correctly within a specific system model.

- **Software Design:** Achieving correct behavior in an unreliable system model is not easy. It is important to understand the concepts of knowledge and truth in distributed systems and consider assumptions and guarantees.

### The Truth Is Defined by the Majority

Imagine a network with asymmetric errors. A node can receive all messages but may lose or delay messages sent from it. The node operates perfectly, but other nodes cannot hear its responses.

- **Error Scenario:** If a node experiences a long stop-the-world **garbage collection (GC)** pause, other nodes might declare the node dead after waiting. After GC completes, the node might continue to operate as if nothing happened.

- **Reliability of Self-Assessment:** A node cannot always trust its assessment of the situation. A distributed system cannot rely on a single node, as any node may fail at any time.

- **Quorum:** Many distributed algorithms rely on a **quorum** to make decisions. Decisions are made through the voting of multiple nodes, reducing dependency on any specific node.

- **Quorum Decision:** If a **quorum** of nodes declares another node dead, it must be considered dead. Individual nodes must follow the **quorum** decision and resign.

- **Absolute Majority Quorum:** Generally, a **quorum** consists of more than half the nodes, forming an absolute majority. Using a majority **quorum** allows the system to continue operating even if individual nodes fail.

- **Safety:** Using a majority **quorum** ensures there cannot be multiple conflicting decisions made simultaneously by different majorities in the system. The use of quorums is discussed further in Chapter 9's **consensus algorithms**.

#### The Leader and the Lock

In a system, there are often situations where only one of something should exist. For example:

- **The leader of a database partition** should be singular to prevent **split brain** scenarios.
- A **lock** for a particular resource or object should only be held by one transaction or client at a time to prevent concurrent writes.
- A username should be unique for user identification, so only one user should be allowed to register a specific username.

When implementing such requirements in a **distributed system**, caution is needed.

- Even if a node believes it is the "chosen one," there is no guarantee that a **quorum** of nodes agrees.
- A node may have been a leader previously, but if declared dead by other nodes, a different leader might have already been elected.

If a node continues to act as the "chosen one," problems can arise if the system is not carefully designed. Such nodes may send messages to other nodes in a self-appointed capacity, and if other nodes believe them, the entire system may behave incorrectly.

**Example:** Data corruption due to incorrect **lock** implementation

- If multiple clients write to a file simultaneously, the file can become corrupted.
- To prevent this, clients should be required to obtain a **lease** from a **lock service** before accessing the file.

![Figure 8-4](https://i.imgur.com/DPVPPqh.png)

*Figure 8-4. Incorrect distributed **lock** implementation: Client 1 believes its **lease** is still valid, but it has expired, resulting in file corruption in the storage.*

- **Problem:** If a client holding a **lease** pauses for too long, the **lease** expires. Another client can then obtain a **lease** for the same file and begin writing to it. When the paused client resumes, it may believe it still has a valid **lease** and start writing to the file. As a result, the writes collide, corrupting the file.

#### Fencing Tokens

When using a **lock** or **lease** to protect access to a resource, it is crucial to prevent a node from disrupting the system based on a mistaken belief of being the "chosen one." A relatively simple technique called **fencing tokens** can be employed.

![Figure 8-5](https://i.imgur.com/0dqo8WQ.png)

*Figure 8-5. Safely accessing storage by allowing writes only in the order of increasing **fencing tokens**.*

- **Fencing Tokens:** Each time a **lock service** grants a **lock** or **lease**, it returns a number called a **fencing token**. This number increases with each granted **lock**. Clients must include the **fencing token** in every write request to the storage service.

- **Example:** Client 1 obtains a **lease** with a **token** of 33 but experiences a long pause, causing the **lease** to expire. Client 2 obtains a **lease** with a **token** of 34 and sends a write request to the storage service with the **token** 34. When Client 1 reactivates and sends a write with **token** 33, the storage server remembers having processed a write with a higher **token** (34) and rejects the request with **token** 33.

- **Implementation:** If ZooKeeper is used as a **lock service**, transaction IDs `zxid` or node versions `cversion` can serve as **fencing tokens**. These are guaranteed to increase monotonically.

- **Server-side Verification:** The resource itself should verify the **token** and reject writes with a lower **token** than previously processed. Clients self-verifying their **lock** status is insufficient. If a resource does not explicitly support **fencing tokens**, workarounds may be possible (e.g., in a file storage, include the **fencing token** in the filename).

Server-side verification of **tokens** is a way to protect against clients that misbehave, rather than relying on the assumption that clients will always behave correctly.

### Byzantine Faults

**Fencing tokens** can detect and block nodes from causing disruptions even if they act incorrectly (e.g., unaware of an expired **lease**). However, if nodes actively attempt to undermine the system's guarantees, they could easily send messages with fake **fencing tokens**.

This book assumes nodes are unreliable but honest: they may respond slowly or not at all, and their state may be outdated, but they attempt to adhere to the protocol's rules when responding.

#### The Byzantine Generals Problem

Distributed system problems become significantly harder when nodes can "lie." Such behavior is known as a **Byzantine fault**, and reaching consensus in an environment with distrust is known as the **Byzantine Generals Problem**.

- **Byzantine Generals Problem:** Imagine several generals communicating via messages and needing to agree. Some generals may be traitors, sending false messages or causing confusion. It is not known in advance who the traitors are.

A system is **Byzantine fault-tolerant** if it can operate correctly even when some nodes misbehave, not following the protocol, or when malicious attackers interfere with the network. These concerns are relevant in certain situations. For example:

- In aerospace environments, radiation may corrupt data in a computer's memory or CPU registers, causing them to respond unpredictably. Flight control systems must withstand such errors.
- In systems with multiple participating organizations, some participants may attempt to deceive or defraud others. In such scenarios, nodes cannot simply trust messages from other nodes, as they could be sent with malicious intent. For instance, **Bitcoin** and other **peer-to-peer networks** can be seen as a way to allow parties that do not trust each other to agree on whether transactions have occurred without a central authority.

However, most server-side data systems can assume there are no Byzantine faults. In data centers, all nodes are controlled by the organization, and radiation levels are low enough that memory corruption is not a major concern. Protocols that make systems **Byzantine fault-tolerant** are quite complex and impractical for most server-side data systems.

#### Weak Forms of Lying

While nodes can generally be assumed to be honest, it might be worthwhile to incorporate mechanisms in the software to handle weak forms of "lying." For instance, hardware issues, software bugs, and misconfigurations can cause incorrect messages. These protective mechanisms may not provide full Byzantine fault tolerance but are simple and practical steps toward better reliability. For example:

- **Network Packets**: Due to hardware issues or bugs in the OS, drivers, routers, etc., network packets may occasionally be corrupted. While TCP and UDP have built-in checksums to detect corruption, they might not catch everything. A simple checksum at the application level can provide protection against such corruption.
- **Publicly Accessible Applications**: Applications that are publicly accessible should carefully sanitize user input. For example, ensure values are within reasonable ranges and limit string sizes to prevent denial-of-service through large memory allocations. Internal services behind firewalls can omit strict input checks, but basic validation (e.g., in protocol parsing) is a good idea.
- **NTP Clients**: NTP clients can be configured with multiple server addresses. When synchronizing, the client contacts all servers to estimate errors and checks if most servers agree on a specific time range. If most servers are reasonable, any mis

configured NTP server reporting incorrect time is detected as an outlier and excluded from synchronization. Using multiple servers makes NTP more robust than relying on a single server.

These defense mechanisms enhance system reliability, making them more resilient to unexpected errors in a distributed environment.

### System Model and Reality

Several algorithms have been designed to solve distributed system problems. For example, Chapter 9 reviews solutions to the **consensus problem**. For these algorithms to be useful, they must endure the various distributed system faults we discussed.

Algorithms should be written so that they do not rely too heavily on the details of the hardware and software configurations on which they are executed. To this end, a **system model** is defined that describes the situations the algorithm can assume. This is an abstraction that formalizes the algorithm's assumptions.

Regarding timing assumptions, three **system models** are commonly used:

##### Synchronous Model

The **synchronous model** assumes network delays, process pauses, and clock errors are bounded. This does not mean that clocks are perfectly synchronized or network delays are zero. Rather, it means knowing that network delays, pauses, and clock drift will not exceed a certain upper bound [88]. The **synchronous model** is not a realistic model of most real systems, as (as discussed in this chapter) unbounded delays and pauses can occur.

##### Partially Synchronous Model

Partial synchronization assumes the system behaves like a synchronous system most of the time, but sometimes network delays, process pauses, and clock drift exceed their bounds [88]. This is a realistic model of many systems: most of the time, the network and processes work very well. Otherwise, we couldn't get anything done. However, we must acknowledge that all timing assumptions can occasionally break, in which case network delays, pauses, and clock errors can become unbounded.

##### Asynchronous Model

In this model, algorithms cannot make any timing assumptions. In fact, there are no clocks (and thus no timeouts) in this model. Some algorithms can be designed for the **asynchronous model**, but they are very limited.

Additionally, we need to consider node failures. The most common **system models** for nodes are:

##### Crash-Stop Faults

In the **crash-stop model**, algorithms assume that nodes can only fail by crashing. This means that nodes can suddenly stop responding at any time, and they are assumed to disappear forever afterward. In other words, they never come back.

##### Crash-Recovery Faults

Nodes can crash at any time and start responding again after some unknown time. The **crash-recovery model** assumes that nodes have **stable storage** that withstands crashes (i.e., non-volatile disk storage) and that the state in memory is assumed to be lost.

##### Byzantine (Arbitrary) Faults

Nodes can behave completely randomly and may try to deceive other nodes, as described earlier.

When modeling real systems, the **partially synchronous model** and **crash-recovery faults** are generally the most useful models. But how do distributed algorithms handle these models?

- **Synchronous Model** assumes network delays, process pauses, and clock errors are within bounds. This model is based on predictable environments but does not align with the actual circumstances of most distributed systems.

- **Partially Synchronous Model** assumes that most systems operate normally but may sometimes experience network delays or process pauses. This model acknowledges network variability and process delays, reflecting realistic scenarios.

- **Asynchronous Model** assumes no timing assumptions can be made, including the absence of clocks. This model challenges the design of universal algorithms that can operate under any conditions.

These various **system models** help understand and address the challenges distributed system design faces when combining with the complexities of real systems. The **partially synchronous model** and **crash-recovery faults** provide useful guidelines for distributed system design as they model the most commonly occurring situations in real environments.

### Correctness of an Algorithm

**Defining the Correctness of an Algorithm:**

The correctness of an algorithm can be defined by describing the properties the algorithm must satisfy. For example, in the case of a sorting algorithm, the two elements in the output list must have the property that the left element is always less than the right element. These properties ensure that the list is sorted.

**Properties of the Fencing Tokens Algorithm:**

1. **Uniqueness:**  
   No two fencing tokens can have the same value.

2. **Monotonic Sequence:**  
   If request x returns token \( t_x \) and request y returns token \( t_y \), and x completes before y, then \( t_x < t_y \).

3. **Availability:**  
   A non-crashed node requesting a fencing token should eventually receive a response.

An algorithm is correct in some system models if it always satisfies the properties in all situations within the model. However, if all nodes crash or network delays become infinite, no algorithm can do anything.

### Safety and Liveness

**Distinguishing Between Safety and Liveness:**

- **Safety Properties:**  
  When violated, they can be pointed out at a specific point in time. For example, if the uniqueness property is violated, the specific operation that returned the duplicate token can be identified. Once violated, safety properties cannot be undone.

- **Liveness Properties:**  
  They may not be satisfied at a particular time, but there is always the possibility of being satisfied in the future. For example, a request may not have received a response yet, but it will eventually receive one.

- **Examples:**  
  - **Safety:** Uniqueness and Monotonic Sequence.
  - **Liveness:** Availability. Often includes the word "eventually."

**Importance of Distinguishing Between Safety and Liveness:**

When dealing with difficult system models, distinguishing between Safety and Liveness properties is essential. In distributed algorithms, Safety Properties must be maintained in all situations. However, Liveness Properties can only be satisfied under certain conditions.

### Mapping System Models to the Real World

**Mapping System Models to the Real World:**

Safety and Liveness properties and System Models are useful for inferring the correctness of distributed algorithms. However, in practical implementation, real-world complexities may become problematic due to the simplification provided by system models.

For example, in the **Crash-Recovery Model**, it is assumed that data is stored in stable storage, enduring crashes. However, in reality, disk data may be corrupted, or hardware errors may occur.

In **Quorum Algorithms**, nodes must remember the data they stored. If nodes forget the data, the quorum condition may be violated, compromising the algorithm's correctness.

Theoretical descriptions can assume certain things will not happen. However, in practical implementation, it may be necessary to handle even impossible scenarios.

**System Models: A Simplification of Reality**

Theoretical and abstract system models are highly useful for systematically understanding and solving problems in real systems. We can prove the correctness of an algorithm by showing that its properties are always maintained within a system model.

Just because an algorithm is proven to be correct in theory does not mean it will always work correctly in a real system, but it is a good first step. Theoretical analysis can reveal issues in an algorithm, allowing us to identify problems that might have remained hidden for a long time in a real system. Both theoretical analysis and empirical testing are important.
