## Chapter 1 Reliable, scalable, and maintainable applications

### Why data systems?

1. Many new tools has emerged and the boundaries between the traditional categories are becoming blurred.
2. Many applications demand difficult requirements and often the work is broken down into tasks that a single tool can handle, and those different tool are managed by application code.

As complexity increased when talking about data system, we are going to focus on three `nonfunctional requirements`, reliability, scalability, maintainability.

### Reliability

- It means continuing to work correctly, even when things go wrong. (fault-tolerant or resilient in different words)
- Fault =/= Failure
- Fault: One component of the system deviating from its spec
- Failure: A system as a whole stops providing the required service to the user
- An example of fault-tolerance oriented system: Netflix Chaos Monkey
- Critical faults are desired to be prevented, not tolerated(e.g. security fault)

#### Type of faults

- Hardware faults
  - MTTF(mean time to failur): 10 to 50 years
  - Redundancy of hardware -> Too expensive as the demand increased.
  - Tolerate the loss of entire machines, by using software fault-tolerance techniques in preference or in addition to hardware redundancy.
- Software errors
  - Systematic errors within the system.
  - Carefully thinking about assumptions and interations in the system
  - Thorough testing
  - Process isolation
  - Allowing processes to crash and restart
  - Measuring, monitoring, and analyzing system behavior in production
- Human errors
  - Design systems minimizing opportunities for error.
  - Decouple possible failure spots.
  - Test thoroughly.
  - Allow quick and easy recovery from human errors.
  - Set up detailed and clear monitoring.
  - Implement good management practices and training.

### Scalability

- A system’s ability to cope with increased load

#### Describing load

- Twitter example: Joining tables(Expensive Read) vs Maintaining timeline table(Expensive Write)
- Hybrid model: Use read-on-the-fly model for accounts with huge number of followers.(celebrity accounts)

#### Describing performance

- Response time: The time between a client sending a request and receiving a response. It includes `network delays` and `queueing delays`
- Latency: The duration that a request is waiting to be handled.
- Distribution of values(e.g. percentiles, median, average) that you can measure - response time
- Tail latencies
- SLO(service level objective), SLA(service level agreement)
- Head-of-line blocking

#### Approaches for coping with load

- Scaling out (horizontal scaling) by distributing load across multiple smaller machines in a shared-nothing architecture, rather than scaling up (vertical scaling) to a more powerful single machine.
- Implementing elasticity to automatically add computing resources when detecting load increases, or manually scaling the system by analyzing capacity and adding more machines.
- Adopting architectures and patterns tailored to the specific application's load parameters (read/write volume, data size, response times, access patterns, etc.), as there is no one-size-fits-all scalable solution.

### Maintainability

- Fixing bugs, keeping its systems operational, investigating failures, adapting it to new platforms, modifying it for new use cases, repaying technical debt, and adding new features

#### Operability

- Make it easy for operations teams to keep the system running smoothly.

#### Simplicity

- Make it easy for new engineers to understand the system, by removing as much complexity as possible from the system.

#### Evolvability

- Make it easy for engineers to make changes to the system in the future, adapting it for unanticipated use cases as requirements change.
