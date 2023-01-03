import java.util.List;

/* Client */
class Client {
  public static void main(String[] args) {
    BluetoothAdapter bluetoothManager = new BluetoothAdapter();
    
    List<String> deviceNames = bluetoothManager.getAllBluetoothDevice();
    
    System.out.println("The list of connectable bluetooth devices");
    deviceNames.forEach(d -> System.out.println(" - " + d));

    bluetoothManager.connect(deviceNames.get(0));
    bluetoothManager.disconnect();
  }
}