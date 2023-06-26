# Ch 07. Apples and Oranges

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
    - **Compare Francs with Dollars**

`Franc`과 `Dollar`를 비교하면 어떻게 될까?
```
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
    assertFalse(new Dollar(5).equals(new Dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6)));
    assertFalse(new Franc(5).equals(new Dollar(5)));
}
```
이 테스트는 실패한다. equality 코드에서 달러와 프랑을 비교하지 않는지 확인해야 한다. 우리는 두 객체의 클래스를 비교함으로써 확인해볼 수 있다. 두 개의 `Money`는 금액과 클래스가 동일한 경우에만 동일한 걸로 간주해야 한다.
```
public boolean equals(Object object) {
    Money money = (Money) object;
    return amount == money.amount && getClass().equals(money.getClass());
}
```
이런 식으로 모델 코드에서 클래스를 사용하는 건 조금 smell이 난다. 우리는 자바 객체의 도메인이 아닌 finance 도메인에서 의미가 있는 기준을 사용하고자 한다. 하지만 현재 currency 같은 것을 갖고 있지 않고, 이것을 소개할 충분한 이유가 되지 않으니 이대로 잠시동안 두기로 하자.

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
    - ~~Compare Francs with Dollars~~
    - Currency?

우리는 이제 진짜로 공통 `times()` 코드를 제거하고 혼합 통화 산수를 할 때가 되었다. 

그 전에 이번 챕터의 리뷰를 해보자.
- Tood an objection that bothering us and turned it into a test.
- Made the test run a reasonable, but not perfect way -- getClass()
- Decided not to introduce more desing until we had a vetter motivation.