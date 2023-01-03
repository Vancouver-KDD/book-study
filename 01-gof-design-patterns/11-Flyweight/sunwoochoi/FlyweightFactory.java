import java.util.HashMap;

/* Singleton factory creates and manage Square objects */
public enum FlyweightFactory {
  INSTANCE;

  private static final HashMap<String, Shape> squareMap = new HashMap<>();

  public static Shape getSquare(String color) {
    Square square = (Square) squareMap.get(color);
    if (square == null ){
      square = new Square(color);
      squareMap.put(color, square);
      System.out.println("Create a new square object with color " + color);
    }
    return square;
  }
}
