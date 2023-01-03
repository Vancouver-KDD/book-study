/**
 * The product class created by builder instance
 */
public class PasswordValidator {
  private int maxLength;
  private int minLength;
  private boolean isRequiredUpper;
  private boolean isRequiredLower;

  public PasswordValidator(int maxLength, int minLength, boolean isRequiredUpper, boolean isRequiredLower){
    this.maxLength = maxLength;
    this.minLength = minLength;
    this.isRequiredLower = isRequiredLower;
    this.isRequiredUpper = isRequiredUpper;
  }

  public void getRules() {
    System.out.println("Rules for the password validator");
    System.out.printf("Maximum length: %d\n", maxLength);
    System.out.printf("Minimum legngth: %d\n", minLength);
    System.out.printf("Require upper case: %b\n", isRequiredUpper);
    System.out.printf("Require lower case: %b\n", isRequiredLower);
  }

  public boolean validate(String password) {
    return Math.random() <= 0.5;
  }
}
