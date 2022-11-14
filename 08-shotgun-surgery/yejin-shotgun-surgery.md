# Shotgun surgery 

## Quick review 
Every time you make a change, you have to make a lot of little edits to a lot of different classes.  
It has the same cause with divergent change but it has the opposite solution.  
For this code smell, you have to put together all common functions. 
  
## Examples 
```java
//Inline class =  the opposite of extract class
//before 
class Person {
    constructor() {
        this._telephoneNumber = new TelephoneNumber();
    }

    get telephoneNumber() {return this._telephoneNumber.telephoneNumber;}
    get officeAreaCode()    {return this._telephoneNumber.areaCode;}
    set officeAreaCode(arg) {this._telephoneNumber.areaCode = arg;}
    get officeNumber()    {return this._telephoneNumber.number;}
    set officeNumber(arg) {this._telephoneNumber.number = arg;}
}

class TelephoneNumber {
    get areaCode()    {return this._areaCode;}
    set areaCode(arg) {this._areaCode = arg;}
    get number()    {return this._number;}
    set number(arg) {this._number = arg;}

    get telephoneNumber() {return `(${this.officeAreaCode}) ${this.officeNumber}`;}
}



//after 
class Person {
    get name() {
        return this._name;
    }

    set name(arg) {
        this._name = arg;
    }

    get telephoneNumber() {
        return `(${this.officeAreaCode}) ${this.officeNumber}`;
    }

    get officeAreaCode() {
        return this._officeAreaCode;
    }

    set officeAreaCode(arg) {
        this._officeAreaCode = arg;
    }

    get officeNumber() {
        return this._officeNumber;
    }

    set officeNumber(arg) {
        this._officeNumber = arg;
    }
}




```