# Alternative Classes with Different Interfaces

##### Change Function Declaration (Rename Function/Change Signature)

```java
  // Before
  public double circum(double radius) {
    return 2 * Math.PI * radius;
  }

  // After
  public double circumference(double radius) {
    return 2 * Math.PI * radius;
  }
```

##### Move Function

##### Extract Superclass (feat. Pull Up Field & Pull up Method)

```java
  // Before
  public class TeslaS {
    private String modelName;
    private double priceInCad;
    private S
  }

  public class GenesisGV60 {
    private String modelName;
    private double priceInCad;
  }
```