# xUnit 패턴

## Assertion
### Boolean 판단
- assertTure(rectangle.area() != 0); // 불명확
  - => assertEquals(50, rectangle.area()); // 명확
- assertTure(String message, boolean condition) // 메세지 제공

## 픽스쳐
- 중복 제거 in model code(=production code) and test code
- 동일 테스트 객체(=fixture or scaffolding) 의 문제
  - 반복 장성의 시간 소요
  - 인터페이스 변경 = 테스트 변경

```java
class EmptyRectangleTest {
    private Rectangle empty;

    public void setUp() {
        empty = new Rectangle(0,0,0,0);
    }
    //...
}
```
- 다른 픽스쳑가 필요하다면?
```java
class NormalRectangleTest {
    private Rectangle empty;

    public void setUp() {
        empty = new Rectangle(0,10,50,100);
    }
    //...
```

## 외부 픽스쳐
- 외부 자원의 해제 => tearDown()
```Python
setUp(self):
  self.file = File("foobar").open()
testMethod(self):
  ## runthe test
tearDown(self):
  self.file.close()
```
## 테스트 메서드
### 장소
- 각각의 테스트 = Method
- 동일한 픽스쳐 공유 = Class
### 이름
- xUnit계에서는 test로 시작하는 메서드 이름을 가진다.
- 이 이름은 길지언정 이해할수 있을만틈 구체적인고 명확해야한다.
### 아웃라인
- 2-3줄 정도의 짧은 아웃라인으로 테스트 코드를 시작하자.

## 예외 테스트
```java
public void testMissingRate() {
    try {
        exchange.findRate("USD", "GBP");
        fail();
    } catch (IllegalArgumentException expected) {
    } 
}
```
## 전체 테스트
- 최근 주요 IDE에서는 이 기능을 제공