package me.whiteship.designpatterns._01_creational_patterns._01_singleton.brian;

public class WeakSingleton {

    private static WeakSingleton weakSingleton;

    private WeakSingleton() {}

    public static WeakSingleton getInstance() {
        if(weakSingleton == null) {
            weakSingleton = new WeakSingleton();
        }
        return weakSingleton;
    }
}
