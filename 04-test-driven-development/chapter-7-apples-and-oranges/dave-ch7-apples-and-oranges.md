## Chapter 7. Apples and Oranges


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
Compare Francs with Dollars <--
```

What happens when we compare Francs with Dollars? 
```java
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
    assertFalse(new Dollar(5).equals(new Dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6)));
    assertFalse(new Franc(5).equals(new Dollar(5))); //<--
}

```
It fails.
So, the equality code needs to check that Dollars and Francs should not be compared.

```java
//money
public boolean equals(Object object) { 
    Money money = (Money) object;
    return amount == money.amount
    && getClass().equals(money.getClass()); 
}
```
Using classes this way in model code is a bit smelly. But we don't currently have anything like a currency, so this will have to do for the moment.


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
//Compare Francs with Dollars
Currency?
```
Now we really need to get rid of the common times() code, so we can get to mixed currency 
arithmetic.


<What we've done>
* Took an objection that was bothering us and turned it into a test
* Made the test run a reasonable, but not perfect way - getClass()
* Decided not to introduce more design until we had a better motivation



