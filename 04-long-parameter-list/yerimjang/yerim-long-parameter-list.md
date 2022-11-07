#Long Parameter List
A long list of parameters might happen after several types of algorithms are merged in a single method. A long list may have been created to control which algorithm will be run and how.
>Don’t get rid of parameters if doing so would cause unwanted dependency between classes.

###1. Replace Parameter with Query (324)
#####Example
```typescript
//Before
let basePrice = quantity * itemPrice;
const seasonDiscount = this.getSeasonalDiscount();
const fees = this.getFees();
const finalPrice = discountedPrice(basePrice, seasonDiscount, fees);

//After
let basePrice = quantity * itemPrice;
let finalPrice = discountedPrice(basePrice);
```

###2. Preserve Whole Object (319)

###3. Introduce Parameter Object (140)

###4. Remove Flag Argument (314)
#####Example
```typescript
//Before
setDimension(name, value): void {
  if (name === 'height') {
    this.height = value;
    return;
  }
  if (name === 'width') {
    this.width = value;
    return;
  }
}

//After
setHeight(value): void {
  this.height = value;
}
setWidth(value): void {
  this.width = value;
}
```

###5. Combine Functions into Class (144)

###Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
- https://refactoring.guru/smells/long-method
