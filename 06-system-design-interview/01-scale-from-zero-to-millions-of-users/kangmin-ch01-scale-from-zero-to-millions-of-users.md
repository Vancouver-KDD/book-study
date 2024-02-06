# Chapter 1 : Scale from zero to millions of users

## Single server setup

- 모든 것이 하나의 서버에서 실행되는 형태로 request flow 는 다음과 같다.
1. User 가 domain names 혹은 address 형태로 요청 (보통의 경우 3rd party 에 의해 제공된 DNS)
2. DNS 으로 부터 IP address 를 받음
3. 받은 Ip address(웹서버) 로 요청
4. 웹 서버에서 response 를 받음

## RDBMS vs NoSQL

- RDBMS 는 table 간의 JOIN 을 지원하고 이를 통해서 쿼리가 이뤄짐
- 반면, NoSQL 은 기본적으로 JOIN 이 불가능하다.
- 전통적으로 RDBMS 가 오랫동안 개발자들에게 널리 사용되어 왔으며, 대부분의 케이스에서 적합하게 사용 가능
- 그러나 다음과 같은 경우 NoSQL 사용 가능 고려
1. super-low latency 를 추구하는 경우
2. 데이터 구조가 자주 변경되거나 unstructured 일 경우
3. You only need to serialize and deserialize data (잘 이해가 안가는 부분)
4. 대량의 데이터를 저장 할 경우

## Verticle scaling vs horizontal scaling

- Vertical scaling 은 단일 서버의 리소스를 늘리는 방식
- 이 방식은 단순하지만, 리소스를 늘리는 방식은 한계가 있고, 장애 발생 시에 대응이 준비가 안되어있다.
- horizontal scaling 이란, 리소스 풀(pool of resources)에 extra server 를 추가시키는 방식
- 하나의 서버가 goes down 되어도 대응이 가능하다.

## Load balancer

- User 에게 Web Server 로 direct 접근하지 않고, 중간에 Load balancer 를 거치게 한다.
- User -> Load balancer (with public ip) -> Web servers (with private ips)
- 보안에 장점이 있고, 하나의 서버가 down 되더라도 대응이 가능하다.

## Database replication

- Master DB, Slave DBs 로 구성하여 write / update 는 Master 에서 read 는 Slave 에서 담당하게 한다.
- Better performance, reliability 의 장점을 가진다.
- Slave DBs 중 하나가 문제가 발생했을 시, 트래픽을 다른 Slave 로 돌릴 수 있고,
- Master DB 가 장애 일 시, Slave 들 중 하나를 Master DB 로 만듬으로써 대응이 가능하다.
- 단, Data sync 를 맞추는 일은 좀 더 복잡하고 정교한 작업이 필요

## Cache

- 리소스가 많이 드는 혹은 자주 엑세스 되는 데이터(expensive responses or frequently accessed data)를 temporary 하게 저장해놓는 기술
- Database 와는 다른 tier. Web Server <-> Cache <-> DB 가 된다.
- (꼭 위와 같지는 않고, 브라우저캐시도 있고 다양한 곳에서 캐시가 구서이 될 수 있다.)
- 기본적인 원리는 User requests 에 대해 바로 DB 에서 Query 를 하는 것이 아닌, Cache 에 먼저 response 하기 적절한 값이 있는지 확인하고 있으면 그것을 내려준다.
- 없으면, 그때 DB 로 query 요청을 보내 적절한 response 값을 얻는다.

## CDN

- Geographically 하게 서버를 분산시켜, static content 를 제공하는데 쓰인다.
- static content 를 캐시개념으로 보관하면서, 응답 값으로 주는 것이 CDN 이다.
- 유럽에서는 유럽에 가장 가까운 CDN, 북미에서는 북미에서 가장 가까운 CDN 에서 static content 를 제공한다.
- 서버 distance 가 물리적으로 멀 수록 latency 가 비례하기 때문
- 하지만 위에 Cache 에서 언급했던 것처럼 expensive responses or frequently accessed data 가 아니라면 사용을 고려해야한다.
- 왜냐하면, CDN 에 올리는 것도 다 비용이기 때문


## Data centers

- 정상상확에서는 가까운 geoDNS 로 routing 이 된다.
- 장애가 발생 시, 다른 정상적인 geoDNS 로 routing 을 하는데, 이를 위해서 Traffic redirection, Synchronization 그리고 Test & deployment 등이 같이 고려되어야 한다.

## Message queue

- buffer 와 같은 역할을 하면서, asynchronous requests 를 분배해준다.
- Producer -> [publish] -> Message Queue -> [consume] -> Consumer -> [subscribe] -> MessageQueue 의 기본 아키텍쳐를 가진다
- MEssage queue 가 존재하기 때문에, consumer 혹은 producer 가 unavailable 한 상황이라도 작동이 가능하다.


## Database scaling

- 위에 서버 예시처럼 vertical, horizontal 두가지 방식이 있다. (사실, DB 도 서버이니.. 같은 얘기를 하는 것이다.)
- 특징 또한 비슷하다 vertical 의 경우 간단하지만 한게가 있고 장애 대응에 단점이 있다.
- horizontal scaling 의 경우 서버 수를 늘리는 것이고, sharding 이라고 불린다.
- Sharding 은 큰 DB 를 shards 라고 불리는 작은 단위로 분리시키는 것이다.
- 동일한 Schema 를 가지면서 actual data 는 공유한다.
- Each Shard 는 sharding key 를 가지고 있어 routing 을 하는데 효율적이다.
- 하지만... 이에 따라 역시 관리하는데 Resharding data, Celebrity problem, Join and de-normalization 등을 고려해야 한다.
