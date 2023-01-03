# Decorator

## Prerequisite

This documents assume that
 - You have basic knowledge of c#
 - You know how to read class diagram

&nbsp;
## 1. Intent

Attach additional responsibilities to an object dynamically. Decorators provide a flexible alternative to subclassing for extending functionality.<sup>a)</sup>


&nbsp;
## 2. Structure
#### Class diagram
![Decorator_diagram](./images/Diagram_Decorator.png "Decorator Diagram")\
https://refactoring.guru/design-patterns/decorator<sup>b)</sup>


&nbsp;
## 3. Participants

- `Component`
    - The Component declares the common interface for both wrappers and wrapped objects.
- `Concrete Component`
    - Concrete Component is a class of objects being wrapped. It defines the basic behavior, which can be altered by decorators.
- `Base Decorator`
    - The Base Decorator class has a field for referencing a wrapped object. The field’s type should be declared as the component interface so it can contain both concrete components and decorators. The base decorator delegates all operations to the wrapped object.
- `Concrete Decorators`
    - Concrete Decorators define extra behaviors that can be added to components dynamically. Concrete decorators override methods of the base decorator and execute their behavior either before or after calling the parent method.
- `Client`
    - The Client can wrap components in multiple layers of decorators, as long as it works with all objects via the component interface.


https://refactoring.guru/design-patterns/decorator<sup>c)</sup>


&nbsp;
## 4. Sample Code 
`Decorator`
```c#
using System;

namespace Decorator
{
    public abstract class Component
    {
        public abstract string Execute();
    }
    class ConcreteComponent : Component
    {
        public override string Execute()
        {
            return "ConcreteComponent";
        }
    }
    
    public class Client
    {
        public void ClientCode(Component component)
        {
            Console.WriteLine("Client Code Result: " + component.Execute());
        }
    }

    
    abstract class Decorator : Component
    {
        protected Component _component;
        public Decorator(Component component)
        {
            _component = component;
        }
        public override string Execute()
        {
            return _component.Execute();
        }
    }
    
    class ConcreteDecoratorA : Decorator
    {
        public ConcreteDecoratorA(Component comp) : base(comp) { }
        public override string Execute()
        {
            return $"ConcreteDecoratorA({base.Execute()})";
        }
    }
    
    class ConcreteDecoratorB : Decorator
    {
        public ConcreteDecoratorB(Component comp) : base(comp) { }
        public override string Execute()
        {
            return $"ConcreteDecoratorB({base.Execute()})";
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            Client client = new Client();

            var simple = new ConcreteComponent();
            Console.WriteLine("Client: I get a simple component:");
            client.ClientCode(simple);
            Console.WriteLine();
            
            ConcreteDecoratorA decorator1 = new ConcreteDecoratorA(simple);
            ConcreteDecoratorB decorator2 = new ConcreteDecoratorB(decorator1);
            Console.WriteLine("Client: Now I've got a decorated component:");
            client.ClientCode(decorator2);
            // client.ClientCode(new ConcreteDecoratorB(new ConcreteDecoratorA(new ConcreteConponent())));
        }
    }
}
```

Output
```
Client: I get a simple component:
Client Code Result: ConcreteComponent

Client: Now I've got a decorated component:
Client Code Result: ConcreteDecoratorB(ConcreteDecoratorA(ConcreteComponent))
```

`Real-world example`
```c#
using System;

namespace Decorator_Example
{
    public abstract class Component { public abstract string SushiRice(); }
    class ConcreteComponent : Component {
        public override string SushiRice() {
            return "Rice";
        }
    }
    abstract class Decorator : Component {
        protected Component _component;
        public Decorator(Component component) {
            _component = component;
        }
        public override string SushiRice() {
            return _component.SushiRice();
        }
    }
    class SalmonOnTop : Decorator {
        public SalmonOnTop(Component comp) : base(comp) { }
        public override string SushiRice() {
            return $"SalmonOnTop({base.SushiRice()})";
        }
    }
    class Wasabi : Decorator {
        public Wasabi(Component comp) : base(comp) { }
        public override string SushiRice() {
            return $"Wasabi({base.SushiRice()}) - Spicy!";
        }
    }
    public class Client {
        public void ClientCode(Component component) {
            Console.WriteLine("Client Code Result: " + component.SushiRice());
        }
    }
    class Program {
        static void Main(string[] args) {
            Client client = new Client();
            //var sushiRice = new ConcreteComponent();
            //SalmonOnTop salmonOnTop = new SalmonOnTop(sushiRice);
            //Wasabi wasabi = new Wasabi(salmonOnTop);
            //client.ClientCode(wasabi);
            client.ClientCode(new Wasabi(new SalmonOnTop(new ConcreteComponent())));
        }
    }
}
```

Output
```
Client Code Result: Wasabi(SalmonOnTop(Rice)) - Spicy!
```

&nbsp;
## 5. Pros and Cons
### Pros 👍
- No need to modify existing classes to add functionality.
- Not only overide existing method, but also add extra methods.

### Cons 👎
- Hard to figure out the whole structure. eg) where does this class inherited from?
- Increase complexity when there are a number of methods that have to be implemented.


&nbsp;
## 6. Related Patterns
### Adaptor
- Adapter changes the interface of an existing object, while Decorator enhances an object without changing its interface. In addition, Decorator supports recursive composition, which isn’t possible when you use Adapter.

&nbsp;
## 7. References
#### a) Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides, _Design Patterns - Elements of Reusable Object-Oriented Software_ (Addison-Wesley, 1994), 175
#### b), c) https://refactoring.guru/design-patterns/decorator
