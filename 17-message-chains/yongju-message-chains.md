# Message Chains

##### Hide Delegate

```java
  public class Client {
    public static void main(String[] args) {
      Server server = new Server();

      String someReturnValueFromNonDelegateMethod = server.getDelegate().aMethod();
    }
  }

  public class Server {
    ...
    private ADelegate delegate;

    public ADelegate getDelegate() {
      return this.delegate;
    }
    ...
  }

  public class ADelegate {
    ...
    public String aMethod() {
      ...
    }
    ...
  }

  // After
  public class Client {
    public static void main(String[] args) {
      Server server = new Server();

      STring someReturnValueFromDelegateMethod = server.aMethod();      
    }
  }

  public class Server {
    ...
    private ADelegate delegate;

    public String aMethod() {
      return this.delegate.aMethod();
    }
    ...
  }

```