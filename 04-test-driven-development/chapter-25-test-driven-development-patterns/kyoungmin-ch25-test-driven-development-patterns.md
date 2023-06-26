### Chapter 25. Test-Driven Development Patterns
<br>


<br>

---
- What do we mean by testing? 테스트란
- When do we test? 언제
- How do we choose what logic to test? 어떤 논리
- How do we choose what data to test? 어떤 데이터

<br>

- Test
  - 평가하는 과정. 수용 또는 거부로 이어지는 절차. 
  - 스트레스가 크면 더 적은 테스트 -> 더 많은 오류 -> 더 많은 스트레스 의 무한 루프
  - 자동화 된 테스트가 중요함
- Isolated Test
  - 첫째, 테스트를 내가 직접 실행할 수 있을 정도로 빠르게 실행하고, 자주 실행해야 함 
  - 하나의 테스트가 일찍 깨져 다음 테스트에 예측할 수 없는 상태를 남기는 경우
- Test List
  - 시작하기 전에 작성해야 할 모든 테스트 목록을 작성해야 함
  - 이미 존재하지 않는 작업에 대한 null 버전을 목록에 추가
  - 리팩터링을 나열
- Test First
  - 테스트할 코드를 작성하기 전 테스트를 작성
- Assert First
  - 생각을 명확하게 하고 문제 해결을 개선
  

``` java
TestCompleteTransaction() {
    ...
    assertTrue(reader.isClosed());
    assertEquals("abc", reply.contents());
}

testCompleteTransaction() {
...
    Buffer reply= reader.contents();
    assertTrue(reader.isClosed());
    assertEquals("abc", reply.contents());
}

testCompleteTransaction() {
...
    Socket reader= Socket("localhost", defaultPort());
    Buffer reply= reader.contents();
    assertTrue(reader.isClosed());
    assertEquals("abc", reply.contents());
}

testCompleteTransaction() {
    Server writer= Server(defaultPort(), "abc");
    Socket reader= Socket("localhost", defaultPort());
    Buffer reply= reader.contents();
    assertTrue(reader.isClosed());
    assertEquals("abc", reply.contents());
}
```