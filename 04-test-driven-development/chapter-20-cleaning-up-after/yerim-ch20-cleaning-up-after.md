# Chapter 20. Cleaning Up After

- TO DO List
    - ~~Invoke test method~~
    - ~~Invoke setUp first~~
    - **Invoke tearDown afterward**
    - Invoke tearDown even if the test method fails
    - Run multiple test
    - Report collected result

가끔은 `setUp()` 에서 외부 자원을 할당하는 경우가 있다. 테스트가 계속 서로 독립적이길 바란다면 외부 자원을 할당받은 테스트 들은 작업을 마치기 전에 `tearDown()` 메소드 같은 곳에서 자원을 다시 반환할 필요가 있다.

단순하게 생각하면 할당 해제를 위한 테스트 방법은 역시 또 하나의 플래그를 도입하는 것이다. 하지만 이제는 이 플래그들이 귀찮게 만들기도 하고 플래그를 사용하는 방식은 메소드의 중요한 면을 하나 놓치고 있다. 바로 `setUp()` 은 테스트 메소드가 실행되기 전에 호출되야 하고 `tearDown()` 은 테스트 메소드가 호출된 후에 실행되어야 한다.

여기선 호출된 메소드의 로그를 간단히 남기는 방식으로 테스트 전략을 바꿀 생각이다. 항상 로그의 끝부분에만 기록을 추가한다면 메소드의 호출 순서를 알 수 있게 될 것이다.

- TO DO List
    - ~~Invoke test method~~
    - ~~Invoke setUp first~~
    - Invoke tearDown afterward
    - Invoke tearDown even if the test method fails
    - Run multiple test
    - Report collected result
    - **Log string in WasRun**

```python
# WasRun class
def setUp(self):
  self.wasRun = None
  self.wasSetUp = 1
  self.log = "setUp "
```

이제 `testSetUp()` 이 플래그 대신에 로그를 검사하도록 변경할 수 있다.

```python
# TestCaseTest class
def testSetUp(self):
  self.test.run()
  assert("setUp " == self.test.log)
```

다음으로 이제 `wasSetUp` 플래그를 지울 수 있다. 그리고 테스트 메소드의 실행을 기록하자.

```python
# WasRun class
def testMethod(self):
  self.wasRun = 1
  self.log = self.log + "testMethod "
```

이 작업은 `TestCaseTest` 의 `testSetUp()` 을 실패하게 만드니까 다시 수정하자.

```python
# TestCaseTest class
def testSetUp(self):
  self.test.run()
  assert("setUp testMethod " == self.test.log)
```

이제 이 테스트는 두 개의 테스트가 할 일을 모두 수행한다. 따라서 `testRunning` 을 지우고 `testSetUp` 의 이름을 바꿔주자.

```python
# TestCaseTest class
def setUp(self):
  self.test = WasRun("testMethod")
  
def testTemplateMethod(self):
  self.test.run()
  assert("setUp testMethod " == self.test.log)
```

`TestCaseTest` 를 보면 `WasRun` 인스턴스를 한 곳에만 사용하니까 다시 분리했던 부분을 돌려놓자.

```python
# TestCaseTest class
def testTemplateMethod(self):
  test = WasRun("testMethod")
  test.run()
  assert("setUp testMethod " == self.test.log)
```

앞에서 사용했던 것들을 리팩토링 했다가 다시 되돌리는 작업은 자주 있는 일이다. 어떤 사람들은 했던 일을 되돌리는 것을 싫어해서 중복이 서너 번 발생할 때까지 기다리기도 한다. 저자는 사고 주기를 설계에 써버리는 것을 선호하기 때문에 직후에 바로 취소하건 말건 반사적으로 리팩토링한다.

- TO DO List
    - ~~Invoke test method~~
    - ~~Invoke setUp first~~
    - **Invoke tearDown afterward**
    - Invoke tearDown even if the test method fails
    - Run multiple test
    - Report collected result
    - ~~Log string in WasRun~~

이제 `tearDown()` 을 테스트 할 준비가 됐다.

```python
# TestCaseTest class
def testTemplateMethod(self):
	test = WasRun("testMethod")
  test.run()
  assert("setUp testMethod tearDown" == test.log)
```

실패. 성공하게 만들어보자.

```python
# TestCase class
def run(self):
  self.setUp()
  method = getattr(self, self.name)
  method()
  self.tearDown()

def tearDown(self):
  pass
  
# WasRun class
def setUp(self):
  self.log = "setUp "
  
def testMethod(self):
  self.log = self.log + "testMethod "
  
def tearDown(self):
  self.log = self.log + "tearDown "
```

다음 장에서는 `assertion`에 관련한 문제가 있음을 파이썬의 에러 핸들링 및 리포팅 시스템이 보고하게 하는 대신 명확한 테스트 실행 결과를 보고할 수 있도록 하는 기능을 다룰 것이다.

이번 장에서 다룬 내용들을 리뷰해보자.
- Restructured the testing strategy from flags to a log
- Tested and implemented tearDown() using the new log
- Found a problem and, daringly, fixed it instead of backing up (Was that a good idea?)