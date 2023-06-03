# Part II: The xUnit Example
The xUnit architecture comes out very smoothly in Python, so it'll be switched to Python for Part II.

## Chapter 18. First Steps to xUnit
First, we need to be able to create a test case and run a test method. 
For example: TestCase("testMethod").run().


```
<To-do List>
Invoke test method <--
Invoke setUp first
Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests
Report collected results
```

First, a little program will print out true if a test method gets called, and false otherwise.

(1) sets a flag inside the test method
(2) print the flag after it's done
(3) after verifying it manaually, the process can be automated.


< Strategy >

- A test case that contains a flag.
- Before the test, the flag is false.
- The test method will set the flag.
- After the test, it's true.
- The TestCase class WasRun is called to indicate whether a method was run.
- The "assert test.wasRun",  built-in Python facility, will be used.

```python
# to print "None" before the method was run, and "1" afterward. 
    test= WasRun("testMethod") 
    print (test.wasRun)
    
    test.testMethod() 
    print (test.wasRun)
```







실행하기 전 테스트 메소드가  `None`을 호출하고 그 후에 `1`을 출력 예상 (None in Python 
is like null or nil, and stands for false, along with 0 and a few other objects.)

But, `WasRun` 클래스를 정의되지 않았으므로 예상대로 X.
```python
class WasRun:
  pass
```

```python
class WasRun:
  def __init__(self, name):
  self.wasRun = None
```

```python
class WasRun:
  def __init__(self, name):
  self.wasRun = None
  
  def testMethod(self):
    pass
```

```python
class WasRun:
  def __init__(self, name):
    self.wasRun = None
  
  def testMethod(self):
    self.wasRun = 1
```

Now we get the right answer - the green bar, hooray! Progress를 만들면서 계속적으로 refactoring이 진행될 것.

다음 인터페이스인 `run()`을 사용.


```python
test = WasRun("testMethod")
print test.wasRun
test.run()
print test.wasRun
```

```python
class WasRun:
  def __init__(self, name):
  self.wasRun = None
  
  def testMethod(self):
    self.wasRun = 1
  def run(self):
    self.testMethod()
```
The next step is to dynamically invoke the testMethod.

```python
class WasRun:
  def __init__(self, name):
    self.wasRun = None
    self.name = name
  
  def run(self):
    method = getattr(self, self.name)
    method(); 
```

Here is another general pattern of refactoring:


An instance가 여러 다른 케이스에서도 작동 할 수있도록 const를 variable로 generalize하는 것.

Now our little WasRun class is doing two distinct jobs: 

(1) 메소드가 호출되었는지 그렇지 않은지를 기억하는 일

(2) 메소드를 동적으로 호출하는 일

Let's create an empty TestCase superclass, and make WasRun.

```python
# TestCase class
class TestCase:
  pass
  
# WasRun Class
class WasRun(TestCase):
  def __init__(self, name):
    self.wasRun = None
    self.name = name
  
  def run(self):
    method = getattr(self, self.name)
    method(); 
```

```python
# TestCase class
class TestCase:
  def __init__(self, name):
    self.name = name
    
# WasRun Class
class WasRun(TestCase):
  def __init__(self, name):
    self.wasRun = None
    TestCase.__init__(self,name)
```

```python
# TestCase class
class TestCase:
  def __init__(self, name):
    self.name = name
    
# WasRun Class
class WasRun(TestCase):
  def __init__(self, name):
    self.wasRun = None
    TestCase.__init__(self,name)
```

```python
# TestCase class
class TestCase:
  def __init__(self, name):
    self.name = name
	
  def run(self):
    method = getattr(self, self.name)
    method()
    
# WasRun Class
class WasRun(TestCase):
  def __init__(self, name):
    self.wasRun = None
    TestCase.__init__(self,name)
```
Between every one of these steps, I run the tests to make sure I'm getting the same answer.

매번 `None`과 `1`이 나오는지 확인하는 것도 지겹기 때문에 우리가 만든 코드를 다음과 같이 바꿀 수 있다.

```python
# TestCaseTest class
class TestCaseTest(TestCase):
  def testRunning(self):
    test = WasRun("testMethod")
    assert(not test.wasRun)
    test.run()
    assert(test.wasRun)
TestCaseTest("testRunning").run()
```

테스트 코드의 내용을 단순히 `print`문에서 `assert`문으로 변경한 것이다.

```
<To-do List>
//Invoke test method
Invoke setUp first
Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests
Report collected results
```


이렇게까지 작은 단계 별로 작업할 필요까진 없지만 우리가 TDD를 마스터하고 나면, 좀 더 큰 단계씩 작업할 수 있을 것이다. 

Next we will tackle calling setUp() before running the test.

- After a couple of hubris-fueled false starts, figured out how to begin with a tiny little step 
- Implemented functionality, by first hardwiring it, and then making it more general by
replacing constants with variables
- Used Pluggable Selector, which we promise not to use again for four months, minimum, 
because it makes code hard to analyze statically
- Bootstrapped our testing framework, all in tiny steps

