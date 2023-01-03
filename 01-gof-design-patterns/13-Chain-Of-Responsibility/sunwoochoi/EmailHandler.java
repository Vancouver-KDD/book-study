import java.util.regex.Pattern;

/* Concrete Handler implementation for email in request */
public class EmailHandler implements IHandler {
  private IHandler nextHandler;

  @Override
  public void setNext(IHandler handler) {
    this.nextHandler = handler;
  }

  @Override
  public boolean validate(Request req) {
    if (!isValidEmail(req.email)) {
      return false;
    }
    if (nextHandler != null) {
      return nextHandler.validate(req);
    }
    return true;
  }

  public boolean isValidEmail(String email) {
    String regex = "^(.+)@(.+)$";
    Pattern pattern = Pattern.compile(regex);
    if (!pattern.matcher(email).matches()) {
      System.out.println("Email validation failed");
      return false;
    }
    System.out.println("Email validation succeeded");
    return true;
  }
  
}
