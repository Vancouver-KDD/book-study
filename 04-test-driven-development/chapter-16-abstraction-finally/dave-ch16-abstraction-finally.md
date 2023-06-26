## Chapter 16. Abstraction, Finally


```
<To-do List>
//$5 + 10 CHF = $10 if rate is 2:1 
//$5 + $5 = $10
Return Money from $5 + $5 
//Bank.reduce(Money)
//Reduce Money with conversion 
//Reduce(Bank, String)
Sum.plus <--
Expression.times
```

We need to implement Sum.plus() to finish Expression.plus, and then we need Expression.times(), and then we're finished with the whole example. Here's the test for 
Sum.plus():

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
The test, in this case, is longer than the code. The code is the same as the code in Money.
```java
//Sum
public Expression plus(Expression addend) { 
  return new Sum(this, addend);
}
```
```
<To-do List>
//$5 + 10 CHF = $10 if rate is 2:1 
//$5 + $5 = $10
Return Money from $5 + $5 
//Bank.reduce(Money)
//Reduce Money with conversion 
//Reduce(Bank, String)
//Sum.plus 
Expression.times <--
```
If we can make Sum.times() work, then declaring Expression.times() will be one simple step.
```java
//Sum
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


```java
//Sum
Expression times(int multiplier) {
  return new Sum(augend.times(multiplier),addend.times(multiplier)); 
}
// we now have to declare times() in Expression for the code to compile:

//Expression
Expression times(int multiplier);

This forces us to raise the visibility of Money.times() and Sum.times():
//Sum
public Expression times(int multiplier) {
  return new Sum(augend.times(multiplier),addend.times(multiplier)); 
}

//Money
public Expression times(int multiplier) {
  return new Money(amount * multiplier, currency); 
}

```
And it works

The only loose end to tie up is to experiment with returning a Money when we add $5 + $5. 
The test would be:
```java
public void testPlusSameCurrencyReturnsMoney() { 
  Expression sum= Money.dollar(1).plus(Money.dollar(1)); 
  assertTrue(sum instanceof Money);}

//Money
public Expression plus(Expression addend) { 
  return new Sum(this, addend);
}
```
```
<To-do List>
//$5 + 10 CHF = $10 if rate is 2:1 
//$5 + $5 = $10
//Return Money from $5 + $5 
//Bank.reduce(Money)
//Reduce Money with conversion 
//Reduce(Bank, String)
//Sum.plus 
//Expression.times
```
* Wrote a test with future readers in mind
* Suggested an experiment comparing TDD with your current programming style
* Once again had changes of declarations ripple through the system, and once again followed the compiler's advice to fix them
* Tried a brief experiment, then discarded it when it didn't work out
