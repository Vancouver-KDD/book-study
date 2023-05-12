### Chapter 7. Apples and Oranges
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
- ~~Common equals~~
- Common times
- **Compare Francs with Dollars**

<br>

---  
- Franc과 Dollar의 비교!
- Two Moneys are equal only if their amounts and classes are equal. -> 이걸 코드로 나타내면 아래와 같다.
  
```java
 public boolean equals(Object object) {
    Money money = (Money) object;
    return amount == money.amount
    && getClass().equals(money.getClass());
}
```

- 위의 코드는 currency를 고려하지 않고 현재 있는 class만을 사용하고 있다. 그래서 currency를 test list에 추가하는 것이 좋겠음!
- mixed currency arithmetic을 하기 위해서 common times()를 제거해야 할 것임.

___
Conclusion
- Took an objection that was bothering us and turned it into a test
- Made the test run a reasonable, but not perfect way—getClass()
- Decided not to introduce more design until we had a better motivation