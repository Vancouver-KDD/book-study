# 셈하기

## Current Todo
- ~~테스트 메서드 호출하기~~
- ~~먼저 setUp 호출하기~~
- ~~나중에 tearDown 호출하기~~
- 테스트 메서드가 실패하더라도 tearDown 호출하기
- 테스트 여러 개 실행하기
- 수집한 결과를 출력하기
- ~~WasRun에 로그 문자열 남기기~~

## 코드
```Python
class TestCase:
    def __init__(self, name):
        self.name = name
    def setUp(self):
        pass
    def tearDown(self):
        pass
    def run(self):
        result = TestResult()               # ch21
        result.testStarted()                # ch21
        self.setUp()
        method = getattr(self, self.name)
        method()
        self.tearDown()
        return result                       # ch21
        
class WasRun(TestCase):
    def __init__(self, name):
        TestCase.__init__(self, name)
    def setUp(self):
        self.log = "setUp "
    def tearDown(self):
        self.log = self.log + "tearDown "
    def testMethod(self):
        self.log = self.log + "testMethod "
    def testBrokenMethod(self):             # ch21
        raise Exception                     # ch21

# ch21
class TestResult:
    def __init__(self):
        self.runCount = 0
    def testStarted(self):
        self.runCount = self.runCount + 1
    def summary(self):
        return "%d run, 0 failed" % self.runCount
        
class TestCaseTest(TestCase):
    def testTemplateMethod(self):
        test = WasRun("testMethod")
        test.run()
        assert("setUp testMethod tearDown " == test.log)
    # ch21
    def testResult(self):
        test = WasRun("testMethod")
        result = test.run()
        assert("1 run, 0 failed" == result.summary())
    # ch21 & it does not pass
    def testFailedResult(self):
        test = WasRun("testBrokenMethod")
        result = test.run()
        assert("1 run, 1 failed", result.summary)

TestCaseTest("testTemplateMethod").run()
TestCaseTest("testResult").run()            # ch21
TestCaseTest("testFailedResult").run()      # ch21
```

## Left Todo
- ~~테스트 메서드 호출하기~~
- ~~먼저 setUp 호출하기~~
- ~~나중에 tearDown 호출하기~~
- 테스트 메서드가 실패하더라도 tearDown 호출하기
- 테스트 여러 개 실행하기
- 수집한 결과를 출력하기
- ~~WasRun에 로그 문자열 남기기~~
- 실패한 테스트 보고하기