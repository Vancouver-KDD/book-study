#Mutable Data
Changes to data can often lead to unexpected consequences and tricky bugs. I can update some data here, not realizing that another part of the software expects something different and now fails.

###1. Encapsulate Variable (132)

###2. Split Variable (240)
#####Example
```typescript
//Before
let temp = 2 * (height + width);
console.log(temp);
temp = height * width;
console.log(temp);

//After
const perimeter = 2 * (height + width);
console.log(perimeter);
const area = height * width;
console.log(area);
```

###3. Slide Statements (223)

###4. Extract Function (106)

###5. Separate Query from Modifier (306)
#####Example
```typescript
//Before
class Customer {
  //...
  getTotalOutStandingAndSendBill(): void {
    const result = this.invoices.reduce((previous, current) { current.amount + total }, 0);
    sendBill();
    return result;
  }
}

//After
class Customer {
  //...
  getTotalOutStanding(): void {
    return this.invoices.reduce((previous, current) { current.amount + total }, 0);
  }

  sendBill(): void {
    emailGateway.send(formatBill(this));
  }
}
```

###6. Remove Setting Method (331)
#####Example
```typescript
//Before
class Person {
  private name: string;

  get name() {
    return this.name;
  }
  setName(name: string): void {
    this.name = name;
  }
}

//After
class Person {
  private name: string;

  get name() {
    return this.name;
  }
}
```

###7. Replace Derived Variable with Query (248)

###8. Combine Functions into Class (144)

###9. Combine Functions into Transform (149)

###10. Change Reference to Value (252)

###Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
- https://refactoring.guru/split-temporary-variable
- https://refactoring.guru/separate-query-from-modifier
