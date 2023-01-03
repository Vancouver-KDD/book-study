import java.util.ArrayList;
import java.util.List;

/** Adaptee: incompatible bluetooth framework */
public class BluetoothFramework {
  private List<BluetoothDevice> devices;
  private boolean isConnected;
  private BluetoothDevice connectedDevice;
  
  public BluetoothFramework() {
    this.isConnected = false;
  }

  public List<BluetoothDevice> search() {
    this.devices = new ArrayList<BluetoothDevice>();
    devices.add(new BluetoothDevice("AirPod"));
    devices.add(new BluetoothDevice("Beats"));
    devices.add(new BluetoothDevice("Apple Watch"));
    devices.add(new BluetoothDevice("Google Home"));
    devices.add(new BluetoothDevice("Buzz"));
    return this.devices;
  }

  public void connect(BluetoothDevice device) {
    device.connect();
    this.connectedDevice = device;
    this.isConnected = true;
  }

  public boolean disconnect() {
    if (isConnected) {
      connectedDevice.disconnect();
      return true;
    }
    return false;
  }
}
