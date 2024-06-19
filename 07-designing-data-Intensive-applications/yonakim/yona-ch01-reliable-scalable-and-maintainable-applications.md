# Chapter 1: Reliable, Scalable, and Maintainable Applications
Introduces: terminology & approach that we'll use throughout this book.

Many applications today are data-intensive, opposed to compute-intensive.
i.e. amount of data, complexity of data, speed at which data is changing > CPU power.

Data-intensive application consists of:
- database
- caches
- search indexes
- stream processing
- batch processing

Building a data-intensive application is not so simple:
- there are many dbs with different characteristics
- application has different requirements
- various approaches to caching
- multiple ways of building search indexes
- need to combine tools
- etc

Figuring out which tool to use and with which approaches is very important.

## Thinking About Data Systems

Why group all ^ these into "data systems"?
- Many tools for data storage and processing have emerged in recent years, and they don't fit into traditional categories:
    - Redis (datastores that are also used as message queues)
    - Apache Kafka (message queues with db-like durability guarantees)
- The boundaries between the categories are becoming blurred.

- Many applications these days have demanding or wide-ranging requirements that a single tool can no longer meet all its data processing and storage needs. 
    - The work is instead broken down into tasks that can be performed efficiently on a single tool, and these tools are stiched together using application code. 

- There are many factors that may influence the design of a data system, that depend on the situation:
    - skills and experience of the people involved
    - legacy system dependencies
    - time-scale for delivery
    - organization's tolerance of different kinds of risk,
    - regulatory constraints
    - etc

## Reliablility
"The system should continue to work correctly even in the face of adversity."

Everyone understands the basics of reliability. For software, it means:
- The application works as expected.
- It handles user mistakes and unexpected usage.
- Its performance meets requirements under expected load.
- It prevents unauthorized access and abuse.

If these conditions are met, the software is "working correctly." Reliability means the software continues to work correctly even when issues arise.

Issues, known as faults, can occur. Systems that handle faults are called fault-tolerant or resilient. It's not feasible to handle every possible fault, so we focus on specific types.

A fault is a component deviating from its specification, while a failure is the system failing to provide its service. Since faults can't be completely eliminated, we design mechanisms to prevent faults from causing failures.

Interestingly, it can help to deliberately trigger faults, like randomly killing processes, to test and improve fault-tolerance. Netflix's Chaos Monkey is an example.

While fault tolerance is preferred, prevention is crucial in cases like security. If a system is compromised, it can't be undone. However, this book focuses on faults that can be cured.

### Hardware Faults
When we think of system failures, hardware faults often come to mind. Common issues include:
- Hard disks crashing
- Faulty RAM
- Power blackouts
- Unplugged network cables

These problems are frequent in large data centers.
- Hard disks have a mean time to failure (MTTF) of 10 to 50 years. With 10,000 disks, expect one to fail each day.

To handle these issues, we add hardware redundancy:
- RAID setups for disks
- Dual power supplies and hot-swappable CPUs for servers
- Batteries and diesel generators for backup power

This setup doesn’t stop failures but helps systems keep running by using redundant components.

Previously, hardware redundancy was enough for most applications, making total machine failure rare and downtime manageable. Only high-availability applications needed multi-machine redundancy.

As data volumes and computing needs have grown, more applications use many machines, increasing fault rates. Cloud platforms like AWS often have virtual machines that can become unavailable without warning, prioritizing flexibility over single-machine reliability.

Now, there’s a shift towards systems that can handle the loss of entire machines using software fault-tolerance along with hardware redundancy. These systems offer benefits:
- Single-server systems need planned downtime for tasks like patching.
- Fault-tolerant systems can patch one node at a time without affecting the entire system.

### Software Errors
Hardware faults are usually random and independent, meaning one machine's failure doesn't imply another will fail. There can be weak correlations, like temperature, but simultaneous failures are unlikely.

Systematic errors are a different type of fault that are correlated across nodes and can cause widespread system failures. Examples include:
- A software bug causing all servers to crash with bad input (e.g., the June 30, 2012 leap second bug in Linux).
- A runaway process consuming shared resources like CPU time, memory, or network bandwidth.
- A critical service becoming slow, unresponsive, or returning corrupted responses.
- Cascading failures, where a fault in one component triggers failures in others.

These software faults often remain dormant until triggered by unusual conditions, revealing flawed assumptions in the software.

To address systematic faults (These measures help mitigate the impact of systematic faults):
- Think carefully about system assumptions and interactions.
- Conduct thorough testing.
- Isolate processes.
- Allow processes to crash and restart.
- Monitor and analyze system behavior in production.
- Implement self-checks for system guarantees (e.g., message queues verifying message counts).

### Human Errors
Humans design, build, and operate software systems, but humans are unreliable. A study found that configuration errors by operators were the leading cause of outages, while hardware faults caused only 10-25% of outages.

To make systems reliable despite human errors, the best systems use several approaches:
- Minimize opportunities for error: Design systems with clear abstractions, APIs, and admin interfaces that encourage correct usage and discourage mistakes. However, if too restrictive, people may bypass them.

- Decouple mistakes from failures: Provide non-production sandbox environments where people can experiment safely using real data without affecting users.

- Thorough testing: Conduct testing at all levels, including unit tests, integration tests, and manual tests. Automated testing is valuable for rare corner cases.

- Quick recovery: Enable fast rollback of configuration changes, gradual code rollout, and tools to recompute data if needed.

- Detailed monitoring: Set up performance metrics and error rates monitoring (telemetry) to detect early warning signals, check assumptions, and diagnose issues when problems occur.

- Good management and training: Essential practices, though beyond the scope of this summary.

### How Important Is Reliability?
Reliability is crucial not only for critical systems like nuclear power stations and air traffic control but also for everyday applications. Bugs in business apps can cause lost productivity and legal risks, while ecommerce outages can lead to revenue loss and reputational damage.

Even in "noncritical" apps, we have a responsibility to users. For example, if a parent stores all their photos and videos in your app, how would they feel if the database was corrupted? Would they know how to restore a backup?

Sometimes, we might reduce reliability to save on development or operational costs, like when creating a prototype for an unproven market or a low-margin service. However, we must be aware of the risks when cutting corners on reliability.

## Scalability
"As the system grows, there should be reasonable ways of dealing with that growth."

Even if a system works reliably today, it might not in the future. Increased load is a common reason for degradation, such as:
- Growing from 10,000 to 100,000 concurrent users
- Increasing from 1 million to 10 million users
- Processing much larger data volumes

Scalability describes a system’s ability to handle increased load. It’s not a simple label; you can’t just say “X is scalable” or “Y doesn’t scale.” Instead, consider questions like:
- How can we cope with system growth?
- How can we add resources to manage the additional load?

### Describing Load
To discuss system scalability, we first need to describe the current load using load parameters. These parameters depend on your system's architecture and could include:
- Requests per second to a web server
- Ratio of reads to writes in a database
- Number of active users in a chat room
- Hit rate on a cache

For example, Twitter handles two main operations:
- Post tweet: 4.6k requests/second on average, 12k at peak
- Home timeline: 300k requests/second

Twitter's challenge is managing the "fan-out" effect, where each user follows many people, and each user is followed by many people. There are two ways to handle this:
- Global collection: Post tweets to a global collection and query followers' tweets when a timeline is requested.
- Cached timelines: Maintain a cache for each user's home timeline and update it with new tweets as they are posted.

Initially, Twitter used the global collection approach but struggled with load. They switched to caching timelines, which improved performance since reads outnumber writes. However, posting a tweet now requires more work due to fan-out, with each tweet potentially needing millions of writes.

Twitter's current approach is a hybrid:
- Most tweets are fanned out to home timelines when posted.
- Tweets from users with many followers (like celebrities) are fetched and merged when timelines are read.

This hybrid method balances performance and scalability effectively.

### Describing Performance
To understand how a system performs under load, there are two key perspectives to consider:
- Impact of increased load on unchanged resources: When load parameters increase but system resources like CPU, memory, and network stay the same, how does system performance change?
- Resource scaling to maintain performance: When load increases, how much must resources be increased to maintain consistent performance levels?

Performance metrics are crucial for both scenarios, especially in different types of systems:
- Batch processing systems (e.g., Hadoop): Focus on throughput, measuring how many records processed per second or total job completion time.
- Online systems: Emphasize response time, the duration from request to response.

Response times vary due to various factors, not just the same request repeated:
- Response time distribution: Each request can have a slightly different response time due to factors like network delays, system processes, or data size.

- Percentiles: Instead of just averages, percentiles like median (p50) and higher (p95, p99, p999) show the range of response times experienced by users.

- Importance of high percentiles: High percentiles, known as tail latencies, impact user experience significantly:
    - For example, Amazon focuses on ensuring even the slowest 1 in 1,000 requests are fast to maintain customer satisfaction and sales.

- Service level agreements (SLAs): Contracts often define performance expectations with metrics like median response time and high percentiles to ensure service quality.

- Challenges with high percentiles: Optimizing response times at very high percentiles can be costly with diminishing returns due to unpredictable factors.

- Queueing delays: Queueing issues can significantly affect high percentile response times, causing delays due to limited server processing capacity.

- Client-side measurement: It’s crucial to measure response times from the client side to accurately assess user experience and system performance.

- Testing scalability: When testing system scalability with artificial load, ensure the load generator sends requests independently of response times to avoid skewing results with artificially short queues.

Understanding these factors helps in effectively managing and scaling systems to meet performance demands as load increases.

### Approaches for Coping with Load
- Understanding Scalability: After defining load parameters and performance metrics, the focus shifts to maintaining good performance as load increases.

- Architectural Adaptation: Systems designed for one load level may require rethinking as load grows exponentially.

- Scaling Strategies: There’s a debate between scaling up (more powerful machines) and scaling out (distributed across multiple smaller machines).

- Hybrid Approaches: Effective architectures often blend both scaling strategies for optimal cost and complexity management.

- Elastic vs. Manual Scaling: Systems can automatically add resources (elastic) or require manual intervention based on load predictions.
- Stateful Challenges: Transitioning from single-node to distributed setups for stateful data adds complexity but enhances scalability.

- Future Trends: Advancements in distributed systems may shift towards default adoption even for smaller-scale applications.

- Application Specificity: Scalable architectures are tailored to unique application needs rather than a one-size-fits-all solution.

- Engineering Considerations: Designing for scalability involves predicting common operations and anticipating future demands.

- Iterative Development: Early-stage startups prioritize rapid iteration over scaling for hypothetical future demands.
Building Blocks: Scalable architectures are constructed from versatile components and established patterns.

## Maintainability
"Over time, many different people will work on the system, and they should all be able to work on it productively."

- Cost of Software Maintenance: The bulk of software expenses arise not from initial development but ongoing upkeep, which includes bug fixes, system maintenance, adapting to new platforms, and adding features.

- Challenges with Legacy Systems: Many developers find maintaining legacy systems challenging due to fixing others’ mistakes, outdated platforms, or systems handling unintended tasks.

- Design Principles for Minimizing Maintenance Pain:
    - Operability: Ensure operations teams can smoothly run the system.
    - Simplicity: Reduce complexity so new engineers can easily grasp the system.
    - Evolvability: Facilitate future changes and adaptations to meet new requirements.

- Striving for Effective Design: To prevent creating future legacy software, prioritize designing systems with these principles in mind: operability, simplicity, and evolvability.

### Operability: Making Life Easy for Operations
Operations teams play a critical role in maintaining software systems, ensuring their smooth functioning and stability. Key responsibilities include monitoring system health, diagnosing issues, and keeping software up to date with security patches. They also anticipate and prevent future problems through capacity planning and establish reliable deployment and configuration management practices.

Operability, which refers to how well a system can be operated and maintained, is crucial for effective operations. It involves making routine tasks straightforward, allowing operations teams to focus on higher-value activities. Good operability includes:
- Monitoring and Recovery: Ensuring quick restoration of service and proactive monitoring to detect and resolve issues promptly.
- System Management: Updating and securing software, managing dependencies between systems, and planning for future needs.
- Deployment and Configuration: Establishing practices for seamless deployment and configuration management.
- Security and Stability: Maintaining system security during configuration changes and ensuring stable production environments.
- Documentation and Automation: Providing clear operational documentation, supporting automation, and integrating with standard tools.
- Adaptability and Control: Allowing for easy adjustments in system behavior while maintaining predictability and control over operational states.

In essence, while automation plays a crucial role in operations, it's ultimately the human expertise and proactive management that ensure reliable and efficient software system operations.

### Simplicity: Managing Complexity
- Complexity in Software: Small projects can be simple and expressive, but complexity grows with project size, leading to maintenance challenges and increased costs. This complexity is often referred to as a "big ball of mud" and manifests in various ways like tangled dependencies and inconsistent naming.

- Impact of Complexity: Complex software makes maintenance harder, leading to budget overruns and increased risk of bugs due to overlooked interactions. Simplifying software improves maintainability and should be a primary goal.

- Removing Accidental Complexity: Simplification involves removing accidental complexity that doesn't stem from the problem the software solves but from its implementation.

- Abstraction as a Solution: Abstraction hides implementation details behind a simple interface, facilitating reuse and improving software quality. Examples include high-level programming languages and SQL for databases.

- Challenges of Abstraction: Finding effective abstractions is difficult, especially in complex domains like distributed systems, where good packaging of algorithms into reusable components is crucial for managing complexity.

### Evolvability: Making Change Easy
- Changing Requirements: Systems rarely maintain static requirements; they evolve due to new facts, emerging use cases, business shifts, user demands, technological advancements, and regulatory changes.

- Agile Practices: Agile methodologies provide frameworks to adapt to these changes, emphasizing iterative development and responsiveness to customer needs.

- Technical Tools: Agile also promotes technical practices like test-driven development (TDD) and refactoring to manage change effectively at the code level.

- Scaling Agile to Data Systems: This book explores applying Agile principles beyond small-scale code changes to larger data systems composed of multiple applications or services.

- Evolvability: Refers to the system's ability to easily adapt and evolve with changing requirements, closely tied to simplicity and effective abstractions in the system's design. This concept is crucial for achieving agility at the level of data systems.
