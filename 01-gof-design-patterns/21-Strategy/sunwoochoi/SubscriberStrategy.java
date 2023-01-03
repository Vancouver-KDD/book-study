/* Concrete strategy for subscribed user */
public class SubscriberStrategy implements Strategy {
  @Override
  public void recommend() {
    System.out.println("Special recommendation only for subscriber!");
  }
}
