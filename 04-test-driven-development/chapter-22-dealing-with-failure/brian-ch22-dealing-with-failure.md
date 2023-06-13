# 실패처리하기

## Current Todo
- ~~테스트 메서드 호출하기~~
- ~~먼저 setUp 호출하기~~
- ~~나중에 tearDown 호출하기~~
- 테스트 메서드가 실패하더라도 tearDown 호출하기
- 테스트 여러 개 실행하기
- 수집한 결과를 출력하기
- ~~WasRun에 로그 문자열 남기기~~
- 실패한 테스트 보고하기

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
        try:                                # ch22
            method = getattr(self, self.name)
            method()
        except:                             # ch22
            result.testFailed()
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
        self.errorCount = 0                 # ch22
    def testStarted(self):
        self.runCount = self.runCount + 1
    # ch22
    def testFailed(self):
        self.errorCount= self.errorCount + 1
    def summary(self):
        return "%d run, d failed" % (self.runCount, self.failureCount) # ch22
        
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
    # ch22
    def testFailedResultFormatting(self):
        result = TestResult()
        result.testStarted()
        result.testFailed()
        assert("1 run, 1 failed" == result.summary())


TestCaseTest("testTemplateMethod").run()
TestCaseTest("testResult").run()            # ch21
TestCaseTest("testFailedResult").run()      # ch21
TestCaseTest("testFailedResultFormatting").run() # ch22
```

## Left Todo
- ~~테스트 메서드 호출하기~~
- ~~먼저 setUp 호출하기~~
- ~~나중에 tearDown 호출하기~~
- 테스트 메서드가 실패하더라도 tearDown 호출하기
- 테스트 여러 개 실행하기
- ~~수집한 결과를 출력하기~~
- ~~WasRun에 로그 문자열 남기기~~
- ~~실패한 테스트 보고하기~~
- setUp 에러를 잡아서 보고하기