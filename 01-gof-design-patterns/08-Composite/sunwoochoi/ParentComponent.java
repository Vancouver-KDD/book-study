import java.util.ArrayList;
import java.util.List;

/* Composite implement interface of component and methods to handle child component 
 * it delegate render method to it's child components
*/
public class ParentComponent implements Component {
  List<Component> children;
  private String name;
  public ParentComponent(String name) {
    children = new ArrayList<Component>();  
    this.name = name;
  }

  public void addComponent(Component component) {
    children.add(component);
  }

  public void removeComponent(Component component) {
    children.remove(component);
  }

  @Override
  public void render() {
    System.out.println("Render container " + name);
    for (Component c: children) {
      c.render();
    }
  }
  
}
