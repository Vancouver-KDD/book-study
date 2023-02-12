# Error Handling
- Error handling is important, but if it obscures logic, it's wrong.

## Use Exceptions Rather Than Return Codes
- Unfortunately, it's easy to forget.
```java
public class DeviceController {
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
}
```
- The calling code is cleaner. 
- Its logic is not obscured by error handling.
```java
public class DeviceController {
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

    private DeviceRecord retrieveDeviceRecord(DeviceHandle handle) {
        ...
        throw new DeviceShutDownError("Device suspended.  Unable to shut down");
        ...
    }
}
```
- The code is better because 
  - the algorithm for device shutdown and error handling are now separated.
- You can look at each of those concerns and understand them independently.

## Write Your  Try-Catch-Finally Statement First
- Try to write tests that force exceptions, 
  - and then add behavior to your handler to satisfy your tests
- This will cause you to build the transaction scope of the try block first 
  - and will help you maintain the transaction nature of that scope.
```java
// Unit Test First
@Test(expected = StorageException.class) 
public void retrieveSectionShouldThrowOnInvalidFileName() {
    sectionStore.retrieveSection("invalid - file");
}
```
```java
// Previous Code
public List<RecordedGrip> retrieveSection(String sectionName) {
    // dummy return until we have a real implementation
    return new ArrayList<RecordedGrip>();
}
```
```java
// Refactoring 1
public List<RecordedGrip> retrieveSection(String sectionName) {
    try {
        FileInputStream stream = new FileInputStream(sectionName)
    } catch (Exception e) {
        throw new StorageException("retrieval error", e);
    }
    return new ArrayList<RecordedGrip>();
}
```
```java
// Refactoring 2
public List<RecordedGrip> retrieveSection(String sectionName) {
    try {
        FileInputStream stream = new FileInputStream(sectionName);
        stream.close();
    } catch (FileNotFoundException e) {
        throw new StorageException("retrieval error”, e);
    }
    return new ArrayList<RecordedGrip>();
}
```
- That logic will be added between the creation of the FileInputStream and the close,
- and can pretend that nothing goes wrong.

## Use Unchecked Exceptions
- The debate is over. Checked exceptions have some benefit. 
- However, it is clear now that they aren’t necessary for the production of robust software.
  1. Open/Closed Principle1 violation
     - The changed modules must be rebuilt and redeployed, even though nothing they care about changed.
  2. Encapsulation is broke
     - because all functions in the path of a throw must know about details of that low-level exception.

## Provide Context with Exceptions
- a stack trace can’t tell you the intent of the operation that failed.
- pass along enough information to be able to log the error in your catch.

## Define Exception Classes in Terms of a Caller’s Needs
- our most important concern should be how they are caught
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
- That statement contains a lot of duplication
- we can simplify our code considerably by wrapping the API that we are calling and making sure that 
  - it returns a common exception type
```java
    LocalPort port = new LocalPort(12);
    try {
      port.open();
    } catch (PortDeviceFailure e) {
      reportError(e);
      logger.log(e.getMessage(), e);
    } finally {
      …
    }
```
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
}
```
- Wrapping like the one, third-party APIs, we defined for ACMEPort can be very useful.
  1. You can choose to move to a different library in the future without much penalty
  2. it easier to mock out third-party calls when you are testing your own code
  3. You can define an API that you feel comfortable with -> cleaner code

## Define the Normal Flow
- there are some times when you may not want to abort.
```java
try {
    MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
    m_total += expenses.getTotal();
} catch(MealExpensesNotFound e) {
    m_total += getMealPerDiem();
}
```
- SPECIAL CASE PATTERN
```java
MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
m_total += expenses.getTotal();
...
public class PerDiemMealExpenses implements MealExpenses {
    public int getTotal() {
        // return the per diem default if MealExpensesNotFound case was occurred
        return getMealPerDiem();
    }
}
```
## Don’t Return Null
- one missing null check to send an application spinning out of control.
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
- other options
  1. throwing an exception
  2. returning a SPECIAL CASE object instead 
     - ex) Collections.emptyList()

# Don’t Pass Null
```java
public double xProjection(Point p1, Point p2) {
    if (p1 == null || p2 == null) {
        throw InvalidArgumentException("Invalid argument for MetricsCalculator.xProjection");     
    }     
    return (p2.x – p1.x) * 1.5;  
} 
```
```java
public double xProjection(Point p1, Point p2) {     
    assert p1 != null : "p1 should not be null";     
    assert p2 != null : "p2 should not be null";     
    return (p2.x – p1.x) * 1.5;  
} 
```
- If someone passes null, we’ll still have a runtime error.
- no good way to deal with a null that is passed by a caller accidentally