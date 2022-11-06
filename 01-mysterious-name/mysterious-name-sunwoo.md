# Mysterious Name

The name of functions, modules, variables and classes should describe what they do and how to use them. The clear and descriptive code makes eaiser to understand for other developers and you self in the future, and save hours of puzzled incomprehension.

Renaming for something is the methodologically easy, just replace the name! 
However, it is one of the hardest thing in the programming and. 

When you can't come up with a good name for something, it could be the a sign of defective design in the software. 

## Solutions

### Change Function Declaration

#### Motivation
Function declarations represent the joints in a software system. The good joints allow developers to understand and modify the system easily. The good name of function declarations helps developers understand what the function does without looking at the code. 

The parameters of functions show how functions fit in the rest of the code. Choosing suitable parameters removes couplings and makes the functions used widely. 

#### Mechanics

##### Simple Mechanics

* Change method declarations, find all references and update them.
* Ensure that the parameter is not referenced in the method's body while removing them.
* <strong>Test</strong>
##### Migration Mechanics

* Refactor functions if necessary - extract function and modify parameters
* Apply the inline function to the old function
* Gradually change the references and remove the old one when changes get done.
* <strong>Test</strong>

#### Example
```JavaScript
// overly abbreved name
function circum(radius) {
  return 2 * Math.PI * radius;
}

function circumference(radius) {
  return 2 * Math.PI * radius;
}
```

```JavaScript
// overly abbreved name
function circum(radius) {
    return 2 * Math.PI * radius;
  }

function circum(radius) {
    return circumference(radius);
  }
  function circumference(radius) {
    return 2 * Math.PI * radius;
  }
  
```
### Rename varibles
#### Motivation
The variable's name can explain what I am up to. 
The importance of a variable's name depends on how widely it's used.
It's okay to have a letter as a variable name if a variable has a narrow scope.

#### Mechanics
* If the variable is used widely, consider encapsulate variable
* Find all references to the variable and change every one
* <strong>Test</strong>

#### Example
```JavaScript
// target variable to change its name
let tpHd = "untitled";
// access to variable
result += `<h1>${tpHd}</h1>`;
// setter
tpHd = obj['articleTitle'];
```

```JavaScript
// encapsulate variable
export function title()       {return tpHd;}
export function setTitle(arg) {tpHd = arg;}
```

```JavaScript
// change the variable name
let _title = "untitled";
export function title()       {return _title;}
export function setTitle(arg) {_title = arg;}
```

### Rename Fields
#### Motivation
Field names in record structure are significant when the record structure is widely used over the program. By improving understanding of the data structure in the software, the improvement should be embedded in the code as well. 
#### Mechanics
1. If the record has limited scope, rename all accesses to the field
2. Apply encapsulate record
3. Rename for methods, constructors and accessors.
#### Example
```JavaScript
class Organization {
    constructor(data) {
      this._name = data.name; // want to change it to title
      this._country = data.country;
    }
    get name()    {return this._name;}
    set name(aString) {this._name = aString;}
    get country()    {return this._country;}
    set country(aCountryCode) {this._country = aCountryCode;}
  }
  const organization = new Organization({name: "Acme Gooseberries", country: "GB"});
```

```JavaScript
class Organization…

  class Organization {
    constructor(data) {
      // migration to accept old name as well
      this._title = (data.title !== undefined) ? data.title : data.name;
      this._country = data.country;
    }
    get title()    {return this._title;} // adjust accessors
    set title(aString) {this._title = aString;} // adjust accessors
    get country()    {return this._country;}
    set country(aCountryCode) {this._country = aCountryCode;}
  }
```

