/* Concrete strategy for free trial user */
public class FreeStrategy implements Strategy {

  @Override
  public void recommend() {
    System.out.println("Recommendation for free user");
  }
}
