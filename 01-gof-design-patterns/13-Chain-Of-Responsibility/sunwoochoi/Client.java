public class Client {
  public static void main(String[] args) {
    IHandler emailHandler = new EmailHandler();
    IHandler tokenHandler = new TokenHandler();
    emailHandler.setNext(tokenHandler);

    Request req1 = new Request("sunwoo.choi@gmail.com", "sdfasfhui213");
    Request req2 = new Request("sunwoo.choigmail.com", "sdfasfhui213");
    Request req3 = new Request("sunwoo.choi@gmail.com", null);

    System.out.println("Test handler with valid request");
    emailHandler.validate(req1);
    // Test handler with valid request
    // Email validation succeeded
    // Token validation succeeded
    System.out.println("Test handler with invalid email request");
    emailHandler.validate(req2);
    // Test handler with invalid email request
    // Email validation failed
    // Test handler with invalid token request
    System.out.println("Test handler with invalid token request");
    emailHandler.validate(req3);
    // Test handler with invalid token request
    // Email validation succeeded
    // Token validation failed
  }
}