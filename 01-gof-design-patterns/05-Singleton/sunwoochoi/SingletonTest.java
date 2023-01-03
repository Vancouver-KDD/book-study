public class SingletonTest {
  public static void main(String[] args) {
    SingletonA singA1 = SingletonA.getInstance();
    SingletonA singA2 = SingletonA.getInstance();

    System.out.println("Lazy initialization singleton: " + isEqualInstance(singA1, singA2));

    SingletonB singB1 = SingletonB.getInstance();
    SingletonB singB2 = SingletonB.getInstance();

    System.out.println("Thread safe initialization singleton: " + isEqualInstance(singB1, singB2));

    SingletonC singC1 = SingletonC.getInstance();
    SingletonC singC2 = SingletonC.getInstance();

    System.out.println("Eager initialization singleton: " + isEqualInstance(singC1, singC2));
  }

  public static boolean isEqualInstance(Object a, Object b) {
    return a.hashCode() == b.hashCode();
  }

}
