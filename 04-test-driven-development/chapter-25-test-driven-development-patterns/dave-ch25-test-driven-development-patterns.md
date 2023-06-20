### Part III: Patterns for Test-Driven Development
# Chapter 25. Test-Driven Development Patterns


<Some basic strategic questions>


* What do we mean by testing? 
* When do we test?
* How do we choose what logic to test? 
* How do we choose what data to test?

## Test (noun)

How do you test your software? Write an automated test.

품질 소프트웨어 다어그램을 보면, 스트레스와 테스트 실행 사이의 루프를 볼 수 있습니다.
노드 사이의 화살표는 첫번째 노드의 증가가 두번째 노드의 증가를 의미한다.
원이 있는 화살표는 첫번째 노드의 증가가 두번째 노드의 감소를 의미한다.


스트레스 수치가 높아지면 긍정적인 피드백 루프다.
스트레스를 많이 받을 수 록 테스트를 덜 하게 된다. 테스트를 적게 할 수록 더 많은 오류를 범하며,  오류가 많을 수록 스트레스를 더 많이 받게 된다. 
이 고리에서 빠져 나오려면, 새 요소를 도입 즉, 자동화된 테스트 입니다.

스트레스를 더 많이 받을수록 테스트를 더 많이 실행합니다.



## Isolated Test

First, make the tests so fast to run that I can run hem myself, and run them often.
Second, the main lesson I took was that tests should be able to ignore one another completely.
One convenient implication of isolated tests is that the tests are order independent.
Isolating tests encourages you to compose solutions out of many highly cohesive, loosely 
coupled objects. 

## Test List

시작하기 전에 작성해야 할 모든 테스트 목록을 작성하십시오.
프로그래밍 스트레스를 다루는 접근 방식
의 첫 번째 부분은 우리 발이 어디에 착지할지 알지 못하는 한 한 발짝도 앞으로 나아가지 않는 것입니다. 해야 할 일이 많을수록 내가 하고 있는 일에 대한 관심이 줄어들었습니다. 내가 하고 있는 일에 대한 관심이 적을수록 성취도도 줄 었습니다. 내가 성취한 것이 적을수록 해야 할 일을 더 많이 알게 되었습니다.


테스트 주도 개발에 적용할 때 우리가 목록에 올린 것은 우리가 구현하고자 하는 테스트입니다. 먼저 구현해야 하는 모든 작업의 예를 나열하십시오. 다음으로, 아직 존재하지 않는 작업의 경우 해당 작업의 null 버전을 목록에 넣습니다. 마지막으로, 이 세션이 끝날 때 깨끗한 코드를 갖기 위해 수행해야 한다고 생각하는 모든 리팩토링을 나열하십시오.


한꺼번에 테스트를 작성하는 것이 안좋은 이유

첫째, 구현하는 모든 테스트는 리팩터링해야 할 때 약간의 확장성입니다.
둘째, 10개의 테스트가 실패했다면 녹색 막대에서 멀리 떨어진 것입니다.


## Test First

프로그래머로서의 목표는 기능을 실행하는 것입니다
However, you need a way to think about design, you need a method for scope control.
What if we adopted the rule that we would always test first? Then we could invert the diagram and get a virtuous cycle: Test-First above negatively connected to Stress below, negatively connected to Test-First.

When we test first, we reduce the stress, which makes us more likely to test. There are lots of other elements feeding into stress, however, so the tests must live in other virtuous cycles or they will be abandoned when stress increases enough. But the immediate payoff for testing a design and scope control tool - suggests that we will be able to start doing it, and keep doing it even under moderate stress.


## Assert First

When should you write the asserts? Try writing them first. Don't you just love selfsimilarity?
* Where should you start building a system? With stories you want to be able to tell about the finished system.
* Where should you start writing a bit of functionality? With the tests you want to pass with the finished code.
* Where should you start writing a test? With the asserts that will pass when it is done.

When you are writing a test, you are solving several problems at once, even if you no longer have to think about the implementation.

* Where does the functionality belong? Is it a modification of an existing method, a new method on an existing class, an existing method name implemented in a new place, or a new class?
* What should the names be called?
* How are you going to check for the right answer? 
* What is the right answer?
* What other tests does this test suggest?

Here's an example. 소켓통신을 통해 다른 시스템과 통신한다고 가정하자
```java
testCompleteTransaction() {
...
    assertTrue(reader.isClosed()); 
    assertEquals("abc", reply.contents());
}

```
reply가 오는 곳은? 소켓!
```java
testCompleteTransaction() {
...
    Buffer reply= reader.contents(); 
    assertTrue(reader.isClosed()); 
    assertEquals("abc", reply.contents());
}
```

서버에 연결하여 소켓만들기
```java
testCompleteTransaction() {
...
    Socket reader= Socket("localhost", defaultPort()); 
    Buffer reply= reader.contents(); 
    assertTrue(reader.isClosed());
    assertEquals("abc", reply.contents()); 
}
```
아 물론 그전에 서버를 열어야지
testCompleteTransaction() {
    Server writer= Server(defaultPort(), "abc"); 
    Socket reader= Socket("localhost", defaultPort()); 
    Buffer reply= reader.contents(); 
    assertTrue(reader.isClosed());
    assertEquals("abc", reply.contents()); 
}

만 아주 작은 단계로 테스트 개요를 만들어 각 결정에 몇 초 안에 피드백을 알려준다.

## Test Data

>What data do you use for test-first tests? Use data that makes the tests easy to read and 
follow!

The alternative to Test Data is Realistic Data, in which you use data from the real world. 
Realistic Data is useful when:
 - You are testing real-time systems using traces of external events gathered from the actual execution
 - You are matching the output of the current system with the output of a previous system 
(parallel testing)
 - You are refactoring a simulation and expect precisely the same answers when you are finished, particularly if floating point accuracy may be a problem

## Evident Data
Include expected and actual results in the test itself, and try to make their relationship apparent.

여기에 예가 있습니다. 한 통화에서 다른 통화로 변환하면 거래에 대해 1.5%의 수수료를 받습니다. USD와 GBP의 환율이 2:1이면 $100 를 교환하면 50GBP 1.5% = 49.25GBP가 됩니다. 이 테스트를 다음과 같이 작성할 수 있습니다.

```java
Bank bank= new Bank().
bank.addRate("USD", "GBP", STANDARD_RATE); 
bank.commission(STANDARD_COMMISSION);
Money result= bank.convert(new Note(100, "USD"), "GBP"); 
assertEquals(new Note(49.25, "GBP"), result);
```
in order to elaboate
```java
Bank bank= new Bank();
bank.addRate("USD", "GBP", 2); 
bank.commission(0.015);
Money result= bank.convert(new Note(100, "USD"), "GBP"); 
assertEquals(new Note(100 / 2 * (1 - 0.015), "GBP"), result);
```

