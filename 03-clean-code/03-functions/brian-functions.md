# Functions 
## Small!
- The first rule of functions is that they should be small. 
- The second rule of functions is that they should be smaller than that

### Blocks and Indenting
- the blocks within 
  - if statements, 
  - else statements, 
  - while statements, 
  - and so on should be one line long

## Do One Thing
- FUNCTIONS SHOULD DO ONE THING. 
- THEY SHOULD DO IT WELL. 
- THEY SHOULD DO IT ONLY

## One Level of Abstraction per Function
### Reading Code from Top to Bottom: The Stepdown Rule
## Switch Statements
- switch(type) => factory
```java
public Money calculatePay(Employee e) {
    switch(e.type) {
        case SALARIED: 
            return calculateSalariedPay(e);
        ...
    }
}
```
```java
public Employee makeEmplyee(EmployeeRecord r) implements EmployeeFactory {
    switch (r.type) {
        return new SalariedEmploye(r);
        ...
    }
}
```
## Use Descriptive Names
- Choosing good names for small functions
- Don't be afraid to make a name long

## Function Arguments
- zero argument(niladic) is the best
- one argument(monadic) is good
- two arguments(dyadic) are not bad
- Three arguments(triadic) should be avoided where possible <- testing every combination

### Common Monadic Forms
- avoid this form
  - void transform(StringBuffer sb);
- the transformation should appear as the return value
  - StringBuffer transform(StringBuffer in);

### Flag Arguments
- String render(boolean isSuite); // X
- String renderForSuite(); // O
- String renderForSingleTest(); // O

### Dyadic Functions
- writeField(outputStream, name);
  - is could be better
- like these
  1. outputStream.writeField(name); 
  2. writeField(name); // outputStream is a member variable
  3. extract a new class like FieldWriter
```java
public class FieldWriter {
    OutputStream outputStream;
    FieldWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    
    public void setFieldName(String name) {
        ...
    }
}
```
### Triads
- significantly harder to understand than dyads

### Argument Objects
- be wrapped into a class of their own
```java
Circle makeCircle(double x, double y, double radius);
Circle makeCircle(Point center, double radius);
```

### Argument Lists
```java
public String format(String format, Object... args)
// String.format("%s worked %.2f hours.", name, hours);
```
```java
void monad(Integer... args);
void dyad(String name, Integer... args);
void triad(String name, int count, Integer... args);
```
### Verbs and Keywords
-  assertExpectedEqualsActual(expected, actual)

## Have No Side Effects
- checkPassword VS checkPasswordAndInitializeSession (violates “Do one thing.”)
```java
public boolean checkPassword(String userName, String password) {     
    User user = UserGateway.findByName(userName);     
    if (user != User.NULL) {       
        ...
        if ("Valid Password".equals(phrase)) {
            Session.initialize();         
            return true;       
        }     
    }     
    return false;  
}
```

### Output Arguments
- public void appendFooter(StringBuffer report)
- report.appendFooter();

## Command Query Separation
- public boolean set(String attribute, String value); // X
  - if (set("username", "unclebob"))...
- public void setAndCheckIfExists(String attribute, String value) // X
```java
    public boolean attributeExists(String attribute); // Query
    public void setAttribute(String attribute, String value); // Command
    
    if (attributeExists("username")) {
        setAttribute("username", "unclebob"); 
        ... 
    }
```

## Prefer Exceptions to Returning Error Codes
- use exceptions instead of returned error codes

### Extract Try/Catch Blocks 
### Error Handling Is One Thing
- Functions should do one thing. 
- Error handing is one thing

### The Error.java Dependency Magnet
- when the Error enum changes, all those other classes need to be recompiled and redeployed

## Don’t Repeat Yourself

## Structured Programming 
- very function, and every block within a function, should have one entry and one exit

## How Do You Write Functions Like This?
- write it, restructure it and refine it until it reads the way you want it to read

## Conclusion
- Master programmers think of systems as stories to be told rather than programs to be written.