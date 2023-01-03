package me.whiteship.designpatterns._01_creational_patterns._03_abstract_factory.brian;

import me.whiteship.designpatterns._01_creational_patterns._03_abstract_factory.brian.ingredients.Cheese;
import me.whiteship.designpatterns._01_creational_patterns._03_abstract_factory.brian.ingredients.Dough;
import me.whiteship.designpatterns._01_creational_patterns._03_abstract_factory.brian.ingredients.Source;

/**
 * 객체의 구성(Composition)을 통해 제품을 생성-> 제품군을 만들때 사용 가능
 */
public interface IngredientFactory {

    String getPizzaName();
    Dough createDough();
    Source createSource();
    Cheese createCheese();
}
