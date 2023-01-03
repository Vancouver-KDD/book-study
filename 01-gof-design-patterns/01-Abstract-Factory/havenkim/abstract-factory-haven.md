# Abstract Factory

### In one liner

If the system is required to generate multiple instances which have "quite close" similar categories, Abstract Factory can be used for flexibility of **Instance Categories**.


>The abstract factory pattern provides a way to encapsulate a group of individual factories that have a **common theme** without specifying their concrete classes. [Wikipedia](https://en.wikipedia.org/wiki/Abstract_factory_pattern)


### Pros 

- Consistency of every instance
- Super decoupled flexibility

### Cons

- Too much consistency of every single class. It is fairly limited when it comes to adding a "fairly" new category.  
- Since everything is abstracted and decoupled, access of Class-Specific features can be frustrating sometimes unless you break the layer of Abstraction.

### Sample

**Abstract Factory**
```c++
class WidgetFactory
{
public:
    virtual Text* CreateText(const std::string& label) = 0;
    virtual Image* CreateImage() = 0;
}
```
\* WidgetFactory doesn't have to be pure virtual class if the Abstract Class can be instantiated itself without subclasses.  

**Abstract Classes**
```c++
class Widget
{
public:
    Widget()
    {
        static uint32_t globalID = 0;
        globalID++;
        id = globalID;
    }
    virtual uint32_t getID() { return id; };
protected:
    uint32_t id;
}

class Text : public Widget
{
public:
    virtual Text(const std::string& label) : mLabel(label) {}
    virtual void RenderText() = 0;
protected:
    std::string mLabel;
}
class Image : public Widget
{
public:
    virtual void RenderImage() = 0;
}
```

**Concrete Class - Window Version**
```c++
class WindowText: public Text
{
public:
    virtual void RenderText()
    {
        std::cout << "WindowText " << mLabel << std::endl;
    }
}
class WindowImage: public Image
{
public:
    virtual void RenderImage()
    {
        std::cout << "WindowImage" << std::endl;
    }
}
```

**Concrete Class - Linux Version**
```c++
class LinuxText: public Text
{
public:
    virtual void RenderText()
    {
        std::cout << "LinuxText" << mLabel << std::endl;
    }
}
class LinuxImage: public Image
{
public:
    virtual void RenderImage()
    {
        std::cout << "LinuxImage" << std::endl;
    }
}
```
**Concrete Factories**
```c++
class WindowFactory: public WidgetFactory
{
public:
    virtual Text* CreateText(const std::string& label)
    {
        return new WindowText(label);
    }
    virtual Image* CreateImage()
    {
        return new WindowImage();
    }
}
class LinuxFactory: public WidgetFactory
{
public:
    virtual Text* CreateText(const std::string& label)
    {
        return new LinuxText(label);
    }
    virtual Image* CreateImage()
    {
        return new LinuxImage();
    }
}
```


now we can use ...
```c++
int main()
{
#ifdef WIN32
    WidgetFactory* factory = new WindowFactory();
#else
    WidgetFactory* factory = new LinuxFactory();
#endif
    // or...
    
    //use Factory Method pattern to generate OS dependent factory
    WidgetFactory* factory = WidgetFactory::GenerateFactory();

    factory->CreateText("Hellowwwww");
    factory->CreateText("KDD");
}
```

### IRL examples

Magnum Library - C++ Rendering
[FrameBuffer](https://github.com/mosra/magnum/blob/master/src/Magnum/GL/AbstractFramebuffer.cpp)
[BufferState](https://github.com/mosra/magnum/blob/master/src/Magnum/GL/Implementation/BufferState.cpp)
- To support various rendering library & multiple platforms, it needs to abstract each layer of every components into several Abstract & Concrete Objects. And at the end, depends on the configuration it uses Abstract Factory & Factory Method to instantiate Concrete Objects.