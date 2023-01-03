# Speculative Generality
* 미래 기능까지 예상하여 만드는 경우 결국 쓰이지 않는 경우 발생 -> 이를 제거해야 한다.
* XP의 YAGNI (You aren't gonna need it) 원칙 -> 상상해서 말들지 말고 필요한것만 만들자.

## Collapse Hierarchy (#Lazy Element)
## Inline Function (#Shotgun Surgery)
## Inline Class (#Shotgun Surgery)
## Change Function Declaration (#Mysterious Name)
## Remove Dead Code
* 사용하지 않는 코드 -> 성능 문제 X But 개발자의 이해가 어렵다.
* 실제로 나중에 필요한 코드라도 지금 사용하지 않는다면 삭제 필요
  * 필요한 경우 git과 같은 버전 관리 툴로 복원