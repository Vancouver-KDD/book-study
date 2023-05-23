# Chapter 19. Set the Table

| Test list |
| ----------- |
~~Invoke test method~~
**Invoke setUp first**
Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests
Report collected results

## A common pattern
1. Arrange - Create some objects.
2. Act - Stimulate them.
3. Assert - Check the results
: Arrange, is often the same from test to test. And the stimulation and expected results are unique.

### Conflicts
- Performance - We would like our tests to run as quickly as possible; namely, if we use similar objects in several tests, we would like to create them once for all tests.
- Isolation - Test decoulping and dependencies between tests.


### 1. wasSetup
```
    # TestCaseTest
    def testSetUp(self):
        test= WasRun("testMethod") 
        test.run() 
        assert(test.wasSetUp)

    # WasRun
    def setUp(self): 
        self.wasSetUp= 1

    # TestCaseTest
    def setUp(self): 
        pass
    def run(self):
        self.setUp()
        method = getattr(self, self.name)
        method()
```
: It works, but the process is redunudant.

### 2. optinos
```
    # TestCaseTest
    def testRunning(self):
        test= WasRun("testMethod")
        test.run() 
        assert(test.wasRun)

    # WasRun    
    def setUp(self):
        self.wasRun= None 
        self.wasSetUp= 1

    # TestCaseTest
    def setUp(self):
        self.test= WasRun("testMethod")
    def testRunning(self): 
        self.test.run() 
        assert(self.test.wasRun)
    def testSetUp(self): 
        self.test.run() 
        assert(self.test.wasSetUp)
```
: We can use setUp() to simplify the example test case.

## Update the test list
| Test list |
| ----------- |
~~Invoke test method~~
~~**Invoke setUp first~~
Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests
Report collected results
