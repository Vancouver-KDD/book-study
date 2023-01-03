/* Concrete Square object */
public class Square implements Shape {
  private int width;
  private int height;
  private String color;

  public Square(String color) {
    this.color = color;
  }
  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public void draw() {
    System.out.printf("Draw %s square with width %d and height %d\n", color, width, height);
  }
  
}
