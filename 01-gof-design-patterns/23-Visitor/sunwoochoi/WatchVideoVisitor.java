/* Concrete visitor to operate playing video */
public class WatchVideoVisitor implements Visitor {

  @Override
  public void visitEle(SubscriberElement ele) {
    System.out.println("Watch the video without ads");
  }

  @Override
  public void visitEle(FreeElement ele) {
    System.out.println("Watch the ads before watching video");
  }

  /* one interface for handling all type of elements */
  public void visitEle(Element ele) {
    if (ele instanceof FreeElement) {
      System.out.println("Watch the ads before watching video");
      return;
    }
    if (ele instanceof SubscriberElement) {
      System.out.println("Watch the video without ads");
      return;
    }
  }
  
}
