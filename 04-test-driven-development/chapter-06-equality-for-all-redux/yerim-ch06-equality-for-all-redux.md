# Ch 06. Equality for All, Redux

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
    - Dollar/Franc duplication
    - **Common equals**
    - Common times

우리는 챕터5에서 속도를 빨리 내기 위해 수많은 코드를 복사하고 붙여넣는 데에 엄청난 죄를 지었다. 이제 그것들을 청소할 시간이다. 

한 가지 가능성은 우리 수업 중 하나가 다른 하나를 확장하도록 하는 것이다. 나는 그것을 시도했고, 그것은 어떤 코드도 거의 저장하지 않는다. 대신, 우리는 Figure 6.1과 같이 두 클래스에 대한 공통 슈퍼클래스를 찾을 것이다. 

- Figure 6.1 A common superclass for two classes
    - Dollor - Franc
    - Money
        - Dollar
        - Franc

만약 공통의 `equals` 코드를 캠쳐할 수 있는 `Money` 클래스가 있다면 어떨까? 우리는 작은 것부터 시작할 수 있다. 

```
class Money
```
모든 테스트는 여전히 실행된다. 우리가 어떤 것도 깨트릴 수 있다는 것은 아니지만, 어쨌든 테스트를 실행하기에는 좋은 시간이다. `Dollar`는 `Money`를 확장하며, 그것은 어떤 것도 깨트릴 수 없다.

```
class Dollar extends Money {
    private int amount;
}
```
깨트릴 수 있는가? 아니다, 테스틑 아직도 모두 잘 작동한다. 이제 `amount` 인스턴스를 `Money`로 옮겨보자.
```
class Money {
    protected int amount;
}

class Dollar extends Money {
}
```
서브클래스가 여전히 볼 수 있도록 visibility를 `private`에서 `protected`로 변경한다. 이제 우리는 `equals()` 코드를 올라갈 준비를 할 수 있다. 먼저 임시 변수의 선언을 변경한다.
```
public boolean equals(Object object) {
    Money dollar = (Dollar) object;
    return amount == dollar.amount;
}
```
cast를 변경한다.
```
public boolean equals(Object object) {
    Money dollar = (Money) object;
    return amount == dollar.amount;
}
```
소통하기 위해서, 우리는 임시 변수의 이름을 변경해야한다.
```
public boolean equals(Object object) {
    Money money = (Money) object;
    return amount == money.amount;
}
```
우리는 이제 `Dollar`에서 `Money`로 바꿀 수 있다.
```
public boolean equals(Object object) {
    Money money = (Money) object;
    return amount == money.amount;
}
```
이제 우리는 `Franc.equals()`를 제거해야 한다. 먼저 우리는 equality에 대한 테스트가 프랑과 프랑을 비교하는 것을 다루지 않는다는 것을 알게 되었다. 코드를 변경하기 전에, 우리는 애초에 거기에 있었어야 했던 테스트를 작성할 것이다. 

테스트는 리팩토링할 때의 실수를 방지해 줄 수 있다. 그렇지 않으면, 리팩토링하는 동안 다른 로직들을 깨트릴 수 있고, 그러면 리팩토링에 대한 두려움이 생길 것이다. 그러므로 리팩토링하기 전에 테스트 코드를 작성하자!

운 좋게도, 이 테스트는 매우 작성하기 쉽다. 복붙하자.
```
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
    assertFalse(new Dollar(5).equals(new Dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6)));
}
```
두 줄이나 더 많은 중복이 생겼다. 이것도 나중에 리팩토링할 예정.
`Franc`도 `Dollar`처럼 `Money`를 extend 하자.
```
class Franc extends Money {
    private int amount;
}
```

```
class Franc extends Money {
}
```

```
public boolean equals(Object object) {
    Money franc = (Franc) object;
    return amount == franc.amount;
}
```

```
public boolean equals(Object object) {
    Money franc = (Money) object;
    return amount == franc.amount;
}
```

```
public boolean equals(Object object) {
    Money money = (Money) object;
    return amount == money.amount;
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
    - Dollar/Franc duplication
    - ~~Common equals~~
    - Common times
    - Compare Franc with Dollars

이제는 `Franc.equals()`와 `Money.equals()`의 차이가 전혀 없으므로 우리는 중복 구현을 지울 것이다. 테스트는 잘 실행되었다. 

`Franc`와 `Dollar`를 비교하면 어떻게 될까? 챕터7 에서 다루도록 하자.