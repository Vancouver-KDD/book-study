public class Client {
  public static void main(String[] args) {
    UserActionData actionData = new UserActionData();
    Observer logger = new Logger();
    Observer ui = new UI();

    actionData.attach(logger);
    actionData.attach(ui);

    actionData.setAction("Click Login Button");
    // Logger gets user action: Click Login Button Time: 13:45:30.607003
    // UI gets user action: Click Login Button Time: 13:45:30.607003

    actionData.detach(logger);
    actionData.setAction("Input user email");
    // UI gets user action: Input user email Time: 13:45:30.609897
  }
}
