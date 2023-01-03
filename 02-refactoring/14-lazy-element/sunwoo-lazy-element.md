# Lazy Element
Sometime, when we build the system, we think of the strtucture and build the structure for the future. If our expectation goes wrong, like it does not have to be extensible, we don't need to keep the relatively complex structure. 

## Collapse Hierachy
While refactoring the class hierachy, we move function or fields. After refactoring, if child class has no big different with parent class, we consider merging them into one class.

1. Choose which one to remove
2. Move field and methods
3. Adjust reference
4. Remove the empty class
5. Test

```ts
class User {
    private _name : string;
    public get name() : string {
        return this._name;
    }
    public get type() : string {
        return "Default";
    }
}

class Subscriber extends User {
    public get type() : string {
        return "Subscriber";
    }
}
class FreeTierUser extends User {
    public get type() : string {
        return "free tier user";
    }
}

// the company decides to release the product for free!
// we don't need to distinguish them to subscriber and freetier user.
// There is only one user type
// delete type getter and merge them into one class
class RefactoredUser {
    private _name : string;
    public get name() : string {
        return this._name;
    }
}
```