public class Client {
  public static void main(String[] args) {
    AutoHomeSystem sys = new AutoHomeSystem();
    sys.printState();

    sys.airConditioner.turnOn();
    sys.printState();
  }
}