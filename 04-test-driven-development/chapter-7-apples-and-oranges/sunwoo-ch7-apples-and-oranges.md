# CH 7. Apples and Oranges

Goal: Compare Francs with Dollars

chapter 6에서 duplication을 줄이는 과정에서, 의도치 않은 메소드의 행동에 변화가 있었습니다.
만약 아래의 테스트 코드와 같이 그 변경을 잡아 줄 수 있는 테스트 코드가 있다면, 바로 그 변화에 대응 할 수 있습니다.

```Java
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
    assertFalse(new Dollar(5).equals(new Dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6)));
    assertFalse(new Franc(5).equals(new Dollar(5))); // will fail
}
```

자바의 object를 사용하는 것이 아닌 금융 도메인에서 의미 있는 기준을 사용하는 것이 더 바람직하지만, 현재 구현에서 화폐와 같은 개념이 없으므로, 이대로 class를 비교하는 코드를 사용하겠습니다.

```Java
public boolean equals(Object object) {
    Money money = (Money) object;
    return amount == money.amount
        && getClass().equals(money.getClass());
}
```
