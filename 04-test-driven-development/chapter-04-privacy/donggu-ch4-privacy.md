# Chapter 4. Privacy

## Example

| Test |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
~~$5 * 2 = $10~~
**Make "amount" private**
~~Dollar side-effects?~~
Money rounding?
~~equals()~~
hashCode()
Equal null
Equal object

1. Write a test that always pass
```
    public void testMultiplication() {
        Dollar five = new Dollar(5);
        Dollar product = five.times(2);
        assertEquals(10, product.amount);
        product = five.times(3);
        assertEquals(15, product.amount);
    }
```
-->
```
    public void testMultiplication() {
        Dollar five = new Dollar(5);
        assertEquals(new Dollar(10), five.times(2));
        assertEquals(new Dollar(15), five.times(3));
    }
```
With these changes to the test, Dollar is now the only class using its amount instance variable, so we can make it private.

| Test |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
~~$5 * 2 = $10~~
~~Make "amount" private~~
~~Dollar side-effects?~~
Money rounding?
~~equals()~~
hashCode()
Equal null
Equal object

## Note

By changing the variable type to private, it could affect equality test. **This is the risk TDD has.**