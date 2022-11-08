# Long Parameter List
* 파라미터가 많으면 메서드의 역할을 이해하기 힘들다.
* 
## Replace Parameter with Query
* method(int a, int b, int c)의 경우 c 값을 a와 b의 조합으로 구할 수 있다면 method(int a, int b)로 파라미터를 줄일 수 있다.
* Ex) getPrice() vs getPrice2() vs getPrice3()
  * Replace temp with Query 와 다른거를 잘 모르겠다.
```java
class Example {
    int quantity = 10;
    int price = 100;
    
    public int getPrice() {
        int basePrice = this.price * this.quantity;
        int level = this.quantity > 100? 2: 1;
        return getDiscountPrice(basePrice, level);
    }
    
    private double getDiscountPrice(int basePrice, int level) {
        return level == 2? basePrice * 0.9 : basePrice * 0.95;
    }

    public int  getPrice2() {
        int basePrice = this.price * this.quantity;
        return getDiscountPrice(basePrice);
    }
    
    private int getLevel() {
        return his.quantity > 100? 2: 1;
    }
    
    private double getDiscountPrice(int basePrice) {
        return getLevel() == 2? basePrice * 0.9 : basePrice * 0.95;
    }

    public int  getPrice3() {
        int basePrice = this.price * this.quantity;
        return getDiscountPrice(basePrice);
    }

    private double getBasePrice() {
        return this.price * this.quantity;
    }

    private double getDiscountPrice() {
        return getLevel() == 2? getBasePrice() * 0.9 : getBasePrice() * 0.95;
    }
}
```

## Remove Flag Argument
* boolean 은 파라미터로 사용하지 않는다.
* 이유는 파라미터의 의미를 부여할 수 없기 때문이다.
* Ex) bookConcert(customer, false); bookConcert(customer, ture);
  * => bookConcert(customer); bookConcertForVIP(customer);

## Combine Functions into Class
* 비슷한 Parameter 들을 여러 메서드들에서 사용하고 있다면 그 메서드들을 모아 Class로 만든다.
* 비슷한 Parameter 들은 Feild로 올리면 Parameter가 줄어든다.
```java
class BeforeExample {
    
    int functionA(int a, int b);
    int functionB(int a, int b, int c);
    int functionC(int a, int b, int d);
    int otherFunctions1();
    int otherFunctions2();
    int otherFunctionsN();
}

class AfterExample {
    int otherFunctions1();
    int otherFunctions2();
    int otherFunctionsN();
}

class ABFunctions() {
    int a;
    int b;
    public ABFunctions(int a, int b) {
        this.a = a;
        this.b = b;
    }
    int functionA();
    int functionB(int c);
    int functionC(int d);
}
```