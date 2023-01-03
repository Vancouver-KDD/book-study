# [Subin] Command

**Command.java**
```
interface Command {
    public void execute();
}
```

**Light.java**
```
class Light {
    public void on() {
        System.out.println("Light is on");
    }
    public void off() {
        System.out.println("Light is off");
    }
}
```

**LightOnCommand.java**
```
class LightOnCommand implements Command {
    Light light;

    public LightOnCommand(Lighth light) {
        this.light = light;
    }

    public void execute() {
        light.on();
    }
}
```

**LightOffCommand.java**
```
class LightOffCommand implements Command {
    Light light;
    public LightOffCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.off();
    }
}
```

**Stereo.java**
```
class Stereo {
    public void on() {
        System.out.println("Stereo is on");
    }

    public void off() {
        System.out.println("Stereo is off");
    }

    public void setCD() {
        System.out.println("Stereo is set" + " for CD input");
    }

    public void setDVD() {
        System.out.println("Stereo is set" +  " for DVD input");
    }

    public void setRadio() {
        System.out.println("Stero is set" +  "for Radio");
    }

    public void setVolume(int volume) {
        System.out.println("Stereo volume set" + " to " + volume);
    }
}
```

**StereoOffCommand.java**
```
class StereoOffCommand implements Command
{
    Stereo stereo;
    public StereoOffCommand(Stereo stereo)
    {
        this.stereo = stereo;
    }
    public void execute()
    {
       stereo.off();
    }
}
```

**StereoOnWithCDCommand.java**
```
class StereoOnWithCDCommand implements Command
{
     Stereo stereo;
     public StereoOnWithCDCommand(Stereo stereo)
     {
         this.stereo = stereo;
     }
     public void execute()
     {
         stereo.on();
         stereo.setCD();
         stereo.setVolume(11);
     }
}
```

**SimpleRemoteControl.java**
```
class SimpleRemoteControl
{
    Command slot;  // only one button
 
    public SimpleRemoteControl()
    {
    }
 
    public void setCommand(Command command)
    {
        // set the command the remote will
        // execute
        slot = command;
    }
 
    public void buttonWasPressed()
    {
        slot.execute();
    }
}
```

**RemoteControlTest.java**
```
class RemoteControlTest
{
    public static void main(String[] args)
    {
        SimpleRemoteControl remote = new SimpleRemoteControl();
        Light light = new Light();
        Stereo stereo = new Stereo();
 
        // we can change command dynamically
        remote.setCommand(new LightOnCommand(light));
        remote.buttonWasPressed();
        remote.setCommand(new
                StereoOnWithCDCommand(stereo));
        remote.buttonWasPressed();
        remote.setCommand(new
                   StereoOffCommand(stereo));
        remote.buttonWasPressed();
     }
}
```
