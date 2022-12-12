# Message Chain

If a client (user of APIs) is accessing too many intermediate classes to get to the final result, it creates lots of coupling in between clients and those classes.

It breaks encapsulation.

## Hide Delegate

Hide the exposed object. But overusing of hide delegate could create a middle man as a consequence.

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

// After
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
```

Ironically, it creates another coupling in between Person and Manager even though they have nothing to share and nothing to reference each other. Literally a double-edged sword.
