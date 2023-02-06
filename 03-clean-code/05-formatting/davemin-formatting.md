## 5. Formatting

### The Purpose of Formatting

코드 포맷 == 의사소통 == 최우선 Task

Professional developer은 돌아가게 하는 것이란 오해는 떨쳐버려야 한다.

Coding style과 Readability는 유지보수성과 확장성에 지속적을 영향을 미치는 중요한 주제이다.

### Vertical Formatting

코드가 수직적으로 길어지는 부분을 보게되면, 

Junit, FitNesse, Time and Money
-> around 200 lines

Tomcat and Ant
-> thousands of lines?

즉, 200줄 정도의 파일이면 중요한 시스템이 구축가능한 것으로 보여진다.


### The Newspaper Metaphor

신문기사도 세로로 읽는 것을 생각해보자.

맨위에서 헤드라인으로 읽을 지 결정 할 수 있게 정보를 제공하고, 첫단락에서 개요를 제공하고 세부정보를 위한 코드로 코드줄이 증가하게 된다.
이를 코드 걔념에 적용해보자 신문 기사와 같은 소스  파일
-  이름은 간단하지만 설명이 가능하게
- 이름만으로 올바른 모듈에 있는지 확인
- 세부정보가 아래로 갈수록 더 많아져야한다. 


### Vertical Openness Between Concepts

왼쪽에서 오른쪽 그리고 위에서 아래로 읽히는 포맷을 유지하면서, 사고별로 그룹화하고 분리하여서 시각적으로 레이아웃을 유지하여야한다. 
빈줄이 주는 시각적큐를 제거하면 가독성이 현저히 줄어들고 초점을 흐리는 것을 아래 두개의 예를 보면 확연하게 차이가 보인다.

```java

package fitnesse.wikitext.widgets;
import java.util.regex.*;
public class BoldWidget extends ParentWidget {
    public static final String REGEXP = "'''.+?'''";
    private static final Pattern pattern = Pattern.compile("'''(.+?)'''", Pattern.MULTILINE + Pattern.DOTALL);
    
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
//================================================================================
package fitnesse.wikitext.widgets;
import java.util.regex.*;
public class BoldWidget extends ParentWidget {
    public static final String REGEXP = "'''.+?'''";
    private static final Pattern pattern = Pattern.compile("'''(.+?)'''", Pattern.MULTILINE + Pattern.DOTALL);
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

```

### Vertical Density
위에서 본 개방성이 주제와 걔념을 분리를 하는 것이라면, 수직적인 density는 tightly related topic를 의미한다. 아래의 예제를 보면, useless한 주석으로 인해 연관성이 깨지는 것을 알 수 있다.
```java
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
```

아래와 같이 나열함이 훨씬 올바른것을 알 수 있다.왜냐면 분명히 내 머리와 눈을 덜 움직이도록 도움을 줌이 명확하기 때문이다.
```java
public class ReporterConfig {
	private String m_className;
	private List<Property> m_properties = new ArrayList<Property>();
	public void addProperty(Property property) {
		m_properties.add(property);
	}
```

### Vertical Distance
하나의 펑션에서 다른 펑션으로 이동하고 소스파일을 위아래로 스크롤하기도하고 펑션의 관련된 operate을 알아내려고 노력하지만 혼란스러운 적이 분명히 있을 겁니다. 물론 이해하려고 노력하는 것이지만, your time과 mental energy를 소모하고 잇다는 것에 답답함을 느낄 수 있을 겁니다.

해서, 밀접하게 관련된 걔념들은 vertically 가깝게 유지되어져야합니다.

물론! file serperation에 해당되지는 핞습니다. 하지만 밀접하게 관련된 걔념을 다른 파일로 분리해서는 안된다.
그래서 protected type의 variable을 피해야 하는 이유이다.

#### (1)Variable Declarations
변수는 가능한 그의 사용과 가깝게 선언되어져야 합니다.
아래와 같이 로컬변수가 함수 바로 위에 보여져야합니다. 
```java
private static void readPreferences() {
	InputStream is= null; <<--
	try {
		is= new FileInputStream(getPreferencesFile());
		setPreferences(new Properties(getPreferences()));
		getPreferences().load(is);
	} catch (IOException e) {
		try {
			if (is != null)
			is.close();
		} catch (IOException e1) {
		}
	}
}
```
다음의 예시가 보여주는 것은, 루프의 대한 제어변수는 루프내에 선언되어져야합니다. 
```java
public int countTestCases() {
	int count= 0;
	for (Test each : tests) // <--
		count += each.countTestCases();
	return count;
}
```

드물게 변수는 블록의 맨 위에 선언되거나 긴 함수의 루프 직전에 선언될 수 있다.
```java
for (XmlTest test : m_suite.getTests()) {
	TestRunner tr = m_runnerFactory.newTestRunner(this, test); <<--
	tr.addListener(m_textReporter);
	m_testRunners.add(tr);
	
	invoker = tr.getInvoker();
	
	for (ITestNGMethod m : tr.getBeforeSuiteMethods()) {
		beforeSuiteMethods.put(m.getMethod(), m);
	}
	for (ITestNGMethod m : tr.getAfterSuiteMethods()) {
		afterSuiteMethods.put(m.getMethod(), m);
	}
}
```

#### (2)Instance variables

클래스 내에서 인스턴스 변수는 맨위에 선언이 되어야 합니다. 클래스내의 이런 변수끼리의 상하거리를 만들어서는 안됩니다 왜냐면 클래스내의 메소드가 이더라도 많은 메소드에서 사용이 되어질수있기 때문입니다. 
C++에서는 instance가 제일 하단에 두는 규칙이 있지만 일반적으로 Java의 일반적인 관습은 클래스의 최상위에 두는 것이다.
특별한 관습이라기 보다는 잘알려진 위치?에 두어져야 한다. 

아래의 케이스는 인스턴스를 중간에 숨겨 우연히 발견해야하는 어려운것에 대한 케이스이다.

```java
public class TestSuite implements Test {
	static public Test createTest(Class<? extends TestCase> theClass, String name) {
		...
	}
	public static Constructor<? extends TestCase>
	getTestConstructor(Class<? extends TestCase> theClass)
	throws NoSuchMethodException {
		...
	}
	public static Test warning(final String message) {
		...
	}
	private static String exceptionToString(Throwable t) {
		...
	}
	
	private String fName; <<--
	
	private Vector<Test> fTests= new Vector<Test>(10); <<--
	
	public TestSuite() {
	}
	public TestSuite(final Class<? extends TestCase> theClass) {
		...
	}
	public TestSuite(Class<? extends TestCase> theClass, String name) {
	...
	}
	... ... ... ... ...
}

```
#### (3)Dependent Functions
특히 디펜던트 펑션 즉 종속함수의 경우, 이들은 수직적으로 가까워야하고 가능하면 caller는 callee위에 위치해야한다. 규칙성을 유지하면, 리더들의 가독성을 패턴에 의해 상향 시킬 수 있다.

아래 예를 보고 위의 펑션이 아래 펑션을 어떻게 호출하고 어떻게 호출된 펑션을 쉽게 찾을 수 있는지 그래서 전체 module의 readability를 어떻게 향상시키는지 알 수 있다. 


```java
public class WikiPageResponder implements SecureResponder {
	protected WikiPage page;
	protected PageData pageData;
	protected String pageTitle;
	protected Request request;
	protected PageCrawler crawler;
	
	public Response makeResponse(FitNesseContext context, Request request)
		throws Exception {
		String pageName = getPageNameOrDefault(request, "FrontPage");
		loadPage(pageName, context);
		if (page == null)
		return notFoundResponse(context, request);
		else
		return makePageResponse(context);
	}
	private String getPageNameOrDefault(Request request, String defaultPageName)
	{
		String pageName = request.getResource();
		if (StringUtil.isBlank(pageName))
		pageName = defaultPageName;
		return pageName;
	}
	protected void loadPage(String resource, FitNesseContext context)
		throws Exception {
		WikiPagePath path = PathParser.parse(resource);
		crawler = context.root.getPageCrawler();
		crawler.setDeadEndStrategy(new VirtualEnabledPageCrawler());
		page = crawler.getPage(context.root, path);
		if (page != null)
		pageData = page.getData();
	}
	private Response notFoundResponse(FitNesseContext context, Request request)
		throws Exception {
		return new NotFoundResponder().makeResponse(context, request);
	}
	private SimpleResponse makePageResponse(FitNesseContext context)
		throws Exception {
		pageTitle = PathParser.render(crawler.getFullPath(page));
		String html = makeHtml(context);
		SimpleResponse response = new SimpleResponse();
		response.setMaxAge(0);
		response.setContent(html);
		return response;
	}
	...
```

#### (4)Conceptual Affinity
Affinity의 의한 코드 배치는 두가지 개념으로 나뉜다.

- Affinity case A. 서로간의 펑션 콜때문에 의존적인 관계의 기초하여 그룹화되어 수직적으로 거리가 줄어들게 코드가 배치되는 경우

- Affinity case B. 서로간의 호출이 아닌 공통의 네이밍을 갖고 동일한 기본의 작업을 수행을 하는 경우( 예시 아래)

```java
public class Assert {
	static public void assertTrue(String message, boolean condition) {
		if (!condition)
			fail(message);
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
...
```


### Vertical Ordering
일반적으로 펑션의 호출의 종송석으로 vertically의 아래의 위치되기를 원합니다.
이는 신문기사와 유사합니다. 
가장 중요한 개념이 먼저나오고 세부적인 사항들은 마지막에 얻을 것으로 기대합니다.
이를 통해 세부사항을 skim하고 처음 몇가지의 gist(요지)를 통해 원하는 바를 빠르게 찾아 갈 수 있습니다.

## Horizontal Formatting
수평적 서식의 경우, 너비는 얼마가 적정할까요? 이를 위해 일반적인 프로그램을 통해 얻어진 통계치를 확인해 보면, 20자에서 60자사이에서 얻어진  통계치는 각각 1%로 입니다. 그러므로 약 40%에 해당하는 것이 인상적이며, 프로그래머들은 분명히 짧은 줄을 선호합니다.


### Horizontal Openness and Density
보통 수평적인 흰색 공간을 사용하여 강하게 연관된 것들을 관계시키고 좀 덜 연관된 것들을 분리합니다.

```java
  private void measureLine(String line) { 
    lineCount++; 
    int lineSize = line.length(); 
    totalChars += lineSize; 
    lineWidthHistogram.addLine(lineSize, lineCount); 
    recordWidestLine(lineSize); 
  }
```

여기서 assignment operators가 아래와 같이 white space로 둘러쌓인걸 알 수 있습니다. Assignment statements은 왼쪽과 오른쪽이라는 방향에 의한 연산이 달라지는 두가지 뚜렷한 의미가 존재하기 때문입니다.

int lineSize` `=` ` line.length(); 

totalChars` `+=` `lineSize; 

이러한 공간적인 요소가 분리됨을 좀 더 명확하게 보여줍니다.

반면에 펑션명과 시작하는 괄호사이에 공간을 넣지 않았습니다. 이는 그 argument가 해당 펑션에 밀접하게 관계되어져 있다는 것을 표현하기 위함입니다.

 private void `measureLine(String` line){ }
 

```java
public class Quadratic { 
  public static double root1(double a, double b, double c) { 
    double determinant = determinant(a, b, c); 
    return (-b + Math.sqrt(determinant)) / (2*a); 
  } 
  public static double root2(int a, int b, int c) { 
    double determinant = determinant(a, b, c); 
    return (-b - Math.sqrt(determinant)) / (2*a); 
  } 
  private static double determinant(double a, double b, double c) { 
    return b*b - 4*a*c; 
  } 
}
```

또한 위의 예시와 같이, 두개의 arguments를 코마를 통해 명확하게 분리하여 인수를 분리합니다.

public static double root1(`double a, double b, double c`){}

그리고 위 예시에서 주의해서 봐야할 것은 공백의 또 다른 용도가 operators의 우선순위를 강조하는 것이라는 것입니다.

### Horizontal Alignment

어셈블리 언어 C,C++ 개발자의 경력이 있은 후 특정 구조를 유지하기 위해 수평적 정렬을 사용하고 이를 java에도 적용하려고 노력한 결과가 아래와 같았다.

```java
public class FitNesseExpediter implements ResponseSender 
{ 
	private   Socket          socket; 
	private   InputStream     input; 
	private   OutputStream    output; 
	private   Request         request; 
	private   Response        response; 
	private   FitNesseContext context; 
	protected long            requestParsingTimeLimit; 
	private   long            requestProgress; 
	private   long            requestParsingDeadline; 
	private   boolean         hasError; 
	
	public FitNesseExpediter(Socket s, FitNesseContext context) 
		throws Exception { 
		this.context =            context; 
		socket =                  s; 
		input =                   s.getInputStream(); 
		output =                  s.getOutputStream(); 
		requestParsingTimeLimit = 10000; 
	}
```

해당 정렬의 문제점은, 

- variable의 타입을 간과한채, 이름의 목록으로 읽어내려가게됩니다.
- 또한 Assignment statement(할당문)에서는 연산자를 보지않고 값을 읽어내려가게 됩니다.

그래서 저자는 Declaration 선언과 Assignment 할당에 집중하게 됨을 말해줍니다.
즉 예시와 같이 긴목록의 경우 정렬의 부족함이 아닌, 목록의 길이가 더 중요함을 보여줍니다. (아래)

```java
public class FitNesseExpediter implements ResponseSender 
{ 
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
```

### Indentation

A source ﬁle is a hierarchy rather like an outline.
- 파일 전체의 정보
- 파일내의 개별 클래스
- 클래스내의 메서드
- 메서드내의 블록
- 블럭내의 recursiveness의 정보

이러한 단위에 한해, 계층 구조를 가시화 하기이애 우리는 소스코드를 들여쓰기를 하게 됩니다.

Indentation이 없는 코드의 예시...
아래와 같다.
```java
public class FitNesseServer implements SocketServer { private FitNesseContext context; public FitNesseServer(FitNesseContext context) { this.context = context; } public void serve(Socket s) { serve(s, 10000); } public void serve(Socket s, long requestTimeout) { try { FitNesseExpediter sender = new FitNesseExpediter(s, context); 
sender.setRequestParsingTimeLimit(requestTimeout); sender.start(); } 
catch(Exception e) { e.printStackTrace(); } } }
```
그리고 위 예시는 들여쓰기한 경우가 아래
```java
  public FitNesseServer(FitNesseContext context) { 
    this.context = context; 
  } 
  public void serve(Socket s) { 
    serve(s, 10000); 
  } 
  public void serve(Socket s, long requestTimeout) { 
    try { 
      FitNesseExpediter sender = new FitNesseExpediter(s, context); 
      sender.setRequestParsingTimeLimit(requestTimeout); 
      sender.start(); 
    } 
    catch (Exception e) { 
      e.printStackTrace(); 
    } 
  } 
}
```
움푹 들어간 파일의 구조를 빠르게 식별할 수 있었으며, 변수, 생성자, 접근자 및 메서드를 거의 즉시 찾을 수 있습니다. 

#### Breaking Indentation.
개인적으로 해당 저자의 기술이 매우 와닿았습니다. 짧은 if문과 짧은 루프 그리고 짧은 함수에 대해 들여쓰기가 도리어 덜 적합하다는 생각.

근데 저자는 항상 되돌아가서 리팩토링해준다고 하네요

```java
public class CommentWidget extends TextWidget 
{ 
    public static final String REGEXP = "^#[^\r\n]*(?:(?:\r\n)|\n|\r)?"; 
    public CommentWidget(ParentWidget parent, String text){super(parent, text);} public String render() throws Exception {return ""; } 
}
```
 따라서 다음과 같이 한 줄로 범위를 축소하는 것은 피합니다.
 
```java
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
### Dummy Scopes
아래와 같이 while문과 for문의 body가 dummy인 경우가 있을 수 있습니다.
이런 구조는 되도록 피해야하고 혼동을 줄여야 합니다.

```
while (dis.read(buf, 0, readBufferSize) != -1) 
  ;
```
## Team Rules
모든 프로그래머들은 자신이 좋아하는 포맷 규칙을 가지고 있지만, 팀이 규칙을 정한다.
개발자 팀은 하나의 형식을 정하고, 팀의 모든 구성원은 해당 형식을 사용해야 합니다. 소프트웨어가 일관된 형식의 format을 갖을 때 올바른 형식이라 생각됩니다. 

## Uncle Bob’s Formatting Rules
Consider this an example of how code makes the best coding standard document

```java
public class CodeAnalyzer implements JavaFileAnalysis { 
  private int lineCount; 
  private int maxLineWidth; 
  private int widestLineNumber; 
  private LineWidthHistogram lineWidthHistogram; 
  private int totalChars; 
  public CodeAnalyzer() { 
    lineWidthHistogram = new LineWidthHistogram(); 
  } 
  
  public static List<File> findJavaFiles(File parentDirectory) { 
    List<File> files = new ArrayList<File>(); 
    findJavaFiles(parentDirectory, files); 
    return files; 
  } 
  
  private static void findJavaFiles(File parentDirectory, List<File> files) {     for (File file : parentDirectory.listFiles()) { 
      if (file.getName().endsWith(".java")) 
        files.add(file); 
      else if (file.isDirectory()) 
        findJavaFiles(file, files); 
    } 
  } 
  
  public void analyzeFile(File javaFile) throws Exception { 
    BufferedReader br = new BufferedReader(new FileReader(javaFile)); 
    String line; 
    while ((line = br.readLine()) != null) 
      measureLine(line); 
  } 
  
  private void measureLine(String line) { 
    lineCount++; 
    int lineSize = line.length(); 
    totalChars += lineSize; 
    lineWidthHistogram.addLine(lineSize, lineCount); 
    recordWidestLine(lineSize); 
  } 
  
  private void recordWidestLine(int lineSize) { 
    if (lineSize > maxLineWidth) { 
      maxLineWidth = lineSize; 
      widestLineNumber = lineCount; 
    } 
  } 
  
  public int getLineCount() { 
    return lineCount; 
  } 
  
  public int getMaxLineWidth() { 
    return maxLineWidth; 
  }
  
  public int getWidestLineNumber() { 
    return widestLineNumber; 
  } 
  
  public LineWidthHistogram getLineWidthHistogram() { 
    return lineWidthHistogram; 
  } 
  
  public double getMeanLineWidth() { 
    return (double)totalChars/lineCount; 
  } 
  
  public int getMedianLineWidth() { 
    Integer[] sortedWidths = getSortedWidths(); 
    int cumulativeLineCount = 0; 
    for (int width : sortedWidths) { 
      cumulativeLineCount += lineCountForWidth(width);       if (cumulativeLineCount > lineCount/2) 
        return width; 
    } 
    throw new Error("Cannot get here"); 
  } 
  
  private int lineCountForWidth(int width) { 
    return lineWidthHistogram.getLinesforWidth(width).size();  
  } 
  
  private Integer[] getSortedWidths() { 
    Set<Integer> widths = lineWidthHistogram.getWidths();     Integer[] sortedWidths = (widths.toArray(new Integer[0]));     Arrays.sort(sortedWidths); 
    return sortedWidths; 
  } 
}  
```
