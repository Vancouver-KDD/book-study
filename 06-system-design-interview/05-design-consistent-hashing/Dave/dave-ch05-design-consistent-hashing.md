# CHAPTER 5: DESIGN CONSISTENT HASHING
To achieve horizontal scaling, it is important to distribute requests/data efficiently and evenly across servers. 

### The rehashing problem
If you have n  cache servers, a common way to balance the load is to use the following hash method:
> serverIndex = hash(key) % N, where N is the size of the server pool.

Let us use an example to illustrate how it works.
As shown in Table 5-1, we have 4 servers and 8 string keys with their hashes.
![t5-1](Image/tb5-1.jpg)
> To fetch the server where a key is stored, we perform the modular operation f(key) % 4.
![fg5-1](Image/fg5-1.jpg)

```
This approach works well when the size of the server pool is fixed, and the data distribution is even.
```
> However, problems arise when new servers are added, or existing servers are removed.
```
For example, if server 1 goes offline, the size of the server pool becomes 3.
Using the same hash function, we get the same hash value for a key.
But applying modular operation gives us different server indexes because the number of servers is reduced by 1.
We get the results as shown in Table 5-2 by applying hash % 3:
```
![t5-2](Image/tb5-2.jpg)
![fg5-2](Image/fg5-2.jpg)
```
Most keys are redistributed, not just the ones originally stored in the offline server (server 1).
This means that when server 1 goes offline, most cache clients will connect to the wrong servers to fetch data.
This causes a storm of cache misses.
```
> 🔥**Consistent hashing is an effective technique to mitigate this problem.**

## Consistent hashing
### Hash space and hash ring
```
Now we understand the definition of consistent hashing, let us find out how it works.
Assume SHA-1 is used as the hash function f, and the output range of the hash function is: x0, x1, x2, x3, …, xn.
In cryptography, SHA-1’s hash space goes from 0 to 2^160 - 1.
That means
  • x0 -> 0
  • xn -> 2^160 – 1
  • all the other hash values in the middle fall
```
By collecting both ends, we get a hash ring as shown in Figure 5-4:
![fg5-4](Image/fg5-4.jpg)

### Hash servers
