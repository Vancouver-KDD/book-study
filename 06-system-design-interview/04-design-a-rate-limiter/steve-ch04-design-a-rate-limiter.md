# Chapter 4: Design a Rate Limiter

## What is a Rate Limiter?

- A rate limiter controls the rate of traffic sent by a client or a service.
- A rate limiter limits the number of client requests allowed to be sent over a specified period.
- Excess calls are blocked if the API request count exceeds the threshold defined by the rate limiter.

## Example of Rate Limiter

- A user can write no more than 2 posts per second.
- Maximum of 10 accounts per day can be created from the same IP address.
- Claim rewards no more than 5 times per week from the same device.

## The Benefits of Using an API Rate Limiter

- Prevent resource starvation caused by Denial of Service (DoS) attacks.
- Reduce costs.
- Prevent servers from being overloaded.

## Step 1: Understand the Problem and Establish Design Scope

- **Questions**:
  - Type of rate limiter?
  - Basis for throttling API requests?
  - Scale of the system?
  - Distributed environment?
  - Rate limiter as a separate service or implemented in application code?
  - Need to inform throttled users?
- **Requirements**:
  - Accurate limiting of excessive requests.
  - Low latency.
  - Minimal memory usage.
  - Distributed rate limiting.
  - Exception handling.
  - High fault tolerance.

## Step 2: Propose High-level Design and Get Buy-in

### Where to Put the Rate Limiter?

- **Server-side implementation**:
  - Placed on the server-side.
  - Utilizes rate limiter middleware.
- **API Gateway**:
  - Supports rate limiting, SSL termination, authentication, etc.

### Algorithms for Rate Limiting

- Token bucket.
- Leaking bucket.
- Fixed window counter.
- Sliding window log.
- Sliding window counter.

### High-level Architecture

- Utilizes in-memory cache for storing counters.
- Handles rate-limiting rules and exceeded limit responses.

## Step 3: Design Deep Dive

### Rate Limiting Rules

- Stored on disk.
- Utilize HTTP response headers for informing clients of throttling.

### Detailed Design

- Rules loaded from cache by rate limiter middleware.
- Requests either forwarded to API servers or rate-limited with 429 response.

### Rate Limiter in a Distributed Environment

- Addresses race conditions and synchronization issues.
- Utilizes centralized data stores like Redis.

### Performance Optimization

- Multi-data center setup.
- Synchronize data with an eventual consistency model.

### Monitoring

- Ensures effectiveness of rate limiting algorithm and rules.

## Step 4: Wrap Up

- Rate limiting is a critical component in maintaining the stability and security of network systems, especially in high-traffic scenarios.
- Careful consideration of design choices, such as algorithm selection and implementation strategy, is essential to meet performance and scalability requirements.
- Continuous monitoring and optimization are necessary to ensure that the rate limiter functions effectively and efficiently over time.
- By implementing a robust rate limiting solution, systems can better withstand potential threats and maintain a consistent user experience.