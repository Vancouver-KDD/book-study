# Chapter 7: Design A Unique ID Generator in Distributed System

## Step 1 - Understand the problem and establish design scope
- IDs must be unique.
- IDs are numerical values only.
- IDs fit into 64-bit.
- IDs are ordered by date.
- Ability to generate over 10,000 unique IDs per second.

## Step 2 - Propose high-level design and get buy-in
- Multi-master replication
- Universally unique identifier (UUID)
- Ticket server
- Twitter snowflake approach
Let us look at each of them, how they

### Multi-master replication
Major drawbacks:
- Hard to scale with multiple data centers
- IDs do not go up with time across multiple servers.
- It does not scale well when a server is added or removed.

### UUID
Pros:
- Generating UUID is simple. No coordination between servers is needed so there will not
be any synchronization issues.
- The system is easy to scale because each web server is responsible for generating IDs
they consume. ID generator can easily scale with web servers.
Cons:
- IDs are 128 bits long, but our requirement is 64 bits.
- IDs do not go up with time.
- IDs could be non-numeric.

### Ticket Server
Pros:
- Numeric IDs.
- It is easy to implement, and it works for small to medium-scale applications.
Cons:
- Single point of failure. Single ticket server means if the ticket server goes down, all
systems that depend on it will face issues. To avoid a single point of failure, we can set up
multiple ticket servers. However, this will introduce new challenges such as data
synchronization.

### Twitter snowflake approach
- Sign bit: 1 bit. It will always be 0. This is reserved for future uses. It can potentially be
used to distinguish between signed and unsigned numbers.
- Timestamp: 41 bits. Milliseconds since the epoch or custom epoch. We use Twitter
snowflake default epoch 1288834974657, equivalent to Nov 04, 2010, 01:42:54 UTC.
- Datacenter ID: 5 bits, which gives us 2 ^ 5 = 32 datacenters.
- Machine ID: 5 bits, which gives us 2 ^ 5 = 32 machines per datacenter.
- Sequence number: 12 bits. For every ID generated on that machine/process, the sequence
number is incremented by 1. The number is reset to 0 every millisecond.

## Step 3 - Design deep dive
### Timestamp
The most important 41 bits make up the timestamp section. As timestamps grow with time, IDs are sortable by time.
### Sequence number
Sequence number is 12 bits, which give us 2 ^ 12 = 4096 combinations. This field is 0 unless
more than one ID is generated in a millisecond on the same server. In theory, a machine can
support a maximum of 4096 new IDs per millisecond.

## Step 4 - Wrap up
- Clock synchronization. In our design, we assume ID generation servers have the same
clock. This assumption might not be true when a server is running on multiple cores. The
same challenge exists in multi-machine scenarios. Solutions to clock synchronization are
out of the scope of this book; however, it is important to understand the problem exists.
Network Time Protocol is the most popular solution to this problem. For interested
readers, refer to the reference material [4].
- Section length tuning. For example, fewer sequence numbers but more timestamp bits
are effective for low concurrency and long-term applications.
- High availability. Since an ID generator is a mission-critical system, it must be highly
available.