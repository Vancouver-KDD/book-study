import java.util.List;

/* declare client interface for bluetooth */
public interface ClientInterface {
  public List<String> getAllBluetoothDevice();
  public boolean connect(String uuid);
  public boolean disconnect();
}
