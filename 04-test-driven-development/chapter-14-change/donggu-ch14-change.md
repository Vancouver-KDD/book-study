# Chapter 14. Change

| Test list |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
$5 + $5  = $10
Return Money from $5 + $5
~~Bank.reduce(Money)~~
**Reduce Money with conversion**
Reduce(Bank, String)

## Steps
### 0. Test: testReduceMoneyDifferentCurrency() 
```
    public void testReduceMoneyDifferentCurrency() {
        Bank bank= new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result= bank.reduce(Money.franc(2), "USD");
        assertEquals(Money.dollar(1), result);
    }
```

### 1. Money
```
    public Money reduce(String to) {
        int rate = (currency.equals("CHF") && to.equals("USD"))
        ? 2
        : 1;
        return new Money(amount / rate, to);
    }
```
--> Bank should be the only place that cares of exchange currency rate

### 2. Bank
```   
    Money reduce(Expression source, String to) {
        return source.reduce(this, to)
    }
```

### 3. implementors
```
    #Expression
    Money reduce(Bank bank, String to);

    #Sum
    public Money reduce(Bank bank, String to) {
        int amount = augend.amount + addend.amount;
        return new Money(amount, to);
    }

    #Money
    public Money reduce(String to) {
        int rate = (currency.equals("CHF") && to.equals("USD"))
        ? 2
        : 1;
        return new Money(amount / rate, to);
    }
```

### 4. Update: Bank and Money
```   
    # Bank
    int rate(String from, String to) {
        return (from.equals("CHF") && to.equals("USD"))
        ? 2
        : 1;
    }

    # Money
    public Money reduce(Bank bank, String to) {
        int rate = bank.rate(currency, to);
        return new Money(amount / rate, to);
    }
```

### 5. Pair for a lookup table
```   
    private class Pair {
        private String from; private String to;
        Pair(String from, String to) { 
            this.from= from;
            this.to= to;
        }
    }

    public boolean equals(Object object) {
        Pair pair= (Pair) object;
        return from.equals(pair.from) && to.equals(pair.to);
    }

    # using '0' as a hash value for quick test and develope for now.
    public int hashCode() {
        return 0;
    }
```
--> We need to implement equals() and hashCode()

### 6. Update Bank to use the hashCode
```
    private Hashtable rates= new Hashtable();

    void addRate(String from, String to, int rate) {
        rates.put(new Pair(from, to), new Integer(rate));
    }

    int rate(String from, String to) {
        Integer rate= (Integer) rates.get(new Pair(from, to)); return rate.intValue();
    }
```

### 7. Add a test for "usd" to "usd" case
```
    public void testIdentityRate() {
        assertEquals(1, new Bank().rate("USD", "USD"));
    }

    # bank
    int rate(String from, String to) {
        if (from.equals(to)) return 1;
        Integer rate= (Integer) rates.get(new Pair(from, to));
        return rate.intValue();
    }
```

## Update the test list
| Test list |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
~~$5 + $5 = $10~~
Return Money from $5 + $5
~~Bank.reduce(Money)~~
~~Reduce Money with conversion~~
~~Reduce(Bank, String)~~
