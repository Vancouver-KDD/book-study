# Memento pattern

> Capture, restore object’s state without violating encapsulation

![memento](images/memento.png)

## Motivation

- sometimes it’s necessary to record the state of an object. ex) undo, check point
- but object normally encapsulate some of their states
- exposing those states would violate encapsulation, reduce application’s reliability
- memento is an object that stores a snapshot of the it’s own object

## Diagram

![memento2](images/memento2.png)

## Implementation

```java
// Originator.java

class Originator {
    private String state;

    public void set(String state) {
        this.state = state;
        System.out.println("Originator: Setting state to " + state);
    }

    public Memento saveToMemento() {
        System.out.println("Originator: Saving to Memento.");
        return new Memento(this.state);
    }

    public void restoreFromMemento(Memento memento) {
        this.state = memento.getSavedState();
        System.out.println("Originator: State after restoring from Mememto " + state);
    }

    // Memento class
    public static class Memento {
        private final String state;
        public Memento(String stateToSave) {
            state = stateToSave;
        }

        private String getSavedState() {
            return state;
        }
    }
}
```

```java
// CareTaker.java

import java.util.List;
import java.util.ArrayList;

class Caretaker {
    public static void main(String[] args) {
        List<Originator.Memento> history = new ArrayList<Originator.Memento>();

        Originator originator = new Originator();

        originator.set("State 1");
        originator.set("State 2");

        // capture snapshot
        history.add(originator.saveToMemento());
        originator.set("State 3");
        // capture snapshot
        history.add(originator.saveToMemento());
        originator.set("State 4");

        originator.restoreFromMemento(history.get(1));

        // Originator: Setting state to State 1
        // Originator: Setting state to State 2
        // Originator: Saving to Memento.
        // Originator: Setting state to State 3
        // Originator: Saving to Memento.
        // Originator: Setting state to State 4
        // Originator: State after restoring from Mememto State 3
    }
}
```

## Pros and Cons

- Pros
    - produce snapshots of the object’s state without violating encapsulation
    - simplify the Originator’s code by letting the caretaker maintain the memento history
- Cons
    - might comsume lots of RAM
    - caretaker should track the originator’s lifecycle to destroy obsolete mementos
    - Most dynamic programming languages(PHP, JS, Python…) can’t guarantee memento’s state stays untouched

## Ref

- Design Patterns Elements of Reusable Object-Oriented Software, **,** 283p
- [https://refactoring.guru/design-patterns/memento](https://refactoring.guru/design-patterns/memento)
- [https://ko.wikipedia.org/wiki/메멘토_패턴](https://ko.wikipedia.org/wiki/%EB%A9%94%EB%A9%98%ED%86%A0_%ED%8C%A8%ED%84%B4)