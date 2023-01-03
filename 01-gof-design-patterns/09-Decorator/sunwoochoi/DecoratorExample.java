/* Client */
public class DecoratorExample {
  public static void main(String[] args) {
    Component component = new ConcreteComponent();
    Decorator decorator = new TimerDecorator(component);
    /*
     * output:
     * Execute component's method
     * Finish component's method
     * The execution time of execute() method: 5004
     */
    decorator.execute();
  }
}
