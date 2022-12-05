# Message Chains
![alt text](https://refactoring.guru/images/refactoring/content/smells/message-chains-01-2x.png "Date Clumps 1")
In code you see a series of calls resembling `$a->b()->c()->d()`. Navigating this way means the client is coupled to the structure of the navigation. Any change to the intermediate relationships causes the client to have to change.


### 1. Hide Delegate (189)
##### Example
```typescript
//Before
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

//After
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
```

### 2. Extract Function (106)
##### Example
```typescript
//Before
printOwing(invoice): void {
  let outstanding = 0;

  console.log("*******************");
  console.log("** Customer Owes **");
  console.log("*******************");

  for (const o of invoice.orders) {
    outstanding += o.amount;
  }

  const today = Clock.today;
  invoice.dueDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 30);

  console.log(`name: ${invoice.customer}`);
  console.log(`amount: ${outstanding}`);
  console.log(`due: ${invoice.dueDate.toLocaleDateString()}`);
}

//**Client Code**
const invoice = new Invoice();
printOwing(invoice);

//After
printOwing(invoice): void {
  printBanner();
  const outstanding = calculateOutstanding(invoice);
  recordDueDate(invoice);
  printDetails(invoice, outstanding);
}

printBanner(): void {
  console.log("*******************");
  console.log("** Customer Owes **");
  console.log("*******************");
}

calculateOutstanding(invoice): number {
  let result = 0;
  for (const o of invoice.orders) {
    result += o.amount;
  }
  return result;
}

recordDueDate(invoice): void {
  const today = Clock.today;
  invoice.dueDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 30);
}

printDetails(invoice, outstanding): void {
  console.log(`name: ${invoice.customer}`);
  console.log(`amount: ${outstanding}`);
  console.log(`due: ${invoice.dueDate.toLocaleDateString()}`);
}

//**Client Code**
const invoice = new Invoice();
printOwing(invoice);
```

### 3. Move Function (198)
##### Example
```typescript
//Before
class ClassA {
  aMethod(): void {
    //...
  }
}

class ClassB {
  //...
}

//After
class ClassA {
  //...
}

class ClassB {
  aMethod(): void {
    //...
  }
}
```

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
- https://refactoring.guru/smells/message-chains
