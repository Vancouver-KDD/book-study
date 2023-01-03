package factory;

import product.Product;


public abstract class Factory {
  // implement a method to get product instance
  public Product getInstance() {
    return createProduct();
  }

  // let subclass decide how to create product instance
  protected abstract Product createProduct();
}