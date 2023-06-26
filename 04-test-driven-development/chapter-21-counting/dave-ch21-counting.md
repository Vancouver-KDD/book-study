# Chapter 21. Counting

```
//Invoke test method
//Invoke setUp first
//Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests
Report collected results <-- 
//Log string in WasRun
```
We'll have TestCase.run() return a TestResult object that records the results of running the 
test (singular for the moment, but we'll get to that).


tearDown()은 테스트 메서드에서 예외가 발생하는 것과 관계없이 호출이 반드시 되어져야 한다. 하지만 테스트가 작동하도록 하려면 예외를 잡아내야 한다. 즉, "5개 테스트가 실행됨. 2개 실패."과 같은 결과를 보여주도록 한다. 이를 위해 TestCase.run()이 실행 결과를 기록하는 TestResult 을 객체로써 반환하게 해본다.

```python
class TestCaseTest(TestCase):
    # ...
    def testResult(self):
        test = WasRun("testMethod")
        result = test.run()
        assert "1 run, 0 failed" == result.summary()
```
우선 가짜 구현으로 테스트를 통과하게끔 시작한다.
```python
class TestResult:
    def summary(self):
        return "1 run, 0 failed"
```
```python
class TestCase:
    # ...
    def run(self):
        self.setUp()
        method = getattr(self, self.name)
        method()
        self.tearDown()
        return TestResult
```

이제 summary() 구현을 조금씩 실체화하자.


```python
class TestResult:
    def __init__(self):
        self.runCount = 0
        
    def testStarted(self):
        self.runCount = self.runCount + 1
        
    def summary(self):
        return "%d run, 0 failed" % self.runCount
```
```python
class TestCase:
    # ...
    def run(self):
        result = TestResult()
        result.testStarted()
        self.setUp()
        method = getattr(self, self.name)
        method()
        self.tearDown()
        return result
```
실패하는 테스트의 수도 비슷한 흐름으로 작성하자. 먼저 테스트 케이스부터.

```python
class TestCaseTest(TestCase):
    # ...
    def testFailedResult(self):
        test = WasRun("testBrokenMethod")
        result = test.run()
        assert "1 run, 1 failed" == result.summary()
```

```python
class WasRun(TestCase):
    # ...
    def testBrokenMethod(self):
        raise Exception
```
현재는 WasRun.testBrokenMethod에서ㅍ예외를 처리하지 않는다 일단.

```
//Invoke test method
//Invoke setUp first
//Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests
//Report collected results  
//Log string in WasRun
```

<Review>

* Wrote a fake implementation, and gradually began making it real by replacing constants with variables

* Wrote another test
* When that test failed, wrote yet another test, at a smaller scale, to support making the failing test work


