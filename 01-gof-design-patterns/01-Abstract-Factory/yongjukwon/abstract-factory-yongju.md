# Abstract Factory

### Pattern with class diagram

<div style="text-align:center">
  <img src="./images/abstract_factory_diagram.png" width=800>
</div>

### Components (Participants)

- AbstractFactory: an interface or abstract class whose subclasses instantiate a family of abstract product objects
- ConcreteFactory: concrete subclasses implement/extend the abstract factory. The instance of this object instantiates a family of AbstractProducts
- AbstractProduct: an interface or abstract class whose subclasses are instantiated by the abstract factory objects
- ConcreteProduct: concrete subclasses implement/extend the abstract product.
- Client: uses abstract factory to get abstract product objects

### Sample codeBB
```java
  /* AbstractFactory - ICompanyFactory interface */
  public interface ICompanyFactory {
    IProduct manufactureKeyboard();
    IProduct manufactureMouse();
  }

  /* AbstractProduct - IKeyboard interface */
  public interface IKeyboard {}
  /* AbstractProduct - IMouse interface */
  public interface IMouse {}

  /* ConcreteProduct - LogitechKeyboard */
  public class LogitechKeyboard implements IKeyboard {}
  /* ConcreteProduct - LogitechMouse */
  public class LogitechMouse implements IMouse {}
  /* ConcreteProduct - RazorKeyboard */
  public class RazorKeyboard implements IKeyboard {}
  /* ConcreteProduct - RazorMouse */
  public class RazorMouse implements IMouse {}

  /* ConcreteFactory - LogitechFactory */
  public class LogitechFactory implements ICompanyFactory {

    @Override
    public IMouse manufactureMouse() {
        return new LogitechMouse();
    }

    @Override
    public IKeyboard manufactureKeyboard() {
        return new LogitechKeyboard();
    }
  }

  /* ConcreteFactory - RazorFactory */
  public class RazorFactory implements ICompanyFactory {

    @Override
    public IMouse manufactureMouse() {
        return new RazorMouse();
    }

    @Override
    public IKeyboard manufactureKeyboard() {
        return new RazorKeyboard();
    }
  }

  /* Client */
  public class Client {

    public static Map<Integer, Object> getProducts(ICompanyFactory companyFactory) {
        Map<Integer, Object> products = new HashMap<>();

        products.put(1, companyFactory.manufactureKeyboard());
        products.put(2, companyFactory.manufactureMouse());

        return products;
    }

    public static void main(String[] args) {
        ICompanyFactory logitech = new LogitechFactory();
        ICompanyFactory razor = new RazorFactory();

        System.out.println("**** Logitech's products R****");
        Map<Integer, Object> logitechProducts = getProducts(logitech);
        logitechProducts.values().forEach(System.out::println);

        System.out.println();

        System.out.println("**** Razor's products ****");
        Map<Integer, Object> razorProducts = getProducts(razor);
        razorProducts.values().forEach(System.out::println);
    }
  }
```

### When to use Abstract Factory?

- A system should be independent of how its products are created, composed, and represented
- A system should be configured with one of multiple families of products
- Code needs to work with various families of related products
- You want to provide a class library of products, and you want to reveal just their interfaces, not their implementations
- Where we have a factory that will need to change based on information that isn't available at compile-time

### Pros and Cons
##### Pros: 
Abstract Factory patten
- Isolating concrete classes 
- Exchanging product families easy
- Promoting consistency among products
- Can make sure products in a family are compatible each other
- We can introduce new variants of products without modifying client code (Open/Closed principle)

##### Cons:
- Supporting new kinds of products is difficult


### Differences between Abstract Factory and Factory method

- Factory method is a method to create/provide a product, whereas Abstract Factory provides a object to create/provide a family of related products 
- Factory method abstracts how the objects are created, whereas Abstract Factory abstracts how the factories are created