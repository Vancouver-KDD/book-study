# Chapter 18. First Steps to xUnit

## xUnit
| Test list |
| ----------- |
**Invoke test method**
Invoke setUp first
Invoke tearDown afterward
Invoke tearDown even if the test method fails Run multiple tests
Report collected results


## Bootstrap test
### 1. This code works, but we need to do some refactoring.
```
    class WasRun:
        def __init__(self, name):
        self.wasRun= 1

        def testMethod(self): 
            pass

        def run(self):
            self.testMethod()

    # Python test code
    test= WasRun("testMethod")
    print test.wasRun
    test.run()
    print test.wasRun
```

### 2. Refactoring.
```    
    class WasRun:
        def __init__(self, name):
            self.wasRun= None
            self.name= name 
            
        def run(self):
            method = getattr(self, self.name) 
            method()
```
A way of general pattern of refactoring: take code that works in one instance and generalize it to work in many by replacing constants with variables. Here the constant was hardwired code, not a data value, but the principle is the same. TDD makes this work well by giving you running concrete examples from which to generalize, instead of having to generalize purely with reasoning.

### 3. Add superclass for WasRun
```
    # TestCase
    class TestCase:
        def __init__(self, name):
            self.name= name

        def run(self):
            method = getattr(self, self.name) method()

    # WasRun
    class WasRun(TestCase): . . .
        def __init__(self, name):
            self.wasRun= None 
            TestCase.__init__(self, name)
```

### 4. Write a test
```
    class TestCaseTest(TestCase):
        def testRunning(self):
            test= WasRun("testMethod") 
            assert(not test.wasRun) 
            test.run() 
            assert(test.wasRun) 
```

## Update the test list
| Test list |
| ----------- |
~~Invoke test method~~
Invoke setUp first
Invoke tearDown afterward
Invoke tearDown even if the test method fails Run multiple tests
Report collected results