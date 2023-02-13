---
marp: true
title: Error Handling
theme: gaia
class: invert
---
<br></br>
<br>
# Error Handling
> Error handling is important, but if it obscures logic, it's wrong.

---
### Use Exceptions Rather Than Return Codes
- The code is better because two concerns that were tangled, the algorithm for device shutdown and error handling, are now separated.
- You can look at each of those concerns and understand them independently.

---
* Before

```java
public class DeviceController {
  ...
  public void sendShutDown() {
    DeviceHandle handle = getHandle(DEV1);
    if (handle != DeviceHandle.INVLAID) {
      retreiveDeviceRecord(handle);
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

---
* After

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

---
### Write Your `Try-Catch-Finally` Statement First
* This helps you define what the user of that code should expect, no matter what goes wrong with the code that is executed in the `try`.

* Unit Test
  ```java
  @Test(expected = StorageException.class)
  public void retrieveSectionShouldThrowOnInvalidFileName() {
    sectionStore.retrieveSection("Invalid - file");
  }
  ```

---
* Before
  ```java
  public List<RecordedGrip> retrieveSection(String sectionName) {
    return new ArrayList<RecordedGrip>();
  }
  ```
* After 1
  ```java
  public List<RecordedGrip> retrieveSection(String sectionName) {
    try {
      FileInputStream stream = new FileInputStream(sectionName);
    } catch (Exception e) {
      throw new StorageException("retrieval error", e);
    }
    return new ArrayList<RecordedGrip>();
  }
  ```

---
* After 2
  ```java
  public List<RecordedGrip> retrieveSection(String sectionName) {
    try {
      FileInputStream stream = new FileInputStream(sectionName);
      stream.close();
    } catch (FileNotFoundException e) {
      throw new StorageException("retrieval error", e);
    }
    return new ArrayList<RecordedGrip>();
  }
  ```

---
### Use Unchecked Exceptions
* We thought that checked exceptions were a great idea.
* However, it is clear now that they aren't necessary for the production of robust software.
* If you throw a checked exception from a method in your code and the `catch` is three levels above, _you must declare that exception in the signature of each method between you and the `catch`_.
  - The changed modules must be rebuilt and redeployed, even though nothing they care about changed.
* Given that the purpose of exceptions is to allow you to handle errors at a distance, it is a shame that checked exceptions break encapsulation in this way.

---
### Provide Context with Exceptions
* Create informative error messages and pass them along with your exceptions.

---
### Define Exception Classes in Terms of a Caller's Needs
* When we define exception classes in an application, our most important concern should be _**how they are caught**_.

---
* Before
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

---
* After
```java
LocalPort port = new LocalPort(12);

try {
  port.open();
} catch(PortDeviceFailure e) {
  reportError(e);
  logger.log(e.getMessage(), e);
} finally {
  ...
}
```

---
* `LocalPort` class
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
      throw new PortDeviceFailure(e)
    } catch (ATM1212UnlockedException e) {
      throw new PortDeviceFailure(e)
    } catch (GMXError e) {
      throw new PortDeviceFailure(e)
    } finally {
      ...
    }
  }
}
```

---
### Define Exception Classes in Terms of a Caller's Needs
* Wrapping third-party APIs is a best practice.
* You can minimize your dependencies upon it.
  - You can choose to move to a different library in the future without much penalty.
* Wrapping also makes it easier to mock out third-party calls when you are testing your own code.
* You aren't tied to a particular vendor's API design choices.
* Often a single exception class is fine for a particular area of code.

---
### Define the Normal Flow
* The process of defining the Normal Flow pushes error detection to the edges of your program.
* Before
```java
try {
  MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
  m_total += expenses.getTotal();
} catch(MealExpensesNotFound e) {
  m_total += getMealPerDiem();
}
```

---
* After
```java
// client code
MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
m_total += expenses.getTotal();

public class PerDiemMealExpenses implements MealExpenses {
  public int getTotal() {
    // return the per diem default
  }
}
```

---
### Don't Return Null
* If you code this way, you will minimize the chance of `NullPointerExceptions` at runtime and your code will be cleaner.
* Before
```java
List<Employee> employees = getEmployees();
if (employees != null) {
  for(Employee e : employees) {
    totalPay += e.getPay();
  }
}
```

---
* After
```java
List<Employee> employees = getEmployees();
for(Employee e : employees) {
  totalPay += e.getPay();
}

public List<Employee> getEmployees() {
  if( .. there are no employees .. ) {
    return Collections.emptyList();
  }
}
```

---
### Don't Pass Null
```java
public class MetricsCalculator {
  public double xProjection(Point p1, Point p2) {
    assert p1 != null : "p1 should not be null";
    assert p2 != null : "p2 should not be null";
    return (p2.x - p1.x) * 1.5;
  }
}
```
* It's good documentation, but it doesn't solve the problem.
* If someone passes `null`, we'll still have a runtime error.
* When you do, you can code with the knowledge that a `null` in an argument list is an indication of a problem, and end up with far fewer careless mistakes.

---
### Conclusion
> We can write robust clean code if we see error handling as a separate concern, something that is viewable independently of our main logic.
