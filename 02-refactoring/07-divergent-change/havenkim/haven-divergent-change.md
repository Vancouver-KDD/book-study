# Divergent Change

If one of the module gets changed in different ways for different reasons, you whiff Divergent Change.

It means that the module wears many hats so that it gets affected by lots of different context and reasons.

In the meantime, **Shotgun Surgery** is a smell when an update affects to other modules cascadingly.

Each context better remain separate and you should not have to care about other context in a matter of coupling.

### 1. Split Phase (154p) ###

Intermingled context in a single function is very fragile when a complicated logic change comes in.

We can separate the phase of each context into a function and use intermediate data object to fill the communication gap.

##### Examples

```ts
interface Product {
    name: string;
    basePrice: number;
    discountRate: number;
    discountQuantityLimit: number;
}

interface ShippingMethod {
    discountPriceTarget: number;
    discountedFee: number;
    feePerCase: number;
}

// Before
function priceOrder(product: Product, quantity: number, shippingMethod: ShippingMethod): number {
    const baseTotalPrice = product.basePrice * quantity;
    const discountedPrice = Math.max(quantity - product.discountQuantityLimit, 0)
        * (product.basePrice * product.discountRate);
    const shippingPerCase = (baseTotalPrice > shippingMethod.discountPriceTarget)
        ? shippingMethod.discountedFee : shippingMethod.feePerCase;
    const shippingCost = quantity * shippingPerCase;
    const finalPrice = baseTotalPrice - discountedPrice + shippingCost;
    return finalPrice;
}

// After
interface PriceData {
    baseTotalPrice: number;
    quantity: number;
    totalDiscount: number;
}

function priceOrder(product: Product, quantity: number, shippingMethod: ShippingMethod): number {
    const priceData = calcPriceData(product, quantity);
    const shippingCost = calcShippingCost(priceData.baseTotalPrice, quantity, shippingMethod);
    return priceData.baseTotalPrice - priceData.totalDiscount + shippingCost;
}

function calcShippingCost(totalPrice: number, quantity: number, shippingMethod: ShippingMethod): number {
    const shippingPerCase = (totalPrice > shippingMethod.discountPriceTarget)
        ? shippingMethod.discountedFee : shippingMethod.feePerCase;
    return quantity * shippingPerCase;
}

function calcPriceData(product: Product, quantity: number): PriceData {
    const baseTotalPrice = product.basePrice * quantity;
    const totalDiscount = Math.max(quantity - product.discountQuantityLimit, 0)
        * (product.basePrice * product.discountRate);
    return { baseTotalPrice, totalDiscount };
}
```