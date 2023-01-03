# Shotgun Surgery

작은 코드 변화를 위해 한 곳이 아닌 다른 많은 module을 수정해야 할 때 우리는 shotgun surgery라고 부른다.

Divergent change와 다른 점은, 한 module(class)이 divergent change는 다른 이유로 인해서 다른 방법으로 수정되는 경우이고, shotgun surgery는 한 변화를 위해서 여러 modules(classes)가 수정이 되어야 하는 경우이다.

### Solutions

수정되어야 하는 여러 modules(classes)을 가능한 한 module에 넣는 방법을 사용한다

##### Move Function

```java
  // before
  public class Rent { ... }

  public class Car {
    private double pricePerDay;

    public double getPricePerDay() { ... }
  }

  public class Customer {
    ...
    public void getChargedPrice() {
      // return chared rental price
      return this.getRentedCar().getChargedPrice() * this.getRentPeriod();
    }
    ...
  }

  // after
  public class Rent {
    public void getChargedPrice(Customer customer) {
      return customer.getRentedCar().getChargedPrice() * customer.getRentPeriod();
    }
  }

  public class Customer { 
    private Rent rentData;
    
    // get rent price through rent data
  }
```