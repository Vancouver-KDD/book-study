import java.util.List;

/* Concrete Composite Command */
public class LeaveHomeCmd implements ICommand{
  private List<ICommand> commands;

  public LeaveHomeCmd(List<ICommand> commands) {
    this.commands = commands;
  }
  
  @Override
  public void execute() {
    for (ICommand command: commands) {
      command.execute();
    }  
  }
  
}
