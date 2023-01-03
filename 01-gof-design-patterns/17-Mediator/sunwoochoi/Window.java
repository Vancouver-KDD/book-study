public class Window implements Component {
  private boolean isOpened;
  private Mediator mediator;

  public Window(Mediator mediator) {
    this.mediator = mediator;
    this.isOpened = true;
  }
  
  @Override
  public void update() {
    mediator.onChanged(this);  
  }
  
  public void open() {
    if (isOpened) return;
    isOpened = true;
    update();
  }

  public void close() {
    if (!isOpened) return;
    isOpened = false;
    update();
  }
  
  public boolean isOpened() {return isOpened;}
}