# Shotgun Surgery
* 어떤 한 변경 사항이 생겼을 때 여러 모듈을 (여러 함수 또는 여러 클래스를) 수정해야 하는 상황.
  * Divergent Change 와 유사하지만 반대의 상황이다. 
    * Divergent Change는 쪼개고 Shotgun Surgery는 모은다. 
    * Ex) 새로운 결제 방식을 도입하려면 여러 클래스의 코드를 수정해야 한다.

## Move Function (#Divergent Change)
## Move Field
* 좋은 데이터 구조는 메소드나 필드를 옮기기 간편하고 단순하다.
* 처음에는 타당해 보였던 프로젝트 과정에서 이해도가 높아지면서, 틀린 의사 결정으로 바뀌는 경우도 많다.
*  필드를 옮기는 단서:
   * 어떤 데이터를 항상 어떤 레코드와 함께 전달하는 경우.
   * 어떤 레코드를 변경할 때 다른 레코드에 있는 필드를 변경해야 하는 경우.
   * 여러 레코드에 동일한 필드를 수정해야 하는 경우
     * (여기서 언급한 ‘레코드’는 클래스 또는 객체로 대체할 수도 있음)

## Combine Functions into Class (#Long Parameter List)
## Split Phase (#Divergent Change)
## Inline Function
* Inline Function VS Extract Function
* 함수 리팩토링이 잘못된 경우에 여러 함수를 인라인하여 커다란 함수를 만든 다음에 다시 함수 추출하기를 시도할 수 있다.

## Inline Class
* Inline Class VS Extract Class
* 리팩토링을 하는 중에 클래스의 책임을 옮기다보면 클래스의 존재 이유가 빈약해 지는 경우가 발생할 수 있다.
* 클래스를 정리하기 위해 Inline Class -> Extract Class 순으로 적용할 수도 있다. 