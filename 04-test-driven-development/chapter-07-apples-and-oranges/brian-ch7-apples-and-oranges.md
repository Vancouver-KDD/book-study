# 사과와 오렌지 (You can't compare apples and oranges)

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
- Franc와 Dollar 비교하기

```java
abstract class Money {    
    protected int amount;
    Money(int amount) {
        this.amount= amount;
    }
   
    public boolean equals(Object object) {
        Money money = (Money) object;
        return amount == money.amount
                && getClass().equals(money.getClass());
    }
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
- ~~Franc와 Dollar 비교하기~~ (삭제)
- 통화? (추가)

## 배운점
- 우릴 괴롭히던 결함을 끄집어내서 테스트에 담아냈다.
- 완벽하진 않지만 그럭저럭 봐줄 만한 방법(getClass())으로 테스트를 통고하게 만들었다.
- 더 많은 동기가 있기 전에는 더 많은 설계를 도입하지 않기로 했다.