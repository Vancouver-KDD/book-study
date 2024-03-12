# Chapter 6 : Distributed K-V store

It’s easy to establish a key-value store on a single machine. How can we expand this key-value store into a distributed system?

## Requirement
- the size of K-V pair < 10KB
- Able to store huge amount of data
- High availability
- High scsalability
- Auto-scaling
- Low latency
- Tunable consistency 


## Key concepts :  See the book. 
1. Data Partitioning
2. Data Replication
3. Tunable consistency
4. Resolving inconsistency : Implementing Versioning using Vector clock
5. Handling failures
    
    5.1 : Detection

    5.2 : Temporary failure : Hinted handoff 

    5.3 : Permanent failure : How to reduce amount of data-sync for failed nodes? 

    5.4 : Data-center level replication : Replication across multiple data centers : (AZ in AWS)

### The unhelpful CAP theroem
Quote from DDIA Chapter 9:
```
CAP is sometimes presented as Consistency, Availability, Partition tolerance: pick 2 out of 3. Unfortunately, putting it this way is misleading [32] because network partitions are a kind of fault, so they aren’t something about which you have a choice: they will happen whether you like it or not [38].

At times when the network is working correctly, a system can provide both consistency (linearizability) and total availability. When a network fault occurs, you have to choose between either linearizability or total availability. Thus, a better way of phrasing CAP would be either Consistent or Available when Partitioned [39]. A more reliable network needs to make this choice less often, but at some point the choice is inevitable.

In discussions of CAP there are several contradictory definitions of the term availability, and the formalization as a theorem [30] does not match its usual meaning [40]. Many so-called “highly available” (fault-tolerant) systems actually do not meet CAP’s idiosyncratic definition of availability. All in all, there is a lot of misunderstanding and confusion around CAP, and it does not help us understand systems better, so CAP is best avoided.
```

## HLD
```
                                                                                    
                                                                                    
                                                   ┌─────┐                          
                                                   │     │                          
                                                   │     │                          
                                       ┌─────┐     └──▲──┘     ┌─────┐              
                                       │     │        │        │     │              
                                       │     │        │        │     │              
                                       └──▲──┘        │        └──▲──┘              
                                          │           │           │                 
                                          │           │           │                 
┌────────┐            ┌────────┐          │           │           │    ┌─────┐      
│        ├───────────►│        │          │           │           │    │     │      
│ user   │            │ master ◄──────────┴───────────┴───────────┘    │     │      
│        │◄───────────┤        │                                       └─────┘      
└────────┘            └────────┘                                                    
                                                                                    
                                        ┌─────┐                ┌─────┐              
                                        │     │                │     │              
                                        │     │                │     │              
                                        └─────┘    ┌─────┐     └─────┘              
                                                   │     │                          
                                                   │     │                          
                                                   └─────┘                          
                                                                                    
                                                                                    
                                                                                    
```

### What's missing in HLD
- Master node (or coordinator node) seems to be a SPOF. 
    
## Scenario : Read / Write path
- What's commit log?
    - Commit log is a file in disk that stores all the write request against Cassandra. (Actually RDBs also have this). When system goes down, data in memory disappears. For recovery purpose, Cassandra stores all writes logs into commit log. 
- What's SSTable?
    - Once data in Memory table exceeds the capacity, it should be stored into disk. SSTable is a data structure that ensures fast-read. 
- Bloom filter
    - Bloom filter reduces SSTables to lookup by letting you know list of tables that NEVER contains the key. (This means bloom filter doesn't tell you if a table does contain a key)


