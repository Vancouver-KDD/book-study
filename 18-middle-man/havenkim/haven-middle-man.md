# Middle Man

Inverse of Message Chain. It is a similar situation of Speculative Generality. As time goes by, you found that too many **Hide delegate** generated lots of method. Then, the intermediate class has no meaning anymore.

## Remove Middle Man (Exposing delegate if it deserves to be a real class)

It is a inverse solution of Hide Delegate.

There is no exact answer about which one is the best practice. You would be developing Message chains at the beginning.

After a couple changes, you might notice the delegate object is too big and the middle class is doing nothing else but forwarding delegation. Then you would consider *Remove Middle Man* at that time.

## Example

```ts
// Before
class Person {
    private _name: string;
    private _department: Department;

    constructor(name: string) {
        this._name = name;
    }

    get name(): string { return this._name; }
    get manager(): Manager { return this._department.manager; }
}

function foo() {
    let person = new Person();
    let manager = person.manager;
}

// After
class Person {
    private _name: string;
    private _department: Department;

    constructor(name: string) {
        this._name = name;
    }

    get name(): string { return this._name; }
    get department() : Department { return this.department; }
    set department(dept: Department) { this.department = dept; }
}
class Department {
    private _manager: Manager;
    private _chargeCode: string;

    constructor(manager: Manager, chargeCode: string) {
        this._manager = manager;
        this._chargeCode = chargeCode;
    }

    get manager() : string { return this._manager; }
    set manager(manager: Manager) { this._manager = manager; }
    get chargeCode() : string { return this._chargeCode; }
    set chargeCode(chargeCode: string) { this._chargeCode = chargeCode; }
}

function foo() {
    let person = new Person();
    let manager = person.department.manager;
}
```