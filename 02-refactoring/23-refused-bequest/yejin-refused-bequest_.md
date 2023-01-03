# Refused bequest
## Quick preview 
Superclass 의 메소드 중 일부만 상속받고 싶을때 발생한다.  
이럴때는 정말 공통된 부분만 superclass 에 남을 수 있도록 그 이외의 부분은 다시 subclass 로 추출하는 방법이 예전에는 많이 쓰였다. 
지금은 아예 상속 구조를 사용하지 않고 delegation 역할로 넘기는 방식을 사용해 해결한다.  

[참고 예시](https://gist.github.com/mihir787/d5464fd97977aa7252e39ef03e9f9d27)


