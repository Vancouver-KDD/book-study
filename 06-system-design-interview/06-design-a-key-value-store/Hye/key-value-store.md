Chapter 6: Design a key-value store

* Key-value store; key-value database; non-relational database. Key must be unique, key-value pair can be strings, lists, objects, etc
* The size of the key-value pair is small. Ability to store big data, high availability, high scalability, automatic scaling, unable consistency, and low latency
* Single server key-value store
    * Store key-value pair in a hash table in-memory. Even though memory access is fast, fitting everything in memory is impossible
    * improvements:
        * Data compression
        * Store only frequently used data in memory and the rest on disk
    * Can still reach capacity quickly
* Distributed key-value store; distributed hash table - distributes key-value pairs across many servers.
    * CAP Theorem - always guarantees two of the threes:
    * Consistency - all clients see the same data at the same time
    * Availability - any client which requests data gets a response even if some of the nodes are down
    * Partition Tolerance - system continues to operate despite network partitions

* In real-world: partitions cannot be avoided.
* CP: We must block all write operations to avoid data inconsistency. (Banks raise error when there is inconsistency to the latest data)
* AP: accepting reads will return stale data, accepting writes the data will be synced when the network partition is resolved

System components:
* Data partition
* Data replication
* Consistency
* Inconsistency resolution
* Handling failures
* System architecture diagram
* Write path
* Read path

* Data partition - split the data into smaller partitions and store them in multiple servers.
    * Challenges:
    * Distribute data across multiple servers evenly
    * Minimize data movement when nodes are added or removed
    * Solutions: Consistent hashing
        * Pro
        * automatic scaling depending on the load
        * Heterogeneity - number of virtual nodes for a server is proportional to the server capacity
* Data replication
    * High availability and reliability
    * Replicas are placed in distinct data centres and data centres are connected through high-speed networks
* Consistency
    * Data replicated at multiple nodes must be synchronized across replicas
    * Example) ACKNOWLEDGEMENT:
    * Coordinator must receive at least one acknowledgement before the write operation is considered as successful
    * A coordinator acts as a proxy between the client and the nodes
* Inconsistency resolution: versioning
    * Replication gives high availability, but causes inconsistencies.
    * Solution: Versioning and vector locks (with increments)
    * Ex) D1([Sx, 1]), DE([Sx, 2]), D3([Sx, 2], [Sy, 1]) + D4([Sx, 2], [Sz, 1]), D5([Sx, 3], [Sy, 1], [Sz, 1])
    * Con: adds complexity to the client to implement conflict resolution logic. Vector clock could grow rapidly (solution is to add threshold for the length and invalidate oldest pairs) - inefficiency in reconciliation
* Handling Failures
    * Failure Detection - depends on at least two independent sources
    * decentralized failure detection methods like gossip protocol (multiple nodes send heartbeats and check last updated)
* Handling temporary failures
    * Need to ensure availability
    * Sloppy quorum: system choses the first W healthy servers for writes and first R healthy servers for reads on the hash ring (offline servers ignored)
    * When servers back online, changes will be pushed back to achieve data consistency (hinted handoff)
    * If replica is permanently unavailable? - anti-entropy protocol: compare each piece of data on replica and updating each replica to the newest version
    * Merkel tree used for inconsistency detection and minimizing the amount of data transferred. - traverse through root hashes -> down to child node until the same hash key is found for the server and check they have same data
* Handling data centre outage
    * Reasons: power outages, network outage, natural disaster, etc
    * Replicate data across multiple data centres
* Write path:
    * Write requests persist on a commit log file -> data saved in memory cache -> cache is flushed to SSTable on disk 
* Read path:
    * Checks if data is in memory -> check the bloom filter to figure out which SSTable might contain the key -> SSTables return the result of the data set -> result returned to client

* Summary
* Ability to store big data - User consistent hashing to spread the load across servers
* High availability reads - data replication and multi-data centre setup
* Highly available writes - versioning and conflict resolution with vector clocks
* Dataset partition / Incremental scalability / Heterogeneity - Consistent Hashing
* Tunable consistency - Quorum consensus
* Handling temporary failures - sloppy quorum and hinted handoff
* Handling permanent failures - Merkel tree
* Handling data centre outage - Cross-data centre replication
