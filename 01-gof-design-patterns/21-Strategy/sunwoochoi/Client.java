public class Client {
  public static void main(String[] args) {
    Strategy fStrategy = new FreeStrategy();
    Strategy sStrategy = new SubscriberStrategy();

    User freeTrialUser = new User();
    freeTrialUser.setStrategy(fStrategy);

    User subUser = new User();
    subUser.setStrategy(sStrategy);

    freeTrialUser.swap(); // Recommendation for free user
    subUser.swap(); // Special recommendation only for subscriber!
  }
}