# Visitor pattern example

```java
/*
  Reference: https://www.youtube.com/watch?v=vG9gEI1dr1o
*/
// Visitor interface
public interface IVisitor {
    void visit(Circle circle);
    void visit(Square square);
    void visit(Triangle triangle);
}

// Concrete Visitor 1
public class AreaCalculator implements IVisitor {

    public double areaTotal;

    @Override
    public void visit(Circle circle) {
        areaTotal += Math.pow(circle.getRadius(), 2.0) * Math.PI;
        System.out.println("Adding circle's area to areaTotal");
    }

    @Override
    public void visit(Square square) {
        areaTotal += Math.pow(square.getSide(), 2.0);
        System.out.println("Adding square's area to areaTotal");
    }

    @Override
    public void visit(Triangle triangle) {
        areaTotal += triangle.getBase() * triangle.getHeight() * 0.5;
        System.out.println("Adding triangle's area to areaTotal");
    }

    public double getAreaTotal() {
        return areaTotal;
    }
}

// Concrete Visitor 2
public class HeightCalculator implements IVisitor {

    private double heightTotal;

    @Override
    public void visit(Circle circle) {
        heightTotal += circle.getRadius();
        System.out.println("Adding circle's radius to the heightTotal");
    }

    @Override
    public void visit(Square square) {
        heightTotal += square.getSide();
        System.out.println("Adding square's height to the heightTotal");

    }

    @Override
    public void visit(Triangle triangle) {
        heightTotal += triangle.getHeight();
        System.out.println("Adding triangle's height to the heightTotal");
    }

    public double getHeightTotal() {
        return heightTotal;
    }
}

// Element interface
public interface IShape {
    void accept(IVisitor visitor);
}

// Concrete Element 1
public class Circle implements IShape {

    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    public double getRadius() {
        return radius;
    }
}

// Concrete Element 2
public class Square implements IShape {

    private double side;

    public Square(double side) {
        this.side = side;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    public double getSide() {
        return side;
    }
}

// Concrete Element 3
public class Triangle implements IShape {

    private double base;
    private double height;

    public Triangle(double base, double height) {
        this.base   = base;
        this.height = height;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    public double getBase() {
        return base;
    }

    public double getHeight() {
        return height;
    }
}

// Client
public class Client {
    public static void main(String[] args) {
        IVisitor heightCalculator = new HeightCalculator();
        IVisitor areaCalculator = new AreaCalculator();

        List<IShape> shapes = new ArrayList<>();
        shapes.add(new Circle(3.0));
        shapes.add(new Square(4.5));
        shapes.add(new Triangle(2.0, 1.5));

        shapes.forEach(s -> {
            s.accept(heightCalculator);
            s.accept(areaCalculator);
        });

    }
}

```