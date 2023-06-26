# Chapter 11. The root of All Evil

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
**Dollar/Franc duplication**
~~Common equals~~
~~Common times~~ 
~~Compare Francs with Dollars~~
~~Currency?~~
Delete testFrancMultiplication?

## Our goal
- Delete the subclasses (Franc and Dollar)

## We can delete Dollar subclass since there is no reference, but we can't delete Franc. Because Franc is used in our test.
```
    public void testDifferentClassEquality() {
        assertTrue(new Money(10, "CHF").equals(new Franc(10, "CHF")));
    }
```

## Update testEquality()
```
    public void testEquality() {
        assertTrue(Money.dollar(5).equals(Money.dollar(5)));
        assertFalse(Money.dollar(5).equals(Money.dollar(6)));
        assertTrue(Money.franc(5).equals(Money.franc(5)));
        assertFalse(Money.franc(5).equals(Money.franc(6)));
        assertFalse(Money.franc(5).equals(Money.dollar(5)));
    }
```
We can delete third and fourth, since they are duplicated.
```
    public void testEquality() {
        assertTrue(Money.dollar(5).equals(Money.dollar(5)));
        assertFalse(Money.dollar(5).equals(Money.dollar(6)));
        assertFalse(Money.franc(5).equals(Money.dollar(5)));
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
~~Dollar/Franc duplication~~
~~Common equals~~
~~Common times~~ 
~~Compare Francs with Dollars~~
~~Currency?~~
Delete testFrancMultiplication?

# Summary
1. Eliminated some tests that were redundant.