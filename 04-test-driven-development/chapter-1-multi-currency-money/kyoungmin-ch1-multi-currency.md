# Test Driven Development

## Part I. The Money Example

### The basic rule and the process of making a unit test code in this book includes:

1. Quickly add a test.
2. Run all tests and see the new one fail.
3. Make a little change.
4. Run all tests and see them all succeed.
5. Refactor to remove duplication.


___
### Chapter 1. Multi-Currency Money
<br>

### Test List
- $5 + 10 CHF = $10 if rate is 2:1
- $5 * 2 = $10
- Make “amount” private
- Dollar side effects?
- Money rounding?

<br>
What set of tests will demonstrate to write a report?
- 두 개의 currencies에 각각 amount를 더하고, 원하는 currency로 그 결과를 convert.
- Multiply the amount (price per share) by a number -> then receive amount.


이 chapter에서는 $5 * 2 = $10 부터 실행
아래와 같은 test code를 입력해보고, 필요한 부분들을 추가한다.


    public void testMultiplication() {
    Dollar five= new Dollar(5);
    five.times(2);
    assertEquals(10, five.amount);
    }

추가해야 할 항목:
- No class Dollar
- No constructor
- No method times(int)
- No field amount

### Dependency and Dupication
- code와 test dependency
  -> 코드를 바꾸지 않고 another test를 만드는 것. DB Vendor를 바꿀 때, code가 DB에 depedency가 있다는 것을 discover. code를 바꾸지 않으면 DB를 바꿀 수 없다. 
- Duplication은 symptom이다. duplication이 있다는 것은 duplicate logic이고, Object를 사용하면 방지할 수 있다.
- 프로그램에서 duplication을 제거하는 것은 의존성을 제거하는 것이다. 다음 test 전에 제거하면, 한번만 test를 진행해도 됨
   
___
### Chapter 2. Degenerate Objects
<br>

### Test List
- $5 + 10 CHF = $10 if rate is 2:1
- ~~$5 * 2 = $10~~
- Make “amount” private
- Dollar side effects?
- Money rounding?
  
<br>

- Dollar Side Effects란?<br>
Dollar에 대한 operation을 진행할 때 the Dollar(object)가 변하는 것<br>
=> times()에서 new object를 반환하면 항상 동일한 값이 유지될 것이다<br>
=> Dollar의 interface를 변경하기 때문에 test도 변경해야 한다. 
   

        Dollar product = five.times(2)

- Fake it<br>
  상수를 return하는 간단한 test. gradually change it to real code

- Use obvious Implementation<br>
  구현 방법이 obvious하면 바로 real code 작성

- Triangulation<br>
  1. 디자인 상의 문제점을 테스트 case로 번역
  2. stub implementation으로 코드를 빠르게 compile
  3. 코드 작성
   
  %% stub implementation이란? <br>
  테스트 용도로 사용되는 가짜 객체. 미리 정의된 결과를 return. DB에 접근하는 Method 테스트 시에 실제 접근하지 않고 Method만 test할 수 있도록

___
### Chapter 3. Equality for All
<br>

### Test List
- $5 + 10 CHF = $10 if rate is 2:1
- ~~$5 * 2 = $10~~
- Make “amount” private
- ~~Dollar side effects?~~
- Money rounding?
- equals()
  
<br>
  
Value Object를 사용하면 Aliasing 없음
- Aliasing: 변수가 같은 메모리 위치를 참조하는 것. 한 변수를 수정하면 다른 변수도 변경됨.
- Value Object: 모든 연산이 new object를 return.
  

        Dollar
        public boolean equals(Object object) {
        Dollar dollar= (Dollar) object;
        return amount == dollar.amount;
        }

___
### Chapter 4. Privacy
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
  
<br>

Dollar.times()는 multiplier를 곱한 Dollar Object 를 return해야 하지만 아직 명확하지 않다. 

        public void testMultiplication() {
        Dollar five= new Dollar(5);
        Dollar product= five.times(2);
        assertEquals(new Dollar(10), product);
        product= five.times(3);
        assertEquals(new Dollar(15), product);
        }

product는 별로 도움이 안되므로 다시 써보면,
        
        public void testMultiplication() {
        Dollar five= new Dollar(5);
        assertEquals(new Dollar(10), five.times(2));
        assertEquals(new Dollar(15), five.times(3));
        }

Dollar class에서만 amount를 쓰기 때문에,

        private int amount;