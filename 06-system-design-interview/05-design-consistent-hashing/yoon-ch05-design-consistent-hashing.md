# 5. Design Consistent Hashing

**The rehashing problem**

If you have _n_ cache servers, a common way to balance the load is to use the following hash method:

_serverIndex = hash(key) % N_, where _N_ is the size of the server pool.

A problem occurs when the pool of servers is scaled up or down. As a result nearly every record needs to be rehashed to assign a new server in the pool. In a scenario where this happens often, the re-hashing process causes cache clients to connect to the wrong servers and a storm of cache misses.

**Consistent Hashing** is a special kind of hashing such that when a hash table is re-sized and consistent hashing is used only k/n keys need to be remapped (on average), where k is the number of keys and n is the number of slots.

**1) Hash space and hash ring**

Assume SHA-1 is used as the hash function f, and the output range of the hash function is : x0, x1, x2, ... xn

- In cryptography, SHA-1's hash scope : 0 to 2^160 - 1
  - x0 -> 0
  - xn -> 2^160 - 1
  - All the other hash values in the middle fall between 0 to 2^160

**2) Hash servers**

- Using the same hash function f, we map servers based on the IP or name onto the ring

**3) Hash keys**

- The hash function used here is different from the one in "the rehashing problem"
- No modular operation.
- Cache keys (key0, key1, key2, key3) are hashed onto the hash ring.

**4) Sever lookup**

- To determine which server a key is stored on, we go clockwise from the key position on the ring until a server is found

**5) Add a server**

- Adding a new server will only require redistribution of a fraction of keys

**6) Remove a server**

- When a server is removed, only a small fraction of keys require redistribution with consistent hashing-

### **Two issues in the basic approach**

- The consistent hashing algorithm was introduced by Karger et al. at MIT.

**The basic steps**

- Map servers and keys on the ring using a uniformly distributed hash function.
- To find out which server a key is mapped to, go clockwise from the key position until the first server on the ring is found
- **Two problems:**
  - It is impossible to keep the same size of partitions on the ring for all servers considering a server can be added ore removed.
  - A partition is the hash space between adjacent servers
  - It is possible the size of the partitions on the ring assigned to each server is very small or fairly large

**7) Virtual nodes**

- A virtual node refers to the real node

- Each server is represented by multiple virtual nodes on the ring

- To find which server a key is stored on, we go clockwise from the key's location and find the first virtual node encountered on the ring- To find out which server k0 is stored on, we go clockwise from k0's location and find virtual node s1_1, which refers to server.

**8) Find affected keys**

- When a server is added or removed, a fraction of data needs to be redistributed. - How can we find the affected range to redistributed the keys?
- **A Server is added**- server4 is added onto the ring- The affected range start from s4 and moves anticlockwise around the ring a server is found (s3)- Thus, keys located between s3 and s4 need to be redistributed to s4
- **A Server is removed**- When a server(s1) is removed, the affected ranges starts from s1 (removed node) and moves anticlockwise around the ring until a server is found(s0)- Keys located between s0 and s1 must be redistributed to s2

## **Wrap up**

- The benefits of consistent hashing include
  - Minimized keys are redistributed when servers are added or removed
  - It's easy to scale horizontally because data are more evenly distributed
  - Mitigate hotspot key problem
  - Consistent hashing in Real-world systems
- Consistent hashing is widely used in real world systems:
  - Partitioning component of Amazon's Dynamo database
  - Data partitioning across the cluster in Apache Cassandra
  - Discord data application
  - Akamai content delivery network
  - Maglev network load balancer
