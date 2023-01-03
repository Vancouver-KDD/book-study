import java.util.ArrayList;
import java.util.List;

/* Object bluetooth adapter */
public class BluetoothAdapter implements ClientInterface {
  private BluetoothFramework bluetoothHandler; // incompatible bluetooth framework object
  private List<BluetoothDevice> devices;

  public BluetoothAdapter() {
    this.bluetoothHandler = new BluetoothFramework();
  }

  @Override
  public List<String> getAllBluetoothDevice() {
    this.devices = bluetoothHandler.search();
    List<String> names = new ArrayList<String>();
    for (BluetoothDevice d: devices) {
      names.add(d.getName());
    }
    return names;
  };
  
  @Override
  public boolean connect(String name) {
    BluetoothDevice tmp = null;
    for (BluetoothDevice d: this.devices) {
      if (name == d.getName()) {
        tmp = d;
      }
    }
    if (tmp == null) {
      return false;
    }
    bluetoothHandler.connect(tmp);
    return true;
  };
  
  @Override
  public boolean disconnect() {
    return bluetoothHandler.disconnect();
  };
}
