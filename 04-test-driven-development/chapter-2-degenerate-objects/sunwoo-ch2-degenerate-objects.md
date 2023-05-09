# Chapter 2. Degenerate Objects

TDD의 목표는 clean하고 예상되로 작동하는 코드를 작성하는 것에 있습니다.

만약 테스트코드(requirements)가 아래와 같이 방식으로 작동되도록 만들고 싶다면 어떻게 해야 할까요?

```Java
public void testMultiplication() {
    Dollar five= new Dollar(5);
    five.times(2);
    assertEquals(10, five.amount);
    five.times(3);
    assertEquals(15, five.amount);
}
```

현재 `times`메소드는 Dollar의 상태를 변경하므로 더이상 `five` object는 5가 아니어버리게 됩니다.
그렇기 때문에 아래와 같이 코드와 같이 새로운 object를 리턴하고, 또한 times가 어떻게 작동하는지 테스트 코드내에서 명시된 것을 수정합니다.

```Java
public void testMultiplication() {
    Dollar five= new Dollar(5);
    Dollar product= five.times(2);
    assertEquals(10, product.amount);
    product= five.times(3);
    assertEquals(15, product.amount);
}
```

그리고 이 변경점에 따라서 다시 소스 코드를 변경합니다.

1. `times`의 인터페이스 변경

```Java
Dollar times(int multiplier) {
    amount *= multiplier;
    return null;
}
```

2. 새로운 오브젝트를 리턴

```Java
Dollar times(int multiplier) {
    return new Dollar(amount * multiplier);
}
```

첫 챕터에서 fake한 implementation을 구현하고 이것을 실제 구현으로 점진적으로 바꾸었다면, 이번에는 우리가 생각하는 코드의 behaviour를 테스트 코드를 통해 표현하고, 테스트를 실행해 볼수도 있다.
