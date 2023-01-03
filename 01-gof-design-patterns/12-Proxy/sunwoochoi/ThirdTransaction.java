import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/* Real object to load all data required by client */
public class ThirdTransaction implements Transaction {
  private HashMap<String, ArrayList<String>> db;
  private ArrayList<String> users;
  
  public ThirdTransaction() {
    db = new HashMap<>();
    String[] tmp = {"Sam", "Eric", "Mina", "Tolu", "Smit", "Sunwoo"};
    users = new ArrayList<>(Arrays.asList(tmp));
    for(String user: users) {
      getHistory(user);
    }
  }

  private void getHistory(String name) {
    ArrayList<String> tmp = new ArrayList<>();
    for (int i = 0; i < (int) (Math.random() * 4 + 1); i++) {
      Date tmpDate = new Date();
      tmp.add("Date: " + tmpDate.toString() + "; Amount:$ "+ (int) (Math.random() * 100));
    }
    db.put(name, tmp);
  }

  @Override
  public ArrayList<String> getTransactionHistory(String name) {
    System.out.println("Get transaction history of user " + name + " from third party library");
    return db.get(name);
  }

  @Override
  public ArrayList<String> getUserList() {
    System.out.println("Get user list from third party library");
    return users;
  }
}
