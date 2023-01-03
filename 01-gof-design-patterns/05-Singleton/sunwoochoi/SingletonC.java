/**
 * Implementation of eager initialization singleton
 */
public class SingletonC {
  private static SingletonC singleton = new SingletonC();
  
  private SingletonC(){}

  public static SingletonC getInstance() {
      return singleton;
  }
}

