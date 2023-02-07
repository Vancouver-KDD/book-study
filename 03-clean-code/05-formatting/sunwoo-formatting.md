# Formatting
우리가 작성하는 코드는 잘 정돈되어 있어야 하고, 정돈에 필요한 몇 가지의 규칙들이 코드 전체에 적용이 되어 있어야 한다.
만약 프로젝트가 팀 단위로 진행한다면, 팀 전체가 규칙들을 이해하고 따라야만 한다.
Automated formatting tool들을 적극적으로 사용한다면, 코드베이스에 포맷에 필요한 룰들을 쉽게 적용 할 수 있다.

## The Purpose of Formatting
개발자에게 가장 필요한 능력은 커뮤니케이션 능력이고, 코드의 포맷은 커뮤니케이션과 직접적인 연관성이 있다.
코드는 항상 변화하고, 코드의 readability는 그 변화에 맞게 쉽게 변경 할 수 있도록 도와준다.

## Vertical Formatting
Vertical Formatting은 한 개의 소스 파일이(Java의 경우 class) 얼마나 긴지에 대한 기준이다.
작은 사이즈의 소스 파일은 개발자가 코드의 readability를 향상시킬수 있다.
필자는 7개의 프로잭트들로 부터, 소스 파일들이 평균적으로 200라인의 길이로 구성되어 있다고 말한다.
이것이 절대적인 기준이 될수는 없지만, 가이드 라인으로써 충분히 사용 할 수 있다.

### The Newspaper Metaphor
신문은 헤드라인이 가장 위에 위치해 있고, 헤드라인은 이 기사가 어떤 내용인지 가장 추상적인 레벨에서 설명을 한다.
산문의 기사는 밑으로 가면 갈수록, 기사에 대한 디테일이 수반되어 있다.
코드또한 마찬가지이다. 코드의 헤드라인은 파일 이름 혹은 class 이름이다.
파일의 이름은 최소한의 개발자에게 naviagtation을 해주어야 한다.
가장 low level의 디테일은 파일의 가장 밑단에 존재해어야 한다.

### Vertical Openness Between Concepts
코드를 vertical로 나누는 방법 중의 하나는 한 개의 thought을 표현하고 있는 code block을 blank line으로 나누는 것이다.
blank line은 코드의 온전한 thought를 separate하고, 또 읽는 사람으로 하여금 시각적인 layout을 제공한다.

예제의 코드처럼 package, import, method 혹은 field처럼 각각의 component들을 blank line으로 분리함으로써,
각각의 역할을 분명히 할 수 있다.
```Java
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

### Vertical Density
Vertical Density는 관계가 있는 코드들이 가까이 dense하게 있어야 하는 경우이다.

```Java
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

```Java
public class ReporterConfig {
 private String m_className;
 private List<Property> m_properties = new ArrayList<Property>();

 public void addProperty(Property property) {
   m_properties.add(property);
}
```
아래의 예제는 위의 예제와 비교했을때, 
한 눈에 직접적으로 연관이 되어 있는 코드들이 (variable and method) 충분히 dense하게 밀접해어 있어,
한 눈에 코드들이 어떻게 class내에서 활용이 되어지는지 보기 편하다.

### Vertical Distance
혹시 한 클래스내에서 메소드들이 어떻게 chaining되어 있는지 연결되어 있는지 확인을 하기 위해서, 소스 코드내에서 jump를 해본 경험이 있다면,
코드를 이해하기 위해서가 아닌, 어느 코드가 어디에 위치해 있는지에 대해서 시간과 노력을 써본적이 있을 것이다.
이러한 문제점들을 막기 위해서 Vertical distance에서 필자는 개념적으로 연결되어 있는 요소들은 vertical 적으로 가까운 거리에 위치해어야 한다.

#### Variable Declarations
변수들은 사용하는 곳 근처에서 declare 되어야 한다. 만약 우리의 function이 작은 사이즈를 잘 유지하고 있다면 local variable은 function의 최상단에 위치해야 한다. Loop의 control variable들은 loop안에서 declare되거나 적은 경우에 function의 최상단에서 declare된다.

```Java
private static void readPreferences() {
    InputStream is = null;
    try {
        is = new FileInputStream(getPreferencesFiles());
        setPreferences(new Properties(getPreferences()));
        getPreferences().load(is);
    } catch (IOException e) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
        }
    }
}
```
#### Instance Variables
class내에 있는 instance variable들은 한 곳에 위치 모여있어야 한다. (Java의 경우에는 class 최상단)
팀으로 진행되는 프로젝트의 경우 모든 팀원들이 instance에 대한 declaration들을 간단히 찾을 수 있어야 한다.

#### Dependent Functions
class내에 있는 method들 중 어떠한 method 들은 다른 method에 의해 호출이 된다. (private methods)
이러한 경우 그 method들은 vertically 가까워야 하고, 또 호출자는 호출 되는 method들 보다 위에 위치해야 한다. (caller는 callee보다 더 추상적인 개념이다.)

The layers of abstraction in the given example
1. `makeResponse`
2. `getPageNameOrDefault` , `loadPage`
3. `notFoundResponse`, `makePageResponse`
4. `makeHtml`
```Java
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

  // added to make code build
  private String makeHtml(FitNesseContext context) {
    return null;  //TODO: Auto-generated
  }
```

### Conceptual Affinity
만약 method들이 서로 호출하지는 않더라도, 개념적으로 동일하거나 비슷한 개념일 경우에도 그 method들을 vertical적으로 가까운 위치에 위치하게 한다.

### Vertical Ordering
이러한 형식으로 vertical ordering을 할 때, 소스 코드의 top에서부터 bottom으로 high level에서 low level로의 flow가 만들어진다.

## Horizontal Formatting
개발자는 short line의 코드를 선호한다. 한 라인이 120줄이 넘어가는 것은 careless한 코드이다.
코드를 읽는 과정에서 오른쪽으로 스크롤 할 일이 생긴하면 그것은 잘못된 코드이다.

### Horizontal Openness and Density
Vertical formatting과 마찬가지로 strongly related된 코드들은 dense해야 하고 관련이 없는 코드들 사이에는 space가 필요하다.

* Assignment operator - two distinct major elements
```Java
int lineSize = line.length();
```
* Function and its arguments - function name and its arguments are associated, but distinct arguments
```Java
double determinant = determinant(a, b, c)
```
* Precedence of operators - product has to be done before substraction!
```Java
return b*b - 4*a*c
```

### Horizontal Alignment
```Java
{
    this.context =              context;
    socket =                    s;
    input =                     s.getInputStream();
    output =                    s.getOutputStream();
    requestParsingTimeLimit =   10000;
}
```
이러한 형식의 horizontal alignment는 코드의 intent를 이해하는데 개발자를 방해를 한다.
만약 horizontal alignment가 필요한 경우가 있다면, 그것은 그 코드의 길이의 문제이지, 
alignment가 없어서 일어나는 일이 아니다.

## Indentation
코드는 hierachical한 구조를 가지고 있고, 
만약 각각 hierachy에 indentation을 적용하지 않는다면 사람이 읽을 수 없는 코드가 된다.
만약 hierachy에 대해 indentation을 적용한다면 즉각적으로 코드의 구조를 파악 할 수 있다.
```Java
public class FitNesseServer implements SocketServer { private FitNesseContext context; public FitNesseServer 
(FitNesseContext context) {this.context = context; } public void serve(Socket s) {serve(s, 10000); } public 
void serve(Socket s, long requestTimeout) { try { FitNesseExpediter sender = new FitNesseExpediter(s, context); 
sender.setRequestParsingTimeLimit(requestTimeout); sender.start(); } catch(Exception e) { e.printStackTrace(); } } }
```
vs
```Java
public class FitNesseServer implements SocketServer { 
    private FitNesseContext context; 

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
        } catch(Exception e) { 
            e.printStackTrace(); 
        } 
    } 
}
```

### Breaking Indentation
짧은 if statements, loop 혹은 function에 대해서 indentation 룰을 깨는 경우가 있다.
Consistency를 위해 이러한 코드에도 indentation을 적용하자.
```Java
public void serve(Socket s) { serve(s, 10000); }

// vs

public void serve(Socket s) {
        serve(s, 10000); 
    }
```

### Dummy Scopes
만약 loop의 바디가 dummy일 경우에는, body를 indented하고 braces로 감싼다. 혹은 indented된 semi-colon을 적용한다.

## Team Rules
각각의 개발자가 자신이 선호하는 formatting의 방법이 있지만, 만약 팀으로 일하는 경우에는 팀의 룰을 따른다. (consistency)
