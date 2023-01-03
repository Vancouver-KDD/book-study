## Builder Pattern

- 복잡한 구성의 객체를 효과적으로 생성하는 패턴
- 2가지 패턴

#### 1st. 생성시 지정해야 할 인자가 많을 때 사용하는 패턴

```Java
public class car {
    private String engine;
    private boolean airbag;
    private String color;
    private boolean cameraSensor;
    private boolean AEB;

    public Car(String engine, boolean airbag, String color, boolean cameraSensor, boolean AEB) {
        this.engine = engine;
        this.airbag = airbag;
        this.color = color;
        this.cameraSensor = cameraSensor;
        this.AEB = AEB;
    }

    @Override
    public String toString() {
        return "Car{" +
                "engine='" + engine + '\'' +
                ", airbag=" + airbag +
                ", color='" + color + '\'' +
                ", cameraSensor=" + cameraSensor +
                ", AEB=" + AEB +
                '}';
    }
}

public class CarBuilder {
    private String engine;
    private boolean airbag;
    private String color;
    private boolean cameraSensor;
    private boolean AEB;

    public CarBuilder setEngine(String engine) {
        this.engine = engine;
        return this;
    }

    public CarBuilder setAirbag(boolean airbag) {
        this.airbag = airbag;
        return this;
    }

    public CarBuilder setColor(String color) {
        this.color = color;
        return this;
    }

    public CarBuilder setCameraSensor(boolean cameraSensor) {
        this.cameraSensor = cameraSensor;
        return this;
    }

    public CarBuilder setAEB(boolean AEB) {
        this.AEB = AEB;
        return this;
    }

    public Car build() {
        return new Car(engine, airbag, color, cameraSensor, AEB);
    }
}

public class MainEntry {
    public static void main(String[] args) {
        Car car = new CarBuilder()
                .setEngine("V8")
                .setAirbag(true)
                .setColor("red")
                .setCameraSensor(true)
                .setAEB(true)
                .build();
        System.out.println(car);

        CarBuilder carBuilder = new CarBuilder()
                .setEngine("V8")
                // .setAirbag(true)
                .setColor("red")
                .setCameraSensor(true)
                .setAEB(true);

        Random random = new Random();
        Car car2 = carBuilder.
            setAirbag(random.nextInt(2) == 0)
            .build();
    }
}
```
