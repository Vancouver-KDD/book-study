package Template;

public class KakaoMapView extends MapView {
    @Override
    protected void connectMapServer() {
        System.out.println("카카오 지도 서버에 연결");
    }
    
    @Override
    protected void showMapOnScreen() {
        System.out.println("카카오 지도를 보여줌");
    }

    @Override
    protected void moveToCurrentLocation() {
        System.out.println("카카오 지도에서 현 좌표로 이동");
    }
}


