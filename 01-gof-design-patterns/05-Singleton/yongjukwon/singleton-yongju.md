# Singleton design pattern example

```java
// Client
public class Client {
    public static void main(String[] args) {
        // Private constructor
        // KDD kdd = new KDD();

        KDD kdd = KDD.getKDDInstance();
        kdd.shoutOut();
    }
}

// KDD
public class KDD {

  private static KDD kdd;

  private KDD() {}

  public static KDD getKDDInstance() {
      if (kdd == null) {
          kdd = new KDD();
      }

      return kdd;
  }

  public void shoutOut() {
      System.out.println("KDD must be one and only!");
  }
}

```