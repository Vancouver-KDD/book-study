/* Abstraction class which has the instance of method handler and call handler's method */
public class VideoCallUI {
  protected VideoCallHandler handler;
  
  public VideoCallUI(VideoCallHandler handler) {
    this.handler = handler;
  }
  
  public void clickCallButton(Peer peer) {
    System.out.println("Call button is clicked");
    System.out.println("Calling with " + peer.getName());
    this.handler.startCall(peer.getId());
  }

  public void clickEndButton() {
    System.out.println("End button is clicked");
    this.handler.endCall();
  }
}
