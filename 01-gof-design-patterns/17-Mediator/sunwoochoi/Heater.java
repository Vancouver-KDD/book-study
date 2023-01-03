public class Heater implements Component {
  private boolean isOn;
  private Mediator mediator;

  public Heater(Mediator mediator) {
    this.mediator = mediator;
    this.isOn = false;
  }
  
  @Override
  public void update() {
    mediator.onChanged(this);  
  }
  
  public void turnOn() {
    if (isOn) return;
    System.out.println("here");
    isOn = true;
    update();
  }

  public void turnOff() {
    if (!isOn) return;
    System.out.println("here2");
    isOn = false;
    update();
  }
  
  public boolean isOn() {return isOn;}
}
