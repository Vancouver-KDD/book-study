# 드디어, 추상화
## 목록
- ~~$5 + 10CHF = $10(환율 2:1일 경우)~~
- ~~$5 + $5 = $10~~
- $5 + $5에서 Money 반환하기  <- **이번 챕터**
- ~~Bank.reduce(Money)~~
- ~~Money에 대한 통화 변환을 수행하는 Reduce~~
- ~~Reduce(Bank, String)~~
- Sum.plus
- Expression.times
- Money 반올림?
- hashCode()
- Equal null
- Equal object

## Sum.plus Test
```java
class MoneyTest {
    public void testSumPlusMoney() {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Sum sum = new Sum(fiveBucks, tenFrances).pluse(fiveBucks);
        Money result = bank.reduce(, "USD");
        assertEquals(Money.dollar(15), result);
    }
}

class Sum {
    public Expression plus(Expression addend) {
        return new Sum(this, addend);
    }
}
```

## 목록
- ~~$5 + 10CHF = $10(환율 2:1일 경우)~~
- ~~$5 + $5 = $10~~
- $5 + $5에서 Money 반환하기  <- **이번 챕터**
- ~~Bank.reduce(Money)~~
- ~~Money에 대한 통화 변환을 수행하는 Reduce~~
- ~~Reduce(Bank, String)~~
- ~~Sum.plus~~
- Expression.times
- Money 반올림?
- hashCode()
- Equal null
- Equal object

```java
class MoneyTest {
    public void testSumPlusMoney() {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Sum sum = new Sum(fiveBucks, tenFrances).times(2);
        Money result = bank.reduce(, "USD");
        assertEquals(Money.dollar(20), result);
    }
}

class Sum {
    Expression times(int multiplier) {
        return new Sum(augend.times(multiplier), 
                addend.times(multiplier));
    }
}

interface Expression {
    Money reduce(Bank bank, String to);
    Expression plus(Expression addend);
    Expression tiems(int multiplier);
}

class Sum {
    public Expression times(int multiplier) {
        //...
    }
}

class Money {
    public Expression times(int multiplier) {
        //...
    }
}
```

## $5 + $5 Test -> 실패??
```java
class MoneyTest {
    public void testPlusSameCurrencyReturnsMoney() {
        Expression sum = Money.dollar(1).plus(Money.dollar(1));
        assertTrue(sum instanceof Money);
    }
}

// ??
class Money {
    public Expression plus(Expression addend) {
        return new Sum(this, addend);
    }
}
```

## 배운점
- 미래에 코드를 읽을 단른 사람들을 염두에 둔 테스트를 작성했다. 
- TDD와 여러분의 현재 개발 스타일을 비교해 볼 수 있는 실험 방법을 제시했다.
- 또 한 번 선업누에 대한 수정이 시스템 나머지 부분으로 번져갔고, 문제를 고치기 위해 역시 컴파일러의 조언을 따랐다.
- 잠시 실험을 시도했는데, 셎대로 되지 않아서 버렸다.