/* Client 
 * client create 10 squares with random color, width and height, then draw it
 * 
 * Reference: https://lee1535.tistory.com/106
*/
/*
  Output:
    Create a new square object with color Grey
    Draw Grey square with width 89 and height 2
    Draw Grey square with width 40 and height 47
    Create a new square object with color White
    Draw White square with width 65 and height 45
    Create a new square object with color Black
    Draw Black square with width 21 and height 80
    Create a new square object with color Blue
    Draw Blue square with width 92 and height 22
    Create a new square object with color Red
    Draw Red square with width 46 and height 6
    Draw Red square with width 80 and height 2
    Draw Black square with width 36 and height 42
    Draw White square with width 64 and height 2
    Draw Grey square with width 94 and height 63
*/
public class Client {
  public static void main(String[] args) {
    String[] colors = {"Red", "Blue", "Grey", "White", "Black"};
    for (int i = 0; i < 10; i++) {
      Square square = (Square) FlyweightFactory.getSquare(colors[(int) (Math.random()*5)]);
      square.setWidth((int) (Math.random() * 100));
      square.setHeight((int) (Math.random() * 100));
      square.draw();
    }
  }
}