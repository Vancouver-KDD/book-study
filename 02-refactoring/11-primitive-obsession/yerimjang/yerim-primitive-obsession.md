# Primitive Obsession
Many programmers are curiously reluctant to create their own fundamental types which are useful for their domain-such as money, coordinates, or ranges. **We thus see calculations** that treat monetary amounts as plain numbers, or calculations of physical quantities that ignore units (adding inches to millimeters), or lots of code doing if (a < upper && a > lower)
### 1. Replace Primitive with Object (174)
##### Example
```typescript
//Before
class Order {
  const customer: string;
  //...
}

//After
class Order {
  const customer: Customer;
  //...
}

class Customer {
  const name: string;
  const address: string;
  //...
}
```
### 2. Replace Type Code with Subclasses (362)
##### Example
```typescript
//Before
class Employee {
  public static readonly ENGINEER = "engineer";
  public static readonly SALESMAN = "salesman";
  const name: string;
  const type: string;

  constructor(name: string, type: string) {
    this.name = name;
    this.type = type;
  }
}

//client code
createEmployee(name: string, type: string): Employee {
  return new Employee(name, type);
}

//After
abstract class Employee {
  const name: string;

  constructor(name: string) {
    this.name = name;
  }

  abstract getType(): string;
}

class Engineer extends Employee {
  constructor(name: string) {
    super(name);
  }

  getType(): string {
    return "engineer";
  }
}

class Salesman extends Employee {
  constructor(name: string) {
    super(name);
  }

  getType(): string {
    return "salesman";
  }
}

//client code
createEmployee(name: string, type: number): Employee {
  switch (type) {
    case "engineer": return new Engineer(name: this.name);
    case "salesman": return new Salesman(name: this.name);
    default: break;
  }
}
```
### 3. Replace Conditional with Polymorphism (272)

### 4. Extract Class (182)

### 5. Introduce Parameter Object (140)

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
