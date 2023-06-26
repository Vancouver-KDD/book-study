## Chapter 11. The Root of All Evil

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
//Common times
//Compare Francs to Dollars 
//Currency?
Delete testFrancMultiplication? 

```


```java
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
```

The two subclasses, Dollar and Franc, have only their constructors. But because a constructor 
is not reason enough to have a subclass, we want to delete the subclasses.

We  can  replace  references  to  the  subclasses with  references to  the  superclass without 
changing the meaning of the code.
```java
//static Money dollar(int amount) {
        return new Dollar(amount, "USD");
    }
//static Money franc(int amount) {
        return new Franc(amount, "CHF");
    }
static Money dollar(int amount) {
    return new Money(amount, "USD");
}
static Money franc(int amount) {
    return new Money(amount, "CHF");
}
```

Then we can delete Dollar classes but not delete Franc because it's related to the testDifferentClassEquality().

And also for the testEquality()
```java
public void testEquality() {
    assertTrue(Money.dollar(5).equals(Money.dollar(5)));
    assertFalse(Money.dollar(5).equals(Money.dollar(6)));
    assertFalse(Money.franc(5).equals(Money.dollar(5))); 
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
//Common times
//Compare Francs to Dollars 
//Currency?
Delete testFrancMultiplication? 
```


 There are separate tests for dollar and franc multiplication. Looking at the code, we 
can see that there is no difference in the logic at the moment based on the currency (there 
was a difference when there were two classes). We can delete testFrancMultiplication() 
without losing any confidence in the behavior of the system.

Single class in place, we are ready to tackle addition. First, to review, we
* Finished gutting subclasses and deleted them
* Eliminated tests that made sense with the old code structure but were redundant with the 
new code structure

