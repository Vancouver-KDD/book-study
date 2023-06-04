# Chapter 19. Set the Table

```python
from TestCase import TestCase
from WasRun import WasRun

class TestCaseTest(TestCase):
    def testRunning(self):
        test= WasRun("testMethod") 
        assert(not test.wasRun)
        test.run() 
        assert(test.wasRun)
        
TestCaseTest("testRunning").run()

```
```python
from TestCase import TestCase

class WasRun(TestCase): 
    def __init__(self, name):
        self.wasRun= None         
        TestCase.__init__(self, name)
    def run(self):
        method = getattr(self, self.name) 
        method()    
```
```python
class TestCase: 
    def __init__(self, name):
        self.name= name
    def run(self):
        method = getattr(self, self.name) 
        method()
```

테스트를 작성 시, 3A라하는 공통된 패턴이있다.
1. Arrange - 객체 생성
2. Act - 자극
3. Assert - 결과 검사

```
<to-do>
//Invoke test method
Invoke setUp first <--
Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests
Report collected results
```

처음 단계인 arrage 단계는 테스트간에 꽤 동일함을 알 수 있다. 
예를 들어 객체 7과 9가 있을 시
 - 첫번째 테스트 7+9 =16 
 - 두번째 테스트 7x9 = 63
 7과 9와 같이 객체 생성은 반복된다.
 ( Act의 자극과 Assert의 결과검사는 바뀌지만.)
 

해당 패턴이 반복되는 정도에 따라, 얼마나 자주 객체를 생성함에 직면하게 되고 여기서 두가지 조건이 conflict한다.

- 성능(Performance) - 최대한 빠른 테스트 실행을 위해 객체를  하나만 생성하여 모든 테스트에 적용한다.
- 격리(Isolation) - 근데 또 하나의 테스트의 실패성공의 여부가 다른 테스트에 영향을 주지 않기를 원한다. 만약 테스트가 객체를 공유하면, 변경이 다른 테스트에 영향을 미친다. 

테스트 간에 tightly coupled는 테스트 결과의 왜곡또한 초래할 수 있으므로, 객체 생성이 충분히 빠르다고 가정하고 테스트 커플링을 피하자. 

테스트를 실행하기 전에 플래그를 false로 설정하기를 원했던 WasRun 에서 위장된 형태를 이미 보았다. 이와 같은 형태를 가져가기위해 먼저 아래 테스트가 필요하다.

```python
# TestCaseTest class
def testSetUp(self):
    test = WasRun("testMethod")
    test.run()
    assert(test.wasSetUp)
```
이를 TestCaseTest class에 추가한다.
```python
from TestCase import TestCase
from WasRun import WasRun

class TestCaseTest(TestCase):
    def testRunning(self):
        test= WasRun("testMethod") 
        assert(not test.wasRun)
        test.run() 
        assert(test.wasRun)
    def testSetUp(self):
        test = WasRun("testMethod")
        test.run()
        assert(test.wasSetUp)    
TestCaseTest("testRunning").run()

```
파이썬이 `wasSetUp`attribute의 부재를 알려준다. 수정하자.


```python
# WasRun class에 추가 
    def setUp(self):
        self.wasSetUp = 1
```
아래와 같이 추가
```python
from TestCase import TestCase

class WasRun(TestCase): 
    def __init__(self, name):
        self.wasRun= None         
        TestCase.__init__(self, name)
    def run(self):
        method = getattr(self, self.name) 
        method()    
    def setUp(self):
        self.wasSetUp = 1    
```

이제 추가된 `setUp` 메소드를 호출하면 `wasSetUp` 이 설정될 것이다. `setUp` 을 호출하는 것은 `TestCase` 가 할 일이니 그곳을 확인해보자.

```python
# TestCase class
    def setUp(self): # <<- 추가
      pass          # <<- 추가    
    def run(self):
      self.setUp() # <<-추가 
      method = getattr(self, self.name)
      method()
```
위의 표시된 부분을 기존코드에 추가하면
```python
class TestCase: 
    def __init__(self, name):
        self.name= name
    def setUp(self): # <<- 추가
        pass
    def run(self):
        self.setUp() # <<-추가 
        method = getattr(self, self.name)
        method()
```

위의경우 테스트 케이스 하나를 위해 두단계가 필요하게 설정이 되어져있는데, 이는 좀 더 까다로운 상황을 만나게 되면 좀 과한 편에 속하게 된다. 
하나로 해서 테스트를 성공하는 방법을 찾아보면, 

wasRun의 flag 를 setUp에서 설정하여 `WasRun` 줄일 수 있다.

```python
# WasRun class
    def setUp(self):
      self.wasRun = None
      self.wasSetUp = 1

# 그리고 TestCase에서는 삭제한다.
class TestCase: 
    # def setUp(self): 
        # pass
```
위에 책에 나온 수정된 코드를 기존코드 적용하면,

```python
from TestCase import TestCase

class WasRun(TestCase): 
    def __init__(self, name):
        # self.wasRun= None <<- 삭제        
        TestCase.__init__(self, name)
    def run(self):
        method = getattr(self, self.name) 
        method()    
    def setUp(self):
        self.wasRun = None
        self.wasSetUp = 1          
```


테스트를 실행하기 전에 플래그를 검사하지 않도록 TestCaseTest 클래스의 `testRunning` 펑션을 단순화한다.

```python
from TestCase import TestCase
from WasRun import WasRun

class TestCaseTest(TestCase):
    def testRunning(self):
        test= WasRun("testMethod") 
        # assert(not test.wasRun) -> 플래그 체크부분 삭제
        test.run() 
        assert(test.wasRun)
    def testSetUp(self):
        test = WasRun("testMethod")
        test.run()
        assert(test.wasSetUp)    
TestCaseTest("testRunning").run()
```
테스트 자체도 단순화 할 수 있다. 위에 코드를 보면 두 경우 모두 test= WasRun("testMethod")  로 `WasRun` 인스턴스를 둘다 생성하는데,

이를 `setUp` 에서 생성하고 아래와 같이 테스트 메소드에서 그걸 사용하도록 할 수 있다.


```python
from TestCase import TestCase
from WasRun import WasRun

class TestCaseTest(TestCase):
    def setUp(self):
        self.test= WasRun("testMethod")
    def testRunning(self):
        # test= WasRun("testMethod") 삭제
        self.test.run() 
        assert(self.test.wasRun)
    def testSetUp(self):
        # test = WasRun("testMethod") 삭제
        self.test.run()
        assert(self.test.wasSetUp)    
TestCaseTest("testRunning").run()
```

```
<to-do list>
//Invoke test method
//Invoke setUp first
Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests
Report collected results
```

다음장에서는 테스트 메소드 후 실행될 tearDown()을 구현한다.

- Decided that simplicity of test writing was more important than performance for the moment
- Tested and implemented `setUp()`
- Used `setUp()` to simplify the example test case
- Used `setUp()` to simplify the test cases checking the example test case (I told you this would become like self-brain-surgery.)
