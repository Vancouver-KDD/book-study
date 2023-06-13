# Chapter 23. How Suite It Is

| Test list |
| ----------- |
~~Invoke test method~~
~~**Invoke setUp first~~
~~Invoke tearDown afterward~~
Invoke tearDown even if the test method fails 
**Run multiple tests**
~~Report collected results~~
~~Log string in WasRun~~
~~Report failed tests~~
Catch and report setUp errors

# What we want
- remove duplications.
- Want to get collective result from out tests.

## TestCaseTest and TestSuite
```
    # TestCaseTest
    def testSuite(self):
        suite= TestSuite() 
        suite.add(WasRun("testMethod")) 
        suite.add(WasRun("testBrokenMethod")) 
        result= suite.run()
        assert("2 run, 1 failed" == result.summary())

    # TestSuite
    # Added 'add' function
    class TestSuite:
        def __init__(self):
            self.tests= []
        def add(self, test):
            self.tests.append(test)
        def run(self):
            result= TestResult() 
            for test in tests:
                test.run(result) 
            return result
```

## TestCaseTest
```
    # TestResult
    def testSuite(self):
        suite= TestSuite() 
        suite.add(WasRun("testMethod")) 
        suite.add(WasRun("testBrokenMethod")) 
        result= TestResult()
        suite.run(result)
        assert("2 run, 1 failed" == result.summary())
```

# Remove the duplicaed line
```
def testTemplateMethod(self):
    test= WasRun("testMethod")
    result= TestResult()
    test.run(result)
    assert("setUp testMethod tearDown " == test.log)
def testResult(self):
    test= WasRun("testMethod")
    result= TestResult()
    test.run(result)
    assert("1 run, 0 failed" == result.summary())
def testFailedResult(self):
    test= WasRun("testBrokenMethod")
    result= TestResult()
    test.run(result)
    assert("1 run, 1 failed" == result.summary())
def testFailedResultFormatting(self): 
    result= TestResult() 
    result.testStarted() 
    result.testFailed()
    assert("1 run, 1 failed" == result.summary())
```
--->
```
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

# Update the test list
| Test list |
| ----------- |
~~Invoke test method~~
~~Invoke setUp first~~
~~Invoke tearDown afterward~~
Invoke tearDown even if the test method fails 
~~Run multiple tests~~
~~Report collected results~~
~~Log string in WasRun~~
~~Report failed tests~~
Catch and report setUp errors
Create TestSuite from a TestCase class

# What we did
- Factored out the common setup code