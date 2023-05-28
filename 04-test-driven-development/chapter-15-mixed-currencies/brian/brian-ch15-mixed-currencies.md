# 서로 다른 통화 더하기
## 목록
- $5 + 10CHF = $10(환율 2:1일 경우) <- **이번 챕터**
- ~~$5 + $5 = $10~~
- $5 + $5에서 Money 반환하기
- ~~Bank.reduce(Money)~~
- ~~Money에 대한 통화 변환을 수행하는 Reduce~~
- ~~Reduce(Bank, String)~~
- Money 반올림?
- hashCode()
- Equal null
- Equal object

## $5 + 10CHF Test
```java
class MoneyTest {
    // 같은 통화의 환율 비는 1이다.
    public void testMixedAddition() {
        Money fiveBucks = Money.dollar(5);
        Money tenFrancs = Money.franc(10);
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result = bank.reduce(fiveBucks.plus(tenFrances)), "USD");
        assertEquals(Money.dollar(10), result);
    }
}

class Sum implements Expression {
    Moeny augend; // 피가산수
    Money addend; // 기수

    public Sum(Moeny augend, Money addend) {
        this.augend = augend;
        this.addend = addend;
    }
    
    public Meony reduce(Bank bank, String to) {
        int amount = augend.reduce(bank, to).amount 
                + addend.reduce(bank, to).amount;
        return new Money(amount, to);
    }
}
```

## Money 를 Expression 으로 바꾸기
```java
class Sum implements Expression {
    Expression augend; // 피가산수
    Expression addend; // 기수

    public Sum(Expression augend, Expression addend) {
        this.augend = augend;
        this.addend = addend;
    }
}

class Money {
    Expression plus(Expression addend) {
        return new Sum(this, addend);
    }
    
    Expression times(int multiplier) {
        return new Money(amount * multiplier, currency);
    }
}
```

## Sum이 Composite 같다. Composite 패턴 이란?
- 클라이언트 입장에서는 ‘전체’나 ‘부분’이나 모두 동일한 컴포넌트로 인식할 수는 계층 구조 를 만든다.
- UML
  - ![compositeUML.png](compositeUML.png)
- Example
  - ![compositeExample.png](compositeExample.png)

```java
class MoneyTest {
    // 같은 통화의 환율 비는 1이다.
    public void testMixedAddition() {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result = bank.reduce(fiveBucks.plus(tenFrances), "USD");
        assertEquals(Money.dollar(10), result);
    }
}

interface Expression {
    Money reduce(Bank bank, String to);
    Expression plus(Expression addend);
}
```

## 목록
- ~~$5 + 10CHF = $10(환율 2:1일 경우)~~
- ~~$5 + $5 = $10~~
- $5 + $5에서 Money 반환하기
- ~~Bank.reduce(Money)~~
- ~~Money에 대한 통화 변환을 수행하는 Reduce~~
- ~~Reduce(Bank, String)~~
- Sum.plus
- Expression.times
- Money 반올림?
- hashCode()
- Equal null
- Equal object

## 배운점
- 원하는 테스트를 작성하고, 한 단계에 달성할 수 있도록 뒤로 물렀다.
- 좀더 추상적인 선언을 통해 가지에서 뿌리(애초의 테스트 케이스)로 일반화했다.
- 변경 후(Expression fiveBucks), 그 영향을 받은 다른 부분들을 변경하기 위해 컴파일러의 지시를 따랐다(Expression에 plus()를 추가하기 등등).