# Mutable Data
* Copy data VS Reference data

## Encapsulate Variable (#Global Data)
## Split Variable
* 변수는 하나당 하나의 책임을 가지도록 한다
* 상수를 활용하라

```java
class Example1 {
    String perimeter;
    String area;
    
    public void printGeometry(double height, double width) {
        double temp = 2 * (height + width);
        System.out.println("Perimeter: " + temp);
        temp = height * width;
        System.out.println("Area: " + temp);
    }

    public int improvedPrintGeometry(double height, double width) {
        final double perimeter  = 2 * (height + width);
        System.out.println("Perimeter: " + perimeter);
        final double area = height * width;
        System.out.println("Area: " + area);
    }
}
```
```java
class Example2 {
    public double discount(double inputValue, int quantity) {
        if(inputValue > 50) inputValue -= 2;
        if(quantity > 100) inputValue -= 1;
        return inputValue;
    }

    public double improvedDiscount(double inputValue, int quantity) {
        double result = inputValue;
        if(result > 50) result -= 2;
        if(quantity > 100) result -= 1;
        return result;
    }
}
```
## Slide Statements (#Duplicated Code)
## Extract Function (#Duplicated Code)
## Query From Modifier
* 질의 함수와 변경 함수를 분리하라 (command-query separation)
  * 어떤 값을 리턴하는 함수는 사이드 이팩트가 없어야 한다.
    * Example Class의 getTotalOutstandingAndSendBill는 미지불 총액을 받는 함수지만 중간에 빌을 보내는 기능(사이드 이팩트)도 포함되어 테스트 하기도 어렵고 메소드를 다른곳으로 옮기기도 어렵다.
  * 그렇다면 Cash 기능은 분리되어야하는가? X
    * Cash 값은 질의 겨로가에 영향을 미치지 않는다 성능에만 영향을 미친다.

```java
class Example {
    public double getTotalOutstandingAndSendBill() {
        final double result = customer.getInvoices().stream()
                .map(Invoice::getAmount)
                .reduce((double) 0, Double::sum);
        sendBill();
        return result;
    }

    private void sendBill() {
        emailGateway.send(formatBill(customer));
    }
}

class ImprovedExample {
    public double getTotalOutstanding() {
        return customer.getInvoices().stream()
                .mapToDouble(Invoice::getAmount)
                .reduce((double) 0, Double::sum);
    }

    private void sendBill() {
        emailGateway.send(formatBill(customer));
    }
}
```

## Remove Setting Method
* Setter가 있다는 것은 변수가 변경될 수 있음을 의미한다.
* Constructor 에서 값을 설정하고 변경할 필요가 없다면 이를 제거 한다.

## Replace Derived Variable with Query
* 변경될 수 있는 데이터는 최대한 줄인다
* 계산해서 알아낼 수 있는 데이터는 제거한다 -> 별도 함수로 제공한다
  * 함수명으로 의도를 표현할 수 있다
  * 잘못된 값으로 수정될 수 있는 가능성을 제거할 수 있다
* 계산 해야하는 값들이 immutable 하다면 결과 값도 immutable 할 수 있다

```java
public class Example {
    private double production;
    private List<Double> adjustments = new ArrayList<>();

    public void applyAdjustment(double adjustment) {
        this.adjustments.add(adjustment);
        this.production += adjustment;
    }

    public double getProduction() {
        return this.production;
    }
}
```
````java
public class ImprovedExample {
    private List<Double> adjustments = new ArrayList<>();

    public void applyAdjustment(double adjustment) {
        this.adjustments.add(adjustment);
    }

    public double getProduction() {
      return this.adjustments.stream().mapToDouble(Double::valueOf).sum();
    }
}
````
## Combine Functions into Class (#Long Parameter List)
## Combine Functions into Transform
* Record 를 통해 데이터를 묶어 준다
* Combine Functions into Transform 를 했어도 그 데이터가 변경되어야 한다면 Combine Functions into Class 해야한다
* 변경된 데이터가 Immutable 하다면 Immutable field 생성해 재 사용할 수 있다

## Change Reference to Value
* Reference Value VS Immutable Value
  * Record = final + constructor + setter 삭제 + equal/hash code 생성
  * 어떤 데이터의 변경 내용을 전파시키고 싶다면 Reference 아니라면 Value