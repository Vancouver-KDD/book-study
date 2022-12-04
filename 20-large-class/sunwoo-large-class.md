# Large Class
If the size of class is large, it often has too many fields and methods. 
Create subset of variable or methods, then create seperate classes.

## Solution
### Extract superclass
If the component has inheritance structure, we can seperate logics as inheritance.

```ts
class Employee {
    _sweTask: string
    _designTask: string
    _sweSalary: string
    _designSalary: string
    
    public get sweSalary() : string {
        return this._sweSalary
    }

    public get designSalary() : string {
        return this._designSalary
    }

    public get sweTask() : string {
        return this._sweTask
    }

    public get designTask() : string {
        return this._designSalary
    }
    
}
```
```ts

class AbsEmployee {
    _salary: string
    _task: string

    public get salary() : string {
        return this._salary
    }
    
    public get task() : string {
        return this._task
    }
}

class SweEmployee extends AbsEmployee {
    _salary: string = "10000";
    _task: string = "code"
}

class DesignEmployee extends AbsEmployee {
    _salary: string = "20000";
    _task: string = "design"
}
```