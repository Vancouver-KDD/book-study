# Factory Method

### Resources

Factory VS Factory Method VS Abstract Factory


[Diagrams define different versions of Animal factories](https://stackoverflow.com/a/65331902)

[Apple and Orange factory in code](https://stackoverflow.com/a/13030163)

> The main difference between Abstract Factory and Factory Method is that Abstract Factory is implemented by **Composition**; but Factory Method is implemented by **Inheritance**.
>
> **Factory Method**
> * Creates one type of Product, many times.
> * The type can be differ depends on ConcreteFactory
> * Exposes a method to the client for creating the object 
>
> **Abstract Factory**
> * Creates **families** of various types of Products.
> * Exposes a family of related objects which may consist of these Factory methods.

[Reference](https://stackoverflow.com/questions/5739611/what-are-the-differences-between-abstract-factory-and-factory-design-patterns)


### In One Liner

Abstract the type of Product I am going to make. I don't care how it is generated and which type it is but just give me that product.

### Pros 

- Hides everything about Product and how they are being generated.

### Cons

- If inheritance & polymorphism is not a concern (depends on type of Products and intends), adding Factory Method to your class is overkill.

### Sample

WidgetFactory from AbstractFactory example could have FactoryMethod  itself to avoid exposing detailed Factory Generation.
**WidgetFactory.h**
```c++
class WidgetFactory
{
public:
    static WidgetFactory* CreateWidgetFactory();
    virtual Text* CreateText(const std::string& label) = 0;
    virtual Image* CreateImage() = 0;
}
```

**WidgetFactory.cpp**
```c++
#include "WidgetFactory.h"
#include "WindowFactory.h"
#include "LinuxFactory.h"

WidgetFactory* WidgetFactory::CreateWidgetFactory(
{
#ifdef WIN32
    return new WindowFactory();
#else
    return new LinuxFactory();
#endif
}
```

The difference in between **Factory** and **FactoryMethod** in this example is that we don't know which WidgetFactory it is. 

<br/>
Factory Pattern will be 

**WidgetFactory.cpp**
```c++
#include "WidgetFactory.h"
#include "WindowFactory.h"
#include "LinuxFactory.h"

WindowFactory* WidgetFactory::CreateWindowFactory(
{
    return new WindowFactory();
}

LinuxFactory* WidgetFactory::CreateLinuxFactory(
{
    return new LinuxFactory();
}
```

You know what you are going to make and you have full control over it.