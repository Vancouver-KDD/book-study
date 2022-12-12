# Data Class

Data class is a little different problem from parameter object. Parameter Object may sound like a class with data as well, it focuses on passing and retrieving parameters more. And they should go after their job is done.

But Data Class prolongs their life in the program while providing important information or states. If they have specific functions to maintain those states internally, it is fine. But if they are only being used externally by other functions, or contain values that should remain immutable, these classes are being dumb data class that have nothing to do with.

## Encapsulate Record, Remove Setting Method

If the fields are exposed as naked, they should have getter and setter with limited visibility to achieve consistence API structure.

```ts
// Before
const organization = { name: "Acme GooseBerries", countryCode: "GB"};
organization.name = "It's me";

// After
class Organization {
    private _name: string;
    private _countryCode: string;
    constructor(name: string, countryCode: string) {
        this._name = name;
        this._countryCode = countryCode;
    }

    get name() {
        return this._name;
    }
    get countryCode() {
        return this._countryCode;
    }
};
```

## Move Function, Extract Function,, Split Phase
