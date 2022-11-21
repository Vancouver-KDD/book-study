#Divergent Change
It occurs when one module is often changed in different ways for different reasons.

###1. Split Phase (154)

###2. Move Function (198)

###3. Extract Function (106)

###4. Extract Class (182)
#####Example
```typescript
//Before
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

//After
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
```
###Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
