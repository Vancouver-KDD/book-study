# Functions
```java
// Listing 3-2
public static String renderPageWithSetupAndTeardowns(
  PageData pageData, boolean inSuite
) throws Exception {
  boolean isTestPage = pageData.hasAttribute("Test");
  if (isTestPage) {
    WikiPage testPage = pageData.getWikiPage();
    StringBuffer newPageContent = new StringBuffer();
    includeSetupPage(testPage, newPageContent, isSuite);
    newPageContent.append(pageData.getContent());
    includeTeardownPages(testPage, newPageContent, isSuite);
    pageData.setContent(newPageContent.toString());
  }
  return pageData.getHtml();
}
```

### Small!
- Functions should be small.
```java
// Listing 3-3
public static String renderPageWithSetupsAndTeardowns(
  PageData pageData, boolean isSuite) thows Exception {
  if (isTestPage(pageData))
    includeSetupAndTeardownPages(pageData, isSuite);
  return pageData.getHtml();
}
```

##### Blocks and Indenting
- The indent level of a function should not be greater than one or two.

### Do One Thing
- Functions should do one thing.
- They should do it well.
- They should do it only.

##### Sections within Functions
- Functions that do one thing cannot be reasonably divided into sections.

### One Level of Abstraction per Function
- Mixing levels of abstraction within a functions is always confusing. Readers may not be able to tell whether a particular expression is an essential concept or a detail.

##### Reading Code from Top to Bottom: The Stepdown Rule
- The Stepdown Rule is followed by those at the next level of abstraction sh that we can read the grogram, descending one level of abstraction at a time as we read down the list of functions.

### Switch Statements
- We can't always avoid `switch` statements, but we can make sure that each `switch` statement is buried in a low-level class and is never repeated.
- with `polymorphism`
```java
// Listing 3-4
// depend on the type of employee
public Money calculatePay(Employee e)
throws InvalidEmployeeType {
  switch (e.type) {
    case COMMISSIONED:
      return calculateCommissionedPay(e);
    case HOURLY:
      return calculateHourlyPay(e);
    case SALARIED:
      return calculateSalariedPay(e);
    default:
      throw new InvalidEmployeeType(e.type);
  }
}
```

```java
// Listing 3-5
// use polymorphic objects
public abstract class Employee {
  public abstract boolean isPayDay();
  public abstract Money calculatePay();
  public abstract void deliverPay(Money pay);
}
//------------
public interface EmployeeFactory {
  public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType;
}
//------------
public class EmployeeFactoryImpl implements EmployeeFactory {
  public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType {
    switch (r.type) {
      case COMMISIONED:
        return new CommisionedEmployee(r);
      case HOURLY:
        return new HourlyEmployee(r);
      case SALARIED:
        return new SalariedEmployee(r);
      default:
        throw new InvalidEmployeeType(r.type);
    }
  }
}
```

### Use Descriptive Names
- Don't be afraid to make a name long.
- Don't be afraid to spend time choosing a name.
- Use the same phrases, nouns, and verbs in the function names you choose for your modules.

### Function Arguments
- One input argument is the next best thing to no arguments.

##### Common Monadic Forms
- useful form for a single argument functions is an `event`.
- Try to avoid any monadic functions that don't follow these forms.

##### Flag Arguments
- Passing a boolean into a function is a truly terrible practice.
```java
render(boolean inSuite)
// should have sprit the function into belows:
renderForSuite();
renderForSingleTest();
```

##### Dyadic Functions
```java
// reasonable function with two arguments
Point p = new Point(0, 0);
```

##### Triads
```java
// not quite so insidious function with three arguments
assertEquals(1.0, amount, .001)
```

##### Argument Objects
```java
// before
Circle makeCircle(double x, double y, double radius);

// after
Circle makeCircle(Point center, double radius);
```

##### Argument Lists
```java
//before
String.format("%s worked %.2f hours,", name, hours);

//after
public String format(String format, Object... args);

//monad, dyad, triad version
void monad(Integer... args);
void dyad(String name, Integer... args);
void triad(String name, int count, Integer... args);
```

##### Verbs and Keywords
```java
//before
write(name);

//after
writeField(name);
```

```java
//before
assertEquals(expected, actual);

//after
assertExpectedEqualsActual(expected, actual);
```

### Have No Side Effects
- Your function promises to do one thing, so there should not be side effects.

##### Output Arguments
- In general output arguments should be avoided.
- If your function must change the state of something, have it change the state of its owning object.

### Command Query Separation
```java
public boolean set(String attribute, String value);

//before
if (set("username", "unclebob"))...

//after
if (attributeExist("username")) {
  setAttribute("username", "unclebob");
  ...
}
```

### Prefer Exceptions to Returning Error Codes
```java
//before
if (deletePage(page) == E_OK) {
  if (registry.deleteReference(page.name) == E_OK) {
    if (configKeys.deleteKey(page.name.makeKey()) == E_OK) {
      logger.log("page deleted");
    } else {
      logger.log("configKey not delete")
    }
  } else {
    logger.log("deleteReference from registry failed")
  }
} else {
  logger.log("delete failed");
  return E_ERROR;
}

//after
try {
  deletePage(page);
  registry.deleteReference(page.name);
  configKeys.deleteKey(page.name.makeKey());
} catch (Exception e) {
  logger.log(e.getMessage());
}
```

##### Extract Try/Catch Blocks
```java
public void delete(Page page) {
  try {
    deletePageAndAllReferences(page);
  } catch (Exception e) {
    logError(e);
  }
}

private void deletePageAndAllReferences(Page page) throws Exception {
  deletePage(page);
  registry.deleteReference(page.name);
  configKeys.deleteKey(page.name.makeKey());
}

private void logError(Exception e) {
  logger.log(e.getMessage());
}
```

##### Error Handling Is One Thine
- This implies (as in the example above) that if the keyword `try` exists in a function, it should be the very first word in the function and that there should be nothing after the `catch`/`finally` blocks.

##### The `Error.java` Dependency Magent
- When you use exception rather than error codes, then new exceptions are derivatives of the exception class.
- They can be added **without forcing any recompilation or redeployment**.

### Don't Repeat Yourself
- Duplication is a problem because it bloats the code and will require multi-fold modification should the algorithm ever have to change.
- It is also a multi-fold opportunity for an errors of omission.

### Structured Programming
- Every function, and every block within a function, should have one entry and one exit.
- There should only be one `return` statement in a function, no `break` or `continue` statements in a loop, and never, ever any `goto` statements.

### How Do You Write Functions Like This?
- massage and refine the code
- splitting out functions
- changing names
- eliminating duplication
- shrink the methods and reorder them
- break out whole classes

### Conclusion
- Functions are the verbs of that language, and classes are the nouns.
- Master programmers think of systems as **stories to be told** rather than programs to be written.
> Never forget that your real goal is to tell the story of the system, and that the functions you write need to fit cleanly together into a clear and precise language to help you with that telling
