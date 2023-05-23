# Chapter 15. Mixed Currencies

| Test list |
| ----------- |
##$5 + 10 CHF = $10 if rate is 2:1##
~~$5 + $5 = $10~~
Return Money from $5 + $5
~~Bank.reduce(Money)~~
~~Reduce Money with conversion~~
~~Reduce(Bank, String)~~

## Steps
### 0. Test: testMixedAddition() 
```    
    public void testMixedAddition() {
        Money fiveBucks= Money.dollar(5);
        Money tenFrancs= Money.franc(10);
        Bank bank= new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result= bank.reduce(fiveBucks.plus(tenFrancs), "USD");
        assertEquals(Money.dollar(10), result);
    }
```

### 1. Update Sum reduce() and constructor
```
    public Money reduce(Bank bank, String to) {
        int amount= augend.reduce(bank, to).amount
        + addend.reduce(bank, to).amount;
        return new Money(amount, to);
    }

    Sum(Expression augend, Expression addend) {
        this.augend= augend;
        this.addend= addend;
    }
```
--> Bank should be the only place that cares of exchange currency rate

### 2. Update money as well
```   
    Expression plus(Expression addend) {
        return new Sum(this, addend);
    }

    Expression times(int multiplier) {
        return new Money(amount * multiplier, currency);
    }
```

### 3. Update testMixedAddition()
```    
    public void testMixedAddition() {
        Expression fiveBucks= Money.dollar(5);
        Expression tenFrancs= Money.franc(10);
        Bank bank= new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result= bank.reduce(fiveBucks.plus(tenFrancs), "USD");
        assertEquals(Money.dollar(10), result);
    }
```

### 4. Update Expresion, Money, and Sum. plus() should be public.
```   
    Expression plus(Expression addend);

    # Money
    public Expression plus(Expression addend) {
        return new Sum(this, addend);
    }

    # Sum
    public Expression plus(Expression addend) {
        return null;
    }
```

## Update the test list
| Test list |
| ----------- |
~~$5 + 10 CHF = $10 if rate is 2:1~~
~~$5 + $5 = $10~~
Return Money from $5 + $5
~~Bank.reduce(Money)~~
~~Reduce Money with conversion~~
~~Reduce(Bank, String)~~
Sum.plus
Expression.times
