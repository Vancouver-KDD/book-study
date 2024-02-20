# Chapter 5 : DESIGN CONSISTENT HASHING


## The rehashing problem

- n개의 cache servers 가 있다면, 부하를 균형있게(balance) 분산하기 위한 일반적인 해싱 방법은 다음과 같이 사용됨
- serverIndex = hash(key) % N, where N is the size of the server pool.

- 이 접근 방식은 서버 풀의 크기가 고정되어 있고 데이터 분배가 균등한 경우 잘 작동함.
- But, 새 서버가 추가되거나 기존 서버가 제거될 때 문제가 발생 할 수 있음.
- 예를 들어, 서버 1이 offline 상태가 되면 서버 풀(pool)의 크기가 3이 됨.
- 동일한 hash 함수를 사용하여 키에 대해 동일한 hash 값을 얻지만 모듈러 연산(modular
  operation)을 적용하면 서버 수가 1개 줄어들기 때문에 다른 server index 를 얻게 됨. (이로 인해, cash miss 가 발생)

## Consistent hashing

- 위의 문제를 해결하는데 좋은 효과적인 기술
- 일관된 해싱은 해시 테이블이 다시 크기 조정되고 일관된 해싱이 사용될 때, 평균적으로 k/n 개의 키만 다시 매핑되어야 함.
- 여기서 k는 the number of keys, n은 the number of slots.
- 대조적으로 대부분의 전통적인 hash table 에서는 the number of array slots 의 변경으로 인해 거의 모든 키가 다시 매핑(mapping)됨.

## Hash space, hash ring and hash servers

- hash space 의 양끝을 모아서 hash ring 을 만듬
- 같은 hash function f 를 사용하여 hash ring 에 mapping

## Hash keys, Server lookup, Add and remove a server

- key 가 저장된 서버를 결정하기 위해 key position 에서 clockwise 으로 이동하여 링에서 첫 번째로 만나는 서버를 찾음.
- 새로운 서버를 추가하는 것은 일부 키의 재분배만 필요함 (전부 필요하지 않음)
- 역시, 제거할 때도 일부 키의 재분배만 필요함.

## Two issues in the basic approach

### basic approach 요약
- 일관된 해싱 알고리즘을 사용하여 링 위의 서버 및 키를 매핑함.
- 키가 매핑된 서버를 찾으려면 키 위치에서 시계 방향으로 이동하여 링 위의 첫 번째 서버를 찾아야 함.

### issues
- 서버가 추가되거나 제거되므로 링에 대한 partition 크기를 동일하게 유지하는 것은 불가능
- partition 은 인접한 서버 사이의 hash space, 할당된 링의 파티션 크기가 매우 작거나 상당히 큰 경우가 있을 수 있음.
- 또한 링 위에 비균일한 키 분배가 일어날 수 있음.

