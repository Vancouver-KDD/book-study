import Button.Button;
import Factory.AndroidFactory;
import Factory.Factory;
import Factory.IOSFactory;
import View.View;

public class Application {
  public static final int IOS = 0;
  public static final int ANDROID = 1;

  private Button button;
  private View view;
  private Factory factory;
  
  public Application(int os) {
    switch (os) {
      case IOS:
        factory = new IOSFactory();
        button = factory.createButton();
        view = factory.createView();
        break;
      case ANDROID:
        factory= new AndroidFactory();
        button = factory.createButton();
        view = factory.createView();
        break;
      default:
        System.err.println("");
        break;
    }
  }

  public void doAction() {
    button.onClick();
    view.render();
  }
}
