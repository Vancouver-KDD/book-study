/* Variable expression */
public class Variable implements AbstractExpression {
  private String name;
  public Variable(String name) {
    this.name = name;
  }
  @Override
  public int evaluate() {
    return Context.getValue(name);
  }

  @Override
  public String getExpression() {
    return name;
  }
  
}
