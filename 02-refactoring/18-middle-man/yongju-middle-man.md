# Middle Man

`inverse of Hide Delegate`

##### Remove Middle Man

Hide Delegate를 통해 계속 delegate method를 만들다 보면 결국 해당 오브젝트는 middle man역할을 하게 된다. 그리고 아마도 그 때가 delegate method 대신 직접 method call을 해야할 때일 수 있다.

```java
  // Before
  public class Client {
    ...
    manager = aPerson.getManager();
    ...
  }

  public class Person {
    ...
    private Department department;
    ...
    public void getManager() {
      return this.department.getManager();
    }
    ...
  }

  public class Department {
    ...
    private Manager manager;
    ...
    public void getManager() {
      return this.manager;
    }
  }

  /*
    After
    If we have too many delegate methods like above, it's time to remove the middle man
  */
  public class Person  {
    ...
    private Department department;
    ...
    public void getDepartment() {
      return this.department;
    }
    ...
  }

  public class Client {
    ...
    manager = aPerson.getDepartment().getManager();
    ...
  }
```

##### Replace Superclass with Delegate

Inheritance를 잘 못 사용하는 경우
- Superclass의 interface가 subclass에서 호환되지 않는 경우
- Subclass가 superclass와의 관계가 child-parent가 아닌 경우

Inheritance의 단점
- Superclass의 interface를 모든 subclass가 받아야 한다

이런 inheritance의 단점들을 보완해주는 방법으로 우리는 Superclass를 delegate로 변경할 수 있다. 두 클래스를 나눠줌으로써 coupling을 줄여서 두 가지의 separate classes로 남겨두는 것이다. 하지만 inheritance 자체는 간단하고 효과적인 메커니즘이기 때문에 저자는 inheritance를 먼저 적용하고 문제가 생길 시 본 리팩토링 방법을 사용할 것을 권장한다. (`Favor object composition over class inheritance 오해`)


```java
  // Before (inheritance)
  class List {
    ...
    private boolean isEmpty;

    public boolean isEmpty() {
      return this.isEmpty()
    }
  }

  class Stack extends List {
    ...
  }

  // After (delegation)
  class List {
    public boolean isEmpty() {
      return this.isEmpty();
    }
  }
  
  class Stack {
    private List list;
    public Stack() {
      list = new List();
    }

    public boolean isEmpty() {
      return this.list.isEmpty();
    }
  }
```


##### Replace Subclass with Delegate

Replace Superclass with Delegate와 리팩토링 자체와 동기는 비슷하다. 

```java
  // Before (inheritance)
  class Order {
    ...
    public int daysToShip() {
      return this.warehouse.getDaysToShip();
    }
    ...
  }

  class PriorityOrder extends Order {
    public int daysToShip() {
      return this.priorityPlan.getDaysToShip();
    }
  }

  // After (delegation)
  class Order {
    public int daysToShip() {
      return this.priorityDelegate 
        ? this.priorityPlan.getDaysToShip()
        : this.warehouse.getDaysToShip();
    }
  }

  class PriorityOrderDelegate {
    public int daysToShip() {
      return this.priorityPlan.getDaysToShip();
    }
  }
```







































