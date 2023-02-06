## 6. Objects and Data Structures 

variable을 private 타입으로 하는 데는 이유 존재
그 변수들을 의존하게 하지않고, 필요에 의해 그 유형과 구현을 자유롭게 바꿀 수 있게 하고 싶어한다.

그러면 왜 게터와 세터를 통해 마치 public 타입처럼 보이게 하는 이유는 why?



### Data Abstraction

*Concrete Point*
```java
public class Point {   
    public double x;   
    public double y; 
}
```

*Abstract Point*
```java
public interface Point { 
  double getX(); 
  double getY(); 
  void setCartesian(double x, double y);    
  double getR(); 
  double getTheta(); 
  void setPolar(double r, double theta); 
}
```

Hiding implementation 은 단순히 변수들 사이에 함수의 층을 두는 걔념이 아니라.
Hiding implementation 은 abstractions에 관한 것.
classs는 단순히  getters and setters를 통해 variable를 push하는게 아닌, 구현에 대한 정보를 제공하지않고, 데이터의 본질을 다르게 하는 방식이다. 

*Concrete Vehicle*
```java
public interface Vehicle { 
  double getFuelTankCapacityInGallons();
  double getGallonsOfGasoline(); 
}

```
차량의 연료수준에 대해 구체적으로 명시함.

*Abstract Vehicle*
```java
public interface Vehicle { 
  double getPercentFuelRemaining(); 
}

```
차량의 연료수준에 대해 백분율로 추상적으로 명시함.

두번째의 경우가 선호되는 이유는 데이터의 세부적인 정보를 노출하지않고, 데이터를 추상적으로 표현하는 하기 위함이다.


### Data/Object Anti-Symmetry
데이터와 객체의 반대칭

Objects 
- abstractions 뒤에 Data 를 숨김
- Data를 다루는 function을 표시

Data structure
- Data를 표시하고
- no meaningful functions



아래 shape클래스는 date structure이고
모든 behaviour는 Geometry class에 존재.


case) function(perimeter() )을 추가한다고 하면,
- shape classes 영향 없음
- shape classes에 depend하는 클래스들도 영향없음.

case) new shape 추가 시,
- Geometry내의 function을 수정해야함.

*Procedural Shape*
```java
public class Square { 
  public Point topLeft; 
  public double side; 
} 
public class Rectangle { 
  public Point topLeft; 
  public double height; 
  public double width; 
} 
public class Circle { 
  public Point center; 
  public double radius; 
} 
public class Geometry { 
  public final double PI = 3.141592653589793; 
  public double area(Object shape) throws NoSuchShapeException 
  { 
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
    throw new NoSuchShapeException(); 
  } 
}
```
반대로 

case) function - perimeter()을 추가한다고 하면,
- all of the shapes 반드시 수정요.

case) new shape 추가 시,
- Geometry내의 function 영향 없음.

*Polymorphic Shapes*

```java
public class Square implements Shape { 
  private Point topLeft; 
  private double side; 
  public double area() { 
    return side*side; 
  } 
} 
public class Rectangle implements Shape { 
  private Point topLeft; 
  private double height; 
  private double width; 
  public double area() { 
    return height * width; 
  } 
}
public class Circle implements Shape { 
  private Point center; 
  private double radius; 
  public final double PI = 3.141592653589793;   public double area() { 
    return PI * radius * radius; 
  } 
}
```
즉, 이 두가지는 상호보완적(the complimentary nature)인 성질을 띈다.
하지만 사실상 반대이다.

>반대적 측면
- Procedural code (code using data structures) makes it easy to add new functions without changing the existing data structures. 

- OO code, on the other hand, makes it easy to add new classes without changing existing functions.

> 상호보완적(the complimentary nature) 측면
- Procedural code makes it hard to add new data structures because all the functions must change. 
- OO code makes it hard to add new functions because all the classes must change.

In a nutshell,
- OO는 새로운 데이터 유형 추가시 굿!(기능말고)
- Procedural code는 기능을 추가 시 적합(데이터 유형말고)

Mature programmers know that the idea that everything is an object is a myth. Some- times you really do want simple data structures with procedures operating on them.

### The Law of Demeter

모듈이 하지말아야하는 Demeter의 2법칙?
heuristic : 복잡한 과제를 간단한 판단 작업으로 해결하는 의사결정 단순화!

Object는 data를 숨기고, operation을 노출 시킵니다. 즉 interanal structure를 보여주고 있기 때문에 접근자(Accessor)를 통한 interanal structure 접근을 허락하면 안된다는 것을 의미합니다. 

The Law of Demeter은 Class C의 Function f는 다음의 Functions들만 호출해야 한다고 말한다

```
• C 

• An object created by f

• An object passed as an argument to f 

• An object held in an instance variable of C
```

즉, The method should not invoke methods on objects that are returned by any of the allowed function.

다음이 하나의 위반의 예

```
final String outputDir = ctxt.getOptions().getScratchDir().getAbsolutePath();
```


#### Train Wrecks

방금 본 위와 같은 코드를 Train wreck이라 부르고 엉성한 스타일로 간주되면 아래와 같은 스타일로 쪼개서 구성되어져야 바람직합니다.

```
Options opts = ctxt.getOptions();
File scratchDir = opts.getScratchDir();
final String outputDir = scratchDir.getAbsolutePath();
```
two snippets의 위 형태는 그럼 Demeter의 법칙을 위반하는 것일까?

경우 1. ctxt, Options, and ScratchDir are **objects**

 internal structure는 **hidden**되야함.
 -> 이경우 Demeter의 법칙을 위반

경우 2. ctxt, Options, and ScratchDir are **data structures** with no behavior

 internal structure는 **exposed** 되야함.


 
그러므로 아래와 같이
```
final String outputDir = ctxt.options.scratchDir.absolutePath;
```
This issue would be a lot less confusing if data structures simply had public variables and no functions, whereas objects had private variables and public functions. 

#### Hybrids
This confusion sometimes leads to unfortunate hybrid structures that are half object and half data structure. Such hybrids make it hard to add new functions but also make it hard to add new data structures. *They are the worst of both worlds.*

#### Hiding Structure

> ctxt.getAbsolutePathOfScratchDirectoryOption(); 

-> 첫 번째 옵션은 ctxt 객체의 메소드를 폭발시킬 수 있다. 

> ctx.getScratchDirectoryOption().getAbsolutePath().
getScratchDirectoryOption()

-> 개체가 아닌 데이터 구조를 반환합니다. 두 가지 선택지 모두 기분이 좋지 않다.

So why did we want the absolute path of the scratch directory? What were we going to do with it?

```
String outFile = outputDir + "/" + className.replace('.', '/') + ".class";
FileOutputStream fout = new FileOutputStream(outFile); 
BufferedOutputStream bos = new BufferedOutputStream(fout);

```
 we see that the intent of getting the absolute path of the scratch directory was to create a scratch ﬁle of a given name.
 
그래서, 아래와 같이 
>BufferedOutputStream bos = ctxt.createScratchFileStream(classFileName);

### Data Transfer Objects

DTOs are very useful structures, especially when communicating with databases or parsing messages from sockets, and so on.

**“Bean” form** 

Beans have private variables manipulated by getters and setters.

address.java
```java
public class Address { 
  private String street; 
  private String streetExtra; 
  private String city; 
  private String state; 
  private String zip; 
  public Address(String street, String streetExtra, 
                  String city, String state, String zip) { 
    this.street = street; 
    this.streetExtra = streetExtra; 
    this.city = city; 
    this.state = state; 
    this.zip = zip; 
  } 
  public String getStreet() { 
    return street; 
  } 
  public String getStreetExtra() { 
    return streetExtra; 
  } 
  public String getCity() { 
    return city; 
  }
  public String getState() {     
    return state; 
  } 
  public String getZip() {     
    return zip; 
  } 
}
```
#### Active Record

- data structures with public (or bean-accessed) variables and navigational methods like save and find
- Active Records are direct translations from database tables, or other data sources

종종 개발자들이 비지니스 로직을 해당 구조를 객체처럼 만드는데, 어색한 방법이다.

해결책은 Active Record를 Data Structure로 취급하고 비지니스 로직을 포함하면서 내부데치터를 숨길 수 있는 Object를 만드는 것입니다.

### Conclusion

**Objects** expose behavior and hide data. 

- pros : This makes it easy to add new kinds of objects without changing existing behaviors. 

- cons :It also makes it hard to add new behaviors to existing objects. 

**Data structures** expose data and have no signiﬁcant behavior. 

- pros : This makes it easy to add new behaviors to existing data structures.

- cons : It makes it hard to add new data structures to existing functions. 


주어진 시스템 환경에 따라
- 새로운 DataType을 추가하기 위한 flexibilty를 위해 ==> object화
- 새로운 Behavior를 추가하기 위한 flexibilty를 위해 ==> data types and procedures 

결국, Good software developers understand these issues without prejudice and choose the approach that is best for the job at hand.
