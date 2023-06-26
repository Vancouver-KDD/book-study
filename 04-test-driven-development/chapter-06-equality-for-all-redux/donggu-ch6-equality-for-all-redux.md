# Chapter 6. equality-for-all-redux

| Test list |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
~~$5 x 2 = $10~~
~~Make "amount" private~~
~~Dollar side-effects?~~
Money rounding?
~~equals()~~
hashCode()
Equal null
Equal object
~~$5 CHF x 2 = $10 CHF~~
Dollar/Franc duplication
**Common equals**
Common times


1. We are going to find a common superclass for the two classes.
    - One possibility is to make one of our classes extedn the other.
    - or Find a common superclass for the two classes

![alt text](./images/donggu-image1.jpeg)


 ```
    class Money {
        protexted int amount;
    }

    class Dollar extend Money {
    }

    class Franc extend Money {
    }
```

2. Now, we need to eliminate Franc.equals().

Our original equals function
```
    public boolean equals(Object object) {
        Dollar dollar = (Dollar) object;
        return amount == dollar.amount;
    }

    --->

    public boolean equals(Object object) {
        Money money = (Money) object;
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
3. Update the test list

| Test list |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
~~$5 x 2 = $10~~
~~Make "amount" private~~
~~Dollar side-effects?~~
Money rounding?
~~equals()~~
hashCode()
Equal null
Equal object
~~$5 CHF x 2 = $10 CHF~~
Dollar/Franc duplication
~~Common equals~~
Common times
Compare Francs with Dollars

# Summary
1. Moved common code from one class (Dollar) to a superclass (Money)
2. Made a second class (Franc) a subclass also
3. Reconciled two implementations - equals() - before elimination the redundant one
