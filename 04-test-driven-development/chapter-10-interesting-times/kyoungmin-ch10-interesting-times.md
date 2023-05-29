### Chapter 10. Interesting Times
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
- ~~Dollar/Franc duplication~~
- ~~Common equals~~
- **Common times**
- ~~Compare Francs with Dollars~~
- ~~Currency?~~
- Delete testFrancMultiplication?

<br>

---  
- 두 개의 subclass를 없애고, Money만 남기는 것이 목표이다. 그럴려면,,
1. amount와 multiplier를 곱한 값으로 새로운 Money object를 return 해야 한다. 그리고 currency가 무엇인지 구별 가능해야 한다. 
   - Money object를 생성할 수 있게 abstract를 지워야 한다.
   - times()를 Money class에 직접 구현해야 한다. 
   - 새로운 object가 currency를 가지려면 currency를 times()에 전달해야 한다. 
   - subclass 두 개를 완전히 같게 만들어야 한다. 그래야 Money class에서 push up 할 수 있다. => 현재는 getClass()를 비교하고 있기 때문에 subclass에 dependency가 있다. 그게 아닌 currency()를 비교해야 한다.  

```java
    Franc
    Money times(int multiplier) {
      return new Franc(amount * multiplier, currency);
    }
 
    Dollar
    Money times(int multiplier) {
      return new Dollar(amount * multiplier, currency);
    }
```

  
 



___
Next
- times()를 push up 할 것임