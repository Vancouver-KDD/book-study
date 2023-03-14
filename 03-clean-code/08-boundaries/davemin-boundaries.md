## 8. Boundaries

타사의 패키지등의 오픈 소스를 이용할 때 the boundaries of our software를 clean 하게 유지하는 practices and techniques 을 얘기해보자.

### Using Third-Party Code

보통 인터페이스는 광범위한 요구사항에 맞춰 제공되고, 하지만 사용자들은 특정 범위내의 초점을 맞춘 인터페이스를 원한다.
이런 경계는 문제를 일으킬 수 있다. 

Let’s look at java.util.Map as an example. 

주어진 API의 기능이 광범위함으로 그 범위를 줄여나가는 방법


보통의 사용이 아래와도 같을 수 있다.
```
Map sensors = new HashMap(); // 타입을 지정하지 않고 광범위한 상태
Sensor s = (Sensor)sensors.get(sensorId );
// 해당 클라이언트 코드가 Map으로 부터 적절한 캐스팅을해서 반환하는 책임을 지는 형식
```

가독성을 제네릭을 사용하여 향상하는 방식이 아래와 같고??
```
Map<Sensor> sensors = new HashMap<Sensor>();
...
Sensor s = sensors.get(sensorId )
//제네릭을 통한 구현.. 근데 어쨌거나 제네릭으로 받아서 internally Sensor 타입으로 반환하는 건 마찬가지 아닌가?...
```

하지만 여전히 Map<Sensor>가 기능을 제한하는 방법이 되지는 못한다.

Map<Sensor>의 instance를 passing한다는 것은 change가 있을때 주변에 고쳐야할 곳이 많다는게 된다.
그래서 좀더 깔끔한 방법은 아래와 같이 제네릭 사용여부에 상관없이 Map<Sensor>의 instance를 passing하지않고
string id 던지는 아래와 같은 형태 그래서 구현 세부로 해결나가는 방식
```java
public class Sensors {
	private Map sensors = new HashMap(); 
	public Sensor getById(String id) {
		return (Sensor) sensors.get(id); 
	}
//snip 
}
```
음 얘기가 나오네 
인터페이스의 경계(map)이 숨겨지고, 타입 캐스팅과 타입 관리는 Sensor Class에서 하게된다는 말이네

그러니깐 standard API의 있는 그대로의 기능으로 사용에 대한 책임을 클라이언트가 지게 하는게 아니고,
클래서에서 관리를 하도록..

이 interface는 어플리케이션의 니즈에 맞게 조정되고 제한이 된다 이해가 쉽고 오용을 방지할 수 있다.
그리고 Sensor클래스를 이용해서 비지니스 룰과 디자인을 적용할 수 있다.
즉 Map과 같은  a boundary interface를 사용할 때는 클래스 내부에서 해결하거나 폐쇠적으로 내부에서
활용을 하되,  API에서 반환하거나 argument로 활용하지는 말라.

### Exploring and Learning Boundaries

third-party code를 배우는 것은 어렵다
third-party code를 통합하는 것도 어렵다
배우고 통합하는 것은 두배로 어렵다.

다른 접근법?

the third-party code를 이해하기 위해 테스트를 작성해서 접하는 방법 이를 learning tests라 부른다.
third-party API에 대한 어플리케이션의 의도를 확인하도록 통제되는 실험을 하게된다.


### Learning log4j

> 테스트 1
진행 : 간단히 hello 콘솔에 적어보기
```java
@Test
public void testLogCreate() {
	Logger logger = Logger.getLogger("MyLogger"); 
	logger.info("hello");
}
```
결과 : 에러 -> logger를 통해 Appender라는 것이 필요하다는 것을 배우고 다음 진행 설정.


> 테스트 2
진행 : 간단히 hello 콘솔에 적어보기 ConsoleAppender를 이용해서
```java
@Test
public void testLogAddAppender() {
	Logger logger = Logger.getLogger("MyLogger"); 
	ConsoleAppender appender = new ConsoleAppender(); 
	logger.addAppender(appender);
	logger.info("hello"); 
}
```
결과 : Appender에 출력 스트림이 없다는 것을 알게 됨. 구글을 통해 look up후 다시 시도.

> 테스트 3
진행 : 간단히 hello 콘솔에 적어보기 ConsoleAppender를 이용해서 그리고 구글서 찾은것도 더해서
```java
@Test
public void testLogAddAppender() {
	Logger logger = Logger.getLogger("MyLogger"); 
	logger.removeAllAppenders();
	logger.addAppender(new ConsoleAppender( 
	new PatternLayout("%p %t %m%n"), 
	ConsoleAppender.SYSTEM_OUT));
	logger.info("hello"); 
}
```
결과 : 성공

여기서 
ConsoleAppender.SystemOut argument 제거해도 여전히 출력은 되고,
PatternLayout을 빼면 the lack of an output stream 하다고 말한다.
이상한 behavior라 생각되게 되고,

다시 여기서
document를 찾아본다.
ConsoleAppender 가 unconfigured가 디폴트라 하는 여전히 모호하다.

그래서 버그처럼 느껴지는데..

다시 여기서
구글링을 통해 조금씩하면 결국 그 lof4j방식에 대해 많은 것을 알게되고 간단한 단위테스트로 다음과 같이 표현된다 

``` java
//LogTest.java
public class LogTest { 
    private Logger logger;
    @Before
    public void initialize() {
        logger = Logger.getLogger("logger"); 
        logger.removeAllAppenders();
        Logger.getRootLogger().removeAllAppenders(); 
    }
    @Test
    public void basicLogger() { 
        BasicConfigurator.configure(); 
        logger.info("basicLogger"); 
    }
    @Test
    public void addAppenderWithStream() { 
	logger.addAppender(new ConsoleAppender(
	new PatternLayout("%p %t %m%n"), 
	ConsoleAppender.SYSTEM_OUT));
        logger.info("addAppenderWithStream"); 
    }
 @Test
    public void addAppenderWithoutStream() { 
	logger.addAppender(new ConsoleAppender(
	new PatternLayout("%p %t %m%n")));
        logger.info("addAppenderWithoutStream"); 
    }
}

```
이로써 콘솔 로거를 초기화고, 나머지 application와 log4j boundary interface와 분리해서 구현할 수 있게된다.

### Learning Tests Are Better Than Free

learning tests은 결국 비용이 들지 않는다. 
third-party API를 배워야 했고, 그 테스트들을 작성하는 것은 그 지식을 얻기 위한 쉽운 방법이었다. 
learning tests는 우리의 이해를 높이는 데 도움이 되는 정확한 실험이었다.

> new releases of the third-party package
- 동작 차이가 있는지 확인
- 타사 패키지가 예상대로 작동하는지 확인
- 통합된 후 호환된다는 보장 X
- 출시될 때마다 새로운 위험이 발생

즉, 그럴 때마다 보유한 테스트로 발견하고 해결해야한다.

Without these boundary tests to ease the migration, we might be tempted to stay with the old version longer than we should.


### Using Code That Does Not Yet Exist
There is another kind of boundary, one that separates the known from the unknown.

종종 알지못하는 boundary에 부딪히곤 하지만, 

Key the transmitter on the provided frequency and emit an analog representation of the 
data coming from this stream. 

```
//API가 설계되지 않은 상태에서 어떻게 구현될지 모른다. 그래서 세부사항은 나중에 결정하기로 함.
//그래서 Transmitter라는 인터페이스를 정의하고 메소드를 transmit이라 칭하고  frequency and a data stream 취하도록 설정했다
//이것이 현상태에서 갖고자 하는 인터페이스이며, 인터페이스라는 이유로 통제가 가능하는 점.
interface Transmitter 
	+ transmit(frequency, stream)

// 아래 컨트롤러 클래스를 API로 부터 격리가 가능하고 코드를 깨끗하고 표현력있게 표현 가능
class CommunicationsController

//Gof Adapter design structure를 통해 interation을 encapsulat하여 a single place를 제공	
class TransmitterAdapter implements Transmitter 

//CommunicationsController를 테스트 
class FakeTransmitter

//  API의 사용성 테스트
class TransmitterAPI 

```
![PredictingTransmitter](images/PredictingTransmitter.jpg "Predicting Transmitter")


### Clean Boundaries
- 좋은 소프트웨어 디자인은 막대한 투자와 수정없이 변경을 받아드린다. 만약 통제가 어려운 코드를 사용시에는 주의해서 비용이 들지않도록 주의해야한다.
- 코드의 경계를 명확히하고 정의하는 테스트가 필요한데. the third-party에 의존하지않도록 주의 기울여야한다.
- MAP의 사례와 같이 wap하거나 interface를 제공하시는식에 Adapter 패턴을 활용할 수 있다. 코드의 의미전달이 더 낫고 경계상에서 consistent한 사용을 가져가고 fewer maintenance 
points when the third-party code changes.





