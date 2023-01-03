/* Add operator expression */
public class AddOperator implements AbstractExpression {
  private AbstractExpression left;
  private AbstractExpression right;
  public AddOperator(AbstractExpression left, AbstractExpression right) {
    this.left = left;
    this.right = right;
  }
  @Override
  public String getExpression() {
    
    return left.getExpression() + " + " + right.getExpression();
  }

  @Override
  public int evaluate() {
    return left.evaluate() + right.evaluate();
  }
  
}
