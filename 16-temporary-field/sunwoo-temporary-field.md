# Temporary Field
Sometimes, in a class, there are fields or methods which are used in a particular circumstance. These fields make developer difficult to understand what class does and where the field is used. To avoid this issue, consider extracting them into class with relative methods. Instead of conditional code, try to eliminate by using introduce special case to create an alternative class.

## Introduce Special Case
If you found the duplicated client side codes which checks the special cases for the objects, it is a signal to consider creating an object for spcial cases.

```ts
class BluetoothDevice {
    private _deviceName: string | null ;
    private _uuid: string;
    
    public get deviceName() : string | null {
        return this._deviceName;
    }
    
    public get uuid() : string {
        return this._uuid;
    }

    public connect() {
        // connect to device
    }
   
    public sendCmd(command: string) {
        // send command
    }
}

// client 1
const ble = new BluetoothDevice();
if (ble.deviceName !== null) {
    ble.sendCmd("GetIP");
}

// client 2
const ble = new BluetoothDevice();
if (ble.deviceName !== null) {
    ble.connect();
}
```

```ts
class NewBluetoothDevice {
    protected _deviceName: string | null ;
    protected _uuid: string;
    
    public get deviceName() : string | null {
        return this._deviceName;
    }
    
    public get uuid() : string {
        return this._uuid;
    }

    public isUnkownDevice() : boolean {
        return false;
    }

    public connect() {
        // connect to device
    }
   
    public sendCmd(command: string) {
        // send command
    }
}

class UnknownBluetooDevice extends NewBluetoothDevice {    
    public get deviceName() : string | null {
        return "Unknown";
    }
    
    public get uuid() : string {
        return this._uuid;
    }

    public isUnkownDevice() : boolean {
        return true;
    }

    public connect() {
        // connect to device
    }
   
    public sendCmd(command: string) {
        // send command
    }
}

// client 1
const ble = new BluetoothDevice();
if (!ble.isUnkownDevice()) {
    ble.sendCmd("GetIP");
}

// client 2
const ble = new BluetoothDevice();
if (!ble.isUnkownDevice()) {
    ble.connect();
}

```