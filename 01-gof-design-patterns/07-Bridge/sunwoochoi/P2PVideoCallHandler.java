/* Concrete implementor which contains actual P2P video call implementation */
public class P2PVideoCallHandler implements VideoCallHandler {
  private int id;
  private int volume = 5;

  @Override
  public void startCall(int id) {
    this.id = id;
    getPublicAddress();
    negotiateWithPeer();
    sendStream();
  }

  @Override
  public void endCall() {
    System.out.println("");
    
  }

  private void getPublicAddress() {
    System.out.println("get public IP address and Port number");
  }

  private void sendStream() {
    System.out.println("send video stream to peer " + this.id);
  }

  private void negotiateWithPeer() {
    System.out.println("negotiate with peer " + this.id);
  }

  @Override
  public int getVolume() {
    return this.volume;
  }

  @Override
  public void setVolume(int volume) {
    System.out.println("set volume from " + getVolume() + " to " + volume);
    this.volume = volume;
  }
  
}
