Chatper 1 Reliable, Scalable, and Maintainable Applications

Functionalities for a data-intensive applications need:
* Database - stores data so different apps can have access to the data again
* Caches - Remembers the results of the expensive operations to speed up the reads
* Search Indexes - Allows users to search data by keyword or filter it in various ways
* Stream processing - Send a message to another process, to be handled asynchronously < ?
* Batch processing - Periodically crunch a large amount of accumulated data


Boundaries are blurred and wide-range of requirements to obtaindata processing and storage needs

- Redis (data stores used as message queues)
- Apache Kafka (message queues with database-like durability guarantees)
- Memcache (application managed caching layer)
- Elasticsearch/Solr (full-text search server separate from the main database)
  
Questions to ask when designing a data system or service/:
1. How do you ensure that the data remains correct and complete, even when things go wrong internally?
2. How do you provide consistently good performance to clients, even when aprts of your system are degraded?
3. How do you scale to handle an increase in load?
4. What does a good API for the service look like?
5. skills and experience of devs? legacy system dependencies, time scale for delivery, org's tolerance, regulatory constraints, etc?


3 Fundamentals - Reliability / Scalability / Maintainability


Reliability - Tolerating hardwware & Software faults and Human Error
   - The system should work correctly with the desired level of performance and prevents any unauthorized access and abuse.
   - You can deliberately induce faults (Ex. Netflix Chaos Monkey) to develop fault-tolerant systems. (faults are usually caused by poor error handling)

   * Hardware Faults
     * Cause: hard disks crach, faulty RAM, power blackout, network cable issues
     * Solution:
       * Before - Add redundancy to the individual hardware components to reduce failure rate of the system (Set up disks in RAID config, servers have dual power supplies and hot-swappable CPUs, data centers have batteries and diesel generators for backup power)
       * Now - Applications require more data volumes and computing demands and use larger numbers of machines. (increases hardware faults) - Needs software fault-tolerance technique (ex. rolling updates)
  
   * Software Errors
     * Cause: Systemic error within the system that are correlatee across nodes
       * For example:
         * Software bug with bad input
         * runaway process that uses up shared resources (CPU time, memory, disk space, or network bandwidth)
           * Runaway process - "Occasionally a process will stop responding to the system and run wild. These processes ignore their scheduling priority and insist on taking up 100% of the CPU. Because other processes can only get limited access to the CPU, the machine begins to run very slowly."
         * A depending system slows down/become unresponsive/starts returning corrupted responses
         * Cascading failures - sall fault in one component triggers a fault in another component, triggering further faults.
     * Solution: Software faults are often triggered by unusual set of circumstances
       * Think about assumptions and interactions in the system
       * thorough testing
       * process isolation
       * allow process to crash and restart
       * measuring
       * monitoring
       * analyze system behaviour in production
       * If system can provides ome guarantee (message queue that the number of incoming messages equals th enumber of outgoing messages - constantly check anraise alerts if a discrepanc is found)
  
   * Human Errors
     * Cause: ~ Unreliable Human ~
     * Solution:
       * Design systems that minimizes errors
           * well designed abstractions, APIs, admin interfaces to do the right thing
       * Decouple the places people make the most mistakes
         * rovide fully featured non-production sandbox environments where people can explore and experiment safely, using real data, without affecting real users
       * Test thoroughly at all levels
         * Unit tests, whole-system integration tests, manual tests, automated testing
       * Allow quick and easy recovery - fast roll back configuration, roll out code gradually, provide tools to recompute data
       * Set up detailed and clear monitoring, such as performance metrics and error rates (== Telemetry)
       * 


Scalability - Measuring load & performance, Latency percentiles, throughput
* Describes the ability to cope with increased load.
    "If a system grows in a particular way, what are our options for coping with the growth?"
* Describing load - load parameter
  * Based on system architecture..
    * requests per second to a web server, the ratio of reads to writes in a database, the number of simultaneously active users in a chat room, th ehit rate on a cache, etc
    * Twitter Example
      - Post tweet
      - Home timeline
      - Two methods:
        1. insert new tweet into a global collection of tweets - for huge influencers
        2. Maintain cache for each user's home timeline (mailbox of tweets) - for regular users
* Describing Performance
    - How much resource do you need to increase a load parameter to keep the performance consistent? etc
    - Variations in request time:
      - random additional latency could be introduced by a context switch to a background process
      - the loss of a network packet and TCP retransmission
      - a garbage collection pause
      - a page fault forcing a read from disk
      - mechanical vibrations in the server rack
* Use percentiles instead of average
  * avg is arithmetic, and does not tell how many users actually experienced that delay
  * Median (50% percentile) - half of the requests return in that performance
  * High percentiles (tail latency) - p95, p99, p999 (99.9%)
    * 99.9% percentile - even though only 1 in 1000 requests is affected, the customer usually has the most data == VIP. We want to keep VIP customers happy and ensure performance is fast enough for them.
    * Optimizing performance for 99.99th is too expensive
  * used in service level objectives (SLOs) and service level agreements (SLAs)


Maintainability - Operability, simplicity, evolvability
* fixing bugs, keeping its system operational, investigating failures, adapting it to new platforms, modifying for new use cases, repaying technical debt, adding new features, etc
* Operability
  - Make it easy for operations team to keep the system running smoothly
  - Provide good default behavior and self-healing, miinimize surprises, establish good practice and tools for deployment, etc
* Simplicity
  - Make it easy for new engineers to understand the system (different from simplicity of the user interface)
  - Design good abstraction that hides the implementation and remove accidental complexity
* Evolvability
  - Make it easy for engineers to make changes to the system in the future (extensibility, modifiab ility, or plasticity)
  - Agile techniques with test-driven development (TDD) and refactoring.
