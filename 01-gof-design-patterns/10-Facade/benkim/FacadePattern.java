import java.util.Map;


// Facade는 장벽이라는 뜻으로, 여러 객체 생성과 로직들을 하나의 클래스에 캡슐화 하는 것이다
public class FacadePattern {
    public static void main(String[] args) {

        // BEFORE

        double[] myGeoLoc = new GeoLocation().getGeoLoc();
        InternetConnection conn = new InternetConnection();
        conn.connect();
        String data = conn.getData("https://주소_API_URL", myGeoLoc);
        conn.disconnect();

        Map<String, Object> address = new Json().parse(data);
        System.out.println(address.get("address"));

        // AFTER
        new MyLocFacade().printMyAddress();
        
    }
}