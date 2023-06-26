### Chapter 8. Makin’ Objects
<br>

### Test List
- $5 + 10 CHF = $10 if rate is 2:1
- ~~$5 * 2 = $10~~
- ~~Make “amount” private~~
- ~~Dollar side effects?~~
- Money rounding?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
- ~~5 CHF * 2 = 10 CHF~~
- **Dollar/Franc duplication**
- ~~Common equals~~
- Common times
- ~~Compare Francs with Dollars~~
- Currency?

<br>

---  
- Dollar와 Franc의 times()가 similar
- 그리고 two subclass의 필요성이 떨어지니 eliminate해보자. 
- Dollar를 return하는 Money의 factory method를 이용하자
  * factory method란? 객체 생성을 캡슐화하는 패턴으로, 객체 생성 코드를 인터페이스에 정의하고 서브클래스에서 이를 구현하는 방식

```java
public void testMultiplication() {
    Dollar five = Money.dollar(5);
    assertEquals(new Dollar(10), five.times(2));
    assertEquals(new Dollar(15), five.times(3));
}
```
- Dollar를 create/return하는 implementation
  
```java
static Dollar dollar(int amount) {
    return new Dollar(amount);
}
```

- 우리는 Dollar에 대한 reference를 없애고 싶다.(Franc도)
  
```java
abstract class Money
abstract Money times(int multiplier);

static Money dollar(int amount) {
return new Dollar(amount);
}

static Money franc(int amount) {
return new Franc(amount);
}

public void testFrancMultiplication() {
    Money five = Money.franc(5);
    assertEquals(Money.franc(10), five.times(2));
    assertEquals(Money.franc(15), five.times(3));
}

public void testEquality() {
    assertTrue(Money.dollar(5).equals(Money.dollar(5)));
    assertFalse(Money.dollar(5).equals(Money.dollar(6)));
    assertTrue(Money.franc(5).equals(Money.franc(5)));
    assertFalse(Money.franc(5).equals(Money.franc(6)));
    assertFalse(Money.franc(5).equals(Money.dollar(5)));
}
```

___
Conclusion
- Took a step toward eliminating duplication by reconciling the signatures of two variants of the same method—times()
- Moved at least a declaration of the method to the common superclass
- Decoupled test code from the existence of concrete subclasses by introducing factory methods
- Noticed that when the subclasses disappear some tests will be redundant, but took no action