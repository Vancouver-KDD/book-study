import java.util.ArrayList;

/* declare shared interface by  third party transaction library object and proxy object*/
public interface Transaction {
  public ArrayList<String> getTransactionHistory(String name);
  public ArrayList<String> getUserList();
}
