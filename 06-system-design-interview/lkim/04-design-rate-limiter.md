# Design a Rate Limiter
**Rate limiter**: used to control the rate of traffic sent by a client or a service
- Prevent resource starvation caused by Denial of Service (DoS) attack
- Reduce cost. Limiting excess requests means fewer servers and allocating more resources to high priority APIs - especially when making call for the paid third party APIs
- Prevent servers from being overloaded

## Step 1 - Understand the problem and establish design scope
Can use different algorithms and its pros and cons
- client-side VS server side
- throttle requests based on IP, user ID, or?
- the scale of the system, the size of the user base
- distributed env?
- a separate service or in the application code
- Inform users when the rate limit was reached

## Step 2 - Propose high-level design and get buy-in
### Where to put the rate limiter? client, server, or middleware
- client is an unreliable place because client requests can easily be forged by malicious actors. Moreover, we might not have control over the client implementation.
#### API gateway
- most popular with cloud microservice, supporting rate limiting, SSL termination, authentication, IP whitelisting, servicing static content, etc.
#### server side or in a gateway?
- things to consider: technology stack, rate limiting algorithm for the business needs, prefer to use a commercial API gateway?
  
### Algorithms for rate limiting
#### Token bucket
- two params: pre-defined capacity (ex: 4), and the refill rate (ex: 4 per 1 minute)
- usually have different buckets for different API endpoints.
- can throttle requests based on IP - each IP requires a bucket size
- Or the system can have a global bucket size shared by all requests (ex: to handle 10,000 requests per second)

- Pros: simple, memory efficient, allows a burst of traffic for short periods
- Cons: tuning two params might be challenging

#### Leaking bucket algorithm
- First in First out (FIFO) queue instead of the token bucket
- Requests are pulled from the queue and processed at the regular 
- Pros: memory efficient, requests are processed at a fixed rate
- Cons: Two params. a burst of traffic fills up the queue and if they're not processed in time, recent requests will be dropped.

#### Fixed window counter algorithm
- divides the timeline into fix-sized time windows and assign a counter for each window.
- Once the counter reaches the pre-defined threshold, new requests are dropped until a new time window starts.
- Pros: easy, memory efficient, fits certain use cases
- Cons: Spike in traffic at the edges of a window could cause more requests than the allowed quota to go through.

#### Sliding window log algorithm
- It keeps track of request timestamps, storing in cache, Redis.
- When a new request comes in, remove all the outdated timestamps. Outdated timestamps are defined as those older than the start of the current time window.
- If the log size is the same or lower than the allowed count, a request is accepted.
- Pros: very accurate. In any rolling window, requests will not exceed the rate limit.
- Cons: consumes a lot of memory because it stores timestamps of rejected requests in memory too

#### Sliding window counter algorithm
- hybrid approach that combines the fixed window
counter and sliding window log.
- Pros: memory efficient, smooths out spikes in traffic because the rate is based on the average rate of the previous window.
- Cons: an approximation of the actual rate
because it assumes requests in the previous window are evenly distributed.

### High-level architecture
- At the high-level, we need a counter to keep track of how many requests are sent from the same user, IP address, etc. If the counter is larger than the limit, the request is disallowed.
- In-memory cache like Redis is a popular option for rate limiting, offering two commands: INCR and EXPIRE
  - INCR: It increases the stored counter by 1.
  - EXPIRE: It sets a timeout for the counter. If the timeout expires, the counter is automatically deleted.

## Step 3 - Design deep dive
**Unanswered question yet**
- How are rate limiting rules created? Where are the rules stored?
- How to handle requests that are rate limited?

### Rate limiting rules
Rules are generally written in configuration files and saved on disk.
```
domain: auth
descriptors:
- key: auth
  Value: login
  rate_limit:
     unit: minute
     requests_per_unit: 5
```
### Exceeding the rate limit
- If some orders are rate limited due to system overload, we may keep those orders to be processed later by enqueueing the rate-limited requests
#### Rate limiter headers
- The rate limiter returns the following HTTP headers to clients, regarding the remaining number of allowed requests, how long they should wait for the next request

### Rate limiter in a distributed environment
- scaling the system to support multiple servers and concurrent threads lead to two challenges: Race condition, Synchronization issue

#### Race condition
- Locks are the most obvious solution for solving race condition but it slows down the system. 
significantly slow down the system.
- Two strategies are commonly used to solve the problem:
Lua script and sorted sets data structure in Redis

#### Synchronization issue
- To support millions of users, one rate limiter server might not be enough to handle the traffic. When multiple rate limiter servers are used, synchronization is required.
- Solution:
  - to use centralized data stores like Redis.
  - not advisable - use sticky sessions that allow a client to send traffic to the same rate limiter (neither scalable nor flexible)

### Performance optimization
1. multi-data center setup is crucial for a rate limiter because latency is high for users
located far away from the data center.
2. synchronize data with an eventual consistency model - not the immediate consistency

### Monitoring
- ensure if the algorithm and rules are effective
- i.e) if rate limiting rules are too strict, many valid requests are dropped - relax the rules a little bit.
- the rate limiter becomes ineffective when there is a sudden increase in traffic like flash sales. - 
replace the algorithm to support burst traffic like Token bucket

## Step 4 - Wrap up
additional talking points to mention if time allows:
- Hard vs soft rate limiting.
  - Hard: The number of requests cannot exceed the threshold.
  - Soft: Requests can exceed the threshold for a short period.
- Rate limiting at different levels.
    - In this chapter, we only talked about rate limiting at the application level (HTTP: layer 7). It is possible to apply rate limiting at other layers - network layer etc
    - rate limiting by IP addresses using Iptables (IP: layer 3).
- **How to avoid being rate limited.** Design your client with best practices:
  - Use client cache to avoid making frequent API calls.
  - Understand the limit and do not send too many requests in a short time frame.
  - Include code to catch exceptions or errors so your client can gracefully recover from exceptions.
  - Add sufficient back off time to retry logic.
