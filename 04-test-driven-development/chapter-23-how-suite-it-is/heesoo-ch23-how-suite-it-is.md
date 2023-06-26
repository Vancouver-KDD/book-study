# Chapter 23. How Suite it is

### Todo List

- ~~Invoke test method~~
- ~~Invoke setUp first~~
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- **Run multiple tests**
- ~~Report collected results~~
- ~~Log string in WasRun~~
- ~~Report failed tests~~
- Catch and report setUp errors

우리는 모든 테스트를 아래와 같이 한번에 실행시킬 것인데 너무 지저분하고 중복되는것이 많음 -> `TestSuite` 가 필요함

```
print TestCaseTest("testTemplateMethod").run().summary()
print TestCaseTest("testResult").run().summary()
print TestCaseTest("testFailedResultFormatting").run().summary()
print TestCaseTest("testFailedResult").run().summary()
```

`TestSuite` 를 실행하는 또다른 이유는 Composite의 좋은 예이기 때문이다. (하나의 테스트와 테스트 그룹을 완전히 똑같이 다룰 수 있고 싶음)

```
class TestCaseTest:
    ...
    def testSuite(self):
        suite = TestSuite()
        suite.add(WasRun("testMethod"))
        suite.add(WasRun("testBrokenMethod"))
        result = suite.run()
        assert("2 run, 1 failed" == result.summary())
```

`add()` : 테스트를 리스트에 추가하는 역할을 함
`run()` : 테스트에서 각가의 개별 테스트를 실행시킴

```
class TestSuite:
    def __init__(self):
        self.tests = []
    def add(self, test):
        self.tests.append(test)
    def run(self):
        result = TestResult()
        for test in tests:
            test.run(result)
        return result
```

Composite의 제약중 하나는 message가 각각의 테스트에 각자 생성된다는 것임
만약 우리가 `TestCase.run()`에 파라미터를 추가하면, `TestSuite.run()` 에 같은 파라미터를 추가해야함 -> 3가지 대안이 있음

- 파이선의 default parameter mechanism 사용 : default value 는 런타임이 아니라 컴파일 되는 순간에 평가되므로 사용 불가함
- method를 두 부분으로 나눔 : 하나는 `TestResult` 를 할당하는 것, 다른 하나는 주어진 `TestResult` 에서 실행하는것 -> 좋은 방법은 아닌 것 같음
- 호출하는 곳에서 `TestResult`를 할당하는 법 -> 이게 좋을 듯! `Collecting Parameter` 라고 함

```
class TestCaseTest:
    ...
    def testSuite(self):
        suite = TestSuite()
        suite.add(WasRun("testMethod"))
        suite.add(WasRun("testBrokenMethod"))
        result = TestResult()
        suite.run(result)
        assert("2 run, 1 failed" == result.summary())
```

```
class TestSuite:
    def __init__(self):
        self.tests = []
    def add(self, test):
        self.tests.append(test)
    def run(self):
        result = TestResult()
        for test in tests:
            test.run(result)
```

-> `run()`에서 특별한 리턴을 하지 않음

```
class TestCase:
    ...
    def run(self, result):
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

처음 테스트들을 부르는 코드를 클린업 할 수 있음

```
suite = TestSuite()
suite.add(TestCaseTest("testTemplateMethod"))
suite.add(TestCaseTest("testResult"))
suite.add(TestCaseTest("testFailedResultFormatting"))
suite.add(TestCaseTest("testFailedResult"))
suite.add(TestCaseTest("testSuite"))
result = TestResult()
suite.run(result)
print result.summary()
```

```
class TestCaseTest:
    def setUp(self):
        self.result= TestResult()
    def testTemplateMethod(self):
        test= WasRun("testMethod")
        test.run(self.result)
        assert("setUp testMethod tearDown " == test.log)
    def testResult(self):
        test= WasRun("testMethod")
        test.run(self.result)
        assert("1 run, 0 failed" == self.result.summary())
    def testFailedResult(self):
        test= WasRun("testBrokenMethod")
        test.run(self.result)
        assert("1 run, 1 failed" == self.result.summary())
    def testFailedResultFormatting(self):
        self.result.testStarted()
        self.result.testFailed()
        assert("1 run, 1 failed" == self.result.summary())
    def testSuite(self):
        suite= TestSuite()
        suite.add(WasRun("testMethod"))
        suite.add(WasRun("testBrokenMethod"))
        suite.run(self.result)
        assert("2 run, 1 failed" == self.result.summary())
```

각각 테스트에서 `TestResult`를 할당하지 않고 `setUp()`에서 생성함

Review

- TestSuite를 위한 테스트를 작성함
- 테스트를 성공시키는 것 없이 그냥 실행하는 부분을 작성
- run method 의 인터페이스 변경 -> 아이템과 composite의 아이템이 완전히 똑같이 작동될 수 있도록 함 -> 테스트 작동하게함
- 공통적인 setup 코드 실행
