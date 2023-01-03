**TextEditor.java**
```
public class TextEditor {

    private TextWindow textWindow;
    private TextWindowState savedTextWindow;

    public TextEditor(TextWindow textWindow) {
        this.textWindow = textWindow;
    }

    public void hitSave() {
        savedTextWindow = textWindow.save();
    }

    public void hitUndo() {
        textWindow.restore(savedTextWindow);
    }
}
```

**TextWindow.java**
```
public class TextWindow {

    private StringBuilder currentText;

    public TextWindow() {
        this.currentText = new StringBuilder();
    }

    public void addText(String text) {
        currentText.append(text);
    }
}
```

**TextWindowState.java**
```
public class TextWindowState {

    private String text;
    private StringBuilder currentText;

    public TextWindowState(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public TextWindowState save() {
        return new TextWindowState(currentText.toString());
    }

    public void restore(TextWindowState save) {
        currentText = new StringBuilder(save.getText());
    }
}
```

**Demo.java**
```
public class Demo {
    TextEditor textEditor = new TextEditor(new TextWindow());
    textEditor.write("The Memento Design Pattern\n");
    textEditor.write("How to implement it in Java?\n");
    textEditor.hitSave();
    
    textEditor.write("Buy milk and eggs before coming home\n");
    
    textEditor.hitUndo();

    assertThat(textEditor.print()).isEqualTo("The Memento Design Pattern\nHow to implement it in Java?\n");
}
```