

## Chapter 3. Equality for All

현재 적용되고 있는 Value Objects인 Dollar에 관련하여서, 저자는 하나의 제약을 말하는데  Values of the instance variables of object(개체의 인스턴스 변수값)이 Constructor를 통해 설정되면 변경되지 않고, 새로운 값에 대한 니즈가 있다면 값을 변경하는 것이 아니고 새로운 Object를 만든다라고 설명한다.  

그와 동시에 그렇게 새로운 Object를 추가적으로 생성하여 사용하므로써, 앨리어싱(aliasing)에 대한 걱정이 없을 것이라고 설명하는데, Aliasing에 대한 설명을 찾아보면, 어떤 포인터의 복사본을 여러 개체 또는 코드에서 갖고 있어 영향을 받는 것을 Aliasing이라고 한다.
그런데, Value Object를 사용하게 되면, 필요에 따라 새롭게 개체를 생성하여 사용하므로 앨리어싱(aliasing)을 통해 연결된 개체의 변경에 따른 에러버그를 걱정할 필요가 없다는 것이다.


결국 저자가 말하고자하는 것은 그렇게 필요에 의해서 새로운 개체를 생성해 나아가는 과정에 우리가 체크를 해야할 것 Test화 해야할 것을 다음과 같이 도출하기 위함이다.

만약, $5 Value object가 존재하는 상태에서 또 다른 $5 Value Object가 필요에 의해서 생성이 되었다면 두개는 같음과 다름 없으므로 이를 체크하는 eqauls() 메소드에 대한 필요성을 말하고 이에 대해 해당 챕터에서 다룰 것을 말한다.


```
//To-do List
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
Make "amount" private 
//Dollar side effects? 
Money rounding?
equals() <--
hashCode()
```
또한 Dollar 오브젝트를 Hash Table의 키로 사용하면서 Equals()을 구현하는 할 경우에는 HashCode() 메소드를 구현이 필요하므로 To-do 리스트에 추가하고 이후에 다룬다.


그래서 저자는 앞서 얘기한봐와 같이 Equals()에 대해서 구현을 생각하기 보단 먼저 Test에 대한 부분을 먼저 생각하라고 얘기하면서 아래와 같이 작성한다.
```
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5))); 
}
```
그리고 일단은 pass를 시키기 위해 아래와 같이 무조건 true인 equals() 메소드를 구현한다.
```
public boolean equals(Object object) { 
    return true;
}
```
이 시점에서 Triangulation라는 가장 보수적인 Test 구현 방식을 소개하는데
위와 같이 5==5, "amount == 5", "amount == dollar.amount" 라는 명확한 True에다가
두번째 Test구문으로 $5 != $6 를 아래와 같이 추가적인 예시를 더하고
```java
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5))); 
    assertFalse(new Dollar(5).equals(new Dollar(6)));
}
```
두개 이상의 Test 예에서 실질적인 대상 Method인 Equals()를 generalize하는 순서로 Triangulation 방식을 설명하면서 아래의 일반화의 equals() method를 도출해낸다.
```java
public boolean equals(Object object) { 
    Dollar dollar= (Dollar) object; 
    return amount == dollar.amount;
    }
```

이로써 Equals에 대한 것은 아래와 같이 완료되었다.
```
//To-do List
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
Make "amount" private 
//Dollar side effects? 
Money rounding? 
//equals()
hashCode(
```
Triangulation 방법은 Refactoring 방법에 확신하지 못할때 사용한다.
만약 Code와Test간에 중복을 제하고 바로 solution을 만들 수 있다면 그냥 진행하면 되지만, 디자인에 대한 아이디어가 떠오르지 않을때 Triangulation 방법을 사용하면 Another approach를 통해 생각할 수 있는 기회를 제공한다. 그러므로 가변점을 다양하게 가져가면 solution을 명확하게 할 수 있다고 저자는 Triangulation 방법에 대해 설명하고 제안한다.

그리고 추가적으로 null과 비교 그리고 other object와의 비교에 대한 테스트도 필요할 수 있으므로 목록에 추가한다. 
```
//To-do List
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
Make "amount" private 
//Dollar side effects? 
Money rounding? 
//equals()
hashCode() 
Equal null 
Equal object
```

#### What we`ve done

* Noticed that our design pattern (Value Object) implied an operation 
* Tested for that operation
* Implemented it simply
* Didn't refactor immediately, but instead tested further 
* Refactored to capture the two cases at once

