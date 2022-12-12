# Speculative Generality

Speculative Generality? 

Methods or classes는 현재는 필요하지 않지만, 미래에 사용 될 수 있다는 점에서 생성되기도 한다. 만약 예측이 맞아 미리 짜놓은 코드가 사용될 경우는 많은 리소스를 절약할 수 있지만, 대부분의 경우에는 사용되지 않고, codebase의 complexity만 증가하게 된다.

speculative genrality code smell의 refactoring 골은 필요하지 않은 부분의 코드를 삭제하는 것이다.

## Solution

### Problem 1. Overly Hierarchical Class Structure
나중의 extensibility를 고려하려 class structure을 디자인 한 경우 혹은 abstract class가 거의 의미가 없는 경우, Collapse Hierachy refactoring 방법을 사용에 parent class를 삭제하거나 child class와 합친다. 

```java
public class BleRssiDevice extends BleDevice {
    private ScanRecord scanRecord;
    private int rssi;
    private long rssiUpdateTime;

    public BleRssiDevice(String address, String name) {
        super(address, name);
    }

    public ScanRecord getScanRecord() {
        return scanRecord;
    }

    public void setScanRecord(ScanRecord scanRecord) {
        this.scanRecord = scanRecord;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public long getRssiUpdateTime() {
        return rssiUpdateTime;
    }

    public void setRssiUpdateTime(long rssiUpdateTime) {
        this.rssiUpdateTime = rssiUpdateTime;
    }
}
```
### Problem 2. Unnecessary Delegation
불필요한 delegation method/fucntion이 있을때, inline class나 inline class refactoring 방법을 사용해 한 곳으로 로직을 옮긴다.

### Problem 3. Unused Function Parameters
만약 지금 사용하지 않는 parameter나 function의 execution에 필요하지 않는 parameter가 있다면 parameter를 삭제한다.

### Problem 4. Unused Code
만약 codebase에 사용하지 않는 code들이 있다면 삭제한다. 

### Cautions
* 만약 개발하고 있는 것이 framework이고, 코드가 framework내에서는 사용되지 않지만 client 쪽 코드에서 필요되어 진다면 삭제해서는 안된다.
* 만약 코드가 unit test에서 special case로 사용되어 진다면, refactoring하지 않는 것이 좋다.

### Mitigation
미래를 위해 짜놓은 코드를 완전히 없애버리는 것 보다는 나중을 대비해 보존해 놓고 싶다면, git의 version control를 사용해 보관해놓고 code나 문서에 commit id를 작성해놓는다. 그렇게 함으로서 codebase의 complexity는 줄이고, 나중에 필요로 하게 되면 언제는 참조해서 사용할 수 있게 된다.