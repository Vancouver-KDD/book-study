# Feature Envy

Generally, we want methods to be decoupled from other modules and have isolated responsibility. In other words, independency.

But if a method is using so much data or functions from other modules than its own, it *envies* features of other module.

## 1. Move Function

### Motivation

A tool for literally solving the *Feature Envy* problem.

>One of the most straightforward reasons to move a function is when it references elements in other contexts more than the one it currently resides in. Moving it together with those elements often improves encapsulation, allowing other parts of the software to be less dependent on the details of this module. - Refactoring(Move Function)

### Mechanics

There is no rule but core idea, find a perfect fit spot where the function belongs better. 

Mostly the commonly favoured place is where the similar logics are happening, same data are being used in a place, or similar functions are being called.

If they share any common, it means they are at proper places.

### Example

```ts
// Before
class AccountType {
    private isPremium: bool;
};

class Account {
  private accountType: AccountType;
  private overdraftCharge: number;
  private daysOverdrawn: number;

  get bankCharge(): number {
    let result = 4.5;
    if (this.daysOverdrawn > 0) result += this.overdraftCharge;
    return result;
  }

  get overdraftCharge(): number {
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
}
```

`overdraftCharge` has a potential to grow and it doesn't share any common.

```ts
class AccountType {
    private isPremium: bool;

    get overdraftCharge(account: Account): number {
        if (this.isPremium) {
            const baseCharge = 10;
            if (account.daysOverdrawn <= 7)
                return baseCharge;
            else
                return baseCharge + (account.daysOverdrawn - 7) * 0.85;
        }    

        return account.daysOverdrawn * 1.75;
    }
};

class Account {
    private accountType: AccountType;
    private overdraftCharge: number;
    private daysOverdrawn: number;

    get bankCharge(): number {
        let result = 4.5;
        if (this.daysOverdrawn > 0)
            result += this.overdraftCharge;
        return result;
    }

    get overdraftCharge(account: Account): number {
        return this.accountType.overdraftCharge(this);
    }
};
```

## 2. Extract Function

### Motivation

If there is a somewhat large size of the code without any function calls, you might end up reading line by line again everytime you get hit by the function.

Because how the code block (fragment) looks sometimes doesn't represent well what it is *trying to do*.

The effective way of describing those code blocks is extracting them out into a separate function with a well self-describing name.

### Example

```ts
// Before
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

```ts
// After
function printOwing(invoice) {
    printBanner();
    const outstanding = calculateOutstanding(invoice);
    recordDueDate(invoice);  
    printDetails(invoice, outstanding);
}

function printBanner() {
    console.log("***********************");
    console.log("**** Customer Owes ****");
    console.log("***********************");  
}
function printDetails(invoice, outstanding) {
    console.log(`name: ${invoice.customer}`);
    console.log(`amount: ${outstanding}`);
    console.log(`due: ${invoice.dueDate.toLocaleDateString()}`);
}
function recordDueDate(invoice) {
    const today = Clock.today;
    invoice.dueDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 30);
}
function calculateOutstanding(invoice) {
    let outstanding = 0;
    for (const o of invoice.orders) {
      outstanding += o.amount;
    }
    return outstanding;
}
```

## 3. Design Pattern - Strategy & Visitor Pattern, Self-delegation Pattern

### Motivation

From above examples, *Move Function* or *Extract Function* are usually used to put similar features together or separate logic branches from long functions to make them feasible for modularity.

Spots for Strategy or Visitor Pattern can be found where the reason of Move & Extract function was for consolidating different algorithms into one piece.

### Mechanics

**Strategy Pattern** lets you decide which Tool / Method / Algorithm to use at runtime.

**Visitor Pattern** visits each Object while appling same algorithm on them.

### Samples 

filter, map functions are one of the good example of Strategy pattern.

```js
const array1 = [1, 4, 9, 16];

// pass a function to map
const map1 = array1.map(x => x * 2);

console.log(map1);
```

> Array [2, 8, 18, 32]
