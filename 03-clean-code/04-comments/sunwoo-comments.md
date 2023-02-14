# Comments
코멘트의 사용은 개발자가 코드를 통해서 개발자의 의도를 표현하는 것을 실패 했을 때에 대한 추가적인 보정이다. 코멘트를 사용한다는 것은 실패를 의미한다고 이야기 하고 있다. 그렇기 때문에 코멘트를 사용하기 전에 충분히 코드자체에서 고민을 해야한다. 코드는 상황에 따라 위치가 달라질수 있고, 변화한다. 하지만 이 변화에 따라 코멘트를 관리하는 것은 현실적으로 힘들기 때문에, 남아 있는 코멘트는 읽는 이로 하여금, misinformation을 야기한다.

## Comments Do Not Make Up for Bad Code
코멘트를 적는 케이스 중 하나는, 코드 베이스 자체가 mess 할 때 인데, 잘못된 접근 방법 중 하나가 bad code에 코멘트를 다는 것이다. 코멘트를 적기보다는 refactoring을 통해서, code base를 정리하자.

## Explain Yourself in Code
만약 코드의 의미가 제대로 전달되지 않는 다면, 그것을 function을 통해서 의도를 전달하자
```Java
// Check to see if the employee is eligible for full benefits
if ((employee.flags & HOURLY_FLAG) > 0) && (employee.age > 65) {
    // ...
}

if employee.isEligibleForFullBenefits() {
    // ...
}
```

## Good Comments
### Legal Comments
법적인 이유의 코멘트는 source file 상단의 위치한다.
```Java
//  Created by John McClane on 01/01/2020.
//  Copyright © 2020 Company. All rights reserved.
```
### Informative Comments
상황에 따라 코멘트를 통해서 정보를 전달하는 것은 유용할 수 있지만, 코멘트를 사용하기 이전에 최대한 function의 이름을 통해서 정보를 전달하는것에 노력을 해야한다.

```Java
// Returns an instance of the Responder being tested
protected abstract Responder responderInstace();

protected abstract Response responderBeingTested();
```
### Explaination of Intent
상황에 따라 코멘트를 통해서 decision에 대한 의도를 전달 할 수 있다.
밑의 예제에서는 for loop의 의도를 코멘트로 설명을 하고 있다.
```Java
// This is our best attempt to get a race condition
// by creating large number of threads.
for (int i = 0; i < 25000; i++) {
    WidgetBuilderThread widgetBuilderThread =
        new WidgetBuilderThread(widgetBuilder, text, parent, failFlag);
    Thread thread = new Thread(widgetBuilderThread);
    thread.start();
}
```
### Clarification
상황에 따라 code block의 의미를 readable하게 해석을 하는 코멘트.
차선으로는 code block에 있는 코드를 readable하게 하는 것이 가장 좋지만,
상황에 따라 (ex. 3rd-party library) 코드를 변경하는 것이 힘들다면, 코멘트를 통해서 설명한다.

### Warning of Consequences
다른 프로그래머에게 어떤 코드 블록의 이유(결과)를 설명하는 코멘트.
다음 예시는 많은 시간이 걸리는 테스트를 왜 테스트 세트에서 제외했는지를 설명하는 코멘트이다.
언어에 따라 코멘트가 아닌 annotation등을 통해서 결과를 전달 할 수도 있다.
```Java
// Don't run unless you have some time to kill.
public void _testWithReallyBigFile() throws Exception {
    writeLinesToFile(10000000);
    response.setBody(testFile);
    response.readyToSend(this);
    String responseString = output.toString();
    assertSubString("Content-Length: 1000000000", responseString);
    assertTrue(bytesSent > 1000000000);
}
```

### TODO Comments
가장 이상적인 접근은 TODO를 코드베이스에 남기는 것이 아니라, 그것을 고치는 것이지만,
상황에 따라서, 개발자가 그것을 고치지 못했을때, 아니면 미래의 상황을 예상해서 TODO를 남길수 있다
TODO 코멘트는 `//TODO things to do` 포맷을 통해, IDE에서 쉽게 찾을 수 있도록 한다.

### Amplification
어떠한 코드가 중요하거나, 조심스럽게 다뤄져야 할때, 코멘트를 통해서 이 점을 재차 강조하는 코멘트.

```Java
String listItemContent = match.group(3).trim();
// the trim is real important. It removes the starting
// spaces that could cause the item to be recognized
// as another list.
```

### Javadocs in Public APIs
client에서 접근하는 코드에는 API에 대한 문서를 Javadocs를 통해서 잘 설명해 놓는다.
하지만 다른 코멘트들과 마찬가지로 maintainence를 통해서 Javadocs의 코멘트에 misleading이나 misinformation의 가능성을 배제한다.

## Bad Comments
### Mumbling
만약에 코멘트를 작성해야하는 상황이 온다면, 최대한 의도를 명확하게 전달 할 수 있는 코멘트를 작성하도록 노력한다.
예제의 코멘트를 읽는 사람을 여러가지 궁금증을 가질수 있다.
ex. all defaults는 어떤 것 들인지?
ex. all defaults는 누가 로드한것인지?
```Java
public void loadProperties(String propertiesLocation) {
    try {
        String propertiesPath = propertiesLocation + "/" + PROPERTIES_FILE;
        FileInputStream propertiesStream = new FileInputStream(propertiesPath);
        loadedProperties.load(propertiesStream);
    } catch(IOException e) {
        // No properties files means all defaults are loaded
    }
}
```

### Redundant Comments
코멘트는 코드가 설명하지 않은 추가적인 정보를 제공해야 한다.
만약 코멘트가 코드의 의도나 의견을 rephrase하고 있다면, 그것은 불필요한 정보이다.
```Java
// Utility method that returns when this.closed is true. Throws an exception
// if the timeout is reached.
public synchronized void waitForClose(final long timeoutMillis) throws Exception {
    if(!closed)
    {
        wait(timeoutMillis);
        if(!closed)
            throw new Exception("MockResponseSender could not be closed");
    }
}
```

```Java
/** The child Containers belonging to this Container, keyed by name. */
protected HashMap children = new HashMap();

/** The processor delay for this component. */
protected int backgroundProcessorDelay = -1;
```

### Misleading Comments
코드의 의도를 잘못 설명하는 코멘트.
변화하는 코드에 맞춰 코멘트를 maintainence하는 것은 힘든일이다.

### Mandated Comments
회사나 조직에 따라 각 변수나 함수에 코멘트를 필수적으로 다는 것을 룰로 하는 것은 좋지 않다.
또한, 의무적으로 작성하는 코멘트는 코드와 동떨어지거나 혼란을 야기할수 있다.
```Java
/**
 * @param title The title of the CD
 * @param author The author of the CD
 * @param tracks The number of tracks on the CD
 * @param durationInMinutes The duration of the CD in minutes
 */
public void addCD(String title,
                  String author,
                  int tracks,
                  int durationInMinutes) {
    // ...
}
```

### Journal Comments
예전 version control을 로컬에서 해야할때에는 누가 어떤 코드를 변경했는지를 소스코드 안에 포함시켜야 했지만,
지금은 journal comments가 하는 일을 version control을 통해서 관리하기 때문에 더이상 필요하지 않은 코멘트 이다.

```Java
/**
 * Changes (from 11-Oct-2001)
 * --------------------------
 * 11-Oct-2001 : Re-organised the class and moved it to new package com.jrefinery.date (DG);
 * 05-Nov-2001 : Added a getDescription() method, and eliminated NotableDate class (DG);
 * 12-Nov-2001 : IBD requires setDescription() method, now that NotableDate class is gone (DG); Changed getPreviousDayOfWeek(), getFollowingDayOfWeek() and getNearestDayOfWeek() to correct bugs (DG);
 * 05-Dec-2001 : Fixed bug in SpreadsheetDate class (DG);
 */
```

### Noise Comments
어떤 코멘트들은 어떠한 정보를 포함하고 있지 않다.
이러한 코멘트는 명백한 정보를 restate 하고 있을 뿐이고, 추가적인 정보를 제공하지 않는다.
```Java
/**
 * Default constructor.
 */
protected AnnualDateRule() {
}
```

### Use Descriptive Function and Variable instead of Comments
필요하다면, function에서 변수를 extract하여서 어떠한 일인지 설명을 한다.
```Java
// does the module from the global list <mod>
// depend on the subsystem we are part of?
if (smodule.getDependSubsystems().contains(subSysMod.getSubSystem())) {
    // ...
}
```

```Java
ArrayList<Module> moduleDependees = smodule.getDependSubsystems();
Module ourSubSystem = subSysMod.getSubSystem();
if (moduleDependees.contains(ourSubSystem)) {
    // ...
}
```

### Position Markers
한 소스코드 안에 있는 코드를 분리해놓는 코멘트.
특별한 경우에는 도움이 될 수 있지만, 너무 많거나 무분별하게 사용된 position marker는 코드에 noise를 일으킨다.
```Java
// ------------------------------ Instance Variables
```

### Closing Brace Comments
closing braces에 넣는 코멘트.
만약 function의 길이가 길거나, deeply nested되어 있는 경우라면 고려해볼수 있지만,
더욱 중요한것은 이 function을 refactoring을 통해서 분명하게 하는 것이다.
```Java
if ((result < 1) || (result > 12)) {
    for (int i = 0; i < monthNames.length; i++) {
        if (s.equals(shortMonthNames[i])) {
            result = i + 1;
            break;
        } // if
        if (s.equals(monthNames[i])) {
            result = i + 1;
            break;
        } // if
    } // for
} // if
```

### Attribution and Bylines
누가 이것을 작성했는지, 언제 작성했는지 등의 책임자에 대한 정보는 source code control system을 통해서 관리한다.
```Java
/* Added by Rick */
```

### Commented-Out Code
코멘트 처리된 코드를 남겨 놓는 것은, 어떤 목적으로 남겨놓았는지 알수 없기때문에 다른 개발자가 봤을때는 정리하기 힘들고 불쾌하다.

```Java
InputStreamResponse response = new InputStreamResponse();
response.setBody(formatter.getResultStream(), formatter.getByteCount());
// InputStream resultStream = formatter.getResultStream();
// StreamReader reader = new StreamReader(resultsStream);
// response.setContent(reader.read(formatter.getByteCount()));
```

### HTML Comments
HTML관한 소스코드 코멘트는 읽기 힘들고, 만약 HTML의 관한 정보를 포함해야 한다면 그것은 코멘트가 아니라 IDE에서 사용되너야 한다.
```Java
/**
* Task to run fit tests.
* This task runs fitnesse tests and publishes the results.
* <p/>
* <pre>
* Usage:
* &lt;taskdef name=&quot;execute-fitnesse-tests&quot;
* classname=&quot;fitnesse.ant.ExecuteFitnesseTestsTask&quot;
* classpathref=&quot;classpath&quot; /&gt;
* OR
* &lt;taskdef classpathref=&quot;classpath&quot;
* resource=&quot;tasks.properties&quot; /&gt;
* <p/>
* &lt;execute-fitnesse-tests
* suitepage=&quot;FitNesse.SuiteAcceptanceTests&quot;
* fitnesseport=&quot;8082&quot;
* resultsdir=&quot;${results.dir}&quot;
* resultshtmlpage=&quot;fit-results.html&quot;
* classpathref=&quot;classpath&quot; /&gt;
* </pre>
*/
```
### Nonlocal Information
코멘트는 코멘트 근처에 있는 코드를 설명을 해야한다.
코멘트는 systemwide한 정보를 포함하여서는 안되고, scope내의 정보만을 포함하여야만 한다.
```Java
/**
 * Port on which fitnesse would run. Defaults to <b>8082</b>.
 */
public void setFitnessePort(int fitnessePort) {
    this.fitnessePort = fitnessePort;
}
```
### Too Much Information
코멘트는 디테일이나 필요 이상의 정보를 포함해서는 안된다.
```Java
/*
RFC 2045 - Multipurpose Internet Mail Extensions (MIME)
The encoding process represents 24-bit groups of input bits as output
   strings of 4 encoded characters.  Proceeding from left to right, a
   24-bit input group is formed by concatenating 3 8bit input groups.
   These 24 bits are then treated as 4 concatenated 6-bit groups, each
   of which is translated into a single digit in the base64 alphabet.
   When encoding a bit stream via the base64 encoding, the bit stream
   must be presumed to be ordered with the most-significant-bit first.
   That is, the first bit in the stream will be the high-order bit in
   the first 8bit byte, and the eighth bit will be the low-order bit in
   the first 8bit byte, and so on.
*/
```

### Inobvious Connection
만약 코멘트를 이해하기 위해서 코드를 읽어야 한다면, 그것은 코드와 코멘트의 연결성이 부족하기 때문이다.
코멘트를 작성할때, 어떠한 단어가 어떤것을 설명하는지는 분명하게 해야한다.

### Function Headers
Function Header로 function의 기능을 설명하기보다는 function을 작게 분리시킴으로서,
function의 이름으로 function header를 대신하게 한다.

### Javadocs in Nonpublic Code
javadocs는 internal한 것들을 설명하기에는 너무 formal하고 이것은 distraction을 일으킬수 있다.


