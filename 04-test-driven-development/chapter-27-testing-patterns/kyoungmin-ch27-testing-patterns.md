### Chapter 27. Testing Patterns

<br>


<br>

---
- Child Test
  - 레드/그린/리팩터링의 리듬
  - 작게 테스트 해보라
- Mock Object
  - 실제 데이터베이스를 사용하지 않는 것
  - 데이터베이스처럼 작동하지만 실제로는 메모리에만 있는 객체를 기반으로 작성
- Self Shunt
  - 테스트 케이스와 테스트 대상 객체 간에 통신

```python
ResultListenerTest
def testNotification(self):
  result= TestResult()
  listener= ResultListener()
  result.addListener(listener)
  WasRun("testMethod").run(result)
  assert 1 == listener.count

ResultListener
class ResultListener:
  def __init__(self):
    self.count= 0
  def startTest(self):
    self.count= self.count + 1

ResultListenerTest
def testNotification(self):
  self.count= 0
  result= TestResult()
  result.addListener(self)
  WasRun("testMethod").run(result)
  assert 1 == self.count
def startTest(self):
  self.count= self.count + 1
```

- Log String
  - 메시지 호출 순서가 올바른지 테스트하기 위해 문자열(log)에 로그를 기록하고, 메시지가 호출될 때마다 문자열에 추가. 이를 통해 메소드 호출의 예상 순서를 확인할 수 있다
- Crash Test Dummy
  - 드물게 발생할 것으로 예상되는 오류 코드를 테스트하는 방법은, 실제 작업을 수행하는 대신 예외를 던지는 특수한 객체를 사용하여 해당 코드를 호출하는 것
- Broken Test
- Clean Check-in
  - 팀으로 프로그래밍을 진행할 때는 어떻게 세션을 마무리해야 할까요? 모든 테스트를 실행한 상태로 남겨둡니다.
  
  