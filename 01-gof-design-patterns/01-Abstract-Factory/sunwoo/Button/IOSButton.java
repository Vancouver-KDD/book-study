package Button;

/**
 * concrete iOS button product
 */
public class IOSButton implements Button {
  @Override
  public void onClick() {
    System.out.println("Click iOS button");
  }
}
