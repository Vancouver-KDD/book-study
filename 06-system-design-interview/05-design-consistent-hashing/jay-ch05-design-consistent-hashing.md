# CHAPTER 5: DESIGN CONSISTENT HASHING

- technique to distribute requests/data efficiently and evenly across servers

## The rehashing problem

- serverIndex = hash(key) % N, where N is the size of the server pool
- hash(key0) % 4 = 1 means a client must contact server 1 to fetch the cached data when there are 4 servers available
- this only works when the server pool is fixed and data distribution is even
- when a new server is added or one is removed, server indexes are messed up causing a cache miss
- to mitigate this, use Consistent hashing: when there is change, only k/n keys need to be remapped on average, where k is the number of keys, and n is the number of slots

## Hash space and hash ring

- store all hash values between x0 and xn (0 ~ 2^160)
- connecting both ends, create a ring
- map server ips onto the ring
- also map hash keys onto the ring
- to determine which server a key is stored on, we go clockwise of a key to find out

## Add and removing a server

- depending on which position a server is stored, an existing key that stored onto an existing server can be not redistributed to be stored on the new server if it is positioned in between
- when a server is removed, multiple keys are stored on a server since it is the first server found going clockwise

## Challenges

- impossible to keep the same size of partitions (hash spaces between adjacent servers) on the ring for all servers (when a new server is removed/added)
  - the space each server gets in a ring can become uneven
- a non-uniform key distribution on the ring

  - one server having multiple keys and others have one
  - virtual node to solve this
  - each server is represented by multiple virtual nodes
  - instead of servers on the ring, we have keys and virtual nodes and partitions (edges) where key and nodes meet represent server
  - As the number of virtual nodes increases, distribution is more balanced due to decreased standard deviation.

- how to find affected keys
  - start from added/removed server, go anticlock and find the closest server, the keys within the range needs redistribution
