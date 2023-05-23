# Chapter 9. Times We're Livin In

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
Dollar/Franc duplication
~~Common equals~~
Common times
~~Compare Francs with Dollars~~
**Currency?**
Delete testFrancMultiplication?


## How do we want to test for currencies?

- With 'string':
```
    public void testCurrency() {
        assertEquals("USD", Money.dollar(1).currency());
        assertEquals("CHF", Money.franc(1).currency());
    }    
```

- Money, Franc, and Money implementation:
- Since the currency() function is identical we can implement the function in the super class.
```
    # Money (**Superclass**)
    static Money franc(int amount) {
        return new Franc(amount, "CHF");
    }
    static Money Dollar(int amount) {
        return new Dollar(amount, "USD");
    }
    protected String currency:
    String currency() {
        return currency;
    }

    # Franc (**Sublass**)
    private String currency;
    Franc(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }  
    String currency() {
        return currency;
    }
    Money times(int multiplier) {
        return Money.Franc(amount * multiplier);
    }

    # Dollar (**Sublass**)
    private String currency;
    Dollar(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    String currency() {
        return currency;
    }
    Money times(int multiplier) {
        return Money.dollar(amount * multiplier);
    }
```

## Final implementation for the currency
```
    # Money (**Superclass**)
    Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    # Franc (**Sublass**)
    Franc(int amount, String currency) {
        super(amount, currency)
    }

    # Dollar (**Sublass**)
    Dollar(int amount, String currency) {
        super(amount, currency)
    }
```

## Update the test list

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
Dollar/Franc duplication
~~Common equals~~
Common times
~~Compare Francs with Dollars~~
~~Currency?~~
Delete testFrancMultiplication?

# Summary
1. In order to support currency, we eliminated two identical implementation by using superclass's constructor.
