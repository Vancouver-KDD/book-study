import java.time.LocalTime;

/* concrete observer  */
public class Logger implements Observer {

  @Override
  public void update(String action, LocalTime time) {
    System.out.println("Logger gets user action: " + action + " Time: " + time);
  }
  
}
