# Repeated Switches

> Whenever you add a clause, you have to find all the switches and update them.

Don't you whiff a really stinky bad smell already?

## Replace Conditional with Polymorphism

### Example

```ts
//before
class CostCalculator {
    private baseRate: number;
    private extraDesignRate: number;
    private extraDevRate: number;
    
    constructor(baseRate: number, extraDesignRate: number, extraDevRate: number) {
        this.baseRate = baseRate;
        this.extraDesignRate = extraDesignRate;
        this.extraDevRate = extraDevRate;    
    }

    calculateCost(type: Type, cost: number): number {
        switch (type) {
            case "design":
                return cost * (baseRate + extraDesignRate);
            case "dev":
                return cost * (baseRate + extraDevRate);
            default:
                return cost * baseRate;
        }
    }
};
function foo() {
    let calculator = new CostCalculator(1.0, 0.3, 0.5);
    console.log(calculator.calculateCost(Type.Design, 1000));
}

//after
class DesignCost extends CostCalculator {
    //...
    calculateCost(cost: number): number {
        return cost * (this.baseRate + this.extraDesignRate);
    }
}
class DevCost extends CostCalculator {
    //...
    calculateCost(cost: number): number {
        return cost * (this.baseRate + this.extraDevRate);
    }
}

class CostCalculator {
    //...
    calculateCost(cost: number): number {
        return cost * this.baseRate;
    }
};

function foo() {
    let calculator = null;
    if (type == Type.Design)
        calculator= new DesignCost();
    else if (type == Type.Dev)
        calculator = new DevCost();
    else 
        calculator = new CostCalculator();

    console.log(calculator.calculateCost(1000));
}
```

As we already know, the last bit will be refactored into Factory Method 😛
