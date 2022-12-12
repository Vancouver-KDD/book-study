# Lazy Element
Understanding and maintaining classes always costs time and money. So if a class doesn’t do enough to earn your attention, it should be deleted.

### 1. Inline Function (115)
##### Example
```typescript
//Before
class PizzaDelivery {
  // ...
  getRating(): number {
    return moreThanFiveLateDeliveries() ? 2 : 1;
  }
  moreThanFiveLateDeliveries(): boolean {
    return numberOfLateDeliveries > 5;
  }
}

//After
class PizzaDelivery {
  // ...
  getRating(): number {
    return numberOfLateDeliveries > 5 ? 2 : 1;
  }
}
```

### 2. Inline Class (186)

### 3. Collapse Hierarchy (380)
##### Example
```typescript
//Before
class Employee {...}
class Salesman extends Employee {...}

//After
class Employee {...}
```

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
