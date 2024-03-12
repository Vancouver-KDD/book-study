# Chapter 2: Back of the Envelope Estimation


## Why it is important? 

> According to Jeff Dean, Google Senior Fellow, “back-of-the-envelope calculations are estimates you create using a combination of **thought experiments** and **common performance numbers** to get a **good feel** for which designs will meet your requirements”.

It's not about precision - it's about *does it make sense*? 

In interviews, back-of-the-envelope estimation is crucial, yet most interviewers aren't focused on the precision of your calculations. Instead, they're interested in whether your assumptions about business requirements are logical and if the figures you mention for various software/hardware components fall within a reasonable range. (Getting 7\*5 wrong- They might care about that tho)




## Techniques
Even though you don't have to come up with *accurate* calculation, getting familar with the calculation always help. If you feel uncomfortable dealing with LARGE scale numbers, you might stuck during the interview - not a good signal. 

### Power of 2
2^10 = 1024 ~= 1000 = 1K
2^20 ~= 10^3^2 = 1M
10^9 = 1B = 1G
10^12 = 1Trillion  = 1T
10^15 = 1Quadrillion = 1P

(After that - there's Exabyte but you wouldn't need it)


### Typical delays
- Memory reference : 100ns
- Compress 1K bytes : 10 micro-s 
- Send 2KB over 1Gbps : 20 micro-s (if you are SRE/network this might be too-simple answer)
- Memory 1MB sequantial read : 250 micro-s
- Disk seek = 10ms
- Read 1MB sequentially from the network : 10ms
- Read 1MB sequentially from disk : 30ms 
- Read 10MB randomly from disk : ??

### Availability
99.99% : Four nines : 0.365 days == 8 hours/year
99.999% : Five-nines : 0.04 days = 0.8 hours = 50 mins/year
99.9999% : six-nines : 5 mins/year

- What's the 'reasonable' SLA? == Do you have experience enduring vendor-failure?


