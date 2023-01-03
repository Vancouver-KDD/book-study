package me.whiteship.designpatterns._02_structural_patterns._08_composite.brian;

public class Label extends DefaultComponent  {

    private String txt;

    public Label(String txt) {
        this.txt = txt;
    }

    @Override
    public void print() {
        if(isVisible) {
            System.out.println("----------------------------");
            System.out.println("| " + txt + " |");
            System.out.println("----------------------------");
        }
    }
}
