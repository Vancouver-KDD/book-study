# Primitive Obession
Using primitive variables is much eaiser than wrting a class for the field. 
However, by adding primitive variables, the class becomes hudage adn unwieldy. 

## Solution
### Replace Primitive with Object
Sometime representing some data as simple primitive variable make complicate while developing the system.
A string of phone number has more logic more than just a string. Instead of representing data as primitive variable, use object to represent some data.
So variable object can handle logics.

#### Mechanics
1. Encapsulate variable
2. Create simple value vlass for the data value
3. replace the field with new object
4. replace accessor
5. Test

```ts
class Contact {
    _phoneNumber = '(+1)778-835-6153'
}

class PhoneNumber {
    _areaCode = 1;
    _numOfDigits = 10;
    _firNum = 778;
    _secNum = 835
    _thridNum = 6153

    get areaCode() {
        return this._areaCode;
    }

    set areaCode(areaCode) {
        this._areaCode = areaCode;
    }
}
```
