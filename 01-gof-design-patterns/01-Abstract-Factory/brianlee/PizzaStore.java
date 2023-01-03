package me.whiteship.designpatterns._01_creational_patterns._03_abstract_factory.brian;

public class PizzaStore {

    private IngredientFactory combinationPizza;
    private IngredientFactory cheesePizza;

    public PizzaStore(IngredientFactory combinationPizza, IngredientFactory cheesePizza) {
        this.combinationPizza = combinationPizza;
        this.cheesePizza = cheesePizza;
    }

    public Pizza orderPizza(String pizza) {
        switch (pizza) {
            case "combination": return new Pizza(
                    combinationPizza.getPizzaName(),
                    combinationPizza.createDough(),
                    combinationPizza.createCheese(),
                    combinationPizza.createSource());
            case "cheese": return new Pizza(
                    cheesePizza.getPizzaName(),
                    cheesePizza.createDough(),
                    cheesePizza.createCheese(),
                    cheesePizza.createSource());
            default:
                throw new IllegalArgumentException("Not support Pizza");
        }

    }
}
