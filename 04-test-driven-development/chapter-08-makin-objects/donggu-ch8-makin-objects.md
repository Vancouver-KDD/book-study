# Chapter 8. makin' Objects

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
Common times
~~Compare Francs with Dollars~~
Currency?


Franc
```
    Franc times(int multiplier) {
        return new Franc(amount * multiplier)
    }
    
    ----->

    Money times(int multiplier) {
        return new Franc(amount * multiplier)
    }

```

Dollar
```
    Dollar times(int multiplier) {
        return new Dollar(amount * multiplier)
    }

    ----->

    Money times(int multiplier) {
        return new Dollar(amount * multiplier)
    }

```

1. Use a factory method in Money that returns a Dollar
```
    public void testMultiplication() {
        Dollar five = Money.dollar(5);
        asserEquals(new Dollar(10), five.times(2));
        asserEquals(new Dollar(15), five.times(3));
    }

    static Dollar dollar(int amount) {
        return new Dollar(amonut);
    }
```

2. Update the declaration of the factory method
```
    public void testMultiplication() {
        Money five = Money.dollar(5);
        asserEquals(new Dollar(10), five.times(2));
        asserEquals(new Dollar(15), five.times(3));
    }

    abstrcat class Money
    abstract Money times(int multiplier);

    static Money dollar(int amount) {
        return new Dollar(amonut);
    }
```

3. Apply the factory method for Franc and update unit test
```
    static Money franc(int amount) {
        return new Franc(amonut);
    }

    public void testMultiplication() {
        Money five = Money.franc(5);
        asserEquals(Money.franc(10), five.times(2));
        asserEquals(Money.franc(15), five.times(3));
    }
```

4. Update the test list

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
Common times
~~Compare Francs with Dollars~~
Currency?
Delete testFrancMultiplication?

# Summary
1. Moved at least a declaration of the method to the common superclass
2. Decoupled test code from the existence of concrete subclasses by introducting factory methods
