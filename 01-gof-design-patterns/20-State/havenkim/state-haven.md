# State

### In One Liner

Turn large switch statements or if statements into Objects!

In depth, when the behaviors of the class depend on instance attributes or other composited classes the behavior decision will be made by state object. 

### Pros 

- Open/Closed Principle.
- Delegate multiple "state-dependent" behaviors into concrete objects which increase flexibility and maintainability.

### Cons

- Overkill if there are not many states because every state will own a class and they should be maintained.

### Example

[Inspiration](https://www.geeksforgeeks.org/state-design-pattern/)
```c++
#pragma once
#include <iostream>

class AlertState
{
public:
    virtual void alert(AlertStateContext* pContext) = 0;
};

class DefaultAlertState : public AlertState
{
public:
    void alert(AlertStateContext* pContext)
    {
        std::cout << "Sound alarm!" << std::endl;
    }
};
class VibrationAlertState : public AlertState
{
public:
    void alert(AlertStateContext* pContext)
    {
        std::cout << "Vibrating!" << std::endl;
    }
};
class SilentAlertState : public AlertState
{
public:
    void alert(AlertStateContext* pContext)
    {
        std::cout << "Slight notification bar..." << std::endl;
    }
};
```

```c++
#pragma once
#include "AlertState.h"
#include <iostream>

class AlertStateContext
{
public:
    AlertStateContext()
    {
        mpCurrentState = new VibrationAlertState();
    }
    
    void setAlarmState(AlertState* mState)
    {
        if (mpCurrentState != NULL)
            delete mpCurrentState;
        mpCurrentState = state;
    }

    void eventOccured()
    {
        std::cout << "Alert invoked" << std::endl;
        mpCurrentState->alert(this);
    }
private:
    AlertState* mpCurrentState;
};
```

```c++
#include "AlertState.h"
#include "AlertStateContext.h"
int main()
{
    AlertStateContext* newContext = new AlertStateContext();
    newContext->alert();
    newContext->setAlarmState(new DefaultAlertState());   
    newContext->alert();
    newContext->setAlarmState(new SilentAlertState());   
    newContext->alert();
    newContext->setAlarmState(new VibrationAlertState());   
    newContext->alert();
};
```