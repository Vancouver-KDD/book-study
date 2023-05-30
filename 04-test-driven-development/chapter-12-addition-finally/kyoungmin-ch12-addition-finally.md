### Chapter 12. Addition, Finally
<br>

### Test List
- **$5 + 10 CHF = $10 if rate is 2:1**
- **$5 + $5 = $10**
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

<br>
- 먼저 간단한 5 + 5 = 10에 대한 test식을 써보자
- plus가 없다는 warning나타남. Money.dollar.plus()를 작성하자
  

```java
public void testSimpleAddition() {
    Money sum= Money.dollar(5).plus(Money.dollar(5));
    assertEquals(Money.dollar(10), sum);
}

Money
Money plus(Money addend) {
    return new Money(amount + addend.amount, currency);
}
```  
- 두 개의 currency를 handling할 때 두 가지의 방법이 있다
  - result를 보기 원하는 currency로 rate를 적용하여 계산하는 법이다. 이 경우 변동되는 rate를 제 때에 반영하기 어렵다
  - expression으로 나타내는 것이다. (2+3)*5 이 형식 그대로 나타내는 것이다. wallet을 예로 들고 있다. 그리고 현실 세계에서 이러한 것을 반영하는 것은 bank일 것이라고 말하고 있다. 

```java
public void testSimpleAddition() {
    …
    assertEquals(Money.dollar(10), reduced);
}
```  
- 여기서 reduced는 중간 결과 값으로, bank에서 rate가 처리되는 과정을 완료한 값이라고 볼 수 있다. 나는 reduce()는 환율 적용, reduced는 환율 적용된 값 이라고 보기로 했다. 
- reduce는 bank에서 이루어 지므로 아래와 같이 추가해볼 수 있다.

```java
public void testSimpleAddition() {
    …
    Money reduced= bank.reduce(sum, "USD");
    assertEquals(Money.dollar(10), reduced);
}
```  
- 완료된 test식은 아래와 같다
```java
public void testSimpleAddition() {
    Money five= Money.dollar(5);
    Expression sum= five.plus(five);
    Bank bank= new Bank();
    Money reduced= bank.reduce(sum, "USD");
    assertEquals(Money.dollar(10), reduced);
}
```  
- compile을 위해 기본 코드를 완료해보자. 
```java
Expression
interface Expression

Money
Expression plus(Money addend) {
    return new Money(amount + addend.amount, currency);
}

Money
class Money implements Expression

Bank
class Bank {
    Money reduce(Expression source, String to) {
    return Money.dollar(10);
    }
}
```  

