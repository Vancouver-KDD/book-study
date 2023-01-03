# [Subin] Adaptor

- 한 클래스의 인터페이스를 클라이언트에서 사용하고자 하는 다른 인터페이스로 변환. 이때 Adaptor를 이용하면 인터페이스 호환성 문제 때문에 같이 쓸 수 없는 클래스들을 연결해서 쓸 수 있다.

- 네모난 못을 동그란 구멍에 맞춰보고자 한다. 
~~~
// round/RoundHole.java
public class RoundHole {
    private double radius;

    public RoundHole(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public boolean fits(RoundPeg peg) {
        boolean result;
        result = (this.getRadius() >= peg.getRadius());
        return result;
    }
}

// round/RoundPeg.java
public class RoundPeg {
    private double radius;

    public RoundPeg() {}

    public RoundPeg(double radius) {
        this.radius = radius
    }

    public double getRadius() {
        return radius;
    }
}

// square/SquarePeg.java
public class SquarePeg {
    private double width;

    public SquarePeg(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public double getSquare() {
        double result;
        result = Math.pow(this.width, 2);
        return result;
    }
}

// ⭐️ adapters/SquarePegAdapter.java
import round.RoundPeg;
import round.SquarePeg;

public class SquarePegAdaptor extends RoundPeg {
    private SquarePeg peg;

    public SquarePegAdapter(SquarePeg peg) {
        this.peg = peg;
    }

    @Override
    public double getRadius() {
        double result;
        result = (Math.sqrt(Math.pow((peg.getWidth() / 2), 2) * 2));
        return result;
    }
}

// Demo.java
public class Demo {
    public static void main(String[] args) {
        RoundHole hole = new RoundHole(5);
        RoundPeg rpeg = new RoundPeg(5);
        if (hole.fits(rpeg)) {
            System.out.println("Fits.");
        }

        SquarePeg smallSqPeg = new SquarePeg(2);
        SquarePeg largeSqPeg = new SquarePeg(20);
        // hole.fits(smallSqPeg); // Won't compile

        // Adaptor solves the problem
        SquarePegAdapter smallSqPegAdaptor = new SquarePegAdapter(2);
        SquarePegAdapter largeSqPegAdaptor = new SquarePegAdapter(20);
        if (hole.fits(smallSqPegAdapter)) {
            System.out.println("Fits.");
        }
        if (!hole.fits(largeSqPegAdapter)) {
            System.out.println("Doens't fit.");
        }
    }
}
~~~

## Source
- https://refactoring.guru/design-patterns/adapter/java/example