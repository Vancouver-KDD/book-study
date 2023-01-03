/**
 * Implementation of thread safe singleton
 */
public class SingletonB {
    private static SingletonB singleton;
  
    private SingletonB(){}
  
    public static synchronized SingletonB getInstance(){
      if (singleton == null) {
        singleton = new SingletonB(); 
      }
      return singleton;
    }
  }