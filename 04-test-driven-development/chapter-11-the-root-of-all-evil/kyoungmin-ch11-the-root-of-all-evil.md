### Chapter 11. The Root of All Evil
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
- ~~Common times~~
- ~~Compare Francs with Dollars~~
- ~~Currency?~~
- Delete testFrancMultiplication?

<br>

---  
- 두 개의 subclass들은 constructor만 가지고 있다. 이것도 마저 없애버리자
- subclass을 없앨 때 앞 chapter에서 한 것처럼 그에 대한 reference를 먼저 없애야 한다. 아래의 부분이다

```java
before
   	static Money dollar(int amount) {
		  return new Dollar(amount, "USD");
	  }	
	  static Franc franc(int amount) {
		  return new Franc(amount, "CHF");
	  }	
```

```java
after
   	static Money dollar(int amount) {
		  return new Money(amount, "USD");
	  }	
	  static Franc franc(int amount) {
		  return new Money(amount, "CHF");
	  }	
```
- test부분에서도 한 개를 찾을 수 있다.
```java
  public void testDifferentClassEquality() {
    assertTrue(new Money(10, "CHF").equals(new Franc(10, "CHF")));
  }
```
이 것을 없앨 수 있는지 다른 equality test를 살펴보는데, 이미 testEquality()에서 잘 커버하고 있고, 이 중 2줄은 없애도 좋을 것으로 보인다. 
```java
  public void testEquality() {
    assertTrue(Money.dollar(5).equals(Money.dollar(5)));
    assertFalse(Money.dollar(5).equals(Money.dollar(6)));
    assertFalse(Money.franc(5).equals(Money.dollar(5)));
  }
```
- multiple class가 있으면 다 test해도 좋겠지만 현재 우리는 subclass를 다 없애려고 하는 중이고, currency를 test하는 것에 집중해야 하므로 이렇게 하면 더 간결한 코드를 작성할 수 있음
  
 



___
Next
- times()를 push up 할 것임