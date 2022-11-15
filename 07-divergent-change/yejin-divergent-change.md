# Divergent change

# Quick Preview
You can find it when you have to change a class many times because of other changes that are made of outside of class.  
If an object does too much, it might change in different directions.
You can fix it by separating by its context so that it can have a single responsibility .  

하나의 모듈이 다른 부분 변경시마다 너무 자주 수정되어한다면 Divergent change code smell을 찾을 수 있다.   
한 모듈이 너무 많은 일을 한다면  (=너무 많은 responsibilites 있다면 ) 중구난방으로 수정이 필요해 제대로 관리할 수 없다.   
이때 context 별로 분리해서 single responsibility만 가지도록 한다.   
  
## 1. Split phase
### Motivation
One of the neatest ways to do a split like this is to divide the behavior into two sequential phases. 
If your function follows sequential logic,  you can seperate it one by one. 
Probably the most obvious example is the complier. It takes code in a programming language, split into some chunks, 
and turn it into execuable form.

하는 일이 sequential 하다면 sequencial phase 별로 분리한다.   
가장 대표적인 예는 compiler 이다.   프로그래밍 랭귀지로 된 코드를 읽음, 청크로 자름, executable 한 꼴로 바꿈 이런 논리적 순서의 sequence를 말한다.  

### Mechanics
1. Extract the second phase code into its own function.
2. Introduce a data structure as an additional argument to the extracted function.
3. Examine each parameter of the extracted second phase. If it is used by first phase, move it to the intermediate data structure. Test after each move.
Sometimes, a parameter should not be used by the second phase. 
In this case, extract the results of each usage of the parameter into a field of the intermediate data structure and use Move Statements to Callers on the line that populates it.
4. Apply Extract Function on the first-phase code, returning the intermediate data structure.
5. It's also reasonable to extract the first phase into a transformer object.

### Examples 
```javascript
//before 
  function priceOrder(product, quantity, shippingMethod) {
    const basePrice = product.basePrice * quantity;  // 기본 가격 설정
    // 할인율 설정
    const discount = Math.max(quantity - product.discountThreshold, 0)
            * product.basePrice * product.discountRate;
    // 조건별 시핑 차지 결정 
    const shippingPerCase = (basePrice > shippingMethod.discountThreshold)
            ? shippingMethod.discountedFee : shippingMethod.feePerCase; 
    // 시핑 차지 확정 
    const shippingCost = quantity * shippingPerCase;
    // 최종가 결정 
    const price =  basePrice - discount + shippingCost;
    //최종가 리턴
    return price;
  }
  
// after
//1. 조건별 shipping fee 결정 하는 Function 분리 => applyShipping function (Extract function mechanic)
function priceOrder(product, quantity, shippingMethod) {
    const basePrice = product.basePrice * quantity;
    const discount = Math.max(quantity - product.discountThreshold, 0)
        * product.basePrice * product.discountRate;
    const price =  applyShipping(basePrice, shippingMethod, quantity, discount);
    return price;
}
function applyShipping(basePrice, shippingMethod, quantity, discount) {
    const shippingPerCase = (basePrice > shippingMethod.discountThreshold)
        ? shippingMethod.discountedFee : shippingMethod.feePerCase;
    const shippingCost = quantity * shippingPerCase;
    const price =  basePrice - discount + shippingCost;
    return price;
    
    // 2. extract function 에 넘길 데이터 스트럭쳐를 만든다. 
    function priceOrder(product, quantity, shippingMethod) {
        const priceData = calculatePricingData(product, quantity);
        const price =  applyShipping(priceData, shippingMethod);
        return price;
    }
    function calculatePricingData(product, quantity) {
        const basePrice = product.basePrice * quantity;
        const discount = Math.max(quantity - product.discountThreshold, 0)
            * product.basePrice * product.discountRate;
        return {basePrice: basePrice, quantity: quantity, discount:discount};
    }
    function applyShipping(priceData, shippingMethod) {
        const shippingPerCase = (priceData.basePrice > shippingMethod.discountThreshold)
            ? shippingMethod.discountedFee : shippingMethod.feePerCase;
        const shippingCost = priceData.quantity * shippingPerCase;
        const price =  priceData.basePrice - priceData.discount + shippingCost;
        return price;
    }

    // tidy it 
    function priceOrder(product, quantity, shippingMethod) {
        const priceData = calculatePricingData(product, quantity);
        return applyShipping(priceData, shippingMethod);
    }

    function calculatePricingData(product, quantity) {
        const basePrice = product.basePrice * quantity;
        const discount = Math.max(quantity - product.discountThreshold, 0)
            * product.basePrice * product.discountRate;
        return {basePrice: basePrice, quantity: quantity, discount:discount};
    }
    function applyShipping(priceData, shippingMethod) {
        const shippingPerCase = (priceData.basePrice > shippingMethod.discountThreshold)
            ? shippingMethod.discountedFee : shippingMethod.feePerCase;
        const shippingCost = priceData.quantity * shippingPerCase;
        return priceData.basePrice - priceData.discount + shippingCost;
    }
```
## Move function 
### Motivation 
If you want to make code understandable and easy to change, you should consider its modularity as a top priority.   
For that, make sure to  software elements which have the same context are grouped together.

수정하기 쉽고 이해하기 쉬운 코드를 위해서는 modularity 를 확보해야한다.   
이를 위해선 같은 context 를 가진 function 들을 모아서 module 로 만든다.

### Mechanics 
1. Examine all the program elements used by the chosen function in its current context. Consider whether they should move too.  
2. Check if the chosen function is a polymorphic method.
->  In OOP language like java, you have to look around if it's used in super or subclass. 
3. Copy the function to the target context. Adjust it to fit in its new home.
4. Figure out how to reference the target function from the source context.
5. Turn the source function into a delegating function.
6. If it fits well, delete source function.


### Example
```javascript
//before
function trackSummary(points) {
    const totalTime = calculateTime();
    const totalDistance = calculateDistance();
    const pace = totalTime / 60 /  totalDistance ;
    return {
        time: totalTime,
        distance: totalDistance,
        pace: pace
    };

    function calculateDistance() {
        let result = 0;
        for (let i = 1; i < points.length; i++) {
            result += distance(points[i-1], points[i]);
        }
        return result;
    }
    function distance(p1,p2) { ... }
    function radians(degrees) { ... }
    function calculateTime() { ... }
}

// 1. calculateDistance function를 바깥으로 이동한다.
function trackSummary(points) {
    const totalTime = calculateTime();
    const totalDistance = calculateDistance();
    const pace = totalTime / 60 /  totalDistance ;
    return {
        time: totalTime,
        distance: totalDistance,
        pace: pace
    };

    function calculateDistance() {
        let result = 0;
        for (let i = 1; i < points.length; i++) {
            result += distance(points[i-1], points[i]);
        }
        return result;
    }
    function distance(p1,p2) { ... }
    function radians(degrees) { ... }
    function calculateTime() { ... }
}
// 바깥으로 복사한 function  아직 원래 function 있기에 이름 구분하고자 top_을 붙였다. 
function top_calculateDistance() {
    let result = 0;
    for (let i = 1; i < points.length; i++) {
        result += distance(points[i-1], points[i]);
    }
    return result;
}

//2. 필요한 parameter, 호출하는 function 을 전달해준다. 
function top_calculateDistance(points) {
    let result = 0;
    for (let i = 1; i < points.length; i++) {
        result += distance(points[i-1], points[i]);
    }
    return result;
    // distance 는 이 Function 안에서만 사용되고,  radians function 은 Distance 안에서만 사용된다. 
    function distance(p1,p2) { ... }
    function radians(degrees) { ... }
    
}

//3. 원래 function 의 로직 부분에 새로 복사한 function 불러오도록 해 잘 작동하는지 확인한다. 
function trackSummary(points) {
    const totalTime = calculateTime();
    const totalDistance = calculateDistance();
    const pace = totalTime / 60 /  totalDistance ;
    return {
        time: totalTime,
        distance: totalDistance,
        pace: pace
    };

    function calculateDistance() {
        return top_totalDistance();
    }
    function distance(p1,p2) { ... }
    function radians(degrees) { ... }
    function calculateTime() { ... }
}

//4. 잘 실행된다면 원래 있던 function 을 삭제하고 이름을 변경한다. (필요하다면 
function trackSummary(points) {
    const totalTime = calculateTime();
    const totalDistance = totalDistance();
    const pace = totalTime / 60 /  totalDistance ;
    return {
        time: totalTime,
        distance: totalDistance,
        pace: pace
    };

    function calculateTime() { ... }
}



```


## Extract class

### Motivation 
If a class has too many responsibilities or methods, it may need to be split. 
Especially when some data and methods can be grouped together, you can extract them as a new class.
Check whether other methods and fields are totally fine after remove some data and method.  

한 클래스가 하는 일이 너무 많아지거나, 데이터나 메서드가 많아진다면 분리하는게 좋다.  
일부 데이터와 메서드를 그룹 지을 수 있다면 더욱 개별 class 로 분리한다.  
분리한다고 생각했을 때 다른 메서드나 필드에 문제가 없다면 extract class 해도 된다는 신호라고 생각해도 된다.   

### Mechanics
1. Decide how to split the responsibilities of the class.
2. Create a new child class to express the split-off responsibilities.
3. Create an instance of the child class when constructing the parent and add a link from parent to child.
4. Use move fields and move function mechanism.
5. Review the interfaces of both classes, remove unneeded methods, change names to better fit the new circumstances.

### Example
~~~javascript
// before 
class Person {
    get name() {
        return this._name;
    }

    set name(arg) {
        this._name = arg;
    }

    get telephoneNumber() {
        return `(${this.officeAreaCode}) ${this.officeNumber}`;
    }

    get officeAreaCode() {
        return this._officeAreaCode;
    }

    set officeAreaCode(arg) {
        this._officeAreaCode = arg;
    }

    get officeNumber() {
        return this._officeNumber;
    }

    set officeNumber(arg) {
        this._officeNumber = arg;
    }
}

//after
class Person {
    constructor() {
        this._telephoneNumber = new TelephoneNumber();
    }

    get telephoneNumber() {return this._telephoneNumber.telephoneNumber;}
    get officeAreaCode()    {return this._telephoneNumber.areaCode;}
    set officeAreaCode(arg) {this._telephoneNumber.areaCode = arg;}
    get officeNumber()    {return this._telephoneNumber.number;}
    set officeNumber(arg) {this._telephoneNumber.number = arg;}
}

class TelephoneNumber {
    get areaCode()    {return this._areaCode;}
    set areaCode(arg) {this._areaCode = arg;}
    get number()    {return this._number;}
    set number(arg) {this._number = arg;}

    get telephoneNumber() {return `(${this.officeAreaCode}) ${this.officeNumber}`;}
}



~~~