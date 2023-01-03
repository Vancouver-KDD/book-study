# Data class

## Quick preview
데이터와 getter, setter 로만 구성된 클래스를 말한다.  순전히 데이터 용으로만 만든 메서드이다.  
우선 public 으로 정의된 데이터가 있다면 encapsulation부터 하고 이를 호출해 사용하는 메서드 중에서 옮길 메서드가 있는지 확인한다. 
데이터를 immutable 하게 만들고 싶다면 외부에서 값도 못 바꾸게 setter 도 없앤다. 

## Example
~~~java
//before 
class Person{
    private String name; 
    private int age; 
    
    public void setName(String name){
        this.name= name; 
    }
    ...
}

//after
class Person{
    private String name;
    private int age;
    
    Person(String name, int age){
        this.name= name; 
        this.age = age; 
    }
    
    public string getName(){
        return this.name; 
    }
    
    ...
}


~~~