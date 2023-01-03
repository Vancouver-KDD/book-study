# Global Data

Why do we need to refactor global data?
"The problem with global data is that it can be modified from anywhere in the code base."

The problem becomes more nasty if the global data is mutable. It can contiminate anywhere in the system.

Global data provides higer accessbility to the system, but it makes hard to debug because we don't know where the problem happens.

Type of forms Global Data
* Global variables
* Class variables (public static)
* Singleton

## Solution
### 1. Encapsulate Variable
Instead of exposing variable directly to the system, provides a point to access/modify the variable. Therefore, we can monitor the changes of variables and add validation easily.

#### Mechanics
1. create encapsulating functions to access and modify the variable.
2. replace reference to encapsulating functions
3. Restrict the visibility of the variable if possible
4. Test

#### Example
```ts
class Context {
    public static ipAddress; // expose to entire system
    // ...
}
```

```ts
class Context {
    private static ip; // restrict visibility

    // getter to get ip address
    public static get getIp() : string {
        return this.ip;
    }

    // setter to set ip address
    public static get setIp() : void {
        return this.ip;
    }
    // ...
}
```