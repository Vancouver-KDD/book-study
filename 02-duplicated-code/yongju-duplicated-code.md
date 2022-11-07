# Duplicate code

같은 code structure가 두 번 이상 존재할 때 우리가 만약 중복된 코드를 합칠 수 있다면, 더 좋은 프로그램을 작성할 수 있다

- 같은 클래스 안에 서로 다른 method들이 같은 expression을 사용한다면 `Extract Function`을 적용할 수 있다

- 비슷하지만 완벽하게 같지는 않은 코드가 있다면, `Slide Statements`를 적용할 수는 없는지 확인해본다

- 중복 코드가 같은 base class를 공유한다면, `Pull Up Method`를 적용할 수 있다

### Example of 'Pull Up Method'

```java

  // Before
  public class AirConditioner {
    private Power power;

    void turnOn() {
      power.on();
    }
  }

  public class Fan {
    void turnOn() {
      power.on();
    }
  }

  // After
  public abstract class Electronic {
    Power power;

    public Electronic(Power power) {
      this.power = power;
    }

    abstract void turnOn() {
      if (power == null) throw new PowerNotDefinedException();
      power.on;
    }
  }

  public class AirConditioner extends Electronic {
    public AirConditioner() {
      Power airConditionalPower = new Power();
      super(airConditionalPower);
    }
  }

  public class Fan extends Electronic {
    public Fan() {
      Power fanPower = new Power();
      super(fanPower);
    }
  }
```