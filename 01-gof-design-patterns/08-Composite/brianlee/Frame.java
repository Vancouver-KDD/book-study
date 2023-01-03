package me.whiteship.designpatterns._02_structural_patterns._08_composite.brian;

import java.util.ArrayList;
import java.util.List;

public class Frame implements Component {

    private final List<Component> componentList;

    public Frame() {
        this.componentList = new ArrayList<>();
    }

    public void addComponent(Component component) {
        this.componentList.add(component);
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.componentList.stream().forEach(component -> {
            component.setVisible(isVisible);
        });
    }

    @Override
    public void print() {
        System.out.println("================================");
        this.componentList.stream().forEach(component -> {
            component.print();
        });
        System.out.println("================================");
    }
}
