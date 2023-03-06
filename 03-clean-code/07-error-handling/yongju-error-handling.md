# Chapter 7. Error Handling

#### Use Exceptions Rather Than Return Codes
예전에 exception이 존재하지 않던 언어들에서는 에러를 관리할 수 있는 방법이 제한적이었다. 아럐 코드에서처럼 에러 플래그를 만들거나 에러 코드를 반환하는 방식으로 관리하곤 했다.
```java
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

위와 같은 방법은 메소드를 부르는 caller들이 산재해 있고, caller들은 에러를 바로 체크해야한다. 안타깝게도 이 방법은 에러핸들링을 까먹는 경우가 생기기 때문에, 에러가 생겼을 떄 바로 exception을 던져주는 방법이 더 좋다. 위의 코드에 exception을 활용해 리팩토링한 아래 코드를 보자.
```java
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
첫번째 코드에서 두 개의 다른 로그로 나뉘어 관리하던 것들을 한 곳에 모으고 기능자체와 에러핸들링은 분리함으로써 따로 따로 바라볼 수 있는 장점이 생겼다.

#### Write your Try-Catch_Finally Statement First
- Exception의 장점 중 하나는 프로그램의 스코프를 결정한다는 것이다
- Try-catch-finally를 통해서 우리는 try안에서의 코드가 언제든 종료되고 catch에서 다시 시작한다는 걸 나타내주고 있다
- 어떤 면에서 try 코드블록은 

#### Use Unchecked Exceptions
- 예전에는 checked exception이 좋은 코딩이라고 여겨질 때가 있었지만 지금은 그렇지 않다
- Checked exception은 Open/Closed principle을 위반한다  
  만약, 어떤 메소드에서 checked exception을 던질 때 catch가 더 높은 레벨의 메소드에 있다면 중간에 존재하는 모든 메소드의 signature에 exception을 명시해줘야 한다. 이 경우에는 더 낮은 레벨의 코드가 더 높은 레벨의 메소드의 signature를 강제하는 구조가 생기기 때문이다. 이 구조는 또한 encapsulation을 깨기도 하는데, 높은 레벨의 코드에서 낮은 레벨의 코드를 알아야 하기 때문이다. 
- Exception의 목적이 코드와 에러핸들링을 분리하는 것이라는 것을 봤을 때, checked exceptions을 사용 하는 
  경우에 encapsulation이 깨지는 건 참 안타까운 일이다
- Checked exception이 유용하게 사용되는 곳도 있지만 보통은 위와 같은 dependency들의 코스트가 장점을 상회하는 경우가 다분하다

#### Provide Context with Exceptions
- 각각의 exception은 충분한 context와 에러의 위치, 소스와 함꼐 던져져야 한다
- Java의 exception은 항상 stack trace를 제공하지만 그것만으로는 context가 부족하다
  항상 누구나 쉽게 알아챌 수 있도록 메세지를 작성해서 exception과 함께 던지도록 하자

#### Define Exception Classes in Terms of a Caller's Needs
- 에러를 분류할 때 에러의 소스나 타입같은 것으로 분류할 수 도 있지만 가장 중요하게 생각해야할 것은 어떻게 그 에러가 잡혔는지이다

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
  ...
}
```
위의 exception들을 처리하는 방법은 모두 똑같다
```java
reportPortError(e);
logger.log("...");
```

따라서 아래 코드처럼 PortDeviceFailure라는 하나의 exception으로 모아주고, 에러 핸들링은 LocalPort라는 Wrapper에서 진행해 줄 수 있다.
```java
LocalPort port = new LocalPort(12);

try {
  port.open();
} catch (PortDeviceFailure e) {
  reportError(e);
  logger.log(e.getMessage(), e);
} finally {
  ...
}

// Wrapper
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

  ...
}
```
- Third-party 라이브러리를 사용할 때 위에서 우리가 ACMEPort를 감싼것처럼 wrapper를 사용해주면 유용하다
  - dependency를 줄일 수 있음
  - 다른 라이브러리를 사용하려고 할 때도 risk를 최소화시킬 수 있음
  - 한 라이브러리의 디자인에 종속되지 않아도 됨

#### Define the Normal Flow
- Wrapper를 사용해서 third-party API를 감싸주는 것은 많은 장점이 있지만, 때로는 에러로 인해서 프로그램을 종료하고 싶지 않을 때도 존재한다

아래의 조금은 이상한 코드를 보자.
```java
try {
  MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
  m_total += expenses.getTotal();
} catch (MealExpenseNotFound e) {
  m_total += getMealPerDiem();
}
```
Meal이 존재하면 meal의 값을 m_total에 더하고, 존재하지 않는다면 getMealPerDiem의 양을 더한다. Exception이 코드를 더럽히고 있다.
이 하나의 특별한 케이스를 없앤다면 코드가 더 간단해지지 않을까?

만약 ExpenseReportDao가 항상 MealExpense를 반환한다면 굳이 exception을 사용하지 않아도 될 것이다.
```java
public class PerDiemMealExpenses implements MealExpenses {
  public int getTotal() {
    // return the per diem default
  }
}

MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
m_total += expenses.getTotal();
```

- 이 방법을 Special Case Pattern이라고 하는데 하나의 특별한 케이스를 관리하기 위해 class나 object를 만들어 사용하는 것이다

#### Don't Return null
- 우리가 에러를 만드는 것 중 대표적인 것이 null을 반환하는 것이다
- null을 반환할 때 우리는 쓸데없는 일을 만들고 있는 셈이다
- null을 반환하는 메소드를 만들려고 할 때에는, exception을 던지거나 Special Case object를 만드는 것을 생각해보자
- Third-party API가 null을 반환하는 메소드를 가지고 있다면, wrapper를 사용해 역시 exception을 던지거나 Special case object를 사용하도록 하자

```java
List<Employee> employees = getEmployees();
if (employees != null) { // getEmployees가 null을 반환할 수 있기 때문에
  for(Employee e: employees) {
    totalPay += e.getPay();
  }
}
```
만약 getEmployees가 null을 반환하지 않는다면 어떨까?
```java
public List<Employee> getEmployees {
  if (...there are no employees..) 
    return Collections.emptyList();
}

List<Employee> employees = getEmployees();
for(Employee e: employees) { // 필요없는 코드 제거
  totalPay += e.getPay();
}
```

#### Don't Pass Null
- null을 반환하는 메소드도 좋지 않지만, null을 argument로 전달하는 메소드는 더 좋지 않다
- 명확하게 null을 전달하는 것을 필요로 하는 API를 사용하지 않는 이상 null을 전달하는 것은 웬만하면 피애햐 한다
```java
public class MetricsCalculator {
  public double xProjection(Point p1, Point p2) {
    return (p2.x - p1.x) * 1.5;
  }
}

// null을 전달하면 어떻게 될까?
calculator.xProjection(null, new Point(12, 13)); 
```
당연히 NullPointerException이 던져질 것이다. 그렇다면 specific exception을 만들어 던져주면 더 나을까?

```java
public class MetricsCalculator {
  public double xProjection(Point p1, Point p2) { 
    if (p1 == null || p2 == null) { 
      throw InvalidArgumentException( "Invalid argument for MetricsCalculator.xProjection"); 
    } 
    return (p2.x – p1.x) * 1.5; 
  }
}
```
null을 핸들링하기에는 좋지만 새로운 InvalidArgumentException을 만들어줘야 한다.
그렇다면 assertion을 이용하면 어떨까?

```java
public class MetricsCalculator { 
  public double xProjection(Point p1, Point p2) { 
    assert p1 != null : "p1 should not be null"; 
    assert p2 != null : "p2 should not be null"; 
    return (p2.x – p1.x) * 1.5; 
  } 
}
```
좋은 시도지만, null이 들어오면 runtime error가 던져진다는 점에서 문제를 해결하지는 못한다. 
거의 모든 프로그래밍 언어중에 전달되는 null을 잘 해결할 수 있는 방법은 거의 없다. 그래서 가장 좋은 방법은 null을 argument로 전달하지 않는 것이다.
