**Car.java**
```
public interface Car {
    public void assemble();
}
```

**BasicCar.java**
```
public class BasicCar implements Car {
    @Override 
    public void assemble() {
        System.out.println("Basic Car.");
    }
}
```

**CarDecorator.java**
```
public class CarDecorator implements Car {
    protected Car car;

    public CarDecorator(Car c) {
        this.car = c;
    }

    @Override
    public void assemble() {
        this.car.assemble();
    }
}
```

**LuxuryCar.java**
```
public class LuxuryCar extends CarDecorator {
    public LuxuryCar(Car c) {
        super(c);
    }

    @Override
    public void assemble() {
        super.assemble();
        System.out.println("Adding features of Luxury Car.");
    }
}
```

**SportsCar.java**
```
public class SportsCar extends CarDecorator {
    public SportsCar(Car c) {
        super(c);
    }

    @Override
    public void assemble() {
        super.assemble();
        System.out.println("Adding features of Sports Car.");
    }
}
```

**DecoratorPatternTest.java**
```
public class DecoratorPatternTest {
    public static void main(String[] args) {
        Car sportsCar = new SportsCar(new BasicCar());
        sportCar.assemble();
        System.out.println("\n****");

        Car sportsLuxuryCar = new SportsCar(new LuxuryCar(new BasicCar()));
        sportsLuxuryCar.assemble();
    }
}
```
Output:
```
Basic Car. Adding features of Sports Car.
****
Basic Car. Adding features of Luxury Car. Adding features of Sports Car.