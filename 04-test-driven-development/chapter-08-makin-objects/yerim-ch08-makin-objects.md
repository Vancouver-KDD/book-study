# Ch08. Mackin' Objects

- TODO LIST
    - $5 + 10 CHF = $10 if rate is 2:1
    - ~~$5 * 2 = $10~~
    - ~~Make "amount" private~~
    - ~~Dollar side-effects?~~
    - Money rounding?
    - ~~equals()~~
    - hashCode()
    - Equal null
    - Equal object
    - ~~5 CHF * 2 = 10 CHF~~
    - **Dollar/Franc duplication**
    - ~~Common equals~~
    - Common times
    - ~~Compare Francs with Dollars~~
    - Currency?

두 군데의 `times()` 구현은 매우 비슷하다.

```
Franc times(int multiplier) {
    return new Franc(amount * multiplier);
}

Dollar times(int multiplier) {
    return new Dollar(amount * multiplier);
}
```
두 개의 함수를 일치시키기 위해 둘 다 `Money`를 리턴하게 만들어 보자.

```
Money times(int multiplier) {
    return new Franc(amount * multiplier);
}

Money times(int multiplier) {
    return new Dollar(amount * multiplier);
}
```

다음 단계는 분명하지 않다. `Money`의 두 서브클래스는 그들의 존재를 정당화하기에는 충분한 역할을 하지 않기 때문에, 우리는 그것을 제거하고 싶다. 하지만, 하나의 큰 스텝으로 할 수는 없다. 왜냐하면 그것은 TDD의 매우 효과적인 데모가 아니기 때문이다.

서브클래스에 참조가 더 적다면 우리는 서브클래스를 제거하는 데 한 걸음 더 가까워질 것이다. 우리는 `Money`에 `Dollar`를 리턴하는 팩토리 메서드를 도입해볼 수 있다. 

```
public void testMultiplication() {
    Dollar five = Money.dollar(5);
    assertEquals(new Dollar(10), five.times(2));
    assertEquals(new Dollar(15), five.times(3));
}
```

`Dollar`를 생성하고 리턴하도록 구현한다.

```
// Money
static Dollar dollar(int amount) {
    return new Dollar(amount);
}
```
하지만 우리는 `Dollar`에 대한 참조들이 사라지기를 원하므로 테스트 코드에 있는 선언은 바꿔줘야 한다.
```
public void testMultiplication() {
    Money five = Money.dollar(5);
    assertEquals(new Dollar(10), five.times(2));
    assertEquals(new Dollar(15), five.times(3));
}
```
테스트 코드를 돌려보면, 컴파일러가 `times()`가 `Money`를 위해서 정의되지 않았다고 알려줄 것이다. 우리는 아직 그것을 구현할 준비가 되지 않았으므로, `Money` abstract를 만들고 `Money.times()`를 선언해보자.

```
abstract class Money
abstract Money times(int multiplier);
```
이제 아까 선언한 팩토리 메서드를 변경해보자.
```
static Money dollar(int amount) {
    return new Dollar(amount);
}
```
모든 테스트가 모두 실행되었고, 적어도 우리는 아무것도 망가트리지 않았다. 이제 이 팩토리 메서드를 다른 테스트 코드에서도 사용할 수 있다.

```
public void testMultiplication() {
    Money five = Money.dollar(5);
    assertEquals(Money.dollar(10), five.times(2));
    assertEquals(Money.dollar(15), five.times(3));
}

public void testEquality() {
    assertTrue(Money.dollar(5).equals(Money.dollar(5)));
    assertFalse(Money.dollar(5).equals(Money.dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6)));
    assertFalse(new Franc(5).equals(Money.dollar(5)));
}
```
이제는 어떤 클라이언트 코드도 `Dollar` 서브 클래스라는 하위클래스가 있다는 것을 알지 못한다. 서브클래스 존재와 테스트를 분리(decoupling)함으로써 우리는 모델 코드에 영향을 미치지 않고 상속을 변경할 수 있는 자유를 주었다.

우리가 맹목적으로 `testFrancMultiplication` 테스트 코드를 바꾸기 전에, 우리는 달러 곱셈에 대한 테스트에 의해 테스트되지 않은 어떤 논리도 테스트하지 않는다는 것을 알아차렸다. 이 말인 즉슨, 프랑 곱셈이나 달러 곱셈이나 결국 같은 논리이기 때문에 중복이라는 의미이다. 그렇다고 테스트를 삭제하면, 코드에 대한 신뢰를 잃게 될까? 아직 조금 두려우니, 그대로 두겠지만 매우 의심스럽다.
```
public void testEquality() {
    assertTrue(Money.dollar(5).equals(Money.dollar(5)));
    assertFalse(Money.dollar(5).equals(Money.dollar(6)));
    assertTrue(Money.franc(5).equals(Money.franc(5)));
    assertFalse(Money.franc(5).equals(Money.franc(6)));
    assertFalse(Money.franc(5).equals(Money.dollar(5)));
}

public void testMultiplication() {
    Money five = Money.franc(5);
    assertEquals(Money.franc(10), five.times(2));
    assertEquals(Money.franc(15), five.times(3));
}
```
팩토리 메서드는 `Money.dollar();`와 같다.
```
// Money
static Money franc(int amount) {
    return new Franc(amount);
}
```

- TODO LIST
    - $5 + 10 CHF = $10 if rate is 2:1
    - ~~$5 * 2 = $10~~
    - ~~Make "amount" private~~
    - ~~Dollar side-effects?~~
    - Money rounding?
    - ~~equals()~~
    - hashCode()
    - Equal null
    - Equal object
    - ~~5 CHF * 2 = 10 CHF~~
    - **Dollar/Franc duplication**
    - ~~Common equals~~
    - Common times
    - ~~Compare Francs with Dollars~~
    - Currency?
    - Delete testFrancMultiplication?

다음에는 `times()`중복을 제거할 것이다.

이번 챕터를 리뷰해보자!
- Took a step toward eliminating duplication by reconsiling the signatures of two variants of the same method -- `times()`
- Moved at least a declaration of the method to the common superclass
- Decoupled test code from the existence of concrete subclasses by introducing factory methods
- Noticed that when the subclasses disappear some tests will be redundant, but took no action