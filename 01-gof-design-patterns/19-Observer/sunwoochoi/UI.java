import java.time.LocalTime;

/* concrete observer  */
public class UI implements Observer {
  @Override
  public void update(String action, LocalTime time) {
    System.out.println("UI gets user action: " + action + " Time: " + time);
  }
}
