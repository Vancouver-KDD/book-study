# Adaptor design pattern example

```java
// Client
public class Client {
  public static void main(String[] args) {
    Java javaSubClass = new JavaSubClass();
    System.out.println(javaSubClass.exampleJavaMethod());

    Python python = new Python();
    JavaAdaptor javaAdaptor = new JavaAdaptor(python);
    System.out.println(javaAdaptor.exampleJavaMethod());
  }
}

// Java (interface)
public interface Java {
  String exampleJavaMethod();
}

// JavaSubClass
public class JavaSubClass implements Java {
  @Override
  public String exampleJavaMethod() {
      return "This is a return value of Java Subclass\n";
  }
}

// JavaAdaptor (Adaptor)
public class JavaAdaptor implements Java {

  private Python python;

  public JavaAdaptor(Python python) {
      this.python = python;
  }

  @Override
  public String exampleJavaMethod() {
      System.out.print(python.examplePythonMethod());
      return ", but translated to Java";
  }
}

// Python
public class Python {

  String examplePythonMethod() {
      return "This is a return value of Python Method";
  }
}

```