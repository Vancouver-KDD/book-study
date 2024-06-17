# Chapter 4 : DESIGN A RATE RIMITER

## Step 1 - Understand the problem and establish design scope

- Rate limiting 은 클라이언트나 서비스의 traffic 속도를 제어하는 기능이며, HTTP 에서는 특정 기간 내에 클라이언트의 요청 수를 제한.

## Step 2 - Propose high-level design and get buy-in

- 클라이언트 측 rate limiting 과 서버 측 API rate limiting 중 어디에 구현할지에 대한 고려가 필요.
- rate limiter middleware 를 사용하여 API gateway 에 구현하는 것이 일반적.
- rate limiting 을 위한 알고리즘 선택은 기업의 기술 스택, 엔지니어링 리소스, 우선 순위 등에 따라 달라짐.

### Algorithms for rate limiting

#### Token Bucket Algorithm
- 특정 비율로 token 을 생성하는 시스템을 사용하여 요청 처리.
- bucket 에는 일정 수의 token 이 들어있고, 클라이언트가 요청을 보낼 때마다 토큰 사용.
- bucket 에 token이 없으면 클라이언트의 요청을 거부하거나 대기.
- Pros: 간단하고 효율적인 방식으로 요청을 제어.
- Cons: 트래픽이 다소 불규칙하거나 급격하게 변하는 경우 효과적으로 대응하기 힘듬.

#### Leaky Bucket Algorithm
- 특정 속도로 요청을 처리하고, 초과된 요청은 누출되는 방식으로 처리.
- bucket 에 일정량의 toekn 이 들어있고, 요청이 들어올 때마다 toekn 소비.
- 일정 시간마다 bucket이 비워지면서, 일정 속도로 누출.
- Pros: 일정한 처리율을 유지하면서 초과 요청을 효과적으로 처리 가능.
- Cons: 요청이 급격하게 변하는 경우 처리에 어려움.

#### Fixed Window Counter Algorithm
- 특정 시간 동안의 요청 수를 세는 방식.
- 시간 창을 고정하고 해당 시간 동안의 요청 수를 세는 방식입니다.
- 시간이 지나면 창을 초기화합니다.
- Pros: 간단한 구현 가능.
- Cons: 시간이 경과할 때마다 초기화되어 트래픽의 불규칙성을 처리하는 데 제약이 있음.

#### Sliding Window Log Algorithm
- 시간별 또는 사건별 로그를 유지하고, 특정 시간 동안의 로그를 보고 처리량을 계산.
- request 가 발생할 때마다 로그를 남기고, 특정 시간 범위 내의 로그를 검사하여 처리량을 계산.
- Pros: 유연성이 있으며, 시간대별로 정확한 처리량을 계산 가능.
- Cons: 로그의 크기가 커지고 처리량이 많아질수록 성능에 영향을 줌.

#### Sliding Window Counter Algorithm
- 슬라이딩 창 카운터 알고리즘은 고정 창 카운터와 유사하지만, 창의 시작 시간이 고정되어 있지 않고 계속 변경.
- 이전 시간 창의 요청 수를 고려하여 새로운 요청을 처리.
- Pros: 시간 창의 이동이 유연하며, 실시간으로 요청을 처리하는 데 적합.
- Cons: 구현이 복잡할 수 있으며, 시간 창의 이동에 따른 성능 영향을 고려해야 함.

### High-level architecture
- rate limiter를 어디에 저장할지에 대한 고려가 중요. 대부분의 경우 Redis와 같은 인메모리 캐시를 사용.
- 클라이언트 요청을 처리하기 전에 rate limiter middleware 에서 요청을 필터링하고, 필요한 경우 rate limiter 를 적용.

## Step 3 - Design deep dive

### Rate limiter in a distributed environment

- Rate limiter 를 multiple servers 및 concurrent threads 를 지원하도록 확장하는 것은 복잡한 문제.
- Race condition 및 Synchronization issue 를 해결하기 위해 Lua 스크립트 및 Redis의 정렬된 집합과 같은 전략을 사용할 수 있다.

### Performance optimization & Monitoring

- multi-data center setup 과 더불어 eventual consistency model 을 사용하여 성능을 최적화 가능.
- 모니터링은 시스템이 효과적인지를 확인하는 데 중요합니다.
