import java.time.LocalTime;

/** declare observer interface */
public interface Observer {
  public void update(String action, LocalTime time);
}
