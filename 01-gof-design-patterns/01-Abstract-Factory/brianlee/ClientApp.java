package me.whiteship.designpatterns._01_creational_patterns._03_abstract_factory.brian;

/**
 * Factory Method vs Abstract Factory
 * https://whereami80.tistory.com/211
 */
public class ClientApp {
    public static PizzaStore pizzaHut =
            new PizzaStore(new CombinationPizzaIngredientFactory(), new CheesePizzaIngredientFactory());

    public static void main(String[] args) {
        System.out.println(pizzaHut.orderPizza("cheese").getDescription());
        System.out.println(pizzaHut.orderPizza("combination").getDescription());
        System.out.println(pizzaHut.orderPizza("vegetable").getDescription());
    }
}
