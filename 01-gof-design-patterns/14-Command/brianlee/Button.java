package me.whiteship.designpatterns._03_behavioral_patterns._14_command.brianlee;

public class Button {
    Runnable runnable;

    public Button(Runnable runnable) {
        this.runnable = runnable;
    }

    public void press() {
        runnable.run();
    }
}
