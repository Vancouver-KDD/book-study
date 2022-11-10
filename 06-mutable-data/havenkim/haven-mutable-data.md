# Mutable Data

Mutability and purity is the key of stability of the program. How much can you trust your functions? Is there any consequences after a call of function?

### 5. Separate Query from Modifier (306p) ###

Do not call side effect functions inside of data manipulation function.

If the function is going to return a resulted value, it really should not touch any other parts of the code.

> You don't have a way to get data without making changes to its condition. And it is even more disaster if someone else calls it without knowing side effects.

##### Examples

```ts
// Before
function calculateTodaySales(date): number {
    const receipts = db.getReceipts(date);
    const totalSales = recepits.reduce((sum, a) => sum + a, 0);
    db.removeReceipts(date);
    return totalSales;
}

//You should not call any side-effect functions during the sales calculation.

//After
function calculateTodaySales(date): number {
    const receipts = db.getReceipts(date);
    const totalSales = recepits.reduce((sum, a) => sum + a, 0);
    return totalSales;
}

function calculateTodaySales(receipts): number {
    return recepits.reduce((sum, a) => sum + a, 0);
}

// In the middle of other function...
const receipts = db.getReceipts(date);
const todaySales = calculateTodaySales(receipts);
db.removeReceipts(date);
```


### 6. Remove Setting Method (331p) ###

Setting method also could be an evil because it actually goes against the field accesing scope. 

##### Examples

```ts
// Before
class Book {
    get ISBN(): string { return this._isbn; }
    set ISBN(isbn: string) { this._isbn = isbn; }
    get name(): string { return this._name; }
    set name(name: string) { this._name = name; }
}

// After
class Book {
    constructor(isbn: string, name: string) {
        this._isbn = isbn;
        this._name = name;
    }

    get ISBN(): string { return this._isbn; }
    get name(): string { return this._name; }
}
```

### 7. Replace Derived Variable with Query (248p) ###

Derived variables are which get decided by other variables, not itself.

Hence, there is a quite high chance that you make a synchronization mistake.

##### Examples

```ts
// Before
class ProductionPlan {
    get production(): number {
        return this._production;
    }
    applyAdjustment(anAdjustment): number {
        this._adjustments.push(anAdjustment);
        this._production += anAdjustment.amount;
    }
}

// After
class ProductionPlan {
    get production(): number {
        return this._adjustments.reduce((sum, a) => sum + a.amount, 0);
    }
    function applyAdjustment(anAdjustment: number) {
        this._adjustments.push(anAdjustment);
    }
}
```