/* originator */
public class TextEditor {
  private Snapshot state;

  public TextEditor() {
    this.state = new Snapshot(0, "'");
  } 
  public void addText(String text) {
    this.state.addText(text);
  }

  public Snapshot createSnapshot() {
    return new Snapshot(state.getPos(), state.getText());
  }

  public void undo(Memento snapshot) {
    this.state = (Snapshot) snapshot;
  }

  public void print() {
    String s = state.getText();
    int pos = state.getPos();
    System.out.println(s.substring(0, pos+1) + '|' + s.substring(pos+1));
  }

  private class Snapshot implements Memento {
    private int pos;
    private String text;

    public Snapshot(int pos, String text) {
      this.pos = pos;
      this.text = text;
    }

    private int getPos() {
      return pos;
    }
    private String getText() {
      return text;
    }
    private void addText(String text) {
      this.pos += text.length();
      this.text += text;
    }
  }
  
}
