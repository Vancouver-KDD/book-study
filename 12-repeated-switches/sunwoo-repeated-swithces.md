# Repeated Swichtes
Switch statement or if statements can be scattered in different places in program.
When you have another case, you need to add new case in switch statement or add another if statement.
Instead of adding new cases into switch or if statements, consider using polymorphism.

## Solution
### Replace Conditional with Polymorphism
If we have different behaviours for different types, we need to have swith/if statments for all functions which has different behaviours. Instead of those swith or if statments, we can use polymorphism for classes.

#### Mechanics
1. If classes do not exist for polymorphic behaviour, create them together with a factory function to return the correct instance.
2. Use factory function in calling code.
3. Move the conditional function to the superclass
4. Create subclass methods
5. Leave default case

```js
class AllEmployee {
    _type: string;

    get type() {
        return this._type;
    }
    // return what to do based off their type
    get task() {
        switch (this.type) {
            case "engineer":
                return "write code";
                break;
            case "analyst":
                return "analyze data";
                break;
            case "designer":
                return "design UI";
                break;
            default:
                throw new Error(`Unknown task for type: ${this.type}`);
                ;
        }
    }
}

// base class for employees
class Employee {
    get type() {
        return "default"
    }
    get task() {
        return "unknown"
    }
}

// polymorphic class for engineer
class Engineer extends Employee {
    get type() {
        return "engineer"
    }
    get task() {
        return "write code";   
    }
}

// polymorphic class for designer
class Designer extends Employee {
    get type() {
        return "designer"
    }
    get task() {
        return "design UI";   
    }
}

// polymorphic class for analyst
class Analyst extends Employee {
    get type() {
        return "analyst"
    }
    get task() {
        return "analyze data";   
    }
}
```