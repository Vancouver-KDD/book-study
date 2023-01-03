package View;

/**
 * implementation of concrete Android view object
 */
public class AndroidView implements View{
  public AndroidView() {
    System.out.println("initial Android view rendered");
  }
  
  @Override
  public void render() {
    System.out.println("Android view re-rendered");
  }
}
