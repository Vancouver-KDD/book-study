# Chapter 19. Set the Table

Common pattern for writing tests

1. Arrange - Create some objects
2. Act - Stimulate them
3. Assert - Check the results

testing framework todo list

- ~~Invoke test method~~
- **Invoke setUp first**
- Invoke tearDown afterward
- Invoke tearDown even if the test method fails
- Run multiple tests
- Report collected results

보통 `arrange` 는 테스트마다 같을 수 도 있는데, `act`는 `assert`유니크함
-> 얼마나 자주 테스트할 new objects 를 만들어야 할까?
-> 두가지 제약이 충돌함

1. Performance : 테스트를 가능한 한 빨리 돌리고 싶음 -> 하나로 모든 테스트 커버
2. Isolation : 하나의 테스트의 성공/실패가 다른 테스트에 영향을 미치게 하고 싶지 않음

`Test Coupling` : 테스트가 서로 연결되어 있어 순서대로 하지 않으면 테스트가 실패함
우리는 여기서 테스트를 할때마다 objects를 생성하고 싶음

```
class TestCaseTest(TestCase):
    def testSetUp(self):
        test = WasRun("testMethod")
        test.run()
        asset(test.wasSetUp)

    def testRunning(self):
        test = WasRun("testMethod")
        assert(not test.wasRun)
        test.run()
        asset(test.wasRun)

TestCasetTest("testSetUp").run()
```

```
class WasRun(TestCase):
    def __init__(self, name):
        self.wasRun = None
        TestCase.__init__(self,name)

    def setUp(self):
        self.wasWetUp = 1
```

`setUp`을 부르는건 `TestCase`의 역할임

```
class TestCase:
    def __init__(self, name):
        self.name = name
    def setUp(self):
        pass
    def run(self):
        self.setUp()
        method = getattr(self, self.name)
        method()
```

우리는 `wasRun` flag 를 `setUp`에서 셋팅하게해서 `WasRun`을 간단하게 만들 수 있음

```
class WasRun(TestCase):
    def __init__(self, name):
        self.wasRun = None
        TestCase.__init__(self,name)

    def setUp(self):
        self.wasRun = None
        self.wasWetUp = 1
```

이제 `testRunning`은 테스트를 돌리기 전에 flag를 체크하지 않음

```
class TestCaseTest(TestCase):
    def setUp(self):
        self.test = WasRun("testMethod")
    def testRunning(self):
        self.test.run()
        asset(self.test.wasRun)
    def testSetUp(self):
        self.test.run()
        asset(self.test.wasSetUp)


TestCasetTest("testSetUp").run()
```

assert(not test.wasRun) //이부분을 제거할 수 있음 -> wasRun에 flag를 셋팅해서 여기서 체크하지 않아도됨

- 테스트 작성을 최대한 간단하게 하는 것이 이번 핵심
- `setUp()` 테스트 및 실행
- 예시 테스트 케이스를 간단하게 하기 위해 `setUp()` 사용
- 테스트케이스를 체크하고 간단화하기 위해 `setUp()` 사용
