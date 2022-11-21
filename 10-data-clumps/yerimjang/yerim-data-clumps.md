# Data Clumps
Date items tend to be like children: They They enjoy hanging around together.
![alt text](https://refactoring.guru/images/refactoring/content/smells/data-clumps-01-2x.png?id=64c7f4113e3c06f10dbec825833fa190 "Date Clumps 1")
Often these data groups are due to poor program structure or "copypasta programming”.
We create a class because using a class gives you the opportunity to create useful classes and remove a lot of duplication and accelerate future development, allowing the data to become productive members of society.
![alt text](https://refactoring.guru/images/refactoring/content/smells/data-clumps-03-2x.png?id=2b4a70e09a6236715a9bc4bd4663508b "Date Clumps 2")

### 1. Extract Class (182)
- If repeating data comprises the fields of a class
##### Example
```typescript
//Before
class Person {
  const officeAreaCode: string;
  const officeNumber: string;

  constructor(officeAreaCode: string, officeNumber: string) {
    this.officeAreaCode = officeAreaCode;
    this.officeNumber = officeNumber;
  }
  officeAreaCode(): string {
    return this.officeAreaCode;
  }
  officeNumber(): string {
    return this.officeNumber;
  }
}
//client code
const person = new Person(officeAreaCode: '778', officeNumber: '123-4567');

//After
class Person {
  const telephoneNumber: TelephoneNumber;

  constructor(telephoneNumber: TelephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }
  get officeAreaCode(): string {
    return this.telephoneNumber.areaCode;
  }
  get officeNumber(): string {
    return this.telephoneNumber.number;
  }
}

class TelephoneNumber {
  const areaCode: string;
  const number: string;

  constructor(areaCode: string, number: string) {
    this.areaCode = areaCode;
    this.number = number;
  }

  get areaCode() {
    return this.areaCode;
  }

  get number() {
    return this.number;
  }
}

//client code
const telephoneNumber = new TelephoneNumber(areaCode: '778', number: '123-4567');
const person = new Person(telephoneNumber: telephoneNumber);
```
##### Benefits
- This refactoring method will help maintain adherence to the Single Responsibility Principle. The code of your classes will be more obvious and understandable.
- Single-responsibility classes are more reliable and tolerant of changes. For example, say that you have a class responsible for ten different things. When you change this class to make it better for one thing, you risk breaking it for the nine others.

##### Drawbacks
- If you “overdo it” with this refactoring technique, you will have to resort to `Inline Class`.

### 2. Introduce Parameter Object (140)
- If the same data clumps are passed in the parameters of methods
##### Example
```typescript
//Before
class Customer {
  amountInvoicedIn(start: Date, end: Date) {}
  amountReceivedIn(start: Date, end: Date) {}
  amountOverdueIn(start: Date, end: Date) {}
}

//After
class Customer {
  amountInvoicedIn(date: DateRange) {}
  amountReceivedIn(date: DateRange) {}
  amountOverdueIn(date: DateRange) {}
}
```
##### Benefits
- More readable code.
- Identical groups of parameters scattered here and there create their own kind of code duplication.

##### Drawbacks
- If you move only data to a new class and don’t plan to move any behaviors or related operations there, this begins to smell of a Data Class.
### 3. Preserve Whole Object (319)
- If some of the data is passed to other methods, think about passing the entire data object to the method instead of just individual fields.
##### Example
```typescript
//Before
let low = daysTempRange.getLow();
let high = daysTempRange.getHigh();
let withinPlan = plan.withinRange(low, high);

//After
let withinPlan = plan.withinRange(daysTempRange);
```
##### Benefits
- Instead of a hodgepodge of parameters, you see a single object with a comprehensible name.
- If the method needs more data from an object, you won’t need to rewrite all the places where the method is used.

##### Drawbacks
- Sometimes this transformation causes a method to become less flexible: previously the method could get data from many different sources but now, because of refactoring, we’re limiting its use to only objects with a particular interface.

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
- https://refactoring.guru/smells/data-clumps
