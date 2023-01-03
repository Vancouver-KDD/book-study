package factory;

import product.GoogleProduct;
import product.Product;

/**
 * concrete factory class for Google
 * it decides what instance will be created
 */
public class GoogleFactory extends Factory {
  @Override
  protected Product createProduct() {
    return new GoogleProduct();
  }
}
