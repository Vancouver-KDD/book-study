# Chapter 25. Test-Driven Development Patterns

Basic strategic questions

- Testing이 무엇을 의미하나?
- 언제 테스트를 해야하나?
- 테스트하기 위한 로직을 어떻게 정하나?
- 테스트를 위한 데이타를 어떻게 선택하나?

### Test

- "테스트 하다" 는 평가한다는 뜻 -> 어떤 개발자도 테스트 없이 release 하지 않는다
- 변화를 테스트 할 수 있다고 해도, 변화를 테스트 하는 것과 테스트를 갖고 있다는 것은 같지 않음
- "테스트" (명사)는 acceptance 또는 rejection 로 이끄는 일련의 과정
- high stress -> less testing -> more errors -> more stress 악순환
- 이것을 해결할 수 있는건 `Automated Testing` -> change 가 의도한 것 외의 다른 것에 영향을 주지 않았다는걸 확신시켜줌
- 언제든지 필요하다면 테스트를 작성할 수 있다

### Isolated Test

- 각각의 테스트는 서로에게 전혀 영향을 주지 않아야 한다
- 테스트는 순서에서 독립적이여야 함
- 문제를 작은 단위로 쪼개서 각각 테스트의 셋업을 쉽고 빠르게 해야함
- 독립적인 테스트는 many highly cohesive, loosely coupled objects 로 soluntion 을 만들수 있도록 도와줌

### Test List

- 테스트를 시작하기전에, 테스트할 리스트를 만들어야 함 -> 놓치거나 distract 되는 일이 없어짐
- 저자는 몇시간 안에 당장 해얄 할 일의 리스트와, 주단위 또는 월단위 할일 리스트를 구분해서 적어놈
- 뭔가 해야할 일이 생기면, 어느 리스트에 속해야 하는지 생각해보고 추가함 (사실 할 필요가 없는 일 일수도 있음)
- 이걸 테스트에 적용시키면,
  1. 실행할 모든 operation 사용 예시를 리스트로 만듦
  2. 아직 존재하지 않는 operations을 위해, 리스트에 Null version 동작들을 추가함 (아무 동작도 하지 않는 버전)
  3. 깔끔한 코드를 얻기 위해 반드시 해야할 리팩토링 목록을 적음
- 테스트 윤곽을 잡아가는 것 대신 그냥 작성해서 실행시킬 수도 있지만 추천하지 않음
  - 테스트를 많이 작성하다가 뭔가 리팩토링 하는 상황이 오면, less likely to go clean up
  - green bar를 보기까지 시간이 더 오래 걸릴 수 있음
- TDD의 기본 원리 : one change away from a green bar
- 테스트를 만들어 갈 수록 새로운 테스트가 필요하게 될 것 -> 새로 필요한 테스트를 리스트에 추가함

### Test First

- 언제 테스트 작성을 해야할까? 코드를 작성하기 전에!
- functionality를 실행시키는 것 뿐만 아니라, 디자인과 scope control을 위한 방법을 생각해야함
- 테스트 실행 -> 스트레스 감소 -> 더 테스트 하고 싶음 : 선순환

### Assert First

- assert를 먼저 작성해라
  - building system -> stories 부터 시작
  - writing functionality -> writhe the tests you want to pass
  - where to start writing a test -> assert to pass
- 여러가지 고려해야 할 사항들이 있는데 이 중 두가지만 따로 생각할 수 있다
  1. 올바른 답이 무엇인지
  2. 어떻게 체크할 것인지
- 여기서 부터 시작해서 천천히 한단계씩 outline을 만들어 갈 수 있음

### Test Data

- 테스트를 읽기 쉽고 실행하기 쉽게 만드는 데이터를 사용해라
- 데이터가 다르다면, 의미있는 차이가 있어야함. 굳이 의미없는 다른 데이터를 여러개 쓸 필요 없음
- 만약 시스템이 여러 inputs 을 다뤄야 하면 테스트 역시 여러 inputs 사용해야함, 하지만 3개로 동일한 디자인과 실행을 이끌어 낼 수 있다면 굳이 10개를 사용할 필요는 없음
- 하나 이상을 의미할 수 있는 같은 constant를 사용하지 않음 eg.2+2 -> 2+3
- 테스트 데이터의 대안은 실제 데이터를 사용하는 것, 다음 경우에 유용함

  - 실제 실행을 통해 수집한 외부 이벤트 결과를 이용해 실시간 시스템을 테스트 할때
  - 예전 시스템 출력과 현재 시스템 출력을 비교할때
  - 시뮬레이션 시스템을 리팩토링한 후 기존과 정확히 동일한 결과가 나오는지 확인할 때

### Evident Data

- 데이터의 의도를 어떻게 나타낼 것인가? 예상되는 결과와 실제 결과를 테스트에 반영하고 그 둘의 결과가 명백히 나타나도록 만들어라

```
Bank bank= new Bank();
bank.addRate("USD", "GBP", 2);
bank.commission(0.015);
Money result= bank.convert(new Note(100, "USD"), "GBP");
assertEquals(new Note(100 / 2 * (1 - 0.015), "GBP"), result);
```

- 이 테스트에서 입력으로 사용된 숫자와 예상되는 결과 사이의 관계를 읽어낼 수가 있다
- 프로그래밍을 쉽게 해줌
- assertion 부분에 수식을 써놓으면 다음에 뭘 해야할지 쉽게 알 수 있음 (나눗셈, 곱셈을 하는 프로그램이 필요함)
- 코드에서 magic number를 사용하지 말라는 것의 예외가 될 수도 있으나, single method 범위에서는 숫자들 사이의 관계가 명확하다. 그래서 이미 정의된 symbolic constants 가 있으면 저자는 그것을 쓰겠다함
