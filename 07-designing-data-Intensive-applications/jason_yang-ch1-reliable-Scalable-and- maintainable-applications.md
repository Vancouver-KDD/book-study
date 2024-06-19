## 1. Understanding Data-Intensive Applications:

### Data-Intensive vs. Compute-Intensive:
- Data-intensive applications primarily deal with large volumes of data, complex data structures, and rapidly changing data.
- The challenge is more about managing and processing this data efficiently rather than simply providing raw computing power.

### Common Building Blocks:
- **Databases:** Essential for storing and retrieving data efficiently.
- **Caches:** Help in speeding up data retrieval by storing results of expensive operations.
- **Search Indexes:** Facilitate efficient searching and filtering of data.
- **Stream Processing:** Manage asynchronous data processing and messaging.
- **Batch Processing:** Handle periodic processing of large datasets.



## 2. Principles of Reliable Systems:

Reliability: Ensuring the system continues to function correctly despite faults.
Let's break down the types of faults and fault tolerance strategies:
### Hardware Faults:
- These are typically random and uncorrelated, such as disk failures, power outages, or memory errors.
- Techniques like RAID configurations, redundant power supplies, and backup systems help mitigate these issues.
### Software Errors:
- Often systematic and correlated, including bugs, memory leaks, or cascading failures.
- Solutions involve rigorous testing, error handling, isolation, and fault injection practices (e.g., Netflix's Chaos Monkey).
### Human Errors:
- Commonly occur due to misconfigurations or operational mistakes.
- Mitigations include better UX design for admin interfaces, thorough documentation, automation, and sandbox environments for testing.
### Fault Tolerance
- Involves designing systems that continue to operate even when components fail.
- Techniques include replication, failover mechanisms, and designing for eventual consistency.


## 3. Principles of Scalable Systems:

Scalability: Strategies for maintaining good performance under increasing load. Let's delve into how to describe and measure load and performance:
### Describing Load:
- Identify key load parameters like requests per second, read/write ratios, or active users. For instance, Twitter handles millions of requests per second with specific load challenges like tweet fan-out.
### Performance Metrics:
- Use response time percentiles (e.g., p95, p99) rather than averages to understand user experience. High percentiles indicate how the system handles peak load and outliers.
### Handling Load:
- Scaling Up (Vertical Scaling): Adding more resources (CPU, RAM) to a single machine.
- Scaling Out (Horizontal Scaling): Distributing load across multiple machines, also known as a shared-nothing architecture.
### Approaches to Load Handling:
- Elastic Systems: Automatically adjust resources based on load.
- Manual Scaling: Human intervention to analyze and add resources as needed.



## 4. Principles of Maintainable Systems:

Maintainability: Ensuring systems are easy to manage, modify, and extend. Let's focus on the core aspects:

### Operability:
- Make operations straightforward for teams managing the system.
- **Monitoring:** Implement robust monitoring to provide visibility into system health.
- Automation: Support automation for deployment, scaling, and recovery tasks.
- Documentation: Maintain clear and comprehensive documentation.
### Simplicity:
- Reduce complexity to make systems easier to understand and modify.
- Abstraction: Use abstractions to hide complex implementation details.
- Modularity: Design systems with well-defined, reusable components.
### Evolvability:
- Make it easy to adapt the system to new requirements.
- Agile Practices: Incorporate Agile methodologies like TDD and continuous refactoring.
- Modifiability: Ensure the system can be easily modified to accommodate new features or changes in business requirements.

## Detailed Case Study: Twitter's Scalability Challenges

### Initial Approach:
- Single Global Collection: All tweets stored in a global collection. Home timeline queries merged tweets from followed users in real-time.
- Challenges: Struggled with high read load due to fan-out. Home timeline queries were slow and resource-intensive.
### Revised Approach:
- Precomputed Timelines: Tweets precomputed and stored in user-specific caches.
- Challenges: Increased write load due to high fan-out during tweet posting.
### Hybrid Approach:
- Combination of Both: Regular users' tweets precomputed, while high-fan-out users (celebrities) handled separately with real-time merging during read.
- Benefits: Balanced load, provided consistent performance, and managed both read and write operations efficiently.