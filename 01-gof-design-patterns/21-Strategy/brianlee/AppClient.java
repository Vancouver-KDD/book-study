package me.whiteship.designpatterns._03_behavioral_patterns._21_strategy.brianlee;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class AppClient {

    public static void main(String[] args) {
        Set<Integer> sortedSet = new TreeSet<>(Comparator.naturalOrder());
        sortedSet.add(10);
        sortedSet.add(5);
        sortedSet.add(8);
        sortedSet.add(1);
        sortedSet.add(9);
        System.out.println(sortedSet);
        Set<Integer> sortedSet2 = new TreeSet<>(Comparator.reverseOrder());
        sortedSet2.addAll(sortedSet);
        System.out.println(sortedSet2);
    }
}
