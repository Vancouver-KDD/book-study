# Divergent Change
* 어떤 한 모듈이 (함수 또는 클래스가) 여러가지 이유로 다양하게 변경되어야 하는 상황
  * Ex) 새로운 타입의 프로세스가 추가되거나, DB가 추가 될때 동일한 클래스에서 여러 메소드를 수정해야힌다.
* Divergent Change VS Shotgun Surgery 
  * Divergent Change: 하나의 기능을 여러 기능으로 쪼개기
  * Shotgun Surgery: 여러개의 기능을 하나로 모으기
* 좋은 소프트웨어란?
  * Cohesion 높고 and Coupling 낮다 => Decoupling 

## Split Phase
* 여러 일을 하는 함수의 처리 과정을 각기 다른 단계로 구분할 수 있다.
  * Ex) 전처리 -> 주요 작업 -> 후처리
  * Ex) 컴파일러: 텍스트 읽어오기 -> 실행 가능한 형태로 변경 
* 서로 다른 데이터를 사용한다면? 
  * 단계를 나누는데 있어 중요한 단서가 될 수 있다. 
* Intermediate Data 를 만들어 단계를 구분하고 매개변수를 줄이는데 활 용할 수 있다.
  * Ex) 밑에 예제에서 Intermediate Data 는 OrderPrice
  
```java
class Example1 {
    public double getPriceOrder(Product product, int quantity, ShipmentMethod shipType) {
        final double basePrice = product.getPrice() * quantity;
        final double discount = getDiscountPrice(
                basePrice, product.getPromotion(), quantity);
        final double shipmentCost = shipType.getPrice(
                basePrice, discount, product.getSize(), product.getWeight()) * quantity;
        return basePrice - discount + shipmentCost;
    }
}

class Example2 {
    public double getPriceOrder(Product product, int quantity, ShipmentMethod shipType) {
        final OrderPrice price = calculateOrderPrice(product, quantity);
        return price.getTotalPrice() + getShipmentPrice(product, price, shipType);
    }
  
    private OrderPrice calculateOrderPrice(Product product, int quantity) {
        final double basePrice = product.getPrice() * quantity;
        final double discount = getDiscountPrice(basePrice, product.getPromotion(), quantity);
        return new OrderPrice(basePrice, discount);
    }
    
    private double getShipmentPrice(Product product, OrderPrice price, ShipmentMethod shipType) {
        return shipType.getPrice(
                price.getBasePrice(), price.getDiscount(), 
                product.getSize(), product.getWeight()) * quantity;
    }
}
```

## Move Function
* 관련있는 함수나 필드가 모여있어야 더 쉽게 찾고 이해할 수 있다 => High Cohesion 
* 함수를 옮겨야 하는 경우 
  * 해당 함수가 다른 문맥 (클래스)에 있는 데이터 (필드)를 더 많이 참조하는 경우.
  * 해당 함수를 다른 클라이언트 (클래스)에서도 필요로 하는 경우. 
* 함수를 옮겨갈 새로운 문맥 (클래스)이 필요한 경우 
  * Combine Functions info Class 또는 Extract Class 를 사용 
* 당장 함수를 옮길 적당한 위치를 찾기가 어렵다면, 그대로 두어도 괜찮다.

## Extract Function (#Duplicated Code)

## Extract Class
* Inline Class VS Extract Class
  * Inline Class: 클래스 합치기
  * Extract Class: 클래스 쪼개기
* 클래스가 다루는 Responsibility 가 처질 수 록 코드가 많아 진다.
* 클래스를 쪼개는 기준
  * 데이터나 메소드 중 일부가 매우 밀접한 관련이 있는 경우
  * 일부 데이터가 대부분 같이 바뀌는 경우
  * 데이터 또는 메소드 중 일부를 삭제한다면 어떻게 될 것인가? 
* 하위 클래스를 만들어 책임을 분산 하는 것도 가능
