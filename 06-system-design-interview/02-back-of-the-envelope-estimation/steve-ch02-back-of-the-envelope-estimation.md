# Chapter 1: Back of the Envelope Estimation

System design estimation techniques involve utilizing a blend of mental exercises and typical performance metrics to gain insights into whether proposed designs align with requirements. Below are some key concepts and strategies:

## Utilizing the Power of Two
Understanding data volume units is crucial, often leveraging the power of two for efficient calculations:

- A byte consists of 8 bits.
- Each ASCII character occupies one byte (8 bits).

| Data Volume Unit | Size (Bytes) |
|------------------|--------------|
| Bit              | 1            |
| Byte             | 8            |
| Kilobyte (KB)    | 1024         |
| Megabyte (MB)    | 1024 KB      |
| Gigabyte (GB)    | 1024 MB      |
| Terabyte (TB)    | 1024 GB      |
| Petabyte (PB)    | 1024 TB      |
| Exabyte (EB)     | 1024 PB      |
| Zettabyte (ZB)   | 1024 EB      |
| Yottabyte (YB)   | 1024 ZB      
...Brontobyte (BB), Geopbyte (GB), Saganbyte (SB)

## Latency Numbers Every Programmer Should Know
These numbers offer insights into the relative speed of various computer operations, aiding in system design decisions:

![](./steve/Latency numbers.png)

## Availability Numbers
High availability refers to a system's ability to remain operational for extended periods. Service Level Agreements (SLAs) often formalize uptime commitments:

| Availability | Downtime per Year | Downtime per Month | Downtime per Week | Downtime per Day |
|--------------|-------------------|--------------------|-------------------|------------------|
| 90% (one nine) | 36.5 days | 72 hours | 16.8 hours | 2.4 hours |
| 99% (two nines) | 3.65 days | 7.20 hours | 1.68 hours | 14.4 minutes |
| 99.9% (three nines) | 8.76 hours | 43.2 minutes | 10.1 minutes | 1.44 minutes |
| 99.99% (four nines) | 52.56 minutes | 4.32 minutes | 1.01 minutes | 8.64 seconds |
| 99.999% (five nines) | 5.26 minutes | 25.9 seconds | 6.05 seconds | 864 milliseconds |

## Example: Estimating Twitter's QPS and Storage Needs
Given assumptions about user behavior on Twitter, estimations for daily active users (DAU), tweets per second (QPS), peak QPS, and media storage requirements can be made. 

| Parameter | Estimation |
|-----------|------------|
| Daily Active Users (DAU) | 150 million |
| Tweets Query per Second (QPS) | ~3500 |
| Peak QPS | ~7000 |
| Media Storage (per day) | 30 TB |
| 5-year Media Storage | ~55 PB |

## Tips for Effective Estimations
- Rounding and Approximation: Avoid complex math operations. Precision isn't crucial; use round numbers and approximations to simplify calculations.
- Document Assumptions: Writing down assumptions aids in referencing them later, ensuring clarity and consistency in your estimations.
- Label Units: Specify units to avoid ambiguity. For instance, use "5 MB" instead of just "5" to denote megabytes.
- Common Estimations: Practice calculations for commonly asked parameters such as QPS, peak QPS, storage, cache, and number of servers to prepare effectively.