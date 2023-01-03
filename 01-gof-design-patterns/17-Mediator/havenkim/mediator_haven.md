# Mediator

### In One Liner

Create a **bidirectional** channel in between multiple objects.

Each object communicates only through mediator.

![](./images/mediator.png)
[Reference](https://refactoring.guru/design-patterns/mediator)

### Point

Facade pattern creates an entrance for the subsystems. But it creates unidirectional channels for subsystems and they do not know the existence of Facade object.

On the other hand, Mediator object allows subsystems (or just objects) to send messages to the mediator object enabling communication protocol in between other objects.

Due to the main purpose of the mediator, it is easy to make a ginormous fat object for lots of handling blocks or callbacks (or event notification listener)

### Pros 

- Abstract out communication and reference in between object group.
- Keep Single Responsibility Principle and Open & Closed Principle.

### Cons

- Very easy to create a fat god.

### Example

[refactoring.guru example](https://refactoring.guru/design-patterns/mediator/cpp/example)

```c++

class BaseComponent;
class Mediator 
{
public:
    virtual void notify(BaseComponent* sender, const std::string& message) const = 0;
};
class BaseComponent
{
public:
    BaseComponent(Mediator* mediator = NULL)
        : mpRefMediator(mediator)
    {
    }
protected:
    Mediator* mpRefMediator;
};
```

```c++
class UIEventManager: public Mediator
{
public:
    UIEventManager()
    {
        mpRenderManager = new RenderManager();
        mpNotificationManager = new NotificationManager();
    }
    ~UIEventManager()
    {
        delete mpRenderManager;
        delete mpNotificationManager;
    }

    // Handles channel message and contact other related objects
    void notify(BaseComponent* sender, const std::string& message) const
    {
        if (message.find("MouseDown") != std::string::npos) 
        {
            std::cout << "Event : Mouse Down" << std::endl;
            mpNotificationManager->InsertNotification("Button Mouse Down");
        }
        else if (message.find("MouseUp") != std::string::npos) 
        {
            std::cout << "Event : Mouse Up" << std::endl;
            mpNotificationManager->InsertNotification("Button Mouse Up");
        }
        else if (message.find("UpdateRender") != std::string::npos) 
        {
            std::cout << "Event : Render Update" << std::endl;
            mpNotificationManager->InsertNotification("Render Update");
            mpRenderManager->SetUpdate(true);
        }
    }
private:
    NotificationManager* mpNotificationManager;
    RenderManager* mpRenderManager;
};

class Button : public BaseComponent
{
public:
    void OnMouseDown()
    {
        mpRefMediator->notify(this, "MouseDown");
    }
    void OnMouseUp()
    {
        mpRefMediator->notify(this, "MouseUp");
    }
};
class ImageButton : public BaseComponent
{
public:
    void OnTextureUpdate()
    {
        mpRefMediator->notift(this, "UpdateRender");
    }    
};
class NotificationManager
{
public:
    void InsertNotification(const std::string& message)
    {
        std::cout << "Notification Pop : " << message << std::end;
    }
};

int main()
{
    Mediator mediator = new UIEventManager();
    Button* buttonA = new Button(mediator);
    ImageButton* imgButton = new ImageButton(mediator);

    buttonA->OnMouseDown();
    buttonA->OnMouseUp();

    imgButton->OnTextureUpdate();
}
```
