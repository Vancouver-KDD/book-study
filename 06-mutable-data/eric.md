변경가능한 변수 10개를 쓰는것보다 변경가능한 변수 1개만쓰고 읽을수만 있는 변수 9개를 쓰는것이 code complexity 를 줄여준다.

그 이유는 number of possible state 이 줄어들기 때문이다.

mutable data 의 반대가 immutable data 인데, 실제로 모든 프로그램은 immutable data 만 가지고 짤수 있고, 그게 바로 functional programming 이다.

하지만 functional programming 의 단점은 코드양이 많아지고 속도가 느리다는 점에 있다.

​

그래서 instead of taking functional programming to the extreme, mutable data 를 허용하되 최대한 encapsulation 을 사용하여 side effect 을 최소한하여 코드양도 줄이고, code complexity 로 줄이는 방법이 제일 좋은것 같다.

물론 변수가 변경이 필요없다면 읽기전용으로 만들어야 하겠다.

​

예: always use `const` if possible, and `let` otherwise

JS 에서는 reassign 을 막는 const 가 존재한다. immutable 은 아니지만 그래도 side effect 를 줄여준다.

let 은 var 과 다르게 block scope 을 할수 있다. 그래서 그 변수가 어떻게 변경 및 사용되는지 쉽게 알수 있다.

​

immutability 의 또다른 장점은 cache 에 있다. 어떤 array 나 object 에 주소값이 변경되지 않았다면 그 속을 들여다보지 않아도 똑같은 값이구나 예측할수있다면 속도를 많이 향상시킬수있다 (예: UI re-renders)
