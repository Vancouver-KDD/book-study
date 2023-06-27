# Chapter 30. Design Patterns

TDD에서 디자인은 디자인 패턴과는 조금 다른 관점이 필요함

### Command

Make an object representing the invocation
Seed it with all the parameters the computation will need
When the invoke is needed, use generic protocol, like `run()`

### Value Object

널리 공유되어 쓰이지만 identity는 중요하지 않은 객체를 어떻게 디자인하는가?
Set their state when they are created and never change it
여러개의 객체가 하나의 reference를 공유한다면, 한쪽에서 그 reference가 변경 되었어도 다른 객체에서 알 방법이 없음

- reference에 의존하지말고 항상 copy를 만들어 사용
- observer: 변경이 일어났을때 알려주는 역할
- treat the object as less than an object -> value object

### Null Object

객체를 사용해서 특별한 경우를 어떻게 나타내나? 특별한 경우를 대표하는 객체를 만들고 보통의 객체와 같은 프로토콜을 줘라

### Template Method

작업 순서는 변하지 않지만 미래의 개선 가능성을 열어두고 싶을땐? 다른 메서드를 실행시키기만 하는 method를 만들어라. Superclass를 다른 methods를 부르기만 하도록 만들고 subclass 에서 다른 방식으로 methods를 실행히키게 함

### Pluggable Object

어떻게 변수를 표현하는가? 가장 쉬운 방법은 조건을 이용하는 것
TDD의 두번째 수칙이 중복을 제거하는것 -> 조건문을 두번째 볼때가 바로 pluggable object 를 사용해야 할 때

### Pluggable Selector

instances 별로 서로 다른 메서드가 동적으로 호출되게 하려면? 메서드의 이름을 저장하고 있다가 그 이름에 해당하는 메서드를 동적으로 호출
한가지 대안은 switch 문을 갖는 하나의 클래스를 만듦 -> 필드 값에 따라 서로 다른 메서드를 호출
메서드 이름이 세 다른 곳에서 쓰여짐

- 인스턴스 생성되는 곳
- switch 문
- method itself

그래서 새로운 메서드를 만들때마다 이 세곳에 모두 추가 되었는지 확인해야함
Pluggable selector가 메서드를 다이나믹하게 호출할 수 있음

```
void print() {
Method runMethod= getClass().getMethod(printMessage, null);
runMethod.invoke(this, new Class[0]);
}
```

직관적인 상황에서 메서드를 한 개만 가지는 하위 클래스들이 많이 존재할때만 코드를 정리하는 용도로 사용하는게 좋음 (남용금지)

### Factory Method

새 객체를 만들 때 유연성을 원하는 경우? 생성자대신 일반 메서드에서 객체 생성
메서드를 추가함으로써 테스트를 변경하지 않고 다른 클래스의 인스턴스를 반환할 수 있는 유연함을 얻음 -> 팩토리 메서드
단점: 메서드가 생성자처럼 생기지 않았지만 거기서 객체를 만들어낸다는 걸 기억해야함 -> 유연성이 필요할 때만 팩토리 메서드를 사용, 그렇지 않으면 생성자 사용

### Imposter

기존의 계산에 새로운 변형을 도입하려면? 기존의 객체와 같은 프로토콜을 갖지만 구현은 다른 새로운 객체를 추가함
sturcture, object가 이미있고 시스템이 뭔가 다른걸 하길 원할때, 조건문을 넣을 수 있는게 명확하고 다른 곳에 잇는 로직을 복제하는게 아니라면 조건문을 사용해서 바꿀 수 있다.
하지만 변형은 종종 여러 메서드의 변경을 필요로 함
리팩토링 동안 나타나는 imposters 예시

- Null object : 데이터가 없을때 있는 것 처럼 보이게 할 수 잇음
- Composite: a collection of objects 를 하나의 object 처럼 다룰 수 잇음

### Composite

하나의 객체가 다른 객체 목록의 행위를 조합한 것처럼 행동하게 만들려면? 객체 집함을 나타내는 객체를 하나의 객체에 대한 imposter 로 구현
Composite 를 적용하는 것은 프로그래머의 트릭임

### Collecting Parameter

여러 객체에 나눠져 있는 오퍼레이션 결과를 수집하고 싶으면? 결과가 수집 될 수 있는 오퍼레이션에 파라미터를 더해라
Collecting parameter 를 더하는 것은 composite 의 흔한 결과임

### Singleton

글로벌 변수를 제공하지 않는 언어에서 글로벌 변수를 사용하려면? 사용하지마라!
