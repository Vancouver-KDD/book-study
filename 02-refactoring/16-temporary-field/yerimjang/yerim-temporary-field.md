# Temporary Field
Oftentimes, temporary fields are created for use in an algorithm that requires a large amount of inputs. So instead of creating a large number of parameters in the method, the programmer decides to create fields for this data in the class. These fields are used only in the algorithm and go unused the rest of the time.

### 1. Extract Class (182)

### 2. Move Function (198)

### 3. Introduce Special Case (289)
##### Example
```typescript
//Before
if (customer == null) {
  plan = BillingPlan.basic();
}
else {
  plan = customer.getPlan();
}

//After
class NullCustomer extends Customer {
  isNull(): boolean {
    return true;
  }
  getPlan(): Plan {
    return new NullPlan();
  }
}

let customer = (order.customer != null) ? order.customer : new NullCustomer();
plan = customer.getPlan();
```


### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
- https://refactoring.guru/introduce-null-object
