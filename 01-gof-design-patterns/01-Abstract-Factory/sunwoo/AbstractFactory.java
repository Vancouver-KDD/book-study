/**
 * 
 * Output: 
 * Test Android
 * initial Android view rendered
 * Click Android button
 * Android view re-rendered
 
 * Test iOS
 * initial iOS view rendered
 * Click iOS button
 * iOS view re-rendered
 * 
 */


public class AbstractFactory{
  public static void main(String[] args) {
    
    System.out.println("\nTest Android");
    Application androidApp = new Application(Application.ANDROID);
    androidApp.doAction();
    
    System.out.println("\nTest iOS");
    Application iosApp = new Application(Application.IOS);
    iosApp.doAction();
  }
}
