# Refused Bequest

Bequest : a financial term describing the act of giving assets such as stocks, bonds, jewelry, and cash, to individuals or organizations, through the provisions of a will or an estate plan.

Usually happens in a consequence of Speculative Generality, or can be found with Temporary Fields.

Well some functions that are unused or refused to inherit are usually fine. It could be a very specific edge case problem with a subclass. Or it could be a result of refactoring a couple hundred lines of code, or too much type checking into a couple lines of inheritance. Inheritance is that powerful.

But the smell, Refused Bequest, the book wants to say is about when the subclasses is reusing behaviors of superclass but does not want to support the interfaces of the superclass. Refusing implementation is fine but refusing interfaces is another problem.

The most common way to solve this is introducing delegates.

![](./images/refused-bequest.png)
[Refactoring.guru](https://refactoring.guru/smells/refused-bequest)

The above image shows that subclass Dog and Chair want to inherite Animal Superclass because it has "Leg" properties. It violates the relationship of superclass and subclasses.

## Replace subclass with Delegate & Replace Superclass with Delegate

```ts
// Before
class Animal {
    protected _leg: string
    protected _temperature: number
    protected _age: number
    protected _species: string
}

class Dog extends Animal {
    get age(): number {
        return this._age;
    }

    function eat() {
        console.log("Eat")
    }

    function walk() {
        console.log(`Use ${this._leg} to move`);
    }
}

class Chair extends Animal {
    function stand() {
        console.log(`Use ${this._leg} to stand`);
    }

    function chairInfo() {
        console.log(this._leg);
        console.log(this._species); //?????????
    } 
}

// After
class Leg {
    private _leg: string;    
    get leg(): string {
        return this._leg;
    }
}

class Animal {
    protected _temperature: number;
    protected _age: number;
    protected _species: string;
}

class Dog extends Animal {
    private _leg: Leg;

    get age(): number {
        return this._age;
    }

    function eat() {
        console.log("Eat")
    }

    function walk() {
        console.log(`Use ${this._leg} to move`);
    }
}

class Chair {
    private _leg: Leg;
    function stand() {
        console.log(`Use ${this._leg} to stand`);
    }
}
```
