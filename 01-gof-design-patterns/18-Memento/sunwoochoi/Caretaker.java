import java.util.ArrayList;

public class Caretaker {
  private ArrayList<Memento> history;
  
  public Caretaker() {
    history = new ArrayList<>();
  }
  public Memento restore() {
    Memento tmp = history.get(history.size()-1);
    history.remove(history.size()-1);
    return tmp;
  }

  public void addBackup(Memento mem) {
    history.add(mem);
  }
}
