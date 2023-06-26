### Chapter 5. Franc-ly Speaking
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
- **5 CHF * 2 = 10 CHF**

<br>
  
- francs에 대한 test식을 입력해보자.
```java 
        public void testFrancMultiplication() {
            Franc five= new Franc(5);
            assertEquals(new Franc(10), five.times(2));
            assertEquals(new Franc(15), five.times(3));
        }
```

- Franc라는 object가 필요하겠군... copy/paste? 어차피 duplication제거할거니까 우선 구현해보자
```java
        class Franc {
            private int amount;

            Franc(int amount) {
            this.amount= amount;
            }

            Franc times(int multiplier) {
                return new Franc(amount * multiplier);
            }

            public boolean equals(Object object) {
                Franc franc= (Franc) object;
                return amount == franc.amount;
            }
        }
```
- duplication을 없애기 위해 equals()를 generalize하는 것이 필요!