package Factory;

import Button.Button;
import View.View;

/**
 * abstract factory
 * declares an interface for operations that create abstract product objects
 */
public interface Factory {
  abstract public Button createButton();
  abstract public View createView();
}
