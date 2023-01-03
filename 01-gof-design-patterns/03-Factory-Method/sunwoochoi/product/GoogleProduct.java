package product;

/**
 * implement Google interface by overriding Product
 */
public class GoogleProduct implements Product {
  private static final String MODEL = "2021 Chrome Book";
  private static final String PRICE = "USD $1,200";

  @Override
  public String getModel() {
    return MODEL;
  }

  @Override
  public String getPrice() {
    return PRICE;
  }
}
