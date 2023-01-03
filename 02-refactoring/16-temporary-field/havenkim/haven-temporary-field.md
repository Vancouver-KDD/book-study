# Temporary Field

If one of a field is being used only in a specific circumstances, it means that the class is trying to cover lots of states at the same time while revealing details of each state outside.

This leads uncertainty of the class due to lack of logical explanation of the code. The reader will expect to use all the fields and will try to find reason of using them. If not, it will leave lots of question marks.

## Introduce Special Case

If a specific type is being used in a similar or same way in different places, we can simply introduce a specific function or an object to elaborate related information into one. Null Object Pattern is one of this case.

## Example

*Using a Transform* should be a bad option unless it gives you huge benefit since it is mutating your referenced object.

*Using an Object Literal* is also one of the option but you better ensure immutability of an object. If not, it will have the same problem as *Using a Transform*

Hence, most common approach will be *Using a class*

By using Null Object, it simplifies repeated type checking code but it reduces readability of the code.

```ts
//Before
class Customer {
    private _name: string;
    private _billingPlan: BillingPlan;
    get isUnknown():bool { return false; }
    get name(): string { return this._name; }
    get billingPlan(): BillingPlan { return this._billingPlan }
    set billingPlan(arg: BillingPlan) { this._billingPlan = arg; }
}
class Site {
    private _customer: Customer;
    get customer() { return this._customer; }
}
// Before TestCode
function changeToNewPlan(newPlan: BillingPlan) {
    const site = new Site();
    const aCustomer = site.customer;
    
    let customerName;
    if (aCustomer === "unknown") customerName = "occupant";
    else customerName = aCustomer.name;

    const plan = (aCustomer === "unknown") ? 
        registry.billingPlans.basic :
        aCustomer.billingPlan;
    
    if (aCustomer !== "unknown") aCustomer.billingPlan = newPlan;
}

//After
class Customer {
    private _name: string;
    private _billingPlan: BillingPlan;
    get isUnknown(): bool { return false; }
    get name(): string { return this._name; }
    get billingPlan(): BillingPlan { return this._billingPlan }
    set billingPlan(arg: BillingPlan) { this._billingPlan = arg; }
}
class UnknownCustomer {
    get isUnknown(): bool { return true; }
    get name(): string { return "occupant"; }
    get billingPlan(): BillingPlan {return registry.billingPlans.basic;}
    set billingPlan(arg: BillingPlan) { /* IGNORE */}
        
}
class Site {
    private _customer: Customer;
    get customer() { return this._customer; }
}
// After TestCode
function changeToNewPlan(newPlan: BillingPlan) {
    const site = new Site();
    const aCustomer = site.customer;
    
    let customerName = aCustomer.name;

    const plan = aCustomer.billingPlan;
    aCustomer.billingPlan = newPlan;
}
```
