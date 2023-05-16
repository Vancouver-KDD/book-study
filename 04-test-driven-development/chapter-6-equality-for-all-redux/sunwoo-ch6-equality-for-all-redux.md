# Ch 6. Equality for All, Redux

철저히 준비하고, 깨끗한 코드를 만드는 것도 중요하지만,
작동하는 혹은 실제로 사용되는 코드를 만들지 않는 한 의미가 없습니다.
우리는 위의 챕터에서 `Dollar`의 코드를 `Franc`에 복사해 넣음으로서,
최소한으로 작동하는 코드를 만들어 이러한 문제점을 피했습니다.

이번 챕터에서는 polymorphism을 사용하여서, `Dollar`와 `Franc`에서 반복되는 코드를 줄여보도록 하겠습니다.

polymorphism? 동일한 인터페이스를 통해 여러 개체 타입을 다룰 수 있는 능력을 가리킵니다. 즉, polymorphism은 동일한 메서드를 호출하는데 있어서 실제로 실행되는 메서드가 호출된 객체의 타입에 따라 달라질 수 있는 성질을 의미합니다.

```Java
class Money {
    protected int amount;
}
```

```java
class Dollar extends Money {
    public boolean equals(Object object) {
        Money money= (Money) object;
        return amount == money.amount;
    }
}
```

```Java
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
    assertFalse(new Dollar(5).equals(new Dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6)));
}
```

이렇게 amount를 위로 올리므로서 코드의 양을 줄일수 있었지만,
`Franc.equals()` 와 `Money.equals()`의 메소드에 구별이 없어졌습니다.
이렇게 duplication을 없앨때, logic의 변화가 생길수 있으므로 주의 해야합니다.
