# Chapter 10. Interesting Times

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
**Common times**  
~~Compare Francs with Dollars~~
~~Currency?~~
Delete testFrancMultiplication?


### How do we handle the times function?
## Current:
```
    # Franc
    Money times(int multiplier) {
        return Money.Franc(amount * multiplier);
    }

    # Dollar
    Money times(int multiplier) {
        return Money.dollar(amount * multiplier);
    }  
```

## Added currency string:
```
    # Franc
    Money times(int multiplier) {
        return Money.Franc(amount * multiplier, currency);
    }

    # Dollar
    Money times(int multiplier) {
        return Money.dollar(amount * multiplier, currency);
    }
```
It causes the error and we add a toString() function to debug the issue.
And we wrote this function without test code, and since this function is only for debugging, so it is acceptable to implement without having tests.
```
    public String toString() {
        return amount + " " + currency;
    }
```

## Updated the return type:
```
    # Franc
    Money times(int multiplier) {
        return new Money(amount * multiplier, currency);
    }

    # Dollar
    Money times(int multiplier) {
        return new Money(amount * multiplier, currency);
    }
```

### We need to check the currencies are the same, not that the classes are the same.
This fails becuase the equals() doesn't compare currencies correctly.
```
    public void testDifferentClassEquality() {
        assertTrue(new Money(10, "CHF").equals(new Franc(10, "CHF")));
    }
```
And this updated equal function passes our unit tests.
```
    public boolean equals(Object object) {
        Money money = (Money) object;
        return amount == money.amount
        && currency().equals(money.currency());
    }
```

## Update the test list

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
~~Common times~~ 
~~Compare Francs with Dollars~~
~~Currency?~~
Delete testFrancMultiplication?

# Summary
1. Reconciled two methods - times()
2. used toString() to debug errors
