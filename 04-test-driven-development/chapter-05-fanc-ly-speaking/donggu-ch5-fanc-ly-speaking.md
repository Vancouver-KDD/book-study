# Chapter 5. franc-ly speaking

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
**$5 CHF x 2 = $10 CHF**


1. How can we create a test for this test?
    - A prerequister seems to be having an object like **Dollar**
    - And we can copy and paste the same test that we use for Dollar
    ```
    public void testMultiplication() {
        Dollar five = new Dollar(5);
        assertEquals(new Dollar(10), five.times(2));
        assertEquals(new Dollar(15), five.times(3));
    }
    ```
    --> The death of abstraction and the killer of clean design

2. The TDD cycle is as follows.
    a. Add a little test.\
    b. Run all tests and fail.\
    c. Make a little change.\
    d. Run the tests and succeed.\
    e. Refactor to remove duplication.\
: The different phases have different purposes. Tje forst tjree [jases meed tp gp bu qiocl;u. sp we get to a known state with the new functionality. The first four steps of the cylce won't work without the fifth.


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
        Dollar dollar = (Dollar) object;
        return amount == dollar.amount;
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
        Franc franc = (Franc) object;
        return amount == franc.amount;
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

# Summary
1. There are many duplication, and we need to eliminate them before writing the next test.
