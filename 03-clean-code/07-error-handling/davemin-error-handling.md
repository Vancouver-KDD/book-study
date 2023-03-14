## 7. Error Handling

Error handling is important, but if it obscures logic, it’s wrong.
In this chapter I’ll outline a number of techniques and considerations that you can use to write code that is both clean and robust—code that handles errors with grace and style.

### Use Exceptions Rather Than Return Codes

예전에는 handling and reporting errors were limited
다음이 그 예제


```java
// DeviceController.java
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
        logger.log("Device suspended.  Unable to shut down");
      }
    } else {
      logger.log("Invalid handle for: " + DEV1.toString());
    }
  }
  ...
}
```
과거 위와 같은 에러 핸들링의 경우, caller가 반드시 에러에 체크를 해주어야 한다.

> 문제점
1. caller가 잊어버리기 쉽다.
2. calling 코드가 지저분해 질 수 있다.
3. 에러핸들링으로 인해 로직을 모호하게 만들 수 있다.


아래는 exception을 통한 에러핸들이다  
```java
//DeviceController.java (with exceptions)
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

> 개선사항
1. a matter of aesthetics
2. 장치종료와 에러처리의 분리화


### Write Your Try-Catch-Finally Statement First

no matter what happens in the try, 일관성 있게 Catch블럭은 프로그램을 반드시 떠나야 합니다.
이러한 이유로 exceptions를 던질 수 있는 코드를 작성할 때는 Try-catch-finally 문으로 시작하는 것이 좋습니다. 이것은 Try 중에 실행되는 코드에 no matter what goes wrong 해당 코드의 users들이 무엇을 기대해야 하는지 정의하는 데 도움이 됩니다.

```java
@Test(expected = StorageException.class)
  public void retrieveSectionShouldThrowOnInvalidFileName() {
    sectionStore.retrieveSection("invalid - file");
  }

//The test drives us to create this stub:

public List<RecordedGrip> retrieveSection(String sectionName) {
  // dummy return until we have a real implementation
  return new ArrayList<RecordedGrip>();
}

// 위의 코드는 exception을 던지지 않는다.

//아래와 같이 exception을 던지게할 수 있다.
public List<RecordedGrip> retrieveSection(String sectionName) {
  try {
    FileInputStream stream = new FileInputStream(sectionName);
    stream.close();
  } catch (FileNotFoundException e) {
    throw new StorageException("retrieval error”, e);
  }
  return new ArrayList<RecordedGrip>();
}

//

```

### Use Unchecked Exceptions

checked Exceptions의 경우 hierarchical 구조에 있어서 연쇄적으로 해당 펑션을 사용하는 다른 계층에 있는 코드들도 a throw clause를 추가해야 하는 dependency 즉 비용을 초래한다.
예를 들어, lower-level의 펑션을 수정해서 in such a way that it must throw an exception로 하게되면, 해당 펑션을 dependent하는 모든 펑션도 a throw clause를 연쇄적으로 작성해야하는 work 발생하게 되는 비용을 발생시킨다.

### Provide Context with Exceptions
 
Create informative error messages and pass them along with your exceptions. Mention the operation that failed and the type of failure.
If you are logging in to your application, pass along enough information to be able to log the error in your catch.

### Deﬁne Exception Classes in Terms of a Caller’s Needs
Error를 분류하는 방버은 여러가지가 있겠습니다. 소스에 따라 한가지 혹은 여러가지 component에 의해서 인지? 아니면 device failures, network failures, or programming errors?
예외 클래스 정의 시, 가장 중요한 것은
> exception classes 어떻게 포착되는가? 가 가장 중요한 요소이다.

- poor exception classiﬁcation 케이스
```java
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
    } finally {
      …
    }
```
와우! 모든 exception 케이스를 잡으려했네...

그러나 대부분의 예외 처리상황에서 우리가 하는 작업은 relatively standard regardless of the actual cause.
호출하는 API를 wrapping하고 공통적인 standard 유형을 반환하므로써 코드를 반환할 수 있다. 아래와 같이

```java
public class LocalPort {
  private ACMEPort innerPort;
  public LocalPort(int portNumber) {
    innerPort = new ACMEPort(portNumber);
  }
  public void open() {
    try {
      innerPort.open();
    } catch (DeviceResponseException e) {
      throw new PortDeviceFailure(e);
    } catch (ATM1212UnlockedException e) {
      throw new PortDeviceFailure(e);
    } catch (GMXError e) {
      throw new PortDeviceFailure(e);
    }
  }
  …
}
```
When you wrap a third-party API, 가장 좋을 수 있는게
의존성도 최소화하고
쉽게 a third-party 를 테스트 할 수 있고,
depenancy를 줄일 수 있음

종종 single exception class는 특정한 경우에 괜찮습니다. 이때 exception과 함께 전송된 정보로 오류를 구별할 수 있습니다.
한 exception를 포착하고 다른 exception가 치나치도록 하는 경우에만 다른 exception 클래스를 사용하십시오.

### Deﬁne the Normal Flow

아래예시는 식사비가 지출되면 전체의 일부가 되고. 그렇지 않은 경우, 직원은 그날의 일일 식사량을 받는다 exception이 논리를 혼란스럽게 한다.
```java
try {
  MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
  m_total += expenses.getTotal();
} catch(MealExpensesNotFound e) {
  m_total += getMealPerDiem();
}
```
그러므로 exception과 같은 예외처리를 필요없다는 줄여나가는 방법이 더 낫다 그래서 코드가 더 간단해 보일 수 있다.
MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
m_total += expenses.getTotal();

더 간단하게 하는 방향으로는 ExpenseReportDAO를 returns a MealExpense object록 변경하고 만약에 no meal
expenses면 아 per diem as its total을 반환되는 MealExpense object 를 반환토록 아래와 같이바꿀 수 있습니다.
```java
//SPECIAL CASE PATTERN [Fowler].
public class PerDiemMealExpenses implements MealExpenses {
  public int getTotal() {
    // return the per diem default
  }
}
```
### Don’t Return Null
오류를 유발하는 다루는 에러핸들링도 얘기해봅시다.

아래와 같은 코드를 많이들 볼 수 있습니다.
```java
public void registerItem(Item item) {
    if (item != null) {
      ItemRegistry registry = peristentStore.getItemRegistry();
      if (registry != null) {
        Item existing = registry.getItem(item.getID());
        if (existing.getBillingPeriod().hasRetailOwner()) {
          existing.register(item);
        }
      }
    }
  }
```
it is bad! When we return null, 일단 코드로직을 개발자 본인할 때에만 고려해서 짜고, 호출자의 시점에서 이슈가 발생하도록 강요하는 것과 다름없다.

```java
 public void registerItem(Item item) {
    if (item != null) {
      ItemRegistry registry = peristentStore.getItemRegistry();
      if (registry != null) {
        Item existing = registry.getItem(item.getID());
        if (existing.getBillingPeriod().hasRetailOwner()) {
          existing.register(item);
        }
      }
    }
  }
```

위 예에서는 peristentStore가 null인 경우를 체크하지 않았다 NullPointerException at runtime로 사용할 수 있거나 상위레벨에서 잡아내거나 할 수 있겠지만 둘다 바람직한 방향은 아니다
현재 문제는 too many null체크라는 점이다.만약에 null을 반환해야한다면 위에서 다룬바와 같이 special case 오브젝트를 반환해서 해결하도록 하시고, 반약에 the third party API의 null-returning method를 호출한다면 1. 예외를 던지는 메소드로 감싸거나 스페셜 오브젝트를 반환하도록 디자인하십시오

스페셜 오브젝트를 반환의 경우 아래와 같이 예를 들 수 있습니다.
예를 들면

```java
List<Employee> employees = getEmployees();
if (employees != null) {
  for(Employee e : employees) {
    totalPay += e.getPay();
  }
}
```

이를 아래와 같이 처리하면  NullPointerExceptions 에러를 최소화 할 수 있을 겁니다.

```
List<Employee> employees = getEmployees();
for(Employee e : employees) {
  totalPay += e.getPay();
}

//----------------------

public List<Employee> getEmployees() {
  if( .. there are no employees .. )
    return Collections.emptyList();

```

### Don’t Pass Null

메서드에 null을 전달하는 더 심각한 문제가 될 수 있습니다.. null을 전달할 것으로 예상되는 API를 사용하는 경우가 아니라면 최대한 피해야 합니다.


```java
public class MetricsCalculator
{
  public double xProjection(Point p1, Point p2) {
    return (p2.x – p1.x) * 1.5;
  }
  …
}

//--------------------

calculator.xProjection(null, new Point(12, 13)); //로 호출

// NullPointerException 결과!!! 에러!!!
```

아래와 같은 null 체크로 해결 할 수 있긴하다 허나
```java
public class MetricsCalculator
{
  public double xProjection(Point p1, Point p2) {
    if (p1 == null || p2 == null) {
      throw InvalidArgumentException(
        "Invalid argument for MetricsCalculator.xProjection");
    }
    return (p2.x – p1.x) * 1.5;
  }
}
```


another alternative

```java
public class MetricsCalculator
{
  public double xProjection(Point p1, Point p2) {
    assert p1 != null : "p1 should not be null";
    assert p2 != null : "p2 should not be null";
    return (p2.x – p1.x) * 1.5;
  }
}

```
그러나 누군가 null을 전달해도 런타임 오류가 계속 발생합니다.
대부분의 프로그래밍 언어에서는 호출자가 실수로 전달한 널을 처리하는 좋은 방법이 없다.
합리적인 접근법은 기본적으로 널 전달을 금지

### Conclusion

*if we see error handling as a separate concern, robust clean code를 작성할 수 있다.*
