# Chapter 8. Boundaries

#### Using Third-Party Code
- interface를 제공하는 쪽과 사용하는 쪽이 항상 다른 의도를 가지고 있다. 이 차이점이 시스템의 boundary를 만들어낸다

예를 들어, java.util.Map을 보자.
우리의 의도가 누구도 map안의 data를 지울수 없게 하는 것이라고 해보자. map의 clear 메소드는 누구나 map의 data를 지울 수 있게 한다. 

아래와 같은 코드는 map에 넣는 알맞은 타입을 관리하는 것은 사용하는 유저가 가지고 있기 때문에 좋지 않은 코드이다.
```java
Map sensors = new HashMap();
Sensor s = (Sensor) sensors.get(sensorId);
```
대신에 아래와 같이 type을 강제함으로써 더 좋은 코드로 만들 수 있다.
```java
Map<Sensor> sensors = new HashMap<Sensor>();
Sensor s = sensors.get(sensorId);
```

하지만 이 방법도, 우리가 필요한 기능보다 더 많은 기능을 제공하는 문제점을 풀지는 못한다.
더 깔끔하게 map을 사용한 방법은 Sensor를 사용하는 유저가 type에 대해 생각하지 않게 하는 것이다.
```java
public class Sensors { 
  private Map sensors = new HashMap();

  public Sensor getById(String id) { 
    return (Sensor) sensors.get(id); 
  }
}
```
이제 map의 boundary를 완벽하게 숨겼다. 해당되는 코드 외에 다른 코드에 어느 정도 영향을 줄 수는 있지만 type을 Sensors 클래스 안에서 관리하기 때문에
type management에 대한 문제는 해결됐다.
Map을 사용할 때마다 이렇게 이용하라는 말은 아니고, class나 가까운 곳에 있는 class를 사용하여 map을 boundary 밖으로 노출시키지 않도록 하자.

#### Exploring and Learning Boundaries
Third-party 코드를 사용하면 적은 시간에 더 많은 기능을 제공할 수 있게 된다. 그 코드를 테스트하는 것은 우리가 해야할 일이 아니지만 적어도 우리가 사용하는 코드에 대해서는 테스트를 하는 것이 바람직하다.
Third-party 코드를 디버깅하고 익히는 것은 어렵다. 그렇다면 사용하기 전에 미리 테스트 코드를 사용해 원하는 결과를 가정한 후에 그 코드를 익히는 것은 어떨까? Jim Newkirk는 이것을 learning tests라고 한다. 
Learning test를 사용하여 우리는 third-party API를 부르고 오직 우리가 원하는 기능에 대해서만 테스트한다.

#### Learning log4j
로깅 기능을 직접 구현하지 않는 대신 log4j를 사용하기로 했다고 가정해보자.
```java
@Test public void testLogCreate() {
  Logger logger = Logger.getLogger("MyLogger");
  logger.info("hello"); 
}
```
테스트 결과에서 Appender가 빠졌다고 나온다.
```java
@Test
public void testLogAddAppender() {
    Logger logger = Logger.getLogger("MyLogger");
    ConsoleAppender appender = new ConsoleAppender();
    logger.addAppender(appender);
    logger.info("hello");
}
```
이번에는 Appender에 output stream이 빠졌다는 것을 알아냈다.
```java
@Test
public void testLogAddAppender() {
    Logger logger = Logger.getLogger("MyLogger");
    logger.removeAppAppenders();
    logger.addAppender(new ConsoleAppender(
        new PatternLayout("%p %t %m%n"), ConsoleAppender.SYSTEM_OUT));
    logger.info("hello");
}
```
이제는 "hello"가 제대로 로깅이 된다. 
하지만 ConsoleAppender.SYSTEM_OUT을 제거해도 제대로 로깅이 된다. 이번에는 PatternLayout을 제거했더니 이제야 output stream이 없다는 에러가 나온다.
구글링, 공식문서 읽기, 테스팅을 통해서 조금 더 살펴보고 나서 log4j가 어떻게 기능하는지 알아낸 후에 몇 개의 간단한 유닛 테스트를 적었다.

```java
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
        logger.addAppender(new ConsoleAppender(new PatternLayout("%p %t %m%n"), ConsoleAppender.SYSTEM_OUT));
        logger.info("addAppenderWithStream");
    }
 
    @Test
    public void addAppenderWithoutStream() {
        logger.addAppender(new ConsoleAppender(new PatternLayout("%p %t %m%n")));
        logger.info("addAppenderWithoutStream");
    }
}
```
이제 간단한 console logger를 어떻게 initialization하는 지 알았고 우리가 필요한 기능만을 사용하면서 우리만의 log4j boundary를 만들어냈다.

#### Learning Tests Are Better Than Free
- Learning tests의 코스트는 0이다. 어짜피 API를 배워야 하고 그 테스트를 작성하는것은 API에 대한 지식을 쌓기 좋은 방법이다
- Learning test는 노력한 만큼 좋은 결과를 준다. API의 새로운 버젼이 release되면 우리는 learning tests를 실행해 다른 점을 찾아낼 수 있다
- Learning tests는 third-party 패키지가 우리가 원하는대로 기능할 수 있도록 확신을 준다
- Learning tests가 필요하던 필요하지 않던, 프로덕션 코드가 작동하는 것과 같은 방법으로 outbound tests를 작성해 명확한 boundary를 설정하는 것이 중요하다

#### Using Code that Does Not Yet Exist
- 다른 종류의 boundary로는 아는 코드와 모르는 코드를 분리하는 것이 있다
- 코드를 작성하다 보면 때로는 현재 내가 작성하는 코드가 어떤 interface들과 결합이 되어 사용이 될 지, 혹은 다른 팀들과 작업을 할 때 제대로 소통이 되지 않아 불분명할 때가 있다. 그럴 때는 implementation을 미루고 interface를 임의로 정의해서 우리의 개발상황을 통제 가능할 수 있도록 만들자. 그 후에 불분명한 코드들이 개발(implement)되었을 때 우리는 우리가 맞춰 작성한 프로그램과 함께 테스트를 하게 되면 더 수월하게 일을 진행할 수 있다.

#### Clean Boundaries
- 좋은 프로그램 디자인은 큰 노력없이 변화에 적응할 수 있다. 우리의 통제를 벗어난 코드를 사용할 때에는 미래의 변화에 대해 너무 많은 노력을 쏟지 않을 수 있도록 주의해야 한다
- Boundary에 존재하는 코드는 명확한 분리와 우리가 원하는 것에 대한 테스트 코드가 반드시 필요하다. 우리의 시스템이 third-party API에 대해 너무 많은 것을 아는 것을 피하고, 우리가 통제할 수 없는 코드 보다는 우리가 통제가능한 코드에 의존하는 것이 좋다
- 우리가 위에서 map을 wrap한것처럼 wrapper를 사용하거나 Adaptor pattern을 이용해 주어진 interface를 우리에게 필요한 interface를 만들 수 있도록 관리하자