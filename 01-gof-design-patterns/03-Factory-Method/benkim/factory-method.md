# Factory method pattern
> 공장에서 물건을 만들 때, 설계도면이나 내부규칙에 따라 상품이 생산된다  
하지만 소비자는 디테일한 내부공정을 알필요가 없고, 주문만 하는 모습과 같은 개념  

![factory-method](./images/factory_method.png)
## Feature
- object 생성 코드를 client -> class 내부로 캡슐화
- object 생성 관련 코드가 변경되더라도 한군데에서만 수정가능

## BEFORE
```dart
import 'package:flutter_test/flutter_test.dart';


void main() {
  test('test', () async {
    PizzaType selectedPizza = PizzaType.HamMushroom;
    Pizza pizza;

    switch (selectedPizza) {
      case PizzaType.HamMushroom:
        pizza = HamAndMushroomPizza();
        break;

      case PizzaType.Deluxe:
        pizza = DeluxePizza();
        break;

      case PizzaType.Seafood:
        pizza = SeafoodPizza();
        break;
    }

    print(pizza.getPrice());
  });
}

enum PizzaType { HamMushroom, Deluxe, Seafood }

abstract class Pizza {
  double getPrice();
}

class HamAndMushroomPizza implements Pizza {
  double price = 10.5;

  @override
  double getPrice() {
    return price;
  }
}

class DeluxePizza implements Pizza {
  double price = 5.5;

  @override
  double getPrice() {
    return price;
  }
}

class SeafoodPizza implements Pizza {
  double price = 7.8;

  @override
  double getPrice() {
    return price;
  }
}

```


## AFTER factory method
```dart
import 'package:flutter_test/flutter_test.dart';

void main() {
  test('factory method', () async {
    
    Map<String, dynamic> json = {
      "type": PizzaType.HamMushroom,
      "orderNumber": "12345"
    };
    print(Pizza.pizzaFactory(json).getPrice());
  });
  
}

enum PizzaType { HamMushroom, Deluxe, Seafood }

abstract class Pizza {
  double getPrice();

  factory Pizza.fromJson(Map<String, dynamic> json) {
    switch (json["type"] as PizzaType) {
      case PizzaType.HamMushroom:
        return HamAndMushroomPizza();
      case PizzaType.Deluxe:
        return DeluxePizza(json["orderNumber"] as String);
      case PizzaType.Seafood:
        return SeafoodPizza();
      default:
        return HamAndMushroomPizza();
    }
  }
}

class HamAndMushroomPizza implements Pizza {
  double price = 10.5;

  @override
  double getPrice() {
    return price;
  }
}

class DeluxePizza implements Pizza {
  double price = 5.5;
  String orderNumber;
  DeluxePizza(this.orderNumber);
  @override
  double getPrice() {
    return price;
  }
}

class SeafoodPizza implements Pizza {
  double price = 7.8;

  @override
  double getPrice() {
    return price;
  }
}

```