package me.whiteship.designpatterns._02_structural_patterns._08_composite.brian;

public class Button extends DefaultComponent {

    private String name;

    public Button(String name) {
        this.name = name;
    }

    @Override
    public void print() {
        if(isVisible) {
            System.out.println("----------");
            System.out.println("| "+name+" |");
            System.out.println("----------");
        }
    }
}
