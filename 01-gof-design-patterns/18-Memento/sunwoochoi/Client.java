public class Client {
  public static void main(String[] args) {
    Caretaker caretaker = new Caretaker();
    TextEditor editor = new TextEditor();

    caretaker.addBackup(editor.createSnapshot());

    editor.addText("hello world");

    caretaker.addBackup(editor.createSnapshot());

    editor.addText("design pattern");

    caretaker.addBackup(editor.createSnapshot());

    editor.undo(caretaker.restore());

    editor.print();

    editor.undo(caretaker.restore());

    editor.print();

    editor.undo(caretaker.restore());

    editor.print();
    
  }
}