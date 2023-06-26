# Chapter 11. The Root of All Evil

### Todo List

- $5 + 10 CHF = $10 if rate is 2:1
- ~~$5 \* 2 = $10~~
- ~~Make "amount" private~~
- ~~Dollar side-effects?~~
- Money rounding?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
- ~~5CHF \* 2 = 10CHF~~
- **Dollar/Franc duplication**
- ~~Common equals~~
- ~~Common times~~
- ~~Compare Francs with Dollars~~
- ~~Currency?~~
- Delete testFrancMultiplication?

Dollar 와 Franc 은 각각 contructors 를 가지고 있음
-> 하지만 contructor는 subclass를 가질 수 있는 충분한 이유가 되지 못하므로 subclass 들을 제거 하고 싶음

subclass 레퍼런스를 superclass 레퍼런스를 가지고 대체할 수 있음

```
// Franc
static Money franc (int amount) {
    return new Moeny(amount, "CHF");
}
```

```
// Dollar
static Money dollar (int amount) {
    return new Moeny(amount, "USD");
}
```

이제 Dollar를 레퍼런스하는 곳은 없는데 Franc은 우리가 마지막에 쓴 테스트 코드에 있음

```
public void testDifferentClassEquality() {
    assertTrue(new Money(10, "CHF")).equals(new Franc(10, "CHF"));
}
```

우리가 이 테스트코드를 삭제해도 될만큼 테스트가 충분히 커버하고 있음 -> 지우자!
-> 테스트를 삭제함으로 `Franc`도 사용되는 곳이 없으니 superclass 로 대체할 수 있음
-> 지금 dollar 랑 franc mulitplication 가 각각 따로 작서오디어 있음
-> 이제 클래스가 superclass로 하나로 합쳐졌으니 테스트도 두개로 나뉠 필요 없음
-> delete testFrancMultiplication()
