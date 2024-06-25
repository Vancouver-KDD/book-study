## 1. Understanding Data-Intensive Applications:

### Data-Intensive
- Data-intensive applications primarily deal with large volumes of data, complex data structures, and rapidly changing data.
- The challenge is more about managing and processing this data efficiently rather than simply providing raw computing power.

#### Common Building Blocks:
- Databases: Essential for storing and retrieving data efficiently.
	- RDBMS: MySQL, PostgreSQL, Oracle Database, Microsoft SQL Server
	- NoSQL: MongoDB, Cassandra, Redis, DynamoDB
- Caches: Help in speeding up data retrieval by storing results of expensive operations.
	- In-memory Caches: Redis, Memcached
	- CDNs (Content Delivery Networks): Cloudflare, Akamai, Amazon CloudFront (for caching web content)
- Search Indexes: Facilitate efficient searching and filtering of data.
	- Search Engines: Elasticsearch, Apache Solr, Amazon CloudSearch
- Stream Processing: Manage asynchronous data processing and messaging.
	- Stream Processing Frameworks: Apache Kafka, Apache Flink, Apache Storm, Amazon Kinesis
- Batch Processing: Handle periodic processing of large datasets.
	- Batch Processing Frameworks: Apache Hadoop, Apache Spark, AWS Batch, Google Cloud Dataflow

#### Stream Processing vs. Batch Processing:
- Stream Processing Frameworks focus on processing data streams in real-time to provide immediate responses,
- while Batch Processing Frameworks focus on processing large volumes of data in batches at regular intervals, efficiently analyzing large-scale data.


### Compute-Intensive
- Compute-intensive applications primarily require significant computational resources to perform complex calculations, simulations, or data processing tasks.
- The challenge is more about providing sufficient raw computing power and optimizing algorithms to handle these computationally demanding tasks efficiently.


## 2. Principles of Reliable Systems:

### Reliability
- Definition: The ability of a system to operate correctly without errors.
- Focus: Evaluates whether the system can continuously operate accurately over a given period.
- Key Aspects: Data integrity, system failure prevention, and recovery capability in case of errors.
- Measurement:
	- Usually measured using metrics like <mark style="background: #BBFABBA6;">MTTF (Mean Time To Failure)</mark>.
	- To test 100 hard drives of the same model, you measure the time it takes for each one to fail. Then, you add up all those times and divide by 100 to get the average.
- Example:
	- The ability of a database to process data without any errors for 24 hours.

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


### Availability
### Availability
- Definition: The ability of a system to be ready for use.
- Focus: Evaluates whether the system is always accessible and operational.
- Key Aspects: System uptime, minimizing downtime, and high-availability design.
- Measurement:
	- Usually measured using metrics like <mark style="background: #BBFABBA6;">uptime percentage</mark>.
	- A system with 99% uptime means it was only down for about 3 days and 15 hours in a year.
- Example:
	- The ability of a web server to be accessible 99.9% of the time, allowing users to connect at any time.


### Reliability vs. Availability
- Perspective: Reliability focuses on the system's ability to operate correctly, while availability focuses on keeping the system accessible at all times.
- Relation: High reliability can lead to high availability, but not necessarily. For example, a system can be highly reliable but have low availability due to frequent maintenance or updates.
- Objective: Reliability emphasizes system accuracy and error prevention, while availability emphasizes continuous access to the system.
- High reliability system: A financial database with multiple redundancy checks and backup systems to prevent data corruption.
- High availability system: A web service designed with failover mechanisms and multiple servers to ensure constant access.


## 3. Principles of Scalable Systems:

Scalability: Strategies for maintaining good performance under increasing load. Let's delve into how to describe and measure load and performance:
### Describing Load:
- Identify key load parameters like requests per second, read/write ratios, or active users.
- For instance, Twitter handles millions of requests per second with specific load challenges like tweet fan-out.
### Performance Metrics:
- Use response time <mark style="background: #BBFABBA6;">percentiles</mark> (e.g., p95, p99) rather than averages to understand user experience.
- High percentiles indicate how the system handles peak load and outliers.
- In an SLA between a service provider and a customer, the 90th percentile can be used to set *response time expectations*. For example, an SLA stating "90th percentile response time of 2 seconds or less" means that 90% of all requests must be processed within 2 seconds.
### Handling Load:
- Scaling Up (Vertical Scaling): Adding more resources (CPU, RAM) to a single machine.
- Scaling Out (Horizontal Scaling): Distributing load across multiple machines, also known as a shared-nothing architecture.
### Approaches to Load Handling:
- Elastic Systems: Automatically adjust resources based on load.
- Manual Scaling: Human intervention to analyze and add resources as needed.

### Percentiles vs. Percentage
- Percentiles: A measure indicating the position within a data set. For example, the 90th percentile means that 90% of the data is below this value, excluding the top 10%.
- Percentage: A ratio expressed as a fraction of 100. For example, 25% of the total means one quarter of the whole.


## 4. Principles of Maintainable Systems:

Maintainability: Ensuring systems are easy to manage, modify, and extend. Let's focus on the core aspects:

### Operability:
- Make operations straightforward for teams managing the system.
- Monitoring: Implement robust monitoring to provide visibility into system health.
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


## Case Study: Twitter's Scalability Challenges

### Initial Approach:
- Single Global Collection: All tweets stored in a global collection. Home timeline queries merged tweets from followed users in real-time.
- Challenges: Struggled with high read load due to fan-out. Home timeline queries were slow and resource-intensive.
### Revised Approach:
- Precomputed Timelines: Tweets precomputed and stored in user-specific caches.
- Challenges: Increased write load due to high fan-out during tweet posting.
### Hybrid Approach:
- Combination of Both: Regular users' tweets precomputed, while high-fan-out users (celebrities) handled separately with real-time merging during read.
- Benefits: Balanced load, provided consistent performance, and managed both read and write operations efficiently.