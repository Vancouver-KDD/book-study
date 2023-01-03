package factory;

import product.Product;
import product.AppleProduct;

/**
 * concrete factory class for Apple
 * it decides what instance will be created
 */
public class AppleFactory extends Factory {
  @Override
  protected Product createProduct() {
    return new AppleProduct();
  }
}
