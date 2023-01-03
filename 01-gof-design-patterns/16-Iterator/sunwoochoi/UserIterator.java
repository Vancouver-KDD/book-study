import java.util.ArrayList;

/* Iterator for user object */
public class UserIterator implements Iterator {
  private ArrayList<User> users;
  private int idx;
  
  public UserIterator(ArrayList<User> users) {
    this.users = users;
    this.idx = 0;
  }

  @Override
  public boolean hasNext() {
    
    return idx != users.size();
  }

  @Override
  public User next() {
    return (User) users.get(idx++);
  }

  
}
