# Objects and Data Structures

### Data Abstraction
- Hiding implementation is not just a matter of putting a layer of functions between the variables. Hiding implementation is about **abstractions**!
- A class does not simply push its variables out through getters and setters, rather it exposes abstract interfaces that allow its users to manipulate the essence of the data, without having to know its implementation.
  - Concrete Vehicle
  ```java
  public interface Vehicle {
    double getFuelTankCapacityInGallons();
    double getGallonsOfGasoline();
  }
  ```
  - Abstract Vehicle
  ```java
  public interface Vehicle {
    double getPercentFuelRemaining();
  }
  ```

### Data/Object Anti-Symmetry
- **Objects** hide their data behind abstractions and expose functions that operate on that data.
- **Data structure** expose their data and have no meaningful function.
- The things that are hard for OO are easy for procedures, and the thins that are hard for procedures are easy for OO!

### The Law of Demeter
- *Law of Demeter* that says a module should not know about the innards of the objects it manipulates.
  - A method `f` of a class `C` should only call the methods of these:
    - `C`
    - An object created by `f`
    - An object passed as an argument to `f`
    - An object held in an instance variable of `C`

### Train Wrecks
- It look like a bunch of coupled train cars.
- Chains of calls are generally considered to be sloppy style and should be avoided.
  - Before:
  ```java
  final String outputDir = ctxt.getOptions().getScratchDir().getAbsolutePath();
  ```
  - After:
  ```java
  Options opts = ctxt.getOptions();
  File scratchDir = opts.getScratchDir();
  final String outputDir = scratchDir.getAbsolutePath();
  ```

### Hybrids
- Hybrid structures are half object and half data structure.
- Such hybrids make it hard to add new functions but also make it hard to add new data structures.
- Avoid creating hybrids

### Hiding Structure
```java
BufferedOutputStream bos = ctxt.createScratchFileStream(classFileName);
```
- This allows `ctxt` to hide its internals and prevents the current function from having to violate the Lay of Demeter by navigating through objects it shouldn't know about.

### Data Transfer Objects
- DTOs are very useful structures, especially when communicating with databases or parsing messages from sockets, and so on.

### Active Record
- Active Records are special forms of DTOs. They are data structures with public variables; but they typically have navigational methods like `save` and `find`.

### Conclusion
- **Objects** expose behavior and hide data.
  - Easy: adding new kinds of objects.
  - Hard: add new behaviors to existing objects.
- **Data structures** expose data and have no significant behavior.
  - Easy: add new behaviors to existing data structures.
  - Hard: add new Data structures to existing functions.

> Good software developers understand these issues without prejudice and choose the approach that is best for the job at hand.
