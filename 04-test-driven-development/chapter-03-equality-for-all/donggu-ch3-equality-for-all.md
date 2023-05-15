# Chapter 3. Equality for All

## Start
- using object as values.
- Advantage: Don't have to worry about aliasing problems.
- implication: all operations must return a new object

## Example

| Test |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
~~$5 * 2 = $10~~
Make "amount" private
~~Dollar side-effects?~~
Money rounding?
**equals()**
hashCode()

1. Write a test that always pass
```
    public boolean equals(Object object) {
        return true;
    }
```
-->
```
    public void testEquality() {
        assertTrue(new Dollar(5).equals(new Dollar(5)));
        assertFalse(new Dollar(5).equals(new Dollar(6)));
    }
```
-->
```
    public boolean equals(Object object) {
        Dollar dollar = (Dollar) object;
        return amount == dollar.amount;
    }
```

| Test |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
~~$5 * 2 = $10~~
Make "amount" private
~~Dollar side-effects?~~
Money rounding?
~~equals()~~
hashCode()
