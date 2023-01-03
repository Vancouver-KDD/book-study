# Bridge design pattern example

```java
// Client

public class Client {
  public static void main(String[] args) {
    Linux linux = new Linux();
    MacOS macOS = new MacOS();
    Windows windows = new Windows();

    List<Platform> platforms = new ArrayList<>();
    platforms.add(linux);
    platforms.add(macOS);
    platforms.add(windows);

    Button button = new Button(platforms);
    Icon icon = new Icon(platforms);
    Menu menu = new Menu(platforms);

    List<GraphicUserInterface> guis = new ArrayList<>();
    guis.add(button);
    guis.add(icon);
    guis.add(menu);

    guis.forEach(GraphicUserInterface::draw);
  }
}

// GraphicUserInterface (Abstraction)
public interface GraphicUserInterface {
  void draw();
}

// Button
public class Button implements GraphicUserInterface {

  private List<Platform> platforms;

  public Button(List<Platform> platforms) {
      this.platforms = platforms;
  }

  @Override
  public void draw() {
      System.out.println("Drawing Button");
      platforms.forEach(p -> {
          p.coordinateX();
          p.coordinateY();
      });
      System.out.println();
  }
}

// Icon
public class Icon implements GraphicUserInterface {
 
  private List<Platform> platforms;

  public Icon(List<Platform> platforms) {this.platforms = platforms;
  }

  @Override
  public void draw() {
      System.out.println("Drawing Icon");
      platforms.forEach(p -> {
          p.coordinateX();
          p.coordinateY();
      });
      System.out.println();
  }
}

// Menu
public class Menu implements GraphicUserInterface {

  private List<Platform> platforms;

  public Menu(List<Platform> platforms) {
      this.platforms = platforms;
  }

  @Override
  public void draw() {
      System.out.println("Drawing Menu");
      platforms.forEach(p -> {
          p.coordinateX();
          p.coordinateY();
      });
      System.out.println();
  }
}

// Platform (Implementor)
public interface Platform {

  void coordinateX();
  void coordinateY();
}

// Linux
public class Linux implements Platform {
  @Override
  public void coordinateX() {
      System.out.println("[Linux] coordinating X");
  }
  
  @Override
  public void coordinateY() {
      System.out.println("[Linux] coordinating Y");
  }
}

// MacOS
public class MacOS implements Platform {
  @Override
  public void coordinateX() {
      System.out.println("[MacOS] coordinating X");
  }

  @Override
  public void coordinateY() {
      System.out.println("[MacOS] coordinating Y");
  }
}

// Windows
public class Windows implements Platform {
  @Override
  public void coordinateX() {
      System.out.println("[Windows] coordinating X");
  }

  @Override
  public void coordinateY() {
      System.out.println("[Windows] coordinating Y");
  }
}

```