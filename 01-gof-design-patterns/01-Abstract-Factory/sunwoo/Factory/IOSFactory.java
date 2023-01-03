package Factory;

import Button.IOSButton;
import Button.Button;
import View.IOSView;
import View.View;

/**
 * concrete iOS factory
 * implements the operations to create concrete iOS objects
 */
public class IOSFactory implements Factory {
  @Override
  public Button createButton() {
    return new IOSButton();
  };
  @Override
  public View createView() {
    return new IOSView();
  };
}
  