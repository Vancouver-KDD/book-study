# Chapter 13. Make it

| Test list |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
**$5 + $5  = $10**
Return Money from $5 + $5

## Steps
### Money.plus()
```
public void testPlusReturnsSum() { 
    Money five= Money.dollar(5); 
    Expression result= five.plus(five);
    Sum sum= (Sum) result;
    assertEquals(five, sum.augend);
    assertEquals(five, sum.addend);
}
```

### Sum and Money class
```
    # Sum    
    class Sum implements Expression {
        Money augend;
        Money addend;
    }
    Sum(Money augend, Money addend) { 
        this.augend= augend;
        this.addend= addend;
    }

    # Money
    class Money implements Expression
    Expression plus(Money addend) {
        return new Sum(this, addend);
    }
```

### Bank
```
    public void testReduceSum() {
        Expression sum= new Sum(Money.dollar(3), Money.dollar(4));
        Bank bank= new Bank();
        Money result= bank.reduce(sum, "USD");
        assertEquals(Money.dollar(7), result);
    }

    #Bank
    Money reduce(Expression source, String to) {
        Sum sum= (Sum) source;
        int amount = sum.augend.amount + sum.addend.amount; return new Money(amount, to);
    }
```
We need to implement Bank.reduce() to work with any Expression.

### update bank and sum
```
    #Bank
    Money reduce(Expression source, String to) {
        if (source instanceof Money) 
            return (Money) source.reduce(to);
        Sum sum= (Sum) source;
        return sum.reduce(to);
    }
    ---> If we add reduce(string) to the Expression interface, we can liminate the cast checks.
        # inteface: Money reduce(String to);
    Money reduce(Expression source, String to) {
        return source.reduce(to);
    }

    #Sum
    public Money reduce(String to) {
        int amount = augend.amount + addend.amount;
        return new Money(amount, to);
    }
```

## Update the test list
| Test list |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
**$5 + $5  = $10**
Return Money from $5 + $5
~~Bank.reduce(Money)~~
Reduce Money with conversion
Reduce(Bank, String)
