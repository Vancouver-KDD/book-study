/* Concrete Command */
public class TurnOffLightCmd implements ICommand {
  private Light light;

  public TurnOffLightCmd(Light light) {
    this.light = light;
  }
  @Override
  public void execute() {
    light.turnOff();
  }
  
}
