# Data Clumps
If you observe some set of data items together in many code blocks, group them into one objects.

Sign
* some classes have same datavalues as fields
* some functions receives a same data values as parameters often.

Caution
If you group some set of data items into one object, it can cause undesirable dependency between classes

## Solution
### Extract Class
By extracting class from the repeatably referenced values, it can reduce the amount of codes and provide clear responsibility for classes
```ts
class UserStat {
    _userName: string;
    _userCard: string
    _userPhoneNum: number;
    get userName () {
        return this._userName;
    }
    get userCard () {
        return this._userCard
    }
    get userPhoneNum () {
        return this._userPhoneNum;
    }
   public printSummary() {
        console.log(`Name: ${this.userName}`)
        console.log(`Card Info: ${this.userCard}`)
        console.log(`Contact: ${this.userPhoneNum}`)
   }
   public printStat(){}
   public getAveragePatment(){}
   public analyzeUser(){}
   //... other methods related to state
}

// assume other classes also use the same subset of user data fields,
// extract user data into one class
class User {
    _userName: string;
    _userCard: string
    _userPhoneNum: number;
    get userName () {
        return this._userName;
    }
    get userCard () {
        return this._userCard
    }
    get userPhoneNum () {
        return this._userPhoneNum;
    }
   public printSummary() {
        console.log(`Name: ${this.userName}`)
        console.log(`Card Info: ${this.userCard}`)
        console.log(`Contact: ${this.userPhoneNum}`)
   }
}
```
### Introduce Parameter Object
If you observer some parameters are regularly passed to different functions, consider combining them into one parameter object
```ts
// can observer some paramters are passed to other functions
const valueWithinRange = (max: number, min: number, value: number): boolean => {
    return value <= max && value >= min;
}

const valueOutRange = (max: number, min: number, value: number): boolean => {
    return !(value <= max && value >= min);
}

// define type of parameter object
type IValue = {
    max: number;
    min: number;
    value: number;
}

// pass parameter object instead of values as parameters
const valueWithinRange = (value:IValue): boolean => {
    return value.value <= value.max && value.value >= value.min;
}

const valueOutRange = (value:IValue): boolean => {
    return !(value.value <= value.max && value.value >= value.min);
}
```

### Preserve Whole Object
```ts
interface IInvoice {
    name: string
    paymentMethod: string
    payments: {
        amount: number
        currency: string
    }
}

const printInvoice = (name: string, paymentMethod: string, amount: number, currency: string): void => {
    console.log(`Payer: ${name}`)
    console.log(`method: ${paymentMethod} & amount: ${currency} ${amount}`)
}

const migratedPrintInvoice = (invoice: IInvoice): void => {
    printInvoice(invoice.name, invoice.paymentMethod, invoice.payments.amount, invoice.payments.currency)
}

const wholePrintInvoice = (invoice: IInvoice): void => {
    console.log(`Payer: ${invoice.name}`)
    console.log(`method: ${invoice.paymentMethod} & amount: ${invoice.payments.currency} ${invoice.payments.amount}`)
}

const invoice: IInvoice = {
    name: 'Sunwoo',
    paymentMethod: 'Credit card',
    payments: {
        amount: 50,
        currency: 'CAD'
    }
}

/**
 * test output
 * Payer: Sunwoo
 * method: Credit card & amount: CAD 50
 */
printInvoice(invoice.name, invoice.paymentMethod, invoice.payments.amount, invoice.payments.currency)
migratedPrintInvoice(invoice)
wholePrintInvoice(invoice)
```