# Chapter 26. Red Bar Patterns

언제, 어디서 테스트를 쓰고 언제 테스트 작성을 멈출지에 관한 패턴

### One Step Test

목록에서 다음으로 작성할 테스트를 어떻게 고를것인가? 모든 테스트가 전체 목표를 향한 `one step`을 나타내야하는가? 정답은 없음, 명확하고 실행할 수 있는 것을 먼저 고르자.
전체 계산의 간단한 케이스부터 테스트하는 방식 -> Top-down
작은 부분부터 시작해서 점점 크게 합쳐가는 방식 -> Bottom-up
top-down 과 bottom-up이 사실 TDD 프로세스를 설명하는데 크게 도움이 되진 않음

- 수직적 메타퍼는 그냥 시간 흐름에 따라 프로그램이 어떻게 변하는지에 대한 간단한 시각화임. 대신 `Growth`는 환경과 프로그램이 서로 영향을 주는 feedback loop를 함축함
- 방향적인 메타퍼가 필요하다면 `known-to-unknown` 이 효과적인 설명임, 우리가 이미 지식과 경험을 가지고 있고 (known), 그걸로 개발과정에서 무언가 배우길 기대한다는 (unknown) 것을 의미함
  -> 테스트 작성의 방향성?

### Starter Test

어떤 테스트 부터 시작해야할까? `operation 이 아무것도 하지 않는 것을 먼저 테스트해라`

1. 이 새로운 operation은 어디에 속하는가? 를 먼저 생각해야함
   - 최대한 빠른게 피드백 (red/green/refactor)를 얻기위해서 한 번에 하나의 질문만 생각하기
   - 결과를 알아차리기 쉬운 입력과 추력을 사용하면 피드백(결과)을 빠르게 받을 수 있다.
   - Polygon reducer test-first 예시
     - "입력은 다각형 그물이고, 출력은 정확하게 똑같은 표면이면서 가능한 한 최소 개수의 다각형으로 구성된 다각형 그물이 된다" 라는 문제
     - output: input 과 같아야함. 어떤 다각형등은 더이상 줄일 수 없을 수 있음
     - input: 가능하면 작아야함. 하나의 다각형 또는 아야 비어있는 다각형 목록
   ```
   Recuder r = new Reducer(new Polygon());
   assertEquals(0, reducer.result().npoints);
   ```
   - 첫번째 테스트가 돌아감 -> 이제 리스트에 있는 나머지 테스트 작성을 시작하면 됨
   - 위의 `One step test` 가 여기서도 적용됨 -> 가장 빠르게 작동시킬 수 있는 것을 찾아 시작함

### Explanation Test

자동화된 테스트가 널리 쓰이게 하려면 어떻게 해야할까? 나만 TDD를 사용한다고 다른 사람에게 강요할 순 없음 -> 테스트를 통해 설명을 요청하고 테스트를 통해 설명해라

### Learning Test

외부에서 만든 소프트웨어에 대한 테스트 작성을 해야할 때도 있을까? 패키지의 새로운 기능을 처음으로 사용해보기 전에 작성할 수 있음
Learning test가 작성되는 방식 :

- 패키지 새 버전이 나오면, 우선 테스트를 실행함 (필요하다면 수정해줌)
- 만약 테스트가 통과되지 않으면, 애플리케이션도 실행한될 것이기 때문에 실행해볼 필요도 없음
- 테스트가 통과하면 애플리케이션도 항상 실행될 것

### Another Test

어떻게 토픽에서 벗어나지 않고 테크니컬 논의를 이어갈 수 있을까? 토픽과 관계없는 아이디어가 떠오르면 리스트에 추가하고 토픽으로 다시 돌아가자

### Regression Test

결함이 발견되었을때 가장 먼저 해야할 일은 무엇일까? 그 결함으로 인해 실패하는 테스트, 그리고 고쳐진 후에는 통과하면 고쳐진거라고 볼 수 있는 가장 작은 테스트를 작성해라
Regression test는 사실 코딩할때 작성했어야 하는 테스트임
애플리케이션 전체 레벨의 테스트를 통해 가치를 얻을 수도 있음. 애플리케이션 단위의 테스트는 시스템의 사용자가 무엇을 기대했으며 무엇이 잘못되어 있는지 말할 기회를 준다. 좀더 작은 단위의 regression test는 테스트를 개선하는 방법이다
결함을 쉽게 나눌 수 없다면 시스템을 리팩토링해야 한다는 뜻이고 즉, 시스템 디자인이 아직 안끝났다는 얘기다.

### Break

피곤하고 뭔가에 stuck 되어 있을 땐? 쉬어라!
잠시 쉬고 거리를 두면 생각지 못했던 아이디어가 떠오를 수 있다.
그러고도 아이디어가 떠오르지 않는다면, 목표를 다시 점검해봐라. 현실적인 목표인지 새로운 목표가 필요한지
`Shower Methodology`
TDD는 이 방법론과 같음 -> 명백한 실행방법이 있으면 작성하지만, 그렇지 않다면 가짜로 만들어라.
디자인이 명확하지 않다면 triangulate

### Do Over

길을 잃은 느낌이 들때는 어떻게 할까? 다 버리고 다시 시작해라

### Cheap Dest, Nice Chair

TDD를 할 때 어떤 물리적 환경이 적절한가? 나머지 시설은 싸구려를 쓸지라도 정말 좋은 의자를 구해라.
