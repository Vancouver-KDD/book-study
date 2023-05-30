# 우리가 사는 시간(곱하기 times)

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
- 통화?  **<- 이번 챕터**
- testFrancMultiplication 제거

## 통화 적용
flyweight factories란?
```java
public class FlyweightInJava {
    public static void main(String[] args) {
        Integer i1 = Integer.valueOf(10);
        Integer i2 = Integer.valueOf(10);
        System.out.println(i1 == i2);
    }
}
```

```java
class Money {
    abstract String currency();
    // OR
    protected String currency;
}
```

```java
class Money {
    Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
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
- ~~Franc와 Dollar 비교하기~~
- ~~통화?~~
- testFrancMultiplication 제거

## 배운점
- 큰 설계 아이디어를 다루다가 조금 곤경에 빠졌다. 그래서 좀 전에 주목했던 더 작은 작업을 수행했다.
- 다른 부분들을 호출자(팩토리 메서드)로 옮김으로써 두 생성자를 일치시켰다.
- times()가 팩토리 메서드를 사용하도록 만들기 위해 리팩토링을 잠시 중단했다.
- 비슷한 리팩토링(Franc에 했던 일을 Dollar에도 적용)을 한번의 큰 다계로 처리했다.
- 동일한 생성자들을 상위 클래스로 올렸다.