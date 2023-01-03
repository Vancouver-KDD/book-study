#  Adapter

### In One Liner

Provide extended interfaces but which are completely different from existing interfaces.

The distict difference from **decorator** is that the decorator provides new interfaces without modifying the existing interface (as a component). But adapter introduces new interfaces by creating a new connection in between A-B classes.

But that does not mean decorator is appliable in all circumstances since each decorator should have matching and quite simple enough interfaces to leverage its characteristics. 

If making a decorator interface is a lot more work or impossible, adapter will do better.

### Pros 

- Provide same access to existing objects or classes even though they cannot do it or impossible to modify them. (Wrapper)

### Cons

- As usual, restricting interfaces will introduce a stricted structure changes when it comes to changing subclasses 

### Sample

**Class Adapter** and **Object Adapter** provide different aspects. 

**Class Adapter**

- Provide new interfaces by inheritance and method overriding.
- It cannot provide subclasses' interfaces since it inherits super class of them.

**Object Adapter**

- Provide new interfaces by object composition and referencing.
- It can provide various behaviors by having subclasses.
- It restricts constructor of adapter class since it should have reference of adaptee instance.


Shape class is what we desire to use. But TextView cannot provide any relating interfaces at this moment.

```c++
class Shape
{
public:
    Shape();
    virtual void BoundingBox(Point& topLeft, Point& bottomRight);
}

class TextView
{
public:
    TextView();
    void GetOrigin(double& x, double& y);
    void GetResolution(double& width, double& height);
    virtual bool IsEmpty();
}
```

TextShape Adapter (Wrapper) class will provide according interfaces. But it only exposes Shape interfaces and hides TextView inheritance.

```c++
class TextShape: public Shape, private TextView
{
public:
    TextShape(TextView* textView);
    virtual void BoundingBox(Point& topLeft, Point& bottomRight);
private:
    TextView* _pTextView;
}

void TextShape::BoundingBox(Point& topLeft, Point& bottomRight)
{
    double x, y;
    _pTextView->GetOrigin(x, y);

    double width, height;
    _pTextView->GetResolution(width, height);

    topLeft.x = x;
    topRight.y = y;
    bottomRight.x = x + width;
    bottomRight.y = y + height;
}
```

