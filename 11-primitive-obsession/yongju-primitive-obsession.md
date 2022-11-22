# Primitive Obsession

Data model들을 표현하기 위해 primitive type(strings, numbers)을 무분별하게 사용할 때 나타나는 code smell이다. 각각의 domain model 대신 primitive field를 사용하는 것이 가장 대표적이다.

### Why is this a problem?

- Primitive field는 자체적으로 logic이나 behaviour를 가질 수 없기 때문에 결국 해당 클래스 안에 관계없는 logic들이 존재하게 되고 Single Responsibility Principle을 위반 하게 된다
- Type safety를 잃게 된다, 즉 primitive type들이 어떤 data model을 나타내는 것인지 확인할 수 없기 때문에 compile이 된다고 하여도 작성된 코드가 의도한 대로 동작하는 지 확인할 수 있는 길이 없다
- 함께 있어야 할 field들이 한 module/class에 있지 않거나 여러 module들에 duplicate으로 존재할 수 있다 (DRY principle 위반)

### Refactoring methods

##### 1. Replace primitive with object

```java
  // before
  public class CheckingAccount {
    ...
    private String streetAddress;
    private String zipCode;
    private String city;
    private String state;
    private String country;
    ...
  }

  // after
  public class Address {
    ...
    private String streetAddress;
    private String zipCode;
    private String city;
    private String state;
    private String country; 
    ...
  }

  public class CheckingAccount {
    ...
    private Address address;
    ...
  }
```

##### 2. Replace type code with subclasses

```java
  // before
  public class Employee {
    public Employee(String name, String type) {
      this.name = name;
      this.type = type;
    }

    Employee createEmployee(String name, String type) {
      return new Employee(name, type);
    }
  }

  // after 
  public class Salesman extends Employee {
    ...
  }

  public class Manager extends Employee {
    ...
  }

  public class Engineer extends Employee {
    ...
  }

  public class Employee {
    Employee createEmployee(String type) {
      switch(type) {
        case "engineer": return new Engineer(name);
        case "salesman": return new Salesman(name);
        case "manager":  return new Manager(name);
        default: throw new Exception("Employee cannot be of type " + type);
      }
    }
  }
```

##### 3. Introduce Parameter Object

```java
  // before
  public double amountInvoiced(String startDate, String endDate) {...}
  public double amountReceived(String startDate, String endDate) {...}
  public double amountOverdue(String startDate, String endDate) {...}

  // after
  public double amountInvoiced(DateRange aDateRange) {...}
  public double amountReceived(DateRange aDateRange) {...}
  public double amountOverdue(DateRange aDateRange) {...}
```

### Benefit 

- Object-specific logic/behaviour를 한 클래스 안에 함께 포함함으로써 Single Responsibility Principle을 지키면서 extensibility도 함께 가져갈 수 있다

### Tip

- UML diagram을 통해 사전에 예방할 수도 있다