# CH 8. Makin' Objects

Goal: Dollar/Franc duplication in method `times()`.

`Franc`과 `Dollar`의 클래스를 보시면, `times()`메소드는 비슷한 모습을 하고 있습니다.

```java
// Franc
Franc times(int multiplier) {
    return new Franc(amount * multiplier);
}

// Dollar
Dollar times(int multiplier) {
    return new Dollar(amount * multiplier);
}
```

두 메소드를 조금더 연관짓기 위해서, Money를 리턴 타입으로 정하지만,
실제적으로 의미는 없습니다.

```java
// Franc
Money times(int multiplier) {
    return new Franc(amount * multiplier);
}

// Dollar
Money times(int multiplier) {
    return new Dollar(amount * multiplier);
}
```

서브클래스에 대한 참조가 적다면, 서브클래스를 제거하기에 한 걸음 더 가까워질 수 있습니다. Money에 Dollar를 반환하는 팩토리 메서드를 도입할 수 있습니다. 다음과 같이 사용할 수 있습니다:

```java
public void testMultiplication() {
    Money five = Money.dollar(5);
    assertEquals(new Dollar(10), five.times(2));
    assertEquals(new Dollar(15), five.times(3));
}
```

```java
static Dollar dollar(int amount) {
    return new Dollar(amount);
}
```

이제 이전보다 약간 더 나은 상태에 있습니다. 클라이언트 코드는 Dollar라는 서브클래스가 있는지 알지 못합니다. 테스트를 서브클래스의 존재와 decoupling함으로서, 우리는 코드에 영향을 주지 않고 상속을 변경할 자유를 얻었습니다.

```java
public void testMultiplication() {
    Money five = Money.dollar(5);
    assertEquals(Money.dollar(10), five.times(2));
    assertEquals(Money.dollar(15), five.times(3));
}

public void testEquality() {
    assertTrue(Money.dollar(5).equals(Money.dollar(5)));
    assertFalse(Money.dollar(5).equals(Money.dollar(6)));
    assertTrue(Money.franc(5).equals(Money.franc(5)));
    assertFalse(Money.franc(5).equals(Money.franc(6)));
    assertFalse(Money.franc(5).equals(Money.dollar(5)));
}
```
