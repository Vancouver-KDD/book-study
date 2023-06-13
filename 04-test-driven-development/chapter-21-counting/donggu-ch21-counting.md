# Chapter 21. Counting

| Test list |
| ----------- |
~~Invoke test method~~
~~**Invoke setUp first~~
~~Invoke tearDown afterward~~
Invoke tearDown even if the test method fails 
Run multiple tests
**Report collected results**
~~Log string in WasRun~~

# What we want
- We want to test every test regardless of expection during the test method.
- However, in this current test, if there is a test failure, we won't be able to test tearDown() because the exceptions won't be reported.
- We want to have test reports to check test results.

## TestResult 
```
    # TestCaseTest    
    def testResult(self):
        test= WasRun("testMethod")
        result= test.run()
        assert("1 run, 0 failed" == result.summary())

    # TestCaseTest
    class TestResult:
        def summary(self): 
            return "1 run, 0 failed"
```
- runCount shouldn't be a constant. It should be computed by counting the nubmer of tests run.

## TestResult with a symbolic constant
```
    # TestResult
    def __init__(self): 
        self.runCount= 0
    def testStarted(self): 
        self.runCount= self.runCount + 1
    def summary(self):
        return "%d run, 0 failed" % self.runCount

    # TestCase
    def run(self):
        result= TestResult() 
        result.testStarted()
        self.setUp()
        method = getattr(self, self.name) 
        method()
        self.tearDown()
        return result
```

## testFailedResult
```
    # TestCaseTest
    def testFailedResult(self):
        test= WasRun("testBrokenMethod") 
        result= test.run()
        assert("1 run, 1 failed", result.summary)
    
    # WasRun
    def testBrokenMethod(self): 
        raise Exception
```

# What we missed
-  We need to catch the exception ('WasRun.testBrokenMethod')

# Update the test list
| Test list |
| ----------- |
~~Invoke test method~~
~~**Invoke setUp first~~
~~Invoke tearDown afterward~~
Invoke tearDown even if the test method fails 
Run multiple tests
~~Report collected results~~
~~Log string in WasRun~~
Report failed tests
