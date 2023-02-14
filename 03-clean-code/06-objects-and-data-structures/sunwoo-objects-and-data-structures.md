# Objects and Data Structures

## Data Abstraction
Implementation을 client 코드에 숨긴다는 것은 단지 variable과 client사이에 function을 집어 넣는 것이 아니라,
implementation을 추상화하는 것이다.
abratct한 interface를 제공함으로써, 그 interface를 통해 유저가 data의 중요한 코어 부분을 manipulate할 수 있게 한다.

```Java
public interface Vehicle {
    double getFuelTankCapacityGallons();
    double getGallonsOfGasoline();
}
```
vs

```Java
public interface Vehicle {
    double getPercentFuelRemaining();
}
```
아래의 예시는 data에 관한 정보를 abstraction하고 정말 유저가 필요한 정보를 제공해준다.
반면 위의 예시는 Fuel에 관한 데이터가 gallon이라는 것을 유추 할 수가 있다.

## Data/Object Anti-Symmetry
* Object: data를 숨기고, 그 data를 operate 할 수 있는 function을 제공한다.
* Data: data를 노출시키고, 그 data관한 function을 제공하지 않는다.

### Procedural Approach
Data(Shape)는 어떠한 기능도 하지 않고, 단지 data를 저장한다.
모든 로직은 `Geometry` 클래스에서 이루어진다.

장점: 새로운 기능에 대해서, `Geometry` class만 수정이 이루어진다.
단점: 새로운 데이터에 대해서, `Geometry` class도 수정이 필요하다
```Java
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
     public double area(Object shape) throws NoSuchShapeException {
         if (shape instanceof Square) {
             Square s = (Square)shape;
             return s.side x s.side;
         }else if (shape instanceof Rectangle) {
             Rectangle r = (Rectangle)shape;
             return r.height x r.width;
         }else if (shape instanceof Circle) {
             Circle c = (Circle)shape;
             return PI x c.radius x c.radius; }
     throw new NoSuchShapeException(); 
     }
}
```

### Object-Oriented Approach
`Geometry` class가 없이 shape(data)의 안에서 로직이 이루어진다.
function들은 polymorphic이다.

장점: 새로운 data를 추가할때, 다른 기존의 코드에 영향을 주지 않는다.
단점: 새로운 기능(function)을 추가 할 때, 모든 shape(data) class들의 수정이 필요하다.

```Java
public class Square implements Shape {
    private Point topLeft;
        private double side;
        public double area() {
            return side x side;
        }
    }

    public class Rectangle implements Shape {
        private Point topLeft;
        private double height;
        private double width;
        public double area() {
            return height x width;
        }
    }

    public class Circle implements Shape {
        private Point center;
        private double radius;
        public final double PI = 3.141592653589793;
        public double area() {
            return PI x radius x radius;
        }
    }
```

## The Law of Demeter
> A module should not know about the innards of the objects it manipulates.
>
> Law of Demeter
>

class C안에 있는 method f는 다음과 같은 오브젝트들만의 method를 호출 할 수 있다.
* C 안의 methods
* f에 의해 생성된 object
* f의 parameter로 넘어온 object
* C가 저장하고 있는 object instance들

밑의 예시는 `getOptions`가 리턴하는 object가 `getScratchDir`라는 method를 포함하고 있는것을 알고 있고,
또 `getScratchDir`가 리턴하는 object가 `getAbsolutePath`라는 method를 가지고 있는 것을 알아야 한다.

```Java
final String outputDir = ctxt.getOptions().getScratchDir().getAbsolutePath();
```

## Train Wrecks
위의 코드같이 chaining으로 오브젝트를 가져오는 것을 Train Wreck이라고 한다.
위의 코드는 아래와 같이 split up 할 수 있다.

```Java
Options opts = ctxt.getOptions();
File scratchDir = opts.getScratchDir();
final String outputDir = scratchDir.getAbsolutePath();
```
하지만 이 코드 또한 한 method가 `ctxt`,`Options`, `File`의 오브젝트에 대한 이해가 필요하고, 이것은 위의 Law of Demeter를 위반하게 된다.
만약 `ctxt`,`Options`, `File`가 오브젝트가 아닌 data structure이라면 내부 구조에 대한 access가 자유롭고, 따라서 Law of Demeter를 위반하지 않게 된다.

```Java
final String outputDir = ctxt.options.scratchDir.absolutePath;
```

## Hybrids
data structure와 object는 위에처럼 서로의 장단점이 있고, 상황에 맞게 사용되어야 한다.
하지만 두가지 특징을 한 코드내에서 사용하는 것은 두 개의 장점을 버리고, 단점만 취합하는 것과 같다.

## Hiding Structure
위의 `final String outputDir = ctxt.getOptions().getScratchDir().getAbsolutePath();` 예제를 다시 한번 가져와보자.
만약 law of demeter를 violation하고 싶지 않고, 오브젝트를 사용하고 싶다면 다음과 같은 method를 `ctxt`에 추가 해보는 것은 어떨까?

```Java
ctxt.getAbsolutePathOfScratchDirectoryOption();
```
모든 composition된 오브젝트들에 이런 식으로 method를 작성 할 경우 `ctxt`는 많은 양의 메소드를 요구하게 된다.

우리는 object에게 그 object가 어떤 data(absolute path)를 가지고 있는지 물어보는 것이 아니라, 그 data(absolute path)를 가지고 어떤 것을 할 것인지를 알려줘야 한다.

```Java
BufferedOutputStream bos = ctxt.createScratchFileStream(classFileName);
```
이렇게 `ctxt` 오브젝트에게 어떤 일을 할 지를 지정해 줌으로서, object안에 있는 internal data를 숨기고, law of demeter를 위반하지 않을수 있다.

## Data Transfor Object
Data structure의 정수는 다른 기능을 제공하지 않고, 단지 데이터를 담고만 있다는 것에 있다.
이 것들은 주로 database communication이나 socket으로 부터 받아온 데이터를 parsing 할때 진가를 발휘한다.
OOP적인 관점에서 봤을때, getter나 setter등을 사용하는 것은 맞는 방법이지만, 하지만 이 data structure안의 getter나 setter들은 어떠한 이점도 개발자에게 주지 않는다.

## Active Record
Active Record: the interface between the databse and application.
Active Record는 다른 database table이나 다른 data source와의 인터페이스 역활을 한다.
우리는 active record를 data structure로 취급하고, business logic은 다른 object내에서 처리하는 것을 목표로 해야한다.

## Conclusion
* Object: 기존의 코드 변경 없이 새로운 종류의 data structure를 추가하기 쉬움.
* Data Structure: 기존의 코드 변경 없이 새로운 종류의 기능을 추가하기 쉬움.

