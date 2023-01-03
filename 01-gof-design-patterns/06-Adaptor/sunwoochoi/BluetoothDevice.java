/* bluetooth device object used in bluetooth framework */
public class BluetoothDevice {
  private String name;

  public BluetoothDevice(String name) {
    this.name = name;
  }

  public void connect() {
    System.out.println("Connected to the device " + this.name);
  }

  public void disconnect() {
    System.out.println("Disconnected to the device " + this.name);
  }

  public String getName() {
    return this.name;
  }
}
