# [Subin] Abstract Factory

사용자 인터페이스를 업데이트 함으로써 사용자에게 새로운 인상을 주고, 다양한 행동을 이끌어낼 수 있다. 사용자 인터페이스를 구성하는 데에는 다양한 컴포넌트가 필요하다. 스크롤 바, 윈도우, 버튼 등의 "widget"이 그 예이다. 

이러한 위젯들을 쉽고 빠르게 업데이트하기 위해서는 현명한 디자인 패턴이 필요하다. 바로 이때 우리는 abstract WidgetFactory 클래스를 만들어 위와 같은 문제를 해결할 수 있다.

## What is Abstract Factory?
Abstract Factory 디자인 패턴은 Creational Pattern의 하나이다. Factory Pattern 과 거의 유사하지만, abstract한 레이어가 추가된 모습이다. Abstract Factory pattern은 super-factory와 같은 역할을 하여 다른 factory들을 생성해낸다. 

Abstract factory pattern은 우리가 객체를 만들 때 공통된 패턴을 따를 수 있도록 도와주는 framework를 제공해준다. 그래서 런타임에 abstract factory는 그 아래의 concrete factory와 coupled 되어 있고, 그 concrete factory들은 원하는 타입의 객체를 생성해낼 수 있다.

### Abstract Factory pattern은 다음과 같은 경우에 사용된다:
- 제품이 생성되거나 구성, 표현되는 방식에서 독립된 시스템을 만들고자 할 때
- 여러 제품군 중 하나의 제품을 선택해 시스템을 설정해야 할 때
- 관련된 제품 객체들이 함께 사용될 수 있도록 설계되어야 하며, 이러한 제약이 계속 지켜질 수 있도록 디자인하고자 할 때
- 제품에 대한 클래스 라이브러리를 제공하고, 그들의 구현이 아닌 인터페이스만 공개하고자 할 때

### Abstract Factory pattern의 구성
- AbstractFactory
  - abstract 제품 객체를 생성하는 operation에 대한 인터페이스를 선언
- ConcreteFactory
  - concrete 제품 객체를 생성하는 operation을 구현
- AbstractProduct
  - 제품 객체의 타입에 대한 인터페이스 선언
- ConcreteProduct
  - 상응하는 Concrete factory가 생성한 제품 객체를 정의
  - AbstractProduct 인터페이스를 구현
- Client
  - AbstractFactory와 AbstractProduct 클래스에서 선언한 인터페이스에만 접근할 수 있음

### Collaboration
- 일반적으로 ConcreteFactory의 인스턴스 한 개가 런타임에 만들어진다. 그럼 Concrete Factory가 특정 구현울 갖는 제품 객체를 생성한다. 서로 다른 제품 객체를 생성하기 위해, client는 다른 concrete factory를 사용해야 한다.
- AbstractFactory는 제품 객체 생성을 ConcreteFactory subclass에 위임한다.

## How to implement it?
> GeeksForGeeks 예시 참고
글로벌 차 공장을 가지고 있다고 생각해보자. 일반 factory design pattern 이었다면, 한 지점의 공장에만 사용될 수 있을 것이다. 하지만 Abstract Factory 패턴을 사용한다면, 하나 이상의 로케이션을 가질 수 있고, 일반 factory design pattern에 변화를 주어야 한다. 우리는 이제 아래와 같은 공장 지점들이 필요하다:
- IndiaCarFactory, USACarFactory, 그리고 DefaultCarFactory

우리의 application은 공장이 위치한 지점과, 공장이 독립적으로 적절히 가동될 수 있도록  확인할 수 있어야 한다. 그러려면 유저가 알아차리지 않도록, 공장 지점을 파악하고 공장이 적절히 구현되고 있는지 확인하기 위해 abstraction 레이어가 필요하다. 

~~~
// Abstract Factory Design Pattern in Java

enum CarType
{
    MICRO, MINI, LUXURY
}

abstract class Car
{
    Car(CarType model, Location location) {
        this.model = model;
        this.location = location;
    }

    abstract void construct();

    CarType model = null;
    Location location = null;

    CarType getModel() {
        return model;
    }

    void SetModel(CarType model) {
        this.model = model;
    }

    Location getLocation() {
        return location;
    }

    void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "CarModel - " + model + " located in " + location;
    }
}

class LuxuryCar extends Car {
    LuxuryCar(Location location) {
        super(CarType.LUXURY, location);
        construct();
    }
    @Override
    protected void construct() {
        System.out.println("Connecting to luxury car");
    }
}

class MicroCar extends Car {
    MicroCar(Location location) {
        super(CarType.MICRO, location);
        construct();
    }

    @Override
    protected void construct() {
        System.out.println("Connecting to Micro Car");
    }
}

class MiniCar extends Car {
    MiniCar(Location location) {
        super(CarType.MINI, location);
        construct();
    }

    @Override
    protected void construct() {
        System.out.println("Connecting to Mini Car");
    }
}

enum Location {
    DEFAULT, USA, INDIA
}

class INDIACarFactory {
    static Car buildCar(CarType model) {
        Car car = null;
        switch(model) {
            case MICRO:
                car = new MicroCar(Location.INDIA);
                break;
            
            case MINI:
                car = new MiniCar(Location.INDIA);
                break;
            
            case LUXURY:
                car = new LuxuryCar(Location.INDIA);
                break;

            defualt:
                break;
        }
        return car;
    }
}

class DefaultCarFactory
{
    public static Car buildCar(CarType model)
    {
        Car car = null;
        switch (model)
        {
            case MICRO:
                car = new MicroCar(Location.DEFAULT);
                break;
             
            case MINI:
                car = new MiniCar(Location.DEFAULT);
                break;
                 
            case LUXURY:
                car = new LuxuryCar(Location.DEFAULT);
                break;
                 
                default:
                break;
             
        }
        return car;
    }
}
 
 
class USACarFactory
{
    public static Car buildCar(CarType model)
    {
        Car car = null;
        switch (model)
        {
            case MICRO:
                car = new MicroCar(Location.USA);
                break;
             
            case MINI:
                car = new MiniCar(Location.USA);
                break;
                 
            case LUXURY:
                car = new LuxuryCar(Location.USA);
                break;
                 
                default:
                break;
             
        }
        return car;
    }
}

class CarFactory {
    private CarFactory() {

    }

    public static Car buildCar(CarType type) {
        Car car = null;
        Location location = Location.INDIA;

        switch(location) {
            case USA:
                car = USACarFactory.buildCar(type);
                break;
            
            case INDIA:
                car = INDIACarFactory.buildCar(type);
                break;

            default:
                car = DefaultCarFactory.buildCar(type);
        }
        return car;
    }
}

class AbstractDesign {
    public static void main(String[] args) {
        System.out.println(CarFactory.buildCar(CarType.MICRO));
        System.out.println(CarFactory.buildCar(CarType.MINI));
        System.out.println(CarFactory.buildCar(CarType.LUXURY));
    }
}
~~~

Output:
~~~
Connecting to Micro Car 
CarModel - MICRO located in INDIA
Connecting to Mini car
CarModel - MINI located in INDIA
Connecting to luxury car
CarModel - LUXURY located in INDIA
~~~

## Advantage
사용자가 생성하고자 하는 객체의 타입을 잘 모를 때 유용하게 쓰일 수 있다.

- **Concrete 클래스들의 독립성**: Abstract Factory 패턴을 통해서, 어플리케이션이 생성하는 객체들의 클래스를 컨트롤할 수 있다. Factory가 객체를 생성하는 책임과 과정을 캡슐화했기 때문에, 클라이언트들은 오히려 클래스의 구현에서 구분되어 있을 수 있다. 클라이언는 abstract interface를 통해 그들의 인스턴스를 조절한다. 
- **Product Family를 쉽게 바꿀 수 있다**: Concrete Factory 클래스는 어플리케이션에서 처음 생성될 때 딱 한번 등장한다. 그래서 어플리케이션이 사용하는 concrete factory를 쉽게 바꿀 수 있다. 또한 concrete factory를 바꿈으로써 간단하게 다양한 제품 설정을 사용할 수 있다. 
- **제품 객체들의 일관성을 높여준다**: 제품 객체들이 함께 작동할 수 있도록 디자인 할 때, 어플리케이션이 오직 하나의 제품군에서만 객체를 가져와 사용하도록 하는 것이 중요하다. Abstract Factory 패턴이 바로 이러한 과정을 쉽게 만들어준다. 

## Disadvantage
- **새로운 제품 종류를 받아들이기 어렵다**: 새로운 타입의 제품을 생성하기 위해 Abstract Factory를 확장하는 것은 쉽지 않다. 새로운 제품군을 만들려면 factory interface를 확장해야 하는데, 이는 AbstractFactory 클래스와 그 아래 모든 subclass들을 바꾸는 작업이 포함될 수밖에 없다. 

## Sources
- Erich Gamma, John Vlissides, Ralph Johnson, Richard Helm, *Design Patterns: Elements of Reusable Object-Oriented Software*, Addison-Wesley, 1994, pp.87-96.
- https://www.geeksforgeeks.org/abstract-factory-pattern/
- '[디자인 패턴] 추상 팩토리 패턴 (Abstract Factory Pattern)', https://devowen.com/326
