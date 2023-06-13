# Chapter 22. Dealing with Failure

### Todo List

- ~~Invoke test method~~
- ~~Invoke setUp first~~
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- Run multiple tests
- ~~Report collected results~~
- ~~Log string in WasRun~~
- **Report failed tests**

테스트가 실패하면 올바른 결과를 출력하도록 하는 걸 확인하기 위해 테스트를 작성한다

```
class TestCaseTest:
    def testFailedResultFormatting(self):
    result = TestResult()
    result.testStarted()
    result.testFailed()
    asset("1 run, 1 failed" == result.summary())
```

`testStarted()` 와 `testFailed()`는 각각 테스트가 실행될때와 실패할때 보내는 메세지이다. 우리가 summary를 올바르게 받는다면, 우리는 이 메세지를 어떻게 보낼 것인가에 초점을 둬서 생각할 수 있다. 그래서 메세지가 보내지만 전체가 모두 잘 작동한다고 리대할 수 있다.

`testFailed()`는 실패한 테스트수를 세는 것임

```
class TestResult:
    def __init__(self):
        self.runCount = 0
        self.failureCount = 0
    def testFailed(self):
        self.failureCount = self.failureCount + 1
    def summary(self):
        return "%d run, %d failed" % (self.runCount, self.failureCount)
```

이제 `testFailed()`가 잘 불러진다면, 예상한 답을 얻을 수 있음
우리는 언제 이 method를 부를까? -> 테스트에서 exception을 잡았을 때

```
class TestCase:
    ...
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

근데, 이 method로는 `setUp()` 에서 문제가 생겼을때는 잡아낼 수 없음
우리는 각각의 테스트들이 독립적으로 실행되기를 원하므로 -> 투두리스트에 추가하자

### Todo List

- ~~Invoke test method~~
- ~~Invoke setUp first~~
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- Run multiple tests
- ~~Report collected results~~
- ~~Log string in WasRun~~
- ~~Report failed tests~~
- Catch and report setUp errors
