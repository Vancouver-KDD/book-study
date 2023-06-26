### Chapter 13. Make It
<br>

### Test List
- ~~$5 + 10 CHF = $10 if rate is 2:1~~
- ~~$5 + $5 = $10~~
- Return Money from $5 + $5
- ~~$5 * 2 = $10~~
- ~~Make “amount” private~~
- ~~Dollar side effects?~~
- Money rounding?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
- ~~5 CHF * 2 = 10 CHF~~
- ~~Dollar/Franc duplication~~
- ~~Common equals~~
- ~~Common times~~
- ~~Compare Francs with Dollars~~
- ~~Currency?~~
- ~~Delete testFrancMultiplication?~~
- ~~Bank.reduce(Money)~~
- ~~Reduce Money with conversion~~
- ~~Reduce(Bank, String)~~
- **Sum.plus**
- Expression.times

<br>

---
- Expression.plus를 finish하기 위해 Sum.plus()를 구현시켜야 한다.먼저 test식을 만들어보자.
  
```java
public void testSumPlusMoney() {
    Expression fiveBucks= Money.dollar(5);
    Expression tenFrancs= Money.franc(10);
    Bank bank= new Bank();
    bank.addRate("CHF", "USD", 2);
    Expression sum= new Sum(fiveBucks, tenFrancs).plus(fiveBucks);
    Money result= bank.reduce(sum, "USD");
    assertEquals(Money.dollar(15), result);
}
```
- 그냥 5 + 10 을 하지 않고, fiveBucks를 더 더한 모습으로 Sum을 생성하는 것이 더 직관적으로 전달되기 때문이라고 한다.
- 그리고 Sum.plus()가 null이 아닌 Sum을 return하도록 수정해준다. 
- ~~Sum.plus~~
```java
Sum
public Expression plus(Expression addend) {
    return new Sum(this, addend);
}
```
- Explression.times를 리스트에서 지우자.
- test식을 작성하면,

```java
public void testSumTimes() {
    Expression fiveBucks= Money.dollar(5);
    Expression tenFrancs= Money.franc(10);
    Bank bank= new Bank();
    bank.addRate("CHF", "USD", 2);
    Expression sum= new Sum(fiveBucks, tenFrancs).times(2);
    Money result= bank.reduce(sum, "USD");
    assertEquals(Money.dollar(20), result);
}
```
  - Sum.times()에 빨간 줄이 쳐지게 된다. 아래와 같이 add해주고,

```java
Sum
Expression times(int multiplier) {
    return new Sum(augend.times(multiplier),addend.times(multiplier));
}
```
- augend와 addend의 times에 빨간 줄
- 이 둘을 Expressions에 abstract했기 때문. compile하기 위해 Expression에 times()도 declare 해줘야 한다.
- 이렇게 할 경우 Money.times()와 Sum.times()가 보이도록(public) 해줘야 한다.

```java
Sum
public Expression times(int multiplier) {
    return new Sum(augend.times(multiplier),addend.times(multiplier));
}

Money
public Expression times(int multiplier) {
    return new Money(amount * multiplier, currency);
}
```
- 마지막으로는 $5 + $5 를 더할 때 Money를 반환하는지 확인하는 것

```java
public void testPlusSameCurrencyReturnsMoney() {
    Expression sum= Money.dollar(1).plus(Money.dollar(1));
    assertTrue(sum instanceof Money);
}
```
- Money가 argument로 전달될 경우에만 해당 통화를 확인하는 깔끔한 방법은 없으므로, 위 test는 삭제한다.

<br>

Review
- Wrote a test with future readers in mind
- Suggested an experiment comparing TDD with your current programming style
- Once again had changes of declarations ripple through the system, and once again followed the compiler’s advice to fix them
- Tried a brief experiment, then discarded it when it didn’t work out


