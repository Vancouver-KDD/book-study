# Chapter 19. Set the Table

```python
class TestCase:
    def __init__(self, name):
        self.name = name

    def run(self):
        method = getattr(self, self.name)
        method()

class WasRun(TestCase):
    def __init__(self, name):
        self.wasRun = None
        TestCase.__init__(self, name)

    def testMethod(self):
        self.wasRun = 1

class TestCaseTest(TestCase):
    def testRunning(self):
        test = WasRun("testMethod")
        assert not test.wasRun
        test.run()
        assert test.wasRun

TestCaseTest("testRunning").run()
```

테스트를 작성 시, 3A라하는 공통된 패턴이있다.
1. Arrange (준비) - 객체를 생성한다.
2. Act (행동) - 어떤 자극을 준다.
3. Assert (확인) - 결과를 검사한다.

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

테스트를 실행하기 전에 플래그를 false로 설정한 WasRun와 마찬가지로 TestCaseTest에서 setUp이 되었는지를 확인하는 코드를 아래와 같이 가져간다.

```python
# TestCaseTest
def testSetUp(self):
    test = WasRun("testMethod")
    test.run()
    assert(test.wasSetUp)

# WasRun 
    def setUp(self):
        self.wasSetUp = 1
```

이제 추가된 `setUp` 메소드를 호출하면 `wasSetUp` 이 설정될 것이다. `TestCase`에서는 `setUp`을 호출하는 코드가 필요할 것이다.
```python
class TestCase: 
    def __init__(self, name):
        self.name= name
    def setUp(self):   # <<- 추가
        pass             # <<- 추가
    def run(self):
        self.setUp()     # <<- 추가
        method = getattr(self, self.name)
        method()
```

위의 경우 테스트 케이스 하나를 위해 두단계가 필요하게 설정이 되어져있는데, 이는 좀 더 까다로운 상황을 만나게 되면 좀 과한 편에 속하게 된다. 
하나로 해서 테스트를 성공하는 방법을 찾아보면, 

wasRun 클래스의 flag 를 setUp에서 설정하여 `WasRun` 줄일 수 있다.

```python
# WasRun 클래스
    def setUp(self):
      self.wasRun = None
      self.wasSetUp = 1
```
위 수정된 코드를 기존코드 적용,

```python
class WasRun(TestCase): 
    def __init__(self, name):
        self.wasRun= None   
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
테스트 자체도 단순화 할 수 있다. 위에 코드를 보면 testSetUp과 testRunning 두 경우 모두 test= WasRun("testMethod") 로 `WasRun` 인스턴스를 둘다 생성하는데,

이를 `setUp` 에서 생성하고  그걸 사용하도록 한다.

```python
class TestCaseTest(TestCase):
    def setUp(self):
        self.test= WasRun("testMethod")
    def testRunning(self):
        # test= WasRun("testMethod") <<- 삭제
        self.test.run() 
        assert(self.test.wasRun)
    def testSetUp(self):
        # test = WasRun("testMethod") <<- 삭제
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
<Review>
- Decided that simplicity of test writing was more important than performance for the moment - 단순성이 성능보다 더 중요하다고 결정
- Tested and implemented `setUp()` - setUp() 테스트 및 구현
- Used `setUp()` to simplify the example test case - 테스트 케이스를 단순화하기 위해 setUp() 사용
- Used `setUp()` to simplify the test cases checking the example test case - 테스트 케이스의 체크하는 테스트 케이스를 단순화하기 위해 setUp() 사용
