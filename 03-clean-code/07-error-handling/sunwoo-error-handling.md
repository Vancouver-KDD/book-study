# Error Handling
개발자는 error가 발생했을때, 코드가 어떤 행동을 해야하는지에 대한 정의를 해야한다.
하지만 코드 베이스가 많은 error handling에 의해서 그 의도를 파악하기 힘들어진다.
이 챕터에서는 어떻게 코드 베이스에 관해 clean하고 robust한 error handling을 사용하는지 알아보자.

## Use Exceptions Rather Than Returns Codes
다음과 같이 error code를 리턴하게 된다면, error handling과 logic이 한 곳에서 이루어지게 된다.
이럴 경우 코드의 의도를 파악하기 힘들어지고, 또한 method가 한 개 이상의 일을 하게된다.
```Java
public class DeviceController { 
    ... 
    public void sendShutDown() { 
        DeviceHandle handle = getHandle(DEV1); 
        // Check the state of the device 
        if (handle != DeviceHandle.INVALID) { 
            // Save the device status to the record field 
            retrieveDeviceRecord(handle); 
            // If not suspended, shut down 
            if (record.getStatus() != DEVICE_SUSPENDED) {
                 pauseDevice(handle); 
                 clearDeviceWorkQueue(handle); 
                 closeDevice(handle); 
            } else { 
                logger.log("Device suspended. Unable to shut down"); 
            } 
        } else { 
            logger.log("Invalid handle for: " + DEV1.toString()); 
        } 
    } 
    ...
}
```

Exception을 throw할 경우, error를 핸들링하는 부분과 로직을 분리 시킬수 있고,
그러므로써 error handling과 로직의 의도를 파악하기 더 쉬워진다.
```Java
public class DeviceController { 
    ... 
    public void sendShutDown() { 
        try { 
            tryToShutDown(); 
        } catch (DeviceShutDownError e) { 
            logger.log(e); 
        } 
    }

    private void tryToShutDown() throws DeviceShutDownError { 
        DeviceHandle handle = getHandle(DEV1); 
        DeviceRecord record = retrieveDeviceRecord(handle); 
        pauseDevice(handle); 
        clearDeviceWorkQueue(handle); 
        closeDevice(handle); 
    } 
    
    private DeviceHandle getHandle(DeviceID id) { 
        ... 
        throw new DeviceShutDownError("Invalid handle for: " + id.toString()); 
        ... 
    } 
    ...
}
```

## Write Your Try-Catch-Finally Statement First
가장 exception의 강력한 부분은 try 블록에 있는 프로그램 전체가 catch의 scope범위가 된다는 것이다.
이것은 error code를 리턴하는 것과는 달리, 깊은 method에서도 exception을 catch해, high abstraction level에서 핸들링이 가능하다는 것이다.

코드를 작성할때, try-catch-finally를 먼저 작성을 한다면, method의 성공유무를 떠나 유저가 어떠한 method의 결과값를 원하는지 파악하기 쉬어진다.

## Use Unchecked Exceptions
checked exception이란 개발자가 필수적으로 핸들링을 필요로 하는 exception을 뜻한다.
unchecked exception은 개발자가 선택적으로 핸들링하는 exception을 뜻한다.

checked exception은 자바 커뮤니티에서 많은 논쟁이 있었지만,
필자는 open/closed principle에 위반하고, 또한 low level의 function의 변경은 많은 top level의 코드의 변경을 가져오기 때문에, 많은 cost가 든다고 설명하고 있다.

## Provide Context with Exceptions
exception에는 error가 일어난 위치와 error message에 operation과 이유를 정확히 명시하자.

## Define Exception Classes in Terms of a Caller's Needs
exception class를 정의할때 가장 고려해야 하는 부분은 어떻게 그것을 catch할 것인가에 대한 질문이다.
가장 좋은 방법중 하나는 예를 들어 3rd party code가 던지는 exception들을 boundary에서 catch한후 하나의 custom exception을 client code에 던지는 것이다.

```Java
ACMEPort port = new ACMEPort(12); 
try { 
    port.open(); 
} catch (DeviceResponseException e) { 
    reportPortError(e); 
    logger.log("Device response exception", e); 
} catch (ATM1212UnlockedException e) { 
    reportPortError(e); 
    logger.log("Unlock exception", e);
} catch (GMXError e) { 
    reportPortError(e); 
    logger.log("Device response exception"); 
} finally { … }
```
이런 식으로 3rd party library `ACMEPort`가 던지는 것을 client에서 catch한다면 필요 이상으로 `ACMEPort`를 client에 노출하게 된다.

```Java
LocalPort port = new LocalPort(12); 
try { 
    port.open(); 
} catch (PortDeviceFailure e) { 
    reportError(e); 
    logger.log(e.getMessage(), e); 
} finally { … }
```
`LocalPort`로 encapsulate하고, 정의된 `PortDeviceFailure`를 새롭게 던진다면, client쪽에 3rd party를 감출수 있다.

## Define the Normal Flow
Error를 던져서 handling하기 전에, normal flow를 먼저 정의하는 것이 좋다.
예를 들어,
```Java
try { 
    MealExpenses expenses = expenseReportDAO.getMeals(employee.getID()); 
    m_total += expenses.getTotal();
} catch(MealExpensesNotFound e) { 
    m_total += getMealPerDiem();
}
```
이렇게 error를 던져서 special case를 핸들링하는 것보다, 

```Java
MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
m_total += expenses.getTotal();

public class PerDiemMealExpenses implements MealExpenses { 
    public int getTotal() { 
        // return the per diem default 
    }
}
```
이렇게 특별한 케이스의 class를 정의하여서 error handling 없이 special case를 핸들링하는 것이 더 바람직하다.

## Don't Return Null
어떠한 method가 Null을 리턴한다면, caller에서는 object가 Null인지 항상 체크를 해야한다. 이러한 코드가 많아진다면, 코드는 clutter해지고 또 많은 error를 가질수 있는 risk를 내포하고 있다.
Null을 리턴하는 것보다는 NullException을 throw하거나 혹은 special case의 오브젝트를 리턴한다.

## Don't Pass Null
null을 method에 파라매터로 넘기는 것은 null을 리턴하는 것보다 더 위험하다. 이것은 `NullPointerException`을 던지게 되는 주요한 원인이 된다. 가장 좋은 방법은 null을 default로 넘기는 것은 금지하는 것이고, 또한 assert를 통해서, null을 operation전에 catch하는 것이다.

## Conclusion
error handling에 가장 중요한 것은 handling 하는 것과 method의 intent를 서로 분리시키는 것이다. 분리시킴으로써 서로의 의도를 독립적으로 관리할수 있고, 코드의 maintainability를 향상시킬수 있다.