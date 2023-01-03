#  Decorator (Wrapper)

### In One Liner

Provide extra behaviors or responsibility of the object at runtime. 

![Decorator](./images/decorator.png)
[Reference](https://refactoring.guru/design-patterns/decorator)

Both Adapter and Decorator are so called wrapper, but Adapter could be described as "Hugging wrapper" while Decorator could be "Bottom-Up Stack wrapper".

### Pros 

- Decorator is a good way to provide features in dynamic ways with leave external codes untouched.
- Inheritance and Decorator have similarity in terms of its abilty opens up extra functionalities, Decorator can solve the problem if inheritance increase complexity of the codebase.
- Every decorator takes minimum responsibility.

### Cons

- You might need to do **over-generalizing** since decorators only can provide its features by **Component**'s interface. If this is not the case, decorator cannot be used.
- Depends on coupling or relational problems of ConcreteDecorators, calling order of decorator can introduce spaguetti debugging problem.

### Sample

**Build your own pizza** is a good example of decorator
[https://stackoverflow.com/questions/2707401/understand-the-decorator-pattern-with-a-real-world-example](https://stackoverflow.com/questions/2707401/understand-the-decorator-pattern-with-a-real-world-example)

ToppingDecorator will have same interface for retrieving price. But each topping is standalone itself to cost in total.

```c++

class BasePizza
{
public:
    BasePizza() : mPrice(6.99) {}
    virtual float getPrice() { return mPrice; }
protected:
    float mPrice;
};

class ToppingPizza : public BasePizza
{
public:
    ToppingPizza(BasePizza& pizza, float price) : mPizza(pizza)
    {
        mPrice = mPizza.getPrice() + price;
    }
    virtual float getPrice() 
    {
        return mPrice;
    }
protected:
    BasePizza mPizza;
};
```

```c++
class ThinCrust : public ToppingPizza
{
public:
    ThinCrust(BasePizza& pizza) :
        ToppingPizza(pizza, 1.5)
    { }
};
class CheeseCrust : public ToppingPizza
{
public:
    CheeseCrust(BasePizza& pizza) :
        ToppingPizza(pizza, 3.5)
    { }
};
class PepperoniAddon : public ToppingPizza
{
public:
    PepperoniAddon(BasePizza& pizza) :
        ToppingPizza(pizza, 1.0)
        { }
};
class OnionAddon : public ToppingPizza
{
public:
    OnionAddon(BasePizza& pizza) :
        ToppingPizza(pizza, 0.5)
    { }
};
class SausageAddon : public ToppingPizza
{
public:
    SausageAddon(BasePizza& pizza) :
        ToppingPizza(pizza, 1.5)
    { }
};


#include <iostream>
int main()
{
    BasePizza pizza;
    std::cout << pizza.getPrice() << std::endl;
    pizza = SausageAddon(pizza);
    std::cout << pizza.getPrice() << std::endl;
    pizza = OnionAddon(pizza);
    std::cout << pizza.getPrice() << std::endl;
    pizza = PepperoniAddon(pizza);

    std::cout << pizza.getPrice() << std::endl;
}
```