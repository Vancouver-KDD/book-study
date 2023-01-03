import java.time.LocalTime;
import java.util.ArrayList;

/* concrete subject to handle user interaction  */
public class UserActionData implements Subject {
  private ArrayList<Observer> observers;
  private String action;
  private LocalTime time;
  
  public UserActionData() {
    observers = new ArrayList<>();
  }

  @Override
  public void attach(Observer ob) {
    observers.add(ob);
  }

  @Override
  public void detach(Observer ob) {
    observers.remove(ob);
  }

  @Override
  public void notifyChange() {
    for (Observer observer: observers) {
      observer.update(action, time);
    }
  }

  public void setAction(String action) {
    this.action = action;
    this.time = LocalTime.now();
    notifyChange();
  }
  

}
