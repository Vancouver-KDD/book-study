# Alternative Classes with Different Interfaces
Two classes perform identical functions but have different method names.

### 1. Change Function Declaration (124)
##### Example
```typescript
//Before
class RetailA {
  private car: Car;

  carType: String {
    this.car.type;
  }
}

class RetailB {
  private car: Car;

  vehicleType: String{
    this.car.type;
  }
}

//After
class RetailA {
  private car: Car;

  carType: String {
    this.car.type;
  }
}

class RetailB {
  private car: Car;

  carType: String{
    this.car.type;
  }
}
```

### 2. Move Function (198)

### 3. Extract Superclass (375)

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
