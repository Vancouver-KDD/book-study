/* declare visit interface for each type of element */
public interface Visitor {
  public void visitEle(SubscriberElement ele);
  public void visitEle(FreeElement ele);
}
