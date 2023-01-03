package View;

/**
 * implementation of concrete iOS view product
 */
public class IOSView implements View{
  public IOSView() {
    System.out.println("initial iOS view rendered");
  }
  @Override
  public void render() {
    System.out.println("iOS view re-rendered");
  }
}
