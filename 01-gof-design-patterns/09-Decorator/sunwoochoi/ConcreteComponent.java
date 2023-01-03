/* Concrete Component */
class ConcreteComponent implements Component {
  public void execute() {
    System.out.println("Execute component's method");
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Finish component's method");
  }
}