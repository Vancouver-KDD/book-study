#Global Data
The problem with global data is that it can be modified from anywhere in the code base, and there's no mechanism to discover which bit of code touched it. Time and again, the leads to bugs that breed from a form of spooky action from a distance and it's very hard to find out where the errant bit of program is.

###1. Encapsulate Variable (132)
#####Example
```typescript
//Before
class Person {
  name: string;
}

//After
class Person {
  private name: string;

  get name() {
    return this.name;
  }
  setName(name: string): void {
    this.name = name;
  }
}
```
###Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
- https://refactoring.guru/encapsulate-field
