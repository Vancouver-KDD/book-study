package Template;

public class NaverMapView extends MapView {
    @Override
    protected void connectMapServer() {
        System.out.println("네이버 지도 서버에 연결");
    }
    
    @Override
    protected void showMapOnScreen() {
        System.out.println("네이버 지도를 보여줌");
    }

    @Override
    protected void moveToCurrentLocation() {
        System.out.println("네이버 지도에서 현 좌표로 이동");
    }
}