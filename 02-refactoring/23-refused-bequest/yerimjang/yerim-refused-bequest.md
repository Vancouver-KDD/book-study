# Refused Bequest
If a subclass uses only some of the methods and properties inherited from its parents, the hierarchy is off-kilter. The unneeded methods may simply go unused or be redefined and give off exceptions.

### 1. Push Down Method (359)
##### Example
```typescript
//Before
class Employee {
  get quota {...}
}

class Engineer extends Employee {...}
class Salesman extends Employee {...}

//After
class Employee {...}
class Engineer extends Employee{...}
class Salesman extends Employee {
  get quota {...}
}
```

### 2. Push Down Field (361)
##### Example
```typescript
//Before
class Employee {
  private String quota;
}

class Engineer extends Employee {...}
class Salesman extends Employee {...}

//After
class Employee {...}
class Engineer extends Employee{...}
class Salesman extends Employee {
  protected String quota;
}
```

### 3. Replace Subclass with Delegate (381)

### 4. Replace Superclass with Delegate (399)

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
