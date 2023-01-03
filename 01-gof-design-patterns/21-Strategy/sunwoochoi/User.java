/* Context - user object store strategy based on their types of user */
public class User {
  Strategy strategy;

  public void setStrategy(Strategy strategy) {
    this.strategy = strategy;
  }
  
  public void swap() {
    strategy.recommend();
  }
}
