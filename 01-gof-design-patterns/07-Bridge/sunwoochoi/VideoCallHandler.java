/* implementor class to declare interface of handler */
public interface VideoCallHandler {
  public void startCall(int id);
  public void endCall();
  public int getVolume();
  public void setVolume(int volume);
}
