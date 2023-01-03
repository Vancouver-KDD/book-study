# Long Parameter List

### 4. Remove Flag Argument (314p) ###

This is a very common API design conflict while adding lots of new features & refactoring at the same time.

PS. I had so many examples of this smell but can't find them 😭 Will bring some later.

##### Examples

```ts
function deliveryDate(anOrder : Order, isRush : boolean): Date {
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

function rushShipmentMenuItem() {
    aShipment.deliveryDate = deliveryDate(anOrder, true);
}

function regularShipmentMenuItem() {
    aShipment.deliveryDate = deliveryDate(anOrder, false);
}
```

This mess can be converted as

```ts
function rushDeliveryDate(anOrder: Order): Date {
    let deliveryTime;
    if (["MA", "CT"]     .includes(anOrder.deliveryState)) deliveryTime = 1;
    else if (["NY", "NH"].includes(anOrder.deliveryState)) deliveryTime = 2;
    else deliveryTime = 3;
    return anOrder.placedOn.plusDays(1 + deliveryTime);
}
function regularDeliveryDate(anOrder: Order): Date {
    let deliveryTime;
    if (["MA", "CT", "NY"].includes(anOrder.deliveryState)) deliveryTime = 2;
    else if (["ME", "NH"] .includes(anOrder.deliveryState)) deliveryTime = 3;
    else deliveryTime = 4;
    return anOrder.placedOn.plusDays(2 + deliveryTime);
}

function rushShipmentMenuItem() {
    aShipment.deliveryDate = rushDeliveryDate(anOrder);
}

function regularShipmentMenuItem() {
    aShipment.deliveryDate = regularDeliveryDate(anOrder);
}

// If the flag argument is determinded by external function

const isRush = determineIfRush(anOrder);
aShipment.deliveryDate = deliveryDate(anOrder, isRush);
```

This code doesn't need to change the signature as well because *isSelected* flag is set by a function call.

```c++
void DrawLabel(std::string id, std::string text, float x, float y, bool isSelected)
{
    //,,,
}
DrawLabel(newGUID, "My Name", 100, 200, entity->isSelected());
```