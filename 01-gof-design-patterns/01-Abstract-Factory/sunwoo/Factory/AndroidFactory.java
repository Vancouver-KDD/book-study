package Factory;

import Button.AndroidButton;
import Button.Button;
import View.AndroidView;
import View.View;

/**
 * concrete Android factory
 * implements the operations to create concrete Android objects
 */
public class AndroidFactory implements Factory {
  @Override
  public Button createButton() {
    return new AndroidButton();
  };

  @Override
  public View createView() {
    return new AndroidView();
  };
}
