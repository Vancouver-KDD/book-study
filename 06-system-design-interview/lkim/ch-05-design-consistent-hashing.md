Why - To achieve horizontal scaling, it is important to distribute requests/data efficiently and evenly across servers

## The rehashing problem
With the traditional hashing approach, for example with modular function, problems arise when new servers are added, or existing servers are removed. 
- If a server goes offline or a new server is added for scaling
- Then most keys are redistributed, not just the ones originally stored in the offline server, which means most cache clients will
connect to the wrong servers to fetch data - a storm of cache misses.

## Consistent hashing
"a special kind of hashing such that when a hash table is resized and consistent hashing is used, only k/n keys need to be remapped on average, where k is the number of keys, and n is the number of slots."

### Hash space and hash ring
ex) Assume SHA-1 is used as the hash function f, SHA-1’s hash space goes from 0 to 2^160 - 1. With the fuction, we map servers based on server IP or name onto the hash ring.

#### Server lookup
To determine which server a key is stored on, we go clockwise from the key position on the ring until a server is found

#### Adding or removing a server
It requires only a small fraction of keys require redistribution

## Two issues in the basic consistent hashing approach
1. Impossible to keep the same size of partitions on the ring for all servers, considering a server can be added or removed.
   - A partition is the hash space between adjacent servers.
2. Possible to have a non-uniform key distribution on the ring.
   - Some servers have most data, while other servers have no data.

### Virtual nodes (or Replicas) - solution
- A virtual node refers to the real node, and each server is represented by multiple virtual nodes on the ring
- As the number of virtual nodes increases, the distribution of keys becomes more balanced, with the standard deviation getting smaller
- tradeoff: more spaces are needed to store data for virtual nodes

## Find affected keys
When a server is added or removed, a fraction of data needs to be redistributed. How can we find the affected range to redistribute the keys?

### addition case 
- From the newly added server, moves anticlockwise until another server is found. Keys between the two servers need to be redistributed to the new server

### removal case 
- From the removed server position, moves anticlockwise until another server is found. Keys between the two servers need to be redistributed to the next (clockwise direction) server


## Why consistent hashing?
- Minimized keys are redistributed when servers are added or removed.
- Easy to scale horizontally because data are more evenly distributed.
- Mitigate hotspot key problem - Excessive access to a specific shard could cause server overload.

## Usages in real-world systems
- Partitioning component of Amazon’s Dynamo database
- Data partitioning across the cluster in Apache Cassandra
- Discord chat application
- Akamai content delivery network
- Maglev network load balancer
