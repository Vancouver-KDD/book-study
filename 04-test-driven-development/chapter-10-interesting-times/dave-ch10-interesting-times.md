
## Chapter 10. Interesting Times
```java

//Money

abstract class Money {
    protected int amount;
    protected String currency;
    //abstract Money times(int multiplier);
    Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    String currency() {
        return currency;
    }
    static Money dollar(int amount) {
        return new Dollar(amount, "USD");
    }
    static Money franc(int amount) {
        return new Franc(amount, "CHF");
    }
    public boolean equals(Object object) {
        Money money = (Money) object;
        return amount == money.amount
                && getClass().equals(money.getClass());
    }
    Money times(int amount) {
        return null;
    }
    public String toString() {
        return amount + " " + currency;
    }
}

//Dollar
public class Dollar extends Money {

    Dollar(int amount, String currency) {
        super(amount, currency);
    }

    Money times(int multiplier) {
        return Money.dollar(amount * multiplier);
    }
}


//Franc
public class Franc extends Money{

    Franc(int amount, String currency) {
        super(amount, currency);
    }

    Money times(int multiplier) {
        return Money.franc(amount * multiplier);
    }
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
Dollar/Franc duplication 
//Common equals 
Common times <--
//Compare Francs to Dollars 
//Currency?
Delete testFrancMultiplication? 

```

When we are done with this chapter, we will have a single class to represent Money.


The two 
implementations of times() below are close, but not identical.
```java
//Franc
Money times(int multiplier) {
    return Money.franc(amount * multiplier); 
}

//Dollar
Money times(int multiplier) {
    return Money.dollar(amount * multiplier); 
}

```

Then,
```java
//Franc
Money times(int multiplier) {
    return new Franc(amount * multiplier, "CHF"); 
}
```
```java
//Dollar
Money times(int multiplier) {
    return new Dollar(amount * multiplier, "USD"); 
}
```
Does it really matter whether we have a Franc or a Money?
To figure out how to combine those two return value, just change it to Money and see the result from IDE.
```java
//Franc
Money times(int multiplier) {
    return new Money(amount * multiplier, currency); 
}
```
The compiler tells us that Money must be a concrete class:

then,

```java
// make it concrete class back
// and see the result
class Money
Money times(int amount) { 
    return null;
}
```



Aftre that, I got a new error message saying that 

```
org.opentest4j.AssertionFailedError: 
Expected :org.example.Franc@3d121db3
Actual   :org.example.Money@3b07a0d6" from the test function below
```

```java
@Test
public void testFrancMultiplication() {
    Money five = Money.franc(5);
    assertEquals(Money.franc(10), five.times(2));
    assertEquals(Money.franc(15), five.times(3));
}
```
To elaborate the error massage, toString() function is added.
* Because toString() is used only for debug output, the risk of it failing is low.
```java
//Money
public String toString() {
    return amount + " " + currency; 
}
```
Then we end up with the following message:

```
org.opentest4j.AssertionFailedError: expected: org.example.Franc@78dd667e<10 CHF> but was: org.example.Money@10db82ae<10 CHF>
Expected :10 CHF
Actual   :10 CHF
```
The data is identical but the class is different.

The conservative course is to 
back out the change that caused the red bar so we're back to green.


When it changes back to the original as below, it gets us back to green.
```
//Franc
//    Money times(int multiplier) {
//        return new Money(amount * multiplier, currency);
//    }
Money times(int multiplier) {
    return new Franc(amount * multiplier, currency);
}

```

Let's add an error test for it.
```java
public void testDifferentClassEquality() {
    assertTrue(new Money(10, "CHF").equals(new Franc(10, "CHF"))); 
}
```

It fails, as expected. The equals() code should compare currencies, not classes:

```java
public boolean equals(Object object) { 
    Money money = (Money) object;
    return amount == money.amount
    && currency().equals(money.currency()); 
}

```

Then, again, change it back to the constructor 'return'-type with Money. 
Now we can return a Money from Dollar/Franc.times() and still pass the tests.
```java
//Franc
Money times(int multiplier) {
    return new Money(amount * multiplier, currency);
}

//Dollar
Money times(int multiplier) {
    return new Money(amount * multiplier, currency); 
}
```
Yes! Now the two implementations are identical, so we can push them up.

```java

class Money {
    protected int amount;
    protected String currency;
    //abstract Money times(int multiplier);
    Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    String currency() {
        return currency;
    }
    static Money dollar(int amount) {
        return new Dollar(amount, "USD");
    }
    static Money franc(int amount) {
        return new Franc(amount, "CHF");
    }
    public boolean equals(Object object) {
        Money money = (Money) object;
        return amount == money.amount
                && currency().equals(money.currency());
    }
    Money times(int multiplier) {
        return new Money(amount * multiplier, currency);
    }
    public String toString() {
        return amount + " " + currency;
    }
}

public class Dollar extends Money {
    Dollar(int amount, String currency) {
        super(amount, currency);
    }
}

public class Franc extends Money{
    Franc(int amount, String currency) {
        super(amount, currency);
    }
}
```

```java
package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Tests {
    @Test
    public void testMultiplication() {
        Money five = Money.dollar(5);
        assertEquals(Money.dollar(10), five.times(2));
        assertEquals(Money.dollar(15), five.times(3));
    }
    @Test
    public void testEquality() {
        assertTrue(Money.dollar(5).equals(Money.dollar(5)));
        assertFalse(Money.dollar(5).equals(Money.dollar(6)));
        assertTrue(Money.franc(5).equals(Money.franc(5)));
        assertFalse(Money.franc(5).equals(Money.franc(6)));
        assertFalse(Money.franc(5).equals(Money.dollar(5)));
    }

    @Test
    public void testCurrency() {
        assertEquals("USD", Money.dollar(1).currency());
        assertEquals("CHF", Money.franc(1).currency());
    }

    @Test
    public void testFrancMultiplication() {
        Money five = Money.franc(5);
        assertEquals(Money.franc(10), five.times(2));
        assertEquals(Money.franc(15), five.times(3));
    }
    @Test
    public void testDifferentClassEquality() {
        assertTrue(new Money(10, "CHF").equals(new Franc(10, "CHF")));
    }
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
Dollar/Franc duplication 
//Common equals 
//Common times
//Compare Francs to Dollars 
//Currency?
Delete testFrancMultiplication?
```
we are ready to eliminate the stupid subclasses

* Reconciled two methods - times() - by first inlining the methods they called and then 
replacing constants with variables
* Wrote a toString() without a test just to help us debug
* Tried a change (returning Money instead of Franc) and let the tests tell us whether it worked 
* Backed out an experiment and wrote another test. Making the test work made the experiment
work

