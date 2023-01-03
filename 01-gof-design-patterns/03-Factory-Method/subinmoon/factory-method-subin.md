# [Subin] Factory Method

> 문제 제기

배송 업무를 관리하는 앱을 만들었다고 생각해보자. 앱의 첫 번째 버전에서는, `Truck` 만 가지고 배송 업무를 진행했기 때문에 모든 코드들이 `Truck` 클래스 내부에 있었다. 

하지만 얼마 되지 않아 어플리케이션이 유명해지면서 항만을 통한 배송 업무를 앱에 추가해야 한다. 지금까지는 `Truck` 클래스에서 모든 코드를 실행해왔는데, 이제와서 `Ships` 클래스를 새로 만든다면, 코드 베이스 전체를 갈아엎어야 할지도 모른다. 이후 항공과 같이 또 다른 배송 방법이 추가된다면 그때도 코드 '대공사'가 필요할 것이다.

> 해결책

Factory Method는 이러한 직접적인 객체 construction call을 대체할 수 있도록 도와준다. 객체들은 여전히 `new` 키워드를 사용해 생성하되, Factory method 안에서 호출될 것이다. 이렇게 생성되어 반환된 객체들이 *product*로 불린다.

## What is Factory Method?
Factory Method는 Creational 디자인 패턴 중 하나로, superclass(상위 클래스)에서 객체를 생성하는 데 필요한 interface를 제공한다. 

간단하게 생각하면, constructor call을 상위 클래스에서 하위 클래스오 옮긴 것 뿐이다. 하지만 하위 클래스에서 Factory method를 override 할 수 있게 되면서, product가 생성되는 method의 위치를 바꿀 수 있다. 하위 클래스에서 서로 다른 타입의 product를 생성할 수 있지만, 이때 반환되는 product들은 반드시 base class나 interface를 공유하고 있어야 한다. 또 base class에 있는 factory method는 interface로서 자체적인 반환 타입을 선언해야 한다. 

예를 들어, `Truck`과 `Ship` 두 클래스는 `Transport`라는 상위 클래스를 implement한 하위 클래스이다. 이 두 하위 클래스에서 `deliver()`라는 메소드를 호출하고자 한다. 이때 각 하위 클래스마다 `deliver()` 메소드를 다른 방식으로 부르려고 한다. 트럭은 육지에서 배송하고, 배는 바다 위에서 배송해야 한다. 따라서 트럭 객체는  `RoadLogistics` 클래스의 factory method에서 반환되어야 하고, 배 객체는 `SeaLogistics` 클래스의 factory method에서 반환되어야 한다.

### Factory Method은 다음과 같은 경우에 사용된다:
- 클래스 차원에서 이후 생성될 객체의 클래스를 예상할 수 없는 경우
- 유저가 내부 component들을 확장할 수 있는 library나 franework를 제공하고자 할 때
- 이미 존재하는 객체들을 새로 만들지 않고 재활용할 수 있도록 시스템 리소스를 저장하고자 할 때

### Factory Method의 구성
- Product
  - Factory method가 생성하는 객체의 인터페이스를 정의
- ConcreteProduct
  - Product 인터페이스를 구현
- Creator
  - Product 타입 객체를 반환하는 factory method를 선언. 또한, Creator는 factory method의 default implementation을 정이. 여기서 default implementation은 default ConcreteProduct 객체를 반환.
  - Product 객체를 생성하기 위해 factory method를 호출
- ConcreteCreator
  - ConcreteProduct 인스턴스를 생성하기 위해 factory method를 override

## Advantage
- Creator와 ConcreteProduct 간의 느슨한 coupling
- *Single Responsibility Principle*, product를 생성하는 코드를 한 곳에 모아둠으로써 코드 보수 유지성이 증가
- *Open/Closed Principle*, 새로운 타입의 product를 프로그램에 추가할 때 기존 클라이언트의 코드를 건드리지 않음

## Disadvantage
- 코드가 비교적 복잡할 수 있음 
  - 패턴을 구현하기 위해 새로운 하위 클래스들이 많이 생길 수 있 

## Sources
- Erich Gamma, John Vlissides, Ralph Johnson, Richard Helm, *Design Patterns: Elements of Reusable Object-Oriented Software*, Addison-Wesley, 1994, pp.107-116.
- https://refactoring.guru/design-patterns/factory-method
- https://www.geeksforgeeks.org/factory-method-design-pattern-in-java/