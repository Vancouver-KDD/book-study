### CH. 12

- `plus` method
    1. write a test 

```java
public void testSimpleAddition() {
Money sum= Money.dollar(5)**.plus(**Money.dollar(5)); assertEquals(Money.dollar(10), sum);
}
```

1. implement a plus for mono currency cases

```java

Money plus(Money addend) {
return new Money(amount + addend.amount, currency);
}
```

1. How are we going to represent multi-currency arithmetic?
    - option1: One possible strategy is to immediately convert all money values into a reference currency.
    - option2: The solution is to create an object that acts like a Money but represents the sum of two Moneys. I've tried several different metaphors to explain this idea. One is to treat the sum like a *wallet:*

**