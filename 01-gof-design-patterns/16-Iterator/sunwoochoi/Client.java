import java.util.ArrayList;

/* Client */
public class Client {
  public static void main(String[] args) {
    ArrayList<User> users = new ArrayList<>();
    users.add(new User("Sam", "sam@gmail.com", 20));
    users.add(new User("Eric", "eric@gmail.com", 23));
    users.add(new User("Mina", "mina@gmail.com", 24));
    users.add(new User("Tom", "Tom@gmail.com", 25));

    UserIterator iter = new UserIterator(users);
    GenericIterator<User> gIter = new GenericIterator<>(users);

    while (iter.hasNext()) {
      iter.next().print();
      gIter.next().print();
    }
  }
}