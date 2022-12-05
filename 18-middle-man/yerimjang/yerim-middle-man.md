# Middle Man
When class performs only one action, delegating work to another class. This smell can be the result of overzealous elimination of `Message Chains`.

### 1. Remove Middle Man (192)
##### Example
```typescript
//Before
class Person {
  private department: Department;

  constructor {
    this.department = new Department();
  }

  getManager(): Manager {
    return this.department.getManager();
  }
}

class Department {
  private manager: Manager;

  constructor {
    this.manager = new Manager();
  }

  getManager(): Manager {
    return this.manager;
  }
}

class Manager {
  //...
}

//**Client Code**
const person = new Person();
const manager = person.getManager();

//After
class Person {
  private department: Department;

  constructor {
    this.department = new Department();
  }

  getDepartment(): Department {
    return this.department;
  }
}

class Department {
  private manager: Manager;

  constructor {
    this.manager = new Manager();
  }

  getManager(): Manager {
    return this.manager;
  }
}

class Manager {
  //...
}

//**Client Code**
const person = new Person();
const manager = person.getDepartment().getManager();

```

### 2. Inline Function (115)

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
