# Repeated Switches
* 예전에는 Switch 문이 하나만 있어도 다형성을 권장했다.
* 하지만 현재 다형성을 많이 사용하고 있으며 여러 언어에서 세련된 switch 문들을 제공하고 있다.
* 그래서 현재는 "반복되는" Switch 문들을 냄새로 보고 있다.
* Repeated Switches 의 문제는 
  * 조건을 수정하거나 추가할때 모든 관련된 Switch 문들을 모두 수정해야만 할 수 있다.
  * 이때 상속에서와 같은 꼭 Override 해야하는 규약이 없다면 실수로 빼먹을 수 있다.

## Replace Conditional with Polymorphism (#Primitive Obsession)

## 참조
### Record
* Java 14 버젼에서 preview 로 등장하여 16에서 정식 스펙으로 포함
* https://scshim.tistory.com/372
### Switch Expression
* Java 12 버젼에서 preview 로 등장하여 14에서 정식 스펙으로 포함
* https://velog.io/@nunddu/Java-Switch-Expression-in-Java-14