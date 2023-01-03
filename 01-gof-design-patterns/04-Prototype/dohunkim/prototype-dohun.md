```c#
using System;

namespace D4_Prototype
{
    /// <summary>
    /// Prototype Design Pattern
    /// </summary>
    public class Program
    {
        public static void Main(string[] args)
        {
            // Create two instances and clone each
            var p1 = new ConcretePrototype1("I");
            var c1 = (ConcretePrototype1)p1.Clone();
            Console.WriteLine("Cloned: {0}", c1.Id);
            
            var p2 = new ConcretePrototype2("II");
            var c2 = (ConcretePrototype2)p2.Clone();
            Console.WriteLine("Cloned: {0}", c2.Id);
            
            // Wait for user
            Console.ReadKey();
        }
    }
    
    /// <summary>
    /// The 'Prototype' abstract class
    /// </summary>
    public abstract class Prototype
    {
        // Constructor
        public Prototype(string id)
        {
            Id = id;
        }
        // Gets id
        public string Id { get; }
        public abstract Prototype Clone();
    }
    
    /// <summary>
    /// A 'ConcretePrototype' class 
    /// </summary>
    public class ConcretePrototype1 : Prototype
    {
        // Constructor
        public ConcretePrototype1(string id)
            : base(id)
        {
        }
        // Returns a shallow copy
        public override Prototype Clone()
        {
            return (Prototype)MemberwiseClone();
        }
    }
    
    /// <summary>
    /// A 'ConcretePrototype' class 
    /// </summary>
    public class ConcretePrototype2 : Prototype
    {
        // Constructor
        public ConcretePrototype2(string id)
            : base(id)
        {
        }
        // Returns a shallow copy
        public override Prototype Clone()
        {
            return (Prototype)MemberwiseClone();
        }
    }
}
```