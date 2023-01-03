/* Concrete Decorator */
public class TimerDecorator implements Decorator {
  private Component component;

  public TimerDecorator(Component component) {
    this.component = component;
  }

  @Override
  public void execute() {

    long startTime = System.currentTimeMillis();
    component.execute();
    long stopTime = System.currentTimeMillis();
    long elapsedTime = stopTime - startTime;
    System.out.println("The execution time of execute() method: " + elapsedTime);
  }
  
}
