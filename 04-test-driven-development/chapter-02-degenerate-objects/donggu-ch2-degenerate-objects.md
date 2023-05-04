# Chapter 2. Dgenerate Objects

## The general TDD cycle
1. Write a test.
2. Make it run.
3. Make it right.

## Example

| Test |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
~~$5 * 2 = $10~~
Make "amount" private
**Dollar side-effects?**
Money rounding?

```
    Dollar times(int multiplier) {
        return new Dollar(amount * multiplier);
    }
```

| Test |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
~~$5 * 2 = $10~~
Make "amount" private
~~Dollar side-effects?~~
Money rounding?

## In pratice
- Fake it - return a constant and gradually replace constants with variables until you have the real code.
- Use obvious implementation - type in the real implemenation.
