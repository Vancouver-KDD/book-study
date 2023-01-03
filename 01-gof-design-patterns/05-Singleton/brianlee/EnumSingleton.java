package me.whiteship.designpatterns._01_creational_patterns._01_singleton.brian;

public enum EnumSingleton {
    singleton();

    EnumSingleton() { }

    public static EnumSingleton getInstance() {
        return singleton;
    }
}
