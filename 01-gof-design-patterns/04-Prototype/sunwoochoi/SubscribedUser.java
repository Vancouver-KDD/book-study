import java.util.ArrayList;
import java.util.List;

/**
 * concrete class to implement abstract methods
 */
public class SubscribedUser extends User {
  List<String> userList;

  public SubscribedUser() {
    userList = new ArrayList<String>();
  }

  public SubscribedUser(List<String> list) {
    userList = list;
  }

  public List<String> getAllUsers() {
    return userList;
  }

  public void handleUnsubscribeRequest(String name) {
    if (userList.remove(name)){
      System.out.printf("Successfully delete user %s\n", name);
    }
  }

  public void loadAllUsers() {
    System.out.println("start fetching user data from db");
    this.userList.add("Sam");
    this.userList.add("Eric");
    this.userList.add("Tolu");
    this.userList.add("Minami");
  }

  @Override
  public Object clone(){
    // deep copy of a list of users
    List<String> tmp = new ArrayList<String>();
    for (String user : userList) {
      tmp.add(user);
    }
    return new SubscribedUser(tmp);
  }
}
