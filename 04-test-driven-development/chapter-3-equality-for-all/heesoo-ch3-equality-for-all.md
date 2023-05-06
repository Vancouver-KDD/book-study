# Chapter 3. Equality for All

### Todo List

- $5 + 10 CHF = $10 if rate is 2:1
- ~~$5 \* 2 = $10~~
- Make "amount" private
- ~~Dollar side-effects?~~
- Money rounding?
- **equals()**
- hashCode()

### Test equality

1. Write a test

```
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
}
```

2. Write a function with fake implementation -> always return `true`

```
public boolean equals(Object object) {
    return true;
}
```

3. generalize equality

```
public boolean equals(Object object) {
    Dollar dollar = (Dollar) object;
    return amount == dollar.amount;
}
```

### Todo List

- $5 + 10 CHF = $10 if rate is 2:1
- ~~$5 \* 2 = $10~~
- Make "amount" private
- ~~Dollar side-effects?~~
- Money rounding?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
