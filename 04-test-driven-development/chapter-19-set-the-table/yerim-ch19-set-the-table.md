# Chapter 19. Set the Table

테스트를 작성하다보면 공통된 패턴을 발견하게 될 것이다. Bill Wake는 이 패턴을 3A라고 부른다.
1. 준비(arrange) - 객체를 생성한다.
2. 행동(act) - 어떤 자극을 준다.
3. 확인(assert) - 결과를 검사한다.

- TO DO List
    - ~~Invoke test method~~
    - **Invoke setUp first**
    - Invoke tearDown afterward
    - Invoke tearDown even if the test method fails
    - Run multiple test
    - Report collected result

두 번째와 세 번째 단계인 행동과 확인 단계는 항상 다르지만 처음 단계인 준비 단계는 여러 테스트에 걸쳐 동일한 경우가 종종 있다. 예를 들어 객체 7과 9가 있다고 해보자 첫번째 테스트에서는 더하기로 16이 나와야 하고 두번째 테스트에서는 곱하기로 63이 나와야 한다. 여기서 객체 7과 9는 항상 반복된다.

우리는 이 패턴에서 테스트를 위해 새로운 객체를 얼마나 자주 생성해야 하는가 하는 문제에 대해 직면하게 되는데 이 때 다음 두 가지 제약이 상충된다.

- 성능(Performance) - 우린 테스트가 될 수 있는 한 빨리 실행되길 원한다. 여러 테스트에서 같은 객체를 사용한다면 객체 하나만 생성해서 모든 테스트가 이 객체를 쓰게 할 수 있을 것이다.
- 격리(Isolation) - 우린 한 테스트에서의 성공이나 실패가 다른 테스트에 영향을 주지 않기를 원한다. 만약 테스트들이 객체를 공유하는 상태에서 하나의 테스트가 공유 객체의 상태를 변경한다면 다음 테스트의 결과에 영향을 미칠 가능성이 있다.

테스트 사이의 커플링은 확실히 지저분한 결과를 야기한다. 한 테스트가 깨지면 다음 열 개의 테스트 코드가 올바르더라도 같이 깨지는 식이다. 또한 드물지만 매우 어려운 문제가 야기될 수 있다. 테스트가 실행되는 순서가 중요한 경우가 있는데, 만약 테스트 B를 실행하기 전에 테스트 A를 실행하면 둘 다 제대로 작동하지만 A를 실행하기 전에 B를 실행하면 A가 실패한다든지 이런 문제 상황이 나올 수 있다.

그러므로 테스트 커플링을 만들지 말자. 지금 일단 객체 생성을 충분히 빠르게 할 수 있다고 가정해보자. 이럴 경우 테스트가 돌 때마다 객체가 생성되길 원한다.

이것을 흉내낸 형태를 이전에 `WasRun` 에서 봤는데 거기에선 우리가 테스트를 실행하기 전에 플래그를 거짓으로 두었다.

이것을 이용해 한 걸음 진전시키자.

```python
# TestCaseTest class
def testSetUp(self):
    test = WasRun("testMethod")
    test.run()
    assert(test.wasSetUp)
```

파이썬이 `wasSetUp`속성이 없다고 알려주므로 테스트를 성공시키기 위해 수정하자.

```python
# WasRun class
def setUp(self):
  self.wasSetUp = 1
```

이제 이 메소드를 호출하면 `wasSetUp` 이 설정될 것이다. `setUp` 을 호출하는 것은 `TestCase` 가 할 일이니 그곳을 확인해보자.

```python
# TestCase class
def setUp(self):
  pass

def run(self):
  self.setUp()
  method = getattr(self, self.name)
  method()
```

테스트 케이스 하나를 돌아가기 위해서는 두 단계가 필요하다. 이렇게 까다로운 상황에서는 너무 많은 단계이다. 한번에 메소드를 하나 이상 수정하지 않으면서 테스트가 통과하게 만들 수 있는 방법을 찾아내려고 노력해보자.

방금 만든 기능을 적용해서 우리 테스트를 짧게 줄일 수 있다. 우선 `wasRun` 플래그를 `setUp` 에서 설정하도록 하면 `WasRun` 을 단순화 할 수 있다.

```python
# WasRun class
def setUp(self):
  self.wasRun = None
  self.wasSetUp = 1
```

테스트를 실행하기 전에 플래그를 검사하지 않도록 `testRunning` 을 단순화한다.

```python
# TestCaseTest class
def testRunning(self):
  test = WasRun("testMethod")
  test.run()
  assert(test.wasRun)
```

테스트 자체도 단순화 할 수 있다. 두 경우 모두 `WasRun` 인스턴스를 생성하는데 이를 `setUp` 에서 생성하고 테스트 메소드에서 그걸 사용하도록 할 수 있다.

```python
# TestCaseTest class
def setUp(self)
	self.test = WasRun("testMethod")

def testRunning(self):
  self.test.run()
  assert(self.test.wasRun)

def testSetUp(self):
  self.test.run()
  assert(self.test.wasSetUp)
```

- TO DO List
    - ~~Invoke test method~~
    - ~~Invoke setUp first~~
    - Invoke tearDown afterward
    - Invoke tearDown even if the test method fails
    - Run multiple test
    - Report collected result

다음 장에서는 테스트 메소드가 실행된 후에 호출될 `tearDown()` 을 구현할 것이다. 리뷰해보자.
- Decided that simplicity of test writing was more important than performance for the moment
- Tested and implemented `setUp()`
- Used `setUp()` to simplify the example test case
- Used `setUp()` to simplify the test cases checking the example test case (I told you this would become like self-brain-surgery.)