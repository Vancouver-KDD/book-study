# Chapter 6. Equality for All, Redux

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
- **Common equals**
- Common times

1. 두개의 중복되는 코드는 clean up 하기 위해서 공통적인 `Money` 라는 superclass를 만들것임

```
class Money
```

2. `Dollar` 를 `Money` class를 extends 해서 다시 만듦

```
class Dollar extends Money {
    private int amount;
}
```

3. `amount` instance varibale 을 `Money` 로 이동시킴

```
class Money {
    protected int amount;
}
```

```
class Dollar extends Money {
}
```

`private`가 `Money` class 안에서는 `protected` 로 변경되야 subclass 들이 사용할 수 있음

4. `equals()` `Money`로 이동시키기

```
public boolean equals(Object object) {
    Money money = (Money) object;
    return amount == money.amount;
}
```

5. `Franc.equals()` 제거하기

- 제거하기 전에, 우리 test code는 Franc 과 Franc 을 비교하는 코드가 없음
- TDD를 구현할때 모두 커버하는 적절한 테스트가 없으면 refactoring 과정에서 오류를 만들 수 있음
- 먼저 Franc 도 커버할 수 있는 테스트를 작성해 줘야함

```
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
    assertFalse(new Dollar(5).equals(new Dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6)));
}
```

`Franc.equals()` 와 `Money.equals()`가 거의 같음 -> `Franc.equals()` 를 `Money.equlas()` 와 완전히 같게 만들어도 작동한다면 우리는 `Franc` 에서 `equals()`를 제거해도 상관없어짐

`Franc` class

```
public boolean equals(Object object) {
    Franc franc= (Franc) object;
    return amount == franc.amount;
}
```

change to

```
public boolean equals(Object object) {
    Money money= (Money) object;
    return amount == money.amount;
}
```

-> 이제 `Franc.equals()` 와 `Money.equals()` 가 완전히 같으므로 `Franc` 에서 `equals()` 를 삭제해줘도 테스트는 통과함
