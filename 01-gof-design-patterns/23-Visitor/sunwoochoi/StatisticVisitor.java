/* concrete visitor to operate seeing statistics */
public class StatisticVisitor implements Visitor {

  @Override
  public void visitEle(SubscriberElement ele) {
    System.out.println("provide monthly, weekly, daily statistics for " + ele.getName());
  }

  @Override
  public void visitEle(FreeElement ele) {
    System.out.println("provide monthly statistics for " + ele.getName());
  }
}
