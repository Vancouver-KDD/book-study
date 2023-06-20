# Chapter 27. Testing Patterns

이러한 패턴은 테스트 작성을 위한 보다 자세한 기술입니다.


## Child Test
How do you get a test case running that turns out to be too big? Write a smaller test case that represents the broken part of the bigger test case. Get the smaller test case running. 
Reintroduce the larger test case.

## Mock Object

How do you test an object that relies on an expensive or complicated resource? Create a fake version of the resource that answers constants.
고전적인 예는 데이터베이스입니다.

```java
public void testOrderLookup() { 
    Database db= new MockDatabase();
    db.expectQuery("select order_no from Order where cust_no is 123"); 
    db.returnResult(new String[] {"Order 2" ,"Order 3"});
... 
}

```

If the MockDatabase does not get the query it expects, then it throws an exception. If the 
query is correct, then it returns something that looks like a result set constructed from the constant strings.
Another value of mocks, aside from performance and reliability, is readability. You can read the preceding test from one end to another. If you have a test database full of realistic data, when you see that a query should have resulted in 14 replies, you have no idea why 14 is the right answer.

## Self Shunt

한 개체가 다른 개체와 올바르게 통신하는지 어떻게 테스트합니까? 테스트 중인 개체가 예상하는 개체가 아닌 테스트 사례와 통신하도록 합니다.
Suppose we wanted to dynamically update the green bar on the testing user interface. If we 
could connect an object to the TestResult, then it could be notified when a test ran, when it failed, when a whole suite started and finished, and so on. Whenever we were notified that a test ran, we would update the interface. Here's a test for this:
```python
#ResultListenerTest
def testNotification(self):
    result= TestResult()
    listener= ResultListener()
    result.addListener(listener) 
    WasRun("testMethod").run(result) 
    assert 1 == listener.count

```
테스트에는 알림 수를 계산하기 위한 개체가 필요합니다.
```python
#ResultListener
class ResultListener:
    def __init__(self):
        self.count= 0
    def startTest(self):
        self.count= self.count + 1
```

The TestCase itself becomes a kind of Mock Object.
```python
#ResultListenerTest
def testNotification(self):
    self.count= 0
    result= TestResult()
    result.addListener(self) 
    WasRun("testMethod").run(result) 
    assert 1 == self.count
def startTest(self):
    self.count= self.count + 1
```

Self Shunt may require that you use Extract Interface to get an interface to implement. You 
will have to decide whether extracting the interface is easier, or if testing the existing class as a black box is easier. I have noticed, however, that interfaces extracted for shunts tend to get their third and subsequent implementations soon thereafter.

## Log String

메시지가 호출되는 순서가 올바른지 어떻게 테스트합니까? 로그를 문자열로 유지하고 메시지가 호출될 때 문자열에 추가합니다.

xUnit의 예)

We have a Template Method, which we expect to call setUp(), a testing method, and tearDown(), in that order. By implementing the methods to record in a 
string that they were called, the test reads nicely:

```python
def testTemplateMethod(self): 
    test= WasRun("testMethod") 
    result= TestResult()
    test.run(result)
    assert("setUp testMethod tearDown " == test.log)

```
Implemantion

```python
#WasRun
def setUp(self): 
    self.log= "setUp "
def testMethod(self):
    self.log= self.log + "testMethod " 
def tearDown(self):
    self.log= self.log + "tearDown "
```

로그 문자열은 관찰자를 구현하고 특정 순서로 알림이 올 것으로 예상할 때 특히 유용합니다. 특정 알림을 예상했지만 순서에 대해 신경쓰지 않는다면 일련의 문자열을 유지하고 어설션에서 집합 비교를 사용할 수 있습니다.


## Crash Test Dummy

호출될 가능성이 없는 오류 코드를 어떻게 테스트합니까? 어쨌든 실제 작업을 수행하는 대신 예외를 발생시키는 특수 개체를 사용하여 호출하십시오.

Let's say we want to test what happens to our application when the file system is full. 

> Crash Test Dummy
```java
private class FullFile extends File { 
    public FullFile(String path) {
        super(path); 
    }
public boolean createNewFile() throws IOException { 
    throw new IOException();
    } 
}
```

> our Expected Exception test:

```java
public void testFileSystemError() { 
    File f= new FullFile("foo");
    try { 
        saveAs(f); 
    fail();
    } catch (IOException e) { 
    }
}

```

충돌 테스트 더미는 전체 개체를 모의할 필요가 없다는 점을 제외하면 모의 개체와 같습니다.
Java's anonymous inner classes work well for sabotaging just the right method to simulate the error we want to exercise. You can override just the one method you want, right there in your test case, making the test case easier to read:

```java
public void testFileSystemError() { 
    File f= new File("foo") {
    public boolean createNewFile() throws IOException { 
        throw new IOException();
    }
};
    try { 
        saveAs(f); 
        fail();
        } catch (IOException e) { 
    }
}

```
## Broken Test

혼자 프로그래밍할 때 어떻게 프로그래밍 세션을 종료합니까? 마지막 테스트를 중단된 상태로 둡니다.

Finish a solo session by writing a test case and running it to be sure it doesn't pass. When you come back to the code, you then have an obvious place to start. You have an obvious, concrete bookmark to help you remember what you were thinking; and making that test work should be quick work, so you'll quickly get your feet back on that victory road.


## Clean Check-in
How do you leave a programming session when you're programming in a team? Leave all of 
the tests running.

You need to start from a place of confidence and certainty. Therefore, always make sure that all of the tests are running before you check in your code.

The simplest rule is to just throw away your work and start over. The broken test is pretty 
strong evidence that you didn't know enough to program what you just programmed. If the 
team adopted this rule, then there would be a tendency for folks to check in more often 
because the first person to check in wouldn't risk losing any work. Checking in more often is probably a good thing.

