# Chapter 16. Refactoring SerialDate

이 챕터에서는 David Gilbert가 작성한 JCommon 라이브러리 안의 SerialDate와, SerialDateTest클래스에 대해 리팩토링을 진행할 것이다.

#### First, Make It Work
현재의 SerialDateTest의 coverage는 50%가 채 되지 않는다. 185개의 실행가능한 코드 중에서 오직 91개만이 테스트 된다. 리팩토링을 진행하기 위해서는 테스트코드의 더 높은 coverage가 필요하다. 저자는 기존의 테스트 클래스를 기반으로 자기만의 테스트 코드를 작성했다. 기존의 많은 테스트 코드들이 예상과는 달리 통과하지 못했고, 그 테스트들을 성공시키기 위해 SerialDate도 리팩토링했다.

먼저 코드가 동작할 수 있도록 리팩토링 한 내용은 다음과 같다

- Insufficient tests: 코드가 원하는대로 동작하기 위해 필요한 테스트 커버리지를 맞춰야 한다
- Dead function: 호출되지 않는 메소드들을 지운다
- Use a Coverage Tool: 테스트 커버리지를 맞추기 위해 Coverage tool을 이용해라. 웬만한 IDE는 모두 이 기능을 지원한다
- Obvious Behavior is Unimplemented: 다른 사람들이 코드를 읽었을 때 명확하게 있어야 하는 동작을 항상 제공하자. 직감적으로 있어야 하는 코드의 부재는 신빙성을 떨어뜨린다
- Don't Skip Trivial Tests: 작은 테스트들을 빠트리지 말자
- Incorrect Behaviour at the Boundaries, Test Boundary Conditions: 모든 corner와 boundary들에 대해 직감적으로 우리가 원하는대로 동작하겠거니 생각 말고, 빠트리지 않고 테스트 할 수 있도록 하자
- Exhaustively Test Near Bugs: 버그는 쉽게 발견되지 않으려고 한다. 버그를 하나 찾았다면 해당 메소드에 대해서 모든 경우의 수를 테스트하도록 하자
- Patterns of Failure Are Revealing: 실패하는 테스트의 패턴을 찾아냄으로써 문제를 찾아낼 수 있다
- Test Coverage Patterns Can Be Revealing: 어떤 코드들이 실행되고 또는 실행되지 않는 지 들여다보면 왜 실패하는 테스트들이 실패를 거듭하는지 알 수 있다

#### Then Make It Right
다음으로, 코드가 제대로 동작할 수 있도록 리팩토링 한 내용은 다음과 같다

- Inappropriate Information: 커멘트는 코드 자체와 디자인에 대한 기술적인 내용에 대한 공간이어야 한다. 코드 변경/업데이트 사항 같은 것들을 포함하지 않도록 하자
- Avoid Long Import Lists By Using Wildcards: 와일드카드(*)를 이용해서 긴 목록의 import를 피하도록 하자
- Multiple languages in One Source File: 한 파일에는 하나의 언어만을 사용해서 코드를 작성하도록 하자
- Choose Descriptive Names: 이름은 항상 충분한 의미를 가질 수 있도록 하자
- Choose Names at the Appropriate Level of Abstraction: 추상화한 레벨에 맞는 이름을 지어주도록 하자. 우리는 알맞은 추상화 정도를 찾는 걸 어려워하기 때문에 때로 너무 낮은 레벨의 이름을 지어주고는 한다. 
- Don't inherit Constants: Constants를 inheritance를 이용해 해결하는 방법은 지양하도록 하고, 대신에 static import를 사용하도록 하자
- Duplication: 중복을 찾아내서 최대한 없애도록 하자. 여러 방법 중 매 번 다른 방법을 써야 할 수도 있겠지만 보통 중복은 다른 subroutine이나 하나의 다른 class로 만들 수도 있다. 만약 비슷한 알고리즘을 사용하지만 공유하는 코드가 없는 경우에는 Template method 혹은 Strategy 패턴을 사용할 수 도 있다
- Overriden Safeties: 실패하는 테스트나, IDE의 경고를 절대 무시하지 말고 시간을 들여 해결하도록 하자
- Obsolete Comment: 해당 코드와 관계가 없거나 정확하지 않은 커멘트를 쓰지 않도록 하자. 만약 커멘트가 너무 오래 되어서 해당 코드와 연관성이 떨어진다면 업데이트하도록 하자
- Code at Wrong Level of Abstraction: 추상화 정도가 높은 일반적인 컨셉과 반대로 그 정도가 낮은 자세한 코드를 나누는 것은 굉장히 중요하다. 잘못된 추상화가 보이면 그냥 넘어가지 말고 모든 코드가 정확한 레벨의 추상화를 가질 수 있도록 하자
- Base Classes Depending on Their Derivatives: 부모와 자식 클래스를 나누는 이유는 부모 클래스의 컨셉(추상화 정도)과 자식 클래스의 컨셉을 확실하게 나누기 위해서이다. 만약 부모 클래스가 자식 클래스의 정보를 가지고 있다면 그것은 추상화에 문제가 있다는 신호이다.
- Constants versus Enums: Constant 대신 Enum을 사용하자!
- Redundant Comment: 만약 코드 자체가 스스로 설명이 가능할 정도라면 코멘트가 필요 없다. 꼭 필요한 경우에만 코멘트를 작성하도록 하자
- Too Much Information: 잘 작성된 모듈은 작은 코드로 많은 것을 할 수 있도록 하고, 그렇지 않은 모듈은 간단한 것들이 동작하기 위해 많은 코드를 필요로 한다. 잘 작성된 인터페이스는 너무 많지 않은 적당한 메소드들만 제공해 커플링을 낮춘다. 좋은 개발자는 인터페이스, 클래스 또는 모듈에서 노출되어야 하는 것들을 최소화시킨다
- Dead Code, Clutter: 사용되지 않는 코드는 발견했을 때 바로 지우도록 하자
- Vertical Separation: 변수와 메소드들은 사용되는 곳에서 멀지 않게 가까운 곳에 작성하도록 하자
- Inconsistency: 가독성과 확장성을 위해 코드를 작성하는 스타일을 항상 같게 고수하도록 하자
- Use Standard Nomenclature Where Possible: 프로젝트에 관련된 이름, 모두가 비슷한 방법으로 떠올릴 수 있는 이름을 쓸 수 있다면 최대한 사용할 수 있도록 하자
- Artificial Coupling: 서로 연관성이 없는 코드들의 커플링을 없애도록 하자. 우리는 떄로 사용하기 편하다는 이유로 적절하지 못한 커플링을 만들고는 한다. 심사숙고해서 메소드, constants, 변수들이 어디에서 declare되어야 하는지 결정하도록 하자
- Obscured Intent: 우리가 코드를 통해 나타내고자 하는 의도를 확실하게 하도록 하자
- Misplaced Responsibility: 코드가 어디에 작성되어야 하는지 잘 생각해서 있어야 하지 않을 곳에 작성하지 않도록 하자
- Inappropriate Static: Polymorphic하게 사용될 수 있는 메소드를 static으로 만들지 않도록 하자. 가능하다면 최대한 메소드는 non-static으로 만들도록 하자
- Use Explanatory Variables: 프로그램의 가독성을 높이는 좋은 방법 중 하나는 변수들에게 의미있는 이름을 부여하는 것이다
- Function Names Should Say What They Do: 만약 메소드가 어떤 동작을 수행하는 지 알기 위해 코드를 들여다 봐야 한다면, 메소드의 이름을 변경해야 하는 좋은 이유가 될 것이다
- Understand the Algorithm: 테스트를 모두 통과하는 코드만으로는 충분하지 않다. 해당 코드가 어떻게 동작하는지 확실하게 이해하고 해당 솔루션이 알맞은 지 알아야 한다
- Make Logical Dependencies Physical: 두 모듈이 서로에게 dependency를 가지고 있다면, 해당 dependency는 logical하지 않고 physical해야 한다. 즉 서로의 responsibility가 명확하게 나누어져 서로가 다른 역할을, 자기가 가져야 하는 역할만을 가질 수 있도록 해야 한다
- Prefer Polymorphism to If/Else or Switch/Case: 우리가 보통 Switch문을 쓰는 이유는 상황에 맞는 해결방법이라서가 아니라 명확하게 풀어낼 수 있는 방법이어서이다. Switch문을 쓰기 전에 polymorphism을 항상 고려해보자. 저자는 "ONE SWITCH" 규칙을 따르는데, 한 유형의 선택에는 하나의 switch문을 사용하고 나머지는 polymorphism을 이용해 해결한다
- Follow Standard Conventions: 모든 팀은 다른 프로그래머들이 사용하는 코딩 스탠다드를 사용하고, 한 팀안의 모든 팀원들은 같은 스탠다드/convention을 사용해야 한다
- Replace Magic Numbers with Named Constants: 매직 넘버(raw numbers)대신에 Constant를 사용하도록 하자

#### Conclusion
- 이 챕터에서 진행한 내용도 역시 Boy Scout Rule에 근거한다. David의 코드를 처음 가져왔을 때보다 더 깨끗하게 만들었다. 시간이 오래 걸리지만, 테스트 커버리지를 높이고, 버그를 고치고, 코드가 더 명확해졌기 떄문에 의미가 있다.
