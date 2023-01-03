/**
 * Implementation of lazy initialization singleton
 */
public class SingletonA {
  private static SingletonA singleton;

  private SingletonA(){};
  
  public static SingletonA getInstance() {
    if (singleton == null) {
      singleton = new SingletonA();
    }
    return singleton;
  }
}