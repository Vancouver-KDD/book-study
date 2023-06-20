### Chapter 28. Green Bar Patterns

<br>


<br>

---
- Fake It (’Til You Make It)
  - 어떻게 하면 테스트를 빠르게 작동시킬 수 있을지 고민하고 빠른 해결책을 적용하는 것
- Triangulate
  -  두 개 이상의 예제가 있는 경우에만 추상화를 수행
- Obvious Implementation
- One to Many

```java
public void testSum() {
  assertEquals(5, sum(5));
  }
  private int sum(int value) {
  return value;
  }

public void testSum() {
  assertEquals(5, sum(5, new int[] {5}));
  }
  private int sum(int value, int[] values) {
  return value;
  }

private int sum(int value, int[] values) {
  int sum= 0;
  for (int i= 0; i<values.length; i++)
  sum += values[i];
  return sum;
  }

public void testSum() {
  assertEquals(5, sum(new int[] {5}));
  }
  private int sum(int[] values) {
  int sum= 0;
  for (int i= 0; i<values.length; i++)
  sum += values[i];
  return sum;
  }

public void testSum() {
  assertEquals(12, sum(new int[] {5, 7}));
  }
```