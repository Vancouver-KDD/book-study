package me.whiteship.designpatterns._03_behavioral_patterns._14_command.brianlee;

public class ClientApp {
    public static void main(String[] args) {
        Light light = new Light();
        // Button 의 코드는 변하지 않는다.
        Button button = new Button(light::turnOn);
        Button button2 = new Button(light::turnOff);
        button.press();
        button2.press();
    }
}
