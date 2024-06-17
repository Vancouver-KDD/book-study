### Chapter 19. Set the Table
<br>

### Test List
- ~~Invoke test method~~
- **Invoke setUp first**
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- Run multiple tests
- Report collected results

<br>

---
Patter for writing tests.
1. Arrange—Create some objects.
2. Act—Stimulate them.
3. Assert—Check the results.

- 그렇다면 새로운 객체를 얼마나 자주 생성해야 할까? 두 가지 constaints가 서로 충돌하게 된다.
  - Performance: 객체를 한 번 생성해서 모든 테스트에서 재사용하고 싶다
  - Isolation: 한 test의 성공/실패가 다른 테스트에 영향을 미치지 않았으면 좋겠다. Test coupling 이라고 부른다.
- 테스트가 실행될 때마다 객체를 생성하도록, test식을 작성해보자.

```python
class TestCaseTest(TestCase):
    def testSetUp(self):
        test = WasRun("testMethod")
        test.run()
        assert(test.wasSetUp) 

class WasRun:
    def setUp(self):
        self.wasSetUp = 1

TestCaseTest("testSetUp").run()
```
- setUp을 calling하는 것은 TestCase의 job이므로 옮겨야 한다. 
  
```python
class TestCase:
    def setUp(self):
        pass
        
    def run(self):
        self.setUp()
        method = getattr(self, self.name)
        method()
```
- WasRun을 simplify해보자
  
```python
class TestCase:
    def __init__(self, name) -> None:
        self.name = name

    def setUp(self):
        pass
        
    def run(self):
        self.setUp()
        method = getattr(self, self.name)
        method()
        

class WasRun:
    def __init__(self, name) -> None:
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
        assert(self.test.wasRun)

    def testSetUp(self):
        self.test.run()
        assert(self.test.wasSetUp)  
```
