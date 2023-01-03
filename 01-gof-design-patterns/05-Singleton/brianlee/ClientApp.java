package me.whiteship.designpatterns._01_creational_patterns._01_singleton.brian;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp {
    public static void main(String[] args) {
        Set<DoubleCheckedLocking> doubleCheckedLockings = new HashSet<>();
        Set<EnumSingleton> enumSingletons = new HashSet<>();
        Set<WeakSingleton> weakSingletons = new HashSet<>();

        int thread = 10;
        ExecutorService executor = Executors.newFixedThreadPool(thread);
        for(int i = 0; i < 100; i++) {
            executor.execute(() -> {
                doubleCheckedLockings.add(DoubleCheckedLocking.getInstance());
                enumSingletons.add(EnumSingleton.getInstance());
                weakSingletons.add(WeakSingleton.getInstance());
            });
        }
        executor.shutdown();

        System.out.println(doubleCheckedLockings);
        System.out.println(enumSingletons);
        System.out.println(weakSingletons);
    }
}
