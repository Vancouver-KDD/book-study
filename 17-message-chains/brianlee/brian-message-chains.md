# Message Chains
* 메서드 호출이 계속 이어지는 경우
  * Ex) user.getDepartment().getManger();
* 문제 
  * 모든 체인 연결 고리를 이해하고 있어야 한다.
  * 체인 중 일부가 변경되면 코드 변경이 불가피 하다.

## Hide Delegate
* 메세지 체인의 캡슐화 => 모듈의 정보 최소화
  * Ex) user.getDepartment().getManger(); => user.getManager();
  * 이점 
    * 변경 후 이제 Department 를 알 필요가 없다.
    * getManager 의 내부 구현이 변경되어도 코드는 유지된다.

## Extract Function (#Duplicated Code)
## Move Function (#Divergent Change)