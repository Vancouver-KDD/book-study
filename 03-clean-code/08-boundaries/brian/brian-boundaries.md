# Boundaries
- Handling 3rd party codes and unknown code(not exist yet)
## Using Third-Party Code
- Map Methods
  - containsKey(Object key) boolean – Map
  - put(Object key, Object value) Object – Map
  - get(Object key) Object – Map
  - remove(Object key) Object – Map
  - clear() void – Map
  - size() int – Map
  - ...
```java
Sensor s = (Sensor)sensors.get(sensorId );
```
```java
Map<Sensor> sensors = new HashMap<Sensor>();
Sensor s = sensors.get(sensorId );
```
```java
public class Sensors {
    private Map sensors = new HashMap(); 
    
    public Sensor getById(String id) {
        return (Sensor) sensors.get(id); }
    }
    // ...
```
- The interface at the boundary (Map) is hidden
  -  The Sensors class can enforce design and business rules.
- not suggesting that every use of Map be encapsulated in this form
  - not to pass Maps (or any other interface at a boundary) around your system
  - keep it inside the class, or close family of classes if you use a boundary interface
  - avoid returning it from, or accepting it as an argument to, public APIs.

## Exploring and Learning Boundaries
- Third-party code helps us get more functionality delivered in less time.
- Suppose it is not clear how to use our third-party library.
- Learning and integrating the third-party code is hard.
## Learning log4j
### Learning tests
- The tests focus on what we want out of the API.
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
        logger.addAppender(new ConsoleAppender( new PatternLayout("%p %t %m%n")));
        logger.info("addAppenderWithoutStream");
    } 
}
```

## Learning Tests Are Better Than Free
1. writing those tests was an easy and isolated way to get that knowledge
2. When there are new releases of the third-party package, we run the learning tests to see whether there are behavioral differences.
3. Learning tests verify that the third-party packages we are using work the way we expect them to.
   - If the third-party package changes in some way incompatible with our tests, we will find out right away.
   - Without these boundary tests to ease the migration, we might be tempted to stay with the old version longer than we should.

## Using Code That Does Not Yet Exist
- when API have not been designed yet, we defined our own interface
  - this was the interface we wished we had
- Once the API was defined, we wrote an Adapter to bridge the gap
![adapter.png](adapter.png)

## Clean Boundarie
- Code at the boundaries needs 
  - **clear separation** and 
  - **tests that define expectations**.
- We may 
  - **wrap** them as we did with Map, 
- or we may use an 
  - **ADAPTER** to convert from our perfect interface to the provided interface.