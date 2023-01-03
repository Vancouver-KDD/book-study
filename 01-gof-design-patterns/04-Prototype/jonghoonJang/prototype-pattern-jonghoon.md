### Prototype Patter

- 실행 중에 생성된 객체로 다른 객체를 생성하는 패턴
- Prototpye으로 만든 객체의 상태를 변경해도 원본 Object의 변경되지 않음(Deep copy)
- 따로 class를 추가 하지 않고, 새로운 개념의 class를 따로 생성
![image](https://user-images.githubusercontent.com/77429796/190929680-2415c3fd-080d-48b0-a3bc-6846f1a06e04.png)
```Java

Public interface Prototpype {
    Object copy();
}

public interface Shape {
    String draw();
    void moveOffset(int dx, int dy);
}

public class Point implements Shape, Prototpype {
    private int x;
    private int y;

    public setX(int x) {
        this.x = x;
    }

    public setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }

    @Override
    public Object copy() {
        Point newPoint = new Point();
        newPoint.setX(x);
        newPoint.setY(y);
        return newPoint;
    }

    @Override
    public String draw() {
        return "Point: (" + x + ", " + y + ")";
    }

    @Override
    public void moveOffset(int dx, int dy) {
        x += dx;
        y += dy;
    }
}

public class Line implements Shape, Prototpype {
    private Point start;
    private Point end;

    public setStart(Point start) {
        this.start = start;
        return this;
    }

    public setEnd(Point end) {
        this.end = end;
        return this;
    }

    public Point getStart() {
        return start;
    }
    
    public Point getEnd() {
        return end;
    }

    @Override
    public Object copy() {
        Line newLine = new Line();
        newLine.setStart((Point) start.copy()); // deep copy
        newLine.setEnd((Point) end.copy());
        return newLine;
    }

    @Override
    public String draw() {
        return "Line: (" + start.draw() + ", " + start.draw() + ") -> (" + end.getX() + ", " + end.getY() + ")";
    }

    @Override
    public void moveOffset(int dx, int dy) {
        start.moveOffset(dx, dy);
        end.moveOffset(dx, dy);
    }
}

public class Group implements Shape, Prototpype {
    private ArrayList<Shape> shapeList = new ArrayList<>();

    private String name;

    public Group add(Shape name) {
        this.name = name;
    }

    Group addShape(Shape shape) {
        shapeList.add(shape);
        return this;
    }

    @Override
    public Object copy() {
        Group newGroup = new Group();
        for (Shape shape : shapes) {
            newGroup.add((Shape) shape.copy());
        }
        return newGroup;
    }

    @Override
    public String draw() {
        StringBuilder sb = new StringBuilder();
        sb.append("Group: ");
        for (Shape shape : shapes) {
            sb.append(shape.draw());
        }
        return sb.toString();
    }

    @Override
    public void moveOffset(int dx, int dy) {
        for (Shape shape : shapes) {
            shape.moveOffset(dx, dy);
        }
    }
}

public class MainEntry {
    public static void main(String[] args) {
        Point p1 = new Point().setX(1).setY(2);
        Point p2 = new Point().setX(3).setY(4);
        Line line = new Line().setStart(p1).setEnd(p2);
        Group group = new Group().addShape(p1).addShape(p2).addShape(line);
        Group group2 = (Group) group.copy();
        group2.moveOffset(1, 1);
        System.out.println(group.draw());
        System.out.println(group2.draw());
    }
}
```

resources:
https://www.youtube.com/watch?v=UPv8u9ndqAs&ab_channel=GISDEVELOPER
