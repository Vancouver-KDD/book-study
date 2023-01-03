#Long Function
![alt text](https://refactoring.guru/images/refactoring/content/smells/long-method-01-2x.png "Method Before")
![alt text](https://refactoring.guru/images/refactoring/content/smells/long-method-02-2x.png "Method After")
Mentally, it’s often harder to create a new method than to add to an existing one: *“But it’s just two lines, there’s no use in creating a whole method just for that...”* Which means that another line is added and then yet another, giving birth to a tangle of spaghetti code.

###1. Extract Function (106)
#####Example
```typescript
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
```
#####Benefits
- More readable code!
- Less code duplication.
- Isolates independent parts of code, meaning that errors are less likely.


###2. Replace Temp with Query (178)
#####Example
```typescript
//Before
calculateTotal(): number {
  let basePrice = quantity * itemPrice;
  if (basePrice > 1000) {
    return basePrice * 0.95;
  }
  else {
    return basePrice * 0.98;
  }
}

//After
calculateTotal(): number {
  if (basePrice() > 1000) {
    return basePrice() * 0.95;
  }
  else {
    return basePrice() * 0.98;
  }
}
basePrice(): number {
  return quantity * itemPrice;
}
```
#####Benefits
- Code readability.
- Slimmer code via deduplication, if the line being replaced is used in multiple methods.

###3. Introduce Parameter Object (140)
#####Example
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
#####Benefits
- More readable code.
- Identical groups of parameters scattered here and there create their own kind of code duplication.

#####Drawbacks
- If you move only data to a new class and don’t plan to move any behaviors or related operations there, this begins to smell of a Data Class.

###4. Preserve Whole Object (319)
#####Example
```typescript
//Before
let low = daysTempRange.getLow();
let high = daysTempRange.getHigh();
let withinPlan = plan.withinRange(low, high);

//After
let withinPlan = plan.withinRange(daysTempRange);
```

#####Benefits
- Instead of a hodgepodge of parameters, you see a single object with a comprehensible name.
- If the method needs more data from an object, you won’t need to rewrite all the places where the method is used.

#####Drawbacks
- Sometimes this transformation causes a method to become less flexible: previously the method could get data from many different sources but now, because of refactoring, we’re limiting its use to only objects with a particular interface.

###5. Replace Function with Command (337)
#####Example
```typescript
//Before
class Order {
  // ...
  price(): number {
    let primaryBasePrice;
    let secondaryBasePrice;
    let tertiaryBasePrice;
    // Perform long computation.
  }
}

//After
class Order {
  // ...
  price(): number {
    return new PriceCalculator(this).compute();
  }
}

class PriceCalculator {
  private primaryBasePrice: number;
  private secondaryBasePrice: number;
  private tertiaryBasePrice: number;

  constructor(order: Order) {
    // Copy relevant information from the
    // order object.
  }

  compute(): number {
    // Perform long computation.
  }
}
```

#####Benefits
- Isolating a long method in its own class allows stopping a method from ballooning in size. This also allows splitting it into submethods within the class, without polluting the original class with utility methods.

#####Drawbacks
- Another class is added, increasing the overall complexity of the program.

###6. Decompose Conditional (260)
#####Example
```typescript
//Before
if (date.before(SUMMER_START) || date.after(SUMMER_END)) {
  charge = quantity * winterRate + winterServiceCharge;
}
else {
  charge = quantity * summerRate;
}

//After
if (isSummer(date)) {
  charge = summerCharge(quantity);
}
else {
  charge = winterCharge(quantity);
}

isSummer(date): boolean {
  return date.before(SUMMER_START) || date.after(SUMMER_END);
}
```
#####Benefits
- By extracting conditional code to clearly named methods, you make life easier for the person who’ll be maintaining the code later (**such as you, two months from now!**).
- This refactoring technique is also applicable for short expressions in conditions. The string `isSalaryDay()` is much prettier and more descriptive than code for comparing dates.

###7. Replace Conditional with Polymorphism (272)
#####Example
```typescript
//Before
class Bird {
  // ...
  getSpeed(): number {
    switch (type) {
      case EUROPEAN:
        return getBaseSpeed();
      case AFRICAN:
        return getBaseSpeed() - getLoadFactor() * numberOfCoconuts;
      case NORWEGIAN_BLUE:
        return (isNailed) ? 0 : getBaseSpeed(voltage);
    }
    throw new Error("Should be unreachable");
  }
}

//After
abstract class Bird {
  // ...
  abstract getSpeed(): number;
}

class European extends Bird {
  getSpeed(): number {
    return getBaseSpeed();
  }
}
class African extends Bird {
  getSpeed(): number {
    return getBaseSpeed() - getLoadFactor() * numberOfCoconuts;
  }
}
class NorwegianBlue extends Bird {
  getSpeed(): number {
    return (isNailed) ? 0 : getBaseSpeed(voltage);
  }
}

// Somewhere in client code
let speed = bird.getSpeed();
```

#####Benefits
- This technique adheres to the Tell-Don’t-Ask principle.
- Removes duplicate code.
- If you need to add a new execution variant, all you need to do is add a new subclass without touching the existing code.

###8. Split Loop (227)
#####Example
```typescript
//Before
let averageAge = 0;
let totalSalary = 0;
for (const p of people) {
  averageAge += p.age;
  totalSalary += p.salary;
}
averageAge = averageAge / people.length;

//After
let totalSalary = 0;
for (const p of people) {
  totalSalary += p.salary;
}

let averageAge = 0;
for (const p of people) {
  averageAge += p.age;
}
averageAge = averageAge / people.length;
```

#####Benefits
- Ensures you only need to understand the behavior you need to modify.

#####Drawbacks
- Many programmers are uncomfortable with this refactoring, as it forces you to execute the loop twice.

###Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
- https://refactoring.guru/smells/long-method
