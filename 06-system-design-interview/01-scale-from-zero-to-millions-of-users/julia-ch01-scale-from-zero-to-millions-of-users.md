## Single server setup

### Request flow (사용자 요청)

1. **DNS (Domain Name System)**:
   - 사용자는 domain name을 통해 웹사이트 접근 (ex. api.mysite.com)
   - DNS : 제3자에 의해 제공되는 유료 서비스

2. **IP (Internet Protocol) 주소**:
   - web browser / mobile app에 IP 주소 반환됨 (ex. 15.125.23.214)

3. **HTTP (Hypertext Transfer Protocol) 요청**:
   - HTTP 요청이 웹 서버로 전송됨

4. **Response from web server**:
   - 웹 서버는 HTML / JSON 형식의 응답 반환

### Traffic source

웹 서버로의 traffic은 다음 두 곳에서 발생:

- **Web application**:
  - 서버 측 언어(Java, Python)와 클라이언트 측 언어(HTML, JavaScript) 사용

- **Mobile application**:
  - HTTP 프로토콜 사용, 주로 JSON 형식의 API 응답 사용


## Database

사용자 기반의 성장에 따라 여러 서버 필요  
-> web/mobile traffic과 database 서버를 분리함으로써 독립적으로 확장할 수 있음


## Which databases to use?

### Relational databases

- RDBMS / SQL database
- ex. MySQL, Oracle, PostgreSQL
- 데이터를 테이블과 rows에 저장, SQL을 사용하여 table 간에 join을 할 수 있음

### Non-Relational databases

- NoSQL databases
- ex. CouchDB, Neo4j, Cassandra, HBase, Amazon DynamoDB
- key-value, graph, column, document 저장소로 분류됨
- join은 지원 X

Non-Relational databases가 적합한 경우:  
(대부분의 경우에는 Relational databases 사용)
- super-low latency가 필요한 application
- 데이터 구조가 relational databases로 표현하기 어려운 경우
- JSON, XML, YAML 등의 데이터를 serialize / deserialize 하는 경우
- 엄청난 양의 데이터를 저장해야 하는 경우


## Vertical scaling vs horizontal scaling

### Vertical scaling (scale up)

- 서버에 더 많은 파워(ex. CPU, RAM)를 추가하는 과정
- traffic이 적을 때 좋은 옵션, 간단함
- hard limit이 있어 unlimited CPU와 메모리를 추가할 수 없음
- 하나의 서버가 다운되면 웹사이트/앱도 다운됨

### Horizontal scaling (scale-out)

- 더 많은 서버를 추가하여 규모 확장
- for large scale applications

사용자가 web server에 직접 연결하면 web server가 오프라인 상태일 때 사용자는 웹사이트에 접속할 수 X 
많은 사용자가 동시에 web server에 접속하여 서버의 load limit에 도달하면, 사용자는 slower response를 경험하거나 fail to connect  
-> load balancer 사용


## Load balancer

Web servers에 들어오는 traffic을 균등하게 분배함

- 사용자는 load balancer의 public IP를 통해 연결
- web servers는 클라이언트에게 직접 접근할 수 X
- 보안 향상을 위해 서버 간 통신에는 private IP 사용
- load balancer는 private IP를 통해 web server와 통신
- server 1이 오프라인이 되면, 모든 traffic은 server 2로 라우팅 (website가 오프라인 상태가 되는 것을 방지)
- traffic이 증가할 경우, web server pool에 새로운 서버를 추가

failover(장애 대비)와 redundancy(중복성) 지원 X  
-> database replication 사용


## Database replication

- **master database** : write operations만 지원 (data-modifying)
- **slave database** : master db로부터 data를 복사하여 read operations만 지원

### System design

- 사용자는 DNS로부터 load balancer의 IP 주소를 얻음
- 사용자는 이 IP 주소를 통해 load balancer에 연결
- HTTP 요청은 server 1 / server 2로 라우팅
- web server는 slave db에서 사용자 데이터를 read
- web server는 data-modifying operations를 master db로 라우팅


## Cache

비용이 많이 드는 responses나 자주 접근하는 데이터를 저장하는 일시적인 저장 공간 (요청을 더 빠르게 처리할 수 있게 함)


## Cache tier

- 데이터베이스보다 훨씬 빠른 일시적인 데이터 저장 계층
- 요청을 받은 후, web server는 먼저 cache에 response가 있는지 확인
- cache에 데이터가 있으면 클라이언트에게 데이터를 바로 돌려보냄
- cache에 데이터가 없다면 데이터베이스를 조회하고 cache에 저장한 후 클라이언트에게 전송

Memcached API:

```python
SECONDS = 1
cache.set('myKey', 'hi there', 3600 * SECONDS)
cache.get('myKey')
```


## Considerations for using cache

- 자주 읽지만 드물게 변경되는 데이터에 사용
- cache된 데이터가 만료되면 cache에서 제거되는 expiration policy 구현 (너무 짧은 expiration date X)
- 데이터 수정할 때 data 저장소와 cache를 sync (challenging)
- single cache server -> SPOF (Single point of failure) -> 여러 데이터 센터에 걸쳐 cache server 배치
- LRU (Least-recently-used) : cache가 가득 차면 기존 항목을 제거해야 함


## Content delivery network (CDN)

- static content를 전달하기 위해 지리적으로 분산된 서버 네트워크  
- 이미지, 비디오, CSS, JavaScript 파일 등을 cache하여 더 빠르게 제공

### CDN 작동 원리

- 사용자가 웹사이트를 방문하면, 가장 가까운 CDN 서버가 static content를 제공 (ex. 샌프란시스코에 CDN 서버가 있다면, 로스앤젤레스 사용자는 유럽 사용자보다 빠르게 콘텐츠를 받음)


## Considerations of using a CDN

- **비용** : 데이터 전송에 요금 부과하는 third-party
- **캐시 만료 설정** : time-sensitive content는 적절한 cache expiry time을 설정하는 것 중요
- **CDN fallback (장애 대비)** : 일시적으로 다운될 경우, 클라이언트가 origin에서 리소스를 요청할 수 있어야 함
- **Invalidating files (파일 무효화)** : CDN에서 파일이 expire 되기 전에 제거할 수 있음 (CDN vendors의 API 사용 / URL의 parameter에 version #를 추가)

![스크린샷 2024-02-06 오후 3 50 20](https://github.com/jylee2033/book-study/assets/85793553/53e25ad3-7a53-4e13-9f6c-2056fbeae365)

-> static assets (JS, CSS, 이미지 등)은 더 이상 웹 서버에서 제공되지 않고, CDN을 통해 제공
-> 데이터베이스 부하는 데이터 caching으로 lightened


## Stateless web tier

web tier를 수평적으로 확장할 때는 state data(ex. user session data)를 web tie에서 제거 
-> relational database / NoSQL에 저장 
-> cluster 내의 각 web server가 데이터베이스에서 state data에 access


## Stateful architecture

- stateful server : client data(state)를 기억하여 한 요청에서 다음 요청으로 전달
- stateless server : keeps no state information

![스크린샷 2024-02-06 오후 4 11 27](https://github.com/jylee2033/book-study/assets/85793553/7eb6b7e0-1f53-4013-bc64-84695b701844)

- 여기서 User A의 session data와 profile image는 서버 1에 저장됨  
- 인증을 위해서 HTTP 요청이 서버 1로 라우팅되어야 함 (서버 2에는 User A의 session data가 없기 때문에 인증 실패)  
- 같은 클라이언트의 모든 요청을 save server로 라우팅해야 함


## Stateless architecture

![스크린샷 2024-02-06 오후 4 11 42](https://github.com/jylee2033/book-study/assets/85793553/b5eff66e-87c4-4f36-a75f-46968dfc9741)

- HTTP 요청이 어떤 웹 서버로든 전송될 수 있음
- 모든 web server는 shared data store에서 state data를 가져옴
- 시스템을 더 단순하고, robust, 확장 가능하게 만듦

![스크린샷 2024-02-06 오후 4 32 55](https://github.com/jylee2033/book-study/assets/85793553/7baaabd0-f259-40bb-ba71-b1df4ff2ea20)

- web tier에서 session data를 제거하고 영구 데이터 저장소에 저장
- shared data store : relational database (ex. Memcached/Redis, NoSQL)
- Autoscaling : web servers를 traffic load에 따라 자동으로 추가 / 제거


## Data centers

웹사이트가 성장함에 따라, 다양한 지역의 사용자 경험을 개선하는 것 중요  
-> 여러 data centers 지원 (사용자에게 더 가까운 위치에서 콘텐츠를 제공하여 성능 향상)  
-> geoDNS 라우팅 사용 (사용자의 위치에 기반)  

![스크린샷 2024-02-06 오후 4 46 09](https://github.com/jylee2033/book-study/assets/85793553/682f7d03-9dac-4b27-a579-371e07c4626b)

- 사용자는 가장 가까운 데이터 센터로 라우팅됨
- DC2(US-West)가 오프라인일 때 모든 트래픽이 DC1(US-East)로 라우팅됨

### Multi-data center 설정의 challenges

- **Traffic redirection** : 올바른 데이터 센터로 트래픽을 효과적으로 리디렉션하는 것 필요
- **Data synchronization** : 사용자는 지역에 따라 다른 로컬 데이터베이스 / caches를 사용할 수 있음
- **Test and deployment** : 모든 데이터 센터를 통해 일관된 서비스를 유지하기 위한 automated deployment tools 중요


## Message queue

- asynchronous communication을 지원하는 component, 메모리에 저장됨
- buffer 역할을 하여 asynchronous requests를 분산시킴
- producers/publishers가 메시지를 생성하여 queue에 게시하고, consumers/subscribers가 큐에 연결하여 메시지에 정의된 동작 수행
- producer가 처리할 수 없을 때 consumer가 메시지를 queue에서 읽을 수 있도록 메시지를 queue에 게시할 수 있음
- 애플리케이션의 생산성과 소비성 부분을 독립적으로 확장할 수 있도록 해줌


## Logging, metrics, automation

웹사이트가 성장함에 따라 필수적

### Logging

- 시스템에서 오류와 문제를 monitor하는 데 중요

### Metrics

- business insights를 얻고 시스템의 health status를 이해하는 데 도움이 됨

### Automation

- 시스템이 복잡해지면 build / 자동화 도구를 활용
- continuous integration은 모든 코드 check-in이 자동화를 통해 검증되어 문제를 조기에 발견할 수 있게 함
- build, test, deploy process를 자동화하면 생산성을 크게 향상시킬 수 있음


## Adding message queues and different tools

![스크린샷 2024-02-06 오후 5 45 13](https://github.com/jylee2033/book-study/assets/85793553/d23610af-ef94-4b85-abad-acc68490d788)

- 시스템을 loosely coupled하고 failure에 강하게 만드는 message queue 포함
- logging, monitoring, metrics, and automation tools 추가


## Database scaling


## Vertical scaling (scaling up)

기존 기계에 더 많은 파워(CPU, RAM, DISK)를 추가하는 방식

### Drawbacks
- 하드웨어 한계로 인해 대규모 사용자 베이스에 대해서는 단일 서버로는 충분하지 않음
- single point of failures 위험 증가
- 전체 비용이 높음


## Horizontal scaling (sharding)

- 더 많은 서버 추가
- 데이터베이스를 더 작고, 관리하기 쉬운 부분으로 나누어 각 shard가 동일한 schema를 공유하지만 각 shard의 데이터는 고유함

### Sharding

- 대규모 데이터베이스를 관리 가능한 작은 부분으로 분리

### Sharding의 고려사항

- **Resharding data** : single shard가 더 이상 데이터를 처리할 수 없을 때 필요
- **Celebrity problem (hotspot key problem)** : 특정 shard에 과도한 접근이 있을 경우 발생
- **Join and de-normalization** : 데이터베이스가 여러 서버에 sharding된 후에는 join이 어려워짐


## Millions of users and beyond

- web tier를 stateless로 유지
- 가능한 한 많은 데이터를 cache
- 여러 data centers 지원
- static assets를 CDN에 host
- sharding을 통해 data tier 확장
- individual services로 tiers 분리
- 시스템을 모니터링하고 자동화 도구 사용
