# CHAPTER 1 - Reliable, Scalable, and Maintainable Applications

Many applications today are data-intensive, as opposed to compute-intensive. 
A data-intensive application is typically built from standard building blocks that provide commonly needed functionality. For example, many applications need to:
```
•  Store data so that they, or another application, can find it again later (databases)
•  Remember the result of an expensive operation, to speed up reads (caches)
•  Allow users to search data by keyword or filter it in various ways (search indexes)
•  Send a message to another process, to be handled asynchronously (stream processing)
•  Periodically crunch a large amount of accumulated data (batch processing)
```

Let's explore the fundamentals of what we are trying to achieve: reliable, scalable, and maintainable data systems.
### Reliability
```
The system should continue to work correctly (performing the correct function at the desired level of performance)
even in the face of adversity (hardware or software faults, and even human error)
```
### Scalability
```
As the system grows (in data volume, traffic volume, or complexity), there should be reasonable ways of dealing with that growth.
```
### Maintainability
```
Over time, many different people will work on the system
(engineering and operations, both maintaining current behavior and adapting the system to new use cases),
and they should all be able to work on it productively.
```

## 1. Reliability
> “continuing to work correctly, even when things go wrong.“
A fault is usually defined as one component of the system deviating from its spec, whereas a failure is when the system as a whole stops providing the required service to the user.
It is usually best to design fault-tolerance mechanisms that prevent faults from causing failures. 

### Hardware Faults
Hard disks are reported as having a mean time to failure (MTTF) of about 10 to 50 years.
Thus, on a storage cluster with 10,000 disks, we should expect on average one disk to die per day.

> - Redundancy to the individual hardware components
```
Disks may be set up in a RAID configuration, servers may have dual power supplies and hot-swappable CPUs,
and datacenters may have batteries and diesel generators for backup power.
```
> - Multi-machine redundancy
```
Manage by placing virtual machine instances on multi-machine
Designed to prioritize flexibility and resilience over individual machine reliability
```

As data volumes and applications’ computing demands have increased, more applications have begun using larger numbers of machines, which proportionally increases the rate of hardware faults. 

### Software Errors

We usually think of hardware faults as being random and independent from each other: one machine’s disk failing does not imply that another machine’s disk is going to fail. 

```
• A software bug that causes every instance of an application server to crash when given a particular bad input. For example, consider the leap second on June 30, 
2012, that caused many applications to hang simultaneously due to a bug in the Linux kernel.
•  A runaway process that uses up some shared resource—CPU time, memory, disk space, or network bandwidth.
•  A service that the system depends on that slows down, becomes unresponsive, or starts returning corrupted responses.
•  Cascading failures, where a small fault in one component triggers a fault in another component, which in turn triggers further faults.
```
There is no quick solution to the problem of systematic faults in software. Lots of small things can help: carefully thinking about assumptions and interactions in the 
system; thorough testing; process isolation; allowing processes to crash and restart; measuring, monitoring, and analyzing system behavior in production. 


### Human Errors
How do we make our systems reliable, in spite of unreliable humans?
```
• Design systems in a way that minimizes opportunities for error.
• Decouple the places where people make the most mistakes from the places where they can cause failures.
• Test thoroughly at all levels, from unit tests to whole-system integration tests and manual tests.
• Allow quick and easy recovery from human errors, to minimize the impact in the case of a failure.
• Set up detailed and clear monitoring, such as performance metrics and error rates. In other engineering disciplines this is referred to as telemetry.
```

## 2. Scalability
Scalability is the term we use to describe a system’s ability to cope with increased load. 
If the system grows in a particular way, what are our options for coping with the growth?” and “How can we add computing resources to handle the additional load?

### Describing Load
- Load can be described with a few numbers which we call load parameters.

 The best choice of parameters depends on the architecture of your system
 
```
- requests per second to a web server
- the ratio of reads to writes in a database
- the number of simultaneously active users in a chat room
- the hit rate on a cache
```


### Describing Performance
Once you have described the load on your system, you can investigate what happens when the load increases.
```
•  When you increase a load parameter and keep the system resources (CPU, memory, network bandwidth, etc.) unchanged,
      how is the performance of your system affected?
•  When you increase a load parameter, how much do you need to increase the resources if you want to keep performance unchanged?
```


```
Batch processing system(Hadoop) - throughput처리량: the number of records we can process per second, or the total time it takes to run a job on a dataset of a certain size
Online systems - 응답 시간(response time)
```

```
응답 시간의 경우, 백그라운드 프로세스의 컨택스트 스위치, 네트워크 패킷 손실과 TCP 재전송, 가비지 컬렉션 휴지, 디스크에서 읽기를 강제하는 페이지 폴트, 서버 랙의 기계적인 진동 등의 다른 여러 원인으로 추가 지연이 생길 수 있다.
보고된 서비스 응답 시간을 "평균"으로 살피는 것은 일반적이지만, 백분위를 사용하는 편이 더 좋다. 
사용자가 보통 얼마나 오랫동안 기다려야 하는지 알고 싶다면 중앙값이 좋은 지표다. 50분위로서 p50으로 축약할 수 있다.
특이 값이 얼마나 좋지 않은지 알아보려면 상위 백분위를 살펴보는 것도 좋다. 이때 사용하는 백분위는 95분위, 99분위, 99.99분위가 일반적이다.
해당 응답시간들은 꼬리 지연 시간이다. 요구사항을 어디까지 정할지는 서비스와 상황에 따라 다르다.
백분위는 서비스 수준 목표(service level objective, SLO)와 서비스 수준 협약서(service level agreement, SLA)에 자주 사용하고 기대 성능과 서비스 가용성을 정의하는 계약서에도 자주 등장한다.
그 중 큐 대기 지연은 높은 백분위에서 응답 시간의 상당 부분을 차지한다.
서버가 한번에 처리할 수 있는 요청이 제한되므로, 소수의 느린 요청의 처리만으로도 후속 요청 처리가 지체된다.
이 현상을 선두 차단이라고 부른다. 따라서 시스템의 확장성을 테스트하려고 인위적으로 부하를 생성하는 경우, 응답시간과 독립적으로 요청을 지속적으로 보내야 한다.
```

### Approaches for Coping with Load
How do we maintain good performance even when our load parameters increase by some amount?
```
용량 확장(Scaling up, = 수직 확장(vertical scaling)): More powerful equipment, simple but very expensive
규모 확장(Scaling out, = 수평 확장(horizontal scaling)): Distributing the load across multiple smaller machines
    일부 시스템은 탄력적(부하 증가 시 자동으로 컴퓨팅 자원을 추가)이다. 하지만 수동으로 확장하는 시스템이 더 간단하고 운영상 예상치 못한 일이 더 적다.
```

```
최근까지의 통념은 확장 비용이나 데이터 베이스를 분산으로 만들어야하는 고가용성 요구가 있기 전까지는 단일 노드에 데이터베이스를 유지하는 것이다. 
단일 노드에 상태 유지 데이터 시스템을 분산 설치하는 일은 아주 많은 복잡도가 추가적으로 발생하기 때문이다.
하지만 점점 분산 시스템을 위한 도구와 추상화가 좋아지면서 일부 애플리케이션에서는 바뀌었다. 
대용량 데이터와 트래픽을 다루지 않는 사용 사례에서도 분산 데이터 시스템이 향후 기본 아키텍쳐로 자리잡을 가능성이 있다.
확장성을 갖춘 아키텍쳐들은 특정 애플리케이션에 특화되어 있지만, 이런 아키텍처는 보통 익숙한 패턴으로 나열된 범용적인 구성 요소로 구축한다. 
```

## 3. Maintainability
소프트웨어 시스템 설계 원칙

```
Operability: Make it easy for operations teams to keep the system running smoothly. 
Simplicity : Make it easy for new engineers to understand the system, by removing as much complexity as possible from the system. 
Evolvability : Make it easy for engineers to make changes to the system in the future, adapting it for unanticipated use cases as requirements change. 
```

### Operability: Making Life Easy for Operations
A good operations team typically is responsible for the following, and more.

```
•  Monitoring the health of the system and quickly restoring service if it goes into a bad state
•  Tracking down the cause of problems, such as system failures or degraded performance
•  Keeping software and platforms up to date, including security patches
•  Keeping tabs on how different systems affect each other, so that a problematic change can be avoided before it causes damage
•  Anticipating future problems and solving them before they occur
•  Establishing good practices and tools for deployment, configuration management, and more
•  Performing complex maintenance tasks, such as moving an application from one platform to another
•  Maintaining the security of the system as configuration changes are made
•  Defining processes that make operations predictable and help keep the production environment stable
•  Preserving the organization’s knowledge about the system, even as individual people come and go
```

Good operability means making routine tasks easy, allowing the operations team to focus their efforts on high-value activities. Data systems can do various things to make routine tasks easy, including:
```
•  Providing visibility into the runtime behavior and internals of the system, with good monitoring
•  Providing good support for automation and integration with standard tools
• Avoiding dependency on individual machines (allowing machines to be taken down for maintenance while the system as a whole continues running uninterrupted)
•  Providing good documentation and an easy-to-understand operational model (“If I do X, Y will happen”)
•  Providing good default behavior, but also giving administrators the freedom to override defaults when needed
•  Self-healing where appropriate, but also giving administrators manual control over the system state when needed
•  Exhibiting predictable behavior, minimizing surprises
```

### Simplicity: Managing Complexity
A software project mired in complexity is sometimes described as a big ball of mud.

Various possible symptoms of complexity
```
- 상태 공간의 급증
- 모듈간 강한 커플링
- 복잡한 의존성
- 일관성 없는 명명과 용어
- 성능 문제 해결을 목표로한 해킹
- 임시방편으로 문제를 해결한 특수 사례
```
```
Reducing complexity greatly improves the 
maintainability of software 
Therefore, simplicity should be a key goal for the systems we build.
Making a system simpler does not necessarily mean reducing its functionality.
우발적 복잡도(accidental complexity)를 줄인다는 뜻일 수도 있다.
One of the best tools we have for removing accidental complexity is abstraction.
A good abstraction can hide a great deal of implementation detail behind a clean, simple-to-understand façade.
A good abstraction can also be used for a wide range of different applications.
Not only is this reuse more efficient than reimplementing a similar thing multiple times,
but it also leads to higher-quality software, as quality improvements in the abstracted component benefit all applications that use it.
```

### Evolvability: Making Change Easy
```
In terms of organizational processes, Agile working patterns provide a framework for adapting to change. 
The Agile community has also developed technical tools and patterns that are helpful when developing software in a frequently changing environment, 
such as test-driven development (TDD) and refactoring
```
