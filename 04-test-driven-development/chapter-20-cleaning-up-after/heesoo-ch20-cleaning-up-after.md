# Chapter 20. Cleaning Up After

testing framework todo list

- ~~Invoke test method~~
- ~~Invoke setUp first~~
- **Invoke tearDown afterward**
- Invoke tearDown even if the test method fails
- Run multiple tests
- Report collected results

테스트는 가끔 `setUp()` 안에 외부요소들을 가져올 필요가 있음 -> 테스트를 계속 독립적으로 유지하고 싶으면 외부 요소를 사용하는 테스트를 먼저 실행시켜줘야함 -> `tearDown()` method

We will append the log -> methods 가 불려지는 순서를 보존할 수 있음

testing framework todo list

- ~~Invoke test method~~
- ~~Invoke setUp first~~
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- Run multiple tests
- Report collected results
- **Log string in WasRun**

```
class WasRun(TestCase):
    def __init__(self, name):
        self.wasRun = None
        TestCase.__init__(self,name)

    def setUp(self):
        self.wasRun = None
        self.wasSetUp = 1  // 1.삭제
        self.log = self.log + "testMethod"  // 3.추가
```

```
class TestCaseTest(TestCase):
    def setUp(self):
        self.test = WasRun("testMethod")
    def testRunning(self):  // 4. 삭제
        self.test.run()
        asset(self.test.wasRun)
    def testSetUp(self):  // 5. Rename
        self.test.run()   // 6. undo
        asset("setUp testMethod" == self.test.log)  // 2.변경

```

1. log 를 추가
2. `testSetUp()` 이 flag 대신에 log를 확인하도록 변경
3. `wasSetUp` flag 를 삭제할 수 있음
4. `testSetUp()` 이 test running 을 record 하기 때문에 `testRunning` 삭제해도 됨
5. Rename `testSetUp` to `testTemplateMethod`
6. `setUp` 에 test instance를 삭제했던 걸 다시 돌려놔야해

```
class TestCaseTest(TestCase):
    def setUp(self):
        self.test = WasRun("testMethod")
    def testTemplateMethod(self):
        test = WasRun("testMethod")
        test.run()
        asset("setUp testMethod" == test.log)

```

testing framework todo list

- ~~Invoke test method~~
- ~~Invoke setUp first~~
- **Invoke tearDown afterward**
- Invoke tearDown even if the test method fails
- Run multiple tests
- Report collected results
- ~~Log string in WasRun~~

이제 `tearDown()` 할 준비가 됨 -> log에 추가하는 테스트 추가

```
class TestCaseTest(TestCase):
    ...
    def testTemplateMethod(self):
        test = WasRun("testMethod")
        test.run()
        asset("setUp testMethod tearDown" == test.log)

```

이 테스트는 실패함 -> 작동하게 먼저 만들기

```
class TestCase:
    ...
    def run(self, result):
        result.testStarted()
        self.setUp()
        method = getattr(self, self.name)
        method()
        self.tearDown()
    def tearDown(self):
        pass
```

```
class WasRun(TestCase):
    ...
    def setUp(self):
        self.log = "setUp"
    def testMethod(self):
        self.log = self.log + "testMethod"
    def tearDown(self):
        self.log = self.log + "tearDown"
```

이건 `TestCaseTest` 에서 에러가남 -> 우리는 `TestCase` 에 `tearDown()` implementation 이 없기 떄문에 -> 추가

- test strategy 를 flags 에서 log로 변경함
- `teatDown()`을 새로운 log 를 통해서 테스트하고 실행시킴
