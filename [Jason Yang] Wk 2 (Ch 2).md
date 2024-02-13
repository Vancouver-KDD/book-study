According to Jeff Dean, Google Senior Fellow, “back-of-the-envelope calculations are estimates you create using a combination of thought experiments and common performance numbers to get a good feel for which designs will meet your requirements”

### Power of two
- The "Power of two" is essential in back-of-the-envelope estimations for system design because it helps in understanding and estimating the scale of data efficiently.
- Computing systems are based on binary, and storage units are calculated in powers of two.
- Being familiar with these units (like kilobyte, megabyte, gigabyte, etc.) allows for quick and rough estimations of data handling requirements in systems, which is a fundamental part of designing scalable and efficient systems.
- Knowing the approximate values corresponding to these powers can greatly simplify the process of estimating how much data a system can store or process, and how it might scale as data grows.

![](https://i.imgur.com/5jvOq7M.png)


### Latency numbers every programmer should know
- Dr. Dean from Google reveals the length of typical computer operations in 2010
- Some numbers are outdated as computers become faster and more powerful.
- However, those numbers should still be able to give us an idea of the fastness and slowness of different computer operations.

![](https://i.imgur.com/mbwjpRV.png)


#### Network
- Send 2K bytes over 1 Gbps network:
	- Transmitting 2 kilobytes of data across a network with a bandwidth of 1 gigabit per second.
- Round trip within the same datacenter:
	- The total time for data to go to a destination and back within the same data center.
- Read 1 MB sequentially from the network:
	- Reading 1 megabyte of data in sequence over a network.
- Send packet CA (California) -> Netherlands -> CA:
	- The time for a packet of data to travel from California to the Netherlands and back.

#### Algorithm
- Compress 1K bytes with Zippy:
	- The time it takes to compress 1 kilobyte of data using the Zippy compression algorithm.
	- The Zippy compression algorithm, also known as Snappy, is a fast data compression and decompression library developed by Google.
	- It prioritizes speed over compression ratio, meaning it's faster than algorithms like zlib that might compress to a smaller size but take longer to do so. Zippy is designed for high-speed compression scenarios and is used in various internal Google projects.
	- It's particularly useful for compressing and decompressing data on the fly, such as in real-time data streaming or fast database storage and retrieval.

#### Multi-threaded
- Mutex lock/unlock:
	- Operations to secure exclusive access to a resource in concurrent programming.
	- A Mutex lock, which stands for mutual exclusion, is a mechanism used in concurrent programming to prevent simultaneous access to a shared resource by multiple threads or processes.

#### CPU
- Branch mispredict:
	- The penalty paid in time when the CPU incorrectly predicts the flow of control in code execution.
	- Branch prediction is a feature in modern CPUs that tries to guess the path a program will take before it knows for sure, in order to keep the pipeline of instructions flowing smoothly. When executing a program, there are points where a decision has to be made, like after an 'if' statement. The CPU uses branch prediction algorithms to predict which way the 'if' statement will go. If the CPU predicts correctly, execution is faster because the instructions it needs next are already being processed. However, if the CPU predicts incorrectly, known as a branch mispredict, it must discard the incorrect instructions that were prefetched and fetch the correct ones, which takes additional time and slows down the execution.

#### Memory
- L1 cache reference:
	- Accessing data from the fastest, but smallest, CPU cache memory.
- L2 cache reference:
	- Accessing data from the second level of CPU cache, which is slightly slower but larger than L1.
- Main memory reference:
	- Accessing data from the main RAM, slower than cache references.
- Read 1 MB sequentially from memory:
	- Reading 1 megabyte of data in sequence from RAM.

#### Disk
- Disk seek:
	- The time it takes for a hard drive’s read/write head to move to the part of the disk where data is stored.
- Read 1 MB sequentially from disk:
	- Reading 1 megabyte of data in sequence from a hard disk.


A Google software engineer built a tool to visualize Dr. Dean’s numbers.
- The tool also takes the time factor into consideration.
- Figures 2-1 shows the visualized latency numbers as of 2020 (source of figures: reference material).

By analyzing the numbers in Figure 2-1, we get the following conclusions:
- Memory is fast but the disk is slow.
- Avoid disk seeks if possible.
- Simple compression algorithms are fast.
- Compress data before sending it over the internet if possible.
- Data centers are usually in different regions, and it takes time to send data between them.

![](https://i.imgur.com/kcGxbd9.png)


#### Availability numbers
- High availability is the ability of a system to be continuously operational for a desirably long period of time.
- High availability is measured as a percentage, with 100% means a service that has 0 downtime.
	- Most services fall between 99% and 100%.
- A service level agreement (SLA) is a commonly used term for service providers.
	- This is an agreement between you (the service provider) and your customer, and this agreement formally defines the level of uptime your service will deliver.
- Cloud providers Amazon, Google and Microsoft set their SLAs at 99.9% or above.
- Uptime is traditionally measured in nines.
	- The more the nines, the better. As shown in Table 2-3, the number of nines correlate to the expected system downtime.
- Downtime in a system or service can be caused by various factors, including
	- Hardware failures
	- Software bugs
	- Maintenance (scheduled or emergency)
	- Network issues
	- Power outages
	- Cyber-attacks like DDoS (Distributed denial of service)
	- Human error
	- Disaster

![](https://i.imgur.com/g2tVdFK.png)


### Tips
- Back-of-the-envelope estimation is all about the process. Solving the problem is more important than obtaining results.
- Interviewers may test your problem-solving skills. Here are a few tips to follow:
	- Rounding and Approximation.
		- It is difficult to perform complicated math operations during the interview. For example, what is the result of “99987/9.1”? There is no need to spend valuable time to solve complicated math problems.
		- Precision is not expected.
		- Use round numbers and approximation to your advantage. The division question can be simplified as follows: “100,000 / 10”.
	- Write down your assumptions.
		- It is a good idea to write down your assumptions to be referenced later.
	- Label your units.
		- When you write down “5”, does it mean 5 KB or 5 MB? You might confuse yourself with this. Write down the units because “5 MB” helps to remove ambiguity.
	- Commonly asked back-of-the-envelope estimations:
		- QPS, peak QPS, storage, cache, number of servers, etc.
		- You can practice these calculations when preparing for an interview. Practice makes perfect.


### (Twitter) Back of the envelope estimation

#### Traffic
- This will be a read-heavy system,
	- let us assume we have 1 billion total users with <mark style="background: #BBFABBA6;">200 million daily active users (DAU)</mark>,
	- and on average <mark style="background: #BBFABBA6;">each user tweets 5 times a day</mark>
	- This gives us 1 billion tweets per day.

>200 million x 5 tweets = 1 billion/day (tweets)

- Tweets can also contain media such as images, or videos.
	- We can assume that <mark style="background: #BBFABBA6;">10 percent of tweets</mark> are media files shared by the users, which gives us additional 100 million files we would need to store.

>10 percent x 1 billion = 100 million/day

- What would be Requests Per Second (RPS) for our system?
	- 1 billion requests per day translate into 12K (12,000) requests per second.

>1 billion / (24 hrs x 3600 seconds) = ~12K requests/second


#### Storage
- If we assume each message on <mark style="background: #BBFABBA6;">average is 100 bytes,</mark>
	- we will require about 100 GB of database storage every day.

>1 billion x 100 bytes = ~100 GB/day

- We also know that around 10 percent of our daily messages (100 million) are media files per our requirements.
	- If we assume <mark style="background: #BBFABBA6;">each file is 50 KB</mark> on average, we will require 5 TB of storage every day.
	- and for 10 years, we will require about 19 PB of storage.

>100 million x 100 KB = 5 TB/day

>(5 TB + 0.1 TB) x 365 days x 10 years = ~19 PB


#### Bandwidth
- As our system is handling 5.1 TB of ingress every day, we will require a minimum bandwidth of around 60 MB per second.

>5.1 TB / (24 hrs x 3600 seconds) = ~60 MB/second

#### High-level estimate
- Daily active users (DAU): 200 million
- Each user 5 tweets per day on average
- Average tweet size 100 bytes:
	- tweet_id: 64 bytes
	- text: 100 bytes
	- media: 50 KB
- 10% of tweets contain media
- Requests per second (RPS): 12K/s
- Peek RPS = 2 * RPS = ~24K/s -> 보통 2~3배
- Storage (per day): ~5.1 TB
	- Tweets Storage: 0.1 TB
	- Media Storage: 5 TB
- Storage (10 years): ~19 PB
- Bandwidth: ~60 MB/s



### (Netflix) Back of the envelope estimation

#### Traffic
- This will be a read-heavy system,
	- let us assume we have 1 billion total users with <mark style="background: #BBFABBA6;">200 million daily active users (DAU)</mark>,
	- and on average <mark style="background: #BBFABBA6;">each user watches 5 videos a day.</mark>
	- This gives us 1 billion videos watched per day.

>200 million x 5 videos = 1 billion/day


- Assuming a 200: 1 read/write ratio, about 50 million videos will be uploaded every day.

>1 billion / 200 = 25 million/day (o)
>1200 x 1 billion = 50 million/day (x)


- What would be Requests Per Second (RPS) for our system?
	- 1 billion requests per day translate into 12K (12,000) requests per second.

>1 billion / (24 hrs x 3600 seconds) = ~12K requests/second

#### Storage
- If we assume each video is 100 MB on average,
	- we will require about 5 PB of storage every day.

>25 million x 100 MB = 2.5 PB/day

- And for 10 years,
	- we will require an astounding 9,125 PB of storage.

>2.5 PB x 365 days x 10 years = ~9,125 PB


#### Bandwidth

- As our system is handling 5 PB of ingress every day,
	- we will require a minimum bandwidth of around 58 GB per second.
>2.5 PB / (24 hrs x 3600 seconds) = ~29 GB/second


#### High-level estimate
- Daily active users (DAU): 200 million
- on average each user watches 5 videos a day.
- on average, each video is 100 MB
- Requests per second (RPS): 12K/s
- Peek RPS = 2 * RPS = ~24K/s -> 보통 2~3배
- Storage (per day): ~5 PB
- Storage (10 years): ~18,250 PB
- Bandwidth: ~58 GB/s



### (Whatsapp) Back of the envelope estimation
#### Traffic

Let us assume we have 50 million daily active users (DAU)
- and on average each user sends at least 10 messages to 2 different people every day.
- This gives us 2 billion messages per day.

>50 million x 2 people x 10 messages = 2 billion/day


Messages can also contain media such as images, videos, or other files.
- We can assume that 5 percent of messages are media files shared by the users, which gives us additional 200 million files we would need to store.

>5 percent x 2 billion = 200 million/day


**What would be Requests Per Second (RPS) for our system?**
- 2 billion requests per day translate into 24K requests per second.

>2 billion/(24 hrs x 3600 seconds) = ~24K requests/second



#### Storage

If we assume each message on average is 100 bytes, we will require about 200 GB of database storage every day.

>2 billion x 100 bytes = ~200 GB/day


As per our requirements,
- we also know that around 5 percent of our daily messages (100 million) are media files.
- If we assume each file is 100 KB on average, we will require 10 TB of storage every day.

>100 million x 100 KB = 10 TB/day


And for 10 years, we will require about 38 PB of storage.

>(10 TB + 0. 2 TB) x 10 years x 365 days = ~38 (37,230) PB


#### Bandwidth

As our system is handling 10.2 TB of ingress every day,
- we will require a minimum bandwidth of around 120 MB per second.

>10. 2 TB / (24 hrs x 3600 seconds) = ~120 MB/second


#### High-level estimate
- Daily active users (DAU): 50 million
- Requests per second (RPS): 24K/s
- Storage (per day): ~10.2 PB
- Storage (10 years): ~38 PB
- Bandwidth: ~120MB/s

### (URL Shortner) Back of the envelope estimation


#### Traffic

- This will be a read-heavy system,
- so let's assume a 100:1 read/write ratio with 100 million links generated per month.

- For reads per month:

>100 x 100 million = 10 billion/month

- Similarly for writes:

>1 x 100 million = 100 million/month


#### What would be Requests Per Second (RPS) for our system?

- 100 million requests per month translate into 40 requests per second.

>100 million / (30 days x 24 hrs x 3600 seconds) = ~40 URLs/second

- And with a 100:1 read/write ratio, the number of redirections will be:

>100 x 40 URLs/second = 4,000 requests/second


#### Bandwidth
- Since we expect about 40 URLs every second, and if we assume each request is of size 500 bytes then the total incoming data for write requests would be:

>40 x 500 bytes = 20 KB /second

- Similarly, for the read requests, since we expect about 4K redirections, the total outgoing data would be:

>4,000 URLs/second x 500 bytes = ~2 MB/second



#### Storage (for write)

- For storage, we will assume we store each link or record in our database for 10 years.
- Since we expect around 100 million new requests every month, the total number of records we will need to store would be:

>100 million x 10 years x 12 months = 12 billion 

Like earlier, if we assume each stored record will be approximately 500 bytes. We will need around 6 TB of storage:

>12 billion x 500 bytes = 6 TB


#### Cache (for read)

- For caching, we will follow the classic [Pareto principle](https://en.wikipedia.org/wiki/Pareto_principle) also known as the 80/20 rule.
- This means that 80% of the requests are for 20% of the data, so we can cache around 20% of our requests.

Since we get around 4K read or redirection requests each second, this translates into 350M requests per day.

>4,000 URLs/second x 24 hours x 3600 seconds = ~350 million requests/day

Hence, we will need around 35GB of memory per day.

>20 percent x 350 million x 500 bytes = 35 GB/ day


#### High-level estimate

- Writes (New URLs): 40/s
- Reads (Redirection): 4K/s
- Bandwidth (Incoming): 20 KB/s
- Bandwidth (Outgoing): 2 MB/s
- Storage (10 years): 6 TB
- Memory (Caching): ~35 GB/day