# Chapter 4. Privacy

이제 챕터 3에서 작성한 `equals` 더 명확하게 코드가 어떤 것을 하는지 명시 할 수 있다.

```java
public void testMultiplication() {
    Dollar five= new Dollar(5);
    Dollar product= five.times(2);
    assertEquals(new Dollar(10), product);
    product= five.times(3);
    assertEquals(new Dollar(15), product);
}
```

더이상 product이 많은 도움을 주지 않으므로, 이것을 inline한다.

```java
public void testMultiplication() {
    Dollar five= new Dollar(5);
    assertEquals(new Dollar(10), five.times(2));
    assertEquals(new Dollar(15), five.times(3));
}
```

위에 테스트 코드에서 더이상 five의 amount 값을 테스트 코드에서 접근하지 않고, 오직 `Dollar` 오브젝트 내에서만 레퍼런스 하기 때문에 `amount` 필드값을 private으로 변경합니다.

```java
class Dollar{
    private int amount;
}
```

TDD는 perfection을 추구하는 것이 아닌, 코드와 테스트를 동시에 진행함으로서 잠재되어있는 결함들을 최소화 하려는 것에 목적이 있습니다.
