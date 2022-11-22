# Shotgun Surgery

**Shotgun Surgery** is a smell when an update affects to other modules cascadingly.

It's very similar to Divergent Change but in the opposite way.

**Divergent Change** -> Many changes of different reasons made to a single class, wearing many hats.
**Shotgun Surgery** -> Single change affects many classes, large coupling.

![](./images/shotgun-surgery.png)
[Reference](https://refactoring.guru/smells/shotgun-surgery)

### 4. Combine Functions into Transform (149p) ###

Even though it mutates the object, it is better than having multiple fragments get shipped together all the time.

##### Example

```ts
// Before
function getReceipt(): number {
    const aReading = acquireReading();
    const base = (baseRate(aReading.month, aReading.year) * aReading.quantity);
    const taxableCharge = Math.max(0, base - taxThreshold(aReading.year));
    return base + taxableCharge;
}

// After
function getReceipt(): number {
    const aReading = acquireReading();
    const enrichedReading = updateReadingWithTax(aReading);
    return enrichedReading.baseCharge + enrichedReading.taxableCharge;
}

function calculateBaseCharge(reading: Reading): number {
    return baseRate(aReading.month, aReading.year) * aReading.quantity;
}

function updateReadingWithTax(originalReading: Reading): Reading {
    const result = _.cloneDeep(originalReading);
    result.baseCharge = calculateBaseCharge(result);
    result.taxableCharge = Math.max(0, result.baseCharge - taxThreshold(result.year));
    return result;
}

// With one unit test
it('check reading unchanged', function() {
  const baseReading = {customer: "ivan", quantity: 15, month: 5, year: 2017};
  const oracle = _.cloneDeep(baseReading);
  updateReadingWithTax(baseReading);

  // Make sure it didn't modify the original object
  assert.deepEqual(baseReading, oracle);
});
```