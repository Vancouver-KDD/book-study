# Long Function

우리는 경험상, 가장 오래 가는 좋은 프로그램은 짧은 function들로 이루어진 프로그램이라는 것을 안다.

function의 길이를 줄이기 위해서는 여러가지 방법이 있는데 99프로의 경우에는 나눌 수 있는 부분을 찾아 새로운 function들을 만드는 `Extract Function`을 적용하면 된다.

그 외에 부수적인 방법으로는 임시로 사용하는 variable을 없앨 수 있는 `Replace Temp with Query`, 많은 parameter 수를 줄일 수 있는 `Introduce Parameter Object`, `Preserve Whole Object`가 있고 그 외에도 `Replace Function with Command`, `Decompose Conditional`, `Replace Conditional with Polymorphism` 등 function의 길이는 상황에 따라 여러가지 방법을 이용해 줄일 수 있다.

### Example of 'Decompose Conditional'

```java
  // Before
  ...
  if (fromRange <= cardNumber && cardNumber <= toRange) {
    // do something
  } else {
    // do something else
  }
  ...

  // After
  if (inRange()) {
    // do something
  } else {
    // do something else
  }

  public boolean inRange() {
    return fromRange <= cardNumber && cardNumber <= toRange;
  }
```