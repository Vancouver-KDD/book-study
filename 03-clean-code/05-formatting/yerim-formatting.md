# Formatting
### The Purpose of Formatting
- Code formatting is about communication.
- The **professional developer's first order of business** is NOT "getting it working" BUT communication (Code formatting).
- Your style and discipline survives, even though your code does not.

### Vertical Formatting
- How big should a source file be?
- It appears to be possible to build significant systems out of files that are typically **200 lines** long, with an **upper limit of 500**.
- Small files are usually easier to understand than large files are.

### The Newspaper Metaphor
- We would like a source file to be like a newspaper article.
- The name should be **simple but explanatory**.
- The topmost parts of the source file should provide the high-level concept and algorithms.

### Vertical Openness Between Concepts
- Thoughts should be separated from each other with **blank lines**.
  - Before:
  ```java
  package fitness.wikitext.widgets;
  imports java.util.regex.*;
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
      return html.toString(); }
  }
  ```

  - After:
  ```java
  package fitness.wikitext.widgets;

  imports java.util.regex.*;

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
  ```

### Vertical Density
- Lines of code that are tightly related should appear **vertically dense**.
  - Before:
  ```java
  public class ReporterConfig {
    /**
     * The class name of the reporter listener
     */
    private String m_className;

    /**
     * The properties of the reporter listener
     */
    private List<Property> m_properties new ArrayList<Property>()'

    public void addProperty(Property property) {
      m_properties.add(property);
    }
  }
  ```

  - After:
  ```java
  public class ReporterConfig {
    private String m_className;
    private List<Property> m_properties new ArrayList<Property>()'

    public void addProperty(Property property) {
      m_properties.add(property);
    }
  }
  ```

### Vertical Distance
- Concepts that are **closely related** should be kept vertically **close to each other**.
##### Vaariable Declarations
- Variables
```java
private static void readPreferences() {
  InputStream is = null;  // <--
  try {
    is = new FileInputStream(getPreferencesFile());
    setPreferences(new Properties(getPreferences()));
    getPreferences().load(is);
  } catch (IOException e) {
    try {
      if (is != null)
        is.close();
    } catch (IOException e1) {}
  }
}
```
- Control variables for loops
```java
public int countTestCases() {
  int count = 0;
  for (Test each : tests)  // <--
    count += each.countTestCases();
  return count;
}
```
- Variables at the top of a block or just before a loop in a long-ish function.
```java
//...
for (XmlTest test : m_suite.getTests()) {
  TestRunner tr = m_runnerFactory.newTestRunner(this, test);  // <--
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
//...
```

##### Instance variables
- It should be declared **at the top of the class**.
- The important thing is for the instance variables to be declared in **one well-known place**.
  - Before:
  ```java
  public class TestSuite implements Test {
    static public Test createTest(Class<? extends TestCase> theClass, String name) {
      //...
    }

    public static Constructor<? extends TestCase> getTestConstructor(Class<? extends TestCase> theClass) throws NoSuchMethodException {
      //...
    }

    public static Test warning(final String message) {
      //...
    }

    private static String exceptionToString(Throwable t) {
      //...
    }

    private String fName;  // <--

    private Vector<Test> fTests = new Vector<Test>(10);  // <--

    public TestSuite() {
      //...
    }

    public TestSuite(final Class<? extends TestCase> theClass) {
      //...
    }

    public TestSuite(Class<? extends TestCase> theClass, String name) {
      //...
    }

    //...
  }
  ```
  - After:
  ```java
  public class TestSuite implements Test {
    private String fName;
    private Vector<Test> fTests = new Vector<Test>(10);

    static public Test createTest(Class<? extends TestCase> theClass, String name) {
      //...
    }

    public static Constructor<? extends TestCase> getTestConstructor(Class<? extends TestCase> theClass) throws NoSuchMethodException {
      //...
    }

    public static Test warning(final String message) {
      //...
    }

    private static String exceptionToString(Throwable t) {
      //...
    }

    public TestSuite() {
      //...
    }

    public TestSuite(final Class<? extends TestCase> theClass) {
      //...
    }

    public TestSuite(Class<? extends TestCase> theClass, String name) {
      //...
    }

    //...
  }
  ```

##### Dependent Functions
- If one function calls another, they should be vertically close, and the caller should be above the callee, if at all possible.
```java
public class WikiPageResponder implements SecureResponder {
  //...

  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    String pageName = getPageNameOrDefault(request, "FrontPage");
    loadPage(pageName, context);
    //...
  }

  private String getPageNameOrDefault(Request request, String defaultPageName) {
    //...
  }

  protected void loadPage(String resource, FitNesseContext context) throws Exception {
    //...
  }

  //...
}
```

##### Conceptual Affinity
- The stronger that affinity, the less vertical distance there should be between them.
```java
// They share a common naming scheme and perform variations of the same basic task.
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
  //...
}
```

### Vertical Ordering
- A function that is called should be below a function that does the calling.

### Horizontal Formatting
- Author is not opposed to lines edging out to 100 or even 120, but beyond that is probably just careless.

### Horizontal Openness and Density
- Assignment operators with white space
```java
private void measureLine(String line) {
  lineCount++;
  int lineSize = line.length();
  totalCharts += lineSize;
  lineWidthHistogram.addLine(lineSize, lineCount);  //didn't put space between the function names and the opening parenthesis.
  recordWidestLine(lineSize);
}
```
- Precedence of operators.
```java
public class Quadratic {
  public static double root1(double a, double b, double c) {
    double determinant = determinant(a, b, b);
    return (-b + Math.sqrt(determinant)) / (2*a);
  }

  public static double root2(int a , int b, int c) {
    double determinant = determinant(a, b, c);
    return (-b - Math.sqrt(determinant)) / (2*a);
  }

  private static double determinant(double a, double b, double c) {
    return b*b - 4*a*c;
  }
}
```

### Horizontal Alignment
- The alignment seems to emphasize the wrong things (type) and leads my eye away from the true intent(name).
If I have long lists that need to be aligned, the problem is the **length** of the lists, not the lack of alignment.
  - Before:
  ```java
  //...
  private     Socket        socket;
  private     InputStream   input;
  private     OutputStream  output;
  protected   long          requestParsingTimeLimit;
  //...
  ```

  - After:
  ```java
  //...
  private Socket socket;
  private InputStream input;
  private OutputStream output;
  protected long requestParsingTimeLimit;
  //...
  ```

### Indentation
- Programmers scan the left for new method declarations, new variables, and even new classes.
  - Before:
  ```java
  public class FitNesseServer implements SocketServer { private FitNesseContext context; public FitNesseServer(FitNesseContext context) { this.context = context; } public void server(Socket s) { server(s, 10000); } public void serve(Socket s, long requestTimeout) { try { FitNesseExpediter sender = new FitNesseExpediter(s, context); sender.setRequestParsingTimeLimit(requestTimeout); sender.start(); } catch (Exception e) { e.printStackTrace(); } } }
  ```
  - After:
  ```java
  public class FitNesseServer implements SocketServer {
    private FitNesseContext context;

    public FitNesseServer(FitNesseContext context) {
      this.context = context;
    }

    public void server(Socket s) {
      server(s, 10000);
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
##### Breaking Indentation
- short `if` statements, short `while` loops, or short functions.
- Avoid collapsing scopes down to one line
  - Before:
  ```java
  public class CommentWidget extends TextWidget
  {
    public static final String REGEXP = "^#[^\r\n]*(?:(?:\r\n)|\n|\r)?";

    public CommentWidget(ParentWidget parent, String text) {super(parent, text);}
    public String render() throws Exception {return ""; }
  }
  ```
  - After:
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
- Before:
```java
while (dis.read(buf, 0, readBufferSize) != -1);
```
- After
```java
while (dis.read(buf, 0, readBufferSize) != -1)
  ;
```

### Team Rules
- Every programmer has his own favorite formatting rules, but if he works in a team, then the team rules.
- A tea mof developers should agree upon a single formatting style, and then every member of that team should use that style.

> Remember, a good software system is composed of a set of documents that read nicely.
