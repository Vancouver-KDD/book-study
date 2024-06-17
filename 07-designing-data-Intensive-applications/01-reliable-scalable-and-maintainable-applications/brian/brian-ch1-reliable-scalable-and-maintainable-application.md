# Chapter 1: Reliable Scalable And Maintainable Application

## A Data-intensive applications need to:
- **Database**: Store data so that they, or another application, can find it again later
- **Caches**: Remember the result of an expensive operation, to speed up reads
- **Search indexes**: Allow users to search data by keyword or filter it in various ways
- **Stream processing**: Send a message to another process, to be handled asynchronously
- **Batch processing**: Periodically crunch a large amount of accumulated data

## Thinking about data systems
- Why should we lump them all together under an umbrella term like data systems?
  - Many new tools for data storage and processing have emerged in recent years. They are optimized for a variety of different use cases, and they no longer neatly fit into traditional categories

![ExampleArchitectureforDataSystem.png](ExampleArchitectureforDataSystem.png)

## Three concerns that are important in most software systems:
1. Reliability: The system should continue to work correctly (performing the correct function at the desired level of performance) even in the face of adversity (hardware or software faults, and even human error)
2. Scalability: As the system grows (in data volume, traffic volume, or complexity), there should be reasonable ways of dealing with that growth.
3. Maintainability: Over time, many different people will work on the system (engineering and operations, both maintaining current behavior and adapting the system to new use cases), and they should all be able to work on it productively.

## Reliability -> "continuing to work correctly, even when things go wrong."
- Typical expectations include:
1. The application performs the function that the user expected. 
2. It can tolerate the user making mistakes or using the software in unexpected ways.
3. Its performance is good enough for the required use case, under the expected load and data volume.
4. The system prevents any unauthorized access and abuse.

- Fault VS Failure
  - Fault: one component of the system deviating from its spec
  - Failure: a whole stops providing the required service to the user

> It is impossible to reduce the probability of a fault to zero; therefore it is usually best to design fault-tolerance mechanisms that prevent faults from causing failures.

- Prevention VS Cure 
  - Prevention -> However, However, this book mostly deals with the kinds of faults that can be cured.

### Hardware Faults
- Hard disks are reported as having a mean time to failure (MTTF) of about 10 to 50 years.
- Thus, on a storage cluster with 10,000 disks, we should expect on average one disk to die per day.
- Our first response is usually to add redundancy to the individual hardware components.
- However, as data volumes and applications’ computing demands have increased, more applications have begun using larger numbers of machines, which proportionally increases the rate of hardware faults.
- Hence there is a move toward systems that can tolerate the loss of entire machines, by using software fault-tolerance techniques in preference or in addition to hardware redundancy.
- A system that can tolerate machine failure can be patched one node at a time, without downtime of the entire system

### Software Errors
- Hardware Faults has weak correlations between machines.
- Software Errors are correlated across nodes.
  - A software bug that causes every instance of an application server to crash when given a particular bad input.
  - A runaway process that uses up some shared resource—CPU time, memory, disk space, or network bandwidth.
  - A service that the system depends on that slows down, becomes unresponsive, or starts returning corrupted responses.
  - Cascading failures, where a small fault in one component triggers a fault in another component, which in turn triggers further faults
- There is no quick solution to the problem of systematic faults in software.
- Lots of small things can help:
  - allowing processes to crash and restart; measuring, monitoring, and analyzing system behavior in production.

### Human Errors
- How do we make our systems reliable, in spite of unreliable humans? The best systems combine several approaches:
  - Design systems in a way that minimizes opportunities for error.
  - Decouple the places where people make the most mistakes from the places where they can cause failures.
  - Test thoroughly at all levels, from unit tests to whole-system integration tests and manual tests.
  - Allow quick and easy recovery from human errors, to minimize the impact in the case of a failure.
  - Set up detailed and clear monitoring, such as performance metrics and error rates.
  - Implement good management practices and training—a complex and important aspect, and beyond the scope of this book.

> There are situations in which we may choose to sacrifice reliability in order to reduce development cost (e.g., when developing a prototype product for an unproven mar‐ ket) or operational cost (e.g., for a service with a very narrow profit margin)—but we should be very conscious of when we are cutting corners.

## Scalability
## Maintainability
