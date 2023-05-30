## Chapter 9. Times We're Livin' In

```java
abstract class Money {
    protected int amount;
    abstract Money times(int multiplier);

    static Money dollar(int amount) {
        return new Dollar(amount);
    }
    static Money franc(int amount) {
        return new Franc(amount);
    }
    public boolean equals(Object object) {
        Money money = (Money) object;
        return amount == money.amount
                && getClass().equals(money.getClass());
    }

}

public class Dollar extends Money {
    Dollar(int amount) {
        this.amount= amount;
    }

    Money times(int multiplier) {
        return new Dollar(amount * multiplier);
    }
}


public class Franc extends Money{

    Franc(int amount) {
        this.amount= amount;
    }

    Money times(int multiplier) {
        return new Franc(amount * multiplier);
    }
}

abstract class Money {
    protected int amount;
    abstract Money times(int multiplier);

    static Money dollar(int amount) {
        return new Dollar(amount);
    }
    static Money franc(int amount) {
        return new Franc(amount);
    }
    public boolean equals(Object object) {
        Money money = (Money) object;
        return amount == money.amount
                && getClass().equals(money.getClass());
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
Common times
//Compare Francs to Dollars 
Currency? <---
Delete testFrancMultiplication?

```

What is there on our to-do list that might help us to eliminate those pesky useless subclasses? 
What would happen if we introduced the notion of currency?

t for the moment, strings will do:



```java
public void testCurrency() {
assertEquals("USD", Money.dollar(1).currency()); 
assertEquals("CHF", Money.franc(1).currency());
}

//we declare currency() in Money
//Money
abstract String currency();

//we implement it in both subclasses
//Franc
String currency() { 
return "CHF";
}

//Dollar
String currency() { 
return "USD";
}

```

Then,
```java
//Franc
private String currency; 
Franc(int amount) {
    this.amount = amount; 
    currency = "CHF";
    }
String currency() { 
    return currency;
}
```
```java
//Dollar
private String currency; 
Dollar(int amount) {
    this.amount = amount; 
    currency = "USD";
}
String currency() { 
    return currency;
}
```
```java
//Money
protected String currency; 
String currency() {
    return currency;
}

```

If we move the constant strings "USD" and "CHF" to the `static factory methods`, then the `two 
constructors` will be identical and we can create a common implementation.

First we'll add a parameter to the constructor:

```java
//Franc
Franc(int amount, String currency) { 
    this.amount = amount;
    this.currency = "CHF"; 
}

```
To break into the two caller of the constructor
```java
//Money
static Money franc(int amount) { 
    return new Franc(amount, null);
}
```
Franc.times() should the factory method instead of calling the constructor.

```java
//Franc
Money times(int multiplier) {
    return Money.franc(amount * multiplier); 
}
```
Now the factory method can pass "CHF":
```java
//Money
static Money franc(int amount) { 
    return new Franc(amount, "CHF");
}
```
And finally we can assign the parameter to the instance variable:
```java
//Franc
Franc(int amount, String currency) { 
    this.amount = amount;
    this.currency = currency; 
}
```

The, let's apply it to USD

```java
//Money
static Money dollar(int amount) { 
    return new Dollar(amount, "USD");
}
```

```java
//Dollar
Dollar(int amount, String currency) { 
    this.amount = amount;
    this.currency = currency; 
}
Money times(int multiplier) {
    return Money.dollar(amount * multiplier); 
}
```

>The two constructors of the Money and Franc are now identical, so we can push up the implementation to the parent class 'Money' :

```java
//Money
Money(int amount, String currency) { 
    this.amount = amount;
    this.currency = currency; 
}
```

```java
//Franc
Franc(int amount, String currency) { 
    super(amount, currency);
}

```
```java
//Dollar
Dollar(int amount, String currency) { 
    super(amount, currency);
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
Common times
//Compare Francs to Dollars 
//Currency?
Delete testFrancMultiplication?

```
We're almost ready to push up the implementation of times() and eliminate the subclasses.
<What we've done>


* Were a little stuck on big design ideas, so we worked on something small we noticed earlier 
* Reconciled the two constructors by moving the variation to the caller (the factory method) 
* Interrupted a refactoring for a little twist, using the factory method in times()
* Repeated an analogous refactoring (doing to Dollar what we just did to Franc) in one big step 
* Pushed up the identical constructors

