## 13. Concurrency

### Why Concurrency?
동시성은 디커플링 전략이다. 그것은 우리가 그것이 완료되었을 때 무엇이 완료되었는지 분리하는 것을 도와줍니다. 
단일 스레드 응용 프로그램에서 언제 무엇이 강력하게 결합되어 전체 응용 프로그램의 상태는 종종 스택 역추적을 보고 결정될 수 있다. 
이러한 시스템을 디버깅하는 프로그래머는 브레이크포인트 또는 브레이크포인트의 시퀀스를 설정하고 브레이크포인트가 적중하는 시스템의 상태를 알 수 있다
When으로부터 what을 분리하면 애플리케이션의 처리량과 구조를 모두 획기적으로 개선할 수 있습니다.


#### Myths and Misconceptions

>common myths and misconception

- 동시성은 항상 성능을 향상시킵니다?
아니다 여러 thread나 processes간에 공유할 수 있는 대기시간이 긴 경우에만 성능을 향상시킨다는 조건이 붙어야 한다.
- 동시 프로그램을 작성할 때 설계는 변경되지 않는다?   아니다 the design of a concurrent algorithm 과 design of a single-threaded system 는 다르며, 사실 무엇과 언제를 분리하는 것은 보통 시스템 구조에 큰 영향을 미친다.
- Web이나 EJB container같은 컨테이너와 work할때 concurrency issue는 중요하지 않다?   컨테이너가 무엇을 하고 어떻게 경계해야 하는지를 아는 것은 중요하며 concurrent update and deadlock에 대해서 아는 것이 중요하다.

>Additional features
-  Concurrency incurs some overhead in 성능과 추가 코드 작성
-  Correct concurrency is complex even for simple problems.
-  concurrency 버그는 일반적으로 반복할 수 없기 때문에 실제 결함 대신 일회성으로 무시되는 경우가 많다.
-  concurrency 종종 design strategy의 근본적인 변화를 필요로 한다.

### Challenges

아래에서 X의 인스턴스를 만들고, lastIdUsed를 42로 initialize하고 그리고 이를 두개의 Threads와 공유를 한 후 두개의 Threads가 getNextId method를 콜하게 된다고 가정하게 되면!
```java
public class X {
   private int lastIdUsed; 
   public int getNextId() { 
        return ++lastIdUsed; 
    }
}
```
아래의 세가지가 가능하다.
-  Thread A:43, thread B:44, lastIdUsed:44.
-  Thread A:44, thread B:43, lastIdUsed:44.
-  Thread A:43, thread B:43, lastIdUsed:43.
이 중 세번째에 주목하자. 동시 발생이다. 두개의 스레드가 자바의 한개 라인을 통과하는 많은 가능한 경로가 있기 이는 발생한다.
 Just-In-Time 컴파일러가 생성된 코드로 무엇을 하는지와 자바메모리 모델이 atomic으로 간주되는 것을 이해해야한다.
바이트 측면에서는 getNextI method로 12,870개의 실행 경로가 있다. 만약 lastIdUsed를 int에서 long으로 바꾸게 된다고 하면 이제는 2,704,156가 되게된다.

### Concurrency Defense Principles
 concurrent code의 문제로부터 system을 보호하기 위한 일련의 원칙과 기술

#### Single Responsibility Principle
Single Responsibility Principle는 지정된 메서드/클래스/구성요소가 변경될 수 있는 single 이유로 구성되어져야한다.
그러나 보통 다른 production code와 엮이는 경우가 많은데 아래를 고려하여야 한다.

- Concurrency-related code 개발, 변경 및 튜닝의 자체 라이프 사이클을 가지고 있다.
- Concurrency-related code는 non concurrency-related code와 다르고 종종 더 어려운 고유한 과제를 가지고 있다.
- 잘못 작성된 Concurrency-related code가 실패할 수 있는 방법의 수는 주변 application 코드의 추가 부담 없이 충분히 도전적이다.

#### Corollary: Limit the Scope of Data
범위를 제한하는 것 즉, shared object의 섹션을 보호하는 것 그러기 위해서는 synchronized 키워드를 사용한다.
그럼 shared data의 portion이 많아지는 것에 대한 위험성은?
- 공유 데이터를 수정하는 모든 코드를 효과적으로 잡아내는 부분을 하나 이상을 보호하는 것을 놓치게 된다.
- 모든 것이 효과적으로 보호되는지 확인하는 데 필요한 노력이 중복됩니다(DRY7 위반).
- 실패의 원인을 찾기 어렵다.

#### Corollary: Use Copies of Data
데이터의 공유를 피하는 방법으로는 데이터의 복사하여 read-only로 사용하는 것이다.
또한 오브젝트를 복사하여 해당 카피데이터의 여러 쓰레드의 결과를 취함하여 싱글 쓰레드로 merge하는 방법이다.
DB접근에 대한 lock을 피하므로서의 절약이 추가 복사 오브젝트를 사용하여 발생하는 cost가 상충될 것이므로 결과적으론 cost efficient이다.

#### Corollary: Threads Should Be as Independent as Possible
스레드는 가능한한 독립적이어야 한다.
각 스레드는 하나의 클라이언트의 요청을 수행하고 공유되지않는 소스로 부터 데이터를 가져오고 로컬 변수로 저장되게 한다.
예로 classes that subclass from HttpServlet receive all of their information 
as parameters passed in to the doGet and doPost methods. This makes each Servlet act 
as if it has its own machine.


### Know Your Library
스레드 코드를 작성 시 고려사항 몇가지
- 제공된 thread-safe 컬렉션을 사용합니다.
- 관련 없는 작업(unrelated tasks)을 실행하려면 Use the executor framework
- 가능하면 nonblocking solutions 사용
- 일부 라이브러리 클래스는 thread safe하지 않다.

#### Thread-Safe Collections
java.util.concurrent package
 the ConcurrentHashMap implementation performs better than HashMap in nearly all situations

advanced concurrency design below
- ReentrantLock
- Semaphore
- CountDownLatch

### Know Your Execution Models
concurrent application 내의 behavior를 구분

 그 전에 아래를 먼저 이해해보자.
- Bound Resources : Resources of a ﬁxed size or number
- Mutual Exclusion : Only one thread can access shared data at a time
- Starvation : One thread or a group of threads is prohibited from proceeding for an excessively long time or forever.
- Deadlock : Two or more threads waiting for each other to ﬁnish. 
- Livelock


#### Producer-Consumer
producer thread가 버퍼나 뷰에 Task를 생성 배치
condumer threads가 큐에서 해당 Task를 완료
이 둘의 관계는 엮여있는 관계의 자원이다. 즉 resource의 free space을 대기하고 작업 존재 시 진행하는 관계 즉 서로의 signal을 주고 받는 것을 의미.

#### Readers-Writers
share data의 경우 Reader를 향한 resource가 주 목적이지만 Reader에 의해 업데이트되는 경우 throughput이 처리되는 양이 문제가 될 수 있다.
데이터의 덜준비된 데이터와 미반영된 데이터에 대한 부분으로 Reader가 Writer의 작업중 read를 못하도록 제한하거나 반대의 경우 throughput이 문제가 될 수 있다는것
Reader가 없을때까지 Writer는 기다린다? 만약 계속 존재하면?
즉 이두개의 균형을 찾아 해결하는 건이 문제해결의 핵심.

#### Dining Philosophers
멸티 스레드가 Resource를 처리하기 위해 경재하는 형식
deadlock, livelock, throughput, and efﬁciency degradation(저하)을 야기한다. 알고리즘을 착안해서 해결하는 방식.

### Beware Dependencies Between Synchronized Methods
Dependencies between synchronized methods cause subtle bugs in concurrent code.
Recommendation: Avoid using more than one method on a shared object.

-  Client-Based Locking
첫번째 메소드 호출 전 클라이언트가 서버를 lock하고 last메소드가 잠그는 범위가 포함되는지 확인					
-  Server-Based Locking
서버내 서버를 잠그고 메소드 호출하게 하고 잠금해제 하는 메소드 활용
-  Adapted Server
잠금에 대한 역할을 중개자에 부여

### Keep Synchronized Sections Small
. 동일한 잠금으로 보호되는 코드의 모든 섹션은 주어진 시간에 하나의 스레드만 실행되도록 한다. 왜냐면 lock은 delay와 overhead를 수반하드로 비용이 든다. 그러므로 최소화하자.


### Writing Correct Shut-Down Code Is Hard
정상 종료는 정확하기 어렵다 왜냐면 교착상태로써 스레드는 오지않을 신호를 계속 기다린다.
Recommendation: Think about shut-down early and get it working early.

### Testing Threaded Code
테스트는 위험성을 최소화 할수있다. 물론 멀티스레드의 경우는 더욱 복자해진다.
Recommendation: Write tests that have the potential to expose problems 
and then run them frequently, with different programatic conﬁgurations and system conﬁgurations and load.

권장 사항:
-  Treat spurious failures as candidate threading issues.
-  Get your nonthreaded code working ﬁrst.
-  Make your threaded code pluggable.
-  Make your threaded code tunable.
-  Run with more threads than processors.
-  Run on different platforms.
-  Instrument your code to try and force failures.

#### Treat Spurious Failures as Candidate Threading Issues
스레드 코드의 버그는 천 번 또는 백만 번의 실행에 한 번씩 증상을 나타낼 수 있습니다.
권장 사항: 시스템 장애를 일회성으로 무시하지 마십시오.

#### Get Your Nonthreaded Code Working First
Make sure code works outside of its use in threads. 
Recommendation: Do not try to chase down nonthreading bugs and threading bugs at the same time. 
Make sure your code works outside of threads.

#### Make Your Threaded Code Pluggable
Write the concurrency-supporting code. 
-  One thread, several threads, varied as it executes
-  Threaded code interacts with something that can be both real or a test double.
-  Execute with test doubles that run quickly, slowly, variable.
-  Conﬁgure tests so they can run for a number of iterations.
Recommendation: Make your thread-based code especially pluggable so that you can run it in various conﬁgurations.

#### Make Your Threaded Code Tunable
Early on, ﬁnd ways to time the performance of your system under different conﬁgurations. Allow the number of threads to be easily tuned.

#### Run with More Threads Than Processors
To encourage task swapping, run with more threads than processors or cores. 
The more frequently your tasks swap, the more likely you’ll encounter code that is missing a critical section or causes deadlock.

#### Run on Different Platforms
Multithreaded code behaves differently in different environments. You should run your tests in every potential deployment environment.

#### Instrument Your Code to Try and Force Failures
It is normal for ﬂaws in concurrent code to hide. 
Let's add calls to methods like Object.wait(), Object.sleep(), Object.yield() and Object.priority().
Each of these methods can affect the order of execution, thereby increasing the odds of detecting a ﬂaw. 
미리 에러케이스 만들어 미리 점검하는 것이 낫다.

### Hand-Coded
분명히, 우리가 시스템을 스레드화에 대해 전혀 모르는 POJO로 나눈다면 스레드화를 제어하는 클래스, 코드를 계측하기에 적합한 위치를 찾는 것이 더 쉬울 것이다.
#### Automated
Aspect-Oriented Framework, CGLIB 또는 ASM과 같은 도구를 사용하여 프로그래밍 방식으로 코드를 계측

```java
public class ThreadJigglePoint { 
    public static void jiggle() { 
    }
}
You can add calls to this in various places within your code:
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
Recommendation: Use jiggling strategies to ferret out errors.

