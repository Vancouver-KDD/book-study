# Chapter 9. Unit Tests


#### The Three Laws of TDD
- First Law: 실패하는 유닛 테스트를 적기 전까지는 프로덕션 코드를 적지 않는다
- Second Law: 컴파일은 되지만 실행이 실패하는 유닛테스트 이상은 적지 않는다
- Third Law: 현재 실패하는 유닛 테스트를 성공시키는 프로덕션 코드 이상은 적지 않는다

이 세가지 법칙을 적용하면, 프로덕션 코드와 테스트 코드를 함께 적게 될 것이다. 이 코드들은 모든 프로덕션 코드를 테스트할 것이고 프로덕션 코드를 관리하기가 수월해질 것이다.

#### Keeping Tests Clean
- 지저분한 테스트(feat. 더러운 코드)는 테스트 코드를 작성하지 않는 것에 비해 더 나쁘면 나쁘지 절대 좋지 않다. 테스트 코드는 프로덕션 코드가 변경될 때 함께 변경되어야 하는데 지저분할 수록 변경하기가 어렵다
  -> 지저분한 테스트 코드는 관리하기가 힘들어져서 결국 버려지게 되는데, 테스트 코드 없이는 업데이트 된 프로덕션 코드가 제대로 돌아가고 있는 건지 알 길이 없다
  -> 개발자들은 프로덕션 코드를 업데이트 하기가 두려워지고 결국 코드는 테스트 코드 없이 방치된다

- 테스트 코드는 프로덕션 코드만큼이나 중요하다. 테스트 코드를 작성할 때도 프로덕션 코드를 작성할 때처럼 생각하고 디자인하고 깨끗하게 신경써야 한다

#### Tests Enable the -ilities
- 테스트 코드를 깨끗하게 관리하지 않으면 결국 버려질것이고, 테스트 코드 없이는 프로덕션 코드를 작성할 때 유연함을 갖지 못할 것이다
- 결국 프로덕션 코드를 유연하게, 관리 가능하게, 재사용 가능하게 만드는 것은 유닛 테스트이다. 테스트 없는 업데이트는 언제나 버그로 변할 수 있다
- 따라서 개발자가 업데이트를 두려움 없이 할 수 있도록 만들어진 든든한 테스트는 모든 -ilities를 가능하게 만든다 

#### Clean Tests
- 깨끗한 테스트 코드를 만드는 세 가지는 1. 가독성, 2. 가독성, 3. 가독성 이다
- 가독성을 좋게 만드는 방법은, 명확함, 간단함, 간결한 표현이다 -> 우리는 짧은 코드로 최대한 많은 것을 표현해야 한다

아래 코드를 한 번 보자. 
```java
/* 문제점: 
 * 1. 많은 중복
 * 2. 쓸데 없는 코드
 * 3. 낮은 가독성
*/

public void testGetPageHieratchyAsXml() throws Exception {
  crawler.addPage(root, PathParser.parse("PageOne")); 
  crawler.addPage(root, PathParser.parse("PageOne.ChildOne"));
  crawler.addPage(root, PathParser.parse("PageTwo")); 

  request.setResource("root"); 
  request.addInput("type", "pages"); 
  Responder responder = new SerializedPageResponder(); 
  SimpleResponse response = (SimpleResponse) responder.makeResponse(new FitNesseContext(root), request); 
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
  SimpleResponse response = (SimpleResponse) responder.makeResponse(new FitNesseContext(root), request);
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
  SimpleResponse response = (SimpleResponse) responder.makeResponse(new FitNesseContext(root), request); 
  String xml = response.getContent();

  assertEquals("text/xml", response.getContentType()); 
  assertSubString("test page", xml); 
  assertSubString("<Test", xml);
}
```

다음으로 아래 리팩토링 된 코드를 보자. 아래 코드는 Build-Operate-Check 패턴을 이용해 테스트의 구조를 만들었다.
각 테스트는 명확하게 세 부분으로 나눠져서 가독성이 좋아졌고, 필요없는 혹은 중복이었던 코드들을 모두 제거했다. 
간결한, 정말 필요한 정보만을 가짐으로써 누가 읽어도 테스트의 의도를 명확하고 빠르게 이해할 수 있게 되었다.
```java
public void testGetPageHierarchyAsXml() throws Exception { 
  // build
  makePages("PageOne", "PageOne.ChildOne", "PageTwo");
  
  // operate
  submitRequest("root", "type:pages");
  
  // check
  assertResponseIsXML(); 
  assertResponseContains( "<name>PageOne</name>", "<name>PageTwo</name>", "<name>ChildOne</name>" );
}

public void testSymbolicLinksAreNotInXmlPageHierarchy() throws Exception { 
  // build
  WikiPage page = makePage("PageOne"); 
  makePages("PageOne.ChildOne", "PageTwo");

  addLinkTo(page, "PageTwo", "SymPage");

  // operate
  submitRequest("root", "type:pages");

  // check
  assertResponseIsXML(); 
  assertResponseContains( "<name>PageOne</name>", "<name>PageTwo</name>", "<name>ChildOne</name>" ); 
  assertResponseDoesNotContain("SymPage"); 
}

public void testGetDataAsXml() throws Exception { 
  // build
  makePageWithContent("TestPageOne", "test page");

  // operate
  submitRequest("TestPageOne", "type:data");

  // check
  assertResponseIsXML(); 
  assertResponseContains("test page", "<Test");
}
```

#### Domain-Specific Testing Language
- 위의 리팩토링된 코드에서는 테스트를 위해 만들어진 domain-specific language 를 볼 수 있다. API를 직접 사용하기 보다는, API를 사용하는 function이나 utility를 만들어 테스트에 이용함으로써, 테스트를 더 쉽게 작성하고 읽을 수 있게 한다. 우리는 이것들을 testing language(API)라고 한다
- Testing API는 불분명한 디테일에 의해서 지저분해진 테스트 코드를 리팩토링하면서 점점 더 확장된다. 경험이 많은, 좋은 개발자들은 이 테스트 코드를 간결하고 표현력이 풍부한 상태로 계속 리팩토링 해 나갈 것이다.

#### A Dual Standard
- 테스팅 API는 프로덕션 코드와 다른 기준을 가진다. 역시 간결하고, 깔끔하게, 표현력이 있어야 하지만 프로덕션 코드만큼 효용성을 가질 필요는 없다

아래 코드를 보자. 코드를 자세하게 뜯어 보지 않아도, 무엇을 테스트하는 지 알 수 있을 것이다.
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
// 낮은 온도(WAY_TOO_COLD)로 설정한 후 heater, blower, 그리고 low temperature alarm이 켜졌는지 확인하는 코드이다
```
위의 코드를 읽을 때 우리는 state의 이름(e.g. heater, blower, etc)과 assert가 true인지, false인지 확인하려고 왔다 갔다 하며 본다.
이 코드를 아래 처럼 리팩토링한다면 어떨까?

```java
@Test public void turnOnLoTempAlarmAtThreshold() throws Exception {
  wayTooCold(); 
  assertEquals("HBchL", hw.getState()); // 대문자는 켜져 있는 상태이고, 소문자는 꺼져 있는 상태이다
}
```
Method names에 대해 얘기할 때 다룬 Mental Mapping을 위반하지만, 이 상황에서는 적절하게 쓰인 것 같다. 한 번 어떤 의미인지 파악하면 빠르게 결과를 확인할 수 있는 장점이 있다. 밑의 코드들을 보고 얼마나 쉽게 이해할 수 있는지 확인해보자.

```java
@Test
public void turnOnCoolerAndBlowerIfTooHot() throws Exception { 
  tooHot(); 
  assertEquals("hBChl", hw.getState()); 
}

@Test 
public void turnOnHeaterAndBlowerIfTooCold() throws Exception {
  tooCold();
  assertEquals("HBchl", hw.getState()); 
}

@Test 
public void turnOnHiTempAlarmAtThreshold() throws Exception {
  wayTooHot();
  assertEquals("hBCHl", hw.getState()); 
}

@Test 
public void turnOnLoTempAlarmAtThreshold() throws Exception {
  wayTooCold();
  assertEquals("HBchL", hw.getState()); 
}
```

아래는 getState의 코드이다. 더 효율적으로 만들기 위해서는 StringBuffer를 사용해야겠지만 테스트 코드에서 리소스가 제한될 가능성은 매우 낮기 때문에 사용하도록 한다. (프로덕션 코드이더라도 코스트가 작다면 그냥 String을 사용할 것이다)
이처럼, 프로덕션 코드에서는 쓰지 않을 코드라도 테스트 코드에서는 허용가능한 경우를 dual standard라고 한다. 주로 메모리나 CPU 효율성에 관련되서 나타나게 된다. 하지만 코드의 깨끗함에는 전혀 문제가 되지 않는다.
```java
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

#### One Assert per Test
- 한 테스트에 하나의 assertion만 가져야 한다는 건 조금 억지스러워 보이지만, 빠르고 쉽게 이해할 수 있다는 장점이 분명 존재한다
- 그렇다면 가장 위의 첫번째 예시처럼 assertion이 많을 때는 어떻게 해야할까? response가 XML인지 확인하는 것과, substring을 가지고 있는 지 확인하는 두 assertion을 합쳐서 하나의 assertion으로 만든다는 건 억지스러워 보인다. 하지만 우리는 두 assertions을 아래처럼 두 개의 테스트로 나눌 수 있다

```java
public void testGetPageHierarchyAsXml() throws Exception { 
  givenPages("PageOne", "PageOne.ChildOne", "PageTwo");

  whenRequestIsIssued("root", "type:pages");

  thenResponseShouldBeXML();
}

public void testGetPageHierarchyHasRightTags() throws Exception { 
  givenPages("PageOne", "PageOne.ChildOne", "PageTwo");

  whenRequestIsIssued("root", "type:pages");

  thenResponseShouldContain( "<name>PageOne</name>", "<name>PageTwo</name>", "<name>ChildOne</name>" );
}
```
위의 코드에는 given-when-then 방법을 적용했다. 
두 개의 테스트로 나누면서 중복된 코드들이 생겼다. `Template method` 패턴이나 @Before/@Test annotation을 이용해 해결 할 수도 있지만, 이 작은 이슈에 너무 많은 코스트가 들기 때문에 이런 경우에는 원래의 코드처럼 여러개의 assertion을 넣는 걸 선호한다.

하나의 assertion은 좋은 가이드라인이고, 그걸 지키기 위해 domain specific testing language를 사용하지만, 하나 이상의 assertion을 넣는 것도 나쁘지 않다고 생각한다. 결국 중요한 건 assertion을 최소화 시키는 것이라고 생각한다.

#### Single Concept per Test
- Single assertion보다 더 나은 규칙은 single concept per test이다
- 아래 코드처럼 길고 여러가지 테스트가 한 곳에 모인 코드들은 무조건 나뉘어져야 한다. 한 곳에 모여 있다면 읽는 사람은 왜 그것들이 모두 한 곳에 모여있는지, 어떤 것이 테스트 되는지 모두 확인해야 한다.
```java
/** 
 * Miscellaneous tests for the addMonths() method.
 */ 
public void testAddMonths() { 
  SerialDate d1 = SerialDate.createInstance(31, 5, 2004);

  // 31이 마지막인 달에 1 month를 추가 했을때 June이기 때문에 31이 아니고 30을 리턴해야 한다
  SerialDate d2 = SerialDate.addMonths(1, d1); 
  assertEquals(30, d2.getDayOfMonth()); 
  assertEquals(6, d2.getMonth()); 
  assertEquals(2004, d2.getYYYY());

  // 31이 마지막인 달에 2 months를 추가 했을때 July이기 떄문에 31을 리턴해야 한다
  SerialDate d3 = SerialDate.addMonths(2, d1); 
  assertEquals(31, d3.getDayOfMonth()); 
  assertEquals(7, d3.getMonth()); 
  assertEquals(2004, d3.getYYYY());

  // 30이 마지막인 달에 1 month를 추가 했을때 July이기 떄문에 30을 리턴해야 한다
  SerialDate d4 = SerialDate.addMonths(1, SerialDate.addMonths(1, d1)); 
  assertEquals(30, d4.getDayOfMonth()); 
  assertEquals(7, d4.getMonth()); 
  assertEquals(2004, d4.getYYYY());
}

// 또한 이 테스트는 28일이 끝인 2월에 대한 테스트가 빠져있다
```
결론적으로, assert가 많아서 테스트가 문제가 되는 것이 아니라 여러가지의 concept들이 한 곳에 모여 있는 것이 문제다.
그렇기 때문에 가장 좋은 방법은 concept마다 최소한의 assert를 사용하고 한 test에 하나의 concept만 가질 수 있도록 하는 것이다.


#### F.I.R.S.T
- Fast: 테스트는 빠르게 동작해야한다. 테스트가 느리게 동작하면 자주 돌리고 싶지 않아지고, 그렇다면 문제를 더 빨리 찾아낼 수 없게 된다
- Independent: 테스트는 다른 테스트들에 의존해서는 안된다. 각각의 테스트는 각각의 영역에서만 동작해야 한다. 만약 의존성이 있다면 한 테스트의 실패가 다른 테스트들의 실패로 이어지고, 디버깅하기도 어려워진다
- Repeatable: 테스트는 어떤 환경에서도 반복적으로 실행 가능해야 한다. 그렇지 않다면 항상 실패하는 이유을 찾게 될 것이고, 준비 되지 않은 환경에서는 테스트를 돌릴 수 없게 된다
- Self-Validating: 테스트는 성공 혹은 실패를 나타내는 boolean output을 가져야 한다. 테스트가 self-validating하지 않으면 실패는 주관적이 되고, 테스트를 돌릴 때 수동적 validation에 많은 시간을 쏟게 된다
- Timely: 테스트 코드는 적절한 때에 작성되어야 한다. 유닛테스트는 프로덕션코드가 적히기 바로 직전에 적어야 한다. 프로덕션 코드 이후에 적게 되면 테스트하기가 어려워질 수 있고, 혹은 프로덕션 코드가 테스트하기 불가능하게 디자인 될 수도 있다

#### Conclusion
테스트가 썩게 놓아 두면, 코드도 썩어들어갈 것이다. 테스트 코드를 꺠끗하게 유지하자.