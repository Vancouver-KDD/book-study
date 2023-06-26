### Chapter 31. Refactoring

<br>


<br>
- 일반적인 의미에서의 refactoring은 코드를 개선하고 가독성을 높이며, 유지보수와 확장을 용이하게 만드는 작업임. 외부 동작은 변경하지 않으면서 내부 구조를 개선하는 것을 목표.
- TDD에서의 refactoring은 이미 통과한 테스트 케이스에 영향을 주지 않으면서 코드를 개선하는 작업을 의미 -> 상수를 변수로 바꾸는 등. 충분한 테스트 케이스를 작성하는 것이 중요


### Reconcile Differences (차이 조정)
- 비슷해 보이는 코드를 통합하는 방법?
- examine the control flows and datavalues carefully
- loops / 조건문의 branches / methods / classes 의 모든 level에서 refactoring이 가능하다. 동일하게 만들어 하나를 제거.
- backward 방향으로도 가능하다. 
  - subclass 를 완전히 비우기 -> 메서드와 field를 super class와 동일하게 만들어 subclass에 아무런 내용이 없게 만들자 -> subclass의 참조를 super class로 대체
  
### Isolate Change (변경해야 할 부분 격리)
- 여러 파트로 구성된 method나 object의 한 부분을 어떻게 변경할 것인가에 대한 내용
- 수술할 때 그 부위만 보이게 다른 부분을 덮어놓는 것처럼, 변경해야 할 부분을 격리한다.
- 만약 findRate()에서 실제로 필요한 것이 인스턴스 변수를 반환하는 것이라면, findRate()를 사용하는 모든 곳에서 이를 인라인으로 처리하고 해당 메서드를 삭제하는 것을 고려해볼 수 있습니다

  
```java
public double findRate() {
    // 일부 코드...
    return rate;
}

// calling method
double currentRate = findRate();

// inlining
double currentRate = rate;
```

- 변경을 격리하는 방법은 -> Extract Method, Extract Object, Method Object


### Migrate Data
- How
 - Add an instance variable in the new format.
 - Set the new format variable everywhere you set the old format.
 - Use the new format variable everywhere you use the old format.
 - Delete the old format.
 - Change the external interface to reflect the new format.
- ??


### Extract Method
- 메서드의 일부를 분리하여 새로운 메서드로 만들고, 새로운 메서드를 호출하는 방법
- 복잡한 코드, 중복 제거

```java
public void processOrder(Order order) {
    // 일부 로직
    validateOrder(order);
    calculateTotalAmount(order);
    applyDiscount(order);
    saveOrderToDatabase(order);
    sendConfirmationEmail(order);
    // 더 많은 로직
}

// Extract Method
public void processOrder(Order order) {
    // 일부 로직
    validateOrder(order);
    calculateOrderTotal(order);
    // 더 많은 로직
}

private void calculateOrderTotal(Order order) {
    calculateTotalAmount(order);
    applyDiscount(order);
    saveOrderToDatabase(order);
    sendConfirmationEmail(order);
}
```

### Inline Method
- Simplify control flow -> 메서드 호출을 해당 메서드 자체로 대체하는 것
  
```java
public class Calculator {
    public int multiply(int a, int b) {
        return a * b;
    }

    public int calculateTotal(int x, int y, int z) {
        int result = multiply(x, y); // multiply 메서드 호출
        return result + z;
    }
}

// inlining
public class Calculator {
    public int calculateTotal(int x, int y, int z) {
        int result = x * y; // multiply 메서드를 인라인으로 대체
        return result + z;
    }
}
```

### Extract Interface
- 코드는 추상화되고 의존성을 낮출 수 있으며, 유지보수와 확장성이 용이
- 기존의 직사각형 클래스(Rectangle)에 타원 클래스(Oval)를 추가하고자 할 때, Shape 인터페이스를 생성하여 공통된 기능을 정의
- How
1. 인터페이스를 선언.
2. 기존 클래스가 해당 인터페이스를 구현하도록 합니다.
3. 인터페이스에 필요한 메서드를 추가. 
4. 가능한 경우, 클래스에서 인터페이스로 타입 선언을 변경.

### Move Method
- 올바른 위치에 method를 이동

```java
Shape
...
int width= bounds.right() - bounds.left();
int height= bounds.bottom() - bounds.top();
int area= width * height;
...

// 사각형으로 면적 계산 이동
Rectangle
public int area() {
int width= this.right() - this.left();
int height= this.bottom() - this.top();
return width * height;
}
Shape
...
int area= bounds.area();
...
```

### Method Object
- 코드 블록에서 일부를 추출하려고 할 때마다 다섯 개나 여섯 개의 임시 변수와 매개변수를 전달해야 함
- 복잡한 several parameter와 local variable가 필요한 복잡한 메서드를 어떻게 표현? 해당 메서드를 객체로.
- ??

### Add Parameter
- 파라미터를 추가하는 것은 종종 확장 단계일 때
- 처음 테스트 케이스를 파라미터 없이 성공시키지만, 새로운 상황에서는 올바르게 계산하기 위해 더 많은 정보를 고려
  
```java
// 기존 메서드
public void printMessage(String message) {
    System.out.println(message);
}

// 파라미터를 추가한 메서드
public void printMessage(String message, int count) {
    for (int i = 0; i < count; i++) {
        System.out.println(message);
    }
}
```
- 이전에는 단순히 메시지를 출력하는 역할만 수행했지만, 추가된 파라미터 count를 활용하여 메시지를 여러 번 출력하도록 변경

### Method Parameter to Constructor Parameter


```java
// 이동 전 메서드
public void process(String data) {
    // data를 사용하여 작업을 수행
}

// 이동 후 생성자
public MyClass(String data) {
    this.data = data;
}

// 이동 후 메서드
public void process() {
    // this.data를 사용하여 작업을 수행
}
```