# Chapter 5. Formatting

#### The Purpose of Formatting
- Code formatting은 communication이고, communication은 프로페셔널한 개발자의 가장 중요한 덕목이다
- 오늘 쓴 코드의 기능은 다음 release에 변경 될 수 있지만 코드의 가독성은 코드의 업데이트에 지대한 영향을 미친다
- 코딩 스타일과 가독성은 원래의 코드가 아주 많이 업데이트 된 후에도 maintainability와 extensibility에 계속해서 영향을 준다 

### 5-1. Vertical Formatting
- 소스 파일은 얼마나 커야 할까?
- 평균 200줄의 코드, 많게는 500줄의 코드로도 큰 시스템을 만드는게 가능하다 (FitNesse)

#### The Newspaper Metaphor
코드는 신문에 쓰인 article과 같이, 
- 이름(article 제목)은 간단하지만 우리가 맞는 모듈을 보고 있는지 스스로 충분한 설명이 되어야 한다
- 가장 윗 부분에는 내용의 high-level을 보여주어야 한다
- 아래로 내려가면서 읽을 수록 점점 디테일해져야 한다

신문은 아주 많은 짧은 article들과, 적지만 조금 긴 article들, 정말 가끔 한 페이지가 가득 찰 만큼의 글로 이루어져 있다. 이 점이 신문을 유용하게 만들어주는 점이다.

#### Vertical Openness Between Concepts
- 생각의 흐름이 흘러가는 걸 blank line으로 나누어 표현해주자
```java
// With vertical openness(blank lines)
package fitnesse.wikitext.widgets;

import java.util.regex.*;

public class BoldWidget extends ParentWidget {
  public static final String REGEXP = "'''.+?'''";

  private static final Pattern pattern = Pattern.compile("'''(.+?)'''",
    Pattern.MULTILINE + Pattern.DOTALL
  );

  public BoldWidget(ParentWidget parent, String text) throws Exception {
    super(parent);
    Matcher match = pattern.matcher(text);
    match.find();
    addChildWidgets(match.group(1));
  }

  public String render() throws Exception {
    StringBuffer html = new StringBuffer("<b>");
    html.append(childHtml()).append("</b>");
    return html.toString();
  }
}

// Without vertical openness(blank lines)
package fitnesse.wikitext.widgets;
import java.util.regex.*;
public class BoldWidget extends ParentWidget {
  public static final String REGEXP = "'''.+?'''";
  private static final Pattern pattern = Pattern.compile("'''(.+?)'''",
    Pattern.MULTILINE + Pattern.DOTALL);
  public BoldWidget(ParentWidget parent, String text) throws Exception {
    super(parent);
    Matcher match = pattern.matcher(text);
    match.find();
    addChildWidgets(match.group(1));}
  public String render() throws Exception {
    StringBuffer html = new StringBuffer("<b>");
    html.append(childHtml()).append("</b>");
    return html.toString();
  }
}
```

#### Vertical Density
- openness가 생각의 흐름을 나누었다면, vertical density는 가까운 결합을 의미한다
```java
// 쓸모없는 코멘트로 인해서 두 variables의 밀집도가 낮아졌다
public class ReporterConfig {
  /**
  * The class name of the reporter listener
  */
  private String m_className;

  /**
  * The properties of the reporter listener
  */
  private List<Property> m_properties = new ArrayList<Property>();

  public void addProperty(Property property) {
    m_properties.add(property);
  }
}

// 밀집도를 높혀 가독성을 올리자 
public class ReporterConfig { 
  private String m_className;
  private List<Property> m_properties = new ArrayList<Property>();

  public void addProperty(Property property) {
    m_properties.add(property);
  }
}
```

#### Vertical Distance 
- 서로 연관된 코드들은 같은 파일 안에서 가까이 존재해야 한다
- Protected variable 사용을 피해야 하는 이유이기도 하다

#### Variable Declarations
- Variable들은 사용되는 곳 가까이에서 declare되어야 한다
```java
private static void readPreferences() {
  InputStream is = null; // variable declaration
  try {
    is = new FileInputStream(getPreferencesFile());
    ...
  } catch (IOException e) {
    ...
  }
}
```
- 때로 variable은 block의 가장 위쪽이나 loop 바로 직전에 declare 되기도 한다
```java
  ...
  for (XmlTest test : m_suite.getTests()) {
    TestRunner tr = m_runnerFactory.newTestRunner(this, test); // the top of a block
    tr.addListener(m_textReporter);
    ...
  }
  ...
```

#### Instance variables
- C++에서는 scissors rule에 따라 가장 밑 쪽에 instance variable들을 위치시키고, Java에서는 가장 위 쪽에 위치시킨다
- 중요한 것은 instance variable은 모든 사람이 어디서 찾을 수 있는 지 알수 있도록 한 곳에 declare 해야 한다
```java
// Bad example
...
public static Test warning(final String message) {
  ...
}

private static String exceptionToString(Throwable t) {
  ...
}

private String fName; // middle of the file
private Vector<Test> fTests = new Vector<Test>(10);

public TestSuite() {
  ...
}
...
```
#### Dependent Functions
- 한 function이 다른 function을 부른다면, 가능하다면 함께 위 아래로 caller를 위에, callee를 밑에 배치해주는 것이 좋다
- 코드를 읽는 사람들이 자연스럽게 다음 function들을 에상하면서 읽을 수 있도록 도와준다
```java
public class WikiPageResponder implements SecureResponder {
  ...
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    String pageName = getPageNameOrDefault(request, "FrontPage");
    loadPage(pageName, context);
    if (page == null) 
      return notFoundResponse(context, request);
    else 
      return makePageResponse(context);
  }

  private String getPageNameOrDefault(Request request, String defaultPageName) { ... }

  protected void loadPage(String resource, FitNesseContext context) { ... }

  private Response NotFoundResponse(FitNesseContext context, Request request) { ... }

  private SimpleResponse makePageResponse(FitNesseContext context) { ... }
}
```

#### Conceptual Affinity
- 코드가 서로 affinity(관련성)가 높을 수록 vertical distance는 적어야 한다
- Affinity는 function을 직접 부르거나 variable로 사용하는 것 같이 직접적인 연관을 통해 생길 수도 있고
- 여러 function들이 비슷한 operation을 실행하는 것도 affinity가 생기는 원인일 수 있다
```java
public class Assert {
  static public void assertTrue(String message, boolean condition) {
    if (!condition) fail(message);
  }

  static public void assertTrue(boolean condition) {
    assertTrue(null, condition);
  }

  static public void assertFalse(String message, boolean condition) {
    assertTrue(message, !condition);
  }

  static public void assertFalse(boolean condition) {
    assertFalse(null, condition);
  }
}
```
#### Vertical Ordering
- 우리가 신문의 article을 볼 때는 가장 중요한 컨셉이 먼저 오고 밑으로 내려가며 읽을수록 점점 디테일한 이야기가 오는 걸 예상한다
- 코드의 function call dependency도 똑같이 생각하면 된다. Function A가 B를 부르고 B가 C를 부른다면, A가 먼저 오고 그 다음 B, 그리고 C가 오는것이 자연스러운 코드 진행이고, 자연스럽게 구성이 high level 부터 low level로 이루어지게 된다

### 5-2. Horizontal Formatting
- 한 라인에 얼마나 많은 코드를 써야 할까?

#### Horizontal Openness and Density
우리가 horizontal white space를 사용하는 경우는
- 관련있는 것들과 없는 것들을 구분할 때
```java
private void measureLine(String line) { // function과 argument는 관련성이 높기 때문에 이름 뒤에 '(' 전에 white space가 없다
  lineCount++;
  int lineSize = line.length(); // operation과 left, right side는 명백하게 서로 나뉘어져야 한다
  totalChars += lineSize; 
  lineWidthHistogram.addLine(lineSize, lineCount); // arguments들은 각각 다르다
  recordWidestLine(lineSize);
}
```
- operator들의 계산 순서를 명확하게 나타낼 때
```java
public static double root1(double a, double b, double c) {
  double determinant = determinant(a, b, c);
  // 계산순서 
  // 1) -b , +, Math.sqrt(), 2*a 
  // 2) /
  return (-b + Math.sqrt(determinant)) / (2*a); 
}

public static double root2(int a, int b, int c) {
  double determinant = determinant(a, b, c);
  return (-b - Math.sqrt(determinant)) / (2*a);
}

private static double determinant(double a, double b, double c) {
  return b*b - 4*a*c;
}
```

#### Horizontal Alignment
```java
// 아래의 horizontal alignment는 rvalue들만 읽기 십상이다
public class FitNesseExpediter implements ResponseSender {
  private Socket          socket;
  private InputStream     input;
  private OutputStream    output;
  private Request         request;
  private Response        response;
  private FitNesseContext context;
  protected long          requestParsingTimeLimit;
  private long            requestProgress; 
  private long            requestParsingDeadline; 
  private boolean         hasError;

  public FitNesseExpediter(Socket          s, 
                           FitNesseContext context) throws Exception 
  {
    this.context = context;
    socket = s;
    input = s.getInputStream();
    output = s.getOutputStream();
    requestParsingTimeLimit = 10000;
  }
}

// 작가가 생각하는 더 나은 코드
// 하지만 문제는 declaration의 수다. 너무 많기 때문에 다른 클래스로 나누는 게 좋다
public class FitNesseExpediter implements ResponseSender {
  private Socket socket;
  private InputStream input;
  private OutputStream output;
  private Request request;
  private Response response;
  private FitNesseContext context;
  protected long requestParsingTimeLimit;
  private long requestProgress; 
  private long requestParsingDeadline; 
  private boolean hasError;

  public FitNesseExpediter(Socket s, FitNesseContext context) throws Exception 
  {
    this.context = context;
    socket = s;
    input = s.getInputStream();
    output = s.getOutputStream();
    requestParsingTimeLimit = 10000;
  }
}
```
#### Indentation
- 한 파일 안의 class, methods, blocks 등 각각의 hierarchy는 하나의 다른 scope을 나타낸다
- 이 hierarchy를 나타내기 위해 우리는 코드에 indentation을 넣어준다
- Indentation은 우리가 빠르고 쉽게 각기 다른 scope를 볼 수 있게 해준다
- Indentation 없이는 사람은 프로그램을 거의 읽기가 불가능하다

```java
// 코드가 이렇게 쓰여졌다고 상상해보자..
public class FitNesseServer implements SocketServer { private FitNesseContext context; public FitNesseServer
(FitNesseContext context) { this.context = context; } public void serve(Socket s) { serve(s, 10000); } public
 void serve(Socket s, long requestTimeout) { try { FitNesseExpediter sender = new FitNesseExpediter(s, context);
  sender.setRequestParsingTimeLimit(requestTimeout); sender.start(); } catch(Exception e) { e.printStackTrace(); } } }

// 같은 코드 with indentation
public class FitNesseServer implements SocketServer { 
  
  private FitNesseContext context;

  public FitNesseServer(FitNesseContext context) { this.context = context; }

  public void serve(Socket s) { serve(s, 10000); }

  public void serve(Socket s, long requestTimeout) { 
    try { 
      FitNesseExpediter sender = new FitNesseExpediter(s, context); 
      sender.setRequestParsingTimeLimit(requestTimeout); 
      sender.start();
    } catch (Exception e) { 
      e.printStackTrace(); 
    } 
  }
}
```
#### Breaking Indentation
- 짧은 if statements, while loops, 혹은 짧은 functions를 쓰게 되면 우리는 indentation을 사용하지 않으려는 유혹에 빠진다
```java
e.g.
public class CommentWidget extends TextWidget {
  public static final String REGEXP = "^#[^\r\n]*(?:(?:\r\n)|\n|\r)?";

  public CommentWidget(ParentWidget parent, String text){super(parent, text);} 
  public String render() throws Exception {return ""; }
}

// 저자는 이런 경우에 indentation을 넣는 걸 추천한다
public class CommentWidget extends TextWidget {
  public static final String REGEXP = "^#[^\r\n]*(?:(?:\r\n)|\n|\r)?";

  public CommentWidget(ParentWidget parent, String text) {
    super(parent, text);
  } 
  
  public String render() throws Exception {
    return ""; 
  }
}
```

#### Dummy Scopes
- 아래 코드처럼 가끔 while 이나 for statement의 body가 비어있을 떄가 있다(dummy). 저자는 이 것을 웬만하면 쓰지 않으려 하고 어쩔 수 없이 써야 할 경우에는 semicolon을 새로운 줄에 넣어서 indentation을 준다.
```java
while (dis.read(buf, 0, readBufferSize) != -1)
  ;
```

#### Team Rules
- 모든 프로그래들은 자기가 좋아하는 formatting rule이 있다. 하지만 만약 팀 안에서 일을 한다면 무조건 팀의 rule을 따라야 한다


