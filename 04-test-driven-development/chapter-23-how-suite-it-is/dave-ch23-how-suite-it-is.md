# Chapter 23. How Suite It Is

```
//Invoke test method
//Invoke setUp first
//Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests <--
//Report collected results
//Log string in WasRun
//Report failed tests
//Catch and report setUp errors
```
우리가 모든 테스트를 호출하는 파일의 끝 부분은 정리해야할 필요가 있어 보입니다.
```python
print (TestCaseTest("testTemplateMethod").run().summary())
print (TestCaseTest("testResult").run().summary())
print (TestCaseTest("testFailedResultFormatting").run().summary())
print (TestCaseTest("testFailedResult").run().summary())
```
누락된 디자인 요소를 찾는 동기로 보지 않는 한 중복은 좋은 선택이 아니다
TestSuite를 생성하고 몇 가지 테스트를 추가한 다음 이를 실행하여 종합적인 결과를 얻도록 해보자


```python
class TestCaseTest(TestCase):
    # ...
    def testSuite(self):
        suite = TestSuite()
        suite.add(WasRun("testMethod"))
        suite.add(WasRun("testBrokenMethod"))
        result = suite.run()
        assert "2 run, 1 failed" == result.summary()
```

```python
class TestSuite:
    def __init__(self):
        self.tests = []

    def add(self, test):
        self.tests.append(test)

    def run(self):
        result = TestResult()
        for test in self.tests:
            test.run(result)
        return result
```
(Python note: [] creates an empty collection.)
run method에 문제가 있어보인다.실행되는 모든 테스트에서 단일 TestResult를 사용해보자.
```python
class TestCaseTest(TestCase):
    # ...
    def testSuite(self):
        suite = TestSuite()
        suite.add(WasRun("testMethod"))
        suite.add(WasRun("testBrokenMethod"))
        result = TestResult()
        suite.run(result)
        assert "2 run, 1 failed" == result.summary()
```

```python
class TestSuite:
    # ...
    def run(self, result):
        for test in self.tests:
            test.run(result)
        return result
```

```python
class TestCase:
    # ...
    def run(self, result):
        result.testStarted()
        self.setUp()
        try:
            method = getattr(self, self.name)
            method()
        except:
            result.testFailed()
        self.tearDown()
```

이제 코드 위에 있는 테스트 호출 코드를 정리해보면
```python
suite = TestSuite()
suite.add(TestCaseTest("testTemplateMethod"))
suite.add(TestCaseTest("testResult"))
suite.add(TestCaseTest("testFailedResult"))
suite.add(TestCaseTest("testRailedResultFormatting"))
suite.add(TestCaseTest("testSuite"))
result = TestResult()
suite.run(result)
print(result.summary())
```
run() 메서드에 TestResult를 넘겨 주고 있지 않으므로...

```python
class TestCaseTest(TestCase):
    def setUp(self):
        self.result = TestResult()

    def testTemplateMethod(self):
        test = WasRun("testMethod")
        test.run(self.result)
        assert "setUp testMethod tearDown " == test.log

    def testResult(self):
        test = WasRun("testMethod")
        test.run(self.result)
        assert "1 run, 0 failed" == result.summary()

    def testFailedResult(self):
        test = WasRun("testBrokenMethod")
        test.run(self.result)
        assert "1 run, 1 failed" == result.summary()

    def testRailedResultFormatting(self):
        self.result.testStarted()
        self.result.testFailed()
        assert "1 run, 1 failed" == result.summary()

    def testSuite(self):
        suite = TestSuite()
        suite.add(WasRun("testMethod"))
        suite.add(WasRun("testBrokenMethod"))
        suite.run(self.result)
        assert "2 run, 1 failed" == result.summary()
```

```
//Invoke test method
//Invoke setUp first
//Invoke tearDown afterward
Invoke tearDown even if the test method fails 
//Run multiple tests 
//Report collected results  
//Log string in WasRun
//Report failed tests
Catch and report setUp errors
Create TestSuite from a TestCase class
```

<Review>

* Wrote a test for a TestSuite
* Wrote part of the implementation, but without making the test work. This was a violation of "da roolz." If you spotted it at the time, take two test cases out of petty cash. I'm sure there is a simple fake implementation that would have made the test case work so we could refactor under the green bar, but I can't think what it is at the moment.
* Changed the interface of the run method so that the item and the Composite of items could 
work identically, then finally got the test working 
* Factored out the common setup code

