# Chapter 5

## Intro
> To **achieve horizontal scaling**, it is important to distribute requests/data efficiently and evenly across servers. Consistent hashing is a commonly used technique to achieve this goal. But first, let us take an in-depth look at the problem.

## Disclaimer
- I won't focus on consistent hashing, rather.. 

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


We got $100M from investors, so let's use expensive hardware. 
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

Now that we've decided to use multiple hard disks, how do we figure out where each file is stored?



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
 |              Option 2 : Horizontal scaling 
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

## Product requirement


#### Algorithm : How to distribute files? 
Assumption : 4 servers. 
1. Based on movie title (Key-ranged based)

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
                        Skewed (Hotspot)
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

### What happens if...
- When one server goes down
- When # of node changes, we need to re-distribute most of keys. 





## Consistent hashing
- Pre-requsite : What is **hash**
- Conclusion first                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      

- MD5 해시를 해야하는 이유
    - 지금 index가 1200개 밖에 없는데 언제까지 클지 모르니까. evenly distribute
- 


# Library analogy
- hashing을 설명하기 위해
- 도서관에서 책 찾는 법 (컴퓨터 없이!)
    - 책 분야에 따라 층 수 먼저 결정. 
    - 자기계발, 과학/공학, 소설만 있다고 칩시다.
        - 당연한 예 아닌가요?
            - 모든 책을 종류 분류 없이 가나다순으로 꽂아둘수도 있죠.
            - 모든 책을 출간 연도 순으로 꽂아둘수도 있습니다. 
            - 그게 '유용할까요?'




# Consistent hashing
Phase 1 : MD5(val) 
    


- 이 책 어디다 둘까요? == 이 책 어디에 꽂혀 있나요? 는 같은 말입니다.


### Hidden cost of option 2:
We need one more server to remember in which server titles are stored. 




### Note
1. Key-based partitioning 도 장단이 많이 있음. 
Consistent: 나중에 나올 Eventual consistency





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