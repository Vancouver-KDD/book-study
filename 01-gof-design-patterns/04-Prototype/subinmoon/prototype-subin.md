# [Subin] Prototype

> 문제 제기

이미 존재하는 객체를 복사하고 싶다면 어떻게 해야 할까? 먼저 같은 클래스 안에서 새로운 객체를 생성해야 할 것이다. 그리고 해당 객체가 가지고 있는 속성들을 그대로 새로 만든 객체에 세팅하면 된다. 하지만 모든 객체가 이런 방식으로 복사될 수 있는 것은 아니다. private한 field가 있을 수도 있고, 객체 바깥에서는 보이지 않는 field가 있을 수도 있기 때문에, 모든 속성을 복사해오지 못하는 경우가 생길 수 있다. 

위와 같이 직접적으로 객체를 복사할 때는 또 다른 문제가 생길 수 있다. 바로 객체가 속한 클래스에 의존성이 더 높아질 수 있다는 점이다. 

> 해결책

## What is Prototype?
Prototype은 Creational 디자인 패턴 중 하나이다. Prototype은 새로운 인스턴스를 생성하는 것의 복잡성을 클라이언트로부터 숨기는 역할을 한다. 이미 존재하는 객체를 복사한다는 개념은 새로운 인스턴스를 새로 만드는 것보다 더 많은 작업이 필요한 일일 수 있다. 

객체를 복사하여 새로 생성된 객체는 필요에 따라 기존 객체가 가지고 있던 속성을 일부 변경할 수도 있다. 완전히 새로운 객체를 만들어 속성을 추가하는 것보다, 위와 같이 복사하여 일부 속성만 변경하는 것이 오히려 더 효과적일 수 있다. 이떄 우리는 `clone()` 메소드를 활용할 것이다. `clone()`은 prototype 패턴을 만드는 데 가장 간단한 방법이다. 

### Prototype은 다음과 같은 경우에 사용된다:
- 시스템이 Product가 생성되고, 구성되고, 표현되는 과정에서 독립적이어야 할 때
- 클래스들의 instantiation이 run-time에서 구체화될 때
  - 예를 들어, 

### Prototype의 구성
- Prototype
  - 기존 객체의 prototype, 샘플
- Prototype registry
  - 간단한 string parameter를 이용해 모든 prototype들에 접근할 수 있도록 하는 등록 서비스 역할을 한다
- Client
  - 클라이언트들은 registry 서비스를 통해 prototype 인스턴스들에 접근할 수 있다

## How to implement it?
~~~
import java.util.HashMap;
import java.util.Map;

abstract class Color implements Clonable {
  protected String colorName;
  abstract void addColor();
  public Object clone() {
    Object clone = null;
    try {
      clone = super.clone();
    }
    catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return clone;
  }
}

class blueColor extends Color {
  public blueColor() {
    this.colorName = "blue";
  }
  
  @Override
  void addColor() {
    System.out.println("Blue color added");
  }
}

class blackColor extends Color {
  public blackColor() {
    this.colorName = "black";
  }
  
  @Override
  void addColor() {
    System.out.println("Black color added");
  }
}

class ColorStore {
  private static Map<String, Color> colorMap = new HashMap<String, Color>();
  
  static {
    colorMap.put("blue", new blueColor());
    colorMap.put("black", new blackColor());
  }
  
  public static Color getColor(String colorName) {
    return (Color) colorMap.get(colorName).clone();
  }
}

class Prototype {
  public static void main(String[] args) {
    ColorStore.getColor("blue").addColor();
    ColorStore.getColor("black").addColor();
    ColorStore.getColor("black").addColor();
    ColorStore.getColor("blue").addColor();
  }
}
~~~

## Advantage
- Product를 run-time에 추가 / 제거할 수 있다.
- value를 다양화 하여 새로운 객체를 구체화할 수 있다. 새로운 클래스를 정의하는 것보다 더 간단하게 다양한 객체를 만들어낼 수 있다.
- structure를 다양화하여 새로운 객체를 구체화할 수 있다.
- 하위 클래스를 만드는 작업을 생략할 수 있다.

## Disadvantage
- 적은 수의 객체만 쓰는 프로젝트에는 불필요
- Concrete product class를 클라이언트로부터 숨길 수 있다.
- Prototype의 모든 하위 클래스들은 `clone()` 메소드를 반드시 구현해야 한다. 하지만 복사를 지원하지 않거나 circular reference를 가지고 있는 객체라면 단순하게 `clone()`하는 게 어려울 수 있다.

## Sources
- Erich Gamma, John Vlissides, Ralph Johnson, Richard Helm, *Design Patterns: Elements of Reusable Object-Oriented Software*, Addison-Wesley, 1994, pp.117-126.
- https://refactoring.guru/design-patterns/prototype
- https://velog.io/@newtownboy/%EB%94%94%EC%9E%90%EC%9D%B8%ED%8C%A8%ED%84%B4-%ED%94%84%EB%A1%9C%ED%86%A0%ED%83%80%EC%9E%85%ED%8C%A8%ED%84%B4Prototype-Pattern