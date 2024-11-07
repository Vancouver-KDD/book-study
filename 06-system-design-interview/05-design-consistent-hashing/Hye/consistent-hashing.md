Chapter 5: Distribute Consistent Hashing

Functional requirements:
- Distribute requests/data efficiently and evenly across servers

Option 1: Modular Hashing
To fetch the server where the key is stored, perform modular operation
	serverIndex = hash(key) % N, N = size(server_pool)
Pro:
- Works well with fixed size server pool
- Data is distributed evenly
Con:
- Does not work well when the servers are added or removed
    - Most keys are redistributed
    - Most cache clients will connect to the wrong servers (Storm of cache misses

Option 2: Consistent Hashing
Basic Steps:
* Map servers and keys on to the ring using a uniformly distributed hash functions
* To find out which server a key is mapped to, go clockwise from the key position until the first server on the ring is found

Hash space: Assume SHA-1 is used as the hash function f, and the output rage of the hash function is: x0, x1, …., xn. Hash space goes from 0 to 2^160 - 1. X0 corresponds to 0, and xn corresponds to 2^160 - 1

Hash ring: By collecting both ends, we get a hash ring

Hash servers: Based on the hash function f, we map servers based on server IP or name onto the ring.

Hash Keys: (There is no modular operation). Cache keys are hashed onto the hash ring
To determine which server a key is stored on, we go clockwise from the key position on the ring until a server is found.

Adding a server: only requires redistribution of a fraction of keys
Removing a server: only a small fraction of keys require redistribution with consistent hashing

Two issues: 
* It’s impossible to keep the same size of partitions on the ring for all servers considering a server can be added or removed (each server can be very small or fairly large)
* It’s possible to have a non-uniform key distribution on the ring. (Some servers may not have data)

Solution: Virtual nodes
Virtual node refers to the real node, and each server is represented by multiple nodes on the ring. (Server 0 can have 3 virtual nodes)
With virtual nodes, each server is responsible for multiple partitions. 
Pro:
- As the number of virtual nodes increases, the distribution of keys becomes more balanced (The standard deviation gets smaller with more virtual nodes, leading to balanced data distribution)
Con:
- More spaces are needed to store data about virtual nodes. Tune the number of virtual nodes to fit our system requirements!

Summary:
Benefits of consistent having:
- Minimized keys are redistributed when servers are added or removed
- It is easy to scale horizontally because data are more evenly distributed
- Mitigate hotspot key problem, which refers to the excessive access to a specify shard which causes server overload.
Real-world Examples:
- Partitioning component of Amazon’s Dynamo database
- Data partitioning across the cluster in Apache Cassandra
- Discord chat application
- Akamai content delivery network
- Maglev network load balancer

