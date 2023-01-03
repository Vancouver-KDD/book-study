import java.util.Map;

public class MyLocFacade {
    public void printMyAddress() {
    double[] myGeoLoc = new GeoLocation().getGeoLoc();
    InternetConnection conn = new InternetConnection();
    conn.connect();
    String data = conn.getData("https://주소_API_URL", myGeoLoc);
    conn.disconnect();

    Map<String, Object> address = new Json().parse(data);
    System.out.println(address.get("address"));
    }
}
