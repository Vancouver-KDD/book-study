# Alternative Classes with Different Interfaces
If there is alternative classes which does same logics, we can swap it with another one.

## Solution
### Move Function
```ts
class FoodDelivery {
    function printInvoice() {
        const totalPrice = this.order.totalPrice();
        const location = this.order.location()
        console.log("Invoice Summary")
        console.log(`Total price: ${totalPrice}`);
        console.log(`Address: ${location}`);
    }
}

class Order {
    get totalPrice () {
        return this.itemList.reduce((sum, item) => sum + item.price);
    }
    get location () {
        return this.address;
    }
}

class OrderWithInvoice {
    get totalPrice () {
        return this.itemList.reduce((sum, item) => sum + item.price);
    }
    get location () {
        return this.address;
    }
    function printInvoice() {
        const totalPrice = this.totalPrice();
        const location = this.location()
        console.log("Invoice Summary")
        console.log(`Total price: ${totalPrice}`);
        console.log(`Address: ${location}`);
    }
}
```
