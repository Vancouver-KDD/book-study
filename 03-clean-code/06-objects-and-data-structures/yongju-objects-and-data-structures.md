# Chapter 6. Objects and Data Structures
> 우리가 variable들을 private으로 만드는 이유가 있다. 우리는 어떤 것도 그 variable들에 dependency를 가지기를 원하지 않는다.
> 그렇다면 왜 많은 프로그래머들이 마치 그 variable이 public인 것처럼 생각 없이 getter와 setter를 만들까?

#### Data Abstraction
아래 두 코드를 비교해보자
```java
// Concrete Point
public class Point {
  public double x;
  public double y;
}

// Abstract Point
public interface Point {
  double getX();
  double getY();
  void setCartesian(double x, double y);

  double getR();
  double getTheta();
  void setPolar(double r, double theta);
}
```
`Absract Point`
- Abstract Point 코드의 장점은 이 코드를 inherit하는 class 안에서의 implementation이 rectangular coordinates일 지 polar coordinates일 지 알 수 없지만, 의심할 여지 없이 하나의 data structure를 나타내고 있다는 것이다
- 그 외에도 유저에게 각각의 coordinates를 읽게 해 주지만 setCartesian, setPolar 메소드를 통해 각각의 coordinates를 한꺼번에만 설정할 수 있도록 강제한다

`Concrete Point`
- 명확하게 rectangular coordinates라는 게 보이고 각각의 coordinates를 따로 설정하도록 강제한다. 이것은 유저에게 implementation을 이미 노출하고 있는 것이다. (x, y의 값을 넣어줌으로써 유저는 '이 두 값으로 rectangular coordinates를 설정하는거구나'라고 알아챌 수 있음)
- 두 variables(x, y)를 private으로 바꾸고 getter와 setter를 만들어도 같은 결과일 것이다  

> Hiding implementation is about abstractions!
- Implementation을 숨기는 것은 variable들 사이에 function이라는 레이어를 넣어주는 것이 아니다.
- Getter와 setter를 통해 variable들을 유저에게 class 밖으로 노출 시키는 게 아니고, abstract interface를 노출시킴으로써 유저가 implementation에 대해서는 전혀 모르는 상태에서 필요한 데이터를 얻어갈 수 있도록 해야 한다

```java
// Concrete Vehicle
// 우리는 아래 두 메소드가 accessor라는 것을 단번에 알 수 있다
public interface Vehicle {
  double getFuelTankCapacityInGallons();
  double getGallonsOfGasoline();
}

// Abstract Vehicle
// 어떤 형태의 데이터가 존재하는 지 알기 쉽지 않다
public interface Vehicle {
  double getPercentFuelRemaining();
}
```
- 위의 두 코드 중에서, 두번째 Abstract Vehicle의 코드를 쓰는 것을 추천한다. 이유는 우리는 data의 디테일이 아닌 abstract term을 노출시켜야 하기 때문이다. 
- interface나 getter/setter를 사용한다고 만사 해결이 되는 것이 아니다. 깊은 생각을 통해 해당 object가 가지고 있는 데이터를 표현할 수 있는 최선의 방법을 선택해야 한다 
- 그럼에도 불구하고 가장 나쁜 방법은 생각 없이 getter/setter를 사용하는 것이다

#### Data/Object Anti-Symmetry
`Object`: abstraction을 통해 데이터를 숨기고 그 데이터를 사용하는 function을 노출한다
`Data structure`: 데이터를 노출하고 의미있는 function은 가지고 있지 않다
- 이 두 컨셉은 거의 반대이다. 그 다른점이 사소해보이지만, 많은 것을 내재하고 있다

밑에서 두 가지 코드 예제(procedural, object-oriented)를 보고 어떤 차이가 있는지 알아볼 것이다.

먼저 **절차 지향적** 코드 예제를 보자. 
Shape는 Data structure이고, Geometry는 Object라는 것을 잘 기억하자
```java
// Procedural Shape
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
  public final double PI  = 3.141592653587893;

  public double area(Object shape) throws NoSuchShapeException {
    if (shape instanceOf Square) {
      Square s = (Square)shape;
      return s.side * s.side;
    } else if (shape instanceOf Rectangle) {
      Rectangle r = (Rectangle)shape;
      return r.height * r.width;
    } else if (shape instanceOf Circle) {
      Circle c = (Circle)shape;
      return PI * c.radius * c.radius;
    }
    throw new NoSuchShapeException;
  }
}
```
- 만약 Geometry에 둘레의 길이를 구하는 perimeter() 라는 function을 추가하면 어떻게 될까? 영향을 받는 것은 아무것도 없을 것이다
- 만약 새로운 shape를 하나 추가하면 어떻게 될까? Geometry 클래스에 새로 추가된 shape에 대한 코드를 적어줘야 한다


다른 예제를 하나 더 보자, 이번엔 **객체 지향적** solution이다.
```java
// Polymorphic Shapes
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
  public final double PI = 3.141592653587893;

  public double area() {
    return PI * radius * radius;
  }
}
```
- 두 예제를 보니 Object와 data structure가 반대의 배경을 가지고 있다는 게 확실하지 않은가?

- 근본적인 차이점은 다음과 같다.
  Data structure를 사용하는 `절차지향적` 코드는 이미 존재하는 data structure를 변경하지 않고 새로운 function을 추가하기가 쉽다. 반대로, Object를 사용하는 `객체지향적` 코드는 이미 존재하는 function들은 변경하지 않고 새로운 class(object)를 추가하기가 쉽다.
  또한,  
  `절차지향적` 코드는 모든 function을 바꿔야 하기 때문에 새로운 data structure를 추가하기가 어렵고, `객체지향적` 코드는 모든 class(object)를 변경해야 하기 때문에 새로운 function을 추가하기가 어렵다.  
  결론적으로 `절차지향적` 코드에서 어려운 것은 `객체지향적` 코드에서 쉽고, `객체지향적` 코드에서 어려운 것은 `절차지향적` 코드에서 쉽다
- 성숙한 프로그래머는 '모든것이 object다'가 미신따위라는 것을 알고 있다. 우리는 때때로 진짜로! 간단한 data structure를 사용해 절차지향적인 코드를 사용하고 싶을 때가 있을 것이다

#### The Law of Demeter
- **Law of Demeter**는 module은 objects의 안을 알아서는 안된다고 말한다. 즉, object는 accessor를 통해 internal structure를 노출해서는 안된다는 말이다
- 조금 더 자세하게 보면, Law of Demeter에서는 클래스 C의 메소드 f는 오직 다음에 해당되는 메소드들만 불러야 한다고 한다
  1. C
  2. f에 의해 만들어진 object
  3. argument를 통해 f로 전달된 object
  4. C의 instance variable이 가지고 있는 object
- 또한 위에 나열된 function이 반환한 object의 method들은 사용해서는 안된다. 즉, 직접적인 연관이 있는 메소드들만 사용하고 그 외에는 사용하지 말라는 뜻이다(talk to friends, not to strangers). 이걸 반하는 예제를 하나 보자
```java
// getScratchDir과 getAbsolutePath는 strangers!
final String outputDir = ctxt.getOptions().getScratchDir().getAbsolutePath();
```

#### Train Wrecks
- 위와 같이 생긴 코드를 train wreck이라고 부르는데, 이유는 여러개의 기차들이 꼬여있는 것 같기 때문이다. 이런 chains of calls는 좋지 않은 디자인이고 쓰는 걸 지양해야한다(아래 [G36]참고)
>[G36] Avoid Transitive Navigation
> 우리는 한 모듈이 주위의 다른 collaborator들에 대해 많이 아는 것을 원하지 않는다. 
그 코드는 `a.getB().getC().doSomething()` 과 같은 코드로 나타날 수 있기 때문이다. 
이 경우에 B와 C 사이에 우리가 Q라는 object를 넣는 디자인으로 변경한다고 하면 우리는 `a.getB().getC()` 가 사용되는 모든 곳을 찾아서 `a.getB().getQ().getC()`와 같이 바꿔주어야 한다. 이런 경우 위에 코드를 변경한 것처럼 collaborator가 우리가 원하는 service를 제공하게끔 하는 것이 좋다.
e.g. `myCollaborator.doSomething()`

```java
// better solution
Options opts = ctxt.getOptions();
File scratchDir = opts.getScratchDir();
final String outputDir = scratchDir.getAbsolutePath();
```
- 여전히 모듈들이 다른 object들을 어떻게 navigate해야 하는 지 다 알고 있으므로 Law of Demeter에 위반된다. 즉, object임에도 불구하고 internal structure이 모두 노출되어 있다는 뜻이다. 
- 만약 ctxt, Options, 그리고 ScratchDir이 data structure라면 어떨까? Data structure는 internal structure를 노출하므로 Law of Demeter는 적용되지 않는다
```java
// Data structure version
final String outputDir = ctxt.options.scratchDir.absolutePath;
```
- 이런 간단한 data structure에도 accessor나 mutator를 강제적으로 요구하는 framework나 standard가 있는데, 다음 문제를 야기할 수 있다

#### Hybrids
- 이런 혼란이 가끔은 반은 object, 나머지 반은 data structure로 이루어진 hybrid structure를 만들어낸다
  public variable들과 많은 일을 하는 function들이 있고, 마치 절차지향적 프로그램에서 data structure를 사용하듯이 private variable들을 public으로 노출하는 getter/setter가 있어서 다른 function들이 그 variable들을 사용하게끔 유혹하기도 한다
- 이런 hybrids는 새로운 function 혹은 새로운 data structure마저 추가하기 어렵기 때문에 사용하는 것을 절대 피하도록 하자

#### Hiding Structure
- 만약 ctxt, options, 그리고 scratch이 behavior을 가진 오브젝트라면 어떨까? Object는 internal structure를 숨겨야 하기 때문에 위의 코드처럼 엮는 코드는 안된다. 그렇다면 어떻게 scratch directory의 absolute path를 가져올 수 있을까?
```java
ctxt.getAbsolutePathOfScratchDirectoryOption(); // 이 메소드는 ctxt 클래스가 많은 메소드들로 가득 차게 할 가능성이 농후하다
// or
ctxt.getScratchDirectoryOption().getAbsolutePath(); // getScratchDirectoryOption이 data structure를 반환하므로 안된다
```

- 만약 ctxt가 object라면 우리는 그 안을 보지 않고, 대신 무언가를 하라고 해야 한다. ctxt가 하려는 게 무엇일까?
```java
String outFile = outputDir + "/" + className.replace('.', '/') + ".class";
FileOutputStream fOut = new FileOUtputStream(outFile);
BufferedOutputStream bos = new BufferedOutputStream(fOut);
```
- 코드의 다른 문제점들이 많지만 지금은 무시하고, ctxt가 하는 일이 무엇인지만 보면 scratch file을 그 absolute path에 만들려고 한다는 것을 알 수 있다
  그렇다면 ctxt에게 이렇게 하라고 하는 건 어떨까?
```java
BufferedOutputStream bos = ctxt.createScratchFileStream(classFileName);
```
- ctxt의 internal structure를 숨기면서도 Law of Demeter를 위반 하지도 않을 수 있다

#### Data Transfer Objects
가장 완벽한 형태의 data structure는 function 하나 없이 public variable만을 가진 클래스이다.
이 형태는 DTO(Data Transfer Object)라고 불리는데 데이터베이스와 소통할 때 혹은 socket으로부터 받은 메세지를 parsing할 때 굉장히 유용하다. 종종 데이터베이스의 raw data를 프로그램안의 object로 변환할 때, 첫번째 과정으로 사용되기도 한다.

더 종종 사용되는 형태는 `bean`이다(아래 코드 참고). Bean은 getter/setter로 만들어지는 private variable을 가지고 있다. Bean의 encapsulation처럼 보이는 것은 Objected oriented 신봉자들을 기분 좋게 할 지는 모르지만 거의 다른 이점은 가지고 있지 않다.

```java
public class Address {

  private String street; 
  private String streetExtra; 
  private String city; 
  private String state; 
  private String zip;

  public Address(String street, String streetExtra, String city, String state, String zip) {
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
Active Record는 DTO의 특별한 형태이다. Public variable을 가지고 있는 data structure이며, save나 find같은 데이터베이스에 corresponding하는 메소드들을 가지고 있다. 보통 Object-Relational Mapping(ORM)에서 보편적으로 쓰인다.   
안타깝게도 종종 비지니스 로직을 관리하는 메소드들을 active record에 넣는 개발자들이 있는데 이건 data structure와 object를 섞은 hybrid를 만들기 때문에 좋지 않다. 좋은 해결 방법은, 당연히, active record는 data structure로 치부하고, 비지니스 로직을 담은 메소드는 새로운 오브젝트를 만들어 그 안에서 사용하는 것이다.

#### Conclusion
- Object는 현재 behavior를 변경하지 않고 새로운 object를 추가하기 쉽지만, 새로운 behavior를 추가하기 쉽지 않다
- Data structure는 새로운 behavior를 추가하기 쉽지만, 새로운 data structure를 추가하기 쉽지 않다
> 주어진 조건에 따라 어떤 문제를 해결해야 하는 지 이해하고 어떤 접근방법을 취해야 할 지 확실히 구분할 수 있는 좋은 프로그래머가 되자
