# Prototype

- PrototypeRegistry
```java
/**
 * Cached prototypes
 */
public class PrototypeRegistry {
  private Map<String, Vehicle> cache = new HashMap<>();

  public PrototypeRegistry() {
      Camry camry = new Camry();
      ModelY modelY = new ModelY();

      cache.put("Toyota Camry", camry);
      cache.put("Tesla Model Y", modelY);
  }

  public Vehicle get(String key) {
      return cache.get(key).clone();
  }
}

```

- Vehicle
```java
public abstract class Vehicle {

  private String brand;
  private String manufacturedYear;
  private Double price;

  public Vehicle() {}

  public Vehicle(Vehicle vehicle) {
      this.brand = vehicle.brand;
      this.manufacturedYear = vehicle.manufacturedYear;
      this.price = vehicle.price;
  }

  public abstract Vehicle clone();
}
```

- Camry
```java
public class Camry extends Vehicle {

  public Camry() {}

  public Camry(Camry camry) {
      super(camry);
  }

  @Override
  public Camry clone() {
      return new Camry(this);
  }
}
```

- ModelY
```java
public class ModelY extends Vehicle {

  private Double batteryCapacity;
  private GpsSystem gpsSystem;

  public ModelY() {}

  public ModelY(ModelY modelY) {
      super(modelY);
      this.batteryCapacity = modelY.batteryCapacity;
      this.gpsSystem = modelY.gpsSystem; // shallow copy
//        this.gpsSystem = new GpsSystem(); // deep copy
  }

  @Override
  public ModelY clone() {
      return new ModelY(this);
  }
}
```

- GpsSystem
```java
public class GpsSystem {}
```
