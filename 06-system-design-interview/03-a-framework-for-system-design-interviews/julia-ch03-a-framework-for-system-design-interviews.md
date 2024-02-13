## A 4-step process for effective system design interview

- 최종 design보다 과정 중요

### Step 1 - Understand the problem and establish design scope

- system design interview에서는 빠르게 답변하는 것 중요 X
- 문제를 이해하고 질문을 통해 요구 사항과 가정을 명확히 하는 것이 중요 -> 최종 디자인을 결정하기 전에 필요한 정보를 수집하는 과정

#### List of questions to help you get started:

- 구축할 기능이 무엇인가요?
- 얼마나 많은 사용자가 있나요?
- 3개월, 6개월, 1년 후의 예상 scale은 어떻게 되나요?
- 회사의 기술 스택은 무엇인가요? 디자인을 단순화하기 위해 활용할 수 있는 기존 서비스는 무엇인가요?

#### Example

뉴스 피드 시스템을 설계하라는 요청을 받았을 때, 요구 사항을 명확히 하는 데 도움이 되는 질문
- 모바일 앱 / 웹 앱 / 둘 다?
- 제품의 핵심 기능?
- 뉴스 피드의 정렬 방식?
- 사용자 한 명당 친구 수?
- traffic의 양? (ex. 10 million DAU)
- 피드에 포함될 수 있는 콘텐츠 유형? (ex. 미디어 파일 - 이미지, 비디오)
-> 시스템의 요구사항을 이해하고, 설계 과정에서 필요한 정보를 수집할 수 있음

### Step 2 - Propose high-level design and get buy-in

initial blueprint (초기 설계안)을 만들고
-> interviewer와 협력하여 feedback 요청
-> back-of-the-envelope 계산을 통해 설계안이 규모에 맞는지 평가

- high-level design을 위한 use cases를 고려
- API endpoint / database schema를 포함할지 여부 결정
  - API endpoint : 웹 기반 서비스와 상호작용하기 위해 네트워크를 통해 접근할 수 있는 URL의 일부, 클라이언트는 API endpoint를 통해 서버에 데이터를 요청하거나 보낼 수 있음, RESTful API - HTTP 메서드 (GET, POST, PUT, DELETE 등)와 결합되어 사용됨 (ex. 소셜 미디어 플랫폼의 API에는 사용자 프로필을 가져오는 엔드포인트, 사용자가 새로운 게시물을 작성할 때 사용하는 엔드포인트 등)
  - Database schema : 데이터베이스 구조를 정의하는 틀이나 청사진, 데이터베이스에 저장되는 데이터의 조직, 형식, 관계 등 설명, 테이블, 칼럼, 데이터 유형, 인덱스, 외래 키 등 포함

#### Example

뉴스 피드 시스템 설계는 피드 게시와 뉴스 피드 구축의 두 가지 흐름으로 나눌 수 있음

1. Feed publishing : 사용자가 게시물을 작성하면 해당 데이터가 cache/database에 기록되고 친구의 뉴스 피드에 게시물이 나타남
2. Newsfeed building : 친구들의 게시물을 역순으로 집계하여 만들어짐

![스크린샷 2024-02-11 오후 8 30 45](https://github.com/jylee2033/book-study/assets/85793553/98c9e9bc-b45a-43cd-ba65-0e6a80008229)

##### Feed publishing

사용자가 웹 브라우저나 모바일 앱을 통해 요청을 보내면 DNS를 거쳐 Load balancer로 이동한 후 웹 서버로 전달되는 구조
웹 서버의 3가지 주요 서비스
1. Post service : 게시물과 관련된 로직 처리, post cache로 cache -> post DB에 저장
2. Fanout service : 사용자의 뉴스피드를 구성할 때, 해당 사용자의 친구들의 게시물을 뉴스피드 cache에 빠르게 배포하는 기능 담당
3. Notification service : 사용자가 새 게시물을 올릴 때, 친구들에게 알림 보내는 서비스

각 서비스는 자체 cache 계층을 가질 수 있음 -> 시스템의 성능 향상, 데이터베이스에 가하는 부하 줄임

![스크린샷 2024-02-12 오후 8 36 44](https://github.com/jylee2033/book-study/assets/85793553/15efc7f8-395a-44e5-a67b-ed289abecfd8)

##### News feed building flows

사용자가 뉴스 피드를 요청할 때

### Step 3 - Design deep dive

#### Achieved following objectives:

- interviewer와 overall goals와 feature scope (기능 범위)에 대한 합의
- high-level blueprint 완성
- feedback from interviewer

여기서는 interviewer와 협력하여 architecture 내의 구성 요소를 identify하고 우선순위를 정함
-> high-level design에 집중하거나 일부 세부 사항에 집중

중요한 것 : 세부 사항 (ex. Facebook의 EdgeRank 알고리즘)에 너무 몰두하지 않고, interviewer에게 능력을 증명할 수 있는 signal을 보내는 것

#### Example

1. Feed publishing

![스크린샷 2024-02-12 오후 9 23 34](https://github.com/jylee2033/book-study/assets/85793553/9162414b-bbfc-4873-ae21-223c9f636582)

##### News feed system의 전체 architecture

- 사용자 요청은 웹 브라우저나 모바일 앱을 통해 DNS로 라우팅되고, Load balanceer로 전송됨
- Web servers : 인증과 속도 제한 기능 처리
- Post Service : 게시물 관련 작업 처리 (Post Cache와 Post DB로 구성)
- Fanout Service : Message Queue와 Fanout Workers를 통해 사용자의 뉴스 피드 구성
- Graph DB : 사용자 간의 관계 관리
- Notification Service : 사용자에게 알림 제공

2. News feed retrieval

![스크린샷 2024-02-12 오후 9 24 18](https://github.com/jylee2033/book-study/assets/85793553/6cdc351c-7f88-43e3-afb1-57a5e3d5dd95)

#### News feed 구축 과정에 초점을 맞춤

- 사용자 요청은 CDN(Content Delivery Network)을 통해 처리될 수 있음
- News Feed Service : User와 Post DB 사이의 상호작용 관리
- Cache : 빠른 데이터 액세스를 위해 사용

### Step 4 - Wrap up

system design 인터뷰의 마지막 단계에서 follow-up 질문을 할 수 있으며, 추가적인 포인트에 대해 discuss할 수 있음
- 시스템의 bottlenecks (병목 현상)을 식별하고 개선점 논의
- design을 요약하여 기억 refresh
- server failure, network loss 등의 오류 사례
- operation issues에 대해 논의
- system 확장 방법에 대해 논의

#### Dos

- assumption이 무조건 맞다고 생각하지 말고 확인 요청
- 문제의 requirements 이해
- 다양한 접근 방법 제안
- interviewer와 아이디어를 공유하며 협력

#### Don'ts

- requirements와 assumptions를 명확히 하지 않고 solution에 뛰어들지 않음
- single component에 대해 너무 많은 detail에 초점을 맞추기 않음
- 막혔을 때 도움 요청하는 것을 주저하지 않음
- design을 마친 후 인터뷰가 끝났다고 생각하지 않음 -> feedback 자주 요청

#### Time allocation on each step

- Step 1 문제 이해하고 design 범위 설정 : 3 - 10 min
- Step 2 high-level design 제안 : 10 - 15 minutes
- Step 3 design deep dive: 10 - 25 minutes
- Step 4 wrap: 3 - 5 minutes
