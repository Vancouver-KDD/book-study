## Chapter 13. Make It


```
<To-do List>
$5 + 10 CHF = $10 if rate is 2:1 
$5 + $5 = $10 <----
```

For the "$5 + $5 = $10"

```java
//Bank
Money reduce(Expression source, String to) { 
  return Money.dollar(10);
}

public void testSimpleAddition() { 
  Money five= Money.dollar(5); 
  Expression sum= five.plus(five); 
  Bank bank= new Bank();
  Money reduced= bank.reduce(sum, "USD"); 
  assertEquals(Money.dollar(10), reduced);
}
```
```
<To-do List>
$5 + 10 CHF = $10 if rate is 2:1 
$5 + $5 = $10 <----
Return Money from $5 + $5
```
First, Money.plus() needs to return a real Expression - a Sum, not just a Money.

```java
public void testPlusReturnsSum() {
  Money five= Money.dollar(5); 
  Expression result= five.plus(five); 
  Sum sum= (Sum) result; 
  assertEquals(five, sum.augend); 
  assertEquals(five, sum.addend);
}
```
 To get it to compile, all we 
need is a Sum class with two fields, augend and addend:


```java
//Sum
class Sum { 
  Money augend; 
  Money addend;
}
```
This gives us a ClassCastException, because Money.plus() is returning a Money, not a Sum:


```java
//Money
Expression plus(Money addend) { 
  return new Sum(this, addend);
}
```
Sum needs a constructor and to be a kind of Expression:

```java
//Sum
  Sum(Money augend, Money addend) { 
}

class Sum implements Expression
```
Now the system compiles again, but the test is still failing, this time because the Sum 
constructor is not setting the fields.
```java
//Sum
Sum(Money augend, Money addend) { 
  this.augend= augend;
  this.addend= addend; 
}
```
Now Bank.reduce() is being passed a Sum. If the currencies in the Sum are all the same, and 
the target currency is also the same, then the result should be a Money whose amount is the 
sum of the amounts:

```java
//testReduceSum
public void testReduceSum() {
  Expression sum= new Sum(Money.dollar(3), Money.dollar(4)); 
  Bank bank= new Bank();
  Money result= bank.reduce(sum, "USD"); 
  assertEquals(Money.dollar(7), result);
}
```

When we reduce a Sum, the result should be a Money whose amount is the sum of 
the amounts of the two Moneys.

```java
//Bank
Money reduce(Expression source, String to) { 
  Sum sum= (Sum) source;
  int amount= sum.augend.amount + sum.addend.amount; 
  return new Money(amount, to);
}
```

* The cast. This code should work with any Expression. 
* The public fields, and two levels of references at that.
```java
//Bank
Money reduce(Expression source, String to) { 
  Sum sum= (Sum) source;
  return sum.reduce(to); 
}
//Sum
public Money reduce(String to) {
  int amount= augend.amount + addend.amount; 
  return new Money(amount, to);
}
```

```
<To-do List>
$5 + 10 CHF = $10 if rate is 2:1 
$5 + $5 = $10 <----
Return Money from $5 + $5 
Bank.reduce(Money)
```
```java
//Bank
Money reduce(Expression source, String to) { 
  if (source instanceof Money)
  return (Money) source.reduce(to); 
  Sum sum= (Sum) source;
  return sum.reduce(to); 
}
//Money
public Money reduce(String to) { 
  return this;
}
```

If we add reduce(String) to the Expression interface, then we can eliminate all those ugly casts and class checks:

```java
//Bank
  Money reduce(Expression source, String to) { 
  return source.reduce(to);
}
```
```
<To-do List>
$5 + 10 CHF = $10 if rate is 2:1 
$5 + $5 = $10 <----
Return Money from $5 + $5 
// Bank.reduce(Money)
Reduce Money with conversion 
Reduce(Bank, String)
```
we'll actually exchange one currency for another

* Didn't mark a test as done because the duplication had not been eliminated 
* Worked forward instead of backward to realize the implementation
* Wrote a test to force the creation of an object we expected to need later (Sum) 
* Started implementing faster (the Sum constructor)Implemented code with casts in one place, then moved the code where it belonged once the tests were running
* Introduced polymorphism to eliminate explicit class checking


