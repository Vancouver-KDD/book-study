### Chapter 13. Make It
<br>

### Test List
- $5 + 10 CHF = $10 if rate is 2:1
- $5 + $5 = $10
- Return Money from $5 + $5
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
- ~~Bank.reduce(Money)~~
- **Reduce Money with conversion**
- Reduce(Bank, String)

<br>

---  
- change, 이번 챕터에서는 환율을 change하는 것을 알아볼 것이다.
- 2개의 fancs이 있고, 이 것을 dollar로 result를 보고 싶을 때,, 아래와 같이 test 케이스를 만들 수 있다.
    - CHF와 USD의 RATE는 2:1이고,
    - franc을 usd로 reduce하고 싶고,
    - 변환된 result가 Money.dollar(1)과 같은지 확인해본다
```java
public void testReduceMoneyDifferentCurrency() {
    Bank bank= new Bank();
    bank.addRate("CHF", "USD", 2);
    Money result= bank.reduce(Money.franc(2), "USD");
    assertEquals(Money.dollar(1), result);
}
```
- equals를 통해 두 currency를 확인하고 true일 경우 rate 2, 아닐 경우 1을 적용한다.
```java
public Money reduce(String to) {
    int rate = (currency.equals("CHF") && to.equals("USD"))
    ? 2
    : 1;
    return new Money(amount / rate, to);
}
```
- Money에서 rate를 handling하는 것은 옳지 않아 보인다. bank로 responsiblity를 넘겨야 할 것 같다.
- Bank를 expression에 parameter로 넘겨서, Bank.reduce가 사용할 수 있도록 하고, rate를 bank을 저장시키고, 그 bank를 Money.reduce에 전달해서 계산된 Money가 return될 수 있도록 한다.
- reduce를 가진 모든 class를 다 적용시킨다.
```java
Bank
Money reduce(Expression source, String to) {
    return source.reduce(this, to);
}

Expression
Money reduce(Bank bank, String to);

Sum
public Money reduce(Bank bank, String to) {
    int amount= augend.amount + addend.amount;
    return new Money(amount, to);
}

Money
public Money reduce(Bank bank, String to) {
    int rate = (currency.equals("CHF") && to.equals("USD"))
    ? 2
    : 1;
    return new Money(amount / rate, to);
}
```
- Money.reduce에서 rate를 다루던 것을 bank로 옮겨주고, Money에서는 계산된 Money가 return되도록 한다.

```java
Bank
int rate(String from, String to) {
    return (from.equals("CHF") && to.equals("USD"))
    ? 2
    : 1;
}
Money
public Money reduce(Bank bank, String to) {
    int rate = bank.rate(currency, to);
    return new Money(amount / rate, to);
}
```
- rate가 2와 1만 있는 것은 아니니 하드코딩하지 않고 그 값을 받을 수 있도록 한다. 
- hashtable이용: two-elements array -> two currencies as the key
    - Pair라는 class를 만들어보자
    - pair object로 convert해서 from과 to로 들어간 값이, from과 to의 currency의 equals에 들어가서 true로 만들어 내는가...(?)
```java
Pair
private class Pair {
    private String from;
    private String to;

    Pair(String from, String to) {
    this.from= from;
    this.to= to;
    }
    public boolean equals(Object object) {
        Pair pair= (Pair) object;
        return from.equals(pair.from) && to.equals(pair.to);
    }
    public int hashCode() {
        return 0;
    }
}
```
- rate를 store할 곳을 만들자
    - set the rate
    - look up the rate when asked

```java
Bank
private Hashtable rates= new Hashtable();

void addRate(String from, String to, int rate) {
    rates.put(new Pair(from, to), new Integer(rate));
}

int rate(String from, String to) {
Integer rate= (Integer) rates.get(new Pair(from, to));
return rate.intValue();
}
```