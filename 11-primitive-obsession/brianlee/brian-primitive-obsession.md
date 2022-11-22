# Primitive Obsession
* 새로운 클래스를 만들지 않고 Primitive 타입을 사용하는 경우
  * Ex) Primitive는 단위 또는 표기법을 표현하기 어렵다(미터법 등)

## Replace Primitive with Object
* 개발 초기에 사용하던 기본 Primitive 숫자 혹은 문자형 타입들이 나중에 더욱 상세한 데이터가 필요한 경우
  * Ex1) 문자열로 표현하던 전화번호가 지역번호 혹은 국가 코드가 필요한 경우
  * Ex2) 섭씨만 쓰는 한국 서비스가 섭시를 쓰는 미국에 서비스를 확장하는 경우 
## Replace Type Code with Subclasses
* Enum, int, String 같은 Primitive Data Type 으로 Type 을 구분하는 경우에 타입을 Subclass 로 바꿀 수 있다.
  * Inheritance 나 Composition 모두 구현 가능
```java
// Inheritance
public abstract class Employee {
    private Example() {}
    
    public static Employee createEmployee(String name, String type) {
        return switch (type) {
            case "engineer" -> new Engineer(name);
            case "manager" -> new Manager(name);
            default -> throw new IlligalArgumentException(type); 
        };
    }
}
// Composition
public class Employee {
    private String name;
    private EmployeeType type;
  
    private Example(String name, String type) {
        this.name = name;
        this.type = createEmployeeType(type);
    }
  
    private EmployeeType createEmployee(String type) {
      return switch (type) {
        case "engineer" -> new Engineer();
        case "manager" -> new Manager();
        default -> throw new IlligalArgumentException(type);
      };
    }
}
```
## Replace Conditional with Polymorphism
* If 문이나 Swich 문 같은 조건문들의 기능을 하위 클래스에서 구현하게 만든다.
* 우의 리팩토링은 Type만 나누어는 것이라고 하면 이것은 Polymorphism 을 이용하여 조건문 별 다른 기능을 하위 클래스에서 구현하는 것아디.
* 사실 Replace Type Code with Subclasses 의 연장선상에 있는 리팩토링 기법이며 나는 사실 두개는 같다고 볼 수도 있다고 생각한다.
* Factory를 사용하여 객체를 생성하거나 Client 에서 구현체를 직접 생성해 쓸 수도 있다.
* Polymorphism 을 사용하니 Inheritance 방법만 쓸 수 있다.
```java
// Conditional Example
class EmployeeWork {
    public void work1(String type) {
        switch (type) {
            case "engineer" -> engineerWork1();
            case "manger" -> managerWork1();
            default -> throw new IlligalArgumentException(type);
        }
    }
    public void work2(String type) {
        switch (type) {
            case "engineer" -> engineerWork2();
            case "manger" -> managerWork2();
            default -> throw new IlligalArgumentException(type);
        }
    }
    public void managerWork1() {
        // do work1
    }
  
    public void managerWork2() {
        // do work2
    }

    public void engineerWork1() {
        // do work1   
    }
  
    public void engineerWork2() {
        // do work2
    }
}

// Polymorphism
class Manager {
    public void managerWork1() {
        // do work1   
    }

    public void managerWork2() {
        // do work2
    }
}

class Engineer {
    public void engineerWork1() {
        // do work1   
    }
  
    public void engineerWork2() {
        // do work2
    }
}
```

## Extract Class (#Divergent Change)
## Introduce Parameter Object (#Long Function)