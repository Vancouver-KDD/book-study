/**
 * Output:
 * start fetching user data from db
 * Successfully delete user Sam
 * Previous subscribed users [Sam, Eric, Tolu, Minami]
 * Previous subscribed users [Eric, Tolu, Minami]
 * 
 * Reference: https://readystory.tistory.com/122
 */

public class Prototype {
  public static void main(String[] args) {
    User user = new SubscribedUser();
    user.loadAllUsers(); // fetch all data from db

    // it copies user list without accessing to db
    // and return new object
    User updatedUser = (User) user.clone();
    updatedUser.handleUnsubscribeRequest("Sam");

    System.out.println("Previous subscribed users " + user.getAllUsers());
    System.out.println("Previous subscribed users " + updatedUser.getAllUsers());
  }
}
