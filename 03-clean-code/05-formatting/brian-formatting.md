# Formatting
## 1. The Purpose of Formatting
Code formatting is about communication, and communication is the professional developer’s first order of business.

## 2. Vertical Formatting
Vertical size = Class size = File size
- Ex) FitNesse poject
    - Average file size = 65 lines
    - 1/3 files -> between 40 and 100+ lines 
    - min 6 lines <-> max 400 lines
- In the example projects
    - Most of the files are less than 200 lines
    - None are over 500 lines

### 2.1 The Newspaper Metaphor
- 신문처럼 중요한것부터 상세한 사항으로 코드를 작성해야한다.
### 2.2 Vertical Openness Between Concepts
```java
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
```
```java
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
        addChildWidgets(match.group(1));
    }   
    public String render() throws Exception {     
        StringBuffer html = new StringBuffer("<b>");     
        html.append(childHtml()).append("</b>");     
        return html.toString();  
    } 
}
```

### 2.3 Vertical Density
```java
public class ReporterConfig {
    private String m_className;
    private List<Property> m_properties = new ArrayList<Property>();

    public void addProperty(Property property) {
        m_properties.add(property);
    }
}
```
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
}
```
### 2.4 Vertical Distance
- Concepts that are closely related should be kept vertically close to each other.
- => Protected variables should be avoided
#### 2.4.1 Variable Declarations 
- Variables should be declared as close to their usage as possible
#### 2.4.2 Instance variables
- ,on the other hand, should be declared at the top of the class
#### 2.4.3 Dependent Functions
- If one function calls another, they should be vertically close, 
- and the caller should be **above the callee**, if at all possible.
#### 2.4.4 Conceptual Affinity
```java
public class Assert {   
    static public void assertTrue(String message, boolean condition) {
        if (!condition)
            file(message);
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
- Even if they didn’t call each other, they would still want to be close together

### 2.5 Vertical Ordering
- function call dependencies to point in the downward direction
- from high level to low level

## 3. Horizontal Formatting
- 45 characters regularly
  - 123456789_123456789_123456789_123456789_12345
- 80 char limit VS 120 char limit (wide monitor & small font)
  - 123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_
  - 123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_
### 3.1 Horizontal Openness and Density
```java
private void measureLine(String line) {
    lineCount++;
    int lineSize = line.length(); // ..Size = line... <- sapce 
    totalChars += lineSize; // ...Cars += line... <- sapce
    lineWidthHistogram.addLine(lineSize,lineCount); // ...Line( <- no space
    recordWidestLine(lineSize);
}
```
### 3.2 Horizontal Alignment
```java
// this kind of alignment is not useful
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
```
### 3.3 Indentation
```java
public class FitNesseServer implements SocketServer { private FitNesseContext
context; public FitNesseServer(FitNesseContext context) { this.context =
context; } public void serve(Socket s) { serve(s, 10000); } public void
serve(Socket s, long requestTimeout) { try { FitNesseExpediter sender = new
FitNesseExpediter(s, context);
sender.setRequestParsingTimeLimit(requestTimeout); sender.start(); }
catch(Exception e) { e.printStackTrace(); } } }
```
#### 3.3.1 Breaking Indentation
```java
public class CommentWidget extends TextWidget
{
    public static final String REGEXP = "^#[^\r\n]*(?:(?:\r\n)|\n|\r)?"; 
    public CommentWidget(ParentWidget parent, String text) {super(parent, text);} 
    public String render() throws Exception {return ""; }
}
```
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
### 3.4 Dummy Scopes
```java
while(dis.read(buf, 0, readBufferSize) != -1);
```
```java
while(dis.read(buf, 0, readBufferSize) != -1)
    ;
```
## 4. Team Rules
- A team of developers should agree upon a single formatting style, and then every member of that team should use that style
