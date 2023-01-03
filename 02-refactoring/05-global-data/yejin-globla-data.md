# Global data
# Quick preview 
Using global data can lead two problems.
1. It can be modified from anywhere 
2. It's hard to keep track it.    
=>  encapsulate data so that it can be tracked. 

Types of global data
1. global variables 
2. class variables
3. singleton 

---

In Java, there's no global variable concept.
(Everything is a part of class and encapsulation is the basic concept of OOP.)      
You can use static variables if you want to access class variables in other classes. 
If you do, you have to make static getter method to approach it.  

[Java Global Variable: Declaration & Examples](https://study.com/academy/lesson/java-global-variable-declaration-examples.html)

---

# Example

```javascript
// before 
let person = {age : 0};
person.age = 3; 
console.log(person.age);

//after 
const person = () =>{ 
    let age=0; 
    
    return({
        afterBDay:()=> age+=1,
        getAge:() => age
    })
};

const person = person();

person.afterBDay();
console.log(person.getAge());
```