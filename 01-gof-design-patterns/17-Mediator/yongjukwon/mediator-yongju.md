# Mediator pattern example

- Restricts direct communications between objects and forces them to collaborate via a mediator

```java
// Client
public class Client {
    public static void main(String[] args) {
        MorningRoutine morningRoutine = new MorningRoutine(); // mediator
        morningRoutine.triggerAlarm("morning"); 
    }
}

// Mediator interface
public interface RoutineMediator {
    void triggerAlarm(RoutineItem alarm);
    Calendar.Day checkCalendar(int date);
    void handleCoffeePot(float temperature);
}

// Concrete Mediator
public class MorningRoutine implements RoutineMediator {

    private Alarm alarm;
    private CoffeePot    coffeePot;
    private Calendar     calendar;

    public MorningRoutine() {
        alarm        = new Alarm(this);
        coffeePot    = new CoffeePot(this);
        calendar     = new Calendar(this);
    }

    @Override
    public void triggerAlarm(String time) {
        Day day = null;
        if (time.equals("morning")) {
            day = checkCalendar(1); // Today

            if (day == WEEKDAY) {
                handleCoffeePot(99.8f);
                // bla bla
            } else if (day == WEEKEND) {
                // bla bla
            } else {
                // bla bla
            }
        } else if (time.equals("night")) {
            // bla bla
        }

    }

    @Override
    public Calendar.Day checkCalendar(int date) {
        return calendar.getDayType(date);
    }

    @Override
    public void handleCoffeePot(float temperature) {
        coffeePot.turnOnCoffeePot(temperature);
    }
}

// Colleagues abstract class
public abstract class RoutineItem {

    private RoutineMediator mediator;

    public RoutineItem(RoutineMediator mediator) {
        this.mediator = mediator;
    }
}


// Concrete Colleague; MorningAlarm
public class Alarm extends RoutineItem {

    public Alarm(RoutineMediator mediator) {
        super(mediator);
    }
}


// Concrete Colleague; CoffeePot
public class CoffeePot extends RoutineItem {

    private float temperature;

    public CoffeePot(RoutineMediator mediator) {
        super(mediator);
    }

    public void turnOnCoffeePot(float temperature) {
        this.temperature = temperature;
    }
}

// Concrete Colleague; Calendar
public class Calendar extends RoutineItem {

    public enum Day { WEEKDAY, WEEKEND, HOLIDAY };

    public Calendar(RoutineMediator mediator) {
        super(mediator);
    }

    public Day getDayType(int date) { // for convenience
        switch (date) {
            case 1:
                return Day.WEEKEND;
            case 2:
                return Day.HOLIDAY;
            default:
                return Day.WEEKDAY;
        }
    }
}
```