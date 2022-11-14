# Mutable data

## Quick preview
If data can be changed in anywhere, it can make unexpected bugs and consequences.  
So in functional programming, data is always considered as immutable. (It should not be changeable and program only use the copy of that.)  

But there are ways to prevent unexpected changes of data. 
The basic concept is encapsulating data. It narrows the ways of changes so that it can be tracked and predictable.   
You can also remove setter method to make variable read-only.   

## example
```java
//before
class Person {
    private name; 
    private age; 
    
    public void setName(String name){
        this.name =name; 
    }
}


//after
class Person {
    private final name; 
    private final age;
    
    Person(final String name, final int age){
        this.name= name; 
        this.age = age; 
    }
    
    public final String getName(){return name; }
    public final int getAge(){return age; }
    
}
```