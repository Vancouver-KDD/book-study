/**
 * Test output:
 * Rules for the password validator
 * Maximum length: 10
 * Minimum legngth: 2
 * Require upper case: true
 * Require lower case: false
 */

public class BuilderExample {
  public static void main (String[] args) {
    PasswordValidator validator = new Builder().setMax(10)
                                              .setMin(2)
                                              .setUpper(true)
                                              .build();
    validator.getRules();
  }
}