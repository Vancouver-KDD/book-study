/**
 * Client
 */
public class Client {
  public static void main(String[] args) {
    Context.assign("x", 10);
    Context.assign("y", 5);
    Context.assign("z", 3);

    AbstractExpression exp = 
    new SubtractOperator(
      new AddOperator(
        new Variable("x") , 
        new Variable("y")), 
      new Variable("z"));
    
    System.out.println(exp.getExpression()); // x + y - z
    System.out.println(exp.evaluate()); // 12
  }
  
}