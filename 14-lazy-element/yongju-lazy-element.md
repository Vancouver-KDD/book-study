# Lazy Element

##### Inline Function (inverse of Extract Function)
```java
  // Before
  public void add(String ingredientName) {
    Ingredient ingredient = IngredientFactory.createIngredient(ingredientName);
    inventoryService.addStock(ingredientName, 0);
  }

  public Long getId(Ingredient ingredient) {
    Ingredient ingredientSaved = ingredientRepository.save(ingredient);
    return ingredientSaved.getId();
  }

  public Long addIngredient() {
    add(ingredient1);
    return getId();
  }

  // After
  public Long add(String ingredientName) {
    Ingredient ingredient = IngredientFactory.createIngredient(ingredientName);
    Ingredient ingredientSaved = ingredientRepository.save(ingredient);
    inventoryService.addStock(ingredientName, 0);

    return ingredientSaved.getId();
  }
```

##### Collapse Hierarchy
- Parent와  child class가 필요에 의해 굳이 나눠져 있는 게 아니라면 합쳐라
```java
  // before
  public class Parent { ... }
  public class ChildButNotWorth { ... }

  // after
  public class Parent { ... }
  // or
  public class NewName { ... }
```