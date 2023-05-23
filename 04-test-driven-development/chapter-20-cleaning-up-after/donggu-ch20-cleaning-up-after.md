# Chapter 20. Cleaning Up After

| Test list |
| ----------- |
~~Invoke test method~~
~~**Invoke setUp first~~
**Invoke tearDown afterward**
Invoke tearDown even if the test method fails 
Run multiple tests
Report collected results


## Invoke tearDown afterward**
- If we want the tests to remain independent, then a test that allocates external resources needs to release them before it is done, perhaps in a tearDown() method. 
- setUp() is called before the test method is run, and tearDown() is called afterward.

## Log string in WasRun
```
    # WasRun    
    def setUp(self): 
        self.wasRun= None 
        self.wasSetUp= 1 
        self.log= "setUp "
    def testMethod(self): 
        self.wasRun= 1
        self.log= self.log + "testMethod "

    # TestCaseTest
    def setUp(self):
        WasRun("testMethod")
    def testTemplateMethod(self):
        test= WasRun("testMethod")
        test.run()
        assert("setUp testMethod " == test.log)
```
: Doing a refactoring based on a couple of early uses, then having to undo it soon after is fairly common.

## Invoke tearDown afterward
```
    # TestCase
    def run(self, result): 
        result.testStarted()
        self.setUp()
        method = getattr(self, self.name)
        method()
        self.tearDown()

    def tearDown(self): 
        pass

    # WasRun    
    def setUp(self): 
        self.log= "setUp "
    def testMethod(self):
        self.log= self.log + "testMethod "
    def tearDown(self):
        self.log= self.log + "tearDown "

    # TestCaseTest
    def testTemplateMethod(self):
        test= WasRun("testMethod")
        test.run()
        assert("setUp testMethod " == test.log)
```
: We can use setUp() to simplify the example test case.

## Update the test list
| Test list |
| ----------- |
~~Invoke test method~~
~~**Invoke setUp first~~
~~Invoke tearDown afterward~~
Invoke tearDown even if the test method fails 
Run multiple tests
Report collected results
~~Log string in WasRun~~
