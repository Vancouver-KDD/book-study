# 드디어, 더하기
## 목록
- $5 + 10CHF = $10(환율 2:1일 경우) <- **이번 챕터**
- Money 반올림?
- hashCode()
- Equal null
- Equal object

## 일단 Dollar 더하기
```java
class MoneyTest {
    public void testSimpleAddition() {
        Money sum = Money.dollar(5).plus(Money.dollar(5));
        assertEquals(Money.dollar(10), sum);
    }
}

class Money {
    public Money plus(Money added) {
        return new Money(amount + added.amount, currency);
    }
}
```

## Expression의 적용(최대한 다른 클래스를 몰라야 한다 -> 유연성 up)
```java
class MoneyTest {
    public void testSimpleAddition() {
        Money five = Money.dollar(5);
        Expression sum = five.plus(five);
        Bank bank = new Bank();
        Money reduced = bank.reduce(sum, "USD");
        assertEquals(Money.dollar(10), reduced);
    }
}

interface Expression {
    
}

class Money implements Expression {
    public Expression plus(Money added) {
        return new Money(amount + added.amount, currency);
    }
}

class Bank {
    Money reduce(Expression source, String to) {
        return Money.dollar(10);
    }
}
```

## 목록
- $5 + 10CHF = $10(환율 2:1일 경우)
- $5 + $5 = $10 <- 추가
- Money 반올림?
- hashCode()
- Equal null
- Equal object

## 배운점
- 큰 테스트를 작은 테스트($5 + 10CHF에서 $5 + $5로)로 줄여서 발전을 나타낼 수 있도록 했다.
- 우리에게 필요한 게산(computation)에 대한 가능한 메타포들을 신중히 생각해봤다.
- 새 메타포에 기반하여 기존의 테스트를 재작성했다.
- 테스트를 빠르게 컴파일했다.
- 그리고 테스트를 실행했다.
- 진자 구현을 만들기 위해 필요한 리팩토링을 약간의 전율과 함께 기대했다.