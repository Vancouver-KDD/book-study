## Chapter 8. Makin' Objects


```
<To-do List>
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
//Make "amount" private 
//Dollar side effects? 
Money rounding? 
//equals()
hashCode()
Equal null
Equal object
//5 CHF * 2 = 10 CHF
Dollar/Franc duplication <--
//Common equals 
Common times
//Compare Francs with Dollars 
Currency?
```
The two implementations of times() are remarkably similar:


```java
//Franc
Franc times(int multiplier) {
    return new Franc(amount * multiplier); 
}
//Dollar
Dollar times(int multiplier) {
    return new Dollar(amount * multiplier); 
}
```
Let's make it return a Money type
```java
//Franc
Money times(int multiplier) {
    return new Franc(amount * multiplier); 
}
//Dollar
Money times(int multiplier) {
    return new Dollar(amount * multiplier); 
}
```
The two subclasses of Money aren't doing enough work to justify their existence, so we would like to eliminate them. But we can't do it with one big step, because that wouldn't make a very effective demonstration of TDD.

If subclasses have fewer references directly, then it is easy to remove. We can use the **Factory Method**.
```java
public void testMultiplication() {
    Dollar five = Money.dollar(5); 
    assertEquals(new Dollar(10), five.times(2)); 
    assertEquals(new Dollar(15), five.times(3));
}
```

we want references to Dollars to disappear, so we need to change the declaration in the 
test:
```java
public void testMultiplication() {
    Money five = Money.dollar(5); 
    assertEquals(new Dollar(10), five.times(2)); 
    assertEquals(new Dollar(15), five.times(3));
}
```
Our compiler politely informs us that times() is not defined for Money. We aren't ready to 
implement it just yet, so we make Money abstract (I suppose we should have done that to 
begin with?) and declare Money.times():


```java
//Money
public void testMultiplication() {
    abstract class Money
    abstract Money times(int multiplier);
}
static Money dollar(int amount) { 
    return new Dollar(amount);
}
```
We can now use our factory method everywhere in the tests:
```java
public void testMultiplication() { 
    Money five = Money.dollar(5);
    assertEquals(Money.dollar(10), five.times(2)); 
    assertEquals(Money.dollar(15), five.times(3));
}
public void testEquality() {
    assertTrue(Money.dollar(5).equals(Money.dollar(5)));
    assertFalse(Money.dollar(5).equals(Money.dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6)));
    assertFalse(new Franc(5).equals(Money.dollar(5))); 
}
```
>We are now in a slightly better position than before. No client code knows that there is a 
subclass called Dollar. By decoupling the tests from the existence of the subclasses, we have given ourselves freedom to change inheritance without affecting any model code.
```java
public void testEquality() {
    assertTrue(Money.dollar(5).equals(Money.dollar(5)));
    assertFalse(Money.dollar(5).equals(Money.dollar(6)));
    assertTrue(Money.franc(5).equals(Money.franc(5)));
    assertFalse(Money.franc(5).equals(Money.franc(6)));
    assertFalse(Money.franc(5).equals(Money.dollar(5))); 
}
public void testFrancMultiplication() { 
    Money five = Money.franc(5);
    assertEquals(Money.franc(10), five.times(2)); 
    assertEquals(Money.franc(15), five.times(3));
}
```
The implementation is just like Money.dollar():
```java
//Money
static Money franc(int amount) { 
    return new Franc(amount);
}
```


```
<To-do List>
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
//Make "amount" private 
//Dollar side effects? 
Money rounding? 
//equals()
hashCode()
Equal null
Equal object
//5 CHF * 2 = 10 CHF
//Dollar/Franc duplication
//Common equals 
Common times
//Compare Francs with Dollars 
Currency?
Delete testFrancMultiplication?
```
Next we'll get rid of the duplication of times().


<What we've done>
* Took a step toward eliminating duplication by reconciling the signatures of two variants of the same method - times()
* Moved at least a declaration of the method to the common superclass
* Decoupled test code from the existence of concrete subclasses by introducing factory methods 
* Noticed that when the subclasses disappear some tests will be redundant, but took no action
