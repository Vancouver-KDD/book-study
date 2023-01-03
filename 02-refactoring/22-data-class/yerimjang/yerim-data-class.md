# Data Class
A data class refers to a class that contains only fields and crude methods for accessing them (getters and setters). These are simply containers for data used by other classes. These classes don’t contain any additional functionality and can’t independently operate on the data that they own.

### 1. Encapsulate Record (162)
##### Example
```typescript
//Before
class Person {
  name: string;
}

//After
class Person {
  private _name: string;

  get name() {
    return this._name;
  }
  setName(arg: string): void {
    this._name = arg;
  }
}
```

### 2. Remove Setting Method (331)

### 3. Move Function (198)

### 4. Extract Function (106)

### 5. Split Phase (154)
##### Example
```typescript
//Before
const orderData = orderString.split(/\s+/);
const productPrice = piceList(orderData[0].split("-")[1]);
const orderPrice = parseInt(orderData[1]) * productPrice;

//After
const orderRecord = parseOrder(order);
const orderPrice = price(orderRecord, priceList);

function parseOrder(aString) {
  const value = aString.split(/\s+/);
  return ({
    productID: values[0].splict("-")[1],
    quantity: parseInt(values[1])
  });
}

function price(order, priceList) {
  return order.quantity * priceList[order.productID];
}
```

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
