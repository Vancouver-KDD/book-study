public class AirConditioner implements Component {
  private boolean isOn;
  private Mediator mediator;

  public AirConditioner(Mediator mediator) {
    this.mediator = mediator;
    this.isOn = false;
  }
  
  @Override
  public void update() {
    mediator.onChanged(this);  
  }
  
  public void turnOn() {
    if (isOn) return;
    isOn = true;
    update();
  }

  public void turnOff() {
    if (!isOn) return;
    isOn = false;
    update();
  }
  
  public boolean isOn() {return isOn;}
}