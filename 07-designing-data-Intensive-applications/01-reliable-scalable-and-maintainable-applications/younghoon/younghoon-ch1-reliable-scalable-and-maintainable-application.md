# Chapter 1: Reliable Scalable And Maintainable Application


## A data-intensive application is typically built from standard building blocks that provide commonly needed functionality. For example, many applications need to:
- Database: Store data so that they, or another application, can find it again later
- Caches: Remember the result of an expensive operation, to speed up reads
- Search indexes: Allow users to search data by keyword or filter it in various ways
- Stream processing: Send a message to another process, to be handled asynchronously
- Batch processing: Periodically crunch a large amount of accumulated data

## Three concerns that are important in most software systems:
- Reliability: The system should continue to work correctly (performing the correct function at the desired level of performance) even in the face of adversity (hardware or software faults, and even human error)
- Scalability: As the system grows (in data volume, traffic volume, or complexity), there should be reasonable ways of dealing with that growth.
- Maintainability: Over time, many different people will work on the system (engineering and operations, both maintaining current behavior and adapting the system to new use cases), and they should all be able to work on it productively.



## 1. Reliability 
- The application performs the function that the user expected. 
- It can tolerate the user making mistakes or using the software in unexpected ways. 
- Its performance is good enough for the required use case, under the expected load and data volume. 
- The system prevents any unauthorized access and abuse.

Note that a fault is not the same as a failure. A fault is usually defined as one component of the system deviating from its spec, whereas a failure is when the system as a whole stops providing the required service to the user. 

### Hardware Faults
the platforms are designed to prioritize flexibility and elasticityi over single-machine reliability. Hence there is a move toward systems that can tolerate the loss of entire machines, by using software fault-tolerance techniques in preference or in addition to hardware redundancy. 

### Software Errors
There is no quick solution to the problem of systematic faults in software. Lots of small things can help: carefully thinking about assumptions and interactions in the system; thorough testing; process isolation; allowing processes to crash and restart; measuring, monitoring, and analyzing system behavior in production.

### Human Errors
There is no quick solution to the problem of systematic faults in software. Lots of small things can help: carefully thinking about assumptions and interactions in the system; thorough testing; process isolation; allowing processes to crash and restart; measuring, monitoring, and analyzing system behavior in production.

- Design systems in a way that minimizes opportunities for error. 
Decouple the places where people make the most mistakes from the places where they can cause failures. 
Test thoroughly at all levels, from unit tests to whole-system integration tests and manual tests [3]. 
Allow quick and easy recovery from human errors, to minimize the impact in the case of a failure.
Set up detailed and clear monitoring, 
Implement good management practices and training—



## 2. Scalability
Scalability is the term we use to describe a systems ability to cope with increased load.

### Describing Performance
-When analyzing system performance under varying loads, two key questions arise:
- Performance Impact with Fixed Resources: What happens to system performance when load increases but resources (CPU, memory, network bandwidth, etc.) remain constant?

- Resource Scaling for Consistent Performance: How much do resources need to increase to maintain performance as load increases?

#### Latency vs. Response Time
-  Latency: The duration a request waits to be handled.
- Response Time: The total time from request to response, including processing, network delays, and queueing delays.

### Measuring Response Time
Response time should be viewed as a distribution rather than a single value due to variability. It can be affected by factors like background processes, network issues, or server-specific events.

### Mean Response Time: Often reported but not ideal as it doesn't represent typical user experience.
Median Response Time (p50): Half of the requests are faster and half are slower than this value, making it a better indicator of typical user experience.
Higher Percentiles (p95, p99, p999): Used to understand the impact of slowest requests on user experience. For example, the 95th percentile shows the time within which 95% of requests are completed.

## Tail Latencies and User Experience
High percentile response times (tail latencies) significantly impact user satisfaction, especially for critical users with more data or interactions (e.g., frequent Amazon customers). Optimizing these times can improve overall user experience and business outcomes (e.g., increased sales).

### Approaches for Coping with Load
People often talk of a dichotomy between scaling up (vertical scaling, moving to a more powerful machine) and scaling out (horizontal scaling, distributing the load across multiple smaller machines). Distributing load across multiple machines is also known as a shared-nothing architecture. A system that can run on a single machine is often simpler, but high-end machines can become very expensive, so very intensive workloads often cant avoid scaling out. In reality, good architectures usually involve a pragmatic mixture of approaches: for example, using several fairly powerful machines can still be simpler and cheaper than a large number of small virtual machines.



## 3. Maintainability
- Operability: Make it easy for operations teams to keep the system running smoothly.
- Simplicity: Make it easy for new engineers to understand the system, by removing as much complexity as possible from the system. (Note this is not the same as simplicity of the user interface.)
- Evolvability: Make it easy for engineers to make changes to the system in the future, adapting it for unanticipated use cases as requirements change. Also known as extensibility, modifiability, or plasticity.

### Operability: Making Life Easy for Operations
- Monitoring the health of the system and quickly restoring service if it goes into a bad state 
- Tracking down the cause of problems, such as system failures or degraded performance 
- Keeping software and platforms up to date, including security patches Keeping tabs on how different systems affect each other, so that a problematic change can be avoided before it causes damage
- Anticipating future problems and solving them before they occur (e.g., capacity planning) 
- Establishing good practices and tools for deployment, configuration management, and more 
- Performing complex maintenance tasks, such as moving an application from one platform to another 
- Maintaining the security of the system as configuration changes are made Defining processes that make operations predictable and help keep the production environment stable 
- Preserving the organizations knowledge about the system, even as individual people come and go

### Simplicity: Managing Complexity
One of the best tools we have for removing accidental complexity is abstraction. A good abstraction can hide a great deal of implementation detail behind a clean, simple-to-understand façade. 

However, finding good abstractions is very hard. In the field of distributed systems, although there are many good algorithms, it is much less clear how we should be packaging them into abstractions that help us keep the complexity of the system at a manageable level.

### Evolablity Making Change Easy
The ease with which you can modify a data system, and adapt it to changing requirements, is closely linked to its simplicity and its abstractions: simple and easy-tounderstand systems are usually easier to modify than complex ones. But since this is such an important idea, we will use a different word to refer to agility on a data system level: evolvability [34].