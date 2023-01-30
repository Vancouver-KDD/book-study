# Chapter 3. Functions

```java
/**
 * HtmlUtil.java
 **/
public static String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
    WikiPage wikiPage = pageData.getWikiPage();
    StringBuffer buffer = new StringBuffer();
    if (pageData.hasAttribute("Test")) {
        if (includeSuiteSetup) {
            WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, wikiPage);
            if (suiteSetup != null) {
                WikiPagePath pagePath = suiteSetup.getPageCrawler().getFullPath(suiteSetup);
                String pagePathName = PathParser.render(pagePath);
                buffer.append("!include -setup .").append(pagePathName).append("\n");
            }
        }
        WikiPage setup = PageCrawlerImpl.getInheritedPage("SetUp", wikiPage);
        if (setup != null) {
            WikiPagePath setupPath = wikiPage.getPageCrawler().getFullPath(setup);
            String setupPathName = PathParser.render(setupPath);
            buffer.append("!include -setup .").append(setupPathName).append("\n");
        }
    }
    buffer.append(pageData.getContent());
    if (pageData.hasAttribute("Test")) {

        WikiPage teardown = PageCrawlerImpl.getInheritedPage("TearDown", wikiPage);
        if (teardown != null) {
            WikiPagePath tearDownPath = wikiPage.getPageCrawler().getFullPath(teardown);
            String tearDownPathName = PathParser.render(tearDownPath);
            buffer.append("\n").append("!include -teardown .").append(tearDownPathName).append("\n");
        }
        if (includeSuiteSetup) {
            WikiPage suiteTeardown = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_TEARDOWN_NAME, wikiPage);
            if (suiteTeardown != null) {
                WikiPagePath pagePath = suiteTeardown.getPageCrawler().getFullPath(suiteTeardown);
                String pagePathName = PathParser.render(pagePath);
                buffer.append("!include -teardown .").append(pagePathName).append("\n");
            }
        }
    }
    pageData.setContent(buffer.toString());
    return pageData.getHtml();
}

/**
 * HtmlUtil.java
 * After refactoring
 **/
public static String renderPageWithSetupsAndTeardowns(PageData pageData, boolean isSuite) throws Exception {
    boolean isTestPage = pageData.hasAttribute("Test");
    if (isTestPage) {
        WikiPage testPage = pageData.getWikiPage();
        StringBuffer newPageContent = new StringBuffer();
        includeSetupPages(testPage, newPageContent, isSuite);
        newPageContent.append(pageData.getContent());
        includeTeardownPages(testPage, newPageContent, isSuite);
        pageData.setContent(newPageContent.toString());
    }
    
    return pageData.getHtml();
}

/**
 * HtmlUtil.java
 * More refactoring
 **/
public static String renderPageWithSetupsAndTeardowns(PageData pageData, boolean isSuite) throws Exception {
    if (isTestPage(pageData)) includeSetupAndTeardownPages(pageData, isSuite);
    return pageData.getHtml();
}
```

#### Small!
- Function의 가장 중요한 규칙은 코드 자체가 작아야 한다
- 두번쨰 중요한 규칙은 그것보다 더 작아야 한다

#### Blocks and Indenting
- if, else, 그리고 while문 안에서는 한 줄
- nested structure을 줄여서 indentation을 한 개 혹은 두 개로 줄이자

`IMO` 굉장히 controversial 한 주제인 것 같다. 현실적으로 가능할 지 모르겠..🫠

#### Do One Thing
- Functions should do one thing. They should do it well. They should do it only
- 같은 레벨의 abstraction만 가질 수 있도록 만들기

#### Section within Functions
- 한 function 안에 여러 section이 존재 한다는 건 한 가지 이상의 일을 한다는 것과 같다

#### One Level of Abstraction per Function
```java
// Example of different abstraction levels in a function
public void example() {
    getHtml(); // high level of abstraction
    String pagePathName = PathParser.render(pagePath); // intermediate level
    String s = aString.append("\n"); // low level
}
```

#### Switch Statements
- Use polymorphism
```java
public Money calculatePay (Employee e) throws InvalidEmployeeType {
    switch (e.type) {
        case COMMISSIONED:
            return calculateCommissionedPay(e);
        case HOURLY:
            return calculateHourlyPay(e);
        case SALARIED:
            return calculateSalariedPay(e);
        default:
            throw new InvalidEmployeeType(e.type);
    }
}
```

문제점1. 코드의 길이가 길다. 새로운 Employee type이 생기면 더 길어진다.
문제점2. 명백하게 한 가지 이상의 기능을 가지고 있다(does one more thing).
문제점3. Single Responsibility Principle을 위반한다(한 가지 이상의 이유로 코드가 변경될 수 있기 때문에)
문제점4. Open Closed Principle을 위반한다(새로운 타입이 새길때마다 코드가 변경되어야 하기 때문에)

#### Function Arguments
- Argument는 function과 다른 레벨의 abstraction이고, 그 순간에(function을 읽을 때) 중요하지 않은 디테일을 알아야 할 것을 강제한다

#### Dyadic Functions
- Dyadic function: 두 개의 arguments를 가진 function
- 자연스러운 cohesion(결합, 연결)이나 순서가 있다면 쓰는 것이 문제가 없다 (e.g. 좌표, new Point(0,0))
- 무조건 쓰지 않아야 하는 건 아니지만 cost가 비싸지고, 다른 로직으로 monads(monadic functions)로 바꿀 수 있는 법이 있다면 사용하는 것이 좋다
(e.g. member method)

#### Argument Objects
- 두 개나 세 개 이상의 arguments가 필요한 function이 있다면, 그 중 분명 class로 묶어야 하는 arguments가 있을 가능성이 크다
e.g.
```java
    Circle makeCircle(double x, double y, double radius);
    Circle makeCircle(Point center, double radius);
```

#### Argument Lists
```java
    String.format("%s worked %.2f hours.", name, hours);
```
- 만약 위의 예처럼 variable arguments가 모두 똑같이 여겨진다면, 그 variable arguments는 List 타입을 가진 single argument와 같다고 할 수 있다
- 따라서 String.format은 dyad 이다
```java
    void monad(Integer... args);
    void dyad(String name, Integer... args);
    void triad(String name, int count, Integer... args);
```

#### Have No Side Effects
- 아래의 checkPassword function은 side effect를 가지고 있다. 무엇일까?
```java
public boolean checkPassword(string userName, String password) {
    User user = UserGateway.findByName(userName);
    if (suer != User.NULL) {
        String codedPhrase = user.getPhraseEncodedByPassword();
        String phrase = cryptographer.decrypt(codedPhrase, password);
        if ("Valid Password".equals(phrase)) {
            Session.initialize();
            return true;
        }
    }
    return false;
}
```
- 맞다. Session.initialize(). 이 function은 session이 initialize되도 괜찮을 때만 사용할 수 있게 된 것이다. 
두 가지 다른 action이 한 module(function)안에서 동시에 묶여있는 것을 temporal coupling이라고 한다. 만약 위 코드처럼 temporal coupling이 존재한다면, function 이름을 제대로 지을 수 있도록 하자. 예를 들면, checkPasswordAndInitializeSession 처럼. 명백하게 `Do one thing`을 위반하고 있기는 하지만..

#### Prefer Exceptions to Returning Error Codes
만약 아래 코드처럼 error code를 반환하는 function을 쓴다면, 반환이 될 때마다 우리는 그 에러를 그 때 그 때 바로 처리해줘야 한다
```java
if (deletePage(page) == E_OK) {
    if (registry.deleteReference(page.name) == E_OK) {
        if (configKeys.deleteKey(page.name.makeKey()) == E_OK) {
            logger.log("page deleted");
        } else {
            logger.log("configKey not deleted");
        }
    } else {
        logger.log("deleteReference from registry failed");
    }
} else {
    logger.log("delete failed");
    return E_ERROR;
}
```
하지만 exception을 쓴다면 에러를 처리하는 코드는 따로 빼낼 수 있다
```java
try {
    deletePage(page);
    registry.deleteReference(page.name);
    configKeys.deleteKey(page.name.makeKey());
} catch (Exception e) {
    logger.log(e.getMessage());
}
```

#### Extract Try/Catch Blocks
```java
public void delete(Page page) {
    try {
        deletePageAndAllReferences(page);
    } catch (Exception e) {
        logError(e);
    }
}

private void deletePageAndAllReferences(Page page) throws Exception {
    deletePage(page);
    registry.deleteReference(page.name);
    configKeys.deleteKey(page.name.makeKey());
}

private void logError(Exception e) {
    logger.log(e.getMessage());
}
```

#### Error Handling Is One Thing
- Function은 한 가지 일만 처리해야 하고, 에러 핸들링도 한 가지 일이다. 따라서 한 function에서 try/catch를 사용하게 되면 try/catch/finally 외 다른 것들은 존재하면 안된다.

#### Don't Repeat Yourself
- Duplication은 소프트웨어에 있어서 악의 근원이다.

#### Conclusion
- Classes are the nouns, functions are the verbs of languages.

