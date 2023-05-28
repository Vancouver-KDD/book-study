# Chapter 9. Times We're Livin' In

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
- ~~Common equals~~
- Common times
- ~~Compare Francs with Dollars~~
- **Currency?**
- Delete testFrancMultiplication?

Times()의 중복을 제거하는 과정중 -----
필요없는 subclasses를 제거하기 위해서 `currency` 를 도입하면 어떨까?

1. 먼저 `currency` 테스트 코드를 작성

```
public void testCurrency() {
    assertEquals("USD", Money.dollar(1).currency());
    assertEquals("CHF", Money.franc(1).currency());
}
```

2. `Money` 에서 `currency()`를 정의해주고

```
abstract String currency();
```

3. Franc 과 Dollar subclasses 에 실행시켜줌

```
//Franc
String currency() {
     return "CHF";
}
```

```
//Dollar
String currency() {
     return "USD";
}
```

3. `currency`를 `instance variable` 로 저장하고 변수를 return 하는 방식으로 두 클래스를 모두 만족시키게 할 수 있음

```
// Franc
private String currency;
Franc(int amount) {
    this.amount = amount;
    currency = "CHF";
}
String currency() {
    return currency;
}
```

```
// Dollar
private String currency;
Dollar(int amount) {
    this.amount = amount;
    currency = "USD";
}
String currency() {
    return currency;
}
```

4. 이제 변수 정의 부분과 currency() 실행 부분은 완전 똑같아지므로 Money로 이동시킬 수 있음

```
// Money
protected String currency;
String currency() {
    return currency;
}
```

5.  "USD" 와 "CHF" constant strings 을 static factory methods 로 옮긴다면, 두 constructor는 완전 똑같아 질 수 있음

    1. contructor 에 Parameter 더하기

    ```
    Franc(int amount, String currency) {
         this.amount = amount;
         this.currency = "CHF";
     }
    ```

    2. 위에서 파라미터를 추가하게 되면 이미 존재하는 contructor를 break함

    ```
    // Money
    static Money franc(int amount) {
        return new Franc(amount, null);
    }
    ```

    ```
    // Franc
    Money times(int multiplier) {
        return new Franc(amount * multiplier, null);
    }
    ```

    3. 먼저 넘어가기 전에, `Franc.times()` 가 factory method 대신 contructor를 호출하고 있음 -> 수정

    ```
    // Franc
    Money times(int multiplier) {
        return Money.franc(amount * multiplier);
    }
    ```

    3. "CHF"를 파라미터로 넘겨줌

    ```
    // Money
    static Money franc(int amount) {
       return new Franc(amount, "CHF");
    }
    ```

    4. 그리고 Franc 에서 parameter를 instacne variable로 할당해줌

    ```
    Franc(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    ```

    5. 같은 방식으로 Dollar도 변경함

    ```
    // Money
    static Money dollar(int amount) {
        return new Dollar(amount, "USD")
    }
    ```

    ```
    // Dollar
    Dollar(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    Money times(int multiplier) {
        return Money.dollar(amount \* multiplier);
    }

    ```

    6. 이제 두 constructors 가 똑같아짐 -> 같은 부분은 `Money` 클래스로 옮길 수 있음

    ```
    // Money
    Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    // Franc
    Franc(int amount, String currency) {
        super(amount, currency);
    }

    // Dollar
    Dollar(int amount, String currency) {
        super(amount, currency);
    }
    ```
