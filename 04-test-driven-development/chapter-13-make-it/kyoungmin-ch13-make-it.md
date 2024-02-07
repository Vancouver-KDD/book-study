### Chapter 13. Make It
<br>

### Test List
- **$5 + 10 CHF = $10 if rate is 2:1**
- **$5 + $5 = $10**
- **Return Money from $5 + $5**
- ~~$5 * 2 = $10~~
- ~~Make “amount” private~~
- ~~Dollar side effects?~~
- Money rounding?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
- ~~5 CHF * 2 = 10 CHF~~
- ~~Dollar/Franc duplication~~
- ~~Common equals~~
- ~~Common times~~
- ~~Compare Francs with Dollars~~
- ~~Currency?~~
- ~~Delete testFrancMultiplication?~~
- Bank.reduce(Money)

<br>

---  
- duplication을 remove할 차례이다 -> Money.dollar(10) 은 test식의 five.plis(five)와 같다. 
- Money.plus()는 real Expression인 Sum을 return 시켜야 한다. Money를 return하는 것은 bank.reduce()가 완료된 이후여야 한다.

```java
public void testPlusReturnsSum() {
    Money five= Money.dollar(5);
    Expression result= five.plus(five);
    Sum sum= (Sum) result;
    assertEquals(five, sum.augend);
    assertEquals(five, sum.addend);
}
```
- Sum 이 없다고 알려주므로, class를 만들자.
```java
Sum
class Sum implements Expression {
    Money augend;
    Money addend;

    Sum(Money augend, Money addend) {
        this.augend= augend;
        this.addend= addend;
    }

    Expression plus(Money addend) {
        return new Sum(this, addend);
    }

    Sum(Money augend, Money addend) {
    }
}
```
- sum을 만들었으니 이 것을 bank.reduce()에 넘겨 환율을 적용시켜 보자
```java
public void testReduceSum() {
    Expression sum= new Sum(Money.dollar(3), Money.dollar(4));
    Bank bank= new Bank();
    Money result= bank.reduce(sum, "USD");
    assertEquals(Money.dollar(7), result);
}
```
- reduce는 Bank에서, sum은 Sum에서 이루어져야 한다. 수정해보자.
```java
Bank
Money reduce(Expression source, String to) {
    Sum sum= (Sum) source;
    return sum.reduce(to);
}

Sum
public Money reduce(String to) {
int amount= augend.amount + addend.amount;
return new Money(amount, to);
}
```
- 다시 refactoring 해보면,,
  
```java
Bank
Money reduce(Expression source, String to) {
    if (source instanceof Money)
    return (Money) source.reduce(to);
    Sum sum= (Sum) source;
    return sum.reduce(to);
}

Money
public Money reduce(String to) {
    return this;
}

Expression
Money reduce(String to);

Bank
Money reduce(Expression source, String to) {
    return source.reduce(to);
}
```
