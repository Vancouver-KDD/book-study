# Emergence
### Getting Clean via Emergent Design
- Many of us feel that Ken Beck's four rules of _Simple Design_ are of significant help in creating well-designed software.
- Kent Beck's four rules of _Simple Design_
  * Runs all the tests
  * Contains no duplication
  * Expresses the intent of the programmer
  * Minimizes the number of classes and methods

### Simple Design Rule 1: Runs All the Tests
- A design must produce a system that acts as intended.
- A system that is comprehensively tested and passes all of its tests all of the time is a testable system.
- Making our systems testable pushes us toward a design where our classes are small and single purpose.
- The more test we write, the more we use principles like DIP and tools like dependency injection, interfaces, and abstraction to minimize coupling. Our designs improve even more.

### Simple Design Rules 2-4: Refactoring
- Once we have tests, we are empowered to keep our code and classes clean.
- The fact that we have these tests eliminates the fear that cleaning up the code will break it.
- During this refactoring step, we can apply anything from the entire body of knowledge about good software design. We can increase cohesion, decrease coupling, separate concerns, shrink our functions and classes, choose better names, and so on.
- And then, we apply the final three rules of simple design.

### No Duplication
- Duplication is the primary enemy of a well-designed system.
- It represents additional work, additional risk, and additional unnecessary complexity.
```java
// Before
int size() {}
boolean isEmpty() {}

// After
boolean isEmpty() {
  return 0 == size();
}
```
- Creating a clean system requires the will to eliminate duplication, even in just a few lines of code.
- Two methods in a collection class
  - Before code
    ```java
    public void scaleToOneDimension(float desiredDimension, float imageDimension) {
      if (Math.abs(desiredDimension - imageDimension) < errorThreshold)
          return;
      float scalingFactor = desiredDimension / imageDimension;
      scalingFactor = (float)(Math.floor(scalingFactor * 100) * 0.01f);

      RenderedOpnewImage = ImageUtilities.getScaledImage(image, scalingFactor, scalingFactor);
      image.dispose();
      System.gc();
      image = newImage;
    }

    public synchronized void rotate(int degrees) {
      RenderedOpnewImage = ImageUtilities.getRotatedImage(image, degrees);
      image.dispose();
      System.gc();
      image = newImage;
    }
    ```
  - After code
    ```java
    public void scaleToOneDimension(float desiredDimension, float imageDimension) {
      if (Math.abs(desiredDimension - imageDimension) < errorThreshold)
          return;
      float scalingFactor = desiredDimension / imageDimension;
      scalingFactor = (float) Math.floor(scalingFactor * 10) * 0.01f);
      replaceImage(ImageUtilities.getScaledImage(image, scalingFactor, scalingFactor));
    }

    public synchronized void rotate(int degrees) {
      replaceImage(ImageUtilities.getRotatedImage(image, degrees));
    }

    private void replaceImage(RenderedOpnewImage) {
      image.dispose();
      System.gc();
      image = newImage;
    }
    ```
- As we extract commonality at this very tiny level, we start to recognize violations of SRP. So we might move a newly extracted method to another class. That elevates its visibility.
- Someone else on the team may recognize the opportunity to further abstract the new method and reuse it in a different context. This "reuse in the small" can cause system complexity to shrink dramatically.
- The TEMPLATE METHOD pattern is a common technique for removing higher-level duplication.
  - Before code
  ```java
  public class VacationPolicy {
    public void accrueUSDDivisionVacation() {
      // code to calculate vacation based on hours worked to date
      // ...
      // code to ensure vacation meeds US minimums
      // ...
      // code to apply vacation to payroll record
      // ...
    }

    public void accrueEUDivisionVacation() {
      // code to calculate vacation based on hours worked to date
      // ...
      // code to ensure vacation meeds US minimums
      // ...
      // code to apply vacation to payroll record
      // ...
    }
  }
  ```
  - After code
  ```java
  abstract public class VacationPolicy {
    public void accrueVacation() {
      calculateBseVacationHours();
      alterForLegalMinimums();
      applyToPayroll();
    }

    private void calculateBaseVacationHours() { /* ... */ };
    abstract protected void alterForLegalMinimums();
    private void applyToPayroll() { /* ... */ };
  }

  public class USVacationPolicy extends VacationPolicy {
    @Override protected void alterForLegalMinimums() {
      // US specific logic
    }
  }

  public class EUVacationPolicy extends VacationPolicy {
    @Override protected void alterForLegalMinimums() {
      // EU specific logic
    }
  }
  ```
- The subclasses fill in the "hole" in the `accrueVacation` algorithm, supplying the only bits of information that are not duplicated.

### Expressive
- Many of us have produced some convoluted code ourselves. It's easy to write code that we understand, because at the time we write it we're deep in an understanding of the problem we're trying to solve. **Other maintainers of the code aren't going to have so deep an understanding.**
- You can express yourself by **choosing good names.**
- You can also express yourself by **keeping your functions and classes small**.
- You can also express yourself by **using standard nomenclature.** By using the standard pattern names, such as COMMAND or VISITOR, in the names of the classes that implement those patterns, you can succinctly describe your design to other developers.
- **Well-written unit tests** are also expressive. Someone reading our tests should be able to get a quick understanding of what a class is all about.
- The most important way to be expressive is to **try**. All too often we get our code working and then move on to the next problem without giving sufficient thought to making that code easy for the next person to read.

### Minimal Classes and Methods
- In an effort to make our classes and methods small, we might create too many tiny classes and methods. So this rule suggests that we also keep our function and class counts low.
- Heigh class and method counts are sometimes the result of pointless dogmatism.
- Our goal is to keep our overall system small while we are also keeping our functions and classes small. Remember, however, **that this rule is hte lowest priority** of the four rules of Simple Design.

> Following the practice of simple design can and does encourage and enable developers to adhere to good principles and patterns that otherwise take years to learn.
