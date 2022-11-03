# Duplicated Code

Duplicated code is very common smell we can see in the code.

It's actually okay to write duplicated codes at the early stage. But you better reduce the duplication as soon as possible before it gets too many couplings.

The problem is that after a while, you cannot trust the *similar looking* codes in multiple places because there might be some extra handlings or unexpected behaviors. It leads to extra testing and higher maintanance cost.

All about doing refactoring is aiming the *self-documented code*.

### 1. Extract Function (106p) ###

##### Motivation

If there is a somewhat large size of the code without any function calls, you might end up reading line by line again everytime you get hit by the function.

Because how the code block (fragment) looks sometimes doesn't represent well what it is *trying to do*. 

The effective way to describe those code blocks is extracting them out into a separate function with a well self-describing name. 

```js
// Not like this long 
void GoToWork(){
    const wakeUpTime = currentTime();
    var isAlarmSet = findAlarm(wakeUpTime);
    if (isAlarmSet) {
        while(notifyAlarmSystem())
        {
            console.log("waiting for waking up...");
        }
    }
    const goToShowerBooth = move(SHOWER_BOOTH);
    var hotWaterAvailable = currentWaterTemperature() > 50;
    if (hotWaterAvailable) {
        takeOffPajamas();
        takeShower();
    }
    //...
    //... Shaving
    //... Change clothes...
    //...
    //... Leave House...
    //...
    //... Driving...
    //...
}

// Better be
void GoToWork(){
    WakeUp();
    Shower();
    Shave();
    GetDressed();
    LeaveHouse();
    DriveCarToOffice();
    ClockIn();
}
```

##### Mechanics

1. Create a new function named after the intent of the code block. It is very important to name it by **what it does**, not by how it does it.
2. **Copy** code block into the function.
3. If there is referenced variables in the local scope, use them as parameters of the function.
4. Compile & Test 
5. Replace the code block with the function call.

##### Examples - No Variables Out of Scope, Using Local Variables, Reassigning a Local Variable

started from here, raw and full of code blocks.

```js
function printOwing(invoice) {
    let outstanding = 0;

    console.log("***********************");
    console.log("**** Customer Owes ****");
    console.log("***********************");

    // calculate outstanding
    for (const o of invoice.orders) {
        outstanding += o.amount;
    }

    // record due date
    const today = Clock.today;
    invoice.dueDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 30);

    //print details
    console.log(`name: ${invoice.customer}`);
    console.log(`amount: ${outstanding}`);
    console.log(`due: ${invoice.dueDate.toLocaleDateString()}`);
}
```

Extract no-variable referenced code blocks into functions.

```js
function printBanner() {
    console.log("***********************");
    console.log("**** Customer Owes ****");
    console.log("***********************");  
}
```

Extract variable referenced code blocks into functions.

```js
function printDetails(invoice, outstanding) {
    console.log(`name: ${invoice.customer}`);
    console.log(`amount: ${outstanding}`);
    console.log(`due: ${invoice.dueDate.toLocaleDateString()}`);
}
```

Extract code blocks of updating local variables or recalculations into functions

```js
function recordDueDate(invoice) {
    const today = Clock.today;
    invoice.dueDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 30);
}
```

```js
function calculateOutstanding(invoice) {
    let outstanding = 0;
    for (const o of invoice.orders) {
      outstanding += o.amount;
    }
    return outstanding;
}
```

At the end, all the code fragments will be merged into 

```js
function printOwing(invoice) {
    printBanner();
    const outstanding = calculateOutstanding(invoice);
    recordDueDate(invoice);  
    printDetails(invoice, outstanding);
}
```

### 2. Slide Statements (223p) ###

##### Motivation

If they do something similar or access the data object at the same time, it is so much easier to read the code by putting them together.

##### Mechanics

1. Identify what each code fragment does.
2. Examine statements between code fragments and see if there are dependencies or interference that cannot be avoided. 
3. Cut the fragment into the desired position.
4. Test

##### Examples  
```js
const pricingPlan = retrievePricingPlan();
const order = retreiveOrder();
const baseCharge = pricingPlan.base;
let charge;
const chargePerUnit = pricingPlan.unit;
const units = order.units;
let discount;
charge = baseCharge + units * chargePerUnit;
let discountableUnits = Math.max(units - pricingPlan.discountThreshold, 0);
discount = discountableUnits * pricingPlan.discountFactor;
if (order.isRepeat) discount += 20;
charge = charge - discount;
chargeOrder(charge);
```

could be 

```js
const pricingPlan = retrievePricingPlan();
const baseCharge = pricingPlan.base;
const chargePerUnit = pricingPlan.unit;

const order = retreiveOrder();
const units = order.units;

let discountableUnits = Math.max(units - pricingPlan.discountThreshold, 0);
let discount = discountableUnits * pricingPlan.discountFactor;
if (order.isRepeat) discount += 20;

let originalCharge = baseCharge + units * chargePerUnit;
let finalCharge = originalCharge - discount;
chargeOrder(finalCharge);
```

### 3. Pull Up Method (350p) ###

##### Motivation

In the inherited subclasses, if some of method bodies look the same and if they actually do, try pull them up to the parent class so that you can remove duplicated method bodies.

##### Mechanics

1. Must look at the code to find any difference. It may contains that a subclass specific handling code or private field acces of the subclass.
2. Make sure they can be shared through parent class.
3. Match the function signatures if they are different.
4. Create a new method in the super class. Copy the body of the code block.
5. Remove subclasses' method one by one while testing for each.

##### Examples  

```js
class Employee extends Party {
    get annualCost() {
        return this.monthlyCost * 12;
    }
}
class Department extends Party {
    get totalAnnualCost() {
        return this.monthlyCost * 12;
    }
}
```

1. Match their function signature.

```js
class Employee extends Party {
    get annualCost() {
        return this.monthlyCost * 12;
    }
}
class Department extends Party {
    get annualCost() {
        return this.monthlyCost * 12;
    }
}
```

2. Pull up the method into the parent class

```js
class Party {
    get monthlyCost() {
        // Party might not have monthlyCost
        return this.monthlyCost * 12;
    }
}
```

3. If the function should remain undefied for the parent class, define exception or make it abstract

```js
class Party {
    get monthlyCost() {
        throw new SubclassResponsibilityError();
    }
}
```

```c++
class Party
{
public:
    virtual double monthlyCost() = 0;
};
```