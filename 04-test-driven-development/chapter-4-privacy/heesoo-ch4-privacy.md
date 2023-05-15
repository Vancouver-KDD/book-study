# Chapter 4. Privacy

### Todo List

- $5 + 10 CHF = $10 if rate is 2:1
- ~~$5 \* 2 = $10~~
- **Make "amount" private**
- ~~Dollar side-effects?~~
- Money rounding?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object

Change assertion to compare `Dollar` to `Dollar`

```
public void testMultiplication() {
    Dollar five= new Dollar(5);
    assertEquals(new Dollar(10), five.times(2));
    assertEquals(new Dollar(15), five.times(3));
}
```

Now, `Dollar` is the only class using its `amount` instance vaiable -> make `amount` private

```
private int amount;
```
