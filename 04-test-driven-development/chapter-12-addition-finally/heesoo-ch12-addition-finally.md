# Chaper 12. Addition, Finally

### Todo List

- **$5 + 10 CHF = $10 if rate is 2:1**
- ~~$5 \* 2 = $10~~
- ~~Make "amount" private~~
- ~~Dollar side-effects?~~
- Money rounding?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
- ~~5CHF \* 2 = 10CHF~~
- ~~Dollar/Franc duplication~~
- ~~Common equals~~
- ~~Common times~~
- ~~Compare Francs with Dollars~~
- ~~Currency?~~
- ~~Delete testFrancMultiplication?~~

### New Todo List for Addition

- $5 + 10 CHF = $10 if rate is 2:1
- **$5 + $5 = $10**

```
public void testSimpleAddition() {
    Money sum = Money.dollar(5).plus(Money.dollar(5));
    assertEquals(Money.dollar(10), sum);
}
```

```
Money plus(Money addend) {
    return new Money(amount + addend.amount, currency);
}
```

use `bank` and `expression` metaphors

- has `Bank` object
- sum of two `Money`s should be an `Expression`

```
public void testSimpleAddition() {
    Money five = Money.dollar(5)
    Expression sum = five.plus(five)
    Bank bank = new Bank();
    Money reduced = bank.reduce(sum, "USD");
    assertEquals(Money.dollare(10), reduced);
}
```

이 테스트 코드를 어떻게 컴파일하게 할까?

1. interface `Expression` 이 필요함

   ```
   interface Expression
   ```

2. `Money.plus()` 는 `Expression`을 리턴함
   ```
   Expression plus(Money addend) {
       return new Money(amount + addend.amount, currency)
   }
   ```
3. Money has to implement Expression
   ```
   // Money
   class Money implements Expression
   ```
4. Bank class 필요함
   ```
   class Bank
   ```
5. `reduce()` 를 가짐
   ```
   // Bank
   Money reduce(Expression source, String to) {
       return null;
   }
   ```
6. 이렇게 하면 컴파일되지만 테스트는 fail 함 -> fake implementation으로 테스트 성공시킴
   ```
   // Bank
   Money reduce(Expression source, String to) {
      return Money.dollar(10);
   }
   ```
