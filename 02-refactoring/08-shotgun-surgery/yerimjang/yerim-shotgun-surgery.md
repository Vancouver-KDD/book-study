#Shotgun Surgery
You whiff this when , every time you make a change, you have to make a lot of little edits to a lot of different classes. When the changes are all over the place, they are hard to find, and it's easy to miss an important change.

###1. Move Function (198)

###2. Move Field (207)
#####Example
```typescript
//Before
class Customer {
  private plan: Plan;
  private discountRate: number;

  get plan() {
    return this.plan;
  }

  get discountRate() {
    return this.discountRate;
  }
}

class Plan {
  //...
}

//After
class Customer {
  private plan;

  get plan() {
    return this.plan;
  }

  get discountRate() {
    return this.plan.discountRate;
  }
}

class Plan {
  private discountRate: number;

  get discountRate() {
    return this.discountRate;
  }
}
```

###3. Combine Functions into Class (144)

###4. Combine Functions into Transform (149)

###5. Split Phase (154)

###6. Inline Function (115)

###7. Inline Class (186)
#####Example
```typescript
//Before
class Person {
  private name: string;
  private telephoneNumber: TelephoneNumber;

  get name(): string {
    return this.name;
  }

  get number(): string {
    return `(${this.telephoneNumber.officeAreaCode}) ${this.telephoneNumber.officeNumber}`;
  }

}

class TelephoneNumber {
  private officeAreaCode: string;
  private officeNumber: string;

  get officeAreaCode(): string {
    return this.officeAreaCode;
  }

  get officeNumber(): string {
    return this.officeNumber;
  }
}

//After
class Person {
  private name: string;
  private officeAreaCode: string;
  private officeNumber: string;

  get name(): string {
    return this.name;
  }

  get officeAreaCode(): string {
    return this.officeAreaCode;
  }

  get officeNumber(): string {
    return this.officeNumber;
  }
}

```

###Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
