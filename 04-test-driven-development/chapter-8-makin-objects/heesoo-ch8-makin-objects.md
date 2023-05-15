# Chapter 8. Makin' Objects

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
- **Dollar/Franc duplication**
- ~~Common equals~~
- Common times
- ~~Compare Francs with Dollars~~
- Currency?

`Franc` 과 `Dollar` 의 `times()` 가 매우 유사함
-> `Money`로 하나씩 이동시킬 것임
-> `Money` 클래스 안에 `Dollar`를 리턴하는 method를 만듦

```
public void testMultiplication() {
    Dollar five = Money.dollar(5);
    assertEquals(new Dollar(10), five.times(2));
    assertEquals(new Dollar(15), five.times(3));
}
```

Money

```
static Dollar dollar(int amount) {
    return new Dollar(amount);
}
```

테스트 코드에서 Dollar references를 제거하고 싶음

```
public void testMultiplication() {
    Money five = Money.dollar(5);
    assertEquals(new Dollar(10), five.times(2));
    assertEquals(new Dollar(15), five.times(3));
}
```

-> `Money` 는 `times()`가 정의되지 않았다고 에러남 -> `Money.times()` 정의해주기
Money

```
abstract class Money
abstract Money times(int multiplier);
```

```
static Money dollar(int amount) {
    return new Dollar(amount);
}
```

`testFrancMultiplication` 도 `Dollar`와 똑같이 실행할 수 있음

```
public void testEquality() {
    assertTrue(Money.dollar(5).equals(Money.dollar(5)));
    assertFalse(Money.dollar(5).equals(Money.dollar(6)));
    assertTrue(Money.franc(5).equals(Money.franc(5)));
    assertFalse(Money.franc(5).equals(Money.franc(6)));
    assertFalse(Money.franc(5).equals(Money.dollar(5)));
}
public void testFrancMultiplication() {
    Money five = Money.franc(5);
    assertEquals(Money.franc(10), five.times(2));
    assertEquals(Money.franc(15), five.times(3));
}
```

```
static Money franc(int amount) {
    return new Franc(amount);
}
```

- method 정의를 공통의 superclass로 옮겨야함
- subclass에서 테스트 코드를 분리시켜야 함?
- subclass에서 사라지면 어떤 테스트들은 불필요하게 된다. 그렇지만 테스트를 삭제하지는 말자.
