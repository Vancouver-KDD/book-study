# Message Chains

The message chains code smell occurs when client object ask one object to another object.
Navigating to the object means that those objects evolved for connecting are coupled each others/

## Solution

### Hide Delegates
In stead of directly access the object, we can encapsulate the accessor as the class method. This can be done any points of chaining.
By doing this, the client side does not need to change the code when the internal field of object is changed. 

1. Create a delegating method on the interface object.
2. Replace the reference of the desired object to the method
3. Test

```ts
interface IPLotBox {
    percent75: number;
    median: number;
    percent25: number;
}

interface IStat {
    avg: number
    boxPlot: IPLotBox
}

class User1 {
    stat: IStat;
    
    // delegating method
    public get median() : number {
        return this.stat.boxPlot.median
    }
    
}


const user = new User1();
// access the median in user object
// it has to be change if one of delegating object has been changed
const median = user.stat.boxPlot.median
// access the median by delegating method
// the client side code does not have to be changed
const medianDelegate = user.median;
```