/* Leaft - single object which has no child object */
public class ChildComponent implements Component {
  private String name;

  public ChildComponent(String name) {
    this.name = name;
  }

  @Override
  public void render() {
    System.out.println("Render single component " + name);
  }
  
}
