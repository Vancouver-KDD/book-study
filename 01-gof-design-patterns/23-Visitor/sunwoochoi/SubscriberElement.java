/* Concrete element for subscriber */
public class SubscriberElement implements Element {
  private String name;

  public SubscriberElement(String name) {
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
