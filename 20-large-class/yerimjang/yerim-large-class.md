# Large Class
A class contains many fields/methods/lines of code.

### 1. Extract Class (182)

### 2. Extract Superclass (375)
##### Example
```typescript
//Before
class JobItem {
  getTotalPrice() {}

  getUnitPrice() {}

  getEmployee() {}
}

//After
class LaborItem {
  getUnitPrice() {}

  getEmployee() {}
}
class JobItem extends LaborItem{
  getTotalPrice() {}

  getUnitPrice() {}

  getEmployee() {}
}
```

### 3. Replace Type Code with Subclasses (362)
##### Example
```typescript
//Before
createEmployee(name: String, type: number) {
  return new Employee(name, type);
}

//After
createEmployee(name: String, type: number) {
  switch (type) {
    case 'engineer': return new Engineer(name);
    case 'salesman': return new Salesman(name);
    case 'manager': return new Manager(name);
  }
}
```

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
