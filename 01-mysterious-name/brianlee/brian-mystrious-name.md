# Mysterious Name

## Change Function Declaration (Function name + Parameters)
* Function name
  * 좋은 메서드 명은 이름만으로 기능을 알수있다. -> 메서드안의 코드를 다 볼 필요가 없다. 
  * 주석을 달아보면 작명하기 쉬워진다.
* Parameters
  * 파라미터를 어떤것을 받냐에 따라 메서드의 기능이 달라질 수 있다.
    * Ex) int[] getTelephoneNumber(Person person); // 개인 전화번호를 받는다. 이 메서드를 통해 회사 번호를 받을 수 없다.
  * 파라미터에 대한 의존성이 생기게 된다.
    * Ex) boolean isExpired(Payment payment) ; // Payment 객체가 있어야만 한다.
## Rename Filed / Variable
* 많이 쓰이고 널리 쓰이는 변수가 더 중요하다
  * static fields > class fields > method variables > variables used in lambda expressions
  * public > protected > package private > private
* java, C#의 Record 혹은 파이썬의 Directories 등 필드 이름이 바로 쓰이기 때문에 중요하다. (static fields와 같이)