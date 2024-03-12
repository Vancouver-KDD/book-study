
# CHAPTER 1: SCALE FROM ZERO TO MILLIONS OF USERS

In this chapter, we build a system that supports a single user and gradually scale it up to serve millions of users.

## Single server setup

everything is running on one server: web app, database, cache, etc.

1. Users access websites throughout domain names: Domain Name System (DNS)

2. Internet Protocol (IP) address is returned to the browser or mobile app.

3. Once IP address is obtained, Hypertext Transfer Protocol (HTTP) requests are sent directly to the webserver.

4. The webserver returns HTML pages or JSON response for rendering.

The traffic to your web server comes from two sources: web application and mobile application.


## database

We need multiple server for database

#### There are two types of database: 

1. Relational Database (SQL database): Relational Database Management System (RDBMS)
- stores data in tables and rows
- support join operations using SQL across different db tables

2. Non-Relational Database (NoSQL database)
- key value stores, graph stores, column stores, document stores
- doesn't support join operations (non relational)

Relational databases are widely used in most cases, but non-relational databases are preferable in certain situations:
- When the application demands extremely low latency.
- When dealing with unstructured data or you don't have any relational data.
- If your primary task involves serializing and deserializing data.
- When there's a need to store a massive amount of data.


## Vertical Scaling vs horizontal Scaling
**Vertical Scaling:**
Making servers stronger (adding more CPU, RAM, etc.).
- Pros: Simple, works well for low traffic.
- Cons: Hits a limit – can't keep adding CPU and memory forever. No backup; if one server fails, the whole web/app crashes.

**Horizontal Scaling:**
Growing resources by adding more servers. Great for big applications.


**Load Balancer:**

When many users access a web server at the same time, and it hits the limit, users may face slow responses or fail to connect. A load balancer fixes this issue.

A load balancer is a device that sits between the user and the server group and acts as an invisible facilitator, ensuring that all resource servers are used equally.

- if one server goes offline, all the traffic will be routed to another server. This prevents the website from going offline.

- You can use load balancers to direct network traffic intelligently among multiple servers. Your applications can handle thousands of client requests. you only need to add more server to the web server pool and the load balancer automatically starts to send requests to them.

## Database replication
Database replication can be used in many database management systems, usually with a master/slave relationship between the original (master) and the copies (slaves)

- better performance: it allows more queries to be processed in parallel

- reliability: You do not need to worry about data loss because data is replicated across multiple locations

- High availability: your website remains in operation even if a database is offline as you can access data stored in another database server.

- If only one slave database is available and it goes offline, read operations will be directed to the master database temporarily. As soon as the issue is found, a new slave database will replace the old one. In case multiple slave databases are available, read operations are redirected to other healthy slave databases. A new database server will replace the old one.
- If the master database goes offline, a slave database will be promoted to be the new master. All the database operations will be temporarily executed on the new master database. A new slave database will replace the old one immediately. In production systems, promoting a new master is more complicated as the data in a slave database might not be up to date. 

## Cache

A cache is a temporary storage area that stores the result of expensive responses or frequently accessed data in memory so that subsequent requests are served more quickly.

Since the application performance is greatly affected by calling the database repeatedly, we can use cache to improve the performance

**Cache tier:**
temporary data store layer, much faster than the database.

- better system performance
- ability to reduce database workloads
- ability to scale the cache tier independently

#### Considerations using cache

1. when to use cache: 
- when data is read frequently but modified infrequently.  
- cache is volatile. Thus, important data should be saved in persistent data store

2. Expiration policy: 
- if the expiration date too short: will cause the system to reload data from the database too frequently. 
- if the expiration date too long: the data can become stale.

3. Consistency: 
- Keeping the data store and the cache in sync. Data-modifying operations on the data store and cache should be in a single transaction

4. Mitigating failures: 
- A single point of failure (SPOF) is a part of a system that, if it fails, will stop the entire system from working. As a result, multiple cache servers across different data centers are recommended to avoid SPOF

5. Eviction Policy: 
- cache eviction: Once the cache is full, any requests to add items to the cache might cause existing items to be removed. 
- Least-recently-used (LRU) is the most popular cache eviction policy. - Other eviction policies, such as the Least Frequently Used (LFU) or First in First Out (FIFO)


## Content delivery network (CDN)

Dynamic content caching:  enables the caching of HTML pages that are based on request path, query strings, cookies, and request headers. The further users are from CDN servers, the slower the website loads

1. User request static contents to the CDN

2. If the CDN server does not have image.png in the cache, the CDN server requests the file from the origin, which can be a web server or online storage like Amazon S3

3. The origin returns the content to the CDN server with Time-to-Live (TTL) which describe how long the image is cached.

4. The CDN caches, and return it to user. the content is caches until the TTL expires.

5. If another user request to get the same content, the cached content will be delivered as long as TTL has not expired.

#### Considerations of using a CDN

- Cost: CDNs are run by third-party providers, and you are charged for data transfers in and out of the CDN

- Setting an appropriate cache expiry (TTL): setting a cache expiry time is important. If it is too long, the content might no longer be fresh. If it is too short, it can cause repeat reloading of content from origin servers to the CDN.

- CDN fallback: If there is a temporary CDN outage, clients should be able to detect the problem and request resources from the origin.

- Invalidating files: remove a file from the CDN before it expires 
    - using APIs provided by CDN vendors
    - use object versioning to serve a different version of the object (add version number parameter to the URL)


## Stateless web tier

store session data in the persistent storage such as relational database or NoSQL. Each web server in the cluster can access state data from databases.


## Stateful architecture

A stateful server remembers client data (state) from one request to the next.

## Stateless architecture

HTTP requests from users can be sent to any web servers, which fetch state data from a shared data store. State data is stored in a shared data store and kept out of web servers. A stateless system is simpler, more robust, and scalable.

**Auto scale:**
Automatically adding or removing web servers based on the traffic load - crucial for fast-growing websites, and it demands multiple data centers.

#### Data centers

- geoDNS: DNS service that allows domain names to be resolved to IP addresses based on the location of a user.

#### Multi data center setup

- Traffic redirection: GeoDNS can be used to direct traffic to the nearest data center depending on where a user is located. 

- Data synchronization: Users from different regions could use different local databases or caches. In failover cases, traffic might be routed to a data center where data is unavailable. - replicate data across multiple data centers so that we direct all traffic to healthy data center.

- Test and development: it is important to test your website/application at different locations. Automated deployment tools are vital to keep services consistent through all the data centers.

## Message queue

it stored in memory, that supports asynchronous communication.

Explain:
- The producer can post a message to the queue when the consumer is unavailable to process it. The consumer can read messages from the queue even when the producer is unavailable.

- The producer and the consumer can be scaled independently. When the size of the queue becomes large. more consumer are added to reduce the processing time.

## Logging, metrics, automation

- Logging: Monitoring error logs to help debugging. tips: You can monitor error logs at per server level or use tools for easy search and viewing.

- Metrics: 
    - Host level metrics: CPU, Memory, disk I/O, etc.
    - Aggregated level metrics: for example, the performance of the entire database tier, cache tier, etc.
    - Key business metrics: daily active users, retention revenue, etc.

- Automation: each code check-in is verified through automation


## Database Scaling

### 1. Vertical Scaling

adding more power (CPU, RAM, DISK, etc.) to an existing machine.
- There are hardware limits
- Greater risk of single point of failures.
- The cost of vertical scaling is high. Powerful servers are much more expensive.

### 2. Horizontal Scaling
Adding more servers.

- Sharding separates large databases into smaller, the actual data on each shard is unique to the shard.
- User data is allocated to a database server based on user IDs. Anytime you access data, a hash function is used to find the corresponding shard.

when implementing a sharding strategy is the choice of the sharding key (unique ID). it allows you to retrieve and modify data efficiently by routing database queries to the correct database.

- Resharding data: when a single shard could no longer hold more data due to rapid growth. Certain shards might experience shard exhaustion faster than others due to uneven data distribution

- Celebrity problem (hotspot key problem): Excessive access to a specific shard could cause server overload.

- Join and de-normalization: Once a database has been sharded across multiple servers, it is hard to perform join operations across database shards. -> de-normalization -> queries can be performed in a single table.

## Millions of users and beyond

- Keep web tier stateless
- Build redundancy at every tier
- Cache data as much as you can
- Support multiple data centers
- Host static assets in CDN
- Scale your data tier by sharding
- Split tiers into individual services
- Monitor your system and use automation tools