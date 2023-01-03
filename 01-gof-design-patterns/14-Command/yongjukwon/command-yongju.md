# Command pattern example

```java
// Client
public class Client {
    public static void main(String[] args) {
        Speaker boseSpeaker = new BoseSpeaker();
        SpeakerOnCommand speakerOnCommand = new SpeakerOnCommand(boseSpeaker);
        SpeakerOffCommand speakerOffCommand = new SpeakerOffCommand(boseSpeaker);

        BedRoomLight bedRoomLight = new BedRoomLight();
        BedRoomLightOnCommand bedRoomLightOnCommand = new BedRoomLightOnCommand(bedRoomLight);
        BedRoomLightOffCommand bedRoomLightOffCommand = new BedRoomLightOffCommand(bedRoomLight);

        List<Command> wakeUpCommands = new ArrayList<>();
        wakeUpCommands.add(speakerOnCommand);
        wakeUpCommands.add(bedRoomLightOnCommand);

        List<Command> goodNightCommands = new ArrayList<>();
        goodNightCommands.add(speakerOffCommand);
        goodNightCommands.add(bedRoomLightOffCommand);

        Alexa alexa = new Alexa();
        alexa.setCommand("Good morning", wakeUpCommands);
        alexa.setCommand("Good night", goodNightCommands);

        alexa.doIt("Good morning");
        alexa.doIt("Good night");

        /*
          Turning on Bose speaker
          Turning Bedroom light on
          Turning off Bose speaker
          Turning Bedroom light off
        */
    }
}

// Alexa (Invoker)
public class Alexa {

    Map<String, List<Command>> commandsMap;
    
    public Alexa() {
        commandsMap = new HashMap<>();
    }

    public void setCommand(String command, List<Command> commands) {
        if (!commandsMap.containsKey(command)) {
            commandsMap.put(command, commands);
        } else {
            commandsMap.replace(command, commands);
        }
    }

    public void doIt(String command) {
        if (!commandsMap.containsKey(command)) {
            System.out.printf("The command '%s' does not exist");
        } else {
            List<Command> commands = commandsMap.get(command);
            for (Command cmd: commands) {
                cmd.execute();
            }
        }
    }
}

// Command (Command interface)
public interface Command {
    void execute();
}

// Speaker (Receiver interface, not necessary)
public interface Speaker {

    void turnOn();
    void turnOff();
    void volumeUp();
    void volumeDown();

}

// BoseSpeaker (Concrete receiver)
public class BoseSpeaker implements Speaker {

    @Override
    public void turnOn() {
        System.out.println("Turning on Bose speaker");
    }
    @Override
    public void turnOff() {
        System.out.println("Turning off Bose speaker");
    }

    @Override
    // Can be added
    public void volumeUp() {
        System.out.println("Turning volume up of Bose speaker");
    }

    @Override
    public void volumeDown() {
        System.out.println("Turning volume down of Bose speaker");
    }
}

// SpeakerOnCommand (Concrete command)
public class SpeakerOnCommand implements Command {

    private Speaker boseSpeaker;

    public SpeakerOnCommand(Speaker speaker) {
        this.boseSpeaker = speaker;
    }

    @Override
    public void execute() {
        this.boseSpeaker.turnOn();
    }
}

// SpeakerOffCommand (Concrete command)
public class SpeakerOffCommand implements Command {

    private Speaker boseSpeaker;

    public SpeakerOffCommand(Speaker speaker) {
        this.boseSpeaker = speaker;
    }

    @Override
    public void execute() {
        this.boseSpeaker.turnOff();
    }
}

// BedRoomLight (receiver)
public class BedRoomLight {

    public void turnOn() {
        System.out.println("Turning Bedroom light on");
    }

    public void turnOff() {
        System.out.println("Turning Bedroom light off");
    }
}

// BedRoomLightOnCommand (Concrete command)
public class BedRoomLightOnCommand implements Command {
    private BedRoomLight bedRoomLight;

    public BedRoomLightOnCommand(BedRoomLight bedRoomLight) {
        this.bedRoomLight = bedRoomLight;
    }

    @Override
    public void execute() {
        bedRoomLight.turnOn();
    }
}

// BedRoomLightOffCommand (Concrete command)
public class BedRoomLightOffCommand implements Command {

    private BedRoomLight bedRoomLight;

    public BedRoomLightOffCommand(BedRoomLight bedRoomLight) {
        this.bedRoomLight = bedRoomLight;
    }

    @Override
    public void execute() {
        bedRoomLight.turnOff();
    }
}

```