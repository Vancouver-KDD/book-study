# Chain of Responsibility

### In One Liner

Propagate handling of the data or action into a sequentially chained handlers.

Each handle can either propagate the data to the next handler or just exit right away. 

[Wikipedia](https://en.wikipedia.org/wiki/Chain-of-responsibility_pattern)
<br/>
[Refactoring.guru](https://refactoring.guru/design-patterns/chain-of-responsibility)

![https://refactoring.guru/design-patterns/chain-of-responsibility](./images/cor-refactoringguru.png)

### KeyPoint

Chain of Responsibility is structually similar to the decorator pattern. But the key difference is in the order of handlers.

CoR's handlers are individuals themselves so that if the pipeline goes directly into the handler, the flow starts from the handler without any dependent problems. And with CoR, request can exit at anytime if the handler does not want further handlings.

Meanwhile, decorators are usually composited with a main component and by a sequence, call order is respected and must be done in a sequence. Also it cannot allow halt of request in the middle of pipeline. 

### Pros 

- Request handling order is highly flexible and customizable thanks to Open/Closed Principle and Single Responsibility Principle. Handlers can be attached or detached at any time. 

### Cons

- If the handler chain is not configured well or by any chance of missing handlers, there is a chance of losing a request unhandled since each handler does not know about relationships and the scope outside of each handler.

### Example

Logger example from [Wikipedia](https://en.wikipedia.org/wiki/Chain-of-responsibility_pattern)

This logger system makes a **continuously handled chain** of log message with multiple logging channel. Each handler does its job and pass the request to the next handler in relationship.


### IRL Example

SDL2 library provides cross-platform interface among other OS environment.

When the SDL Application takes user input (keyboard, mouse, etc.), it can propagate the handling of InputEvent toward Abstraction level, UI level, System level, or etc depends on how many input layers are needed to be handled. 

```c++
class AppSDL: public Platform::Application 
{
public:
    void mousePressEvent(MouseEvent& event) override;
    void mouseMoveEvent(MouseEvent& event) override;
};

void AppSDL::mousePressEvent(MouseEvent& event)
{
    app.OnInputEvent(&event, App::MouseDown);
}
void AppSDL::mouseMoveEvent(MouseEvent& event)
{
    app.OnInputEvent(&event, App::MouseMove);
}
```

```c++
class InputManager
{
public:
    void OnInputEvent(void*, int);
};

void InputManager::OnInputEvent(void* pInputStruct, int psEvent)
{
    switch (psEvent)
    {
    case App::MouseMove:
    {
        // If UIHandler should handle the event, exit right away.
        if (UIHandler::I().OnMouseMove(pInputStruct))
            break;

        SystemInputHandler::I().OnMouseMove(pInputStruct);
        break; 
    }
    case App::MouseDown:
    {
        // If UIHandler should handle the event, exit right away.
        if (UIHandler::I().OnMouseDown(pInputStruct))
            break;

        SystemInputHandler::I().OnMouseDown(pInputStruct);
        break;
    }
}

class UIHandler
{
public:
    bool OnMouseMove(void* event);  //Sdl2Application::MouseEvent
    bool OnMouseDown(void* event);  //Sdl2Application::MouseEvent
};
class SystemInputHandler
{
public:
    bool OnMouseMove(void* event);  //Sdl2Application::MouseEvent
    bool OnMouseDown(void* event);  //Sdl2Application::MouseEvent
};
```