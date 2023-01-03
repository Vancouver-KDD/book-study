package me.whiteship.designpatterns._03_behavioral_patterns._22_template.brianlee;

@FunctionalInterface
public interface Operator {

    int getResult(int result, Email email);
}
