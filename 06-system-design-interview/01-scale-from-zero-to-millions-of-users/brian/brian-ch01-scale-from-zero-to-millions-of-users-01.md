# CHAPTER 1: SCALE FROM ZERO TO MILLIONS OF USERS		

## 1 Single server setup
![singleServerSetup.png](pictures%2FsingleServerSetup.png)
### Request flow
![requestFlow.png](pictures%2FrequestFlow.png)

## 2 Database
![Database.png](pictures%2FDatabase.png)
### Which database to use?
1. Traditional Database/Relational Database/RDB/RDBMS/SQL Database
    - Join operations using SQL across different database tables
    - Ex) MySQL, Oracle database, PostgreSQL, etc.
2. Non-traditional Database/NoSQL Database
    - 4 categories: key-value stores, graph stores, column stores, and document stores
    - These generally do not support Join operations
    - Ex) CouchDB, Neo4j, Cassandra, HBase, Amazon DynamoDB, etc.
#### Choose non-relational database when these cases
1. Super-low latency
2. Unstructured, or no relational data
3. Serialize & deserialize data(Json, XML, Yaml, etc.)
4. massive amount of data
### Vertical scaling vs horizontal scaling
- Vertical scaling = "scale up" -> add more power(CPU, RAM, etc)
  - Pros: simple
  - Cons: 
    1. Impossible to add unlimited CPU and memory to a single server.
    2. It does not have failover and redundancy.
- horizontal scaling = "scale out" -> add more servers

## 3 Load balancer
![loadBalancer.png](pictures%2FloadBalancer.png)
1. Better security with private IPs 
2. No failover issue 
3. Improved the availability

## 4 Database replication
![dbMasterSlaves.png](pictures%2FdbMasterSlaves.png)
- Master: write
- Slaves: Read
### Advantages of database replication
1. Better performance: Master and Slaves
2. Reliability: Replications across multiple locations 
   - Minimize system failures & downtime
   - MTBF(Mean Time Between Failures)
3. High availability: Servers remains in operation
   - -> Minimize operational time
   - Availability = MTBF / (MTBF + MTTR)
     - MTTR(Mean Time To Repair)

### Usecase
1. One master + One salve 
   - -> the slave down 
     - -> Redirect to Master to do read operation 
       - -> Replace with a new salve
2. One master + Multiple slaves 
   - -> A slave down
     - -> Redirect to other slave
       - -> Replace with a new salve
3. One master + Multiple slaves
    - -> The master down
        - -> One of the slave will be promoted to be the master
            - -> Replace with a new salve
    - * In production, promoting a new master is more complicated
      - -> Multi-masters
      - -> Circular Replication
      
![circularReplication.png](pictures%2FcircularReplication.png)

### System design after adding the load balancer & DB replication
![dbReplication.png](pictures%2FdbReplication.png)

## 5 Cache
1. Read-Through Caching
![readThroughCache.png](pictures%2FreadThroughCache.png)
2. Write-Through Caching
![writeThroughCache.png](pictures%2FwriteThroughCache.png)
3. Write-Back Caching
![Write-BackCache.png](pictures%2FWrite-BackCache.png)
4. Cache-Aside
![CacheAsideCache.png](pictures%2FcacheAside.png)

### Considerations for using cache
1. Data is stored in volatile memory
2. Expiration policy
3. Consistency
4. Mitigating failures -> SPOF(Single Point of Failure)
   - ![sfof.png](pictures%2Fsfof.png)
5. Eviction Policy
   - LRU(Least-Recently-Used)
   - LFU(Least Frequently Used)
   - FIFO(First In First Out)

## 6 Content delivery network (CDN)
- CDN Server cache static content like images, videos, CSS, JavaScript files, etc.
- CDN improves load time

![cdnResponseTime.png](pictures%2FcdnResponseTime.png)
- CDN workflow

![cndWorkflow.png](pictures%2FcndWorkflow.png)
- data includes optional HTTP header Time-to-Live(TTL)
- The data is returned from the cache as long as the TTL has not expired.

### Considerations of using a CDN
- Cost: Only frequently using data
- Setting an appropriate cache expiry: 
  - too long -> no fresh
  - too short -> repeat reloading of content from original server
- CDN fallback
- Invalidating files
  - Using APIs provide by CDN vendors
  - Adding version in the url

### System design after adding CDN & Cache
![cdnAndCache.png](pictures%2FcdnAndCache.png)