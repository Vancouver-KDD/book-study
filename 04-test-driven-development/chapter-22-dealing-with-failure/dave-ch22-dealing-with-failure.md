# Chapter 22. Dealing with Failure

```
//Invoke test method
//Invoke setUp first
//Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests
//Report collected results
//Log string in WasRun
Report failed tests <--
```
실패한 테스트를 기록하면 올바른 결과를 출력할 수 있도록 더 작은 세분화된 테스트를 작성해보자 

```python
class TestCaseTest(TestCase):
    # ...
    def testRailedResultFormatting(self):
        result = TestResult()
        result.testStarted()
        result.testFailed()
        assert "1 run, 1 failed" == result.summary()
```
```python
class TestResult:
    def __init__(self):
        self.runCount = 0
        self.failureCount = 0

    def testStarted(self):
        self.runCount = self.runCount + 1

    def testFailed(self):
        self.failureCount = self.failureCount + 1

    def summary(self):
        return "%d run, %d failed" % (self.runCount, self.failureCount)
```


```python
class TestCase:
    # ...
    def run(self):
        result = TestResult()
        result.testStarted()
        self.setUp()
        try:
            method = getattr(self, self.name)
            method()
        except:
            result.testFailed()
        self.tearDown()
        return result
```
물론 위 run() 메서드에는 setUp()에서 발생한 예외에 대한 처리를 하지 못한다는 문제가 있다. 다음 테스트와 그 구현은 여러분을 위해 남겨두자.

```
//Invoke test method
//Invoke setUp first
//Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests
//Report collected results  
//Log string in WasRun
//Report failed tests
Catch and report setUp errors
```

<Review>

* Made our small-scale test work 
* Reintroduced the larger scale test
* Made the larger test work quickly using the mechanism demonstrated by the smaller test
* Noticed a potential problem and noted it on the to-do list instead of addressing it immediately

