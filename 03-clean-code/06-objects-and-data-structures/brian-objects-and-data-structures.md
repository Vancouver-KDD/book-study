# Objects and Data Structures
## Data Abstraction
- 데이터 추상화는 단순히 변수를 private 로 하고 getter 와 setter 를 추가한다고 이루어지는 것이 아니다
- 문제는 데이터를 얼마나 추상화하여 드러내느냐이다
```java
public interface Vehicle {
    double getGallonsOfGasoline();
    // VS
    double getPercentFuelRemaining();
}
```

## Data/Object Anti-Symmetry
- http://guruble.com/activerecord%EB%8A%94-%EC%99%9C-active-record%EC%9D%B8%EA%B0%80/?ckattempt=1

| Data structure                                      | Object                                                           |
|-----------------------------------------------------|------------------------------------------------------------------|
| 기존의 **자료구조**를 변경하지 않으면서<br/> 새로운 **함수**를 추가하기 쉽다.   | 기존의 **함수**를 변경하지 않으면서<br/> 새로운 **클래스**를 추가하기 쉽다.                 |
| 새로운 **자료구조**를 추가하기 위해서<br/> **기존의 코드를을 변경**해 주어야하는 어려움이 있다. | 새로운 **함수**를 추가하기 위해서<br/> **기존의 인터페이스의 모든 클래스들을 변경**해 주어야하는 어려움이 있다. |

```java
public class Square {
  public Point topLeft;
  public double side;
}
 
public class Rectangle {
  public Point center;
  public double height;
  public double width;
}
 
public class Circle {
  public Point center;
  public double radius;
}
 
public class Geometry {
  public final double PI = 3.141592653589793;
 
  public double area(Object shape) throws NoSuchShapeException{
    if (shape instanceof Square) {
      Square s = (Square)shape;
      return s.side * s.side;
    } 
    else if (shape instanceof Rectangle) {
      Rectangle r = (Rectangle)shape;
      return r.height * r.width;
    }
    else if (shape instanceof Circle) {
      Circle c = (Circle)shape;
      return PI * c.radius * c.radius;
    }
    else {
      throw new NoSuchShapeException();
    }
  }
}
```
```java
public class Square implements Shape {
  private Point topLeft;
  private double side;
 
  public double area() {
    return side * side;
  }
}
 
public class Rectangle implements Shape {
  private Point center;
  private double height;
  private double width;
 
  public double area() {
    return height * width;
  }
}
 
public class Circle implements Shape {
  private Point center;
  private double radius;
  public final double PI = 3.141592653589793;
 
  public double area() {
    return PI * radius * radius;
  }
}
```

## The Law of Demeter
- https://tecoble.techcourse.co.kr/post/2020-06-02-law-of-demeter/
- a module should not know about the innards of the objects it manipulates.
- a method f of a class C should only call the methods of these:
```java
class Demeter {
    private Member member;

    public myMethod(OtherObject other) {
        // ...
    }

    public okLawOfDemeter(Paramemter param) {
        myMethod();                 // 1. 객체 자신의 메서드
        param.paramMethod();        // 2. 메서드의 파라미터로 넘어온 객체들의 메서드
        Local local = new Local();
        local.localMethod();        // 3. 메서드 내부에서 생성, 초기화된 객체의 메서드
        member.memberMethod();      // 4. 인스턴스 변수로 가지고 있는 객체가 소유한 메서드
    }
}
```
- In other words, talk to friends, not to strangers

### Train Wrecks
- Violate the Law of Demeter
```java
final String outputDir = ctxt.getOptions().getScratchDir().getAbsolutePath();
```
```java
Options opts = ctxt.getOptions();
File scratchDir = opts.getScratchDir();
final String outputDir = scratchDir.getAbsolutePath();
```
```java
final String outputDir = ctxt.options.scratchDir.absolutePath;
```
### Hybrids
- Such hybrids make it hard to add new functions but also make it hard to add new data structures.
### Hiding Structure
```java
ctxt.getAbsolutePathOfScratchDirectoryOption();
// OR
ctx.getScratchDirectoryOption().getAbsolutePath(); // getScratchDirectoryOption() will return data structure
```
```java
final String outputDir = ctxt.getAbsolutePathOfScratchDirectoryOption();
String outFile = outputDir + "/" + className.replace('.', '/') + ".class";
FileOutputStream fout = new FileOutputStream(outFile);
BufferedOutputStream bos = new BufferedOutputStream(fout);
// VS
BufferedOutputStream bos = ctxt.createScratchFileStream(classFileName); 
```
## Data Transfer Objects
- a data transfer object or DTO
- bean
## Active Record
- Active Records are special forms of DTOs
- but they typically have navigational methods like save and find
- This is awkward because it creates a hybrid between a data structure and an object.
- treat the Active Record as a data structure

## Conclusion
| Data structure                                                  | Object                                                           |
|-----------------------------------------------------------------|------------------------------------------------------------------|
| easy to add new behaviors <br/>to existing data structures      | easy to add new kinds of objects <br/>without changing existing behaviors |
| makes it hard to add new data structures <br/>to existing functions | hard to add new data structures <br/>to existing functions |
