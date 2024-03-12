### Chapter 19. Set the Table
<br>

### Test List
- ~~Invoke test method~~
- ~~Invoke setUp first~~
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- Run multiple tests
- Report collected results
- Log string in WasRun

<br>

---
- test가 setUp()에서 외부 리소스를 할당해야 할 때도 있다. 테스트가 독립적으로 유지되려면 테스트가 완료되기 전에 그 리소스를 해제해야 한다. tearDown() method에서 해제하는 것이 일반적이다. 
- 또는 또 다른 flag를 도입하는 것이다. 
  
```python
WasRun
def setUp(self):
    self.wasRun= None
    self.wasSetUp= 1
    self.log= "setUp"

def testMethod(self):
    self.wasRun= 1
    self.log= self.log + "testMethod"

TestCaseTest
def testSetUp(self):
    self.test.run()
    assert("setUp " == self.test.log)

def setUp(self):
    self.test= WasRun("testMethod")

def testTemplateMethod(self):
    self.test.run()
    assert("setUp testMethod " == self.test.log)
```
- WasRun의 인스턴스를 한 곳에서만 사용하므로 setUp을 되돌려야 한다.
   
```python
TestCaseTest
def testTemplateMethod(self):
    test= WasRun("testMethod")
    test.run()
    assert("setUp testMethod" == test.log)
```
- tearDown()을 만들어보자. 
```python
TestCase
def run(self, result):
    result.testStarted()
    self.setUp()
    method = getattr(self, self.name)
    method()
    self.tearDown()

WasRun
def setUp(self):
    self.log= "setUp"

def testMethod(self):
    self.log= self.log + "testMethod"

def tearDown(self):
    self.log= self.log + "tearDown"
```
- error 발생, testcase에 추가해줘야 한다. 

```python
TestCase
def tearDown(self):
    pass
```
