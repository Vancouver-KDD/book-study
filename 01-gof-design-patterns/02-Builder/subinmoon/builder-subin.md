# [Subin] Builder

> 문제 제기

예를 들어, `House`라는 객체를 만든다고 생각해보자. 
기본적인 House를 만들기 위해, 4개의 벽, 바닥, 현관문, 창문, 지붕 등이 필요하다. 그런데 만약 더 큰 집이나, 창이 더 많은 집, 뒷마당 있는 집, 혹은 난방 시스템, 배관과 같은 업데이트가 필요한 집이라면 어떨까?

첫째, 가장 간단한 해결책으로는, `House` 클래스를 extend 해서, 여러 개의 subclass를 만들 수 있을 것이다. 위에서 나열한 다양한 타입의 집을 만들기 위해 다른 개수, 다른 타입의 parameter를 가진 constructor들을 만들어야 한다. 이렇게 늘려가다 보면, 결국엔 수많은 ubclass들에 둘러싸이게 될 것이다. 

그렇다면 몇 십개의 subclass들을 만드는 대신, 베이스 클래스인 `House` 내부에 거대한 constructor를 만드는 것은 어떨까? 이 constructor 안에 우리가 필요한 모든 parameter들을 다 넣어서 다양한 형태의 house 객체를 만들 수 있도록 한다면?

하지만 이 방법 또한 문제점이 있다. 쓰이지 않는 parameter들을 관리하기 어렵다는 점이다. 예를 들어, 우리가 필요한 집은 수영장이 있는 집이라면, hasGarage, hasStatues, hasGarden 과 같은 parameter들은 null 값으로 줄줄이 채워넣어야 한다. 

> 해결책

그렇다면 우리가 객체를 생성할 때 쓰는 construction 코드를 해당 클래스에서 빼내보자. 그리고 그 코드를 builder라고 하는 다른 독립적인 객체에 옮겨주는 것이다. 

e.g. HouseBuilder 클래스 > buildWalls(), buildDoors(), buildWindows(), buildRoof(), buildGarage(), getResult(): House

## What is Builder?
- Creational design pattern 중 하나
- 복잡한 객체를 step by step으로 생성하는 것을 도와줌
  - Builder 안의 모든 메소드를 불러오지 않아도 괜찮다. 개발자가 특정 객체를 만들기 위해 필요한 step만 설정하여 객체를 만들면 된다.
- 똑같은 construction code를 사용해서 다른 타입과 표현의 객체를 만들 수 있음
- 여러 개의 Builder를 만들어, 다양하게 표현(represent)되는 객체를 만들 수 있음
  - e.g. 4개 벽 + 1개 문 + 2개 창 -> 벽돌 집, 나무 집, ...

### Builder pattern의 구성
- Builder
  - Product 객체를 만들기 위해 필요한 파트를 생성하는 abstract interface
- ConcreteBuilder
  - 객체 생성 스텝의 다양한 구현 방법을 제공 
  - 기본 interface를 따라가지 않는 형식의 객체를 생성할 수 있음
- Director
  - 꼭 필요한 클래스는 아니지만, 파트들을 모아 조립하는 생성 루틴을 Director 클래스에 저장해두면 이후에 다시 사용할 수도 있음
  - 클라이언트 코드에서는 제품 생성 디테일에 관해 보이지 않도록 설정할 수 있음
    클라이언트 -> Director -> get the result from the Builder
  - Builder interface를 사용해 객체를 생성
- Product
  - 결과적으로 생성된 객체
  - 서로 다른 Builder들에 의해 생성된 객체는 똑같은 클래스 hierarchy나 interface에 속해있지 않아도 됨
  - 생성되는 복잡한 객체를 의미. ConcreteBuilder를 통해 객체를 만들고, 각 파트가 어떻게 조립되어야 하는지 그 과정을 정의

## How to implement it?
`House`는 공사 과정의 결과로, 최종적으로 만들어지는 객체. 공사 과정 중에는, 지하실 공사, 벽 공사, 지붕 공사 등 다양한 스텝이 있다. Builder 패턴을 사용하면, 똑같은 절차이지만, 다른 속성을 가진 집들을 만들어낼 수 있다.

~~~
interface HousePlan {
    public void setBasement(String basement);
    public void setSturcture(String structure);
    public void setRoof(String roof);
    public void setInerior(String interior);
}

class House implements HousePlan {
    private String basement;
    private String structure;
    private String roof;
    private String interior;

    public void setBasement(String basement) {
        this.basement = basement;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public void setRoof(String roof) {
        this.roof = roof;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }
}

interface HouseBuilder {
    public void buildBasement();
    public void buildStructure();
    public void buildRoof();
    public void buildInterior();
    public House getHouse();
}

class IglooHouseBuilder implements HouseBuilder {
    private House house;

    public IglooHouseBuilder() {
        this.house = new House();
    }

    public void buildBasement() {
        house.setBasement("Ice Bars");
    }

    public void buildStructure() {
        house.setStructure("Ice Blocks");
    }

    public void buildInterior() {
        house.setInterior("Ice Carvings");
    }

    public void buildRoof() {
        house.setRoof("Ice Dome");
    }

    public House getHouse() {
        return this.house;
    }
}

class TipiHouseBuilder implements HouseBuilder
{
    private House house;
 
    public TipiHouseBuilder()
    {
        this.house = new House();
    }
 
    public void buildBasement()
    {
        house.setBasement("Wooden Poles");
    }
 
    public void buildStructure()
    {
        house.setStructure("Wood and Ice");
    }
 
    public void buildInterior()
    {
        house.setInterior("Fire Wood");
    }
 
    public void buildRoof()
    {
        house.setRoof("Wood, caribou and seal skins");
    }
 
    public House getHouse()
    {
        return this.house;
    }
}

# Director Class
class CivilEngineer {
    private HouseBuilder houseBuilder;

    public CivilEngineer(HouseBuilder houseBuilder) {
        this.houseBuilder = houseBuilder;
    }

    public House getHouse() {
        return this.houseBuilder.getHouse();
    }

    public void constructHouse() {
        this.houseBuilder.buildBasement();
        this.houseBuilder.buildStructure();
        this.houseBuilder.buildRoof();
        this.houseBuilder.buildInterior();
    }
}

class Builder {
    public static void main(String[] args) {
        HouseBuilder iglooBuilder = new IglooHouseBuilder();
        CivilEngineer engineer = new CivilEngineer(iglooBuilder);
        
        engineer.constructHouse();
        House house = engineer.getHouse();
        System.out.println("Builder constructd: " + house);
    }
}
~~~

## Advantage
- 복잡한 객체를 step-by-step으로 생성할 수 있음
- 생성 과정을 조정하거나 하나의 스텝을 반복해서 진행할 수도 잇음
- construction code를 이후에 재활용할 수 있음
- Single Responsibility Principle: 복잡한 construction code를 따로 분리해서 관리할 수 있음 

## Disadvantage
- 여러 개의 새로운 클래스를 생성해야 하기 때문에, 전반적인 코드 복잡도는 상승

## Sources
- Erich Gamma, John Vlissides, Ralph Johnson, Richard Helm, *Design Patterns: Elements of Reusable Object-Oriented Software*, Addison-Wesley, 1994, pp.97-106.
- https://refactoring.guru/design-patterns/builder
- https://www.geeksforgeeks.org/builder-design-pattern/