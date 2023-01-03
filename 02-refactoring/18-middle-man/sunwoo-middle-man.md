# Middle Man
Encapsulation is the prime feature of OOP. We can hide the internal data from the outside of object.
It often comes with delegation between objects. However, over using delegation makes code base large and most of them just delegation the request from the client to the target object.
To avoid this, we consider deleting those unnecessary delegating methods.

## Remove Middle Man
Remove Middle Man is inverse of Hide Delegate.
If you find some classes exist only for the delegating the request, we can directly request to the target object

1. Create getter method for the target object
2. chang the client code by referencing the getter of target object
3. Test

```ts
class Ip {
    _ip: string
    
    public get ip() : string {
        return this._ip;
    }
    
}

class DeviceInfo {
    ip: Ip
}

class OS {
    deviceInfo: DeviceInfo

    
    public get ip() : string {
        return this.deviceInfo.ip._ip
    }
    
}

// it delegate from os object -> deviceInfo -> Ip -> _ip to get device ip address
const os = new OS();
const ip = os.ip

// it directly request to ip object to get ip address
const ipObj = new Ip();
const ipDirect = ipObj.ip
```