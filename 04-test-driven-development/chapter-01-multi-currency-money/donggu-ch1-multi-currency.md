# Chapter 1. Multi-Currency Money

1. Make a to-do list
2. Make a test bold when we start working on item
3. Cross a test off, when we finish an item.
4. Add test to the list if we need.

## Start
```
    public void testMultiplication() {
        Dollar five = new Dollar(5);
        five.times(2);
        assertEquals(10, five.amount);
    }
```
: From the above code, the author added the below test.

| Test |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
$5 * 2 = $10
Make "amount" private
Dollar side-effects?
Money rounding?

## The TDD cycle is as follows.
1. Add a little test.
2. Run all tests and fail.
3. Make a little change.
4. Run the tests and succeed.
5. Refactor to remove duplication.

>Steve Freeman pointed out: The porblem with the test and code as it sits is not duplication. The problems is the dependency between the code and the test. You can't change one without changing the other. Our goal is to be able to write another test that "makes sens" to us, without having to change the code, something that is not possible with the current implementation.

## Elimination duplication

The second rule appears in TDD: By eliminating duplication before we go on to the next test, we maximize our change of being able to get the next test running with one and only one change.

```
    int amount;
    void times(int multiplier) {
        amount = 5*2;
    }
```
--> After revmoing duplication and adding a parameter, we can get the below code.
```
    int amount;
    Dollar(int amount) {
        this.amount = amount;
    }

    void times(int multiplier) {
        amount *= multiplier;
    }
```

Then we can remove **'$5 * 2 = $10'** test.

| Test |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
~~$5 * 2 = $10~~
Make "amount" private
Dollar side-effects?
Money rounding?

### Questions and comments:
*Q: If we can catch some mistakes and erros, isn't it easy to change and fix codes directly instead of adding tests?*\
*Q: How do we know if there are duplicated logic and code with TDD methology?*