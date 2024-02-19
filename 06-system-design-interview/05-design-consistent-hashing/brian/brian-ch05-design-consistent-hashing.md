# CHAPTER 5: DESIGN CONSISTENT HASHING
## The rehashing problem 
- serverIndex = hash(key) % N, where N is the size of the server pool. 
![HashKey.png](HashKey.png)
- However, problems arise when new servers are added, or existing servers are removed.
- ![RmoveAServer.png](RmoveAServer.png)

## Consistent hashing
![HashRing.png](HashRing.png)
### Server lookup
![ServerLookup.png](ServerLookup.png)
### Add a server
![Add a server.png](AddAServer.png)
### Remove a server
![RemoveAServer.png](RemoveAServer.png)

### Two issues in the basic approach
1. It is impossible to keep the same size of partitions on the ring for all servers considering a server can be added or removed.
![FirstProblem.png](FirstProblem.png)
2. It is possible to have a non-uniform key distribution on the ring.
![SecondProblem.png](SecondProblem.png)

### Virtual nodes
![VirtualNodes.png](VirtualNodes.png)

### Find affected keys
- Add a server
![FindKeysAddAServer.png](FindKeysAddAServer.png)
- Remove a server
![FindKeysRmoveAServer.png](FindKeysRmoveAServer.png)

Wrap up
-  The benefits of consistent hashing include:
  - Minimized keys are redistributed when servers are added or removed.
  - It is easy to scale horizontally because data are more evenly distributed.
  - Mitigate hotspot key problem.