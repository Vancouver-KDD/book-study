/* Concrete Command */
public class TurnOffTVCmd implements ICommand {
  private TV tv;

  public TurnOffTVCmd (TV tv) {
    this.tv = tv;
  }
  @Override
  public void execute() {
    tv.turnOff();
  }
  
}
