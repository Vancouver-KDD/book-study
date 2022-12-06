# Middle Man <-> Message Chain
* Message Chain의 반대 경우
* 메서드 호출을 다른 클래스에 위임하고 있다면 중재자를 제거하거 바로 사용 하도록 변경

## Remove Middle Man
* Hide Delegate 의 반대 리팩토링
* Ex) user.getManager(); => user.getDepartment().getManger();
* 변경 방법
  1. Middle Man의 getter 제공 Ex) getDepartment() 제공
  2. 클라이언트 코드 변경 Ex) user.getManager(); => user.getDepartment().getManger();
  3. 캡슐화에 사용된 메소드 제거 Ex) user.getManager(); 제거

## Inline Function (#Shotgun Surgery)

## Replace Superclass with Delegate
* Inheritance -> Delegation
* Inheritance 의 문제
  * Liskov Substitution Principle 을 따라야 한다.
  * 서브 클래스는 슈퍼 클래스의 변화에 취약
    * 그렇다면 Inheritance 는 나쁜것 인가?
      * 적절한 경우 Inheritance 는 효율적이다
      * Inheritance 적용 후 적절하지 않다면 Delegation 으로 변경 한다. 

## Replace Subclass with Delegate
* Inheritance 은 한번만 사용할 수 있다.
  * 경우에 따라 두가지 이상의 Group 에 해당 된다면 Delegation 으로 이를 해결 할 수 있다.