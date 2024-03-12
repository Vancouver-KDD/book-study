# Chapter 5: Design Consistent Hashing

To achieve horizontal scaling, it is important to distribute requests/data efficiently and evenly across servers. Consistent hashing is commonly used technique to achieve this goal.

## The Rehashing Problem

- If you have n cache servers, a common way to balance the load is to use the following hash method:
    - serverIndex = hash(key) % N, where N is the size of the server pool

- To fetch the server where a key is stored, we perform the modular operation f(key) % 4
    - ex: hash(key0) % 4 = 1 means a client must contact server 1 to fetch the cached data

- works well when the size of the server pool is fixed, and the data distribution is even. 
- However, when new servers are added, or existing servers are removed, problems arise.
    - ex: if server 1 goes offline, she size of the server pool becomes 3. Using the same hash function, we get the same hash value for a key. But applying modular operation gives us different server indexes because the number of servers is reduced by 1. 
    - most keys are redistributed, not just the ones originally stored in the offline server (server 1). This means that when server 1 goes offline, most cache clients will connect to the wrong servers to fetch data. This causes a storm of cache misses. Consistent hashing is an effective technique to mitigate this problem

## Consistent Hashing

- "Consistent hashing is a special kind of hashing such that when a hash table is re-sized and consistent hashing is used, only k/n keys need to be remapped on average, where k is the number of keys, and n is the number of slots. In contrast, in most traditional hash tables, a change in the number of array slots causes nearly all keys to be remapped".

### Hash space and hash ring
- Looking at the example/ explanation in the book helps understanding.

- Benefits of consistent hashing:
    - Minimized keys are redistributed when servers are added or removed
    - It is easy to scale horizontally because data are more evenly distributed
    - Migitate hotspot hey problem. Excessive access to a specific shard could cause server overload. Consistent hashing helps to mitigate the problem by distributing the data more evenly. 

- Consistent hashing in real-world systems:
    - Partitioning component of AWS Dynamo DB
    - Data partitioning across the cluster in Apache Cassandra
    - Discord chat application
    - Akamai content delivery network
    - Maglev network load balancer