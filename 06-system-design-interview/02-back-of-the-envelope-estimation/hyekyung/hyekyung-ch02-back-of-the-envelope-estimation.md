# 02. Back of the envelope estimation
"back-of-the-envelope calculations are estimates you create using a combination of thought experiments and common performance numbers to get a good feel for which designs will meet your requirements"
	
### Power of two
| Power | Approximate value |  Full name | Short name |
|-------|-------------------|------------|------------|
|10     | 1 Thousand        | 1 Kilobyte | 1 KB       |
|20     | 1 Million         | 1 Megabyte | 1 MB       |
|30     | 1 Billion         | 1 Gigabyte | 1 GB       |
|40     | 1 Trillion        | 1 Terabyte | 1 TB       |
|50     | 1 Quadrillion     | 1 Petabyte | 1 PB       |

### Latency numbers every programmer should know
- 1 ns = 10^-9 seconds
- 1 μs = 10^-6 seconds = 1,000 ns
- 1 ms = 10^-3 seconds = 1,000 μs = 1,000,000 ns

| Operation name                 | Time               |
|--------------------------------|--------------------|
| L1 cache reference             | 0.5 ns       |
| Branch mispredict              | 5 ns         |
| L2 cache reference             | 7 ns         |
| Mutex lock/unlock              | 100 ns       |
| Main memory reference          | 100 ns       |
| Compress 1K bytes with Zippy   | 10,000 ns = 10 μs |
| Send 2K bytes over 1 GPS network| 20,000 ns = 20 μs |
| Read 1 MB sequentially from memory | 250,000 ns = 250 μs|
| Round trip within the same datacenter| 500,000 ns = 500 μs|
| Disk seek | 10,000,000 ns = 10 ms|
| Read 1 MB sequentially from the network | 10,000,000 ns = 10 ms |
| Read 1 MB sequentially from disk | 30,000,000 ns = 30 ms |
| Send packet CA(California)-> Netherlands->CA | 150,000,000 ns = 150 ms|

- Memory is fast but the disk is slow
- Avoid disk seeks if possible
- Simple compression algorithms are fast
- Compress data before sending it over the internet if possible
- Daa centers are usually in different regions, and it takes time to send data between them. 

### Availability numbers
- High availability: continuously operational for a desirably long period of time
- Most services fall between 99% and 100%

| Availability % | Downtime per day | Downtime per week | Downtime per month | Downtime per year|
|----------------|------------------|-------------------|--------------------|------------------|
| 99% | 14.40 minutes | 1.68 hours | 7.31 hours | 3.65 days |
| 99.9% | 1.44 minutes | 10.08 minutes | 43.83 minutes | 8.77 hours |
| 99.99% | 8.64 seconds | 1.01 minutes | 4.38 minutes | 52.60 minutes |
| 99.999% | 864.00 milliseconds | 6.05 seconds | 26.30 seconds | 5.26 minutes |
| 99.9999% | 86.40 milliseconds | 604.80 milliseconds | 2.63 seconds | 31.56 seconds |

### Example : Estimate Twitter QPS and storage requirements 
- Assumptions & Estimations
	- 300 million monthly active users
	- 50% of users use Twitter daily 
		- 300 *0.5 = 150 million daily active users
	- Users post 2 tweets per day on average 
		- 150 million*2 = 300 million tweets per day
		- Tweets QPS(Query per second) = 300 million % 24 hour % 60 min % 60 sec = ~3500
		- Peak QPS = 2* QPS <span style="color:red"><-- 질문 : 왜 2배를 곱한거지??</span>
	- 10% of tweets contain media
		- 300 million * 0.1 = 30 million media per day
	- Data is stored for 5 years
	- average tweet size: tweet_id (64 bytes), text (140 bytes), media ( 1MB)
		- media storage: 30 million * 1MB = 30,000,000 MB = 30,000 GB = 30 TB per day
		- 5 year media storage: 30 TB * 365 * 5 = ~ 55 PB

### Tips
- Rounding and Approximation
- Write down your assumptions
- Label your units
- Commonly asked back-of-the-envelope estimations