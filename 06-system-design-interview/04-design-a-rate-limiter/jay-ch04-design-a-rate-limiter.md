# CHAPTER 4: DESIGN A RATE LIMITER

## Rate Limiter

- Controls the rate of traffic over a specific period
- Blocks excess traffics over threshold
- ex) no more than 2 writes per second, 10 account creation per day
- Prevents Denial of Service (DoS) attack (resource starvation)
- Reduce cost (especially when using 3rd part API)
- Prevents overload

## Step 1 - Understand the problem and establish design scope

Questions to ask:

- Client side vs server side api rate limiter?
- Based on IP, user id or other?
- Scale of systems?
- Is system distributed system?
- Limiter is part of the application code or separate service?
- Need to inform users who are throttled?

## Step 2 - Propose high-level design and get buy-in

- Client-side limiter is unreliable. Can be easily forged by malicious actors
- Server-side limiter
- Middleware limiter
- Limiters reutn HTTP 429 (Too many requests) when throttling
- Micro-services have rate limiter within them (API gateway middleware)
- Limiter on server vs middleware?
  - depends on tech stack (does the current language and framework is easy to implement it?)
  - u have full control over the limter if you have it on serverside
  - do you have enough resource to build the limiter yourself?

### Token bucket algorithm

- most common
- certain number of tokens get refilled per second for the server
- each request takes a token to be processed
- when a request comes in check if server has enough tokens, if enough go through, if not drop
- Bucket size: the maximum number of tokens allowed in the bucket
- Refill rate: number of tokens put into the bucket every second
- A bucket is required per api endpoint

Pros:

- The algorithm is easy to implement.
- Memory efficient.
- Token bucket allows a burst of traffic for short periods. A request can go through as long as there are tokens left.
  Cons:
- Two parameters in the algorithm are bucket size and token refill rate. However, it might
  be challenging to tune them properly.

### Leaking bucket algorithm

- Same as above except that requests are processed at a fixed rate
- First-in-first-out (FIFO) queue
- Requests are processed from the queue at a fixed rate
- Bucket size: it is equal to the queue size. The queue holds the requests to be processed at a fixed rate
- Outflow rate: it defines how many requests can be processed at a fixed rate, usually in seconds

### Fixed window counter algorithm

- The algorithm divides the timeline into fix-sized time windows and assign a counter for each window.
- Each request increment count
- Once threshold is reached, requests are dropped until new window is opened
- since this is using a fixed window, a burst of requests at the end & start of the time window can go above allowed amount
  Pros:
- Memory efficient.
- Easy to understand.
- Resetting available quota at the end of a unit time window fits certain use cases.
  Cons:
- Spike in traffic at the edges of a window could cause more requests than the allowed quota to go through.

### Sliding window log algorithm

- fixes the issue where it goes above threshold at the edges of the windows since it is a sliding window instead of fixed window
- keep track of timestamps of the oldest request that starts a new window
- if the number of requests is same or lower than allowed within the window, accept it
- if above, reset the oldest request timestamp as current one

Pros:

- Rate limiting implemented by this algorithm is very accurate. In any rolling window, requests will not exceed the rate limit.
  Cons:
- The algorithm consumes a lot of memory because even if a request is rejected, its timestamp might still be stored in memory.

### Sliding window counter algorithm

- a hybrid approach that combines the fixed window counter and sliding window log
- Requests in current window + requests in the previous window \* overlap percentage of the rolling window and previous window
- still uses fixed widnows but also a sliding window
- Requests in current window + requests in the previous window \* overlap percentage of the rolling window and previous window

Pros

- It smooths out spikes in traffic because the rate is based on the average rate of the previous window.
- Memory efficient.
  Cons
- It only works for not-so-strict look back window. It is an approximation of the actual rate because it assumes requests in the previous window are evenly distributed. However, this problem may not be as bad as it seems. According to experiments done by Cloudflare [10], only 0.003% of requests are wrongly allowed or rate limited among 400 million requests.

## Where to store counter of requests?

- not DB slow disk access
- In-memory cache because it is fast and supports time-based expiration strategy.

## Design deep dive

- Set rules
- When requests are throttled HTTP 429 is returned
- Header
- X-Ratelimit-Remaining: The remaining number of allowed requests within the window.
- X-Ratelimit-Limit: It indicates how many calls the client can make per time window.
- X-Ratelimit-Retry-After: The number of seconds to wait until you can make a request again without being throttled.

## Detailed design

- rules are stored in disk, workers put them in cache
- rate limiter middleware uses the cache, fetches counter and last request timestamps from Redis

## Rate limiter in a distributed environment

- race condition
  - can happen when requests are made concurrently
  - Locks can be a solution but will slow down
  - Lua script and sorted sets data structure in Redis can be alternatives
- sync issue
  - sticky session
  - or use centralized redis
-
