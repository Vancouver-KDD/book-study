/* Concrete element for free tier user */
public class FreeElement implements Element {
  private String name;

  public FreeElement(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visitEle(this);
  }
}
