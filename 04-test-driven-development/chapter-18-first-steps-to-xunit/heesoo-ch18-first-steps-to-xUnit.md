# Chapter 18. First Steps to xUnit

첫번째 할일: create a test case and ren a test method
testing framework todo list

- **Invoke test method**
- Invoke setUp first
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- Run multiple tests
- Report collected results

proto-test 를 위해 test method가 불려지면 true를 프린트하는 프로그램이 필요함

1. create a test case that contains a flag : false

```
test = WasRun("testMethod")
print test.wasRun  // will print "None"
test.testMethod()
print test.wasRun  // will print 1
```

2. define class `WasRun`

```
class WasRun:
    pass
```

`pass`: used when there is no implementation of a class or method

3. add attribute

```
class WasRun:
    def __init__(self, name);
    self.wasRun = None
```

it will print out `None`

4. define `testMethod` and make it print out `1` when it is called

```
class WasRun:
    def __init__(self, name);
    self.wasRun = None

    def testMethod(self):
    self.wasRun =1
```

5. need real interface `run()` instead of calling the test method directly -> test will change to

```
test= WasRun("testMethod")
print test.wasRun
test.run()
print test.wasRun
```

6. Add `run()` in WasRun class

```
class WasRun:
    def __init__(self, name);
    self.wasRun = None

    def testMethod(self):
    self.wasRun =1

    def run(self):
        self.testMethod()
```

7. refactor WasRun

```
class WasRun:
    def __init__(self, name):
        self.wasRun = None
        self.name = name
    def run(self):
        method = getattr(self, self.name)
        method()
```

WasRun class 두가지 일을 하는데, 하나는 keeping track of whether a method was invoked or not, 또 다른 하나는 dynamically invoking the method

8. create a `TestCase` superclass and make `WasRun` a subclass

```
class TestCase:
    def __init__(self, name):
        self.name = name
```

```
class WasRun(TestCase):
    def __init__(self, name):
    self.wasRun = None
    TestCase.__init__(self,name)
```

9. `run()` method uses attributes from the superclass only -> belong to the superclass

```
class TestCase:
    def __init__(self, name):
        self.name = name
    def run(self):
        method = getattr(self, self.name)
        method()
```

10. TestCaseTest

```
class TestCaseTest(TestCase):
    def testRunning(self):
        test = WasRun("testMethod")
        assert(not test.wasRun)
        test.run()
        asset(test.wasRun)
TestCaseTest("testRunning").run()
```

testing framework todo list

- ~~Invoke test method~~
- Invoke setUp first
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- Run multiple tests
- Report collected results
