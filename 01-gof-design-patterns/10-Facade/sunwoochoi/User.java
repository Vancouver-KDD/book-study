/* user object store basic information of user */
public class User {
  private String uid;
  private boolean isSubscriber;
  
  public User(String uid, boolean isSubscriber) {
    this.uid = uid;
    this.isSubscriber = isSubscriber;
  }

  public String getId() {
    return uid;
  }

  public boolean getStatus() {
    return isSubscriber;
  }
}
