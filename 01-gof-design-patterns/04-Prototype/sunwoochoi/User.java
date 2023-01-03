import java.util.List;

/*
 * declare interface of user object
 */
public abstract class User implements Cloneable {
  public abstract Object clone();
  public abstract List<String> getAllUsers();
  public abstract void loadAllUsers();
  public abstract void handleUnsubscribeRequest(String name);
}