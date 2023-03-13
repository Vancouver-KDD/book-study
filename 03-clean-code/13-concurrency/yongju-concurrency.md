# Chapter 13. Concurrency

#### Why Concurrency?
Concurrency은 decoupling 전략 중 하나이다. Single-thread 어플리케이션은 *what*과 *when*이 강하게 결합을 가지고 있기 떄문에(어떤 일이 언제 일어났는 지 명확하기 때문에) stack backtrace만 보고도 전체 어플리케이션의 상태를 알 수 있다.

*What*과 *when*의 결합을 끊어내는 것은 어플리케이션의 구조와 처리량과 관련해 엄청난 이득을 가져다 준다.
구조적인 면에서는, 한 컴퓨터안에서 하나의 큰 어플리케이션이 돌아간다기보다는 여러 대의 작은 컴퓨터가 작업하고 있는 것처럼 보이기 떄문에 시스템을 이해하기 더 쉽고 concern들을 분리하기도 쉬워진다.
어떤 시스템에서는 반응시간과 처리량과 관련해 concurrency solution이 필요할 떄가 있는데 이럴 떄 multi-threaded algorithm을 이용해 performance를 향상시킬 수 있다.

###### Myths and Misconceptions

- Concurrency는 언제나 performance를 향상시킨다
  - multiple threads나 processor가 공유하는 wait time이 길 때, 때때로 향상이 이루어질 수는 있다
- Concurrency 프로그램을 작성할 때에는 디자인이 변하지 않는다
  - single-threaded system과 concurrency system은 구조적으로 굉장히 다르다 
- Web이나 EJB 컨테이너를 가지고 프로그램을 작성할 때에는 concurrency issue를 이해하는 것이 중요하지 않다
  - 컨테이너가 무엇을 하는지 어떻게 concurrent issue들을 해결하는 지 알고 있는 게 좋다

오해 중에 사실인 부분도 존재하는데,

- Concurrency는 performance면에서도, 혹은 코드를 더 작성해야 하기 때문에 비용이 비싸다
- Concurrency는 간단한 문제에서도 복잡하다
- Concurrency bug는 반복적이지 않다
- Concurrency는 종종 근본적인 디자인 전략 변화를 필요로 한다

#### Challenges
```java
public class X { 
  private int lastIdUsed;
  public int getNextId() {  
    return ++lastIdUsed; 
  }
}
```
위의 instance X가 두 개의 thread에 의해 공유되고 두 쓰레드 모두 getNextId를 호출한다고 가정해보자. 우리가 가질 수 있는 결과는 정확하지 않다. 
자세한 내용은 이 블로그 글을 참고하자: [Concurrency](https://remotelegs.tistory.com/12)
빠른 답으로는, 총 12,870개의 가능한 실행 경로가 존재한다. 대부분의 경로가 우리가 원하는 결과(lastIdUsed가 2번 증가되는)를 주지만, 그렇지 않은 경우가 있다는 게 문제이다.

#### Concurrency Defense Principles
  ###### Single Responsibility Principle
Single Responsibility Principle는 메소드, 클래스, 혹은 컴포넌트가 변경되어야 하는 이유는 오직 하나 뿐이어야 한다. 즉, 하나의 책임만을 가지고 있어야 한다. Concurrency 디자인은 이미 그 자체로 복잡하기 떄문에 다른 코드들과는 별개로 작성되어야 한다. 하지만, concurrency 코드를 다른 코드들과 함께 붙여 작성하는 것이 보편적이다.
- Concurrency 관련 코드는 그들만의 개발주기가 존재한다
- Concurrency 관련 코드는 non-concurrency코드들과는 다른, 떄떄로 더 어려운 문제들이 존재한다
- 잘 못 작성된 concurrency 코드는 다른 코드를 작성해 해결하지 않는 한 수정하기가 어렵다

  ###### Corollary: Limit the Scope of Data
- 두 개 이상의 thread가 하나의 오브젝트를 동시에 접근하는 것을 막는 방법으로는 synchronized 키워드를 사용해 [critical section](https://en.wikipedia.org/wiki/Critical_section#:~:text=A%20critical%20section%20is%20typically,conflicting%20changes%20to%20that%20data.)을 보호할 수 있다.(data의 scope를 제한한다)
공유되는 data가 더 많은 곳에서 update 될수록
  - 하나 이상의 코드에서 critical section을 보호하는 것을 깜빡할 수 있다
  - 모든 것이 완벽하게 돌아가기 위해 코드 중복이 생길 수 있다
  - 에러의 원인을 찾기 어려워진다
> Recommendation: Data encapsulation을 명심하고, 공유되는 data에 접근할 수 있는 access를 줄여라
  ###### Corollary: Use Copies of Data
- 공유되는 data를 피하는 방법은 처음부터 만들지 않는 것이다
  - 오브젝트를 복사해서 read-only로 만들 수도 있고,
  - 오브젝트를 복사한 후, 결과를 multiple threads에서 받은 후 마지막에 한 thread에서 결과를 종합할 수도 있다
- 오브젝트를 만들어내는 비용이 비쌀까 걱정이 될 수도 있는데, 당연히 조사해봐야겠지만, 보통은 오브젝트의 복사본을 사용해 synchronizing과 [intrinsic lock](https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html#:~:text=A%20thread%20is%20said%20to,attempts%20to%20acquire%20the%20lock.)을 피하는 것이 추가적인 오브젝트 생성이나 garbage collection보다 적은 비용이 들 가능성이 있다
  ###### Corollary: Threads SHould Be as Independent as Possible
- Thread 코드를 작성할 때에는 각 thread가 분리되어 다른 thread와 공유하는 데이터가 없는 것처럼 작성해라. 그러면 각 쓰레드가 마치 synchronization이 필요하지 않은 채 해당 쓰레드만 존재하게끔 동작하도록 한다
> Recommendation: 데이터가 독립적인 쓰레드에서, 가능하다면 다른 프로세서에서 필요로 할 수 있도록 하자

#### Know Your Library
Java에서 threaded 코드를 작성할 떄 몇 가지 생각해야할 점들이 있다

  ###### Thread-Safe Collections
- Concurrency 디자인을 위한 collection을 사용하도록 하자. (e.g. HashMap > ConcurrentHashMap)

| Name      | Description |
| ----------- | ----------- |
| ReentrantLock      | 한 메소드에서 얻은 후 다른 메소드에서 release가 가능한 lock       |
| Semaphore   | lock에 일정한 count를 주어 접근 가능한 thread 수를 제어한다        |
| CountDownLatch | 일정한 숫자의 이벤트만큼 기다렸다가 모든 threads를 release하는 lock |

> Recommendation: 사용가능한 class를 리뷰하고 concurrency 관련 클래스들과 친해지자

#### Know Your Execution Models
- Concurrent 어플리케이션의 동작원리를 나누는 몇 가지 방법이 있는데 그 전에 필요한 개념을 알아보자
| Name      | Description |
| ----------- | ----------- |
| Bound Resources  | Concurrent 환경에서 사용되는 혹은 고정된 resource의 수 |
| Mutual Exclusion | 오직 한 번에 한 쓰레드만이 공유되는 데이터나 resource에 접근할 수 있다 |
| Starvation | [참고](https://remotelegs.tistory.com/13) |
| Deadlock | [참고](https://remotelegs.tistory.com/13) |
| Livelock | [참고](https://remotelegs.tistory.com/13) |

  ###### Producer-Consumer

  ###### Readers-Writers

  ###### Dining Philosophers

#### Beware Dependencies between Synchronized Methods

#### Keep Synchronized Sections Small

#### Writing Correct Shut-Down Code Is Hard

#### Testing Threaded Code

  ###### Treat Spurious Failures as Candidate Threading Issues

  ###### Get Your Non-threaded Code working First

  ###### Make Your Threaded Code Pluggable

  ###### Make Your Threaded Code Tunable

  ###### Run with More Threads Than Processors

  ###### Run on DIfferent Platforms

  ###### Instrument Your Code to Try and Force Failures

  ###### Hand-Coded

  ###### AUtomated

#### Conclusion
