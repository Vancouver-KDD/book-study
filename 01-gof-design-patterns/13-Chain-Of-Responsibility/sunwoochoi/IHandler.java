/* Validation Handler interface*/
public interface IHandler {
  public void setNext(IHandler handler);
  public boolean validate(Request req);
}
