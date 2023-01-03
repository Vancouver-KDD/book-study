/* Simple user object */
public class User {
  private String name;
  private String email;
  private int age;

  public User(String name, String email, int age) {
    this.name = name;
    this.email = email;
    this.age = age;
  }

  public void print() {
    System.out.println("User:" + name + ",Email:" + email + ",age:" + age);
  }
}
