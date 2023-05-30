### Chapter 9. Times We’re Livin’ In
<br>

### Test List
- $5 + 10 CHF = $10 if rate is 2:1
- ~~$5 * 2 = $10~~
- ~~Make “amount” private~~
- ~~Dollar side effects?~~
- Money rounding?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
- ~~5 CHF * 2 = 10 CHF~~
- ~~Dollar/Franc duplication~~
- ~~Common equals~~
- Common times
- ~~Compare Francs with Dollars~~
- **Currency?**
- Delete testFrancMultiplication?

<br>

---  
- 
- 

```java
public void testCurrency() {
    assertEquals("USD", Money.dollar(1).currency());
    assertEquals("CHF", Money.franc(1).currency());
}
```
  1. currency declare
       - dollar object에 currency를 declare해주고, currency를 "USD"로 할당해야 한다. 
       - franc object도 같은 방식으로 처리
  
  2. currency()라는 함수를 선언해야 한다. 이 함수는 currency를 return하는 함수여야 한다.
       - subclass에서 currency()를 declare하기 위해 Money class에서 declare를 먼저 해야 한다.
  
            ```java
            Money
            abstract String currency();
            ```
        - 그리고 franc과 dollar에 currency()를 추가해준다.
            ```java
            Franc
            String currency() {
                return "CHF";
            }
            Dollar
            String currency() {
                return "USD";
            }
            ```
        - string 값을 바로 return하는 것은 보기 좋지 않으니, instance variable을 이용하여 currency를 store하고 variable을 return하도록 한다. (여기서 instance variable이란 객체의 속성이나 상태를 나타내는 변수로서, 특정 상태를 저장하고 유지하는데 사용된다)
        ```java
            Franc
            private String currency;
            Franc(int amount) {
                this.amount = amount;
                currency = "CHF";
            }
            String currency() {
                return currency;
            }
            Dollar
            private String currency;
            Dollar(int amount) {
                this.amount = amount;
                currency = "USD";
            }
            String currency() {
                return currency;
            }
        ```
        - Franc와 Dollar가 identical(완전히 똑같은)하니, 똑같은 부분을 Money로 push up 해도 될 것이다.
        ```java
            Money
            protected String currency;
            String currency() {
                return currency;
            }  
        ```
        - Franc과 Dollar의 currency는 서로 다르므로 (USD 코드 여기서는 생략)
        ```java
            Franc
            Franc(int amount, String currency) {
                this.amount = amount;
                this.currency = "CHF";
            }  
        ```
        - object의 variable이 하나가 더 추가되었으므로 영향을 받는 곳을 수정해야 한다. constructor에 정의가 각자 되어 있으므로 null값을 추가한다.
        ```java
           Franc
            Money times(int multiplier) {
                return new Franc(amount * multiplier, null);
            } 
        ```
        - times()가 new Franc 이렇게 constructor를 호출하는 것을 발견! factory method를 쓰도록 변경시키자
        ```java
           Franc
            Money times(int multiplier) {
                return Money.franc(amount * multiplier);
            } 
        ```
        - 그리고 null을 pass하고 constructor에 currency를 정의하는 대신, "CHF"를 parameter로 pass하도록 하자. 받는 쪽에도 String currency를 parameter에 추가하는 것 don't forget!
        ```java
            Money
            static Money franc(int amount) {
                return new Franc(amount, "CHF");
            }
            Franc
            Franc(int amount, String currency) {
                this.amount = amount;
                this.currency = currency;
            }
        ```
        - constructors가 identical하니 push up할 수 있음
        ```java
            Money
            Money(int amount, String currency) {
                this.amount = amount;
                this.currency = currency;
            }
            Franc
            Franc(int amount, String currency) {
                super(amount, currency);
            }
            Dollar
            Dollar(int amount, String currency) {
                super(amount, currency);
            }
        ```

___
Next
- times()를 push up 할 것임