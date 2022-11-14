# Mutable Data

### Remove Setting 

```java
  // before
  public class Example {
    public String variable1;
    public double variable2; // won't be changed after an object is created
    public boolean variable3; // won't be changed after an object is created

    public String getVariable1() {...}
    public double getVariable2() {...}
    public boolean getVariable3() {...}

    public void setVariable1() {...}
    public void setVariable2() {...}
    public void setVariable3() {...} 
  }

  // after
  public class Example {
    public String variable1;
    public double variable2;
    public boolean variable3;

    public Example(double variable2, boolean variable3) {
      this.variable2 = variable2;
      this.variable3 = variable3;
    }

    public String getVariable1() {...}
    public double getVariable2() {...}
    public boolean getVariable3() {...}
  
    public void setVariable1() {...} 
  }
```