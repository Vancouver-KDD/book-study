# Single server setup 
## How http request traffic flows
- DNS query -> ip address returned -> http request is send to web server -> web server returns html page.
- https://m.blog.naver.com/PostView.naver?blogId=cmw1728&logNo=222158613750&categoryNo=46&proxyReferer=

# Database
- Two tiers: web server and DB
- Types
  - RDBMS
  - NOSQL
    - https://blog.teamtreehouse.com/should-you-go-beyond-relational-databases
    - http://highscalability.com/blog/2010/12/6/what-the-heck-are-you-actually-using-nosql-for.html
Scaling
  - Vertical
    - Pros: simple
    - Cons: hard limit, no failover, no redundancy
  - Horizontal: 
    - Pros: failover, redundancy
    - Cons: complex

# Load balancer
- Web server is located behind load balancer
- client can't access web server directly

# Database replication
- Setup
  - Master: write
  - Slave: replicates master, read
- Advantages: performance, reliability, availability
- Fail over
  - Slave goes offline, read will be redirected to the next slave or master
  - Master goes offline, slave will be promoted to master and performs all DB operations until a new slave is ready
- https://en.wikipedia.org/wiki/Multi-master_replication

# Cache
- It is a temporary data store layer.
- Few considerations
  - Frequent read, not for persisting data
  - Expiration
  - Consistency
  - Mitigating failures: avoid SPOF
  - Eviction policy: LRU
- https://codeahoy.com/2017/08/11/caching-strategies-and-how-to-choose-the-right-one/

# CDN
- static content like images, css, js
- Few considerations
  - Cost: cache frequently used data
  - Cache expiry
  - Fallback for failure
  - Invalidating files

# Stateless web tier
- Move out session data from web server to persistent data store

# Data centers
- Technical challenges of multi data centers
  - Traffic redirection
  - Data sync
  - Test and deployment

# Message queue
- Producer and consumer
- Async

# Logging, metrics, automation
- Logging: Tools to aggregate logs to a centralized service
- Metrics
  - Host level: CPU, Memory, Disk I/O
  - Aggregated level: Performance of DB / Cache tier
  - Business metrics: DAU, retention, etc
- Automation
  - Ci/CD, dev env automation

# DB Scaling
 - Vertical
   - Hardware limit
   - SPOF
   - Expensive
 - Horizontal: sharding
   - Choice of sharding key
   - Challenges
     - Resharding data: consistent hashing
     - Celebrity problem: excessive access to a specific shard
     - Join / de-normalization:n
     - 