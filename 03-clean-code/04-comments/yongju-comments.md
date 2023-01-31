# Chapter 4. Comments

> Don't comment bad code, rewrite it - Biran W.Kernighan and P. J. Plaugher

- Comment의 알맞은 사용 방법은 우리가 코드로 표현하기 **실패**한 것에 대한 것에 대한 한 겹의 방어막일 것이다.
- 내가 comments를 싫어하는 이유는 항상 혹은 의도적이지는 않지만 너무 자주 거짓을 말하기 때문이다. 코드는 항상 바뀌고 진화한다. 그에 비해 코멘트는 항상 그것들을 따라가지 못하거나 꽤 자주 처음 코드로부터 분리되어서 버려지기도 한다
- 정확하지 못한 코멘트는 코멘트가 없는 것 보다 훨씬 나쁘다
- 진실은 오직 코드에만 존재한다. 그래서 코멘트가 가끔씩은 필요하겠지만 우리는 코멘트를 최소화하는 데에 많은 에너지를 쏟을 것이다

#### Comments Do Not Make Up for Bad Code
우리는 나쁜 코드를 썼을 때 흔하게 코멘트를 써야겠다고 생각한다. 아니, 대신에 그 코드를 깨끗하게 리팩토링하는 것이 좋을것이다. 적은 코멘트를 가진 깨끗하고 표현력이 강한 코드가 많은 코멘트를 가진 복잡하고 읽기 힘든 코드 보다 훨씬 우월하다.

### Explain Yourself in Code
아래 두 코드 중에 어떤 것을 더 보고 싶을까?
```java
// Check to see if the employee is eligible for full benefits
if ((employee.flags & HOURLY_FLAG) && (employee.age > 65))
 
OR

if (employee.isEligibleForFullBenefits())
```

#### Good Comments
코멘트가 필요하거나 유용할 때가 있다. 하지만 딱 하나, 좋은 코멘트는 쓰지 않아도 될 코멘트이다.

#### Legal Comments
가끔 회사 코딩 standard는 우리에게 법적 이유로 아래와 같은 특정한 코멘트를 작성하게끔 한다.
```java
// Copyright (C) 2003,2004,2005 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the GNU General Public License version 2 or later.
```
가능하면 다른 문서에 작성할 수 있도록 하자

#### Informative Comments
```java
// Returns an instance of the Responder being tested.
protected abstract Responder responderInstance();
```
이런 코멘트는 가끔 유용할 수도 있지만 function이 코멘트가 담긴 정보를 직접 전할 수 있도록 하는게 더 좋다
```java
protected abstract Responder responderBeingTested();
```

#### Explanation of Intent
왜 이 코드를 썼는지에 대해 설명하는 코멘트
```java
public void testConcurrentAddWidgets() throws Exception {
  ...
  // This is our best attempt to get a race condition
  // by creating large number of threads
  for (int i = 0; i < 25000; i++) {
    ...
  }
  ...
}
```

#### Clarification
```java
public void testCompareTo() throws Exception {
  ...
  assertTrue(a.compareTo(a) == 0);    // a == a
  assertTrue(a.compareTo(b) != 0);    // a != b
  assertTrue(ab.compareTo(ab) == 0);  // ab == ab
  assertTrue(a.compareTo(b) == -1);   // a < b
  ...
}
```
Clarifying comments는 필요할 때가 있지만 정확하지 못 할 수 있다는 리스크가 있다. 쓰기 전에 꼭 다른 더 좋은 방법은 없는지, 없다면 정확한지 한 번 더 확인할 수 있도록 하자.

#### Warning of Consequences
```java
// Don't run unless you have some time to kill
public void _testWithReallyBigFile() {
  ...
}
```

#### TODO Comments
```java
// TODO-MdM these are not needed
// We expect this to go away when we do the checkout model
protected VersionInfo makeVersion() throws Exception {
  return null;
}
```
여러가지 이유로 TODO 코멘트를 남길 수 있지만, 나쁜 코드를 남기는 용도로 사용되어서는 안된다.

#### Amplification
```java
String listItemContent = match.group(3).trim();
// the trim is real important. It removes the starting spaces that
// could cause the item to be recognized as another list
new ListItemWidget(this, listItemContent, this.level + 1);
...
```

#### Javadocs in Public APIs
Java로 Public API를 만든다면 당연히 javadocs를 써야 할 것이다. 하지만 Javadocs역시 다른 코멘트들과 같은 단점을 가지고 있다는 것을 명심하자.

#### Mumbling
단지 코멘트가 필요할 것 같아서 대충 쓰는 코멘트는 나쁘다. 코멘트를 쓰기로 마음 먹었으면, 충분한 시간을 들여서 좋은 코멘트를 쓰려고 노력해라.

#### Redundant Comments
```java
// Utility method that returns when this.closed is true. 
// Throws an exception if the timeout is reached
public synchronized void waitForClose(final long timeoutMillis) throws Exception {
  if (!closed) {
    wait(timeoutMillis);
    if (!closed) throw new Exception("MockResponseSender could not be closed");
  }
}
```
위 코멘트는 코드 자체보다 정보를 더 가지고 있는 것도 아니고, 의도도, 쓰여야할 이유도 가지고 있지 않다. 

More examples:
```java
public abstract class ContainerBase implements Container, Lifecycle, Pipeline, MBeanRegistration, Serializable {

  /**
   * The processor delay for this component.
   */
  protected int backgroundProcessorDelay = -1;

  /**
   *  The lifecycle event support for this component.
   */
   protected LifecycleSupport lifecycle = new LifecycleSupport(this);

   ...
}
```

#### Misleading Comments
[Redundant Comments](#redundant-comments)의 코멘트가 잘 못 되었다는 걸 알고 있었나? 해당 메소드는 아무것도 반환하지 않고 있다. 이 코드를 읽는 다른 프로그래머는 코멘트를 통해 이 메소드가 true를 반환할 것을 기대할 수 있다. 결국 그 불쌍한 프로그래머는 디버깅을 통해서야만 무엇이 잘 못 되었는 지를 찾아낼 것이다.

#### Mandate Comments
모든 function이 javadocs를 가져아 한다는 것은 얼토당토않다.
```java
/**
 *
 * @param title The title of the CD
 * @param author The author of the CD
 * @param tracks The number of tracks on the CD
 * @param durationInMinutes The duration of the CD in minutes
 */
public void addCD(String title, String author, int tracks, int durationInMinutes) {
  CD cd = new CD();
  cd.title = title;
  cd.author = author;
  cd.tracks = tracks;
  cd.duration = duration;
  cdLIst.add(cd);
}
```

#### Journal Comments
```java
/**
 * Changes (from 11-Oct-2001)
 *
 * 11-Oct-2001: ...
 * 05-Nov-2001: ...
 *              ... 
 * 04-Jan-2005: ...
 ...

```

#### Noise Comments
```java
/**
 * Default constructor.
 */
protected AnnualDateRule() {}

/** The day of the month */
private int dayOfMonth;

private void startSending() {
  try {
    response.add(...);
    response.closeAll();
  } catch (Exception e1) {
    // Give me a break!
  }
}
```

#### Scary Noise
```java
/** The name. */
private String name;

/** The version */
private String version;

/** The version */    > cut-paste error🫠
private String info;
```

#### Don't Use a Comment When you Can Use a Function or a Variable
```java
// does the module from the global list <mod> depend on the subsystem we are part of?
if (smodule.getDependSubsystems().contains(subSysMod.getSubSystem()))

Should be

ArrayList moduleDependees = smodule.getDependSubsystems();
String ourSubSystem = subSysMod.getSubSystem();
if (moduleDependees.contains(ourSubSystem))
```

#### Position Markers
```java
// Actions /////////////////////////////////   WTH..🫠
```

#### Closing Brace Comments
많은 프로그래머들이 긴 function이나 nested structures이 보이면 closing braces뒤에 코멘트를 작성하는 경우가 있다. 만약 그런 경우가 생긴다면 코멘트를 쓰기 보다는 function의 크기를 줄일 생각을 먼저 해보자.
```java
public class WC {
  public static void main(String[] args) {
    ...
    try {
      ...
      while () {
      ...

      } //while
      ...
    } //try
    catch (IOException e) {
      ...
    } //catch
  } // main
}
```

#### Attributions and Bylines
```java
/* Added by Rick */
```
Git blame 🙏🏻

#### Commented-Out Code
```java
    InputStreamResponse response = new InputStreamResponse();
    response.setBody(formatter.getResultStream(), formatter.getByteCount());
//  InputStream resultsStream = formatter.getResultsStream();
//  StreamReader reader = new StreamReader(resultsStream);
```
다른 프로그래머들은 코멘트 아웃 처리 된 코드를 지워도 될 지 장담할 수 없다. 코멘트아웃 하는 대신 지우자. 우리는 아주 좋은 source code control system이 있기 때문에 그 코드는 항상 다시 가져올 수 있다.

#### Nonlocal Information
코멘트를 꼭 써야 한다면 이상한 곳에 써서 헷갈리게 하지 말고 코멘트가 해당되는 곳에 가까이 놓도록 하자.

#### Too Much Information
필요없는, 관련없는 내용 혹은 코드가 어떻게 바뀌어왔는 지 같은 내용은 적지 말자.

#### Inobvious Connection
코멘트가 해당 코드를 명확하게 설명하고 있는지 다시 한 번 확인해라
```java
/**
 * start with an array that is big enough to hold all the pixels
 * (plus filter bytes), and an extra 200 bytes for header info
 */
 this.pngBytes = new byte[((this.width + 1) * this.height * 3) + 200];
```

#### Function Headers
짧은 function은 설명이 필요 없다. 잘 선택한 이름이 코멘트 헤더보다 훨씬 낫다.

