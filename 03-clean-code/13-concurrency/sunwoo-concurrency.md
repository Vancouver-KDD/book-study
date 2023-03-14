# Concurrency
Concurrent 프로그램은 동시에 여러가지 테스크를 하기위해 혹은 그렇게 보이도록 디자인된 프로그램입니다.
대표적으로는 데이터베이스, 웹 서버등이 있고, 여러 유저의 request나 event등을 동시에 처리하도록 디자인 되어 있습니다.
Concurrent 프로그램은 작성하기도 어렵지만, 테스트 또한 어렵습니다. 어떤 에러들은 특정한 시나리오에서만 나오기도 하기 때문입니다.
이 챕터에서는 concurrent 프로그램에서 발생할수 있는 문제나 테스트의 어려움에 대해서 알아보겠습니다.

## Why Concurrency?
Concuurency는 프로그램이 <strong>무엇</strong>을 하는지와 <strong>언제</strong> 하는지를 decouple하는 하나의 전략으로 볼 수 있습니다.
이 decoupling은 코드의 시스템의 구조를 향상시키는 것 뿐만아니라, throughput(시스템의 효율성)을 극적으로 향상시킬수 있습니다.

예를 들어, 프로그램이 한 개의 request를 1초에 처리하고, 한 request가 처리된 후 다음 것을 처리한다면,
조금만 request의 수가 늘어나도 마지막에 들어오는 request는 굉장히 많은 시간을 기다려야 할 수 있지만,
이 request들을 asynchronously하게 처리하게 된다면, 각각의 request를 보낸 end point에서는 평균적으로 빠른 response를 받을 수 있습니다.

Concurrency한 프로그램이나 프레임워크를 사용할때에도, 혹은 직접 작성해야 하는 경우에도 concurrency에 대한 이해와 함께 조심스럽게 접근을 해야합니다.

### Myths and MisConceptions

Concurrency를 적용해야 하는 많은 이유들이 있지만, Concurrency를 적용하는 것은 많은 노력이 필요하고, 또한 많은 예상되지 못한 에러들을 발생시킬수 있습니다.

Concurrency를 적용하기 전에, Concurrency에 대한 잘못된 오해와 통념들을 알아보겠습니다.
* Concurrency always improves performance.
Concurrency는 thread 혹은 프로세스가 idle한 시간이 많을때 퍼포먼스 형상에 대해 기대할 수 있습니다.
* Design does not change when writing concurrent programs.
concurrent에 사용 되는 알고리즘은 근본적으로 single thread에서 사용 되는 것과 많이 다를 수있고, 이 차이는 시스템 구조에 대한 변경이 필요 할 수 있습니다.
* Understanding concurrency issues is not important when working with a container such as a Web or EJB container.
conccurency 문제에 대한 이해는 concurrency한 프로그램을 사용될때에도 문제를 인식하는데 도움을 줄 수 있습니다.


Conccurency는 퍼포먼스와 코드량에 대해 추가적인 오버헤드(operations to manage threads and processes)를 발생시키며, 간단한 문제에도 복잡한 코드의 오버헤드를 발생 시킵니다.
또한 concurrent에 관련된 버그는 thread와 프로세서의 interaction에 의해 발생 할 수 있어, 반복적으로 일어나지 않고, context나 enviroment에 영향을 받을 수 있습니다.
또한 근본적인 design strategy에 변화가 필요 할 수 있습니다.

### Challenges
다음과 같은 간단한 클래스에서도 다음과 같은 3가지 결과값이 나올 수 있습니다.
```Java
public class X {
    private int lastIdUsed;
    
    public int getNextId() {
       return ++lastIdUsed;
    }
}
```
* Thread one gets the value 43, thread two gets the value 44, lastIdUsed is 44.
* Thread one gets the value 44, thread two gets the value 43, lastIdUsed is 44.
* Thread one gets the value 43, thread two gets the value 43, lastIdUsed is 43.

lower level로 들어가보면 두개의 thread가 `getNextId`에 대한 총 12,870가지의 다른 excution path에 가질수 있습니다.

## Concurrency Defense Principle
다음으로 conccurent code로부터 발생 할 수 있는 문제에 대해 어떻게 시스템을 보호 할 수 있을지 알아보겠습니다.

### Single Responsibility Principle
Concurrent 디자인은 그 복잡성으로 인해 분리된 코드로 관리되야 하고, 다음과 같은 요소들을 고려해야 합니다.
* Concurrency와 관련된 코드는 자체적인 developement life cycle을 가지고 있습니다.
* Concurrency와 관련된 코드는 비동시성 프로그램과 다르며 더 어렵습니다.
* Concurrency와 관련된 코드는 실패하는 시나리오가 많기 때문에, 추가적인 어플리케이션 코드 없이도 충분히 도전적입니다.

### Corollary: Limit the Scope of Data
`synchronized` keyword를 통해 코드의 critical한 부분을 Concurrency issue로 부터 보호 할 수 있지만, 
이러한 critical한 부분을 최소화 하고, thread에 의해 share될 수 있는 data object의 encapsulation에 대해 신중히 고려해야 합니다.

### Corollary: Use Copies of Data
오브젝트를 카피하고 그것을 read-only로 관리하는 것도 data를 share할 때 취할수 있는 좋은 방법입니다.
하지만 이 방법도 object creation이나 garbage collection에 대해 오버헤드를 발생 시킬수 있습니다.

### Corollary: Threads Should Be as Independent as Possible
각각의 thread가 다른 데이터를 서로 공유하지 않도록 작성을 하는 것도 좋습니다.
각 thread는 각각의 리퀘스트를 처리하며, 공유되지 않는 소스에서 데이터를 가져오고, 로컬 변수에 값을 저장합니다.
이렇게 할 경우 각각의 thread가 마치 single thread에서 작동하는 것처럼 동작하고 복작한 synchronization requirement가 없습니다.

## Know Your Library
Java5에서는 concurret 프로그램을 작성할때, 다음과 같은 점을 고려해볼수 있습니다.

### Thread-Safe Collections
`java.util.concurrent` 패키지의 있는 `Collection` class는 concurrent한 상황에서 안전하게 사용이 가능하고 잘 기능합니다.
또한 다음과 같은 concurrency 디자인에 사용될 수 있는 클래스들이 있습니다
* ReentrantLock[[1]](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/ReentrantLock.html)
lock 메카니즘으로, 기본적으로 resources에 여러 쓰레드의 접근을 허가하지만, 그것을 execution하는 것은 하나의 쓰레드만이 동시에 하도록 lock하는 것입니다.

* Semaphore[[2]](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Semaphore.html)
Semaphore? shared resource에 동시에 접근 할 수 있는 쓰레드의 갯수를 제한 하는 것. 만약 sempahore를 통해 받은 integer 값이 0보다 크다면 데이터에 접근이 가능하고, 그렇지 않다면 sempaphore가 available할때까지 block된다.

* CountDownLatch[[3]](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CountDownLatch.html#:~:text=A%20CountDownLatch%20is%20a%20versatile,a%20thread%20invoking%20countDown()%20.)
Count value를 생성하고, 그 카운트 밸류 만큼의 operation이 complete 되었을때, latch가 릴리즈 됩니다. latch가 릴리즈 될때, 대기하고 있는 쓰레드들은 다음 operation으로 진행할수 있습니다.
## Know Your Execution Models
Concurrent 어플리케이션에는 다음과 같이 다른 방법으로 hehaviours를 분할 할 수 있습니다.
* Bound Resource - concurrent 환경에서 사용 되는 고정된 사이즈나 갯수의 리소스
* Mutual Exclusion - 오직 한개의 쓰레드만이 같은 시간에 shared resource에 대한 접근성을 가진다.
* Starvation - 한개 혹은 그 이상의 쓰레드가 긴 시간이나 계속적으로 shared resource에 대한 접근성을 가지는 것을 금지합니다. 만약 higher priority의 쓰레드가 접근성을 계속 가져간다면, 다른 lower priority의 쓰레드는 resource에 대한 접근이 불가능 합니다.
* Deadlock - 두개 혹은 그 이상의 쓰레드가 서로에게 필요한 resource를 hold하고, 서로 hold하고 있는 resource를 필요로 할때입니다.
* Livelock - 두개 이상의 쓰레드가 지속적으로 상대의 상태 변화에 반응 하는 것입니다.

### Producer-Consumer
![pc-image](https://jenkov.com/images/java-concurrency/producer-consumer-1.png)
Producer - create work and push it to queue or buffer
Consumer - acquire the work and complete it
Queue(bound resource) - producer는 queue에 free space가 생길때까지 대기해야 하고, consumer는 queue에 task가 있을때까지 대기해야 합니다.

### Readers-Writters
Readers-Writers는 concurrent program에서 일어날 수 있는 문제 중 하나로,
하나의 shared resource에 한 개의 쓰레드가 writing을 함으로써, 많은 reader 쓰레드가 lock이 풀리는 것을 기다리는 문제입니다.
이것은 프로그램에 throughput에 writing을 하는 시간에 비례하여 문제를 일으킬수 있습니다.

### Dining Philosophers
Dining philosophers 문제는 deadlock이 발생 하는 것에 대한 문제와 대안책을 표현한 것입니다.[[1]](https://en.wikipedia.org/wiki/Dining_philosophers_problem)
philosopher를 thread로 fork를 resource로 치환한다면, threads가 resource를 획득하는 것을 경쟁하는 문제로 볼 수 있습니다.
잘 디자인되어있는 시스템에서도 이러한 경쟁상황에 놓이면  deadlock, livelock, throughput 그리고 efficiency 저하등의 문제들을 경험 할 수 있습니다.

## Beware Dependencies Between Synchronized Methods
synchronized methods 사이의 의존성은 알아채기 어려운 에러들을 발생시킬수 있는 리스크가 있습니다.
그러므로 shared object에 접근하는 메소드를 한개로 줄이는 것이 이상적입니다.
그렇지 못한 경우에는 다음과 같은 방법을 취할수 있습니다.
* Client-Based Locking: 클라이언트에서 첫 메소드를 부르기전에 lock을 걸고 이 lockd은 마지막 메소드를 부르기 전까지 지속됩니다.
* Server-Based Locking: 서버내에서 서버를 lock 시키고, 모든 메소드가 호출 된후 unlock하는 방법입니다.
* Adapted Server: lock역할을 하는 중개자를 구성합니다. 이것은 server-based locking의 예중하나로 서버의 코드를 변경시키지 않습니다.

## Keep Synchronized Secitions Small
synchronized 키워드로 생성된 코드가 많을수록, 프로그램은 single thread로써 동작하게 되며, 이것은 delay와 오버헤드가 추가적으로 생성됩니다.
따라서 synchronized 코드블럭은 최소한으로 유지되어야 합니다.

## Writing Correct Shut-Down Code is Hard
Graceful하게 시스템을 shutdown하는 것은 deadlock 같은 문제들을 자주 야기합니다.
Shut-down에 대해 초기부터 고려하고 초기부터 작성을 합니다. 이것은 쉬어보이는 알고리즘에서도 생각보다 많은 시간을 소비 할수 있습니다.

## Testing Threaded Code
테스트가 코드가 정확하다는 것을 보장해주지 않지만, 테스트를 통해 리스크를 최소화 할 수는 있습니다.
테스트를 문제가 생길수 있는 환경에서 작성을 합니다. 예를 들어 다른 programatic이나 system configuration에서 테스트 할 수 있습니다.

### Treat spurious failures as candidate threading issues
쓰레드 코드는 간단한 코드에서도 실패 할수 있습니다. 이러한 실패를 하나의 반복되지 않는 우연의 실패로 보지않고, 제대로 문제를 인지해야 합니다.

### Get your nonthreaded code working first
쓰레드 코드를 테스트 하기 전에 쓰레드가 아닌 코드가 제대로 동작한다는 확신이 있어야 합니다. 
쓰레드 에러와 쓰레드가 아닌 에러를 동시에 trace하는 것은 어렵습니다.

### Make your threaded code pluggable
Thread-based 코드는 pluggable 해야 합니다, 그렇게 다른 configuration(one thread, several threads)에서도 동작할수 있습니다.

### Make your threaded code tunable
다른 환경에서 혹은 configuration에서 최적의 thread 세팅은 다를수 있습니다. 또한 이 최적화 과정은 trials and errors를 필요로 합니다.
따라서 사용되는 쓰레드의 갯수를 쉽게 조정할 수있고, 또한 런타임 도중 thoughput과 system utilization등을 모니터링하여 self-tuning 할 수 있는 시스템을 빌드하는 것도 좋은 선택입니다.

### Run with more threads than processors
실행해야하는 execution unit(thread)양이 그것을 처리해야 하는 processor의 갯수보다 많을 때, 더 많은 task swap이 일어나고, 
따라서 더 많은 잠재되어 있는 리스크에 노출이 되어 발견하지 못한 에러들을 찾아낼수 있습니다.

### Run on different platforms
thread는 각각의 플랫폼 혹은 OS등에 깊이 연관 되어있고, 플랫폼에 따라 다르게 행동 할 수 있습니다.
그러므로 사용이 될 가능성이 있는 모든 environment에서 테스트를 실행해보는 것이 중요합니다.

### Instrument your code to try and force failures
concurrent code의 에러는 수많은 execution path중 몇몇에서만 나타날수 있어서, 일반적인 테스트로는 잡아내기 힘들수 있습니다.
concurrent code의 코드에 추가적으로 코드를 작성하고 다른 순서들로 execution해보아서 실패에 대한 데이터를 모으는 것이, 초기에 에러에 노출되는 가능성을 증가 시킬수 있습니다.

#### Hand-Coded
`wait()`,`sleep()`,`yield()`그리고 `priority()`등을 추가적으로 직접 넣어서 강제적으로 thread의 execution 순서를 변경 시킬수 있습니다.
```Java
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
위에 예제처럼 `yield()`메소드를 통해서 지금 진행중인 쓰레드를 멈추고, 또 이러한 변경은 평소에는 실행되지 않았던 execution path를 지나, 새로운 에러를 발견할수 있습니다.
이러한 변경은 오직 테스트 환경에서만 실행해야 하고, production 코드에서는 당연히 이러한 hand-coded 코드는 포함되지 않습니다.

#### Automated
혹은 프레임워크등을 통해 이러한 테스트를 자동화 할 수 있습니다.
```Java
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
`ThreadJiglePoint.jiggle()`는 쓰레드의 메소드를 랜덤으로 실행합니다. 또한 이 메소드는 production코드에서는 아무런 기능을 하지 않습니다.

## Conclusion
concurrent code는 작성도 어렵지만 관리하는 것도 어렵습니다.
따라서 concurrent code를 작성을 할때에는 더욱더 엄격하게 clean하게 작성을 할것이 요구가 됩니다.
concurrent code를 기존 코드와 분리해야 하고, concurrency 혹은 관련된 라이브러리에 대한 이해가 필요합니다.
clean한 어프로치를 취한다면, concurrency 코드가 예상되로 행동할 가능성이 증가합니다.