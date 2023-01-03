# Factory Method

- Client
```java
public class Client {
  public static void main(String[] args) {
      PizzaFactory pizzaFactory = new PizzaFactory();
      Pizza pizza = pizzaFactory.orderPizza(PizzaKinds.POTATO);

      System.out.println(pizza);
  }
}
```

- PizzaFactory
```java
public class PizzaFactory {

  public Pizza orderPizza(PizzaKinds pizzaKinds) {
      switch (pizzaKinds) {
          case POTATO:
              return new PotatoPizza();
          case PINEAPPLE:
              return new PineapplePizza();
          default:
              return new NoToppingPizza();
      }
  }
}
```

- Pizza
```java
public abstract class Pizza {

  abstract void putToppings();
  abstract void putSauce();
}
```

- NoToppingPizza
```java
public class NoToppingPizza extends Pizza {

  @Override
  void putToppings() {
      System.out.println("No Toppings");
  }

  @Override
  void putSauce() {
      System.out.println("No sauce");
  }

  @Override
  public String toString() {
      return "Don't order this please";
  }
}
```

- PineapplePizza
```java
public class PineapplePizza extends Pizza {

  @Override
  void putToppings() {
      System.out.println("Toppings: pineapple, pineapple, and pineapple again");
  }

  @Override
  void putSauce() {
      System.out.println("No sauce on pineapple pizza!");
  }

  @Override
  public String toString() {
      return "This is the greatest pineapple pizza in the world, not for Gorden Ramsey!";
  }
}
```

- PotatoPizza
```java
public class PotatoPizza extends Pizza {

  @Override
  void putToppings() {
      System.out.println("Toppings: Potato, cheese, and potato again!");
  }

  @Override
  void putSauce() {
      System.out.println("Sauce: Tomato sauce");
  }

  @Override
  public String toString() {
      return "This is the greatest potato pizza in the world!";
  }
}
```

- PizzaKinds
```java
public enum PizzaKinds {

  POTATO,
  PINEAPPLE
}
```
