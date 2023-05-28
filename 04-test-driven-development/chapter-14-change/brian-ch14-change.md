# 바꾸기 
## 목록
- $5 + 10CHF = $10(환율 2:1일 경우)
- $5 + $5 = $10
- $5 + $5에서 Money 반환하기
- ~~Bank.reduce(Money)~~
- Money에 대한 통화 변환을 수행하는 Reduce <- **이번 챕터**
- Reduce(Bank, String)
- Money 반올림?
- hashCode()
- Equal null
- Equal object

```java
class MoneyTest {
    public void testReduceMoneyDifferentCurrency() {
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result = bank.reduce(Money.franc(2), "USD");
        assertEquals(Money.dollar(1), result);
    }
}

class Money implements Expression {
    public Meony reduce(String to) {
        // Money 가 환율을 안다고?!
        int rate = (currency.equals("CHF") && to.equals("USB")) ? 2 : 1;
        return new Money(amount / rate, to);
    }
}
```

##  환율을 숨겨보자
```java
class Bank {
    Money reduce(Expression source, String to) {
        return source.reduce(this, to);
    }
    
    int rate(String from, Strin to) {
        return (currency.equals("CHF") && to.equals("USB")) ? 2 : 1;
    }
}

interface Expression {
    Money reduce(Bank bank, String to);
}

class Money implements Expression {
    public Meony reduce(Bank bank, String to) {
        int rate = bank.rate(currency, to);
        return new Money(amount / rate, to);
    }
}

class Sum implements Expression {
    public Meony reduce(Bank bank, String to) {
        int amount = augend.amount + addend.amount;
        return new Money(amount, to);
    }
}
```

## 환율표를 만들어 보자!
```java
private class Pair {
    private String from;
    private String to;
    
    Pair(Sring from, Sring to) {
        this.from = from;
        this.to = to;
    }
    
    public boolean equals(Object object) {
        Pair pair = (Pair) object;
        return form.equals(pair.from) && to.equals(pair.to);
    }
    
    public int hashcode() {
        return 0; //최악의 코드
    }
}

class Bank {
    private Hashtable rates = new Hashtable();
    
    void addRate(String from, String to, rate) {
        rates.put(new Pair(from, to), new Integer(rate));
    }
    
    int rate(String from, String to {
        if(from.equals(to)) return 1;
        Integer rate = (Integer) rates.get(new Pair(from, to));
        return rate.intValue();
    }
}

class MoneyTest {
    // 같은 통화의 환율 비는 1이다.
    public void testIdentityRate() {
        assertEquals(1, new Bank().rate("USD", "USD"));
    }
}
```

## 목록
- $5 + 10CHF = $10(환율 2:1일 경우)
- ~~$5 + $5 = $10~~
- $5 + $5에서 Money 반환하기
- ~~Bank.reduce(Money)~~
- ~~Money에 대한 통화 변환을 수행하는 Reduce~~
- ~~Reduce(Bank, String)~~
- Money 반올림?
- hashCode()
- Equal null
- Equal object

# 배운점
- 필요할 거라고 생가한 인자를 빠르게 추가했다.
- 코드와 테스트 사이에 있는 데이터 중복을 끄집어냈다.
- 자바의 오퍼레이션에 대한 가정을 검사해보기 위한 테스트(testArray-Equals)를 작성했다.
- 별도의 테스트 없이 전용(private) 도우미(helper) 클래스를 만들었다.
- 리팩토링하다가 실수를 했고, 그 문제를 분리하기 위해 또 하나의 테스트를 작성하면서 계속 전진해 가기로 선택했다.