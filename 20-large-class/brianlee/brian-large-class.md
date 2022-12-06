# Large Class
* 특정 클래스가 너무 많은 일을 하게 되면
  1. 필드가 많아 진다.
  2. 중복 코드가 생긴다.

## 클래스 분리하기 위한 리팩토링
### Extract Class (#Divergent Change)
### Extract Superclass
* 두개의 클래스에서 비슷한 기능이 보이면 Inheritance 적용하여
  * Pull Up Field, Pull Up Method 를 실행
* Inheritance 가 적절하지 않다면 Replace Superclass with Delegate 적용하여 위임으로 변경
### Replace Type Code with Subclasses (#Primitive Obsession)

## 중복 제거
### Extract Function (#Duplicated Code)