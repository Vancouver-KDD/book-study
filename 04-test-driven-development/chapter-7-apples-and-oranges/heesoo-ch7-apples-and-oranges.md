# Chapter 7. Apples and Oranges

### Todo List

- $5 + 10 CHF = $10 if rate is 2:1
- ~~$5 \* 2 = $10~~
- ~~Make "amount" private~~
- ~~Dollar side-effects?~~
- Money rounding?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
- ~~5CHF \* 2 = 10CHF~~
- Dollar/Franc duplication
- ~~Common equals~~
- Common times
- **Compare Francs with Dollars**

chapter 6 에서 새로운 의문점이 제기됨 : Franc 이랑 Dollar 를 비교하면 어떻게 될까?
-> Todo list 에 추가해서 알아보자

```
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
    assertFalse(new Dollar(5).equals(new Dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6)));
    assertFalse(new Franc(5).equals(new Dollar(5)));
}
```

`assertFalse` 테스트는 실패한다.
두 `Money` 는 그들의 `amounts`와 `class`가 모두 같을 때에만 같은 것임

```
public boolean equals(Object object) {
    Money money = (Money) object;
    return amount == money.amount
    && getClass().equals(money.getClass());
}
```
