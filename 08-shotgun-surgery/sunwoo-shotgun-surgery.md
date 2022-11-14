# Shotgun Surgery
Shotgun Surgery? "an antipattern in software development and occurs where a developer adds features to an application codebase which span a multiplicity of implementors or implementations in a single change"[[1]](https://en.wikipedia.org/wiki/Shotgun_surgery#:~:text=Shotgun%20surgery%20is%20an%20antipattern,implementations%20in%20a%20single%20change.)

Increasing the size of the code base is avoidable while developing the system. It consumes a lot of time and effort to find the places to edit if we need to modify many small pieces of code from different areas in the system. Moreover, it can cause system failure due to missing an important change.

The goal of optimization for the shotgun surgery code smell is to minimize the number of code blocks requiring modification.

## Solution
Putting all the changes into a single module
### Move Function 
Sign to move the function to another module
* a function is referred in other contexts than the one it currently resides in.
* depends on the location of a caller
* make more accessible to a helper function inside another function

Concerns
* what functions call the target function
* what functions are called by the target function
* what data/field(if class method) that the taget function uses

Mechanics
1. Look all program elements used by the chosen function in its current context
2. Check it's polymorphic methods if the function is in class
3. Copy function to the target context and ajust function to fit in the new context
4. Create reference from the source conext into the target context
5. From the source context, the source function delegates to the function in the new context
6. Test

Example
```js
class Account…

  get bankCharge() {
    let result = 4.5;
    if (this._daysOverdrawn > 0) result += this.overdraftCharge;
    return result;
  }

  get overdraftCharge() {
    if (this.type.isPremium) {
      const baseCharge = 10;
      if (this.daysOverdrawn <= 7)
        return baseCharge;
      else
        return baseCharge + (this.daysOverdrawn - 7) * 0.85;
    }    
    else
      return this.daysOverdrawn * 1.75;
  }
```

```js
class Account…

  get bankCharge() {
    let result = 4.5;
    if (this._daysOverdrawn > 0) result += this.overdraftCharge;
    return result;
  }

  get overdraftCharge() {
    return this.type.overdraftCharge(this);
  }

class AccountType…

  overdraftCharge(account) {
    if (this.isPremium) {
      const baseCharge = 10;
      if (account.daysOverdrawn <= 7)
        return baseCharge;
      else
        return baseCharge + (account.daysOverdrawn - 7) * 0.85;
    }    
    else
      return account.daysOverdrawn * 1.75;
  }

```
### Move Field 
Even though we did well modulize our codebase, if the data structure is bad, many functions are being called just for handling the poor data structure.

Sign to move field
* Too many methods just for parsing them
* pieces of data are always passed to functions together 
* a change in one record requires a change in another record

Mechanics
1. Ensure the source field is encapsulated
2. Test
3. Create a field in the target
4. Create a reference from the source object to the target object
5. Adjust accessors to use the target field
6. Test
7. Remove the source field
8. Test

Example
```js
class Customer…

  constructor(name, discountRate) {
    this._name = name;
    this._discountRate = discountRate;
    this._contract = new CustomerContract(dateToday());
  }
  get discountRate() {return this._discountRate;}
  becomePreferred() {
    this._discountRate += 0.03;
    // other nice things
  }
  applyDiscount(amount) {
    return amount.subtract(amount.multiply(this._discountRate));
  }
class CustomerContract…

  constructor(startDate) {
    this._startDate = startDate;
  }
```

```js
class Customer…

  constructor(name, discountRate) {
    this._name = name;
    this._setDiscountRate(discountRate);
    this._contract = new CustomerContract(dateToday());
  }
  get discountRate() {return this._contract.discountRate;}
  _setDiscountRate(aNumber) {this._contract.discountRate = aNumber;}
  becomePreferred() {
    this._setDiscountRate(this.discountRate + 0.03);
    // other nice things
  }
  applyDiscount(amount) {
    return amount.subtract(amount.multiply(this.discountRate));
  }

class CustomerContract…

  constructor(startDate, discountRate) {
    this._startDate = startDate;
    this._discountRate = discountRate;
  }
  get discountRate()    {return this._discountRate;}
  set discountRate(arg) {this._discountRate = arg;}

```

### Combine Functions into Classes 
When you have a bunch of functions operating on similar data, combine them into a class and manage the class instead of managing multiple functions from the different modules.

Example
```js
reading = {customer: "ivan", quantity: 10, month: 5, year: 2017};

//client 1…
const aReading = acquireReading();
const baseCharge = baseRate(aReading.month, aReading.year) * aReading.quantity;

//client 2…
const aReading = acquireReading();
const base = (baseRate(aReading.month, aReading.year) * aReading.quantity);
const taxableCharge =  Math.max(0, base - taxThreshold(aReading.year));

//client 3…
const aReading = acquireReading();
const basicChargeAmount = calculateBaseCharge(aReading);

class Reading {
  constructor(data) {
    this._customer = data.customer;
    this._quantity = data.quantity;
    this._month = data.month;
    this._year = data.year;
  }
  get customer() {return this._customer;}
  get quantity() {return this._quantity;}
  get month()    {return this._month;}
  get year()     {return this._year;}
}
```

### Combine Functions into Transform
If some functions are repeatedly called on the other sides of the code for the same record, create a field and put the result of executions as its field.

Example
```js
reading = {customer: "ivan", quantity: 10, month: 5, year: 2017};

//client 1…
const aReading = acquireReading();
const baseCharge = baseRate(aReading.month, aReading.year) * aReading.quantity;

//client 2…
const aReading = acquireReading();
const base = (baseRate(aReading.month, aReading.year) * aReading.quantity);
const taxableCharge =  Math.max(0, base - taxThreshold(aReading.year));

//client 3…
const aReading = acquireReading();
const basicChargeAmount = calculateBaseCharge(aReading);

// 
function enrichReading(original) {
  const result = _.cloneDeep(original);
  result.baseCharge = calculateBaseCharge(result);
  result.taxableCharge = Math.max(0, result.baseCharge - taxThreshold(result.year));
  return result;
}

```
### Split Phase
If the common functions can combine their output for a consuming phase of logic, separate them.

```ts
const getTweetReport = (tweet: ITweet) => {
    const context:string = tweet.text;
    const words: string[] = context.split(' ');
    const tokens: string[] = tokenize(words);
    const cleanTokens: string[] = cleanTokens(tokens);
    const result: ITweetResult = tweetModel(cleanTokens);
    createReport(result);
}

const getTweetReport = (tweet: ITweet) => {
    const tokens: ITweetTokens = tokenize(tweet);
    const result: ITweetResult = tweetModel(tokens);
    createReport(result);
}

// tokenize can be commonly used
const tokenize = (tweet: ITweet): ITweetTokens => {
    const context:string = tweet.text;
    const words: string[] = context.split(' ');
    const tokens: string[] = tokenize(words);
    const cleanTokens: ITweetTokens[] = cleanTokens(tokens);
    return cleanTokens;
}
```

### Inline Function
If the functions are separated poorly like when they don't need to be separted, combine them into one function and assign good name for readability!

Example
```js
function rating(aDriver) {
    return moreThanFiveLateDeliveries(aDriver) ? 2 : 1;
  }
function moreThanFiveLateDeliveries(aDriver) {
    return aDriver.numberOfLateDeliveries > 5;
  }

function rating(aDriver) {
    return aDriver.numberOfLateDeliveries > 5 ? 2 : 1;
  }
```

### Inline Classes
If inline classes do little works and no longer decrease the complexity of the system, the class has to be combined.

```js
class TrackingInformation {
    get shippingCompany()    {return this._shippingCompany;}
    set shippingCompany(arg) {this._shippingCompany = arg;}
    get trackingNumber()    {return this._trackingNumber;}
    set trackingNumber(arg) {this._trackingNumber = arg;}
    get display()            {
      return `${this.shippingCompany}: ${this.trackingNumber}`;
    }
  }

class Shipment…

  get trackingInfo() {
    return this._trackingInformation.display;
  }
  get trackingInformation()    {return this._trackingInformation;}
  set trackingInformation(aTrackingInformation) {
    this._trackingInformation = aTrackingInformation;
  }
```

```js
class Shipment…

  get trackingInfo() {
    return `${this.shippingCompany}: ${this.trackingNumber}`;
  }
  get shippingCompany()    {return this._shippingCompany;}
  set shippingCompany(arg) {this._shippingCompany = arg;}
  get trackingNumber()    {return this._trackingNumber;}
  set trackingNumber(arg) {this._trackingNumber = arg;}
```
