# 진짜로 만들기
## 목록
- $5 + 10CHF = $10(환율 2:1일 경우)
- $5 + $5 = $10 <- **이번 챕터**
- $5 + $5에서 Money 반환하기 <- 추가 (최적화 일때)
- Money 반올림?
- hashCode()
- Equal null
- Equal object

## Expression 이란?

```java
class MoneyTest {
    public void testSimpleAddition() {
        Money five = Money.dollar(5);
        Expression result = five.plus(five);
        Sum sum = (Sum) result;
        Bank bank = new Bank();
        Money reduced = bank.reduce(sum, "USD");
        assertEquals(Money.dollar(10), reduced);
    }
}

class Sum implements Expression {
    Moeny augend; // 피가산수
    Money addend; // 기수
    
    public Sum(Moeny augend, Money addend) {
        this.augend = augend;
        this.addend = addend;
    }
}

class Money {
    Expression plus(Money addend) {
        return new Sum(this, addend);
    }
}
```

## Sum Test
```java
class MoneyTest {
    public void testSimpleAddition() {
        Sum sum = new Sum(Money.aollar(3), Money.aollar(4));
        Bank bank = new Bank();
        Money reduced = bank.reduce(sum, "USD");
        assertEquals(Money.dollar(7), reduced);
    }
}

class Bank {
    Money reduce(Expression source, String to) {
        Sum sum = (Sum) source; // 지저분스 (모든 Expression 지원 필요)
        int amount = sum.augend.amount + sum.addend.amount; // 지저분스 (두번 호출)
        return Money(smount, to);
    }
}
```
## 지저부한 코드 수정 코드
```java
class Bank {
    Money reduce(Expression source, String to) {
        Sum sum = (Sum) source;
        return sum.reduce(to);
    }
}

class Sum implements Expression {
    public Money reduce(String to) {
        int amount = augend.amount + addend.amount;
        return new Money(amount, to);
    }
}
```

## 목록
- $5 + 10CHF = $10(환율 2:1일 경우)
- $5 + $5 = $10 <- **이번 챕터**
- $5 + $5에서 Money 반환하기
- Bank.reduce(Money) <- 추가
- Money 반올림?
- hashCode()
- Equal null
- Equal object

## Bank.reduce(Money) test
```java
class MoneyTest {
    public void testSimpleAddition() {
        Bank bank = new Bank();
        Money reduced = bank.reduce(Money.dollar(1), "USD");
        assertEquals(Money.dollar(1), reduced);
    }
}

class Bank {
    Money reduce(Expression source, String to) {
        if (source instanceof Moeny) return (Money) source; // 지저분스
        Sum sum = (Sum) source; // still 지저분스
        return sum.reduce(to);
    }
}
```

## 지저분한 Bank 리팩토링
```java
class Bank {
    Money reduce(Expression source, String to) {
        return source.reduce(to); // 깔끔스
    }
}

interface Expression {
    Money reduce(String to);
}

class Money implements Expression {
    public Money reduce(String to) {
        return this;
    }
}
```

## 고민
- Bank.reduce(Expression, String)와 Expression.reduce(String)의 차이점
  - 위치 매개 변수(positional parameters)만으로 두 메서드가 어떻게 다른지에 대해 코드에 명확히 담아내기 어려움

## 목록
- $5 + 10CHF = $10(환율 2:1일 경우)
- $5 + $5 = $10 <- **이번 챕터**
- $5 + $5에서 Money 반환하기
- ~~Bank.reduce(Money)~~
- Money에 대한 통화 변환을 수행하는 Reduce <- 추가
- Reduce(Bank, String) <- 추가
- Money 반올림?
- hashCode()
- Equal null
- Equal object

## 배운점
- 모든 중복이 제거되기 전까지는 테스트를 통과한 것으로 치지 않았다.
- 구현하기 위해 역방향이 아닌 순방향으로 작업했다.
- 앞으로 필요할 것으로 예상되는 객체(Sum)의 생성을 강요하기 위한 테스트를 작성했다.
- 빠른 속도로 구현하기 시작했다(Sum의 생성자).
- 일단 한 곳에 캐스팅을 이용해서 코드를 구현했다가, 테스트가 돌아가자 그 코드를 적당한 자리로 옮겼다.
- **명시적인 클래스 검사를 제거하기 위해 다형성을 사용했다.**