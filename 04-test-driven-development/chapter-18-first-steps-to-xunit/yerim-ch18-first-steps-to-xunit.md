# Chapter 18. First Steps to xUnit

2부는 테스트 주도 개발을 통해 '실제' 소프트웨어를 만드는 발전된 예제로 생각하면 된다. 또 2부를 자기 참조(self-referential) 프로그래밍에 대한 computer-sciency 실습으로 생각할 수 있다.

우선 테스트 케이스를 만들고 테스트 메소드를 실행할 수 있어야 한다.

예를 들면 `TestCase("testMethod")`과 같이 말이다. 하지만 우리는 테스트 케이스를 작성하기 위해 사용할 프레임워크를 테스트 하기 위한 테스트 케이스를 작성해야 한다.

아직 프레임워크가 없기 때문에 첫 번째 작은 단계를 수동적으로 검증해야 할 것이다.

우리는 이전처럼 모든 것을 검증하면서 작은 단계를 밟아 나갈 것이다. 다음은 프레임워크를 테스트하기 위한 투두리스트이다.

- TO DO List
    - **Invoke test method**
    - Invoke setUp first
    - Invoke tearDown afterward
    - Invoke tearDown even if the test method fails
    - Run multiple test
    - Report collected result

물론 이번에도 테스트를 먼저 만들 것이다. 우리의 첫 번쨰 프로토 테스트에는 테스트 메소드가 호출되면 `true` 그렇지 않으면 `false`를 반환하는 프로그램이 필요하다. 테스트 메소드 안에 플래그를 설정하는 테스트 케이스가 있다면, 그 테스트 케이스가 실행된 이후에 플래그를 print해 볼 수 있고, 그러면 그게 맞는지 아닌지 확인해 볼 수 있다. 

부트스트랩 테스트를 위한 전력이 있다. 플래그를 가지고 있는 테스트 케이스를 만드는 것이다. 테스트 메소드가 실행되지 전에는 플래그가 `false`인 상태여야 하고 그 이후 테스트 메소드가 플래그를 설정할 것이고, 테스트 메소드가 실행된 후에는 플래그가 `true`여야 한다.

메소드가 실행되었는지 알려주는 테스트 케이스이므로 클래스 이름을 `WasRun`이라 하자. 플래그 역시 `wasRun`으로 하겠다.

파이썬은 파일에서 읽는 순서대로 명령문을 실행하기 때문에 테스트 메소드를 수동으로 호출하는 식으로 시작할 수 있다. 
```
test = WasRun("testMethod")
print test.wasRun
test.testMethod()
print test.wasRun
```

우리가 예상하는 이 코드는 테스트 메소드가 실행하기 전에 `None`을 호출하고 그 후에 `1`을 출력할 것으로 예상한다. (파이썬에서는 `None` = `null`이고, `0`이나 다른 몇몇 객체와 함께 논리 값 `false`를 의미한다.)

하지만 우리는 `WasRun` 클래스를 정의하지 않았으므로 예상대로 실행되지는 않는다.
```python
class WasRun:
  pass
```

```python
class WasRun:
  def __init__(self, name):
  self.wasRun = None
```

```python
class WasRun:
  def __init__(self, name):
  self.wasRun = None
  
  def testMethod(self):
    pass
```

```python
class WasRun:
  def __init__(self, name):
    self.wasRun = None
  
  def testMethod(self):
    self.wasRun = 1
```

이제 우리는 정답을 얻었다. 이후에 해야 할 리팩토링이 많이 남았지만 일단 진전한 것이다. 

다음으로 필요한 것은 테스트 메소드를 직접 호출하는 대신 진짜 인터페이스인 `run()`을 사용하는 것이다. 테스트는 다음과 같이 변경되어야 한다.

```python
test = WasRun("testMethod")
print test.wasRun
test.run()
print test.wasRun
```
구현 코드는 다음과 같다.
```python
class WasRun:
  def __init__(self, name):
  self.wasRun = None
  
  def testMethod(self):
    self.wasRun = 1

  def run(self):
    self.testMethod()
```
다음 단계는 `testMethod`를 동적으로 호출하는 것이다. 파이썬의 가장 멋진 특성 중 하나는 클래스의 이름이나 메소드의 이름을 함수처럼 다룰 수 있다는 점이다.

테스트 케이스의 이름과 같은 문자열을 갖는 필드가 주어지면 함수로 호출될 때 해당 메소드를 호출하게끔 하는 객체를 얻어낼 수 있다.

```python
class WasRun:
  def __init__(self, name):
    self.wasRun = None
    self.name = name
  
  def run(self):
    method = getattr(self, self.name)
    method(); 
```

여기에는 리팩토링의 일반적인 패턴이 있다. 하나의 특별한 사례에 대해서만 작동하는 코드를 다른 여러 사례에 대해서도 동작할 수 있도록 상수를 변수로 일반화 시키는 것이다. 우리의 경우에 상수는 데이터 값이 아니라 하드 코딩된 코드이지만 이 경우에도 원리는 같다.

이제 작은 `WasRun` 클래스는 독립된 두 가지 일을 한다. 하나는 메소드가 호출되었는지 그렇지 않은지를 기억하는 일이고, 또 다른 하나는 메소드를 동적으로 호출하는 일이다.

이제 `TestCase` 상위 클래스를 만들고 `WasRun`이 이를 상속받도록 하자.
```python
# TestCase class
class TestCase:
  pass

# WasRun Class
class WasRun(TestCase):
  def __init__(self, name):
    self.wasRun = None
    self.name = name
  
  def run(self):
    method = getattr(self, self.name)
    method(); 
```

```python
# TestCase class
class TestCase:
  def __init__(self, name):
    self.name = name

# WasRun Class
class WasRun(TestCase):
  def __init__(self, name):
    self.wasRun = None
    TestCase.__init__(self,name)
```

```python
# TestCase class
class TestCase:
  def __init__(self, name):
    self.name = name

# WasRun Class
class WasRun(TestCase):
  def __init__(self, name):
    self.wasRun = None
    TestCase.__init__(self,name)
```

```python
# TestCase class
class TestCase:
  def __init__(self, name):
    self.name = name
	
  def run(self):
    method = getattr(self, self.name)
    method()

# WasRun Class
class WasRun(TestCase):
  def __init__(self, name):
    self.wasRun = None
    TestCase.__init__(self,name)
```
모든 스텝마다 같은 답변을 받을 수 있게 테스트를 실행시켰다. 

매번 `None`과 `1`이 나오는지 확인하는 것도 지겹기 때문에 우리가 만든 코드를 다음과 같이 바꿀 수 있다.

```python
# TestCaseTest class

class TestCaseTest(TestCase):
  def testRunning(self):
    test = WasRun("testMethod")
    assert(not test.wasRun)
    test.run()
    assert(test.wasRun)

TestCaseTest("testRunning").run()
```
테스트 코드의 내용을 단순히 `print`문에서 `assert`문으로 변경한 것이다.

- TO DO List
    - ~~Invoke test method~~
    - Invoke setUp first
    - Invoke tearDown afterward
    - Invoke tearDown even if the test method fails
    - Run multiple test
    - Report collected result


이렇게까지 작은 단계 별로 작업할 필요까진 없지만 우리가 TDD를 마스터하고 나면, 좀 더 큰 단계씩 작업할 수 있을 것이다. 

다음 장에서는 `setUp`을 호출하는 부분을 다룰 것이다. 그 전에 지금까지 한 것들을 리뷰해보자.
- 자만심에 불타오르는 몇 번의 잘못된 시작 후에, 아주 작은 발걸음으로 시작하는 방법을 알아냈다.
- 먼저 하드코드로 연결한 다음 상수를 변수로 대체하여 기능을 보다 일반적으로 구현했다.
- 코드를 정적으로 분석하기 어렵게 만들기 때문에 최소 4개월 동안 다시 사용하지 않겠다고 약속하는 Pluggable Selector를 사용했다.
- 테스트 프레임워크를 모두 작은 단계로 부트스트랩했다.