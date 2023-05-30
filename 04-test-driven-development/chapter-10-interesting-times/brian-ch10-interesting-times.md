# 흥미로운 시간
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
- 공용 times **<- 이번 챕터**
- ~~Franc와 Dollar 비교하기~~
- ~~통화?~~
- testFrancMultiplication 제거

## 공용 times
```java
class Franc {
    Money times(int multiplier) {
        return new Money(amount * multiplier, currency);
    }   
}

class Money { // 콘크리트 클래스로 만든다
    Money times(int multiplier) {
        return null;
    }
    //...
    boolean equals(Obejct object) {
        Money money = (Money) object;
        return amount == money.amount
                && currency().equals(money.currency());
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
- ~~공용 times~~
- ~~Franc와 Dollar 비교하기~~
- ~~통화?~~
- testFrancMultiplication 제거

## 배운점
- 두 times()를 일치시키기 위해 그 메서드들이 호출하는 다른 메서드들을 인라인시킨 후 상수를 변수로 바꿔주었다.
- 단지 디버깅을 위해 테스트 없이 toString()을 작성했다.
- Franc 대신 Money를 반환하는 변경을 시도한 뒤 그것이 잘 작동할지를 테스트가 말하도록 했다.
- 실험해본 걸 뒤로 물리고 또 다른 테스트를 작성했다. 테스트를 작동했더니 실험도 제대로 작동했다.