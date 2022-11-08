# Long Parameter List

프로그래밍을 처음 배울 때, 우리는 function이 필요로 하는 모든 것은 parameter로 받아야 한다고 배운다. 그렇지 않으면 global data를 사용해야 하는데, global data는 언제든 악마로 변할 수 있다😈. 하지만 너무 많은 parameter들은 그 자체로도 혼란을 주기도 한다.

- 주어진 parameter를 통해 다른 parameter 정보를 가져올 수 있다면 [Replace Parameter with Query](#1-replace-parameter-with-query)를 적용할 수 있다

- [Preserve Whole Object](#2-preserve-whole-object)를 적용해 한 Data structure에서 많은 정보를 가져오기 보다는 해당 data structure를 직접 전달할 수 있다

- 공통적인 parameter들이 지속적으로 함께 사용된다면 [Introduce Parameter Object](#3-introduce-parameter-object)를 적용해 여러 parameter들을 한 object로 변경해 전달 할 수 있다

- Parameter가 다른 behaviour로 나눠지는 flag 역할을 한다면 [Remove Flag Argument](#4-remove-flag-argument)를 적용할 수 있다

- 특히 여러 function들이 같은 혹은 비슷한 parameter value들을 공유한다면 [Combine Functions into Class](#5-combine-functions-into-class)를 적용해 클래스를 통해서 parameter를 줄일 수 있다


### 1. Replace Parameter with Query

**1.1 Motivation**

Parameter는 function이 매 번 다르게 행동할 수 있도록 만드는 요소이다. 어떤 코드에서든, 중복을 줄이는 것이 좋고 parameter의 수는 줄이는 것이 이해하기가 쉽다.

Function을 호출할 때 function 안에서 쉽게 얻어낼 수 있는 정보를 parameter로 전달한다면, 데이터의 중복이며 사용자 입장에서는  불필요하게 어떤 parameter를 전달해야 할 지 생각해야 하는 복잡함이 생긴다.

Parameter가 존재할 때는 어떤 값을 전달해야할 지 정하는 주체가 사용자다. 불필요한 parameter를 제거함으로써, 우리는 해당 responsibility를 사용자가 아닌 function에게 준다. 

주의해야할 점으로는 function 내에 불필요한 dependency가 생길 수 있다는 점이다. 보통 function안에서 문제가 될 수 있는 다른 function을 호출하거나 ***access something within a receiver object*** 이런 경우에는 리팩토링을 하지 않는 걸 선호한다.

보통 Replace Parameter with Query가 안전하게 사용되는 경우는 주어진 parameter를 통해 다른 parameter를 직접 얻어낼 수 있는 경우이다. 이 때는 굳이 두 개의 parameter를 전달할 이유가 거의 없다.

**1.2 Mechanics**

- 필요하다면, `Extract Function`을 적용한다
- Function안에 존재하는 parameter에 대한 reference들을 해당 parameter를 가져오는 새로운 expression으로 변경한다
- `Change Function Declaration`을 적용해 해당 parameter를 제거한다

**1.3 Examples**
```javaScript
class Order {
  get finalPrice() {
    const basePrice = this.quantity * this.itemPrice;
    let discountLevel;
    if (this.quantity > 100) discountLevel = 2;
    else discountLevel = 1;
    return this.discountedPrice(basePrice, discountLevel);
  }

  discountedPrice(basePrice, discountLevel) {
    switch (discountLevel) {
      case 1: return basePrice * 0.95;
      case 2: return basePrice * 0.9;
    }
  }
}

// Replace Temp with Query를 적용해 function을 간단한게 만든다
class Order {
  get finalPrice() {
  const basePrice = this.quantity * this.itemPrice;
  return this.discountedPrice(basePrice, this.discountLevel);
  }

  get discountLevel() {
    return (this.quantity > 100) ? 2 : 1;
  }

  discountedPrice(basePrice, discountLevel) {
    switch (discountLevel) {
      case 1: return basePrice * 0.95;
      case 2: return basePrice * 0.9;
    }
  }
}

// 이제는 discountLevel을 function을 통해 직접 얻을 수 있으므로 discountedPrice에 전달 할 필요가 없어졌다
class Order {
  get finalPrice() {
    const basePrice = this.quantity * this.itemPrice;
    return this.discountedPrice(basePrice);
  }

  get discountLevel() {
    return (this.quantity > 100) ? 2 : 1
  }
  
  discountedPrice(basePrice) {
    return basePrice * this.discountLevel;
  }
}
```

### 2. Preserve Whole Object
**1.1 Motivation**
여러 data를 다른 source에서 얻은 후에 function으로 전달하는 경우, 해당 source를 직접 전달 해 function안에서 필요할 때 필요한 data를 직접 추출해 사용할 수 있도록 한다

**1.2 Mechanics**

- 원하는 parameter를 가진 빈 function을 만든다 (Tip: 마지막에 이름을 바꿀 수 있도록 쉽게 찾을 수 있는 이름을 사용하자)
- 새로운 function안에서 원래의 function을 호출하되, 새로운 parameter들을 사용해 전달한다
- Run static checks
- 원래의 function이 사용 되고 있는 자리를 모두 새로운 function으로 변경하고, 매 번 변경할 때마다 테스트한다
  이 때, 해당 parameter들을 사용하는 코드들이 필요없어질 수 있다, 이 때 `Remove Dead Code`를 적용한다
- 모두 새로운 function으로 수정 되었다면, `Inline Function`을 원래의 function에 적용한다
- 새로운 function의 이름을 바꾸고 호출 되는 곳도 모두 수정해준다

**1.3 Examples**

```js
class HeatingPlan {
  withinRange(bottom, top) {
    return (bottom >= this._temperatureRange.low) && (top <= this._temperatureRange.high); 
  }
}

const low   = aRoom.daysTempRange.low;
const hight = aRoom.daysTempRange.high;
if (!aPlan.withinRange(low, high))
  alerts.push("Room temperature went outside range");

// 1. 원하는 parameter를 가진 빈 function을 만든다 
class HeatingPlan {
  ...
  xxNewWithinRange(aNumberRange) {}
  ...
}

// 2. 새로운 function안에서 원래의 function을 호출하되, 새로운 parameter들을 사용해 전달한다
class HeatingPlan {
  ...
  xxNewWithinRange(aNumberRange) {
    return this.withinRange(aNumberRange.low, aNumberRange.hight);
  }
  ...
}

// 3. Static checks

// 4. 원래의 function이 사용 되고 있는 자리를 모두 새로운 function으로 변경하고, 매 번 변경할 때마다 테스트한다
//    `Remove Dead Code`를 적용한다
class HeatingPlan {
  ...
}

const low   = aRoom.daysTempRange.low;
const hight = aRoom.daysTempRange.high;
if (!aPlan.xxNEWwithinRange(aRoom.daysTempRange))
  alerts.push("Room temperature went outside range");


// 5. 모두 새로운 function으로 수정 되었다면, `Inline Function`을 원래의 function에 적용한다
class HeatingPlan {
  ...
  xxNEWwithinRange(aNumberRange) {
  return (aNumberRange.low >= this._temperatureRange.low) &&
    (aNumberRange.high <= this._temperatureRange.high);
  }
  ...
}

// 6. 새로운 function의 이름을 바꾸고 호출 되는 곳도 모두 수정해준다
class HeatingPlan {
  withinRange(aNumberRange) {
  return (aNumberRange.low >= this._temperatureRange.low) &&
    (aNumberRange.high <= this._temperatureRange.high);
  }
}

if (!aPlan.withinRange(aRoom.daysTempRange))
    alerts.push("room temperature went outside range");
```

# ***Note: A Variation to Create the New Function***


### 3. Introduce Parameter Object

**1.1 Motivation**
같은 data들이 보통 함께 묶여 여러 function에서 사용되는 것을 보면 하나의 data structure로 바꿔줄 수 있다

**1.2 Mechanics**

- 적당한 structure가 없다면, 만들어라
- 테스트한다
- `Change Function Declaration`을 적용해서 새로운 structure를 이용할 parameter를 추가한다
- 테스트한다
- 각 caller에 새로운 structure의 instance를 전달 할 수 있도록 수정한 후 테스트한다
- 원래의 element를 새로운 structure로 수정한 후 필요 없는 parameter는 지워준다
- 테스트한다

**1.3 Examples**
```js
// 1. Data를 합치기 위해 class를 정의한다
class NumberRange {
  constructor(min, max) {
      this._data = {min: min, max: max};
    }
  get min() {return this._data.min;}
  get max() {return this._data.max;}
}

// 2. Change Function Declaration을 적용해 새로 만든 object를 parameter로 전달한다
function readingsOutsideRange(station, min, max, range) {
  return station.readings
    .filter(r => r.temp < min || r.temp > max);
}

// Caller side
const range = new NumberRange(operatingPlan.temperatureFloor, operatingPlan.temperatureCeiling);
alerts = readingsOutsideRange(station,
                              operatingPlan.temperatureFloor,
                              operatingPlan.temperatureCeiling,
                              range);

// 3. 테스트한다
// 4. 새로운 parameter를 사용하도록 function을 수정하고 caller들도 수정한다
function readingOutsideRange(station, range) { // mix, max 제거
  return station.readings.filter(r => r < range.min || r > range.max); // use a new object (range)
}

// Caller side
const range = new NumberRange(operatingPlan.temperatureFloor, operatingPlan.temperatureCeiling);
alerts = readingsOutsideRange(station, range);

// 이것으로 Introduce Parameter Object Refactoring은 끝이다. 
// 이렇게 parameter를 object로 변겨함으로써 얻을 수 있는 이점이 있는데 
// 아래와 같이 우리가 수정한 function이 가진 behaviour를 새로 만들어진 
// object 안으로 옮길 수 있다는 점이다

class NumberRange {
  ...
  contains(arg) { return (arg >= this.min && arg <= this.max>);}
  ...
}

function readingsOutsideRange(station, range) {
  return station.readings
    .filter(r => !range.contains(r.temp));
}
```

### 4. Remove Flag Argument
**1.1 Motivation**
Flag argument는 caller가 사용하는 function argument로써 호출한 function 안의 어떤 logic이 실행되야 하는 지 결정한다. 

Flag argument는 function을 어떻게 사용하는지, 어떤 logic들이 있는지 이해하는 과정을 복잡하게 만든다. 특히, flag argument가 boolean일 때는 더 심한데, true 와 false가 무엇을 의미하는 지 전혀 알 수 없기 때문이다. 이 경우에는 각각의 function을 만들어주는 게 명확하다.

한 function 안에 flag argument가 여러 개인 경우 사용될 수도 있는데, 각 조합마다 새로운 function을 만들어줄 수는 없는 노릇이기 때문이다. 하지만 이 경우에도, 해당 function이 너무 많은 logic을 가지고 있을 수 있다는 하나의 신호일 수 있기 때문에 간단한 여러 function들로 나눌 수는 없는 지 주의깊게 보도록 하자.

**1.2 Mechanics**

- 각 value에 해당하는 function을 만들어준다 
  (원래 function이 확실히 다른 조건들을 가지고 있다면 `Decompose Conditional`을 적용해서 새로운 function들을 만들어준다. 그렇지 않은 경우에는 wrapper function을 만들어준다)

- Flag argument를 사용하는 caller들을 모두 새로 만든 function으로 수정한다

**1.3 Examples**

```js
aShipment.deliveryDate = deliveryDate(anOrder, true);
aShipment.deliveryDate = deliveryDate(anOrder, false);

function deliveryDate(anOrder, isRush) {
  if (isRush) {
    let deliveryTime;
    if (["MA", "CT"]     .includes(anOrder.deliveryState)) deliveryTime = 1;
    else if (["NY", "NH"].includes(anOrder.deliveryState)) deliveryTime = 2;
    else deliveryTime = 3;
    return anOrder.placedOn.plusDays(1 + deliveryTime);
  }
  else {
    let deliveryTime;
    if (["MA", "CT", "NY"].includes(anOrder.deliveryState)) deliveryTime = 2;
    else if (["ME", "NH"] .includes(anOrder.deliveryState)) deliveryTime = 3;
    else deliveryTime = 4;
    return anOrder.placedOn.plusDays(2 + deliveryTime);
  }
}

// Decompose Conditional 적용
function deliveryDate(anOrder, isRush) {
  if (isRush) return rushDeliveryDate(anOrder);
  else        return regularDeliveryDate(anOrder);
}

// 1. 각 value에 해당하는 function을 만들어준다
function rushDeliveryDate(anOrder) {...}
function regularDeliveryDate(anOrder) {...}

// 2. caller 수정
aShipment.deliveryDate = rushDeliveryDate(anOrder);
aShipment.deliveryDate = regularDeliveryDate(anOrder);

// Decompose Conditional 적용 불가할 때 (조건들이 서로 dependency가 있을 때)
function deliveryDate(anOrder, isRush) {
  let result;
  let deliveryTime;
  if (anOrder.deliveryState === "MA" || anOrder.deliveryState === "CT")
    deliveryTime = isRush? 1 : 2;
  else if (anOrder.deliveryState === "NY" || anOrder.deliveryState === "NH") {
    deliveryTime = 2;
    if (anOrder.deliveryState === "NH" && !isRush)
      deliveryTime = 3;
  }
  else if (isRush)
    deliveryTime = 3;
  else if (anOrder.deliveryState === "ME")
    deliveryTime = 3;
  else
    deliveryTime = 4;
  result = anOrder.placedOn.plusDays(2 + deliveryTime);
  if (isRush) result = result.minusDays(1);
  return result;
}

// Wrapper function을 이용
function rushDeliveryDate   (anOrder) {return deliveryDate(anOrder, true);}
function regularDeliveryDate(anOrder) {return deliveryDate(anOrder, false);}
```

### 5. Combine Functions into Class
**1.1 Motivation**
같은 data가 여러 function들에서 사용될 때(보통 argument로 전달될 때), 우리는 이 function들을 class로 묶을 수 있지 않을까 생각해보아야 한다.  

**1.2 Mechanics**
- `Encapsulate Record`를 적용하여 function들이 공유하는 data를 클래스 안에 포함시켜 encapsulate 한다
- 해당 function들을 새로 만든 클래스 안으로 옮긴다
- 해당 data를 manipulate하는 logic들은 `Extract Function`을 적용해 역시 새로운 클래스 안으로 옮긴다

**1.3 Examples**
```js
reading = {customer: "ivan", quantity: 10, month: 5, year: 2017};

// Client 1
const aReading = acquireReading();
const baseCharge = baseRate(aReading.month, aReading.year) * aReading.quantity;
// Client 2
const aReading = acquireReading();
const base = (baseRate(aReading.month, aReading.year) * aReading.quantity);
const taxableCharge =  Math.max(0, base - taxThreshold(aReading.year));
// Client 3
const aReading = acquireReading();
const basicChargeAmount = calculateBaseCharge(aReading);

// Existed function, but we missed using this function for client 1 & 2
function calculateBaseCharge(aReading) {
  return  baseRate(aReading.month, aReading.year) * aReading.quantity;
}

// 1. Encapsulate record to a class
class Reading {
  constructor(data) {
    this._customer = data.customer;
    this._quantity = data.quantity;
    this._month = data.month;
    this._year = data.year;
  }
  get customer() {return this._customer;}
  get quantity() {return this._quantity;}
  get month()    {return this._month;}
  get year()     {return this._year;}
}

// 2. Move the original function into the class
class Reading {
  ...
  get baseCharge() { // Renamed
    return  baseRate(this.month, this.year) * this.quantity;
  }
}

// Update callers
const rawReading = acquireReading();
const aReading = new Reading(rawReading);
const basicChargeAmount = aReading.baseCharge;
```