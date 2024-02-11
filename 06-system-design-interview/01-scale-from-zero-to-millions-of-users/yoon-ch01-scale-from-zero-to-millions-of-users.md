# Chapter 1: Scale from zero to millions of users

## 1. Single Server Setup

### Everything is running on **one server**

- Users access websites via domain name ⇒ DNS (e.g. bluehost, hostpapa..)
- IP address is returned to the browser or mobile app from DNS.
- HTTPS requests are sent directly to the web server once the IP address is obtained.
- The web server returns HTML pages or JSON response

### Two traffic sources

- **Web application**: Server-side languages to handle business logic, storage, etc., (Backend) + Client-side languages for presentation (Front end)
- **Mobile application**: mobile app and web server communicates using **HTTP Protocol.** JSON format: commonly used API response format **to transfer data between them**.

## 2. Database

Separate web/mobile traffic (web tier) and database (data tier) ⇒ for them to scale independently.

### Two types of Database

- Relational (SQL): represent and store data in **tables and rows**. (e.g. MySQL, PostgresSQL)
- Non-Relational (NoSQL): grouped into four categories: **key-value stores, graph stores, column stores, and document stores**.

## 3. Vertical Scaling vs Horizontal Scaling

**Vertical Scaling**: “scale up”, adding more power

- impossible to add unlimited CPU and memory to a single server
- no failover and redundancy ⇒ when server goes down, app and web does too.

**Horizontal Scaling**: “scale-out”, adding more servers

- desirable for large scale apps

## 4. Load Balancer

web/app traffic improve 할 수 있는 솔루션!

- evenly distributes incoming traffic among web servers that are defined in a load-balanced set
- user connects to the public IP of load balancer instead of directly connects to the server + add a second server
  ⇒ if one of the server goes down, a load balancer routes all the traffic to the other server.

## 5. Database Replication

data tier improve 할 수 있는 솔루션!

- **master-slave relationship**
  - master database: the original, write operations (Insert, delete or upadte)
  - slave databases: copies of the data from the master, support read operations
- If none of the slave database is available, master handles both read and write temporarily.
- If master goes down, one of the slave databases will be promoted to master.

## 6. Cache

- Cache: a temporary storage area that stores responses, much faster than database.
- After receiving a request, a web server first checks if the cache has the available response. ⇒ If not, it queries the database.
- 서버와 데이터베이스 사이에서 중간 임시 저장소 역할. Request 올때마다 데이타베이스에서 쿼리 안해도 되도록.
- **Considerations**
  - Use when the data is frequently read, not modified.
  - Expiration policy
  - Consistency between database and cache store
  - Mitigating failure: having multiple cache servers is recommended to avoid SPOF (Single point of failure)
  - Eviction policy: when the storage is full, it can remove the existing items. methods: LRU / LFU / FIFO

## 7. CDN (Content Delivery Network)

- geographically dispersed servers used to deliver static content.
- Considerations
  - Can be costly ⇒ run by third-party providers and charged for data transfers
  - Appropriate cache expiry (TTL) ⇒ not too long or too short
  - CDN fallback: client should be able to request resources from the origin, when CDN is down.

## 8. Stateless web tier

- Stateful vs Stateless
  - Stateful server stores client data in each server. ⇒ every request from the same client must be routed to the same server, which makes it hard to add or remove servers.
  - Stateless server stores data in a shared data store and keeps it out of web servers. ⇒ adding or removing servers is easier based on the traffic load. (auto scaling)

## 9. Data centers

- geoDNS: a DNS service that allows domain names to be resolved to IP addresses based on the location of a user.
  - ‘geoDNS-routed, geo-routed’ : route users to the closest data center.
- Technical challenges:
  - Traffic redirection: effective tools are needed to handle traffic to the correct data center.
  - Data synchronization: In failover cases, a traffic might be routed to a data center where data is not available. ⇒ replicate data across multiple data centers
    - e.g. Netflix - asynchronous multi-data center replication
  - Test and deployment: Automated deployment tools are necessary.

## 10. Message queue

- Message queue: a durable component, stored in memory that supports async communication.
- The basic structure of message queue
  - 1. Input service (i.e. photo processing jobs)
  - 2. Called producers/publishers
  - 3. Create messages (i.e. editing tasks-cropping, sharpening..)
  - 4. Publish them to a message queue
  - 5. Consumers pick up jobs from the message queue (photo processing workers)
- When the size of the message queue becomes large, more workers are added to reduce the processing time.

## 11. Logging, metrics, automation

1. Logging: Monitoring error logs

2. Metrics: Collecting diferent types of metrics

- Host level metrics: CPU. Memory, disk IO
- Aggregated level metrics: performance of entire database tier
- Key business metrics: daily active users, retention.. e.g. Google Analytics?

3. Automation: leverage automation tools for code check-in, build, test, deploy.

## 12. Database scaling

1. Vertical scaling

- adding more power (CPU, RAM, DISK)
- Hardward limit, risk of SPOF (single point of failures)

2. Horizontal scaling (sharding)

- adding more servers
- separate large databases into smaller, more easilly managed part called **shard**s.
- Each shard shares the same data schema, but unique data.
- Hash function used to fin the corresponding shard.
- **Sharding key**
  - consists of one or more columns that determine how data is distributed.
  - helps retrieve and modify data efficiently by routing database queries to the correct database.
