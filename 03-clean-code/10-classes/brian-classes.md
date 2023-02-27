# Classes
- we’ve paid attention to higher levels of code organization

## Class Organization
```java
public static member1;
private static member2;
private member3;
public void function1();
private void function2();
```
### Encapsulation
- protected or package scope are possible so that it can be accessed by a test

## Classes Should Be Small!
- The name of a class should describe what responsibilities it fulfills.
- We should also be able to write a brief description of the class in about 25 words, 
  - without using the words “if,” “and,” “or,” or “but.”
### The Single Responsibility Principle
- we focus on 
  1. getting our code to work more than 
  2. organization and cleanliness.

- many developers fear that 
  - a large number of small, single-purpose classes 
  - makes it more difficult to understand the bigger picture. => No!!

- Do you want
  - small drawers each containing well-defined and well-labeled components?
  - Or
  - a few drawers that you just toss everything into?

- lots of things we don’t need to know right now
### Cohesion
- When cohesion is high, 
  - it means that the methods and variables of the class are co-dependent 
  - and hang together as a logical whole.
### Maintaining Cohesion Results in Many Small Classes
- 응집도를 유지하면 작은 클래스 여럿이 나온다.
- If we promoted those four variables to instance variables of the class, 
  - then we could extract the code without passing any variables at all.
- instance variables that exist solely to allow a few functions to share them
  - It means it lose cohesion 
    - => When classes lose cohesion, split them!

## Organizing for Change
```java
public class Sql {
    public Sql(String table, Column[] columns) {
        ...
    }
    public String create() {
        ...
    }
    public String insert(Object[] fields) {
        ...
    }
    public String select(Column column, String pattern) {
        ...
    }
    ...
}
```
- Sql class violates the SRP

```java
abstract public class Sql {
    public Sql(String table, Column[] columns) {
        ...
    }    
    abstract public String generate(); 
} 

public class CreateSql extends Sql {
    public CreateSql(String table, Column[] columns){
        ...
    }    
    @Override public String generate() {
        ...
    } 
} 

public class SelectSql extends Sql {
    public SelectSql(String table, Column[] columns) {
        ...
    }   
    @Override public String generate() {
        
    } 
} 
public class InsertSql extends Sql {
    public InsertSql(String table, Column[] columns, Object[] fields) {
        ...
    }    
    @Override public String generate() {
        ...
    }    
    private String valuesList(Object[] fields, final Column[] columns) {
        ...
    } 
    ...
} 
```
- It supports the SRP. 
- It also supports another key OO class design principle known as the Open-Closed Principle

- But Sql 클래스만 봤을때 바로 기능을 알 수 없고 instance of 를 사용해 각장의 실제 객체를 확인해야 한다.
  - 새로운 개발자가 보기에 코드를 이해하기 어려울 수 있다. 
### Isolating from Change
- concrete details is at risk when those details change
  - interfaces and abstract classes to help isolate the impact of those details
- The lack of coupling means that 
  - the elements of our system are better isolated from each other and from change
- Dependency Inversion Principle (DIP) 
  - the DIP says that our classes should depend upon abstractions, not on concrete details
