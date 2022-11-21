# Feature Envy
* 한 모듈에서 다른 모듈의 데이터나 함수를 더 많이 참조하는 경우 -> 데이터나 함수를 옮겨라
  * Ex) 다른 객체의 getter를 여러개 사용하는 경우

## Move Function (#Divergent Change)
## Extract Function (#Duplicated Code)
* Extract Function -> Move Function 

## 예외 
### Strategy
* 알고리즘을 캡슐화 하여 런타임에 변경해 주면서 사용 가능
* 클라이언트 코드가 구체적인 전략을 알아야 한다
### Visitor
* 기존 코드를 변경하지 않고 새로운 기능을 추가하는 패턴
* n * m 개의 조합으로 구성할 수 있다.
  * Ex) n개의 각기 다른 디바이스(phone, tablet, watch 등)에 공통으로 들어가는 m개의 기능(네모, 세모, 동그라미 그리기 등)을 구현 