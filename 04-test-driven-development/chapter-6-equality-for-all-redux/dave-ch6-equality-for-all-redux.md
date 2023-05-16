Chapter 6. Equality for All, Redux


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
//5 CHF * 2 = 10 CHF <--
Dollar/Franc duplication 
Common equals 
Common times
```

A prerequisite seems to be having an object like Dollar, but to represent francs. If we can get the object Franc to work the way that the object Dollar works now, we'll be closer to being able to write and run the mixed addition test.

Let's utilize the Dollar test.
```java
public void testFrancMultiplication() { 
    Franc five= new Franc(5);
    assertEquals(new Franc(10), five.times(2)); 
    assertEquals(new Franc(15), five.times(3));
}
```
we sinned mightily in copying and pasting tons of code in order to do it quickly. Now it is time to clean up.

One possibility is to make one of our classes extend the other. I tried it, and it hardly saves any code at all. Instead, we are going to find a common superclass for the two classes.
```
 Dollar          Money
   |               |
   |            |     |
 Franc       Dollar  Franc      
```   
What if we had a Money class to capture the common equals code? We can start small:




```java
//Money
class Money
```
All of the tests still run - not that we could possibly have broken anything, but it's a good time to run the tests anyway. If Dollar extends Money, that can't possibly break anything.

```java
//Dollar
class Dollar extends Money { 
    private int amount;
}
```
Tests still all run. Now we can move the amount instance variable up to Money:
```java
//Money
class Money { 
    protected int amount;
}

//Dollar
class Dollar extends Money { 
}
```
The visibility has to change from **private** to **protected** so that the subclass can still see it.

#### Getting the equals() code ready to move up!

Change the declaration of the temporary variable:

```java
//Dollar
public boolean equals(Object object) { 
    Money dollar= (Dollar) object; 
    return amount == dollar.amount;
}
```
We still can check the code running.
Change the cast:
```java
//Dollar
public boolean equals(Object object) { 
    Money dollar= (Money) object; 
    return amount == dollar.amount;
}
```
For understanding, change the name of the temporary variable:
```java
//Dollar
public boolean equals(Object object) { 
    Money money= (Money) object; 
    return amount == money. amount;
}
```

Before dealing with comparison of Francs as Equlity, write the Franc equality test.

#### What if the proper tests are skipped?
> Refactorings not associated with tests will arise.

> A refactoring mistake might happen.

> While refactoring, something will be broken.

> Then, you will end up feeling uncomfortable with refactoring.

**So, to keep your code healthy, retroactively test before refactoring.**


Utilize the test code for Dollar. (More duplication?? more sin?)
```java
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
    assertFalse(new Dollar(5).equals(new Dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6))); 
}
```

Now then Franc can extend Money.

```java
class Franc extends Money 
{
    private int amount;
}
```
Also the field amount can be removed.
```java
class Franc extends Money 
{
}
```
Change the the declaration of the temporary variable like what we did for dollar:

```java
//Franc
public boolean equals(Object object) { 
    Money franc= (Franc) object; 
    return amount == franc.amount;
}
```
Change the cast:
```java
//Franc
public boolean equals(Object object) { 
    Money franc= (Money) object; 
    return amount == franc.amount;
}
```
Change the name of the temporary variable to match the superclass:
```java
//Franc
public boolean equals(Object object) { 
    Money money= (Money) object; 
    return amount == money.amount;
}
```

```
class Dollar {
    private int amount;
    Dollar(int amount) {
        this.amount = amount;
    }
    Dollar times(int multiplier) {
        return new Dollar(amount * multiplier)
    }
    public boolean equals(Object object) {
        Money money = (Money) object;
        return amount == money.amount;
    }
}
class Franc {
    private int amount;
    Franc(int amount) {
        this.amount = amount;
    }
    Franc times(int multiplier) {
        return new Franc(amount * multiplier)
    }
    public boolean equals(Object object) {
        Money money = (Money) object;
        return amount == money.amount;
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
//5 CHF * 2 = 10 CHF <--
Dollar/Franc duplication 
//Common equals 
Common times
Compare Francs with Dollars
```

Check the test running.
Now there is no difference between Franc.equals()  and Money.equals(), so we delete the redundant implementation in Franc.

<What we've done>
* Stepwise moved common code from one class (Dollar) to a superclass (Money)
* Made a second class (Franc) a subclass also
* Reconciled two implementations - equals() - before eliminating the redundant one
