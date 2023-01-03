public class FlyweightPattern {
    public static void main(String[] args) {

        String[] colors = {"Red", "Green", "Blue", "Yellow"};

        for (int i = 0; i < 10; i++) {
            Circle circle = (Circle)ShapeFactory.getCircle(colors[(int)(Math.random()*4)]);
            circle.setX((int)(Math.random()*100));
            circle.setY((int)(Math.random()*4));
            circle.setRadius((int)(Math.random()*10));
            circle.draw();
        }
        // 같은색상의 인스턴스들은 하나에서 공유된다

        // 새로운 Circle 생성Red
        // [Circle] color Red X= 59 Y= 2 Radius= 9
        // [Circle] color Red X= 80 Y= 1 Radius= 2
        // 새로운 Circle 생성Green
        // [Circle] color Green X= 42 Y= 0 Radius= 7
        // 새로운 Circle 생성Yellow
        // [Circle] color Yellow X= 63 Y= 1 Radius= 0
        // [Circle] color Yellow X= 27 Y= 1 Radius= 9
        // [Circle] color Green X= 23 Y= 1 Radius= 5
        // [Circle] color Yellow X= 24 Y= 2 Radius= 2
        // 새로운 Circle 생성Blue
        // [Circle] color Blue X= 78 Y= 3 Radius= 8
        // [Circle] color Yellow X= 66 Y= 1 Radius= 5
        // [Circle] color Blue X= 97 Y= 0 Radius= 6
    }
    
}
