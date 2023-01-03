// 객체가 생성자 변수로 다른 객체 안에 들어감으로써 실되는 메소드의 행동을 추가할 수 있다

public class DecoratorPattern {
    public static void main(String[] args) {
        
        new XWingFighter().attack();
        // 탄환 발사

        new LaserDecorator(new XWingFighter()).attack();
        // 탄환 발사
        // 레이저 발사

        new PlasmaDecoratro(
            new MissileDecorator(
                new LaserDecorator(
                    new XWingFighter()))).attack();
        // 탄환 발사
        // 레이저 발사
        // 미사일 발사
        // 플라즈마 발사
        
    }
}
