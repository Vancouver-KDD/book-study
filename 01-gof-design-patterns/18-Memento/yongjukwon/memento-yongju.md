# Memento pattern example

```java
// Originator
public class TextArea {

    private String state;

    public void setState(String state) {
        System.out.println("Writing the text: " + state);
        this.state = state;
    }

    public Snapshot takeSnapshot() {
        System.out.println("Taking snapshot: " + this.state);
        return new Snapshot(this.state);
    }

    public void restore(Snapshot snapshot) {
        this.state = snapshot.getSnapshot();
        System.out.println("Restoring snapshot: " + this.state);
    }

    // Memento
    public static class Snapshot {
        private final String state;

        public Snapshot(String state) {
            this.state = state;
        }

        // Only accessible by Originator
        private String getSnapshot() {
            return this.state;
        }
    }
}

// CareTaker
public class Editor {
    private Deque<Snapshot> snapshots; // Memento
    private TextArea        textArea;  // Originator

    public Editor() {
        snapshots = new LinkedList<>();
        textArea  = new TextArea();
    }

    public void write(String text) {
        textArea.setState(text);
        snapshots.add(textArea.takeSnapshot());
    }

    public void undo() {
        textArea.restore(snapshots.pop());
    }
}

public class Client {
    public static void main(String[] args) {
        Editor editor = new Editor();

        editor.write("This is the first line");
        editor.write("I want to change my text to this");
        editor.write("Actually this is the final line");
        editor.write("Nope, one more line");

        System.out.println();

        editor.undo();
    }
}
```