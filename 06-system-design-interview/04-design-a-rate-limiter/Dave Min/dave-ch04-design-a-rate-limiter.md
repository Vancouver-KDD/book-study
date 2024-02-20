# CHAPTER 4: DESIGN A RATE LIMITER
In a network system, a rate limiter is used to control the rate of traffic sent by a client or a service. 
In the HTTP world, a rate limiter limits the number of client requests allowed to be sent over a specified period. 
If the API request count exceeds the threshold defined by the rate limiter, 
all the excess calls are blocked. 

Here are a few examples:
  • A user can write no more than 2 posts per second.
  • You can create a maximum of 10 accounts per day from the same IP address.
  • You can claim rewards no more than 5 times per week from the same device.
  
> In this chapter, you are asked to design a rate limiter. 

#### Benefits of using an API rate limiter:
```
  • Prevent resource starvation caused by Denial of Service (DoS) attack [1]. 
      Almost all APIs published by large tech companies enforce some form of rate limiting. 
        For example,
        Twitter limits the number of tweets to 300 per 3 hours [2]. 
        Google docs APIs have the following default limit: 300 per user per 60 seconds for read requests [3]. 
      A rate limiter prevents DoS attacks, either intentional or unintentional, by blocking the excess calls.
  • Reduce cost.
      Limiting excess requests means fewer servers and allocating more resources to high priority APIs.
      Rate limiting is extremely important for companies that use paid third party APIs.
        For example, you are charged on a per-call basis for the following external APIs:
          check credit, make a payment, retrieve health records, etc. 
      Limiting the number of calls is essential to reduce costs.
  • Prevent servers from being overloaded.
      To reduce server load, a rate limiter is used to filter out excess requests caused by bots or users’ misbehavior.
```

## Step 1 - Understand the problem and establish design scope
Rate limiting can be implemented using different algorithms, each with its pros and cons. 
The interactions between an interviewer and a candidate help to clarify the type of rate limiters we are trying to build.

- What kind of rate limiter? client-side rate limiter? or server-side API rate limiter?
- The rate limiter throttle API requests based on IP, the user ID, or other properties?
- Scale of the system? Is it built for a startup? or a big company with a large user base?
- The system work in a distributed environment?
- Is the rate limiter a separate service or should it be implemented in application code?
- Do we need to inform users who are throttled?

Requirements
```
• Accurately limit excessive requests.
• Low latency. The rate limiter should not slow down HTTP response time.
• Use as little memory as possible.
• Distributed rate limiting. The rate limiter can be shared across multiple servers or processes.
• Exception handling. Show clear exceptions to users when their requests are throttled.
• High fault tolerance. If there are any problems with the rate limiter
  (for example, a cache server goes offline), it does not affect the entire system.
```

## Step 2 - Propose high-level design and get buy-in

#### Where to put the rate limiter? Client-side? or Server-side?
```
• 🥵Client-side implementation.
    Generally speaking, client is an unreliable place to enforce rate limiting!
      because client requests can easily be forged by malicious actors.
      Moreover, we might not have control over the client implementation.
```

#### 😉 Server-side implementation.
![fg4-1](image/fg4-1.jpg)
