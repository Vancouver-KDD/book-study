import factory.*;
import product.Product;

/**
 * Output: 
 * The factory creates model 2022 Macbook Pro and its price is USD $1,800
 * 
 * The factory creates model 2021 Chrome Book and its price is USD $1,200
 */
public class FactoryMethod {
  private static void printInfo(Factory factory) {
    Product product = factory.getInstance();
    System.out.printf(
      "The factory creates model %s and its price is %s \n\n", 
      product.getModel(), 
      product.getPrice());
  }

  public static void main(String[] args) {
    Factory appleFactory = new AppleFactory();
    printInfo(appleFactory);

    Factory googleFactory = new GoogleFactory();
    printInfo(googleFactory);  
    
  }
}
