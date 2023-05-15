# 객체 만들기

## 목록
- $5 + 10CHF = $10(환율 2:1일 경우)
- ~~$5 x 2 = $10~~
- ~~amount를 private로 만들기~~
- ~~Dollar 부작용?~~
- Money 반올림?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
- ~~5CHF x 2 = 10CHF~~
- Dollar/Franc 중복
- ~~공용 equals~~
- 공용 times
- ~~Franc와 Dollar 비교하기~~
- 통화?

```java
abstract class Money {
    static Money dollar(int amount) {
        return new Dollar(amount);
    }
    
    protected int amount;
    Money(int amount) {
        this.amount= amount;
    }
    
    abstract Money times(int multiplier);
   
    public boolean equals(Object object) {
        Money money = (Money) object;
        return amount == money.amount;
    }
}
```

### Money Class가 Test에서 사라짐 -> Decoupling
```java
public void testMultiplication() {
    Money five = Money.dollar(5);
    assertEquals(Money.dollar(10), five.times(2));
    assertEquals(Money.dollar(15), five.times(3));
}

public void testEquality() {
    assertTrue(Money.dollar(5).equals(Money.dollar(5)));
    assertFalse(Money.dollar(5).equals(Money.dollar(6)));
    assertTrue(new Franc(5).equals(new Franc(5)));
    assertFalse(new Franc(5).equals(new Franc(6)));
    assertFalse(new Franc(5).equals(Money.dollar(5)));
}
```
## 목록
- $5 + 10CHF = $10(환율 2:1일 경우)
- ~~$5 x 2 = $10~~
- ~~amount를 private로 만들기~~
- ~~Dollar 부작용?~~
- Money 반올림?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
- ~~5CHF x 2 = 10CHF~~
- Dollar/Franc 중복
- ~~공용 equals~~
- 공용 times
- ~~Franc와 Dollar 비교하기~~
- 통화?
- testFrancMultiplication을 지워야 할까? (추가)

## 배운점
- 동일한 메서드(times)의 두 변이형 메서드 서명부를 통일시킴으로써 중복 제거를 향해 한 단계 더 전진했다.
- 최소한 메서드 선언부만이라도 곹오 상위클래스로 옮겼다.
- 팩토리 메서드를 동비하여 테스트 코드에서 콘크리트 하위 클래스의 존재 사실을 분리해 냈다.
- 하위 클래스가 사자리지면 몇몇 테스트는 불필요한 여분의 것이 된다는 것을 인식했다. 하지만 일단 그냥 뒀다.