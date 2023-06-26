# Chapter 5. Franc-ly Speaking

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
- **5CHF \* 2 = 10CHF**

### 1. Write a test

- Dollar 테스트 코드를 복사해서 Dollar를 Franc 으로만 변경

```
public void testFrancMultiplication() {
    Franc five= new Franc(5);
    assertEquals(new Franc(10), five.times(2));
    assertEquals(new Franc(15), five.times(3));
}
```

- 왜? 우리는 복사 붙여넣기를 안하는거 아닌가? -> 우리의 TDD 사이클 각각의 순서는 다른 목적을 가지고 있음 -> 일단 1-3단계는 빠르게 실행시키는 것이 주 목적임
  1. Write a test
  2. Make it compile
  3. Run it to see that it fails
  4. Make it run
  5. Remove duplication

### 2. Make it run

- 일단 Dollar 함수를 복사해서 함수를 빠르게 만들어줌

```
class Franc {
    private int amount;

    Franc(int amount) {
        this.amount= amount;
    }

    Franc times(int multiplier) {
        return new Franc(amount * multiplier);
    }

    public boolean equals(Object object) {
        Franc franc= (Franc) object;
        return amount == franc.amount;
    }
}
```

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
- Dollar/Franc duplication
- Common equals
- Common times

  **5CHF \* 2 = 10CHF** 문제는 해결했지만 Dollar/Franc duplication 문제가 생겼고, **equals()** 와 **times()** 를 generalize 할 필요가 있음

- 하나씩 해결해 나갈 수 있는 수준의 작은 테스트를 만들어냄
- 그냥 복사 붙여넣기에서 약간 수정을 통한 테스트를 막 만들어냄
- 테스트를 통과시키기 위해서 모델코드를 복사 붙여넣기 해서 약간의 수정을 통해 테스트를 통과시킴
- 하지만, duplication을 없앨때 까지 다 끝난게 아님
