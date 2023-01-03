# Primitive Obsession

For some people, defining own custom data type feel like a two-edge sword.

But in fact and most of the time, custom type represents the intention and the context of the data much better and it gives a integrated behavior as well.

Even further, overloading operators will make a very strong component.

## Replace Primitive with Object

### Examples

```javascript
//Before
class Order {
    private _priority: string;
    constructor(data) {
        this.priority = data.priority;
    }

    get priority(): string { return this._priority; }
};

function foo(): number {
    highPriorityCount = orders.filter(o => "high" === o.priority
                                        || "rush" === o.priority)
                                .length;
    return highPriorityCount
};

// After
class Priority {
    constructor(value) {
        if (value instanceof Priority) return value;
        if (Priority.legalValues().includes(value))
            this._value = value;
        else
            throw new Error(`<${value}> is invalid for Priority`);
    }
    toString(): string { return this._value; }
    get _index(): number { return Priority.legalValues().findIndex(s => s === this._value); }
    static legalValues() { return ['low', 'normal', 'high', 'rush']; }

    equals(other): bool { return this._index === other._index; }
    higherThan(other): bool { return this._index > other._index; }
    lowerThan(other): bool { return this._index < other._index; }
};

class Order {
    private _priority: Priority;
    constructor(data) {
        this._priority = data.priority;
    }
    get priority(): Priority { return this._priority; }
    set priority(aString: string) { this._priority = aString; }
};

function foo(): number {
    highPriorityCount = orders.filter(o => o.priority.higherThan(new Priority("normal")))
                                .length;
    return highPriorityCount
};
```

## Replace Type Code with Subclasses

Goal : convert various types of a single class into several subclasses

## Replace Conditional with Polymorphism

Goal : same as `Replace Type Code with Subclasses`, turn them into several subclasses.
