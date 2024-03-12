# Chapter 5

## Intro
> To **achieve horizontal scaling**, it is important to distribute requests/data efficiently and evenly across servers. Consistent hashing is a commonly used technique to achieve this goal. But first, let us take an in-depth look at the problem.


## TL;DR
- 수평 확장을 위해서는
    - key/데이터/트래픽의 분배가 균등해야 합니다.
        - 균등하지 않다면 확장을 할 이유가 없습니다.
    - 규모 확장/축소시 수반되는 재배치 비용은 비쌉니다. 따라서 규모 변경에 따라 이동이 되는 키의 개수가 적어야 합니다.
    - 키가 어디에 배치되어야 하는지 알려주는 로직의 결과는 deterministic 해야 합니다.
- 수평 확장을 하게되면 키가 어디로 배치되어야 하는지 알려주는 함수/모듈/서비스가 필요합니다.
    - 이를 구현하는 방법은 다양합니다. 


## Disclaimer
- Consistent hashing에 집중하기 보다는, 간략화된  수평 확장 시나리오를 제시합니다. 

## Example
### Context
*Cited numbers were totally **made up***

You work for Netflix. Your team manages storage system : store movie files, serve movie files when Frontend/App requests movie files. 

####  **In 2010**, we have 1000 movie titles. 
```
File size of each movie is 1GB
Total file size : 1000 * 1GB = 1TB
```
You started with 1 storage server with 10TB capacity. 

```
Total file size : 1TB
Storage capacity : 10TB
Storage usage : 10%
```



#### **In 2011**, we have 5000 movie titles.
```
Total file size : 5TB
Storage capacity : 10TB
Storage usage : 50%
```

  - Option 1 : Purchase 40TB Storage server, replace. 
(2000$) - Vertical 
  - Option 2 : Purchase 10TB Storage server (250$), run 2 servers


We got $1M from investors, so let's use expensive hardware. 
```
Total file size : 5TB
Storage capacity : 10TB -> 40TB
Storage usage : 50%     -> 12.5%
```

**In 2012**, now we have 35000 movie titles.
```
Total file size : 35TB
Storage capacity : 40TB
Storage usage : 87.5% (!!)
```
- Option 1 : Purchase 160TB Storage server, replace (10000$)
- Option 2 : Purchase 40TB*3 Storage servers (2000$*3 = 6000$) - Horizontal

Which option would be better?

### Go with Horizontal scaling

Now that we've decided to use multiple storage servers, how do we figure out where each file is stored?



```

 |              Option 1 : Vertical scaling
 │                                           ┌───────────┐
 │                                           │           │
 │                                           │           │
 │                                           │ movie 3842│
 │            ┌───────────┐                  │ .....     │
 │            │           │  give me movie 4 │ .....     │
 │            │   user    ├─────────────────►│ .....     │
 │            │           │                  │ movie 105 │
 │            └───────────┘                  │ movie 4   │
 │                                           │ movie 17  │
 │                                           │ movie 123 │
 │                                           └───────────┘
 │                                              (160TB)
 │
 │--------------------------------------------------------------
 |              Option 2 : Horizontal scaling (Not working)
 │
 │                                             server A (40TB)
 │                                           ┌───────────┐
 │                                           │ .....     │
 │                                           │ movie 17  │
 │                                           │ movie 123 │
 │                                           └───────────┘
 │                                             server B (40TB)
 │            ┌───────────┐                  ┌───────────┐
 │            │           │ what the heck..? │ .....     │
 │            │   user    ├───────────►      │ movie 3842│
 │            │           │                  │ movie 105 │
 │            └───────────┘                  └───────────┘
 │                                             server C (40TB)
 │                                           ┌───────────┐
 │                                           │ .....     │
 │                                           │ .....     │
 │                                           │ movie 4   │
 │                                           └───────────┘
 │
 │---------------------------------------------------------------
 |              Option 2 : Horizontal scaling + Routing service
 |
 │                                             server A (40TB)
 │                                           ┌───────────┐
 │                                           │ .....     │
 │                                           │ movie 17  │
 │                                           │ movie 123 │
 │                                           └───────────┘
 │                                             server B (40TB)
 │            ┌───────────┐                  ┌───────────┐
 │            │           │ 3. gimme movie 4 │ .....     │
 │            │   user    ├─────────────┐    │ movie 3842│
 │            │           │             │    │ movie 105 │
 │            └──┬────────┘             │    └───────────┘
 │               │    ▲                 │      server C (40TB)
 │    1.where's  │    │ 2. goto         │    ┌───────────┐
 │      movie 4  │    │ server C        │    │ .....     │
 │      located? │    │                 └───►│ .....     │
 │               ▼    │                      │ movie 4   │
 │            ┌───────┴───┐                  └───────────┘
 │            │ new       │
 │            │ service   │
 │            └───────────┘
 │

```
## Implementing 'Routing service'
### Algorithm: Deciding which node stores which file

```
Assumption:
We managed with a single storage server, now we have 4 servers.
We have a collection of 1214 movie titles all launched in 2023.

Goal: distribute the storage burden across these four servers.
```
1. Partitioning based on movie title (Key-ranged partitioning)

    - Server 0 : Movie title starts with A-G (7)
    - Server 1 : Movie title starts with H-N (7)
    - Server 2 : Movie title starts with O-T (6)
    - Server 3 : Movie title starts with U-Z (6)

    ```
    Result

    <Server 0>  <Server 1>  <Server 2>  <Server 3>
    A  :  82    H  :  31    O  :  43    U  :  17
    B  :  77    I  :  35    P  :  43    V  :  4
    C  :  60    J  :  18    Q  :  3     W  :  43
    D  :  47    K  :  24    R  :  50    X  :  0
    E  :  24    L  :  58    S  :  106   Y  :  9
    F  :  34    M  :  83    T  :  266   Z  :  4
    G  :  32    N  :  21    

    356         270         511         77
    29%	      22%	      42%	      6%
                            Skewed 
                            (Hotspot)
    ```
    Quiz : Why we have a spike on **T**? How can we evenly distribute these workload?
    


2. Hash-based partitioning

    - Server 0 : movie_id%4 == 0 
    - Server 1 : movie_id%4 == 1 
    - Server 2 : movie_id%4 == 2 
    - Server 3 : movie_id%4 == 3 
    
    ```
    Result

    <Server 0>  <Server 1>  <Server 2>  <Server 3>
    304         304         303         303
    25%         25%         25%         25%
    ```

    (Note : Not exactly **hash-based** tho)



### Re-partitioning : What happens when adding a server
1. Movie title based
```
<Server 0>  <Server 1>  <Server 2>  <Server 3>
A  :  82    H  :  31    O  :  43    U  :  17
B  :  77    I  :  35    P  :  43    >V :  4
C  :  60    J  :  18    >Q :  3     >W :  43
D  :  47    K  :  24    >R :  50    >X :  0
E  :  24    >L :  58    >S :  106   >Y :  9
F  :  34    >M :  83    >T :  266   >Z :  4
>G  :  32   >N :  21    

356         270         511         77
29%	        22%	        42%	        6%

-----------------------------------------------------------
<Server 0>  <Server 1>  <Server 2>  <Server 3>  <Server 4>
A  :  82    >G :  32    >L :  58    >Q :  3     >V :  4
B  :  77    H  :  31    >M :  83    >R :  50    >W :  43
C  :  60    I  :  35    >N :  21    >S :  106   >X :  0
D  :  47    J  :  18    O  :  43    >T :  266   >Y :  9
E  :  24    K  :  24    P  :  43    U  :  17    >Z :  4
F  :  34
324	        140     	248     	442     	60
27%     	12%     	20%     	36%     	5%

Server 0->1 : 32
Server 1->2 : 162
Server 2->3 : 266
Server 3->4 : 60

679 out of 1214 moved. (56%)
```

2. Hash-based
```
<Server 0>  <Server 1>  <Server 2>  <Server 3>
304         304         303         303
25%         25%         25%         25%

<Server 0>  <Server 1>  <Server 2>  <Server 3>  <Server 4>
243         243         243         243         242
20%         20%         20%         20%         20%

971 out of 1214 moved. (80%)
```

### What happens if...
- When one server goes down?
- We haven't considered Read traffic. What if all popular movie are in Server 3- and the server 3 is about to explode? - Hotspot issue 

### Conclusion
- Each strategy has its pros / cons. 
  - It is crucial to select one that aligns with your data/query patterns. 
  - You need to consider the trade-offs


## Consistent hashing
### Phase 1 : Put hashed value on the hash ring
```
movie_titles = ["Oppenheimer", "Barbie", "Fast X","Past Lives"]
------O-F---------------------------------------------B-P-------------

```

### Phase 2 : Put servers  on the hash ring
```
servers = ["100.20.30.10","100.20.30.20","100.20.30.30","100.20.30.40"]
----------------------3-----------------1--------2------------0-------

```

### Phase 3: Using this 'hash ring' as 'router'
```

Where's Oppenheimer?

------O-F---------------------------------------------B-P-------------
      ................>
----------------------3-----------------1--------2------------0-------


It's on the server 3! 
```



### Expansion : As we have more server, distribution gets even. 
```
--------O---F-----------------------------------------------------------------B-P-------------------
---KK---O---F-M----A--------------A-----A-M-A----T---------B--------------T-R-BCP-------H-----M--S-I
------------2-----------------------0--------------------3------------------------------1-----------
-8-4--------2--------------6---7----0--------------------3-----9------5-----------------1-----------

line 1 : 4 movies
line 2 : 25 movies
line 3 : 4 servers
line 4 : 10 servers

```







```python
import hashlib

def md5_numeric_representation(content: str) -> int:
    md5_hash = hashlib.md5(content.encode()).hexdigest()
    numeric_representation = int(md5_hash, 16)
    return numeric_representation

movie_titles = ["Oppenheimer", "Barbie", "Fast X","Past Lives"]

# movie_titles = ["Oppenheimer","Barbie","Fast X","Past Lives","Air","Killers of the Flower Moon","Knock at the Cabin","The Holdovers","Cocaine Bear","Scream 6","The Little Mermaid","A Haunting in Venice","Anatomy of a Fall","Asteroid City","Creed III","Ferrari","House Party","Infinity Pool","M3GAN","May December","Monster","Renfield","Theater Camp","BlackBerry"]

servers = ["100.20.30.10","100.20.30.20","100.20.30.30","100.20.30.40"]
# servers = ["100.20.30."+str(5*fourth_mask) for fourth_mask in range(1, 11)]


str_len = 150
movie_str_list = ['-'] * str_len
server_str_list = ['-'] * str_len


movie_hash = []
server_hash = []
for movie_title in movie_titles:
    movie_hash.append((movie_title, md5_numeric_representation(movie_title)))
for idx, server in enumerate(servers):
    server_hash.append((idx, md5_numeric_representation(server)))

print(movie_hash)
print(server_hash)
# modify str
for title, hashval in movie_hash:
    movie_str_list[int(hashval/(2**128)*str_len)] = title[0]

for idx, hashval in server_hash:
    server_str_list[int(hashval/(2**128)*str_len)] = str(idx)


print(''.join(movie_str_list))
print(''.join(server_str_list))
```


