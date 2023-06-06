### Chapter 18. First Steps To xUnit
<br>

### Test List
- **Invoke test method**
- Invoke setUp first
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- Run multiple tests
- Report collected results

<br>

---
- 테스트 메서드가 호출되면 true를 출력하고 그렇지 않으면 false를 출력하는 간단한 프로그램이 필요합니다
- 테스트 케이스가 테스트 메서드 내에서 플래그를 설정한다면, 테스트가 끝난 후 플래그를 출력하여 올바른지 확인할 수 있습니다.
- WasRun이라는 class는 우리가 테스트할 test case이다. testMethod가 실행되었는지를 보고한다. assert test.wasRun과 같이 작성할 예정이다. 
  
```python
class WasRun:
    def __init__(self, name) -> None:
        self.wasRun = None

test = WasRun("testMethod")
print(test.wasRun)
test.testMethod()
print(test.wasRun)
```
- self.wasRun = None은 testMethod가 실행 안됐어. 라는 의미.
- wasRun이란 값은 실행되었는지 여부, 를 보여주는 result값

```
AttributeError: 'WasRun' object has no attribute 'testMethod'
```

```python
class WasRun:
    def __init__(self, name) -> None:
        self.wasRun = None

    def testMethod(self):
        self.wasRun = 1
```
- Next we need to use our real interface, run(), instead of calling the test method directly. run()이 testMethod()를 call할 수 있도록 refactoring.

```python
test.run()

class WasRun:
    def __init__(self, name) -> None:
        self.wasRun = None

    def testMethod(self):
        self.wasRun = 1

    def run(self):
        self.testMethod()
```
- 다음 단계는, testMethod를 dinamically 호출하는 것. test case의 name을 입력하면, 그 이름에 해당하는 method를 function처럼 호출하는 것이다.
- run()의 method에서 getattr()를 활용해 입력된 값을 받아 method()와 같이 함수로 바꿔준다. 
- getattr()는 parameter로 (class, attribute)를 받는다.

```python
class WasRun:
    def __init__(self, name) -> None:
        self.wasRun = None
        self.name = name

    def testMethod(self):
        self.wasRun = 1

    def run(self):
        method = getattr(self, self.name)
        method()
```

- TestCase라는 superclass를 생성하고 WasRun을 subclass로 바꿔주자.

```python
class TestCase:
    def __init__(self, name) -> None:
        self.name = name
    
    def run(self):
        method = getattr(self, self.name)
        method()
        
class WasRun:
    def __init__(self, name) -> None:
        self.wasRun = None
        TestCase.__init__(self, name)
        
    def testMethod(self):
        self.wasRun = 1
```
- test를 run하기 위해서 새로운 test식을 만들어 보자.


```python
class TestCaseTest(TestCase):
    def testRunning(self):
        test = WasRun("testMethod")
        assert(not test.wasRun)
        test.run()
        assert(not test.wasRun)

TestCaseTest("testRunning").run()
```

```
AttributeError: 'WasRun' object has no attribute 'run'
```
- 흠....??