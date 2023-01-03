# Facade

## Prerequisite

This documents assume that
 - You have basic knowledge of c#
 - You know how to read class diagram

&nbsp;
## 1. Intent

Provide a unified interface to a set of interfaces in a subsystem. Facade defines a higher-level interface that makes the subsystem easier to use.<sup>a)</sup>

&nbsp;
## 2. Structure
#### Class diagram
![Facade_diagram](./images/Diagram_Facade.png "Facade Diagram")\
https://refactoring.guru/design-patterns/facade<sup>b)</sup>


&nbsp;
## 3. Participants
- `Facade`
    - The Facade provides convenient access to a particular part of the subsystem’s functionality. It knows where to direct the client’s request and how to operate all the moving parts.
- `Subsystem`
    - The Complex Subsystem consists of dozens of various objects. To make them all do something meaningful, you have to dive deep into the subsystem’s implementation details, such as initializing objects in the correct order and supplying them with data in the proper format.
    - Subsystem classes aren’t aware of the facade’s existence. They operate within the system and work with each other directly.


&nbsp;
## 4. Sample Code 
```c#
using System;

namespace RefactoringGuru.DesignPatterns.Facade.Conceptual
{
    public class Facade
    {
        protected Subsystem1 _subsystem1;
        protected Subsystem2 _subsystem2;

        public Facade(Subsystem1 subsystem1, Subsystem2 subsystem2)
        {
            this._subsystem1 = subsystem1;
            this._subsystem2 = subsystem2;
        }
        
        public string Operation()
        {
            string result = "Facade initializes subsystems:\n";
            result += this._subsystem1.operation1();
            result += this._subsystem2.operation1();
            result += "Facade orders subsystems to perform the action:\n";
            result += this._subsystem1.operationN();
            result += this._subsystem2.operationZ();
            return result;
        }
    }
    
    public class Subsystem1
    {
        public string operation1() { return "Subsystem1: Ready!\n"; }
        public string operationN() { return "Subsystem1: Go!\n"; }
    }
    public class Subsystem2
    {
        public string operation1() { return "Subsystem2: Get ready!\n"; }
        public string operationZ() { return "Subsystem2: Fire!\n"; }
    }

    class Client
    {
        public static void ClientCode(Facade facade)
        {
            Console.Write(facade.Operation());
        }
    }
    
    class Program
    {
        static void Main(string[] args)
        {
            Subsystem1 subsystem1 = new Subsystem1();
            Subsystem2 subsystem2 = new Subsystem2();
            Facade facade = new Facade(subsystem1, subsystem2);
            Client.ClientCode(facade);
        }
    }
}
```
https://refactoring.guru/design-patterns/facade/csharp/example#example-0<sup>c)</sup>\
Output
```
Facade initializes subsystems:
Subsystem1: Ready!
Subsystem2: Get ready!
Facade orders subsystems to perform the action:
Subsystem1: Go!
Subsystem2: Fire!
```

&nbsp;
## 5. Pros and Cons
### Pros 👍
- Easy for client to use
- Encapsulate data

### Cons 👎
- Increase coupling
- Can become an extremely complex

&nbsp;
## 6. References
#### a) Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides, _Design Patterns - Elements of Reusable Object-Oriented Software_ (Addison-Wesley, 1994), 185
#### b) https://refactoring.guru/design-patterns/facade
#### c) https://refactoring.guru/design-patterns/facade/csharp/example#example-0

