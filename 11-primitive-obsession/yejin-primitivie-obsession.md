#Primitivie obsession 

## Quick preview
When you are obsessed with using only primitive type everywhere,  
*setbacks* 1. Lose type safety 2. They can't contain model-specific logic. So that logic should be handled in class.  

## Example

```java
// Replace primitive with objects 
//before
class Student {
    ...
    String phoneNumber;

    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber.startsWith("010")) {
            this.phoneNumber = phoneNumber;
        }else{
           // ... validation logic
        }
    }
}

//after
class Student {
    ...
    private PhoneNumber phoneNumber; 
    
    Student(..., String inputNumber){
        ...
        phoneNumber = new PhoneNumber(inputNumber);
    }
    public void getPhoneNumber() {
       this.phoneNumber =phoneNumber.value();
    }
}

class PhoneValid {
    private String validNumber; 
    PhoneValid(String inputNumber){
        //... validation & initialization logic
    }
    
    public String value(){
        //... validation logic
        return validNumber;
    }
}



```

[What is Primitive Obsession and How Can we Fix it?](https://hackernoon.com/what-is-primitive-obsession-and-how-can-we-fix-it-wh2f33ki)