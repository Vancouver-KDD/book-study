## Chapter 14. Change

```
<To-do List>
$5 + 10 CHF = $10 if rate is 2:1 
$5 + $5 = $10
Return Money from $5 + $5 
//Bank.reduce(Money)
Reduce Money with conversion <--
Reduce(Bank, String)
```
we have two francs and we want a dollar. 

`So, let's get started from a method for a test!!`
```java
public void testReduceMoneyDifferentCurrency() { 
  Bank bank= new Bank();
  bank.addRate("CHF", "USD", 2);
  Money result= bank.reduce(Money.franc(2), "USD"); 
  assertEquals(Money.dollar(1), result);
}
```
When I convert francs to dollars, I divide by two.
```java
//Money
public void testReduceMoneyDifferentCurrency() { 
  int rate = (currency.equals("CHF") && to.equals("USD")) 
  ?2
  : 1;
  return new Money(amount / rate, to);
}
```
Now, suddenly, Money knows about exchange rates.
he Bank should be the only place we care about exchange rates. We'll have to pass the Bank as a parameter to 
Expression.reduce(). 
```java
//Bank
Money reduce(Expression source, String to) { 
  return source.reduce(this, to);
}
```
Then the implementors:
```java
//Sum
public Money reduce(Bank bank, String to) {
  int amount= augend.amount + addend.amount; 
  return new Money(amount, to);
}
//Money
public Money reduce(Bank bank, String to) {
  int rate = (currency.equals("CHF") && to.equals("USD")) 
  ?2
  : 1;
  return new Money(amount / rate, to); 
}
```
The methods have to be public because methods in interfaces have to be public (for some excellent reason, I'm sure).
Now we can calculate the rate in the Bank:
```java
//Bank
int rate(String from, String to) {
  return (from.equals("CHF") && to.equals("USD")) 
  ?2
  : 1; 
}
//Money
public Money reduce(Bank bank, String to) { 
  int rate = bank.rate(currency, to);
  return new Money(amount / rate, to); 
}
```
we have to create a real object for the key:
```java
//Pair
private class Pair { 
  private String from; 
  private String to;
  Pair(String from, String to) { 
  this.from= from;
  this.to= to; 
  }
}
```
Because we are using Pairs as keys, we have to implement equals() and hashCode(). 

```java
//Pair
public boolean equals(Object object) { 
  Pair pair= (Pair) object;
  return from.equals(pair.from) && to.equals(pair.to); 
}
public int hashCode() { 
  return 0;
}
```


```java
//Bank
  //We need somewhere to store the rates:
  private Hashtable rates= new Hashtable();
  
  //We need to set the rate when told:
  void addRate(String from, String to, int rate) { 
    rates.put(new Pair(from, to), new Integer(rate));
  }
  
  //we can look up the rate when asked:
  int rate(String from, String to) {
    Integer rate= (Integer) rates.get(new Pair(from, to)); 
    return rate.intValue();
  }
```
We got a red bar. What happened? 
if we ask for the rate from USD to USD, we expect the value to be 1. 

```java
//Bank
  public void testIdentityRate() {
    assertEquals(1, new Bank().rate("USD", "USD")); 
  }
```
we have three errors
```java
//Bank
int rate(String from, String to) { 
  if (from.equals(to)) return 1;
  Integer rate= (Integer) rates.get(new Pair(from, to)); 
  return rate.intValue();
}
```
Green bar!

```
<To-do List>
$5 + 10 CHF = $10 if rate is 2:1 
//$5 + $5 = $10
Return Money from $5 + $5 
//Bank.reduce(Money)
//Reduce Money with conversion <--
//Reduce(Bank, String)
```
Next we'll implement our last big test, $5 + 10 CHF

* Added a parameter, in seconds, that we expected we would needFactored out the data duplication between code and tests
* Wrote a test (testArrayEquals) to check an assumption about the operation of Java 
* Introduced a private helper class without distinct tests of its own
* Made a mistake in a refactoring and chose to forge ahead, writing another test to isolate the 
problem

```java

package org.example;

class Money implements Expression{
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
        return new Money(amount, "USD");
    }
    static Money franc(int amount) {
        return new Money(amount, "CHF");
    }
    public boolean equals(Object object) {
        Money money = (Money) object;
        return amount == money.amount
                && currency().equals(money.currency());
    }

    Expression plus(Money addend) {
        return new Sum(this, addend);
    }
    Money times(int multiplier) {
        return new Money(amount * multiplier, currency);
    }
    public Money reduce(Bank bank, String to) {
        int rate = bank.rate(currency, to);
        return new Money(amount / rate, to);
    }
    public String toString() {
        return amount + " " + currency;
    }
}

package org.example;

public interface Expression {
    Money reduce(Bank bank, String to);
}


package org.example;

import java.util.Hashtable;

public class Bank {
    private Hashtable rates= new Hashtable();
    Money reduce(Expression source, String to) {
        return source.reduce(this, to);
    }
    int rate(String from, String to) {
        if (from.equals(to)) return 1;
        Integer rate= (Integer) rates.get(new Pair(from, to));
        return rate.intValue();
    }
    void addRate(String from, String to, int rate) {
        rates.put(new Pair(from, to), new Integer(rate));
    }
}

package org.example;

public class Sum implements Expression {
    Money augend;
    Money addend;
    Sum(Money augend, Money addend) {
        this.augend= augend;
        this.addend= addend;
    }
    public Money reduce(Bank bank, String to) {
        int amount= augend.amount + addend.amount;
        return new Money(amount, to);
    }

}

package org.example;

public class Pair {
    private String from;
    private String to;
    Pair(String from, String to) {
        this.from= from;
        this.to= to;
    }
    public boolean equals(Object object) {
        Pair pair= (Pair) object;
        return from.equals(pair.from) && to.equals(pair.to);
    }
    public int hashCode() {
        return 0;
    }
}


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
        //assertTrue(Money.franc(5).equals(Money.franc(5)));
        //assertFalse(Money.franc(5).equals(Money.franc(6)));
        assertFalse(Money.franc(5).equals(Money.dollar(5)));
    }

    @Test
    public void testCurrency() {
        assertEquals("USD", Money.dollar(1).currency());
        assertEquals("CHF", Money.franc(1).currency());
    }

//    @Test
//    public void testFrancMultiplication() {
//        Money five = Money.franc(5);
//        assertEquals(Money.franc(10), five.times(2));
//        assertEquals(Money.franc(15), five.times(3));
//    }
    @Test
    public void testDifferentClassEquality() {
        assertTrue(new Money(10, "CHF").equals(new Franc(10, "CHF")));
    }

    @Test
    public void testSimpleAddition() {
        Money five= Money.dollar(5);
        Expression sum= five.plus(five);
        Bank bank= new Bank();
        Money reduced= bank.reduce(sum, "USD");
        assertEquals(Money.dollar(10), reduced);
    }

    @Test
    public void testPlusReturnsSum() {
        Money five= Money.dollar(5);
        Expression result= five.plus(five);
        Sum sum= (Sum) result;
        assertEquals(five, sum.augend);
        assertEquals(five, sum.addend);
    }

    public void testReduceMoney() {
        Bank bank= new Bank();
        Money result= bank.reduce(Money.dollar(1), "USD");
        assertEquals(Money.dollar(1), result);
    }

    @Test
    public void testReduceSum() {
        //Expression sum= new Sum(Money.dollar(3), Money.dollar(4));
        Bank bank= new Bank();
        Money result= bank.reduce(Money.dollar(1), "USD");
        assertEquals(Money.dollar(1), result);
    }
    @Test
    public void testReduceMoneyDifferentCurrency() {
        Bank bank= new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result= bank.reduce(Money.franc(2), "USD");
        assertEquals(Money.dollar(1), result);
    }
    public void testArrayEquals() {
        assertEquals(new Object[] {"abc"}, new Object[] {"abc"});
    }
    public void testIdentityRate() {
        assertEquals(1, new Bank().rate("USD", "USD"));
    }

}

package org.example;

public class Dollar extends Money {
    Dollar(int amount, String currency) {
        super(amount, currency);
    }
}

package org.example;

public class Franc extends Money{
    Franc(int amount, String currency) {
        super(amount, currency);
    }
}
```
