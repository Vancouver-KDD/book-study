## Chapter 4. Privacy

```
//To-do List
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
Make "amount" private <--
//Dollar side effects? 
Money rounding? 
//equals()
hashCode() 
Equal null 
Equal object
```

Equality를 다루었으므로 Test를 Speaking로 만들 수 있다.
걔념적으로 보면, Dollar.times()가 리턴하는 Dollar를 반환할때 Dollar Object의 variable인 amount를 반환하는 것이 아니라 Dollar Object 자체를 반환하므로 이에 맞게 우리 테스트도 수정되어야 한다. 현재 Test는 아래와 같이 product.amount에 대한 비교확인 테스트이다.
```java
public void testMultiplication() { 
    Dollar five= new Dollar(5); 
    Dollar product= five.times(2); 
    assertEquals(10, product.amount); 
    product= five.times(3); 
    assertEquals(15, product.amount);
}
```
이를 Dollar 오브젝트와 Dollar 오브젝트간의 비교로 바꾸면 아래와 같고
```java
public void testMultiplication() { 
    Dollar five= new Dollar(5);
    Dollar product= five.times(2); 
    assertEquals(new Dollar(10), product); //<--
    product= five.times(3); 
    assertEquals(15, product.amount);
}
```
보다 더 보기 좋게 바꾸기 위해 두번째 Assertion도 바꾸게 되면,
```java
public void testMultiplication() { 
    Dollar five= new Dollar(5);
    Dollar product= five.times(2); 
    assertEquals(new Dollar(10), product); //<--
    product= five.times(3); 
    assertEquals(new Dollar(15), product); //<--
}
```
이제는 임시의 변수로 사용되는 product는 큰 의미가 없으므로 추가 수정을 하면,
```java
public void testMultiplication() { 
    Dollar five= new Dollar(5);
    assertEquals(new Dollar(10), five.times(2)); 
    assertEquals(new Dollar(15), five.times(3));
}
```

이러므로 해당 Test는 우리에게 Speak(말한다)라고 볼수있는데 마치 sequencial한 operation의 느낌이 아닌, assertion of truth를 말하는 것처럼 볼 수 있게 된다.


이제 Dollar는 amount라는 instance variable을 쓰는 유일한 class이므로 private화 할 수  있다.

```java
//Dollar Class
private int amount;
```

이제 To-do list에서 Make "amount" private를 완료처리 한다.

```
//To-do List
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
//Make "amount" private
//Dollar side effects? 
Money rounding? 
//equals()
hashCode() 
Equal null 
Equal object
```
주목해야할 것은 아래 코드를 참조해 보면 Equality Test 여부를 정확하게 확인하지 못하는 경우, Mutuplication Test도 정상작동하는 지 확인을 하기 어려울수 있는 형태에 이르렀다. 이또한 TDD에서 적극적으로 관리하게될 대상이고 Code와 Test에 대해서 계속적으로 언급해 나가면서 발견되는 결함들을 줄여나가도록 앞으로의 챕터들을 통해 배워나갈 것이다.
```java
public class Dollars {
    private int amount;

    Dollars(int amount) {
        this.amount= amount;
    }

    Dollars times(int multiplier) {
        return new Dollars(amount * multiplier);
    }

    public boolean equals(Object object) {
        Dollars dollar= (Dollars) object;
        return amount == dollar.amount;
    }
}

class DollarsTest {
    @Test
    public void testMultiplication() {
        Dollars five= new Dollars(5);
        assertEquals(new Dollars(10), five.times(2));
        assertEquals(new Dollars(15), five.times(3));
    }

    @Test
    public void testEquality() {
        assertTrue(new Dollars(5).equals(new Dollars(5)));
    }
}
```

#### What we`ve done

* Used functionality just developed to improve a test
* Noticed that if two tests fail at once we're sunk 
* Proceeded in spite of the risk
* Used new functionality in the object under test to reduce coupling between the tests and the code
