package me.whiteship.designpatterns._02_structural_patterns._08_composite.brian;

public class ClientApp {

    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.addComponent(new Button("submit"));
        frame.addComponent(new Label("please, push the button"));
        frame.setVisible(true);
        System.out.println("Visible Ture");
        frame.print();
        frame.setVisible(false);
        System.out.println("Visible False");
        frame.print();
    }
}
