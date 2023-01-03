public class AutoHomeSystem implements Mediator {
  public Door door = new Door(this);
  public Window window = new Window(this);
  public Heater heater = new Heater(this);
  public AirConditioner airConditioner = new AirConditioner(this);

  @Override
  public void onChanged(Component component) {
    if (component == door && ((Door) component).isOpened()) {
      airConditioner.turnOff();
      heater.turnOff();
    }
    if (component == window && ((Window) component).isOpened()) {
      airConditioner.turnOff();
      heater.turnOff();
    }
    if (component == airConditioner && ((AirConditioner) component).isOn()) {
      door.close();
      window.close();
      heater.turnOff();
    }
    if (component == heater && ((Heater) component).isOn()) {
      door.close();
      window.close();
      airConditioner.turnOff();
    }
  }

  public void printState() {
    System.out.println("Door is open? " + door.isOpened());
    System.out.println("Window is open? " + window.isOpened());
    System.out.println("Heater is on? " + heater.isOn());
    System.out.println("Air conditioner is on? " + airConditioner.isOn());
  }
}
