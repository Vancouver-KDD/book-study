# Chapter 30. Design Patterns

## Commandc - Test Writing: O / Refactoring: X
- Q: What do you do when you need the invocation of a computation to be more complicated than a simple method call?
- A: Make an object for the computation and invoke it.
- Example:
```
interface Runnable
public abstract void run();
```

## Value Object - Test Writing: O / Refactoring: X
- Q: How do you design objects that will be widely shared, but for whom identity is unimportant?
- A: Set their state when they are created and never change it. Operations on the object always return a new object.
- Aliasing problem can occur
- a. never to give out the objects that you rely on, but instead always make copies.
- b. use Observer pattern
- c. treat the object as less than an object

## Null Object - Test Writing: X / Refactoring: O
- Q: How do you represent special cases using objects?
- A: Create an object representing the special case. Give it the same protocol as the regular objects.
- 

## Template Method - Test Writing: X / Refactoring: O
- Q: How do you represent the invariant sequence of a computation while providing for future refinement?
- A: Write a method that is implemented entirely in terms of other methods.
- Classic sequences:
- a. Input/process/output
- b. Send message/receive reply
- c. Read command/return result

## Pluggable Object - Test Writing: X / Refactoring: O
- Q: How do you express variation?
- A: The simplest way is with explicit conditionals
- Example:
```
SelectionMode mode; 
public void mouseDown() {
    selected= findFigure(); 
    if (selected != null)
        mode= SingleSelection(selected); 
    else
        mode= MultipleSelection();
}
public void mouseMove() { 
    mode.mouseMove();
}
public void mouseUp() {
    mode.mouseUp(); 
}
```

## Pluggable Selector - Test Writing: X / Refactoring: O
- Q: How do you invoke different behavior for different instances?
- A: Store the name of a method, and dynamically invoke the method.
- Example:
```
void print() {
    Method runMethod= getClass().getMethod(printMessage, null);
    runMethod.invoke(this, new Class[0]);
}
```
- The biggest problem with it is tracing code to see whether a method is invoked. Use Pluggable Selector only when you are cleaning up a fairly straightforward situation in which each of a bunch of subclasses has only one method.

## Factory Method - Test Writing: O / Refactoring: O
- Q: How do you create an object when you want flexibility in creating new objects?
- A:  Create the object in a method instead of using a constructor.
- The downside of using Factory Method is precisely its indirection. You have to remember that the method is really creating an object, even though it doesn't look like a constructor. Use Factory Method only when you need the flexibility it creates. Otherwise, constructors work just fine for creating objects.

## Imposter - Test Writing: O / Refactoring: O
- Q: How do you introduce a new variation into a computation?
- A: Introduce a new object with the same protocol as an existing object but a different implementation.

## Composite - Test Writing: O / Refactoring: O
- Q: How do you implement an object whose behavior is the composition of the behavior of a list of other objects?
- A:  Make it an Imposter for the component objects.

## Collecting Parameter - Test Writing: O / Refactoring: O
- Q: How do you collect the results of an operation that is spread over several objects?
- A: Add a parameter to the operation in which the results will be collected.

## Singleton
- Q: How do you provide global variables in languages without global variables?
- A: Don't. Your programs will thank you for taking the time to think about design instead.
