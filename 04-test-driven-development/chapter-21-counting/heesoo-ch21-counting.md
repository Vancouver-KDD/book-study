# Chapter 21. Counting

### Todo List

- ~~Invoke test method~~
- ~~Invoke setUp first~~
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- Run multiple tests
- **Report collected results**
- ~~Log string in WasRun~~

tearDown()이 예외없이 불러지게 할 것임 -> 그러기 위해서는 exception 을 잡아낼 수 있어야함
일반적으로 테스트하는 순서가 중요함
만약 테스트 하나가 통과하고 그 다음 순서에서 실패했다면, 두단계 앞으로 사거 살펴볼 필요가 있음
-> programming environment 가 각 테스트를 돌릴때마다 확인해주는 체크포인트로 작동하면 좋을 것 같음

테스트를 돌렸을때 결과를 보고싶음 -> 그럼 에러를 바로 캐치할 수 있기 때문에
우리는 `TestCase.run()` 이 테스트 하나의 실행 결과를 기록하는 `TestResult` 객체를 반환하게 만들 것임

1. 성공하는 테스트케이스를 위한 결과를 만들기 위한 테스트의 테스트케이스를 작성함

```
class TestCaseTest:
    ...
    def testResult(self):
        test = WasRun("testMethod")
        result = test.run()
        asset("1 run, 0 failed" == result.summary())
```

2. fake implementation 부터 시작함

```
class TestResult:
    def summary(self):
        return "1 run, 0 failed"
```

3. `TestResult` 를 `TestCase.run()`의 결과로 리턴해줌

```
class TestCase:
    ...
    def run(self):
        self.setUp()
        method = getattr(self, self.name)
        method()
        self.tearDown()
        return TestResult()
```

-> 이제 테스트가 실행되면 summary()가 실해될 것임

5. 테스트 실행 횟수를 constant로 만들 수 있음

```
class TestResult:
    def __init__(self):
        self.runCount = 1
    def summary(self):
        return "%d run, 0 failed" % self.runCount
```

6. `runCount` 테스트 실행시킨 횟수로 바꿔주기: 0으로 시작하고 실행될때마다 1씩 증가할 수 있도록

```
class TestResult:
    def __init__(self):
        self.runCount = 0
    def testStarted(self):
        self.runCount = self.runCount + 1
    def summary(self):
        return "%d run, 0 failed" % self.runCount
```

7. 이제 `TestCase` 가 불려질때 `testStarted` method를 실행시킴

```
class TestCase:
    ...
    def run(self):
        result = TestResult()
        result.testStarted()
        self.setUp()
        method = getattr(self, self.name)
        method()
        self.tearDown()
        return result
```

실패한 테스트를 카운트하는 method도 똑같은 방식으로 필요함. 아직 실패하는 테스트가 존재하지 않으니 실패하는 테스트를 검증하기 위한 테스트케이스를 위한 또 다른 테스트를 하나 작성해야 한다.

```
class TestCaseTest:
    ...

    def testFailedResult(self):
        test = WasRun("testBrokenMethod")
        result = test.run()
        assert("1 run, 1 failed" == run.summary())
```

`testBrokenMethod()` 작성

```
class WasRun:
    ...
    def testBrokenMethod(self):
        raise Exception
```

### Todo List

- ~~Invoke test method~~
- ~~Invoke setUp first~~
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- Run multiple tests
- ~~Report collected results~~
- ~~Log string in WasRun~~
- Report failed tests

`WasRun.testBrokenMethod` 에서 던진 예외를 아직 처리하지 않았음!

Review

- fake implementation 을 작성하고 constants 를 변수로 만들면서 실제화해라
- 또다른 테스트를 작성함
- 테스트가 실패했을 때, 좀 더 작은 스케일로 또 다른 테스트를 만들어 실패한 테스트가 성공하도록 보조할 수 있음
