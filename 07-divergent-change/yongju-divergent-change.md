# Divergent Change

우리는 코드를 변경할 떄 명확한 한 곳에서만 변경하길 원한다. 만약 한 곳이 아니라 여러 곳에서 변경해야 한다면 잘못된 구조로 이루어져 있을 가능성이 있다

같은 맥락으로 한 module(class)이 다른 이유로, 다른 방법으로 자주 변경된다면 Divergent change를 의심해보아야 한다

### Solutions
##### Split Phase

다른 behaviour를 가진 코드를 각기 다른 module로 분리한다

```java
// before
public Long order(Long managerId, Map<String, Integer> ingredients) {
  Optional<Person> manager = personRepository.findById(managerId);
  if (!manager.isPresent())
      throw new IllegalArgumentException("No such person!");

  List<OrderIngredient> orderIngredients = ingredients.keySet()
          .stream()
          .map(ingredientName -> {
              int quantity = ingredients.get(ingredientName);
              // Create ingredient if it is a new ingredient
              Ingredient ingredient = ingredientRepository.findByName(ingredientName) == null
                      ? IngredientFactory.createIngredient(ingredientName)
                      : ingredientRepository.findByName(ingredientName);

              inventoryService.addStock(ingredientName, quantity);
              return OrderIngredient.createOrderIngredient
                      (ingredient, quantity);
          })
          .collect(Collectors.toList());

  Order order = Order.createOrder(manager.get(), orderIngredients);
  orderRepository.save(order);
  return order.getId();
}

// after
public Long order(Long managerId, Map<String, Integer> ingredients) {
  Optional<Person> manager = personRepository.findById(managerId);
  if (!manager.isPresent())
      throw new IllegalArgumentException("No such person!");

  Order order = Order.createOrder(manager.get(), getOrderIngredients(ingredients));
  orderRepository.save(order);
  return order.getId();
}

private List<OrderIngredient> getOrderIngredients(Map<String, Integer> ingredients) {
  List<OrderIngredient> orderIngredients = ingredients.keySet()
    .stream()
    .map(ingredientName -> {
        int quantity = ingredients.get(ingredientName);
        inventoryService.addStock(ingredientName, quantity);
        
        return OrderIngredient.createOrderIngredient
                (getIngredientFromInventory(ingredientName), quantity);
    })
    .collect(Collectors.toList());

  return orderIngredients;
}

private Ingredient getIngredientFromInventory(String ingredientName) {
    return ingredientRepository.findByName(ingredientName) == null
      ? IngredientFactory.createIngredient(ingredientName) // Create ingredient if it is a new ingredient
      : ingredientRepository.findByName(ingredientName);
}
```
