# Chapter 6 : DESIGN A KEY-VALUE STORE

- Key-Value store는 고유 식별자를 key로, 연결된 값을 value로 저장하는 non-relation 데이터 베이스.
- Key는 고유해야 하고, value에 접근하기 위해 key를 사용.
- Key-Value store는 빠르지만 모든 데이터를 메모리에 저장하기는 어려울 수 있다. ()

## Single server key-value store

- 단일 서버에 저장하는 것은 간단하지만, 메모리 제약으로 인해 모든 데이터를 저장하기 어려울 수 있음.
- 압축과 데이터 일부만 메모리, 나머지는 디스크에 저장하는 방식으로 최적화할 수 있다.
- 실제로 보통 사이드프로젝트에서나.. 하나로 처리하지; 실제 프로덕트에서 이 방식을 채택하는 것을 생각하는건 쉽지 않다고 생각한다.

## Distributed key-value store

- Distributed Key-Value store는 여러 서버에 걸쳐 key-value 쌍을 분산 시킨다.
- CAP theorem 은 Consistency (일관성), Availability (가용성), Partition Tolerance (분할 허용) 중 2가지를 만족할 수 있다는 뭐 그런말이다.

### CP system: 일관성과 분할 허용을 지원하지만 가용성을 희생.
### AP system: 가용성과 분할 허용을 지원하지만 일관성을 희생.
### CA system: 일관성과 가용성을 지원하지만 분할 허용을 희생. (실제로는 존재 ㄴㄴ)


## Data Partitioning

- Consistent Hashing을 사용하여 데이터를 여러 서버에 분산.

## Data Replication

- 높은 가용성과 신뢰성을 위해 데이터를 N개 서버에 복제 (N은 설정 가능한 파라미터).

## Consistency

- Quorum Consensus 를 사용하여 읽기/쓰기 작업 모두 일관성을 유지.
- W는 쓰기 quorum 크기이며, 쓰기 작업이 성공적으로 간주되려면 W개 복제본으로부터 확인 메시지를 받아야 한다.
- W = 1은 데이터가 단일 서버에 쓰여진다는 의미가 아니다! R은 읽기 quorum 크기이며, 읽기 작업이 성공적으로 간주되려면 최소 R개 복제본으로부터 응답을 받아야 한다는 것을 의미, 강력한 일관성을 위해서는 W + R > N이 성립해야 함.

## Inconsistency Resolution

- 복제는 가용성을 높이지만 복제본 간 불일치를 야기할 수 있다. (항상... 복제는 모든게 sync 를 맞추는데에서 문제가 발생한다. 이 로직을 만드는게 빡센 것 같음)
- 버전 관리와 벡터 클록을 사용하여 불일치 문제를 해결하는 것도 방안이 됨.

## Handling Failures

- Gossip Protocol: 서로 정보를 주고받아 장애 노드를 파악하는 분산 방식의 장애 감지.
- Sloppy Quorum: 읽기/쓰기 작업을 완전히 차단하지 않고 복제본 중 일부만 사용하여 가용성을 향상.
- Hinted Handoff: 임시적으로 사용할 수 없는 복제본의 데이터를 다른 사용 가능한 복제본에 전달.
- Anti-Entropy Protocol: 모든 복제본의 데이터를 동기화 유지하는 프로토콜 - Merkle Tree 를 사용하여 불일치를 효율적으로 감지하고 데이터 전송량을 최소화 한다.

## System architecture diagram

- 클라이언트는 get(key)와 put(key, value)와 같은 간단한 API를 통해 Key-Value 의 store 와 연결된다.
- Coordinator 노드는 클라이언트와 Key-Value store 사이의 중계 역할을 하는 노드. (미들...웨어 같은 건지는;)
- 노드들은 Consistent Hashing을 사용하여 링 상에 분산되어 있음.
- 데이터는 여러 노드에 복제.
- 모든 노드는 동일한 작업을 수행하므로 단일 실패 지점이 없다.

### Write Path

- 쓰기 요청은 commit log 파일에 기록.
- 데이터는 메모리 캐시에 저장.
- 메모리 캐시가 가득 찼거나 사전 설정된 임계값에 도달하면 데이터는 디스크에 있는 SSTable(Sorted String Table)에 플러시 된다.

### Read Path

- 읽기 요청은 먼저 데이터가 메모리 캐시에 있는지 확인 -> 메모리 캐시에 없으면 디스크에서 검색.
- Bloom filter를 사용하여 데이터가 포함된 SSTable을 검색 -> SSTable에서 데이터를 리턴.
