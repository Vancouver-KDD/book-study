/* Concrete implementor which contains actual server relaying video call implementation */
public class RelayVideoCallHandler implements VideoCallHandler {
  private int id;
  private int volume = 5;

  @Override
  public void startCall(int id) {
    this.id = id;
    sendRequest();
    sendStream();
  }

  @Override
  public void endCall() {
    System.out.println("Send request to server to disconnect " + id);
  }

  private void sendRequest() {
    System.out.println("send request to server to connect " + id);
  }

  private void sendStream() {
    System.out.println("send video stream to server");
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
