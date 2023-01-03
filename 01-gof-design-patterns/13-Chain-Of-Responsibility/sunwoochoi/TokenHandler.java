/* Handler implementation for token in the request */
public class TokenHandler implements IHandler {
  private IHandler nextHandler;

  @Override
  public void setNext(IHandler handler) {
    nextHandler = handler;
  }

  @Override
  public boolean validate(Request req) {
    if (!validateToken(req.token)) {
      return false;
    }
    if (nextHandler != null) {
      return nextHandler.validate(req);
    }
    return true;
  }
  
  public boolean validateToken(String token) {
    if (token == null){
      System.out.println("Token validation failed");
      return false;
    }
    System.out.println("Token validation succeeded");
    return true;
  }
}
