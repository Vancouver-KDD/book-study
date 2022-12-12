# Alternative classes with different interfaces 

## 발생 원인 
내부 동작은 비슷한데 인터페이스가 다를 경우 (비슷한 동작을 하는 클래스가 다른 이름의 메소드, 파라미터를 가지고 있을 경우) 발생한다.  
작은 차이만 있는 클래스가 중복되는 것과 비슷하다.  

## 해결 방법
#### *  1, 2 번 방법은 전 회차에서 자주 다룬 내용이라 3번 Extract superclass 만 상세히 다루겠습니다.   
*1) Change Function Declaration* : 이름이나 파라미터 리스트 등을 통일시켜서 언제든지 대체할 수 있도록 만든다.    
*2) move function* : 비슷한 동작을 하는 function 끼리 뭉칠 수 있도록  으로 조정하기도 한다.
## Example 
~~~ java   
//before
public class CheckoutHanlder {
// ...

     public double convertToCurrency(double price, String currencyTo) {
         if (currencyTo.equalsIgnoreCase("EUR")) {
             return price * 0.9;
         } else if (currencyTo.equalsIgnoreCase("CAD")) {
             return price * 1.35;
         } else {
             throw new IllegalArgumentException("Unrecognized currency: " + currencyTo);
         }
     }
}

public class SimpleCurrencyConverter {
private String currencyTo;

     public SimpleCurrencyConverter(String currencyTo) {
         this.currencyTo = currencyTo;
     }

     public double convert(double price) {
         if (currencyTo.equalsIgnoreCase("EUR")) {
             return price * 0.9;
         } else if (currencyTo.equalsIgnoreCase("CAD")) {
             return price * 1.35;
         } else {
             throw new IllegalArgumentException("Unrecognized currency: " + currencyTo);
         }
     }
}
~~~

convert 와 convertToCurrency 메서드가 똑같은 일을 하면서 서로 다른 파라미터, 이름을 가지고 중복되어있다.  
이 경우는 SimpleCurrencyConverter 에서 하는게 자연스러운 내용이기에 CheckoutHandler의 중복된 부분을 제거하고 SimpleCurrencyConverter의 메소드로 대체한다.  

~~~java
//after
public class CheckoutHanlder {
// ...
}

public class SimpleCurrencyConverter {
    private String currencyTo;

    public SimpleCurrencyConverter(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public double convert(double price) {
        if (currencyTo.equalsIgnoreCase("EUR")) {
            return price * 0.9;
        } else if (currencyTo.equalsIgnoreCase("CAD")) {
            return price * 1.35;
        } else {
            throw new IllegalArgumentException("Unrecognized currency: " + currencyTo);
        }
    }
}
~~~
[참고 출처](https://ducmanhphan.github.io/2020-01-24-Fixing-object-oriented-abusers/#alternative-classes-with-different-interfaces)

---

*3) Extract Superclass* :   
중복된 부분이 있는 클래스들이 있다면  통해서 Superclass 에 공통적인 부분을 빼 상속받아 사용한다.   

### Motivation 
비슷한 일을 하는 두 클래스를 보면 공통된 부분을 묶어서 superclass 를 만들어 상속을 사용한다. 
이를 대신해 Extract class로 공통된 부분을 별개의 클래스로 만들 수도 있다. 

### Mechanics 
1. 빈 superclass 를 만든다.  
2. pull up field, pull up method 통해 subclass의 공통된 부분을 superclass 로 옮긴다.  
3. test 한다.  

### Example 
~~~javascript
// before 

class Employee {
  constructor(name, id, monthlyCost) {
    this._id = id;
    this._name = name;
    this._monthlyCost = monthlyCost;
  }
  get monthlyCost() {return this._monthlyCost;}
  get name() {return this._name;}
  get id() {return this._id;}
  
  get annualCost() {
    return this.monthlyCost * 12;
  }
}
class Department {
  constructor(name, staff){
    this._name = name;
    this._staff = staff;
  }
  get staff() {return this._staff.slice();}
  get name() {return this._name;}

  get totalMonthlyCost() {
    return this.staff
      .map(e => e.monthlyCost)
      .reduce((sum, cost) => sum + cost);
  }
  get headCount() {
    return this.staff.length;
  }
  get totalAnnualCost() {
    return this.totalMonthlyCost * 12;
  }
}

~~~

~~~javascript
class Party {
    get annualCost() {
        return this.monthlyCost * 12;
    }
    get name(){
        return this._name;
    }
}

class Employee {
    constructor(name, id, monthlyCost) {
        super(name);
        this._id = id;
        this._monthlyCost = monthlyCost;
    }
    get monthlyCost() {return this._monthlyCost;}
    get name() {return this._name;}
    get id() {return this._id;}

}
class Department {
    constructor(name, staff){
        super(name);
        this._staff = staff;
    }
    get staff() {return this._staff.slice();}
    get name() {return this._name;}

    get monthlyCost() {
        return this.staff
            .map(e => e.monthlyCost)
            .reduce((sum, cost) => sum + cost);
    }
    get headCount() {
        return this.staff.length;
    }
 
}

~~~