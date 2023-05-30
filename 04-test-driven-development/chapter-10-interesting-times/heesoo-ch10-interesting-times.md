# Chapter 10. Interesting Times

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
- **Common times**
- ~~Compare Francs with Dollars~~
- ~~Currency?~~
- Delete testFrancMultiplication?

Franc 과 Dollar의 `times()`가 매우 비슷하지만 완전히 똑같지는 않음

```
// Franc
Money times(int multiplier) {
    return Money.franc(amount * multiplier);
}
```

```
// Dollar
Money times(int multiplier) {
    return Money.dollar(amount * multiplier);
}
```

현재 상황에서 이 둘을 완전 똑같이 만들수 있는 방법이 없음 -> 이 전 챕터에서 factory method 로 만들었던 것을 이 전 형태로 다시 돌려줌

```
// Franc
Money times(int multiplier) {
    return new Franc(amount * multiplier, "CHF");
}
```

```
// Dollar
Money times(int multiplier) {
    return new Dollar(amount * multiplier, "USD");
}
```

`Franc` 에서 우리는 `currency instacne variable` 이 항상 `CHF` 인 것을 알기 때문에 다음과 같이 바꿀 수 있음 -> `Dollar` 도 똑같음

```
// Franc
Money times(int multiplier) {
    return new Franc(amount * multiplier, currency);
}
```

```
// Dollar
Money times(int multiplier) {
    return new Dollar(amount * multiplier, currency);
}
```

여기서 우리가 new `Franc` 을 쓰는것과 `Money`를 쓰는 것에 차이가 있을까? 알아보기 위해서 `Franc.times()`를 `Franc` 대신에 `Money`를 리턴하게 한 후 테스트를 돌려보자
-> 컴파일러가 `Money` 는 concrete class 이여야 한다고 말함

```
// Money
class Money
Money times(int amount) {
    return null;
}
```

컴파일러를 통과하면 `"expected:<Money.Franc@31aebf> but was:<Money.Money@478a43>".` 라는 테스트 에러가 남
-> 더 나은 에러메세지를 위해 `toString()` 을 정의함

```
// Money
public String toString() {
    return amount + " " + currency;
}
```

?? `toString()` 코드를 테스트 코드 없이 써도 되는건가?

- 이건 단순하게 debug output을 보기 위해서 쓴거기 때문에 실패할 확률이 낮음
- 이미 테스트가 실패한 상태일때는 테스트를 작성할 필요가 없음

`toString()`을 통해 `"expected:<10 CHF> but was:<10 CHF>".` 이라는 더 나은 에러메세지를 볼 수 있음
-> 데이터는 올바른데, 클래스가 잘못 되어있음 `Franc 대신 Money`를 사용한 것
-> `equals()` 에서 문제가 발생한 것임

```
// Money
public boolean equals(Object object) {
    Money moeny = (Money) object;
    return amount == money.amount
        && getClass().equals(moeny.getClass());
}
```

- 우리가 진짜 체크해야하는 것은 클래스가 아니라 `currency`
- 테스트가 실패한 상태에서는 다른 테스트 코드를 작성하지 않는 걸 선호하지만, real model code 를 바꿀꺼기 때문에 테스트가 필요함
- conservative 방식으로 접근함 -> 테스트를 실패시키는 코드를 뺀다음 -> 테스트가 통과하면 -> equals() 를 위한 테스트를 변경하고 -> equals() 코드를 수정한 후 -> 원래 하려고 했던 change 를 시도할 수 있음 (`times`에서 `Franc` 대신 `Money`를 리턴시키는 것)

다시 Franc의 `times()`를 `Franc`을 리턴하는 코드로 돌림

```
// Franc
Money times(int multiplier) {
    return new Franc(amount * multiplier, currency)
}
```

이제 다시 테스트가 통과함
지금은 `Franc(10, "CHF")` 와 `Money(10, "CHF")` 가 같지 않다고 하는 상태 -> 우리는 같기를 예상함
-> 이걸 그대로 테스트 코드로 작성할 수 있음

```
public void testDifferentClassEquality() {
    assertTrue(new Money(10, "CHF")).equals(new Franc(10, "CHF"))
}

```

이 테스트 코드는 실패함. 왜냐면 `equals()`는 현재 class가 같은지 여부를 보고 있기 때문에 -> 우리가 원하는건 class가 같은게 아니라 `currency` 가 같은지 여부를 확인하고 싶음
-> `equals()` 코드를 변경해줌

```
// Money
public boolean equals(Object object) {
    Money moeny = (Money) object;
    return amount == money.amount
        && currency().equals(moeny.currency());
}
```

이제 `equals()` 가 해결되었으니 원래 변경하려고 했던 것 (`Franc.times()` 에서 `Money`를 리턴하게 하는 것)으로 돌아감

```
// Franc
Money times(int multiplier) {
    return new Money(amount * multiplier, currency)
}
```

테스트가 통과함 -> 달러에도 똑같이 적용시킴

```
// Dollar
Money times(int multiplier) {
return new Money(amount * multiplier, currency)
}

```

이제 `times()`가 완전히 똑같아짐 -> `Money`로 공통으로 옮길 수 있음

```
// Money
Money times(int multiplier) {
    return new Moeny(amount * multiplier, currency)
}
```

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
- ~~Common times~~
- ~~Compare Francs with Dollars~~
- ~~Currency?~~
- Delete testFrancMultiplication?
