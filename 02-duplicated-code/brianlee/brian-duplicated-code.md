# Duplicated Code
* 중복 코드란?
  * 완전히 동일한 코드
  * 조금 수정하면 같은 코드
  * 구조가 비슷한 코드
    * 코드는 다르나 내부 절차가 비슷한 경우
* 중복 코드의 단점
  * 코드를 볼때 완전히 같은것인지 아니면 조금 다은것인지 주의깊게 봐야한다.
  * 코드를 변경할때, 같은 모든 곳을 찾아서 같이 수정해야한다.
## Extract Function
* 긴 메서드안에서 특정 기능을 하는 라인들을 메서드로 빼낸다. 이때 메서드명이 이 기능을 다 설명해야한다.
* 한줄 메서드 도 괜찮은가? Sure!
* 주석이 필요할 정도로 긴 메서드는 Extract Faction의 중요한 단서.
* Intention VS Implementation
  * Implementation: 구현을 모두 봐야하는 경우
```java
public class Example {
    public static void main(String[] args) {
        int sum = 0;
        for(int i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println("Sum from 1 to 100: " + sum);
    }
}
```
  * Intention: 구현부와 상관없이 의도를 알수 있는 경우(Extract Function을 통해 메서드를 추출하고 메서드 명으로 메서드 구현부를 파악 가능)
```java
public class Example {
    public static void main(String[] args) {
        int sum = getRangeSum(1,100);
        System.out.println("Sum from 1 to 100: " + sum);
    }
    
    public static int getRangeSum(int from, int to) {
        if(from > to) throw IllegalArgumentException("'from' cannot be greater than 'to'.");
        int sum = 0;
        for(int i = start; i <= end; i++) {
            sum += i;
        }
        return sum;
    }
}
```
## Slide Statements(코드 정리 하기)
* 변수는 상단에 미리 정의 하지 않는다.
* 관련 있는 코드끼리 가까이 있어야 이해가 쉽다.
  * 보통 IDE에서는 라인 옯기기를 단축기로 지원한다.
* 이렇게 관련 코드끼리 모여있느면 Extract Function하기 쉽다.

## Pull Up Method(하위 클래스의 중복 기능을 상위 클래스로 올린다.)
* 하위 클래스의 기능 중복은 결국 ㅁ누제를 만들수 있다.
  * Ex) A 하위 클래스는 변경하고 B 하위 클래스는 변경하지 않는 경우
* 다른 리팩토링 기술과 결합되어 사용된다.
  * 다음 2가지 리팩토링을 사용하고 Pull Up Method를 사용
    * Parameterize Function: 메서드 추출 시 Parameter를 사용하여 위연성을 높인다.
    * Pull up Field: 하위 클래스끼리 의존성이 있는 Field가 있다면 상위클래스로 올린다.
* 비슷한 절차의 코드가 있다면 Template Method Pattern 적용