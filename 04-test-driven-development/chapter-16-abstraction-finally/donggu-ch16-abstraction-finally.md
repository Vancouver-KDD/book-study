# Chapter 16. Abstraction, Finally.

| Test list |
| ----------- |
~~$5 + 10 CHF = $10 if rate is 2:1~~
~~$5 + $5 = $10~~
Return Money from $5 + $5
~~Bank.reduce(Money)~~
~~Reduce Money with conversion~~
~~Reduce(Bank, String)~~
**Sum.plus**
Expression.times

## Steps
### 0. Test: testSumPlusMoney() 
```    
    public void testSumPlusMoney() {
        Expression fiveBucks= Money.dollar(5);
        Expression tenFrancs= Money.franc(10);
        Bank bank= new Bank();
        bank.addRate("CHF", "USD", 2);
        Expression sum= new Sum(fiveBucks, tenFrancs).plus(fiveBucks);
        Money result= bank.reduce(sum, "USD");
        assertEquals(Money.dollar(15), result);
    }
```

* In TDD, it is common to write the same number of lines of test code.

### 1. Update Sum.plus()
```
    public Expression plus(Expression addend) {
        return new Sum(this, addend);
}
```

## Expression.times
### Steps
#### 0. Test: testSumPlusMoney() 
```
    public void testSumTimes() {
        Expression fiveBucks= Money.dollar(5);
        Expression tenFrancs= Money.franc(10);
        Bank bank= new Bank();
        bank.addRate("CHF", "USD", 2);
        Expression sum= new Sum(fiveBucks, tenFrancs).times(2); 
        Money result= bank.reduce(sum, "USD");
        assertEquals(Money.dollar(20), result);
    }
```
--> Bank should be the only place that cares of exchange currency rate

#### 2. Update Sum, Expression, and Money
```   
    # Sum
    public Expression times(int multiplier) {
        return new Sum(augend.times(multiplier),addend.times(multiplier));
    }

    # Expression
    Expression times(int multiplier);

    # Money
    public Expression times(int multiplier) {
        return new Money(amount * multiplier, currency);
    }
```

## Return Money from $5 + $5
### Steps
#### 0. Test: testPlusSameCurrencyReturnsMoney() 
```
    public void testPlusSameCurrencyReturnsMoney() {
        Expression sum= Money.dollar(1).plus(Money.dollar(1));
        assertTrue(sum instanceof Money);
    }
```

#### 2. Update Money
```   
    public Expression plus(Expression addend) {
        return new Sum(this, addend);
    }
```

## Update the test list
| Test list |
| ----------- |
~~$5 + 10 CHF = $10 if rate is 2:1~~
~~$5 + $5 = $10~~
~~Return Money from $5 + $5~~
~~Bank.reduce(Money)~~
~~Reduce Money with conversion~~
~~Reduce(Bank, String)~~
~~Sum.plus~~
~~Expression.times~~
