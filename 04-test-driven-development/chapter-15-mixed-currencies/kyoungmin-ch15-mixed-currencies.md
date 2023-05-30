### Chapter 13. Make It
<br>

### Test List
- **$5 + 10 CHF = $10 if rate is 2:1**
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

<br>
- 드디어 $5 + 10CHF를 할 차례이다. test 식을 만들어보자.
---  

```java
public void testMixedAddition() {
    Expression fiveBucks= Money.dollar(5);
    Expression tenFrancs= Money.franc(10);
    Bank bank= new Bank();
    bank.addRate("CHF", "USD", 2);
    Money result= bank.reduce(fiveBucks.plus(tenFrancs), "USD");
    assertEquals(Money.dollar(10), result);
}
```
- Error: The method plus(Expression) is undefined for the type Expression
- Money로 수정
```java
public void testMixedAddition() {
	Money fiveBucks = Money.dollar(5);
	Money tenFrancs = Money.franc(10);
	Bank bank = new Bank();
	bank.addRate("CHF", "USD", 2);
	Money result = bank.reduce(fiveBucks.plus(tenFrancs), "USD");
	assertEquals(Money.dollar(10), result);
}
```
- 10 대신 15 USD가 나옴
- Sum.reduce()가 동작하지(reduce argument) 않는 것 같음
- 두 개의 arguments를 모두 reduce 해보자
```java
Sum
public Money reduce(Bank bank, String to) {
    int amount= augend.reduce(bank, to).amount
    + addend.reduce(bank, to).amount;
    return new Money(amount, to);
}
```
- 이제 처음에 해보려고 했던 Expression 적용을 해보려고 한다. ripple effect를 피하기 위해 edge에서부터 작업해서 test까지 해보려고 한다. 먼저 Sum을 적용해보자.
  - ripple effect란? 객체 지향 프로그래밍에서 한 클래스의 변경이 다른 클래스들에 영향을 미치는 현상을 가리킴
```java
Sum
Expression augend;
Expression addend;
Sum(Expression augend, Expression addend) {
    this.augend= augend;
    this.addend= addend;
}

Money
Expression plus(Expression addend) {
return new Sum(this, addend);
}

Money
Expression times(int multiplier) {
    return new Money(amount * multiplier, currency);
}
```
- Expression 이 plus()와 times()를 include 했으므로 plus()의 argument도 Expression으로 바꿀 수 있다. 

```java
public void testMixedAddition() {
    Money fiveBucks= Money.dollar(5);
    Expression tenFrancs= Money.franc(10);
    Bank bank= new Bank();
    bank.addRate("CHF", "USD", 2);
    Money result= bank.reduce(fiveBucks.plus(tenFrancs), "USD");
    assertEquals(Money.dollar(10), result);
}
```
- fiveBucks도 Expression으로 바꾸려고 하면 에러 나타나기 때문에 이에 맞게 수정해줘야 한다.
- Error: The method plus(Expression) is undefined for the type Expression
  
```java
Expression plus(Expression addend);
```
- 그러면 Sum class와 Money.plus()가 complaining을 한다. 
  
```java
Money
public Expression plus(Expression addend) {
    return new Sum(this, addend);
}
Sum
public Expression plus(Expression addend) {
    return null;
}
```
- Sum에 또 다른 implementation이 생겼으니 이 것을 to-do-list에 추가해야 함
<br>

---
Review
- Wrote the test we wanted, then backed off to make it achievable in one
step
- Generalized (used a more abstract declaration) from the leaves back to the root (the test case)
- Followed the compiler when we made a change (Expression fiveBucks),
which caused changes to ripple (added plus() to Expression, and so on)