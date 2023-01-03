package Button;

/**
 * concrete Android button product
 */
public class AndroidButton implements Button {
  @Override
  public void onClick() {
    System.out.println("Click Android button");
  }

}
