package me.whiteship.designpatterns._02_structural_patterns._08_composite.brian;

public abstract class DefaultComponent implements Component {
    boolean isVisible;
    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
