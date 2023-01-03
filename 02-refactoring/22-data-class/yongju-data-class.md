# Data Class

Data Classes are often a sign of behaviour in the wrong place, but there are exceptions. One of the exceptions is a record being used as a result record from a distinct function invocation. A key characteristic of such a result record is that it's immutable. Immutable fields don't need to be encapsulated and information derived from immutable data can be represented as fields rather than getting methods.


If a class has fields, getter and setters only, we call it dumb data holder. If the class become to have public fields, we need to apply `Encapsulate Record`. 

##### Encapsulate Record

```
Encapsulating a record means going deeper than just the variable itself; We want to control how it's manipulated.
```

```js
// before
let anObject = {fieldOne: 24, fieldTwo: true, fieldThree: "aString"};

// after
class AnObject {
  constructor(data) {
    this.fieldOne   = data.fieldOne;
    this.fieldTwo   = data.fieldTwo;
    this.fieldThree = data.fieldThree;

    get fieldOne() { return this.fieldOne; }
    set fieldOne(arg) { this.fieldOne = arg;}
    get fieldTow() { return this.fieldTwo; }
    set fieldTow(arg) { this.fieldTwo = arg; }
    get fieldThree() { return this.fieldThree; }
    set fieldThree(arg) { this.fieldThree = arg; }
  }
}
```

If any field that should not be changed exists, use `Remove Setting Method`

##### Remove Setting Method

```js
// before
class AnObject {
  constructor(data) {
    ...
    this.fieldThree = data.fieldThree;
    
    ...
    get fieldThree() { return this.fieldThree; }
    set fieldThree(arg) { this.fieldThree = arg; }
    ...
  }
}

// after
class AnObject {
  constructor(data) {
    ...
    this.fieldThree = data.fieldThree;
    
    ...
    get fieldThree() { return this.fieldThree; }
    ...
  }
}
```