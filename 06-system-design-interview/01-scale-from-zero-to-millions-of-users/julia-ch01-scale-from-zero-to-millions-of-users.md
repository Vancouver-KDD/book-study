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

사용자가 web server에 직접 연결하면 web server가 오프라인 상태일 때 사용자는 웹사이트에 접속할 수 없음  
많은 사용자가 동시에 web server에 접속하여 서버의 load limit에 도달하면, 사용자는 slower response를 경험하거나 fail to connect  
-> load balancer 사용


## Load balancer

: Web servers에 들어오는 traffic을 균등하게 분배함

- 사용자는 load balancer의 public IP를 통해 연결
- web servers는 클라이언트에게 직접 접근할 수 없음
- 보안 향상을 위해 서버 간 통신에는 private IP가 사용됨
- load balancer는 private IP를 통해 web server와 통신함
- server 1이 오프라인이 되면, 모든 traffic은 server 2로 라우팅됨 (website가 오프라인 상태가 되는 것을 방지)
- traffic이 증가할 경우, web server pool에 새로운 서버를 추가하면 됨

failover(장애 대비)와 redundancy(중복성) 지원 X  
-> database replication 사용


## Database replication

- **master database** : write operations만 지원 (data-modifying)
- **slave database** : master db로부터 data를 복사하여 read operations만 지원

### System design

- 사용자는 DNS로부터 load balancer의 IP 주소를 얻음
- 사용자는 이 IP 주소를 통해 load balancer에 연결함
- HTTP 요청은 server 1 / server 2로 라우팅됨
- web server는 slave db에서 사용자 데이터를 read
- web server는 data-modifying operations를 master db로 라우팅함


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
