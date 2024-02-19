# Chapter 5

## Intro
> To **achieve horizontal scaling**, it is important to distribute requests/data efficiently and evenly across servers. Consistent hashing is a commonly used technique to achieve this goal. But first, let us take an in-depth look at the problem.


## Example
### Context
*Cited numbers were totally **made up***

You work for Netflix. Your team manages storage system : store movie files, serve movie files when Frontend/App requests movie files. 

**In 2010**, Netflix had 1000 movie titles. 
```
File size of each movie is 1GB
Total file size : 1TB
```
So- You started with 1 server with 10 TB hard drive. 


**In 2011**, we have 5000 movie titles.
5TB-> Now we need to prepare for scenario you'd have more than 10000 movie titles.
```
5000 titles, in total 5TB
Hard disk capacity : 10TB
Hard disk usage : 50%
```

  - Option 1 : Purchase 40TB hard drive, replace. 
(2000$) - Vertical 
  - Option 2 : Purchase 10TB hard drive (250$) - Horizontal

We got $100M from investors, so let's use expensive hardware. 
```
5000 titles, in total 5TB
Hard disk capacity : 40TB
Hard disk usage : 12.5%
```

**In 2012**, now we have 35000 movie titles.
```
35000 titles, in total 35TB
Hard disk usage : 87.5%
```
- Option 1 : Purchase 160TB hard drive, replace (10000$)
- Option 2 : Purchase 40TB*3 hard drives (1500$*3 = 6000$) - Horizontal

Now we have decided to use multiple hard disks-but how do we know which file is in where? 


```


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
 │
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
 ├─--------------------------------------------------------------
 │
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

### Product requirement
Design a service that tells which file is in which server. 
- Non-func Requirement에 대해서도 고려. 
  - scalability 
  - no hot spot


#### Algorithm : How to distribute files? 
Assumption : 4 servers. 
1. Based on movie title

- Server 0 : Movie title starts with A-G (7)
- Server 1 : Movie title starts with H-N (7)
- Server 2 : Movie title starts with O-T (6)
- Server 3 : Movie title starts with U-Z (6)

```
<Server 0>  <Server 1>  <Server 2>  <Server 3>
A  :  82    H  :  31    O  :  43    U  :  17
B  :  77    I  :  35    P  :  43    V  :  4
C  :  60    J  :  18    Q  :  3     W  :  43
D  :  47    K  :  24    R  :  50    X  :  0
E  :  24    L  :  58    S  :  106   Y  :  9
F  :  34    M  :  83    T  :  266   Z  :  4
G  :  32    N  :  21    

356         270         511         77
29%	        22%	        42%	        6%
```
2. Hash-based 
- Server 0 : movie_id%4 == 0 
- Server 1 : movie_id%4 == 1 
- Server 2 : movie_id%4 == 2 
- Server 3 : movie_id%4 == 3 
```
<Server 0>  <Server 1>  <Server 2>  <Server 3>
304         304         303         303
25%         25%         25%         25%
```


#2 looks way better. However ~


### What happens when adding a server
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

- MD5 해시를 해야하는 이유
    - 지금 index가 1200개 밖에 없는데 언제까지 클지 모르니까. evenly distribute
- 


### Hidden cost of option 2:
We need one more server to remember in which server titles are stored. 








[] Sidetrack : Is this number makes sense? (Quote)
- 10TB, 20TB등 다양한 서비스 하는거보다 10TB*&10개 하는게 좋은 이유 : Homogeneity 



horizontal scaling이 왜 필요한가? 

DB만을 위한게 아님 - CDN에도 쓰임.
자원을 서빙하는 서비스와, 자원을 실제 홀딩하고 있는 노드에 대한 분산 문제임. 


예시 후보
DB - SIN number : partition by 
Real life - 사무실 이사
file - transfer when re-locating
    partition by file-name
    

Business requirement 

직관적이지 않다는 것이 흠. 