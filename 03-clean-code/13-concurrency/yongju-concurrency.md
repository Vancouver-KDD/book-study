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
b
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
- 하나 이상의 produce가 task를 만든 후에 buffer나 queue에 전달하면 하나 이상의 consumer threads가 그 task를 가져온 후 완료한다. Producer와 Consumer 사이의 Queue가 bound resource이다.
  ###### Readers-Writers
- Reader에게 주된 데이터 소스가 writer와 공유되고, writer가 불규칙한 주기로 그 데이터를 업데이트한다면 문제가 생길 수 있다. 모든 Reader를 기다리자니 writer가 starvation 상태가 되고, writer가 reader를 무시한 채 업데이트하게 하자니 reader가 기다려야 하는 상황이 생긴다.
  ###### Dining Philosophers
- [Wikipedia](https://en.wikipedia.org/wiki/Dining_philosophers_problem)
- Process들이 resource를 위해 경쟁하는 모습은 많은 enterprise 어플리케이션에서 볼 수 있다.

#### Beware Dependencies between Synchronized Methods
- 공유되는 클래스 안에 두 개 이상의 synchronized 메소드가 있다면 그 시스템은 잘 못 쓰여진 것이다
- 반드시 두 개 이상의 메소드를 사용해야 하는 경우가 있을텐데, 그럴 때는 다음 옵션들로 바로잡을 수 있다
  - Client-Based Locking: 첫번쨰 메소드를 부르기 전에 클라이언트에게 lock을 주고 마지막 코드를 부를때까지 lock이 작동할 수 있도록 한다
  - Server-Based Locking: 서버 안에 서버를 잠그는(lock) 메소드를 만들고 모든 메소드를 호출 한 후에 unlock한다
  - Adapted Server: 중간에서 Lock을 관리할 수 있는 중개자를 만들어라(e.g. server-based locking을 사용하려고 하는데 server를 변경할 수 없을 때)

#### Keep Synchronized Sections Small
- Lock을 사용하는 것은 딜레이를 만들고 overhead를 만들기 때문에 비용이 크기 떄문에 우리는 최대한 적은 critical section을 이용해 코드를 디자인해야 한다

#### Writing Correct Shut-Down Code Is Hard
- 완벽한 종료 코드는 구현하기가 어려운데 가장 보편적인 문제는 deadlock이다. 예를 들어, 많은 child threads를 만드는 parent thread가 있고, 모든 child threads가 끝나기를 기다린 후에 resource를 release하고 완료하는 프로그램이 있다. Child threads 중 하나만 문제가 생겨도 parent thread는 deadlock에 걸리게 될 것이다

#### Testing Threaded Code
Testing은 완벽함을 장담하지는 못하지만, 좋은 테스트는 risk를 줄일 수 는 있다
  ###### Treat Spurious Failures as Candidate Threading Issues
- Threaded 코드의 버그는 아주 많은 실행 중에 딱 한 번만 발견 될 수도 있다. 한 번만에 코드를 검증하는 것은 불가능하다.
  ###### Get Your Non-threaded Code working First
- thread 밖의 코드들이 정상적으로 동작하는 것을 확실하게 확인해라. POJO는 thread와 동떨어져 있기 떄문에 threaded 환경과 별개로 테스트가 가능하기 떄문에 thread안에서 POJO를 최대한 사용하도록 하자
  ###### Make Your Threaded Code Pluggable
- 변수가 다른 많은 환경에서 threaded code를 작성해라
  - thread의 개수를 변경해가면서 테스트해라
  - 최대한 많은 실행을 할 수 있도록 테스트를 작성해라
  ###### Make Your Threaded Code Tunable
- 여러 환경에서 performance를 테스트 할 수 있는 환경을 만들고, 쉽게 thread의 수를 변경할 수 있도록 해라
  ###### Run with More Threads Than Processors
- task들이 전환될 떄 문제가 발생하기 때문에 프로세서 혹은 코어 수보다 많은 쓰레드를 돌려보아라
  ###### Run on Different Platforms
- 다른 OS들은 각기 다른 threading policy를 가지고 코드를 실행시키기 때문에, 다른 platform들에서 모두 테스트 해라
  ###### Instrument Your Code to Try and Force Failures
- Concurrent code에서의 버그는 찾기 어려운 게 일반적이다. 몇 시간, 며칠, 혹은 몇 주에 한 번만 일어날 수도 있다. 수없이 큰 경우의 수중에 얼마 되지 않는 경우가 버그로 이어진다. 그래서 우리는 wait(), sleep(), yield(), 혹은 priority() 같이, 순서를 변경할 수 있는 메소드들을 호출해서 많은 다양한 경우를 테스트 해야 한다
- 이 순서를 변경하는 방법에는 아래 두 가지가 있다
  ###### Hand-Coded
```java
public synchronized String nextUrlOrNull() { 
  if(hasNext()) { 
    String url = urlGenerator.next(); 
    Thread.yield(); // inserted for testing. 
    updateHasNext(); 
    return url; 
  } 
  return null; 
}
```
yield() 메소드를 추가함으로써 코드가 일어날 수 있는 경우의 수를 추가했고 테스트가 실패할 수 있다. 하지만 그건 해당 메소드를 추가해서가 아니라 원래 실패할 코드였고 증거가 드러났을 뿐이다.
이 방법에는 문제가 몇 가지 있는데
- 메소드를 추가할 적당한 장소를 직접 찾아야 한다
- 어디에 어떤 메소드를 넣을 지 정해지지 않았다
- 프로덕션 코드에 쓸 데 없이 추가가 된다
- 버그를 찾을 수 있을 지 없을 지 운에 달려 있다

만약 우리가 시스템을 threading과 전혀 동떨어진 POJO들과 threading을 관리하는 class로 나눈다면, 테스트하기가 쉬워질 것이다
  ###### Automated
- CGLIB 혹은 ASM같은 Aspect-Oriented Framework를 사용할 수 있다

```java
public class ThreadJigglePoint { public static void jiggle() { } }
```
만약 위와 같은 하나의 메소드를 가진 클래스가 있다면, 아래처럼 다양한 곳에 넣어 테스트할 수 있을 것이다
```java
public synchronized String nextUrlOrNull() { 
  if(hasNext()) { 
    ThreadJiglePoint.jiggle(); 
    String url = urlGenerator.next(); 
    ThreadJiglePoint.jiggle(); 
    updateHasNext(); 
    ThreadJiglePoint.jiggle(); 
    return url; 
  } 
  return null;
}
```

중요한 점은 코드를 변경해서 thread가 다른 순서로 다른 시간에 동작할 수 있도록 하는 것이다.

#### Conclusion
- Concurrent 코드는 완벽하게 만들기 어렵다. 그렇기 때문에,
- 첫째로, 그리고 가장 중요하게 Single Responsibility Principle을 따라라
- concurrency issue를 낳을 수 있는 곳을 알아라
- library와 fundamental 알고리즘을 배워라
- 어떻게 코드를 나누는지, lock을 이용해 잠그고 unlock할 수 있는 지 배워라
- 문제는 예상치 못하게 발견될 것이다. 다양한 환경에서 계속 많이 반복적으로 threaded-code를 돌려보아라
- 코드를 jiggle할 시간을 할애한다면 에러를 가지고 있는 코드를 발견할 가능성이 높아질 것이다
