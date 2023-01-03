/**
 * the seperate builder class to store all parameters from the client and create the product
 */
public class Builder {
  private int maxLength;
  private int minLength;
  private boolean isRequiredUpper;
  private boolean isRequiredLower;

  public Builder setMax(int maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  public Builder setMin(int minLength) {
    this.minLength = minLength;
    return this;
  }

  public Builder setUpper(boolean isRequiredUpper) {
    this.isRequiredUpper = isRequiredUpper;
    return this;
  }

  public Builder setLower(boolean isRequiredLower) {
    this.isRequiredLower = isRequiredLower;
    return this;
  }

  /**
   * generate PasswordValidator instance with the current parameters
   * @return PasswordValidator the password validator object
   */
  public PasswordValidator build() {
    return new PasswordValidator(maxLength, minLength, isRequiredUpper, isRequiredLower);
  }
}
