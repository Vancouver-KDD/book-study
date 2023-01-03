# Decorator design pattern example

- Client
```java
public class Client {
    public static void main(String[] args) {
        ComponentInterface finalResult = new DecoratorTwo(new DecoratorOne(new ConcreteComponent()));

        System.out.println("-- method one --");
        finalResult.methodOne();

        System.out.println("-- method two --");
        finalResult.methodTwo();
    }
}
```

- ComponentInterface
```java
public interface ComponentInterface {

    void methodOne();
    void methodTwo();
}
```

- DecoratorOne (Decorator)
```java
public class DecoratorOne implements ComponentInterface {

    private ComponentInterface componentInterface;

    public DecoratorOne(ComponentInterface componentInterface) {
        this.componentInterface = componentInterface;
    }

    @Override
    public void methodOne() {
        System.out.println("[DecoratorOne_MethodOne] adding its logic...");
        componentInterface.methodOne();
    }

    @Override
    public void methodTwo() {
        System.out.println("[DecoratorOne_MethodTwo] adding its logic...");
        componentInterface.methodTwo();
    }
}
```

- DecoratorTwo (Decorator)
```java
public class DecoratorTwo implements ComponentInterface {

    private ComponentInterface componentInterface;

    public DecoratorTwo(ComponentInterface componentInterface) {
        this.componentInterface = componentInterface;
    }

    @Override
    public void methodOne() {
        System.out.println("[DecoratorTwo_MethodOne] adding its logic...");
        componentInterface.methodOne();
    }

    @Override
    public void methodTwo() {
        System.out.println("[DecoratorTwo_MethodTwo] adding its logic...");
        componentInterface.methodTwo();
    }
}
```

- ConcreteComponent
```java
public class ConcreteComponent implements ComponentInterface {
    @Override
    public void methodOne() {
        System.out.println("[ConcreteComponent_MethodOne] Concrete logic");
    }

    @Override
    public void methodTwo() {
        System.out.println("[ConcreteComponent_MethodTwo] Concrete logic");
    }
}
```