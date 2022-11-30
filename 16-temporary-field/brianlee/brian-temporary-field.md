# Temporary Field
* 클래스 필드가 특정 경우만 값을 가지는 경우 -> 이해하기 어렵다

## Extract Class (#Divergent Change)
## Move Function (#Divergent Change)
## Introduce Special Case
* 특정 조건에서 동작하는 케이스가 존재한다면 이를 따로 Class로 빼서 구현 -> 특이 케이스 패턴 
  * Ex) Null Object 패턴 -> unknown 이나 null 값인 경우 대체할 값들을 가지고 있는 class를 만들어 사용