### Robot.java
```Java

package command;

public class Robot {
  public enum Direction { LEFT, RIGHT }

  public void moveForward (int space) {
    System.out.println(space + " 칸 전진");
  }

  public void turn (Direction _direction) {
    System.out.println(
      (_direction == Direction.LEFT ? "왼쪽" : "오른쪽") + "으로 방향전환"
    );
  }

  public void pickup () {
    System.out.println("앞의 물건 집어들기");
  }
}

```

### Command.java
```Java

package command;

abstract class Command {
  protected Robot robot;

  public void setRobot (Robot _robot) {
    this.robot = _robot;
  }
  public abstract void execute ();
}

class MoveForwardCommand extends Command {
  int space;
  public MoveForwardCommand (int _space) { 
    space = _space; 
  }
  public void execute () {
    robot.moveForward(space);
  }
}

class TurnCommand extends Command {
  Robot.Direction direction;
  public TurnCommand (Robot.Direction _direction) {
    direction = _direction;
  }
  public void execute () {
    robot.turn(direction);
  }
}

class PickupCommand extends Command {
  public void execute () {
    robot.pickup();
  }
}

```

### RobotKit.java
```Java
package command;

import java.util.ArrayList;

public class RobotKit {
  private Robot robot = new Robot();
  private ArrayList<Command> commands = new ArrayList<Command>();

  public void addCommand (Command command) {
    commands.add(command);
  }
  public void start () {
    for (Command command : commands) {
      command.setRobot(robot);
      command.execute();
    }
  }
}
```

### MyProgram.java
```Java
package command;

public class MyProgram {

  public static void main(String[] args) {
    RobotKit robotKit = new RobotKit();

    robotKit.addCommand(new MoveForwardCommand(2));
    robotKit.addCommand(new TurnCommand(Robot.Direction.LEFT));
    robotKit.addCommand(new MoveForwardCommand(1));
    robotKit.addCommand(new TurnCommand(Robot.Direction.RIGHT));
    robotKit.addCommand(new PickupCommand());

    robotKit.start();

    // 2 칸 전진
    // 왼쪽으로 방향전환
    // 1 칸 전진
    // 오른쪽으로 방향전환
    // 앞의 물건 집어들기
  }
}

```
