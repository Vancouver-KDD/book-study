# Unit Tests
- Once I got a suite of tests to pass, I would make sure that those tests were convenient to run for anyone else who needed to work with the code. 
- I would ensure that the tests and the code were checked in together into the same source package.

## The Three Laws of TDD
- First Law You may not write production code until you have written a failing unit test.
  - 실패하는 단위 테스트를 작성할 때까지 실제 코드를 작성하지 않는다.
- Second Law You may not write more of a unit test than is sufficient to fail, and not compiling is failing.
  - 컴파일은 실패하지 않으면서 실행이 샐패하는 정도로만 단위 테스트를 작성한다.
- Third Law You may not write more production code than is sufficient to pass the currently failing test.
  - 현재 실패하는 테스트를 통과할 정도로만 실제 코드를 작성한다.

## Keeping Tests Clean
- dirty tests is better than having no tests? -> No!!
- having dirty tests is equivalent to having no tests
- Test code is just as important as production code

### Tests Enable the -ilities
- It is unit tests that keep our code flexible, maintainable, and reusable.
  - =>  you can improve that architecture and design without fear!

## Clean Tests
- Readability, readability, and readability.
  - = clarity, simplicity, and density of expression
- The problems
  1. a terrible amount of duplicate code
  2. details that interfere with the expressiveness of the test


- this code was not designed to be read.
```java
class SerializedPageResponderTest {
    public void testGetPageHieratchyAsXml() throws Exception {
        crawler.addPage(root, PathParser.parse("PageOne"));
        crawler.addPage(root, PathParser.parse("PageOne.ChildOne"));
        crawler.addPage(root, PathParser.parse("PageTwo"));

        request.setResource("root");
        request.addInput("type", "pages");
        Responder responder = new SerializedPageResponder();
        SimpleResponse response =
                (SimpleResponse) responder.makeResponse(
                        new FitNesseContext(root), request);
        String xml = response.getContent();

        assertEquals("text/xml", response.getContentType());
        assertSubString("<name>PageOne</name>", xml);
        assertSubString("<name>PageTwo</name>", xml);
        assertSubString("<name>ChildOne</name>", xml);
    }


    public void testGetPageHieratchyAsXmlDoesntContainSymbolicLinks() throws Exception {
        WikiPage pageOne = crawler.addPage(root, PathParser.parse("PageOne"));
        crawler.addPage(root, PathParser.parse("PageOne.ChildOne"));
        crawler.addPage(root, PathParser.parse("PageTwo"));

        PageData data = pageOne.getData();
        WikiPageProperties properties = data.getProperties();
        WikiPageProperty symLinks = properties.set(SymbolicPage.PROPERTY_NAME);
        symLinks.set("SymPage", "PageTwo");
        pageOne.commit(data);

        request.setResource("root");
        request.addInput("type", "pages");
        Responder responder = new SerializedPageResponder();
        SimpleResponse response =
                (SimpleResponse) responder.makeResponse(
                        new FitNesseContext(root), request);
        String xml = response.getContent();

        assertEquals("text/xml", response.getContentType());
        assertSubString("<name>PageOne</name>", xml);
        assertSubString("<name>PageTwo</name>", xml);
        assertSubString("<name>ChildOne</name>", xml);
        assertNotSubString("SymPage", xml);
    }

    public void testGetDataAsHtml() throws Exception {
        crawler.addPage(root, PathParser.parse("TestPageOne"), "test page");

        request.setResource("TestPageOne");
        request.addInput("type", "data");

        Responder responder = new SerializedPageResponder();
        SimpleResponse response =
                (SimpleResponse) responder.makeResponse(
                        new FitNesseContext(root), request);
        String xml = response.getContent();

        assertEquals("text/xml", response.getContentType());
        assertSubString("test page", xml);
        assertSubString("<Test", xml);
    }
}
```
1. The first part builds up the test data
2. the second part operates on that test data
3. the third part checks that the operation yielded the expected results
```java
class SerializedPageResponderTest {
    public void testGetPageHierarchyAsXml() throws Exception {     
        makePages("PageOne", "PageOne.ChildOne", "PageTwo");
        
        submitRequest("root", "type:pages");
        
        assertResponseIsXML();     
        assertResponseContains(       
                "<name>PageOne</name>", "<name>PageTwo</name>", "<name>ChildOne</name>"     
        );  
    }
    
    public void testSymbolicLinksAreNotInXmlPageHierarchy() throws Exception {     
        WikiPage page = makePage("PageOne");     
        makePages("PageOne.ChildOne", "PageTwo");
        
        addLinkTo(page, "PageTwo", "SymPage");
        
        submitRequest("root", "type:pages");
        
        assertResponseIsXML();     
        assertResponseContains(       
                "<name>PageOne</name>", "<name>PageTwo</name>", "<name>ChildOne</name>"     
        );     
        assertResponseDoesNotContain("SymPage");  
    }

    public void testGetDataAsXml() throws Exception {     
        makePageWithContent("TestPageOne", "test page");
    
        submitRequest("TestPageOne", "type:data");
    
        assertResponseIsXML();     
        assertResponseContains("test page", "<Test");  
    }
}
```
### Domain-Specific Testing Language
```java
    void makePages(String ... pageNames);
    WikiPage makePage(String pageName);
    makePageWithContent(String pageName, String contnet);

    void addLinkTo(WikiPage, String ... pageNames);
    
    void submitRequest(Strig wiki, String wikiType);
    
    void assertResponseIsXML();
    void assertResponseContains(String ... contents);
    void assertResponseDoesNotContain(String ... notContianedContents);
```
### A Dual Standard
- those two environment have very different needs
1. test environment
2. production environment
```java
@Test   
public void turnOnLoTempAlarmAtThreashold() throws Exception {
    hw.setTemp(WAY_TOO_COLD);
    controller.tic();
    assertTrue(hw.heaterState());
    assertTrue(hw.blowerState());
    assertFalse(hw.coolerState());
    assertFalse(hw.hiTempAlarm());
    assertTrue(hw.loTempAlarm());
}
```
```java
MockControlHardware hw;

@Test
public void turnOnLoTempAlarmAtThreshold() throws Exception {
    wayTooCold();
    assertEquals("HBchL", hw.getState());
}
```
```java
class MockControlHardware implements EnvironmentControlHardware {
    EnvironmentControlHardware environmentControlHardware;
    // ...
    public String getState() {
        String state = "";
        state += heater ? "H" : "h";
        state += blower ? "B" : "b";
        state += cooler ? "C" : "c";
        state += hiTempAlarm ? "H" : "h";
        state += loTempAlarm ? "L" : "l";
        return state;
    }
    // ...
} 
```
## One Assert per Test
- quick and easy to understand but it maks a lot of duplicate code
- Therefor, I am not afraid to put more than one assert in a test
### Single Concept per Test
- Multiple asserts are not a problem. 
- Rather it is the problem that more than one concept be tested.

## F.I.R.S.T
- Fast: Run them frequently
- Independent: not depend on each other
- Repeatable: repeatable in any environment
- Self-Validating: pass or fail
- Timely: be written just before the production code