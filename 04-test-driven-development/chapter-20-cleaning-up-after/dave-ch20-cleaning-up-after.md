# Chapter 20. Cleaning Up After

```python
class TestCase:
    def __init__(self, name):
        self.name = name
    def setUp(self):
        pass
    def run(self):
        self.setUp()
        method = getattr(self, self.name)
        method()

class WasRun(TestCase):
    def __init__(self, name):
        self.wasRun = None
        TestCase.__init__(self, name)
    def setUp(self):
        self.wasRun = None
        self.wasSetUp = 1
    def testMethod(self):
        self.wasRun = 1

class TestCaseTest(TestCase):
    def setUp(self):
        self.test = WasRun("testMethod")
    def testRunning(self):
        self.test.run()
        assert self.test.wasRun        
    def testSetUp(self):
        self.test.run()
        assert self.test.wasSetUp
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

때때로 `setUp()` 에서 external resources을 할당할 필요가 있다.  테스트가 loosely-coupled하기 바란다면 external resources가 할당 된 테스트들은 종료되기 전에  `tearDown()` 메소드와 같은 것들을 통해 자원을 다시 반환할 필요가 있다.

파이썬 unit testing 표준 라이브러리인 unittest에서 예를 들어보면, setUp에는 주로 공통으로 시작하는 부분, 셋팅되어야 하는 부분을 작성하고, tearDown은 close와 같이 정리되는 작업을 작성한다. 

호출되는 프로세스는 예를 들어 테스트 함수가 testcase1, testcase2가 있다면 setUp -> testcase1 -> tearDown -> setUp -> testcase2 -> tearDown 순으로 호출된다.

간단한 de-allocation에 대한 테스트를 작성 approach는 flag를 또 도입하는 것인데 이것이 도리어 메소드의 중요한 측면을 놓치게 할 수 있으므로 (`setUp()` 은 테스트 메소드 실행 전에 호출, 그리고 `tearDown()` 은 테스트 메소드 호출 후 실행되야 한다.) 호출된 메소드의 로그를 간단히 남기는 방식으로 전략을 변경해서 로그를 남김으로써 호출되는 순서를 확인한다.

```
<to-do list>
//Invoke test method
//Invoke setUp first
Invoke tearDown afterward
Invoke tearDown even if the test method fails 
Run multiple tests
Report collected results
Log string in WasRun <---
```

```python
class WasRun(TestCase): 
    def __init__(self, name):
        self.wasRun = None
        TestCase.__init__(self, name)
    def setUp(self):
        self.wasRun = None
        self.wasSetUp = 1
	self.log = "setUp " # <<--추가
    def testMethod(self):
        self.wasRun = 1    
```

이제 `testSetUp()`를 flag가 아닌 log를 바라보게 한다.

```python
class TestCaseTest(TestCase):
    def setUp(self):
        self.test= WasRun("testMethod")
    def testRunning(self):
        self.test.run()
        assert self.test.wasRun
    def testSetUp(self):
        self.test.run()
        # assert(self.test.wasSetUp)    <<--변경 전
        assert("setUp " == self.test.log)    # 변경 후
```

WasRun 클래스내 setUp()의  `wasSetUp` flag를 삭제. 그리고 `testMethod`의 실행을 기록

```python
class WasRun(TestCase): 
    def __init__(self, name):
        self.wasRun= None      
        TestCase.__init__(self, name)
    def run(self):
        method = getattr(self, self.name) 
        method()    
    def setUp(self):
        # self.wasRun = None  <<- 삭제
        # self.wasSetUp = 1 <<- 삭제
        self.log= "setUp"
    def testMethod(self):   #<<- 추가
        self.wasRun= 1      #<<- 추가
        self.log= self.log + "testMethod "   #<<- 추가    
```

이헐게 되면, 실제로 로그가 포함하는 값은  "setUp testMethod "가 되어서 아까 TestCaseTest에는 "setUp"으로만 적었으므로 같음("==") 처리가 안되므로 assert문애에 예상되는 값을 수정해서 맞춰준다 아래와 같이
```python
# TestCaseTest class
def testSetUp(self):
  self.test.run()
  assert("setUp testMethod " == self.test.log) # <<-- 맞춰줌
```

이제 해당 테스트가 testRunning과 testSetUp의 역할을 중복 수행함. So, `testRunning` 삭제

`testSetUp` -> `testTemplateMethod`로 Rename

```python
class TestCaseTest(TestCase): 	
    def setUp(self):
        self.test= WasRun("testMethod")
    # def testRunning(self): 삭제 
    #     self.test.run() 
    #     assert(self.test.wasRun)
    def testTemplateMethod(self): # <<- Rename
        self.test.run()
        assert("setUp testMethod " == self.test.log)  
```

`TestCaseTest` 를 보면 `WasRun` 의 instance를 한 곳에만 사용 중이 니깐 self(current object를 pointing to)를 이용해서 앞장에서 줄였던 부분을 다시 돌려놓자.

```python
class TestCaseTest(TestCase):
    def setUp(self):
        self.test= WasRun("testMethod")
    def testTemplateMethod(self):
        test = WasRun("testMethod") 		# <<-원위치
        test.run()                  		# <<-원위치
        assert("setUp testMethod " == test.log)  # <<-원위치
```
 
리팩토링을 수행한 다음 곧 실행 취소해야 하는 것은 매우 일반적이다.  어떤 사람들은 작업 취소를 좋아하지 않기 때문에 리팩토링하기 전에 서너 번 사용할 때까지 기다리지만  저자는 생각의 cycle을 설계에 집중하므로 바로 취소하는 것에 대한 걱정없이 반사적(reflexively)으로 바로 리팩토링한다.

```
<to-do list>
//Invoke test method
//Invoke setUp first
Invoke tearDown afterward <<--
Invoke tearDown even if the test method fails 
Run multiple tests
Report collected results
//Log string in WasRun
```

이제 tearDown을 테스트할 준비가 되었습니다.

```python
class TestCaseTest(TestCase):
    # ...
    def testTemplateMethod(self):
        test = WasRun("testMethod")
        test.run()
        assert "setUp testMethod tearDown " == test.log
```

실행하면 fail, 이를 실행되도록 하려면 
```python
class TestCase: 
    #...
    def run(self):
      self.setUp()
      method = getattr(self, self.name)
      method()
      self.tearDown()
    def tearDown(self):
      pass
  
class WasRun(TestCase): 
    #...
    def setUp(self):
      self.log = "setUp "
      
    def testMethod(self):
      self.log = self.log + "testMethod "
      
    def tearDown(self):
      self.log = self.log + "tearDown "
```

`TestCaseTest` 의 에러 -> `TestCase` 에 `tearDown()`의 no-op(무연산) implementation 필요

적용 아래
```python
class TestCase: 
    def tearDown(self): 
    pass
```

다음장에선 assertion 문제가 있을 때, Python의 기본 오류 처리 및 보고 시스템이 알려주는 결과 대신 테스트 실행 결과를 명시적으로 나타내보고자 한다.

<Review>
* Restructured the testing strategy from flags to a log - 플래그에서 로그로 테스트 전략 재구성
* Tested and implemented tearDown() using the new log - 새 로그를 사용하여 tearDown() 테스트 및 구현
* Found a problem and, daringly, fixed it instead of backing up - 문제를 발견, 과감하게 수정

```python
class TestCase:
    def __init__(self, name):
        self.name = name
    def setUp(self):
        pass
    def run(self):
        self.setUp()
        method = getattr(self, self.name)
        method()
        self.tearDown()
    def tearDown(self):
        pass

class WasRun(TestCase):
    def __init__(self, name):
        self.wasRun = None
        TestCase.__init__(self, name)
    def setUp(self):
        self.log = "setUp "
    def testMethod(self):
        self.log = self.log + "testMethod "    
    def tearDown(self):
        self.log = self.log + "tearDown "

class TestCaseTest(TestCase):
    def setUp(self):
        self.test = WasRun("testMethod")
       
    def testTemplateMethod(self):        
        test = WasRun("testMethod") 
        test.run()
        print(test.log)
        assert("setUp testMethod tearDown " == test.log)
TestCaseTest("testTemplateMethod").run()
```	
