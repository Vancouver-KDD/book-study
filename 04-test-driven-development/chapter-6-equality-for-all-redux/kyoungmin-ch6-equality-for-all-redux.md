### Chapter 5. Equality for All, Redux
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
- Dollar/Franc duplication
- **Common equals**
- Common times

<br>

---  
- new test(franc)가 working하였음
- clean-up 하기 위해서,
  - to make one of our classes extend the other. 
  - **to find a common superclass for the two classes -> Money**
    (subclass가 access할 수 있도록 private 대신 protected를 쓴다)

```java
class Money {
    protected int amount;
}

class Dollar extends Money {
}
```
- common class(Money)를 create했으니 equals()의 dollar(franc) 부분을 Money/money에 맞게 바꿔야 함

(before) 

```java
public boolean equals(Object object) {
    Dollar dollar= (Dollar) object;
    return amount == dollar.amount;
}
```
(after)   
```java
public boolean equals(Object object) {
    Money money= (Money) object;
    return amount == money.amount;
}
```

- 공통적인 equals()가 생겼으니 Franc.equals()를 제거해야 함
  - 이전 챕터에서 multiply를 test하기 위한 Franc.equals()는 작성했지만 Franc to Franc을 comparing하는 test는 cover하지 못 했다. 이렇게 test가 enough하지 못할 때는 refactoring할 때 뭔가를 break할 수 있으니 조심해야 함. 

```java
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
    assertFalse(new Dollar(5).equals(new Dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6)));
}
```

-  또 다른 duplication이 생겼으니, Money를 extend한 Franc class를 만들자

```java
class Franc extends Money {
}
```

- Franc.equals()도 만들어주자. dollar 대신 franc을 넣어주면,

```java
public boolean equals(Object object) {
    Money franc= (Franc) object;
    return amount == franc.amount;
}
```
  -   조금 더 common한 형식으로 만들면, 
  
```java
public boolean equals(Object object) {
    Money money= (Money) object;
    return amount == money.amount;
}
```

==> Franc.equals()와 Money.equals()가 같은 모양이다. 그럼로 Franc를 지워주고 test를 run해보자.

___
Conclusion
- Stepwise moved common code from one class (Dollar) to a superclass
(Money)
- Made a second class (Franc) a subclass also
- Reconciled two implementations—equals()—before eliminating the redundant
one