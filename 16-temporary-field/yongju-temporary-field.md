# Temporary Field

##### Introduce Special Case

```java
  // Before
  // class 1
  public class Class1 {
    ...
    public void method1() {
      if (aVar == "specialCase") {
        // do something
      }
    }
    ...
  }

  // class 2
  public class Class2 {
    ...
    public void method1() {
      if (aVar != "specialCase") {
        // do something
      } else if (aVar == "differentCase") {
        // do something else        
      }
    }
    ...
  }

  // class 3
  public class Class3 {
    ...
    public void method1() {
      if (aVar == "specialCase") {
        // do something
      } else if (aVar != "specialCase") {
        // do something else
     } else if (aVar == "differentCase") {
        // do something else
      } else {
        // do something else
      }
    }
    ...
  }

  // After
  public class SpecialCase {
    ...
    public boolean isSpecialCase(aVar) {
      return aVar == "specialCase";
    }
    ...
  }

  public class DifferentCase {
    ...
    public boolean isDifferentCase(aVar) {
      return aVar == "isDifferentCase";
    }
    ...
  }
```