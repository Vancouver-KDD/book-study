### Thinking About Data Systems

- How do you ensure that the data remains correct and complete, even when things go wrong internally? 
- How do you provide consistently good performance to clients, even when parts of your system are degraded? 
- How do you scale to handle an increase in load?
- What does a good API for the service look like?

## Reliability
- The application performs the function that the user expected.
* It can tolerate the user making mistakes or using the software in unexpected ways.
* Its performance is good enough for the required use case, under the expected
load and data volume.
* The system prevents any unauthorized access and abuse.

- Systems that anticipate faults and can cope with them are called fault-tolerant or resilient. 

- a fault is not the same as a failure. A fault is usually defined as one component of the system deviating from its spec, whereas a failure is when the system as a whole stops providing the required service to the user. ... it is usually best to design fault-tolerance mechanisms that prevent faults from causing failures.

- Testing: it can make sense to increase the rate of faults by triggering them deliberately—
- Although we generally prefer tolerating faults over preventing faults, there are cases where prevention is better than cure (e.g., because no cure exists). This is the case with security matters.

**1) Hardware Errors**

However, as data volumes and applications’ computing demands have increased, more applications have begun using larger numbers of machines, which proportionally increases the rate of hardware faults. Moreover, in some cloud platforms such as Amazon Web Services (AWS) it is fairly common for virtual machine instances to become unavailable without warning [7], as the platforms are designed to prioritize flexibility and elasticity over single-machine reliability.
Hence there is a move toward systems that can tolerate the loss of entire machines, by using software fault-tolerance techniques in preference or in addition to hardware redundancy. Such systems also have operational advantages: a single-server system requires planned downtime if you need to reboot the machine (to apply operating system security patches, for example), whereas a system that can tolerate machine failure can be patched one node at a time, without downtime of the entire system

**2) Software Errors**

There is no quick solution to the problem of systematic faults in software. Lots of small things can help: 
- carefully thinking about assumptions and interactions in the system; thorough testing; process isolation; allowing processes to crash and restart; measuring, monitoring, and analyzing system behavior in production. 
- If a system is expected to provide some guarantee (for example, in a message queue, that the number of incoming messages equals the number of outgoing messages), it can constantly check itself while it is running and raise an alert if a discrepancy is found

**3) Human Errors**

How to prevent?
- Design systems minimizing opportunities for error - well-designed abstractions, APIs, and admin interfaces with a good balance not too restrictive
- Decouple the places where people make the most mistakes from the places where they can cause failures. - Sandbox env
- Through testing
- easy recovery - rollback, gradual rollout
- Detailed & clear monitoring

## Scalability
System’s ability to cope with increased load. Even if a system is working reliably today, that doesn’t mean it will necessarily work reliably in the future. One common reason for degradation is increased load

**Describing loads**

Twitter problem: 
1. Posting a tweet inserts the new tweet into a global collection. When a user requests home timeline, look up all people they follow and merge them
2. Maintain a cache for each user’s home timeline. When a user posts a tweet, look up all follwers, and insert the new tweet into each of their home timeline caches. The request to read the home timeline is then cheap, because its result has been computed ahead of time.

Approach 2 has issue for user with larger follower number. a single tweet may result in over 30 million writes to home timelines! Doing this in a timely manner—Twitter tries to deliver tweets to followers within five seconds—is a significant challenge... a single tweet may result in over 30 million writes to home timelines! 

- hybrid: 
Most users’ tweets continue to be fanned out to home timelines at the time when they are posted, but a small number of users with a very large number of followers (i.e., celebrities) are excepted from this fan-out. Tweets from any celebrities that a user may follow are fetched separately and merged with that user’s home timeline when it is read, like in approach 1.

**Describing performance**

what happens if the load increases. 
- throughput—the number of records we can process per second
- the total time it takes to run a job on a dataset of a certain size.
- In online systems, what’s usually more important is the service’s response time: response time not as a single number, but as a distribution of values that you can measure.

- Amazon example: For example, if the 95th percentile response time is 1.5 seconds, that means 95 out of 100 requests take less than 1.5 seconds, and 5 out of 100 requests take 1.5 seconds or more. 
High percentiles of response times, also known as tail latencies, are important because they directly affect users’ experience of the service. For example, Amazon describes response time requirements for internal services in terms of the 99.9th percentile, even though it only affects 1 in 1,000 requests. TThis is because the customers with the slowest requests are often those who have the most data on their accounts because they have made many purchases.

On the other hand, optimizing the 99.99th percentile (the slowest 1 in 10,000 requests) was deemed too expensive and to not yield enough benefit for Amazon’s purposes. Reducing response times at very high percentiles is difficult because they are easily affected by random events outside of your control.

- Queueing delays often account for a large part of the response time at high percentiles. As a server can only process a small number of things in parallel. example, by its number of CPU cores), it only takes a small number of slow requests to hold up the processing of subsequent requests—head-of-line blocking. Even if those subsequent requests are fast to process on the server, the client will see a slow overall response time due to the time waiting for the prior request to complete. Due to this effect, it is important to measure response times on the client side.

- The end-user request still needs to wait for the slowest of the parallel calls to complete. It takes just one slow call to make the entire end-user request slow. Even if only a small percentage of backend calls are slow, the chance of getting a slow call increases if an end-user request requires multiple back‐end calls, and so a higher proportion of end-user requests end up being slow (an effect known as tail latency amplification)

**Approaches for Coping with Load**

how do we maintain good performance even when our load parameters increase by some amount?

- scaling up (vertical scaling, moving to a more powerful machine) and scaling out (horizontal scaling, distributing the load across multiple smaller machines). Distributing load across multiple machines is also known as a shared-nothing architecture.
- so very intensive workloads often can’t avoid scaling out. In reality, good architectures usually involve a pragmatic mixture of approaches: for example, using several fairly powerful machines can still be simpler and cheaper than a large number of small virtual machines.
Some systems are elastic, meaning that they can automatically add computing resources when they detect a load increase, whereas other systems are scaled manually (
- taking stateful data systems from a single node to a distributed setup can introduce a lot of additional complexity. For this reason, common wisdom until recently was to keep your database on a single node (scale up)
- The architecture of systems that operate at large scale is usually highly specific to the application—there is no such thing as a generic, one-size-fits-all scalable architecture (informally known as magic scaling sauce)


## Maintainability

**Operability**

Make it easy for operations teams to keep the system running smoothly.
“good operations can often work around the limitations of bad (or incomplete) software, but good software cannot run reliably with bad operations”

**Simplicity**

managing complexity. Make it easy for new engineers to understand the system, by removing as much complexity as possible from the system. 
- symptoms of complexity: explosion of the state space, tight coupling of modules, tangled dependencies, inconsistent naming and terminology, hacks aimed at solving performance problems, special-casing to work around issues elsewhere
- when the system is harder for developers to understand and reason about, hidden assumptions, unintended consequences, and unexpected interactions are more easily overlooked.
- One of the best tools we have for removing accidental complexity is abstraction.- For example, high-level programming languages are abstractions that hide machine code, CPU registers, and syscalls. SQL is an abstraction that hides complex on-disk and in-memory data structures, concurrent requests from other clients, and inconsistencies after crashes.

**Evolvability**

Making changes easy. Make it easy for engineers to make changes to the system in the future, adapting it for unanticipated use cases as requirements change.
