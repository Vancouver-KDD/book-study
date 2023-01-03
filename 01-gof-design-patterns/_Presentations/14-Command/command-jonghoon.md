# Command

## 1. Command Pattern

### 1.1 Definition:
* The command pattern is a behavioral design pattern and is part of the GoF‘s formal list of design patterns. Simply put, the pattern intends to encapsulate in an object all the data required for performing a given action (command), including what method to call, the method's arguments, and the object to which the method belongs.
* 요청을 객체의 형태로 캡슐화하여 사용자가 보낸 요청을 나중에 이용할 수 있도록 매서드 이름, 매개변수 등 요청에 필요한 정보를 저장 또는 로깅, 취소할 수 있게 하는 패턴
* This model allows us to decouple objects that produce the commands from their consumers, so that's why the pattern is commonly known as the producer-consumer pattern.

### 1.2 Intent - why Command?
- To encapsulate all information needed to perform an action or trigger an event at a later time
- 실행될 기능을 캡슐화함으로써 기능의 실행을 요구하는 호출자(Invoker) 클래스와 실제 기능을 실행하는 수신자(Receiver) 클래스 사이의 의존성을 제거한다.실행될 기능의 변경에도 호출자 클래스를 수정 없이 그대로 사용 할 수 있도록

- Example 1: The paper order serves as a command. It remains in a queue until the chef is ready to serve it. The order contains all the relevant information required to cook the meal
- Example 2: Remote control. Remote control just has to not know what to execute but how to execute when a certain button is clicked (encapsulation) 
![image](https://user-images.githubusercontent.com/77429796/194801501-d9281d26-ac15-4e81-a81f-58da7d18c880.png)


## 2. Implementation
- UML
  * ![image](https://user-images.githubusercontent.com/77429796/194803162-6b6ac9eb-255b-4560-9f96-6fe213377267.png)
- Command
  * an object whose role is to store all the information required for executing an action, including the method to call
  * interface usually declares just a single method for executing the command
  * ConcreteCommand implementing the command interface
- Receiver
  * an object that performs a set of cohesive actions. 
  * It's the component that performs the actual action when the command's execute() method is called.
  * implements all Component methods
- Invoker
  * knows how to execute a given command but doesn't know how the command has been implemented
  * It only knows the command's interface.
- Client
  * controls the command execution process by specifying what commands to execute and at what stages of the process to execute them
  * creates and configures concrete command objects

## 3. Example

#### Command classes
```Java
@FunctionalInterface
public interface TextFileOperation {
    String execute();
}
```
```Java
public class OpenTextFileOperation implements TextFileOperation {

    private TextFile textFile;
    
    // constructors
    
    @Override
    public String execute() {
        return textFile.open();
    }
}
```
```Java
public class SaveTextFileOperation implements TextFileOperation {
    
    // same field and constructor as above
        
    @Override
    public String execute() {
        return textFile.save();
    }
}

```

 #### Receiver
```Java
public class TextFile {
    
    private String name;
    
    // constructor
    
    public String open() {
        return "Opening file " + name;
    }
    
    public String save() {  
        return "Saving file " + name;
    }
    
    // additional text file methods (editing, writing, copying, pasting)
}

```

 #### Invoker
```Java
public class TextFileOperationExecutor {
    
    private final List<TextFileOperation> textFileOperations
     = new ArrayList<>();
    
    public String executeOperation(TextFileOperation textFileOperation) {
        textFileOperations.add(textFileOperation);
        return textFileOperation.execute();
    }
}
```

 #### Client
```Java
public static void main(String[] args) {
    TextFileOperationExecutor textFileOperationExecutor
      = new TextFileOperationExecutor();
    textFileOperationExecutor.executeOperation(
      new OpenTextFileOperation(new TextFile("file1.txt"))));
    textFileOperationExecutor.executeOperation(
      new SaveTextFileOperation(new TextFile("file2.txt"))));
}

```

## 4. Pros and Cons
- Pros
  * Open/Closed Principle. You can introduce new commands into the app without breaking existing client code.
  * Single Responsibility Principle. You can decouple classes that invoke operations from classes that perform these operations.
  * As long as these new classes follow the existing interface they will work with
any existing client code without the client code needing to change
- Cons
  * The code may become more complicated since you’re introducing a whole new layer between senders and receivers.


### Sources:
- https://www.baeldung.com/java-command-pattern
- https://huisam.tistory.com/entry/CommandPattern
- https://gmlwjd9405.github.io/2018/07/07/command-pattern.html
- https://refactoring.guru/design-patterns/command
- https://en.wikipedia.org/wiki/Command_pattern
