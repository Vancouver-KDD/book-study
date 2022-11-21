# Long Function
* 긴 함수 길이 vs 짦은 함수 문맥 전환 -> 메서드 명이 좋으면 문맥 전환 필요 X
* 주석을 남기고 싶다면, 주석 대신 메서드로 Intention을 표현하라!

## Extract Function  (#Duplicated Code)
* Long Function의 99% 문제를 Extract Function으로 해결

## Parameter 줄이는 Refactoring
### 1. Replace Temp with Query
* Parameter 대신 다른 메서드로 대체하라!
  * Ex) getPrice() VS getPrice2()
    * getPrice2()는 짧고 이해하기 쉽지만 getBasePrice를 2번 호출한다.
    * 
```java
class Example {
    int quantity = 10;
    double price = 100;
    
    public double getPrice() {
        double basePrice = this.price * this.quantity;
        double discountFactor = 0.98;
        if(basePrice > 1000) discountFactor -= 0.03;
        return basePrice * discountFactor;
    }

    public double getPrice2() {
        // 기존: return basePrice * discountFactor;
        // temp => query
        // basePrice => getBasePrice()
        // discountFactor => getDiscountFactor()
        return getBasePrice() * getDiscountFactor();
    }
    
    private double getBasePrice() {
        return this.price * this.quantity;
    }
    
    private double getDiscountFactor() {
      double discountFactor = 0.98;
      if(this.getBasePrice() > 1000) discountFactor -= 0.03;
      return discountFactor;
    }
}
```
### 2. Introduce Parameter Object
* 파라미터를 줄이고 Record 명으로 파라미터의 의미를 부여할 수 있다.
  * Ex) methodA(int a, int b, int c); methodB(int a, int b, int c); 
  * => methodA(Price price); methodB(Price price);
    * int a, int b, int c 가 Price 가 가지는 변수인 경우 하나의 Object로 묶어서 표현한다.

### 3. Preserve Whole Object
* 파라미터를 줄이고 Record 명으로 파라미터의 의미를 부여할 수 있다.
  * Ex) method(int a, int b, int c, int d) => method(Price price, int d)
  * 사실 Introduce Parameter Object 와 Preserve Whole Object 는 구현상 다를것이 없다. 

## Replace Function with Command
* Command Pattern 을 사용하기
  * undo 기능 구현 가능하다.
  * 인터페이스나 상속등을 사용해 기능을 확장 할 수 있다.
  * 필요한 파라미터를 constructor 에서 받고 Field 로 올려 구현한다.
* 대부분의 경우 Command 보다는 Method를 사용한다.

## Decompose Conditional
* 조건문의 경우 보통 코드가 길어지고 Intention 보다는 Implementation 이된다.
* 조건문 안의 기능들을 Extract Function 할 수 있다.
* Ternary operation(삼항연산자) 더 줄일 수 있다.

## Split Loop
* 루프 안의 서로 다른 기능을 Extract Function 한다.
* 특징은 for 문도 같이 Extract 하여 Function으로 옮겨진다는 것이다.
* 성능상의 문제가 될 수 도 있겠지만 사실 O(n) 이나 O(2n)은 같으며 리팩토링과 성능최적화는 별개로 봐야한다.
* 리팩토링 후 문제가 있을시 최적화하는 것이 맞다.

```java
class BeforeExample {
    public static void main(String[] args) {
        int sum = 0;
        int product = 1;
        for(int i = 1; i <= 100; i++) {
            sum += i;
            product *= i;
        }
        System.out.println("sum from 1 to 100:" + sum);
        System.out.println("product from 1 to 100:" + product);
    }
}
```
```java
class AfterExample {
    public static void main(String[] args) {
        System.out.println("sum from 1 to 100:" + getSum());
        System.out.println("product from 1 to 100:" + getProduct());
    }
    
    private int getSum() {
      int sum = 0;
      for(int i = 1; i <= 100; i++) {
        sum += i;
      }
      return sum;
    }

  private int getProduct() {
    int product = 1;
    for(int i = 1; i <= 100; i++) {
      product *= i;
    }
    return product;
  }
}
```

## Replace Conditional with Polymorphism
* Conditional(if or switch) => Polymorphism (다형성)
* State Pattern 사용

```java
class ConditionalExample {
  public void typeFunction(Type type) {
    switch (type) {
      case TypeA -> functionA();
      case TypeB -> functionB();
      default -> throw new IlligalArgumentException();
    }
  }
}
```
```java
class PolymorphismExample {
    Type type;
    PolymorphismExample(Type type) {
        this.type = type; 
    }
    
    public void typeFunction() {
        type.function();
    }
}

class TypeA extends Type {
    public void function() {
        functionA();
    }
}
class TypeB extends Type {
  public void function() {
    functionB();
  }
}
```