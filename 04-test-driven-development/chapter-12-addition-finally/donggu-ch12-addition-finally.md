# Chapter 12. Addition, Finally

## Write a new test list for only pending items from the old test list
| Test list |
| ----------- |
$5 + 10 CHF = $10 if rate is 2:1
$5 + $5  = $10

## Explanation
: Acoording to the book, TDD can't guarantee to help us to capture all uncertain design changes, new requirment, or missing features.


## Expression
```
    # Expression
    interface Expression

    # Money: Money class has to implement Expression
    class Money implements Expression
    Expression plus(Money addend) {
        return new Money(amount + addend.amount, currency);
    }

    #bank
    class Bank
    Money reduce(Expression source, String to) {
        return Money.dollar(10);
    }
```

# Summary
1. Reduced a big test to a smaller test