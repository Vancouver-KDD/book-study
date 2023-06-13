# Chapter 22. Dealing with Failure

| Test list |
| ----------- |
~~Invoke test method~~
~~**Invoke setUp first~~
~~Invoke tearDown afterward~~
Invoke tearDown even if the test method fails 
Run multiple tests
~~Report collected results~~
~~Log string in WasRun~~
**Report failed tests**

# What we want
- To ensure that if we onte a failed test.

## TestCaseTest and TestResult
```
    # TestCaseTest
    def testFailedResultFormatting(self): 
        result= TestResult() 
        result.testStarted() 
        result.testFailed()
        assert("1 run, 1 failed" == result.summary())

    # TestResult
    def summary(self):
        return "%d run, %d failed" % (self.runCount, self.failureCount)
```

## TestCase
```
    # TestResult
    def __init__(self):
        self.runCount= 0
        self.errorCount= 0 
    def testFailed(self):
        self.errorCount= self.errorCount + 1

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
- When test is failed, **testFailed** will be caled.
- However, if setUp() fails, then the exception won't be caught.

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
~~Report failed tests~~
Catch and report setUp errors
