# Unit Tests
테스트의 목적은 우리의 코드가 우리의 예상되로 움직이는지 확신하기 위해서이다.
테스트 코드를 isolate 시킴으로서, OS나 환경에 구애받지 않고 컨트롤 할 수 있다. 

## The Three Laws of TDD
Test-Driven Development의 핵심은 production code를 짜기전에 test를 짜고, 
그 테스트 코드를 통과 할 수 있는 minimum의 code를 짜는 것에 있다.
이 핵심은 다음과 같은 세 가지 룰을 지킴으로서 확립 할 수 있다.

* You may not write production code until you have written a failing unit test.
* You may not write more of a unit test than is sufficient to fail, and not compiling is failing.
* You may not write more production code than is sufficient to pass the currently failing test.

## Keeping Tests Clean
테스트 코드를 clean하게 관리하는 것 또한 중요하다.
만약 테스트 코드가 clean하지 않다면, 테스트 코드를 수정하는 것이 어렵고 복잡해져, production 코드를 유지하는 비용이 높아지고, 
또한 production 코드의 품질또한 떨어질수 있다.

## Tests Enable the -ilities
clean한 테스트 코드가 있다면, production 코드를 변경하는데 필요한 비용이 줄고, 쉽게 변경이 어떠한 변화를 시스템에 가져오는지 관찰할 수 있다. clean한 테스트 코드는 production 코드의 flexibility를 향상시키고 resuable한 코드로 유지 되는 것을 돕는다.
자동화된 clean한 테스트 코드는 production의 디자인과 architecture를 clean하게 유지시키는 핵심이다.

## Clean Tests
clean한 테스트의 가장 중요한 것은 readability이다.
readable한 테스트는 다음과 같은 요소들로 인해 정해진다: clarity, simplicity 그리고 density of expression

```Java
public void testGetPageHieratchyAsXml() throws Exception
{
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
public void testGetPageHieratchyAsXmlDoesntContainSymbolicLinks()
 throws Exception
{
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
public void testGetDataAsHtml() throws Exception
{
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
```
위의 코드는 `addPage`나 `assertSubString`같은 많은 코드들이 반복되어 있고,
또한 많은 pre-steps로 인해, 테스트가 어떤것을 테스트 하고싶은지 어렵게 만든다.

```Java
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
```
위의 코드는 똑같은 것을 반복하지만, 훨씬더 이해하기 쉽다.
테스트의 모든 부분은 vertical space로 나누어져있고, 많은 부분의 디테일(set up)들이 제거되어 있다.

### Domain-Specific Testing Language
위의 코드는 테스트를 위한 domain-specific language를 어떻게 활용하는지를 보여준다.
이미 시스템을 위한 API들을 사용하는 것이 아닌, 테스트를 위한 function이나 utilities들을 사용하여서, 테스트 코드를 작성하거나 이해를 돕는다.

### A Dual Standard
testing API는 simple, succinct, 그리고 expressive 해야하지만, 꼭 production코드 처럼 효율적인 필요는 없다. 
테스팅에서 사용되는 환경은 production의 환경과 다르고, 요구되는 것도 다르기 때문이다.

```Java
public String getState() {
    String state = "";
    state += heater ? "H" : "h";
    state += blower ? "B" : "b";
    state += cooler ? "C" : "c";
    state += hiTempAlarm ? "H" : "h";
    state += loTempAlarm ? "L" : "l";
    return state;
 }
 ```

 이 코드처럼 효율적이지 못한 코드는 production에서 사용되지 않지만, 이것이 테스트의 목적에 부합한다면 테스트 환경에서는 사용하는데 제한이 되지 않는다.

## One Assert per Test
저자는 모든 test function이 한 개만의 assert statement를 가지는 것을 권장하고 있다.
이렇게 함으로서 엔지니어가 test가 어떤 것을 테스트하는지를 이해하는데 돕는다.

```Java
public void testGetDataAsXml() throws Exception {
    makePageWithContent("TestPageOne", "test page");
 
    submitRequest("TestPageOne", "type:data");
 
    assertResponseIsXML();
    assertResponseContains("test page", "<Test");
}
 ```

위의 코드를 one assert 룰을 적용하면 다음과 같이 refactoring 할 수 있다.
```Java
public void testGetPageHierarchyAsXml() throws Exception {
    givenPages("PageOne", "PageOne.ChildOne", "PageTwo");

    whenRequestIsIssued("root", "type:pages");

    thenResponseShouldBeXML();
 }
 public void testGetPageHierarchyHasRightTags() throws Exception {
    givenPages("PageOne", "PageOne.ChildOne", "PageTwo");

    whenRequestIsIssued("root", "type:pages");

    thenResponseShouldContain(
        "<name>PageOne</name>", "<name>PageTwo</name>", "<name>ChildOne</name>"
    );
 }
```
또한 function의 이름을 given-when-then convention을 사용해서 readability를 증가시켰다.

## Single Concept per Test
더 적절한 룰을 하나의 테스트 케이드가 한 가지의 concept을 가지는 것이다.
```Java
/**
 * Miscellaneous tests for the addMonths() method.
 */
 public void testAddMonths() {
    SerialDate d1 = SerialDate.createInstance(31, 5, 2004);

    SerialDate d2 = SerialDate.addMonths(1, d1);
    assertEquals(30, d2.getDayOfMonth());
    assertEquals(6, d2.getMonth());
    assertEquals(2004, d2.getYYYY());

    SerialDate d3 = SerialDate.addMonths(2, d1);
    assertEquals(31, d3.getDayOfMonth());
    assertEquals(7, d3.getMonth());
    assertEquals(2004, d3.getYYYY());

    SerialDate d4 = SerialDate.addMonths(1, SerialDate.addMonths(1, d1));
    assertEquals(30, d4.getDayOfMonth());
    assertEquals(7, d4.getMonth());
    assertEquals(2004, d4.getYYYY());
 }
 ```
 다음과 같은 `addMonths`를 테스트 할때, 3가지의 경우로 나눠서 테스트 코드를 작성 할 수 있다.
 * 31일 달에서 30일 달로 달을 추가했을때 (31일이 30으로 변경되는지 확인)
 * 31일 달에서 31일 달로 달을 추가했을때 (31일이 변경없이 31일로 유지되는지 확인)
 * 30일 달에서 31일 달로 달을 추가했을때 (30일이 변경없이 30일로 유지되는지 확인)

## F.I.R.S.T
clean test는 다음의 5가지 룰을 지켜야 한다.
* Fast: 테스트는 빨라야 한다. 만약 테스트가 많은 시간을 소요한다면, 테스트를 많이 run하지 않을 것이고, production코드는 rot할 것이다.
* Independent: 테스트는 테스트의 순서에 영향을 미치지 않고, 서로 independent해야 한다.
* Repeatable: 테스트는 isolate한 환경에서 이루어져야 한다. 이 의미는 테스트는 테스트를 execute하는 환경에 상관없이 똑같은 값을 리턴해야 한다.
* Self-Validating: 테스트는 boolean 아웃풋을 가져야한다.(pass or fail) 테스트의 결과를 테스트 안에서 해결해야 하고, 만약 테스트의 결과를 log파일이나 다른 소스등을 통해 알아야 한다면, 그것은 잘못된 테스트 케이스이다.
* Timely: unit 테스트는 production코드 전에 작성되어야, production 코드 또한 testable하게 작성 될 수 있다.
  
## Conclusion
테스트는 production코드의 flexibility, maintainability 그리고 reusability를 보존하고 향상시킨다. 
테스트 코드를 clean하도록 노력하고, 또 testing APIs를 작성해 테스트의 코드가 이해 될 수 있도록 노력하자.