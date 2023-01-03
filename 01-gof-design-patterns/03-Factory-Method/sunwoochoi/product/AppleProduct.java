package product;

/**
 * implement Apple interface by overriding Product
 */
public class AppleProduct implements Product {
  private static final String MODEL = "2022 Macbook Pro";
  private static final String PRICE = "USD $1,800";

  @Override
  public String getModel() {
    return MODEL;
  }

  @Override
  public String getPrice() {
    return PRICE;
  }
}
