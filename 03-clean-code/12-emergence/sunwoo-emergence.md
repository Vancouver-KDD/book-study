# Emergence
## Getting Clean via Emergent Design
다음과 같은 룰을 코드에 적용 함으로서, 우리는 잘 디자인 된 소프트웨어를 작성할 수 있다.

* Runs all the tests
* Contains no duplication
* Expresses the intent of the programmer
* Minimizes the number of classes and methods
 
## Simple Design Rule 1: Runs All the Tests
가장 중요한 점은 의도되로 움직이는 시스템을 만들어야 한다는 것이다.
테스트는 코드가 우리가 설계한대로 움직인다는 것을 확인시켜주고, 
그렇기 때문에 코드는 testable하게 작성되야 한다.
테스트를 작성함으로서 우리는 코드가 잘 디자인되어 있는지 확인을 할 수 있다.

## Simple Design Rules 2-4: Refactoring
우리가 테스트 코드를 가짐으로서, 코드의 변경점이 테스트코드로 확인할 수 있기 때문에, 리팩토링 또한 쉬워진다.

## No Duplication
반복되는 코드는 좋은 설계의 디자인에 가장 큰 적입니다. 
코드가 반복된다는 것은, 불필요한 일, 리스크 그리고 복잡성을 시스템이 내포하고 있다는 것을 의미한다.

코드가 완벽히 같은 것 뿐아니라, implementation이 반복되는 경우도 있을수 있다.
예를 들어, 밑의 메소드들은 각각의 implementation을 가질수 있지만,
각각의 메소드의 정의를 연결하여 구현할수 있다.
```Java
int size();
boolean isEmpty();

boolean isEmpty() {
    return 0 == size();
}
```

```Java
public class VacationPolicy {
 public void accrueUSDivisionVacation() {
    // code to calculate vacation based on hours worked to date
    // ...
    // code to ensure vacation meets US minimums
    // ...
    // code to apply vaction to payroll record
    // ...
 }
 public void accrueEUDivisionVacation() {
    // code to calculate vacation based on hours worked to date
    // ...
    // code to ensure vacation meets EU minimums
    // ...
    // code to apply vaction to payroll record
    // ...
 }
}
 ```

```Java
abstract public class VacationPolicy {
    public void accrueVacation() {
        calculateBaseVacationHours();
        alterForLegalMinimums();
        applyToPayroll();
    }
}

public class USVacationPolicy extends VacationPolicy {
    @Override protected void alterForLegalMinimums() {
        // US specific logic
    }
}
public class EUVacationPolicy extends VacationPolicy {
    @Override protected void alterForLegalMinimums() {
        // EU specific logic
    }
}
```
높은 레벨의 복제를 제거하는 일반적인 기술중 하나는 template method 패턴이다. `VacationPolicy`에 중복되는 메소드는 higher level 클래스에서 implementation하고, 
세부 사항은 확장된 클래스에서 정의 할 수 있다.

## Expressive
소프트웨어 프로젝트의 대부분의 비용은 장기간 관리하는 곳에서 나온다. 그러므로 그것을 다른사람이 이해하도록 쉽도록 작성하는 것은 비용을 많이 줄일수 있다.
다음과 같은 방법으로 자신이 작성한 코드를 더 expressive하게 만들수 있다.
* expressive한 이름을 선택
* function과 class를 작은 크기로 유지
* standard한 용어를 사용
* 잘 작성된 유닛 테스트
  
## Minimal Classes and Methods
위의 규칙들을 너무 따르려하다 보면, 반대로 너무 지나친 갯수의 class나 methods를 작성해버리는 경우도 있다. 그러므로 class와 function의 숫자도 적게 유지 하려는 것을 노력해야 한다.
이것은 위의 규칙보다 우선순위가 높지 않지만, 룰에 너무 집착하는 것보다는 조금더 실용적인 것을 선택해야 한다.

## Conclusion
이 위의 룰들이 경험을 대체 할 수 없지만, 이러한 룰을 따름으로써 오랜 경험이 필요한 학습을 조금더 효과적으로 습득할 수 있다.
