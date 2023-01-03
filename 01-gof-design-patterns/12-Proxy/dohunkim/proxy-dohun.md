# Proxy

## Prerequisite

This documents assume that
 - You have basic knowledge of c#
 - You know how to read class diagram

&nbsp;
## 1. Intent

Provide a surrogate or placeholder for another object to control access to it.<sup>a)</sup>

&nbsp;
## 2. Structure
#### Class diagram
![Proxy_diagram](./images/Diagram_Proxy.png "Proxy")\
https://refactoring.guru/design-patterns/proxy<sup>b)</sup>


&nbsp;
## 3. Participants
- `Service Interface`
    - The Service Interface declares the interface of the Service. The proxy must follow this interface to be able to disguise itself as a service object.
- `Service`
    - The Service is a class that provides some useful business logic.
- `Proxy`
    - The Proxy class has a reference field that points to a service object. After the proxy finishes its processing (e.g., lazy initialization, logging, access control, caching, etc.), it passes the request to the service object.
    - Usually, proxies manage the full lifecycle of their service objects.

https://refactoring.guru/design-patterns/proxy<sup>c)</sup>

&nbsp;
## 4. Sample Code 
```c#
using System;

namespace RefactoringGuru.DesignPatterns.Proxy.Conceptual
{
    public interface ISubject
    {
        void Request();
    }
    
    class RealSubject : ISubject
    {
        public void Request()
        {
            Console.WriteLine("RealSubject: Handling Request.");
        }
    }
    
    class Proxy : ISubject
    {
        private RealSubject _realSubject;
        
        public Proxy(RealSubject realSubject)
        {
            this._realSubject = realSubject;
        }
        
        public void Request()
        {
            if (this.CheckAccess())
            {
                this._realSubject.Request();
                this.LogAccess();
            }
        }
        
        public bool CheckAccess()
        {
            Console.WriteLine("Proxy: Checking access prior to firing a real request.");
            return true;
        }
        
        public void LogAccess()
        {
            Console.WriteLine("Proxy: Logging the time of request.");
        }
    }
    
    public class Client
    {
        public void ClientCode(ISubject subject)
        {
            // ...
            subject.Request();
            // ...
        }
    }
    
    class Program
    {
        static void Main(string[] args)
        {
            Client client = new Client();
            
            Console.WriteLine("Client: Executing the client code with a real subject:");
            RealSubject realSubject = new RealSubject();
            client.ClientCode(realSubject);

            Console.WriteLine();

            Console.WriteLine("Client: Executing the same client code with a proxy:");
            Proxy proxy = new Proxy(realSubject);
            client.ClientCode(proxy);
        }
    }
}
```
https://refactoring.guru/design-patterns/proxy/csharp/example#example-0<sup>d)</sup>\
Output
```
Client: Executing the client code with a real subject:
RealSubject: Handling Request.

Client: Executing the same client code with a proxy:
Proxy: Checking access prior to firing a real request.
RealSubject: Handling Request.
Proxy: Logging the time of request.
```

&nbsp;
## 5. Pros and Cons
### Pros 👍
- Control and manage the lifecycle of the service object without clients knowing about it
- Faster response when there is permission issue

### Cons 👎
- Response from the service might get delayed if there is any problem with proxy class itself
- Code becomes more complecated

&nbsp;
## 6. References
#### a) Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides, _Design Patterns - Elements of Reusable Object-Oriented Software_ (Addison-Wesley, 1994), 207
#### b), c) https://refactoring.guru/design-patterns/proxy
#### d) https://refactoring.guru/design-patterns/proxy/csharp/example#example-0

