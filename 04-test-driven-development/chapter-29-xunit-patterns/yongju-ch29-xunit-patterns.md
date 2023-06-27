# Chapter 29. xUnit Patterns

xUnit family 테스트 프레임워크 중 하나를 사용하는 패턴에 관한것

### Assertion

어떻게 테스트가 올바르게 작동했는지 체크하는가? boolean expression 사용

- 결정은 boolean 이여야 함 : 모든것이 괜찮을 때는 `True`, 문제이 있을 경우에는 `False`
- boolean 은 `assert()` 를 통해 체크되어야 함
- equality는 정확하게 비교해야함, 그리고 에러 메세지는 informative 해야함
- `assertEquals(expected value, result of the code)`

`black box testing`
`white box testing`

### Fixture

여러 테스트에 필요한 공통적인 객체를 어떻게 만들까? 테스트의 local variables 를 instance variable 로 바꾸고 `setUp()`을 override 하고 이 변수들을 초기화 해라
테스트 객체의 중복은 나쁘다. 왜냐면

- 쓰는데 오래걸린다. (복사 붙여넣기도). 우리는 테스트를 빠르게 쓰길 원함
- interface를 바꾸려면 각각 테스트 마다 모두 바꿔야함
  근데 또, 이 중복이 좋은 점도 있음 왜냐면
- 위에서 아래로 읽는데 테스트 객체가 위에 적혀 있어서 보기 좋음
- 다른 테스트 코드를 쓰기 전에 객체가 어떻게 생겼는지 알기 좋음

xUnit 은 이 두가지 방식을 모두 지원함. test-fixture-creating code 를 테스트와 함께 쓸 수도 있고 이것을 공통으로 사용하게 `setUp()` 메서드 안에 쓸 수 있음

### External Fixture

각각의 테스트는 끝날때 항상 시작하기 전의 상태로 돌려놔야함 (eg. 테스트를 시작하고 파일을 여는 작동을 했으면 끝나기 전에 파일을 다시 닫아야함)
xUnit `tearDown()` 메서드를 제공함 -> 테스트 후에 작동함
`setUp()` 이 불려진 한 마지막은 항상 `tearDown()` 이 불려질 수 있음

### Test Method

Object programming languages 3단계의 hierarchy 를 가짐

- Module
- Class
- Method

테스트 작성도 이 구조를 따라야함
fixture를 Class 에서 사용한다면, 테스트를 작성하는 곳은 Method 여야 함
같은 클래스 안에서 하나의 fixture를 공유하는 모든 테스트는 methods 로 작성됨
다른 fixture를 사용하는 테스트는 다른 클래스여야 함
test methods 읽기 쉽고 직설적이여야 함

### Exception Test

예상되는 예외들을 어떻게 테스트 할까? 예상되는 예외들을 찾고 무시해라?

### All Tests

어떻게 모든 테스트를 함께 돌리나? Make a suite of all the suites
