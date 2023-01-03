import java.util.ArrayList;
import java.util.HashMap;

/* Proxy object to manage the access to third transaction object */
public class TransactionProxy implements Transaction {
  private Transaction transaction;
  private HashMap<String, ArrayList<String>> cache;
  private ArrayList<String> users;

  public TransactionProxy(Transaction transaction) {
    this.transaction = transaction;
    cache = new HashMap<>();
  }

  @Override
  public ArrayList<String> getTransactionHistory(String name) {
    ArrayList<String> tmp = cache.get(name);
    if (tmp == null) {
      tmp = transaction.getTransactionHistory(name); // only access to real object when no cached data found
      cache.put(name, tmp);
    }
    return tmp;
  }

  @Override
  public ArrayList<String> getUserList() {
    if (users == null) {
      users = transaction.getUserList(); // only access to real object when no user list found
    }
    return users;
  }
  
}
