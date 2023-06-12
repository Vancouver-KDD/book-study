# xUnit 회고

## xUnit 을 직접 구현해볼 만한 두 가지 이유
1. 숙달: xUnit의 정신은 간결함에 있다. 
   - 그러나 몇몇 구현은 내 식견으로 조금 복잡해 보인다.
   - 직접만들어 사용하면 숙달된 도구를 쓴느 느낌을 받게 될 것이다.
2. 탐험: 새로운 프로그래밍 언어를 처음 접하면 그 언어로 xUnit을 만들어 본다.
   - 8-9개 테스트가 넘어가면 필요한 기능을 경험해보게 된다.

## Assertion fail VS Other errors
- Assertion 실패가 더 많은 디버깅 시간이 걸림 

## Interface 한정
```java
public interface Test {
    public abstract int countTestCases();
    public abstract void run(TestResult result);
}
```

Languages with optimistic (dynamic) typing don't even have to declare their allegiance to an interface.