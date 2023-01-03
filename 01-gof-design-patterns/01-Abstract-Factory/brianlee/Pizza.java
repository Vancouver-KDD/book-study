package me.whiteship.designpatterns._01_creational_patterns._03_abstract_factory.brian;

import me.whiteship.designpatterns._01_creational_patterns._03_abstract_factory.brian.ingredients.Cheese;
import me.whiteship.designpatterns._01_creational_patterns._03_abstract_factory.brian.ingredients.Dough;
import me.whiteship.designpatterns._01_creational_patterns._03_abstract_factory.brian.ingredients.Source;

public class Pizza {

    private final String name;
    private final Dough dough;
    private final Cheese cheese;
    private final Source source;

    public Pizza(String name, Dough dough, Cheese cheese, Source source) {
        this.name = name;
        this.dough = dough;
        this.cheese = cheese;
        this.source = source;
    }

    String getDescription() {
        return "The "+ name +"'s ingredients are "
                + dough.getClass().getSimpleName() + ", "
                + cheese.getClass().getSimpleName() + ", "
                + source.getClass().getSimpleName() + ".";
    }
}
