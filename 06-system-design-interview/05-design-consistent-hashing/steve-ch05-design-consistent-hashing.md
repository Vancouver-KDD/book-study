# Chapter 5: Design Consistent Hashing

## The Reshaping Problem

- When the size of the server pool changes, the traditional hash method (`serverIndex = hash(key) % N`) causes significant key redistribution.
- This creates challenges when new servers are added or existing ones are removed.

## Consistent Hashing

- Consistent hashing ensures that only a fraction of keys need to be remapped when the number of servers changes.
- It utilizes a hash ring to distribute keys and servers evenly.

## Hash Space and Hash Ring

- The output range of the hash function forms a hash ring, representing the entire hash space.
- Servers and keys are mapped onto this ring using the hash function.

## Hash Servers

- Servers are mapped onto the hash ring based on their IP or name.

## Hash Keys

- Cache keys are hashed onto the hash ring without using modular operations.

## Server Lookup

- To determine which server a key is stored on, we traverse clockwise from the key's position until a server is found.

## Adding a Server

- Adding a new server requires redistributing only a fraction of keys that fall within the affected range.

## Removing a Server

- Removing a server also necessitates redistributing only the keys within the affected range.

## Two Issues in the Basic Approach

- The basic consistent hashing approach may lead to uneven partition sizes and non-uniform key distribution.

## Virtual Nodes

- Virtual nodes address these issues by representing each server with multiple virtual nodes on the hash ring.
- This leads to more balanced data distribution and minimizes key redistribution during server changes.

## Finding Affected Keys

- When a server is added or removed, the affected range of keys can be determined by traversing the hash ring.

## Wrap Up

- Consistent hashing offers several benefits, including minimized key redistribution, easier horizontal scaling, and mitigation of hot-spot key issues.
- Real-world systems such as Amazon's Dynamo database, Apache Cassandra, Discord, and Akamai content delivery network utilize consistent hashing for efficient data partitioning and load balancing.