# State pattern example

```java
// Client
public class Client {
    public static void main(String[] args) {
        AlertContext context = new AlertContext(); // sound mode by default
        context.alert();
        context.changeStatus(Silent.getSilentState());
        context.alert();
        context.changeStatus(Vibration.getVibrationState());
        context.alert();
    }
}

// Context 
public class AlertContext {
    private AlertState currentState;

    public AlertContext() {
        currentState = Sound.getSoundState();
    }

    public void alert() {
        currentState.alert(this);
    }

    // Used to change status by user
    public void changeStatus(AlertState alertState) {
        this.currentState = alertState;
    }
}

// State Interface
public interface AlertState {
    void alert(AlertContext alertContext);
}

// Concrete State object (Singleton)
public class Silent implements AlertState {

    private static AlertState silentState;

    private Silent(){}

    public static AlertState getSilentState() {
        return silentState = silentState == null ? new Silent() : silentState;
    }

    @Override
    public void alert(AlertContext alertContext) {
        // logic to alert when it is in silent mode
        System.out.println("Vibrating..");
    }
}

// Concrete State object (Singleton)
public class Vibration implements AlertState {

    private static AlertState vibrationState;

    private Vibration(){}

    public static AlertState getVibrationState() {
        return vibrationState = vibrationState == null ? new Vibration() : vibrationState;
    }

    @Override
    public void alert(AlertContext alertContext) {
        // logic to alert when it is in vibration mode
        System.out.println("Vibrating..");
    }
}

// Concrete State object (Singleton)
public class Sound implements AlertState {

    private static AlertState soundState;

    private Sound() {}

    public static AlertState getSoundState() {
        return soundState = soundState == null ? new Sound() : soundState;
    }

    @Override
    public void alert(AlertContext alertContext) {
        // logic to alert when it is in sound mode
        System.out.println("Ringing..");
    }
}
```
