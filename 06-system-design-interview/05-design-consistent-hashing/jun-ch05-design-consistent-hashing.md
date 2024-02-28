# CHAPTER 5: DESIGN CONSISTENT HASHING
To distribute requests/data efficiently and evenly across servers. 

## The rehashing problem

a common way to balance the load is to use the following hash method:
n: number of cache servers,
serverIndex = hash(key) % N, where N is the size of the server pool

![Table 5-1](./img/table5-1.jpg)

hash(key0) % 4 = 1 means a client must contact server 1 to fetch the cached data

![Figure 5-1](./img/figure5-1.jpg)

if server 1 goes offline, the size of the server pool becomes 3. Using
the same hash function, we get the same hash value for a key. But applying modular
operation gives us different server indexes because the number of servers is reduced by 1.

![Table 5-2](./img/table5-2.jpg)
![Figure 5-2](./img/figure5-2.jpg)

when server 1 goes offline, most cache clients will connect to the wrong servers to fetch data. This causes a storm of cache misses. **Consistent hashing** is an effective technique to mitigate this problem.

### Consistent hashing
Consistent hashing is a special kind of hashing such that when a
hash table is re-sized and consistent hashing is used, only k/n keys need to be remapped on
average, where k is the number of keys, and n is the number of slots. In contrast, in most
traditional hash tables, a change in the number of array slots causes nearly all keys to be
remapped.

#### Hash space and hash ring
how it works. Assume SHA-1 is used as the hash function f, and the output range of the hash function is: x0, x1, x2,
x3, ..., xn. SHA-1’s hash space goes from 0 to 2^160 - 1. That means x0 = 0, xn = 2^160 – 1, and all the other hash values (x1,x2 ...) in the middle fall between 0 and 2^160 - 1.

![Figure 5-3&4](./img/figure5-3&4.jpg)

#### Hash servers
We map servers based on server IP or name onto the ring.
![Figure 5-5](./img/figure5-5.jpg)

#### Hash keys
Cache keys are hashed onto the hash ring
![Figure 5-6](./img/figure5-6.jpg)

#### Server lookup
To determine which server a key is stored on, we go clockwise from the key position on the ring until a server is found.
![Figure 5-7](./img/figure5-7.jpg)

#### Add a server

![Figure 5-8](./img/figure5-8.jpg)
key0 is stored on server 0. Now, key0 will be stored on server 4 because server 4 is the first
server it encounters by going clockwise from key0’s position on the ring. The other keys are
not redistributed based on consistent hashing algorithm.

#### Remove a server

![Figure 5-9](./img/figure5-9.jpg)
When server 1 is removed, only key1 must be remapped to server 2. The rest of the keys are unaffected.

#### Two issues in the basic approach
Basic steps:
1. Map servers and keys on to the ring using a uniformly distributed hash function.
2. To find out which server a key is mapped to, go clockwise from the key position until the first server on the ring is found.

problems: 
1. It is possible that the size of the partitions on the ring assigned to each server is very small or fairly large considering a server can be added or removed.

![Figure 5-10](./img/figure5-10.jpg)
If s1 is removed, s2’s partition is twice as large as s0 and s3’s partition.

2. It is possible to have a **non-uniform key distribution** on the ring.

![Figure 5-11](./img/figure5-11.jpg)
If servers are mapped to positions listed in Figure 5-11 above, Most of the keys are stored on server 2. However, server 1 and server 3 have no data. **virtual nodes** will solve these problems

#### Virtual nodes
A virtual node refers to the real node, and each server is represented by multiple virtual nodes on the ring.

![Figure 5-12](./img/figure5-12.jpg)
Both server 0 and server 1 have 3 virtual nodes. Instead of using s0, we have s0_0, s0_1, and s0_2 to represent server 0 on the ring. Similarly, s1_0, s1_1, and s1_2 represent server 1 on the ring. With virtual nodes, each server is responsible for multiple partitions. Partitions (edges) with label s0 are managed by server 0. On the other hand, partitions with label s1 are managed by server 1.

![Figure 5-13](./img/figure5-13.jpg)
To find which server a key is stored on, we go clockwise from the key’s location and find the **first virtual node** encountered on the ring.

As the number of virtual nodes increases, the distribution of keys becomes more balanced. Standard deviation measures how data are spread out. The standard deviation is between 5% (200 virtual nodes) and 10% (100 virtual nodes) of the mean. The standard deviation will be smaller when we increase the number of virtual nodes. However, more spaces are needed to store data about virtual nodes.

#### Find affected keys
When a server is added or removed, a fraction of data needs to be redistributed. How can we find the **affected range** to redistribute the keys?

![Figure 5-14](./img/figure5-14.jpg)
server 4 is added onto the ring. The affected range starts from s4 (newly added node) and moves anticlockwise around the ring until a server is found (s3). Thus, keys located between s3 and s4 need to be redistributed to s4.

![Figure 5-15](./img/figure5-15.jpg)
When a server (s1) is removed, the affected range starts from s1 (removed node) and moves anticlockwise around the ring until a server is found (s0). Thus, keys located between s0 and s1 must be redistributed to s2.
