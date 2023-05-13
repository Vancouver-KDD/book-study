# Chapter 7. apples-and-oranges

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
**Compare Francs with Dollars**

1. Use getClass to compare Francs with Dollars
```
    public void testEquality() {
        assertTrue(new Dollar(5).equals(new Dollar(5)));
        assertFalse(new Dollar(5).equals(new Dollar(6)));
        assertTrue(new Franc(5).equals(new Franc(5)));
        assertFalse(new Franc(5).equals(new Franc(6)));
        assertFalse(new Franc(5).equals(new Dollar(6)));
    }
```

It fails, until we fix the equal function

```
    public boolean equals(Object object) {
        Dollar dollar = (Dollar) object;
        return amount == dollar.amount
            && getClass().equals(money.getClass());
    }
```

2. Update the test list

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
~~Compare Francs with Dollars~~
Currency?


# Summary
1. Moved common code from one class (Dollar) to a superclass (Money)
2. Made a second class (Franc) a subclass also
3. Reconciled two implementations - equals() - before elimination the redundant one
