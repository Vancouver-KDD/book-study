# Chapter 13. Make it

### Addition Todo List

- $5 + 10 CHF = $10 if rate is 2:1
- **$5 + $5 = $10**
- Return Money from $5 + $5

모든 duplication을 제거하기 전까지 우리는 테스트를 통과했다고 여길 수 없음 -> 지금 우리는 코드 중복은 없지만 데이터 중복이 있음

Money.plus() 는 `Money` 가 아니라 real Expression 인 `Sum`을 리턴해야함 -> 두 Money의 합은 Sum 이여야함

```
public void testPlusReturnsSum() {
    Money five = Money.dollar(5);
    Expression result = five.plus(5);
    Sum sum = (Sum) result;
    assertEquals(five, sum.augend);
    assertEquals(five, sum.addend);
}
```

이 테스트코드를 compile 하기 위해서는 Sum class 에 두개의 필드가 필요함 : augend, addend

```
class Sum {
    Money augend;
    Money addend;
}
```

`Money.plus()` 가 Sum 이 아니라 Money를 리턴하고 있기 때문에 `ClassCastException`

```
// Money
Expression plus(Money addend) {
    return new Sum(this, addend);
}
```

Sum 은 contructor가 필요함

```
Sum(Money augend, Money addend) {
}
```

Sum 은 Expression 임

```
// Sum
class Sum implements Expression
```

테스트는 아직도 fail. 왜냐면 Sum contructor is not setting the fields 6. Setting the contructor fields

```
// Sum
Sum (Money augend, Money addend) {
    this.augend = augend;
    this.addend = addend;
}
```

`Bank.reduce()` 에 `Sum` 이 불려지고 있음.

```
public void testReduceSum() {
    Expression sum = new Sum(Money.dollar(3), Money.dollar(4));
    Bank bank = new Bank();
    Money result = bank.reduce(sum, "USD");
    assertEquals(Money.dollar(7), result);
}
```

When we reduce a Sum, 두 Moneys 의 합을 나타내는 Sum result는 Money를 리턴해야함

```
// Bank
Money reduce(Expression source, String to) {
    Sum sum = (Sum) source;
    int amount = sum.augend.amount + sum.addend.amount;
    return new Money(amount, to);
}
```
