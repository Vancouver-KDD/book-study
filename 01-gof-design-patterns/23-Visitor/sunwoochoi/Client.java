public class Client {
  public static void main(String[] args) {
    Visitor watch = new WatchVideoVisitor();
    Visitor stat = new StatisticVisitor();

    Element freeEle = new FreeElement("sunwoo");
    Element subsEle = new SubscriberElement("choi");
    
    freeEle.accept(watch); // Watch the ads before watching video
    freeEle.accept(stat); // provide monthly statistics for sunwoo

    subsEle.accept(watch); // Watch the video without ads
    subsEle.accept(stat); // provide monthly, weekly, daily statistics for choi
  }
}