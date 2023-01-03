import java.util.ArrayList;
import java.util.List;

/**
 * Client
 */
public class Client {
  public static void main(String[] args) {
    Light light = new Light();
    TV tv = new TV();
    
    ICommand turnOffLightCmd = new TurnOffLightCmd(light);
    ICommand turnOffTVCmd = new TurnOffTVCmd(tv);
    List<ICommand> cmds = new ArrayList<>();
    cmds.add(turnOffTVCmd);
    cmds.add(turnOffLightCmd);
    ICommand leaveHomeCmd = new LeaveHomeCmd(cmds);
    
    SmartHome smartHome = new SmartHome();
    
    smartHome.setCommand(turnOffLightCmd);
    smartHome.execute(); // Turn off the light

    smartHome.setCommand(turnOffTVCmd);
    smartHome.execute(); // Turn off the TV

    smartHome.setCommand(leaveHomeCmd);
    smartHome.execute();
    // Turn off the TV
    // Turn off the light
  }
  
}