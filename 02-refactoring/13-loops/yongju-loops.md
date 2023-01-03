# Loops

First-class function을 사용해라

##### Replace loop with pipeline

```java
  ...
  List<Drink> drinks;
  ...
  
  //before
  public void updateCoffeeBeanToKenya() {
    (for int i = 0; i < 10; ++i) {
      Drinks drink = drinks.get(i);
      if (drink.kind == "Coffee") {
          drink.setBean("Kenya");
      }
    }
  }

  //after
  public void updateCoffeeBeanToKenya() {
    drinks.filter(d -> d.kind == "Coffee")
          .forEach(d -> d.setBean("Kenya"))
  }
```