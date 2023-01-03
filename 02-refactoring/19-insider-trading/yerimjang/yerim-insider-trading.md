# Insider Trading
Software people like strong walls between their modules and complain bitterly about how trading data around too much increases coupling. To make things work, some trade has to occur, but we need to reduce it to a minimum and keep it all above board.

### 1. Move Function (198)

### 2. Move Field (207)

### 3. Hide Delegate (189)

### 4. Replace Subclass with Delegate (381)
##### Example
```typescript
//Before
class Order {
  private warehouse;

  constructor(warehouse: WareHouse) {
    this.warehouse = warehouse;
  }

  daysToShip() {
    return this.warehouse.daysToShip;
  }
}

class PriorityOrder extends Order {
  private priorityPlan;

  constructor(priorityPlan: PriorityPlan) {
    this.priorityPlan = priorityPlan;
  }

  daysToShip() {
    return this.priorityPlan.daysToShip;
  }
}

//After
class Order {
  private warehouse;
  private priorityDelegate;

  constructor(warehouse: WareHouse, priorityDelegate: PriorityDelegate) {
    this.warehouse = warehouse;
    this.priorityDelegate = priorityDelegate;
  }

  daysToShip() {
    return (this.priorityDelegate) ? this.priorityDelegate.daysToShip : this.warehouse.daysToShip;
  }
}

class PriorityOrderDelegate {
  private priorityPlan;

  constructor(priorityPlan: PriorityPlan) {
    this.priorityPlan = priorityPlan;
  }

  daysToShip() {
    return this.priorityPlan.daysToShip;
  }
}
```

### 5. Replace Superclass with Delegate (399)
##### Example
```typescript
//Before
class Stack {
  //...
}

class Vector extends Stack {
  isEmpty() {
    //..
  }
}

//After
class Stack {
  private vector = new Vector();

  isEmpty() {
    return this.vector.isEmpty();
  }
}

class Vector {
  isEmpty() {
    //..
  }
}
```

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
